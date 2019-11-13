package kamerverhuur;

import kamerverhuur.users.HomeOwner;
import kamerverhuur.users.User;
import kamerverhuur.exception.*;
import java.util.ArrayList;

/**
 * This class acts as a stand-in for a database, because database connections are outside of the scope of this exercise
 */
public class Storage {

    ArrayList<User> users = new ArrayList<User>();

    private static Storage instance;

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     *
     * Add a user to the list of kamerverhuur.users
     * @param user the User object to be added
     * @throws UserException when the user is already in the list
     */
    public void addUser (User user) throws UserException {
        for(User u : users){
            if (u.getUserName().equals(user.getUserName())){
                throw new UserException("User already exists");
            }
        }
        users.add(user);
    }

    /**
     * Search for a User object with a certain userName, due to the process of adding, each username can exist only once
     * @param userName the username to search
     * @return the User object with the searched-for username, or null if there is no such user
     */
    public User getUserByName(String userName){
        for (User user : users) {
            if (user.getUserName().equals(userName)){
                return user;
            }
        }

        return null;
    }

    public boolean isValidLogin(String username, String password){
        if (getUserByName(username) != null && getUserByName(username).getPassword().equals(password)){
            return true;
        }

        return false;
    }

    /**
     * Add a room
     * @param room the room to be added
     */
    public void addRoom(Room room, HomeOwner owner){
        owner.addRoom(room);
    }
}
