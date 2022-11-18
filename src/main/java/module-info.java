module dnu.fpecs.rudyson.cardbfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires org.json;
    requires java.desktop;


    opens dnu.fpecs.rudyson.cardbfx to javafx.fxml;
    exports dnu.fpecs.rudyson.cardbfx;
    exports dnu.fpecs.rudyson.cardbfx.db;
    opens dnu.fpecs.rudyson.cardbfx.db to javafx.fxml;
}