package kamerverhuur;

public class Room {

    String title;
    String address;
    int squareMeters;
    int rent;
    String city;

    /**
     * Constructor om een kamer aan te maken
     * @param title Titel van de kamer
     * @param address adres van de kamer
     * @param squareMeters Oppervlakte van de kamer
     * @param rent Huurprijs van de kamer
     * @param city locatie van de kamer
     */
    public Room(String title, String address, int squareMeters, int rent, String city) {
        this.title = title;
        this.address = address;
        this.squareMeters = squareMeters;
        this.rent = rent;
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public int getRent() {
        return rent;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "<h2>" + title + "</h2>" +
                "<h3>" + address + "</h3>" +
                "<h3>" + city + "</h3>" +
                "<h3>" + squareMeters + "m² </h3>" +
                "<h3>" + rent + " euro </h3>";
    }
}
