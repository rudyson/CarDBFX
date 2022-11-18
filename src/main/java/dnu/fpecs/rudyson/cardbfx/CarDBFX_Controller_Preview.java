package dnu.fpecs.rudyson.cardbfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarDBFX_Controller_Preview implements Initializable {
    private final Logger logger = Logger.getLogger(CarDBFX_Controller_Preview.class.getName());
    @FXML
    public Button btnGoBack;
    @FXML
    private ImageView previewImage;
    @FXML
    private Hyperlink viewCarOnWiki;

    @FXML
    protected void goBack(ActionEvent event) {
        try {
            logger.log(Level.CONFIG, "Switching scene to \"Main\"");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scenes/main.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Car DB FX");
            stage.show();
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    protected void setPreviewImage(Image image) {
        previewImage.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        apfitimg.widthProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                previewImage.setFitWidth(apfitimg.getWidth());
            }
        });
        apfitimg.heightProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                previewImage.setFitHeight(apfitimg.getHeight());
            }
        });

         */
        /*
        apfitimg.widthProperty().addListener(e->{
            System.out.printf("%.0f_%.0f%n",apfitimg.getWidth(),apfitimg.getHeight());
            previewImage.setFitWidth(apfitimg.getWidth());
        });
        apfitimg.heightProperty().addListener(e->{
            System.out.printf("%.0f_%.0f%n",apfitimg.getWidth(),apfitimg.getHeight());
            previewImage.setFitHeight(apfitimg.getHeight());
        });

         */
    }

    public void setViewCarOnWiki(String title, String url, String article) {
        viewCarOnWiki.setText(title);
        viewCarOnWiki.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Tooltip.install(previewImage, new Tooltip(article));
        previewImage.setAccessibleText(article);
    }
}
