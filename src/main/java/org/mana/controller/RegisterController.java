package org.mana.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import org.mana.App;

public class RegisterController {

    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }
}