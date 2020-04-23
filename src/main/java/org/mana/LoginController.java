package org.mana;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    @FXML
    private void authenticate() {
        String username =  usernameField.getText();
        String password =  passwordField.getText();

        if (isNullOrWhitespace(username) || isNullOrWhitespace(password)){
            //TODO: Show Error Message
            System.out.print("It's empty");
        }
    }

    @FXML
    private void openRegister() throws IOException {
        App.setRoot("register");
    }


    private boolean isNullOrWhitespace(String str) {
        if(str == null || str.length() == 0)
            return true;

        for(int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i)))
                return false;
        }

        return true;
    }
}
