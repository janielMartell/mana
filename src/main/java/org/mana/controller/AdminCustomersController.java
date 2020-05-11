package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.User;
import org.mana.db.model.Model;
import org.mana.db.model.UserModel;
import org.mana.utils.CurrentUser;

import java.io.IOException;
import java.util.Collection;

public class AdminCustomersController implements Controller {
    private final String view = "admin_customers.fxml";
    private final UserModel userModel = new UserModel(new HikariCPDataSource());
    @FXML
    private TableView<User> customersTable;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Collection<User> users;


    @FXML
    private void initialize() {
        var selectionModel = customersTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            } else {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            }
        });

        setColumns();

        users = userModel.findAll();

        users.forEach(user -> {
            customersTable.getItems().add(user);
        });
    }

    private void setColumns() {
        var idColumn = new TableColumn<User, String>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        var firstNameColumn = new TableColumn<User, String>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        var lastNameColumn = new TableColumn<User, String>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        var usernameColumn = new TableColumn<User, String>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        var cityColumn = new TableColumn<User, String>("City");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));

        var dobColumn = new TableColumn<User, String>("Birthday");
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        var roleColumn = new TableColumn<User, String>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));

        customersTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, usernameColumn, cityColumn, dobColumn, roleColumn);
    }

    @FXML
    private void goBack() {
        Controller movies = new AdminHomepageController();
        try {
            movies.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong, please try again later.");
            alert.show();
        }
    }

    @FXML
    private void goToRegister() {
        ParametrizedController registerController = new RegisterController(new AdminCustomersController());
        try {
            registerController.showView(registerController);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong, please try again later.");
            alert.show();
        }
    }

    @FXML
    private void gotToEditUser() {
        User user = customersTable.getSelectionModel().getSelectedItem();

        ParametrizedController editUserController = new EditUserController(new AdminCustomersController(), user);

        try {
            editUserController.showView(editUserController);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong, please try again later.");
            alert.show();
        }
    }

    @FXML
    private void refresh() {
        try {
            this.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong, please try again later.");
            alert.show();
        }
    }

    @FXML
    private void delete() {
        User user = customersTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure you want to delete %s?", user.username));
        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    userModel.delete(user);
                    refresh();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Something went wrong! Try again later.");
                    alert.show();

                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showView() throws IOException {
        App.setRoot(this.view);
    }

    @Override
    public String getView() {
        return view;
    }
}