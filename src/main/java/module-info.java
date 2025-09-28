module ayzekssoft.nextday {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires org.postgresql.jdbc;

    opens ayzekssoft.nextday to javafx.fxml;
    opens ayzekssoft.nextday.ui to javafx.fxml;
    exports ayzekssoft.nextday;
}