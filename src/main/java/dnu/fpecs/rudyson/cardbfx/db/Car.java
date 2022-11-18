package dnu.fpecs.rudyson.cardbfx.db;

public class Car {

    private int id;
    private String name;
    private String vendor;
    private Transmission type;

    public Car(int id, String name, String vendor, Transmission type) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Transmission getType() {
        return type;
    }

    public void setType(Transmission type) {
        this.type = type;
    }
}