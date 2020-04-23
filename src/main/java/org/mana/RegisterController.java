package org.mana;

import java.io.IOException;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }
}