#include <iostream>
#include <unistd.h>
#include <wait.h>
#include <cstring>
#include "Pipeline.h"
#include "SimpleCommand.h"

/**
 * Destructor.
 */
Pipeline::~Pipeline() {
    for (SimpleCommand *cmd : commands)
        delete cmd;
}

/**
 * Executes the commands on this pipeline.
 */
void Pipeline::execute() {

    // If there is only one command
    if (commands.size() == 1) {
        // Return and execute the command
        return commands.front()->execute();
    }

    int fileNo = 0;

    // For every command
    for (SimpleCommand *command : commands) {

        // Create pipelines
        int fd[2];

        // If there is an error on creating the pipes
        if (pipe(fd) == -1) {
            perror("Error at creating pipe!");
        }

        // Create a fork
        int pid = fork();
        switch (pid) {
            // If there is an error at creating pid
            case -1: {
                perror("Error at creating fork!");
                exit(EXIT_FAILURE);
            }
                // If pid is a child
            case 0: {
                /*
                 * 0 = STDIN_FILENO
                 * 1 = STDOUT_FILENO
                 * 2 = STDERR_FILENO
                 */

                // Close the read of the fd
                close(fd[STDIN_FILENO]);

                // To check if its the first command
                if(fileNo != STDIN_FILENO){
                    dup2(fileNo, STDIN_FILENO);
                }

                // To check if its the last command
                if (command == commands.back()){
                    close(fd[STDIN_FILENO]);
                    close(fd[STDOUT_FILENO]);
                } else {
                    dup2(fd[STDOUT_FILENO], STDOUT_FILENO);
                }

                // Execute the program
                command->execute();
                exit(EXIT_SUCCESS);

            }
                // If pid is a parent
            default: {

                // Close the write of the pipepline
                close(fd[STDOUT_FILENO]);

                // Open the read of the fd
                fileNo = fd[STDIN_FILENO];

                // To check if its the last command
                if (&command == &commands.back()){
                    // Close the read of the fd
                    close(fd[STDIN_FILENO]);
                    // Wait for the childprocess to end
                    waitpid(pid, nullptr, 0);
                }

            }
        }
    }
}
