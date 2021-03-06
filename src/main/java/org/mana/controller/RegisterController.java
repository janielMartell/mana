package org.mana.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.City;
import org.mana.db.entity.Role;
import org.mana.db.entity.User;
import org.mana.db.model.Model;
import org.mana.db.model.UserModel;
import org.mana.exception.EntityAlreadyExistsException;
import org.mana.utils.CurrentUser;
import org.mana.utils.FieldValidator;

public class RegisterController implements ParametrizedController<RegisterController> {

    private final String view = "register.fxml";
    private final Controller sourceController;

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
    @FXML
    ChoiceBox roleChoice;

    public RegisterController(Controller controller) {
        sourceController = controller;
    }

    @Override
    public void showView(RegisterController controller) throws IOException {
        App.setRoot(this.view, controller);
    }

    @Override
    public String getView() {
        return this.view;
    }

    @FXML
    private void initialize() {
        roleChoice.getItems().add("user");

        try {
            User currentUser = CurrentUser.getInstance();

            if (currentUser.getRoleName().equals("admin"))
                roleChoice.getItems().add("admin");

        } catch (IllegalStateException ex) {
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
        String role;

        try {
            role = roleChoice.getValue().toString();
        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Role is required.");
            alert.show();
            return;
        }

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
            userModel.save(new User(username, password, firstName, lastName, Date.valueOf(dob), new City(cityName), new Role(role)));
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

        goBack();
    }

    @FXML
    private void goBack(){
        try {
            sourceController.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}