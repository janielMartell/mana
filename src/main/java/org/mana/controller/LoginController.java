package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.City;
import org.mana.db.entity.User;
import org.mana.db.model.Model;
import org.mana.db.model.UserModel;
import org.mana.exception.UserNotFoundException;
import org.mana.utils.FieldValidator;

import java.io.IOException;
import java.sql.Date;

public class LoginController implements Controller {
    private final String view = "login.fxml";

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    @FXML
    private void authenticate() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        FieldValidator validator = new FieldValidator();

        if (validator.isNullOrWhitespace(username) || validator.isNullOrWhitespace(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("All fields are required, please fill out the form.");
            alert.show();
            return;
        }
        User user = null;
        try {
            UserModel userModel = new UserModel(new HikariCPDataSource());
            user = userModel.findByUsernameAndPassword(username, password);
        } catch (UserNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("The credentials don't match an account.");
            alert.show();
            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong, please try again later.");
            alert.show();
            return;
        }

        if (user != null) {
            // TODO: go to homescreen
        }
    }

    @FXML
    private void openRegister() throws IOException {
        Controller register = new RegisterController();

        try {
            register.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void showView() throws IOException {
        App.setRoot(this.view);
    }

    @Override
    public String getView() {
        return this.view;
    }
}
