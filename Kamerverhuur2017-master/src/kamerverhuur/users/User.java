package kamerverhuur.users;

/**
 * Created by Aram_ on 6-9-2017.
 */
public class User {

    private String userName;
    private String password;

    /**
     * Super klasse voor Verhuurder en Huurder
     * @param userName
     * @param password
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //Getter om de gebruikersnaam op te halen
    public String getUserName() {
        return userName;
    }

    //Getter om het wachtwoord op te halen
    public String getPassword() {
        return password;
    }

}
