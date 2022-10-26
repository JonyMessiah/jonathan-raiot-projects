module com.raiot.raiotprojects {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.raiot.raiotprojects to javafx.fxml;
    opens com.raiot.raiotprojects.controllers to javafx.fxml;
    exports com.raiot.raiotprojects;
}