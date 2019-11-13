package model;

public class PacketPart {
    private int id;
    private int huidigNr;
    private int totaal;
    private byte[] part;

    public PacketPart(int id, int huidigNr, int totaal, byte[] part) {
        this.id = id;
        this.huidigNr = huidigNr;
        this.totaal = totaal;
        this.part = part;
    }
}
