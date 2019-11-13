package kamerverhuur.users;

public class Tenant extends User {
    /**
     * Constuctor om een Huurder aan te maken
     * @param userName Gebruikersnaam voor de Huurder
     * @param password wachtwoord voor de Huurder
     */
    public Tenant(String userName, String password) {
        super(userName, password);
    }
}
