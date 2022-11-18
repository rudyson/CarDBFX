package dnu.fpecs.rudyson.cardbfx;

import dnu.fpecs.rudyson.cardbfx.db.DBConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CarDBFX_Application extends Application {
    private static final Logger logger = Logger.getLogger(CarDBFX_Application.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scenes/main.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/main.css")).toExternalForm());
        stage.setTitle("Car DB FX");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/car.png"))));
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();
        logger.log(Level.INFO, "Scene main loaded");
    }

    public static void main(String[] args) {
        LogManager logManager = LogManager.getLogManager();
        String fileName = "application.properties";
        InputStream inputStreamCfg;
        try {
            inputStreamCfg = (new File(fileName).exists()) ?
                    new FileInputStream(fileName) :
                    DBConnector.class.getResourceAsStream("/dnu/fpecs/rudyson/cardbfx/properties/" + fileName);
            logManager.readConfiguration(inputStreamCfg);
            if (inputStreamCfg != null)
                inputStreamCfg.close();
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "IOException:\n", exception);
        }
        CarDBFX_Application.launch(args);
    }
}