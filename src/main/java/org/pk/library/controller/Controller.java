package org.pk.library.controller;

import java.io.IOException;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}


