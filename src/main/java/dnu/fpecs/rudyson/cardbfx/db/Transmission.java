package dnu.fpecs.rudyson.cardbfx.db;

public class Transmission {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    private final int id;
    private final String name;

    public Transmission(int id, String name) {
        this.id = id;
        this.name = name;
    }
}