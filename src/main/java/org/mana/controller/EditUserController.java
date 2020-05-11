package org.mana.controller;

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
import org.mana.exception.EntityNotFoundException;
import org.mana.utils.CurrentUser;
import org.mana.utils.FieldValidator;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class EditUserController implements ParametrizedController<EditUserController> {
    private final String view = "edit_user.fxml";
    private final Controller sourceController;
    private final User user;

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

    public EditUserController(Controller controller, User user) {
        sourceController = controller;
        this.user = user;
    }

    @FXML
    private void initialize() {
        roleChoice.getItems().add("user");

        firstnameField.setText(user.firstName);
        lastnameField.setText(user.lastName);
        cityField.setText(user.getCityName());
        dobPicker.setValue(user.dob.toLocalDate());
        usernameField.setText(user.username);
        roleChoice.setValue(user.getRoleName());

        if (CurrentUser.getInstance().getRoleName().equals("admin"))
            roleChoice.getItems().add("admin");
    }

    @FXML
    private void goBack() {
        try {
            sourceController.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void update() {
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
            UserModel userModel = new UserModel(new HikariCPDataSource());
            userModel.update(new User(user.id, username, password, firstName, lastName, Date.valueOf(dob), new City(cityName), new Role(role)));
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

    @Override
    public void showView(EditUserController controller) throws IOException {
        App.setRoot(this.view, controller);
    }

    @Override
    public String getView() {
        return view;
    }
}
