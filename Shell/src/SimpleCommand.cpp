#include <iostream>
#include <unistd.h>
#include <dirent.h>
#include <fcntl.h>
#include <wait.h>
#include <fstream>
#include "SimpleCommand.h"
using namespace std;

void SimpleCommand::execute() {

    // If exit command is fired
    if (command.compare("exit") == 0) {
        // Call the exit system call
        exit(0);
        // If change directory command is fired
    } else if (command.compare("cd") == 0) {
        int pid = fork();
        if (pid == 0) {
            // If the arguments list is empty or the first argument is ~
            if (arguments.empty() || arguments[0].compare("~") == 0) {
                // Fire the change directory system call
                chdir(getenv("HOME"));
            } else {
                // If the change directory command returns -1 it doesn't exist, print an error
                const char *directory = arguments[0].c_str();
                if (chdir(directory) == -1) {
                    perror("Directory does not exist!");
                }
            }
            char cwd[1024];
            // Print the working directory
            printf(getcwd(cwd, sizeof(cwd)));
        } else if (pid > 0) {
            // Wait for the process to end
            waitpid(pid, nullptr, 0);
        } else {
            // Print error when there is an error
            perror("Error in forking (ls command)!");
        }

        // If the list files and directories command is fired
    } else if (command.compare("ls") == 0) {
        char cwd[1024];
        // Get the current directory
        getcwd(cwd, sizeof(cwd));
        DIR *dir;
        struct dirent *ent;
        // Fork the command
        int pid = fork();
        if (pid == 0) {
            // If the directory is not null
            if ((dir = opendir(cwd)) != nullptr) {
                // If there are no arguments, so the command will be the ls command
                if (arguments.empty()) {
                    // Loop through all the entries in the directory and print the name
                    while ((ent = readdir(dir)) != nullptr) {
                        // Print the name of the entity
                        printf("%s\n", ent->d_name);
                    }
                    // Pass the argument with the ls command and run it (e.g. ls -la)
                } else {
                    char *argv[] = {"ls", const_cast<char *>(arguments[0].c_str()), nullptr};
                    execvp("/bin/ls", argv);
                }
                // Close the directory
                closedir(dir);
            }
        } else if (pid > 0) {
            int status;
            // Wait for the command to end
            waitpid(pid, &status, 0);
        } else {
            // Print an error when something is wrong with the fork
            perror("Error in forking (ls command)!");
        }
        // If the print working directory command is fired
    } else if (command.compare("pwd") == 0) {
        char cwd[1024];
        // Get working directory
        getcwd(cwd, sizeof(cwd));
        // Print the working directory
        printf("Current working directory: %s\n", cwd);
    } else {
        // Get the program
        char *prog = (char *) command.c_str();

        ifstream ifile(prog);
        if (ifile){
            // Fork the program
            int pid = fork();
            if (pid == 0) {
                // For every redirect
                for (const auto &redirect : redirects) {

                    // Get the redirect
                    const char *newFile = redirect.getNewFile().c_str();

                    switch (redirect.getType()) {
                        // >
                        case 0: {
                            // Build file descriptor
                            int fd = open(newFile, O_RDWR | O_TRUNC | O_CREAT, S_IRUSR | S_IWUSR);
                            // If > (normal output)
                            if (redirect.getOldFileDescriptor() == 1) {
                                // Duplicate stdout file descriptor and close stdout
                                dup2(fd, STDOUT_FILENO);
                            // else 2>
                            } else {
                                // If 2>&1
                                if (redirect.getNewFile() == "&1"){
                                    // Redirect the errors to the Output
                                    dup2(STDOUT_FILENO, STDERR_FILENO);
                                } else {
                                    // Duplicate stderr file descriptor and close stderr
                                    dup2(fd, STDERR_FILENO);
                                }

                            }
                            break;
                        }
                        // >>
                        case 1: {
                            // Build file descriptor
                            int fd = open(newFile, O_RDWR | O_APPEND, S_IRUSR | S_IWUSR);
                            // If normal append
                            if (redirect.getOldFileDescriptor() == 1) {
                                // Duplicate stdout file descriptor and close stdout
                                dup2(fd, STDOUT_FILENO);
                              // Else error append
                            } else {
                                // Duplicate stderr file descriptor and close stderr
                                dup2(fd, STDERR_FILENO);
                            }
                            break;
                        }
                        // <
                        case 2: {
                            // Build file descriptor
                            int fd = open(newFile, O_RDONLY, 0);
                            // Duplicate stdin file descriptor and close stdin
                            dup2(fd, STDIN_FILENO);
                            break;
                        }
                        default: {
                            // Print error if there is one in ioRedirection
                            perror("Error on redirecting");
                        }
                    }
                }
                // Execute the program
                execvp(prog, nullptr);
            } else if (pid > 0) {
                int status;
                // Wait for the program to end
                waitpid(pid, &status, 0);
                // Print status code
                printf("Program ended with status code: %s\n", reinterpret_cast<char *>(status));
            } else {
                // Print an error when something is wrong with the fork
                perror("Error in forking (program)!");
            }
        } else {
            perror("This program doesn't exist!");
        }

    }
}
