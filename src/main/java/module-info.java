module org.pk.library {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.pk.library.controller to javafx.fxml;
    exports org.pk.library.controller;

}