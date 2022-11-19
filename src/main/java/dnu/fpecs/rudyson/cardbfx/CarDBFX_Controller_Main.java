package dnu.fpecs.rudyson.cardbfx;

import dnu.fpecs.rudyson.cardbfx.db.Car;
import dnu.fpecs.rudyson.cardbfx.db.DBConnector;
import dnu.fpecs.rudyson.cardbfx.db.Transmission;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarDBFX_Controller_Main implements Initializable {
    private final Logger logger = Logger.getLogger(CarDBFX_Controller_Main.class.getName());
    private boolean carSelected = false;
    @FXML
    private TextFlow textFlowAbout;
    @FXML
    private TextField tf_Name;
    @FXML
    private TextField tf_Vendor;
    @FXML
    private ComboBox<Transmission> cb_Type;
    @FXML
    private Button btn_Add;
    @FXML
    private Button btn_Edit;
    @FXML
    private Button btn_Delete;
    @FXML

    private Button btn_Deselect;
    @FXML
    private Button btn_Preview;
    @FXML
    private TableView<Car> table;
    @FXML
    private TableColumn<Car, Integer> tc_id;
    @FXML
    private TableColumn<Car, String> tc_name;
    @FXML
    private TableColumn<Car, String> tc_vendor;
    @FXML
    private TableColumn<Car, Transmission> tc_type;

    private DBConnector dbConnector;
    private final HashMap<Integer, Transmission> transmissionMap = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_vendor.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        tc_type.setCellValueFactory(new PropertyValueFactory<>("type"));

        dbConnector = new DBConnector();
        boolean conState = dbConnector.init();
        ArrayList<Transmission> transmissions;
        if (conState){
            transmissions = dbConnector.getTransmissions();
            for (Transmission transmission : transmissions) {
                transmissionMap.put(transmission.getId(), transmission);
            }

            cb_Type.setItems(FXCollections.observableArrayList(transmissions));
            cb_Type.getSelectionModel().selectFirst();
            this.loadTable();

            table.setOnMouseClicked(e -> updateSelection());
            table.setOnKeyPressed(e -> updateSelection());
            table.setOnTouchPressed(e -> updateSelection());
            clearSelection();
        }
        Text infoAbout_Copyright = new Text("© 2022 Ruslan Diadiushkin\n");
        Hyperlink infoAbout_Github = new Hyperlink("https://github.com/rudyson");
        infoAbout_Github.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(infoAbout_Github.getText()));
            } catch (IOException exception) {
                logger.log(Level.SEVERE,"IOException:", exception);
            }
        });

        textFlowAbout.getChildren().addAll(
                infoAbout_Copyright,
                infoAbout_Github
        );
    }

    @FXML
    protected void carRecordAdd() {
        if (!Objects.equals(tf_Name.getText(), "") && !Objects.equals(tf_Vendor.getText(), "")) {
            if (dbConnector.addCar(new Car(
                    -1,
                    tf_Name.getText(),
                    tf_Vendor.getText(),
                    cb_Type.getValue()
            ))) {
                logger.log(Level.INFO, "Record added.");
                this.clearSelection();
                this.loadTable();
            } else {
                logger.log(Level.SEVERE, "Record adding error. MySQL query not executed.");
                new Alert(Alert.AlertType.ERROR, "MySQL Error").show();
            }
        } else {
            logger.log(Level.WARNING, "Record adding refused. The fields must be filled out.");
            new Alert(Alert.AlertType.WARNING, "The fields must be filled out.").show();
        }
    }

    @FXML
    protected void carRecordEdit() {
        if (!Objects.equals(tf_Name.getText(), "") && !Objects.equals(tf_Vendor.getText(), "")) {
            if (dbConnector.editCar(new Car(
                    table.getSelectionModel().getSelectedItem().getId(),
                    tf_Name.getText(),
                    tf_Vendor.getText(),
                    cb_Type.getValue()
            ))) {
                logger.log(Level.INFO, "Record edited.");
                this.clearSelection();
                this.loadTable();
            } else {
                logger.log(Level.SEVERE, "Record editing error. MySQL query not executed.");
                new Alert(Alert.AlertType.ERROR, "MySQL Error.").show();
            }
        } else {
            logger.log(Level.WARNING, "Record editing refused. The fields must be filled out");
            new Alert(Alert.AlertType.WARNING, "The fields must be filled out.").show();
        }
    }

    @FXML
    protected void carRecordDelete() {
        if (dbConnector.deleteCar(table.getSelectionModel().getSelectedItem().getId())) {
            logger.log(Level.INFO, "Record deleted.");
        } else {
            logger.log(Level.SEVERE, "Record deleting error. MySQL query not executed.");
            new Alert(Alert.AlertType.ERROR, "MySQL Error").show();
        }

        this.clearSelection();
        this.loadTable();
    }

    @FXML
    protected void carRecordDeselect() {
        clearSelection();
    }

    private void buttonEnabledState() {
        btn_Add.setDisable(carSelected);
        btn_Delete.setDisable(!carSelected);
        btn_Deselect.setDisable(!carSelected);
        btn_Edit.setDisable(!carSelected);
        btn_Preview.setDisable(!carSelected);
    }

    private void clearSelection() {
        tf_Name.setText("");
        tf_Vendor.setText("");
        cb_Type.getSelectionModel().selectFirst();
        table.getSelectionModel().clearSelection();

        carSelected = false;
        buttonEnabledState();
    }

    private void updateSelection() {
        if (table.getSelectionModel().getSelectedIndex() != -1) {
            Car car = table.getSelectionModel().getSelectedItem();
            tf_Name.setText(car.getName());
            tf_Vendor.setText(car.getVendor());
            cb_Type.setValue(car.getType());

            carSelected = true;
            buttonEnabledState();
        }
    }

    private void loadTable() {
        table.getItems().clear();
        try {
            ResultSet resultSet = dbConnector.getCars();
            while (resultSet.next())
                table.getItems().add(new Car(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        transmissionMap.get(resultSet.getInt(4))
                ));
            resultSet.close();
            logger.log(Level.INFO, "Table's content loaded");
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "SQLException:\n",exception);
        }
    }

    @FXML
    protected void previewScene(ActionEvent event) {
        try {
            logger.log(Level.CONFIG, "Switching scene to \"Preview\"");
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("scenes/preview.fxml")));

            Car car = table.getSelectionModel().getSelectedItem();

            String previewTitle = String.format("%s %s, %s", car.getVendor(), car.getName(), car.getType().getName());
            String carName = car.getVendor() + " " + car.getName();

            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle(carName + " - Preview");
            stage.show();

            URL searchWiki1 = new URL(
                    String.format(
                            "https://en.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=%s&srlimit=1",
                            URLEncoder.encode(carName, StandardCharsets.UTF_8)
                    )
            );
            String stringJSON = new Scanner(searchWiki1.openStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
            logger.log(Level.CONFIG, "Loaded " + searchWiki1);
            logger.log(Level.CONFIG, stringJSON);
            JSONObject json = new JSONObject(stringJSON);
            String articleTitle = json.getJSONObject("query").getJSONArray("search").getJSONObject(0).getString("title");
            String articleURL = String.format(
                    "https://en.wikipedia.org/w/index.php?curid=%d/",
                    json.getJSONObject("query").getJSONArray("search").getJSONObject(0).getInt("pageid")
            );


            URL searchWiki2 = new URL(
                    String.format(
                            "https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&prop=pageimages&piprop=original&titles=%s",
                            URLEncoder.encode(articleTitle, StandardCharsets.UTF_8)
                    )
            );
            stringJSON = new Scanner(searchWiki2.openStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
            logger.log(Level.CONFIG, "Loaded " + searchWiki2);
            logger.log(Level.CONFIG, stringJSON);
            json = new JSONObject(stringJSON);
            String imageURL = json.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getJSONObject("original").getString("source");
            logger.log(Level.CONFIG, "Loaded " + imageURL);

            CarDBFX_Controller_Preview carDBFX_controller_preview = fxmlLoader.getController();
            carDBFX_controller_preview.setPreviewImage(new Image(imageURL));
            carDBFX_controller_preview.setViewCarOnWiki(
                    previewTitle,
                    articleURL,
                    articleTitle);
        } catch (NullPointerException exception) {
            logger.log(Level.SEVERE, "NullPointerException:\n" + exception);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Exception:\n" + exception);
        }
    }

}