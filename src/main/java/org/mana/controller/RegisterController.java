package org.mana.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

import com.sun.source.tree.CatchTree;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mana.App;
import org.mana.db.datasource.DataSource;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.City;
import org.mana.db.entity.Role;
import org.mana.db.entity.User;
import org.mana.db.model.Model;
import org.mana.db.model.UserModel;
import org.mana.exception.EntityAlreadyExistsException;
import org.mana.utils.FieldValidator;

public class RegisterController implements Controller {

    private final String view = "register.fxml";
    @FXML
    TextField firstnameField;
    @FXML
    TextField lastnameField;
    @FXML
    TextField cityField;
    @FXML
    DatePicker dobPicker;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    @Override
    public void showView() throws IOException {
        App.setRoot(this.view);
    }

    @Override
    public String getView() {
        return this.view;
    }

    @FXML
    private void openLogin() {
        Controller login = new LoginController();

        try {
            login.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void register() {
        FieldValidator validator = new FieldValidator();

        String firstName = firstnameField.getText();
        String lastName = lastnameField.getText();
        String cityName = cityField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        LocalDate dob = dobPicker.getValue();

        if (validator.isNullOrWhitespace(firstName) ||
                validator.isNullOrWhitespace(lastName) ||
                validator.isNullOrWhitespace(cityName) ||
                validator.isNullOrWhitespace(username) ||
                validator.isNullOrWhitespace(password) ||
                dob == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("All fields are required, please fill out the form.");
            alert.show();
            return;
        }

        try {
            Model userModel = new UserModel(new HikariCPDataSource());
            userModel.save(new User(username, password, firstName, lastName, Date.valueOf(dob), new City(cityName), new Role("user")));
        } catch (EntityAlreadyExistsException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("UsernameTaken");
            alert.show();

            ex.printStackTrace();
            System.out.println(ex.getMessage());

            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong! Try again later.");
            alert.show();

            ex.printStackTrace();
            System.out.println(ex.getMessage());

            return;
        }

        openLogin();
    }
}