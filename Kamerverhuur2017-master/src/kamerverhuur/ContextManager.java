package kamerverhuur;

import kamerverhuur.exception.UserException;
import kamerverhuur.users.HomeOwner;
import kamerverhuur.users.Tenant;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextManager implements ServletContextListener {

    /**
     * Hier wordt er dummy data aan het systeem toegevoegd
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Storage storage = Storage.getInstance();

        try {
            storage.addUser(new Tenant("Huurder", "p@ssw0rd"));
            storage.addUser(new Tenant("Pietje", "Ejteip"));
            storage.addUser(new Tenant("Jantje", "Ejtnaj"));
            storage.addUser(new HomeOwner("Owner", "Renwo"));
            storage.addUser(new HomeOwner("Klaasje", "Ejsaalk"));
            storage.addUser(new HomeOwner("Verhuurder", "p@ssw0rd"));

            storage.addRoom(new Room("Mooie woning in hartje Enschede!", "Van Galenstraat 1", 30, 475, "Enschede"), (HomeOwner) storage.getUserByName("Owner"));
            storage.addRoom(new Room("Het krakkemikkige schuurtje van je opa", "Zeven Bosjes 1", 5, 30, "Almelo"), (HomeOwner) storage.getUserByName("Owner"));
            storage.addRoom(new Room("Unieke bunker uit de oorlogstijd", "Strand 3", 60, 943, "Zandvoort"), (HomeOwner) storage.getUserByName("Klaasje"));
            storage.addRoom(new Room("Wij weten ook niet waarom je hier zou gaan wonen", "Middle of nowhere", 10, 9000, "Hengelo"), (HomeOwner) storage.getUserByName("Verhuurder"));
            storage.addRoom(new Room("Het Witte Huis", "1600 Pennsylvania Ave NW", 5110, 1800000, "Washington"), (HomeOwner) storage.getUserByName("Klaasje"));
            storage.addRoom(new Room("Doos onder de brug", "Wierdensebrug", 1, 0, "Almelo"), (HomeOwner) storage.getUserByName("Verhuurder"));
        } catch (UserException ue) {
            System.out.println(ue.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //Nothing to do here
    }
}
