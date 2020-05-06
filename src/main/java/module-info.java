module org.pk.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.calendarfx.view;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires java.sql;

    opens org.pk.library.view to javafx.fxml;
    exports org.pk.library.view;

}