package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Rental;
import org.mana.db.entity.Status;
import org.mana.db.model.RentalModel;
import org.mana.exception.EntityNotFoundException;

import java.io.IOException;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

public class AdminRentalsController implements Controller {
    private final String view = "admin_rentals.fxml";

    @FXML
    private TableView<Rental> rentalTable;
    @FXML
    private Button reservationButton;
    @FXML
    private Button returnButton;

    @FXML
    private void initialize() {
        rentalTable.setPlaceholder(new Label("No rentals found"));

        var selectionModel = rentalTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                reservationButton.setDisable(true);
                returnButton.setDisable(true);
            } else {
                Rental rental = selectionModel.getSelectedItem();

                if (rental.status.name.equals("reserved")) {
                    reservationButton.setDisable(false);
                    returnButton.setDisable(true);
                } else if (rental.status.name.equals("rented")) {
                    returnButton.setDisable(false);
                    reservationButton.setDisable(true);
                } else {
                    reservationButton.setDisable(true);
                    returnButton.setDisable(true);
                }

            }

        });

        setColumns();

        RentalModel rentalModel = new RentalModel(new HikariCPDataSource());
        Collection<Rental> rentals = rentalModel.findAll();

        for (Rental rental : rentals) {
            if (!rental.getStatusName().equals("returned"))
                rentalTable.getItems().add(rental);
        }
    }

    private void setColumns() {
        var idCol = new TableColumn<Rental, Integer>("Rental ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        var statusCol = new TableColumn<Rental, String>("Rental Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusName"));

        var titleCol = new TableColumn<Rental, String>("Movie Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        var usernameCol = new TableColumn<Rental, String>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userUsername"));

        var fullNameCol = new TableColumn<Rental, String>("Full Name");
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("userFullName"));

        var rentedDateCol = new TableColumn<Rental, Date>("Rented Date");
        rentedDateCol.setCellValueFactory(new PropertyValueFactory<>("rentedDate"));

        rentalTable.getColumns().addAll(idCol, statusCol, titleCol, usernameCol, fullNameCol, rentedDateCol);
    }

    @FXML
    public void newRental() {
        AddRentalController controller = new AddRentalController();

        try {
            controller.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rentReservation() {
        Rental rental = rentalTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure want to rent %s to %s?", rental.movie.title, rental.user.username));

        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    RentalModel rentalModel = new RentalModel(new HikariCPDataSource());
                    rentalModel.updateStatus(rental.id, "rented");
                    refresh();
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Something went wrong! Try again later.");
                    alert.show();
                }
            }
        });
    }

    @FXML
    public void returnRental() {
        Rental rental = rentalTable.getSelectionModel().getSelectedItem();



        LocalDate now = LocalDate.now();
        Period period = Period.between(rental.rentedDate.toLocalDate(), now);

        int cost = 5 * period.getDays();
        double total = cost + (cost * 0.115);


        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        confirmation.setHeaderText("You are returning " +
                rental.movie.title +
                "." +
                "\nPrice per day: $5.00" +
                "\nRented Date: " + dtf.format(rental.rentedDate.toLocalDate()) +
                "\nReturn Date: " + dtf.format(now) +
                "\nTax: 11.5%" +
                "\nTotal: " + format.format(total)
        );
        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    RentalModel rentalModel = new RentalModel(new HikariCPDataSource());
                    rentalModel.updateStatus(rental.id, "returned");
                    refresh();
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Something went wrong! Try again later.");
                    alert.show();
                }
            }
        });
    }

    @FXML
    public void refresh() {
        AdminRentalsController controller = new AdminRentalsController();

        try {
            controller.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack() {
        AdminHomepageController controller = new AdminHomepageController();

        try {
            controller.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showView() throws IOException {
        App.setRoot(view);
    }

    @Override
    public String getView() {
        return view;
    }
}
