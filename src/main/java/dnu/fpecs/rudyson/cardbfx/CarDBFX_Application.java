package dnu.fpecs.rudyson.cardbfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarDBFX_Application extends Application {
    private static final Logger logger = Logger.getLogger(CarDBFX_Application.class.getName());
    /*
    private static boolean getProperties(){
        try {
            logger.info(System.getProperty("user.dir"));
            Properties properties = new Properties();
            String path = System.getProperty("dnu.fpecs.rudyson.cardbfx.config");
            if (path==null)
                return false;
            FileInputStream fileInputStream = new FileInputStream(path);
            properties.load(fileInputStream);
            System.setProperty(
                    "java.util.logging.config.file",
                    properties.getProperty("dnu.fpecs.rudyson.cardbfx.logging.properties")
            );
            System.setProperty(
                    "dnu.fpecs.rudyson.cardbfx.database.properties",
                    properties.getProperty("dnu.fpecs.rudyson.cardbfx.database.properties")
            );
            fileInputStream.close();
            return true;
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (NullPointerException exception){
            System.err.println((System.getProperty("dnu.fpecs.rudyson.cardbfx.config"))==null?"Yes":"No");
        }
        return false;
    }

     */

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scenes/main.fxml")));
        // Scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/main.css")).toExternalForm());
        // Stage
        stage.setTitle("Car DB FX");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/car.png"))));
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();
        logger.log(Level.INFO,"Scene main loaded");
    }

    public static void main(String[] args) {
        /*
        String fileName = "application.properties";
        String filePath = "src/main/resources/dnu/fpecs/rudyson/cardbfx/properties/";
        String propPath = (new File(fileName).exists())?fileName:filePath.concat(fileName);

        System.setProperty("java.util.logging.config.file", propPath);


        logger.config("Launched at "+System.getProperty("user.dir"));

         */
        CarDBFX_Application.launch(args);
    }
}