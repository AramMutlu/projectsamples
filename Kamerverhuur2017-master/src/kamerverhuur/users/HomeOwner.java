package kamerverhuur.users;

import kamerverhuur.Room;

import java.util.ArrayList;

public class HomeOwner extends User {

    ArrayList<Room> rooms;

    /**
     * Verhuurder constructor
     *
     * @param userName gebruikersnaam voor de Verhuurder
     * @param password wachtwoord voor de Verhuurder
     */
    public HomeOwner(String userName, String password) {
        super(userName, password);
        rooms = new ArrayList<Room>();
    }

    //Kamer toevoegen aan de lijst van de verhuurer
    public void addRoom(Room room) {
        rooms.add(room);
    }

    //Getter om alle kamers van de verhuurder op te halen
    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
