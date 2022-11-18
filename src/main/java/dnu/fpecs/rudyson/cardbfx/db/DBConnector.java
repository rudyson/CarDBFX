package dnu.fpecs.rudyson.cardbfx.db;

import dnu.fpecs.rudyson.cardbfx.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnector {
    private final Logger logger = Logger.getLogger(DBConnector.class.getName());
    private Connection connection;

    public boolean init() {
        try {
            String fileName = "application.properties";

            Properties properties = new Properties();

            logger.log(Level.INFO, "Runtime dir " + System.getProperty("user.dir"));

            if (new File(fileName).exists()) {
                logger.log(Level.INFO, "Using properties file from disk");
                properties.load(new FileInputStream(fileName));
            } else {
                logger.log(Level.INFO, "Using properties resources");
                InputStream inputStream = DBConnector.class.getResourceAsStream("/dnu/fpecs/rudyson/cardbfx/properties/" + fileName);
                properties.load(inputStream);
                if (inputStream != null)
                    inputStream.close();
            }
            Class.forName(properties.getProperty("java.sql.Driver.class.name"));
            connection = DriverManager.getConnection(
                    properties.getProperty("java.sql.Connection.url"),
                    properties.getProperty("java.sql.Connection.username"),
                    properties.getProperty("java.sql.Connection.password")
            );
            logger.log(Level.INFO, String.format("Connected to database %s as %s", connection.getMetaData().getURL(), connection.getMetaData().getUserName()));
            return true;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "SQLException:\n" + exception);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "IOException:\n" + exception);
        } catch (NullPointerException exception) {
            logger.log(Level.SEVERE, "Wrong database configuration file path:\n" + exception);
        } catch (ClassNotFoundException exception) {
            logger.log(Level.SEVERE, "ClassNotFoundException:\n" + exception);
        }
        return false;
    }

    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public ResultSet getCars() {
        try {
            return connection.createStatement().executeQuery("SELECT * FROM cars");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Transmission> getTransmissions() {
        ArrayList<Transmission> transmissions = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM transmissions");
            while (resultSet.next()) {
                transmissions.add(new Transmission(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                ));
            }
            return transmissions;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean addCar(Car car) {
        try {
            Statement statement = connection.createStatement();
            int n = statement.executeUpdate(
                    String.format("INSERT INTO cars (name, vendor, type) VALUES ('%s','%s','%d')",
                            car.getName(),
                            car.getVendor(),
                            car.getType().getId())
            );
            statement.close();
            return (n > 0);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return false;
    }

    public boolean editCar(Car car) {
        try {
            Statement statement = connection.createStatement();
            int n = statement.executeUpdate(
                    String.format("UPDATE cars SET name='%s',vendor='%s',type=%d WHERE id=%d",
                            car.getName(),
                            car.getVendor(),
                            car.getType().getId(),
                            car.getId())
            );
            statement.close();
            return (n > 0);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return false;
    }

    public boolean deleteCar(int id) {
        try {
            Statement statement = connection.createStatement();
            int n = statement.executeUpdate(
                    String.format("DELETE FROM cars WHERE id=%d", id)
            );
            statement.close();
            return (n > 0);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }
        return false;
    }
}