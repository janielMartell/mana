package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Rental;
import org.mana.db.model.RentalModel;
import org.mana.exception.EntityNotFoundException;
import org.mana.utils.CurrentUser;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

public class UserRentalsController implements Controller {
    private String view = "user_rentals.fxml";

    @FXML
    private TableView rentalTable;

    @FXML
    private void initialize() {
        rentalTable.setPlaceholder(new Label("No rentals found"));

        setColumns();

        RentalModel rentalModel = new RentalModel(new HikariCPDataSource());

        Collection<Rental> rentals = new ArrayList<>();

        try {
            rentals = rentalModel.findByUserId(CurrentUser.getInstance().id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        for (Rental rental : rentals) {
            rentalTable.getItems().add(rental);
        }
    }

    @FXML
    private void goBack() {
        UserMoviesController userMoviesController = new UserMoviesController();

        try {
            userMoviesController.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setColumns() {
        var idColumn = new TableColumn<Rental, Integer>("Rental ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        var statusColumn = new TableColumn<Rental, String>("status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusName"));

        var titleColumn = new TableColumn<Rental, String>("Movie Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));

        var dateColumn = new TableColumn<Rental, Date>("Rented Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("rentedDate"));


        rentalTable.getColumns().addAll(idColumn, statusColumn, titleColumn, dateColumn);

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
