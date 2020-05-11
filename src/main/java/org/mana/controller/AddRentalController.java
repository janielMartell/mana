package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Movie;
import org.mana.db.entity.Rental;
import org.mana.db.entity.Status;
import org.mana.db.entity.User;
import org.mana.db.model.MovieModel;
import org.mana.db.model.RentalModel;
import org.mana.db.model.UserModel;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class AddRentalController implements Controller {
    private String view = "add_rental.fxml";

    @FXML
    TableView<Movie> movieTable;
    @FXML
    TableView<User> userTable;
    @FXML
    Button rentButton;

    private boolean isUserPicked = false;
    private boolean isMoviePicked = false;

    @FXML
    public void initialize() {
        fillMovieTable();
        fillUserTable();
    }

    private void fillUserTable() {
        var selectionModel = userTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                isUserPicked = true;
                tryEnableRent();
            } else {
                isUserPicked = false;
                rentButton.setDisable(true);
            }
        });

        setUserColumns();

        UserModel userModel = new UserModel(new HikariCPDataSource());
        Collection<User> users = userModel.findAll();

        users.forEach(user -> {
            userTable.getItems().add(user);
        });
    }

    private void setUserColumns() {
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

        userTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, usernameColumn, cityColumn, dobColumn, roleColumn);
    }

    private void fillMovieTable() {
        movieTable.setPlaceholder(new Label("No movies found"));
        var selectionModel = movieTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                isMoviePicked = true;
                tryEnableRent();
            } else {
                isMoviePicked = false;
                rentButton.setDisable(true);
            }
        });

        setMovieColumns();

        MovieModel movieModel = new MovieModel(new HikariCPDataSource());
        Collection<Movie> movies = movieModel.findAllAvailable();

        for (Movie movie : movies) {
            movieTable.getItems().add(movie);
        }
    }

    private void setMovieColumns() {
        var idColumn = new TableColumn<Movie, String>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        var titleColumn = new TableColumn<Movie, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        var releaseDateColumn = new TableColumn<Movie, Date>("Release Date");
        releaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));

        var genreColumn = new TableColumn<Movie, String>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));

        var runtimeColumn = new TableColumn<Movie, Time>("Runtime");
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        var ratingColumn = new TableColumn<Movie, String>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("ratingName"));

        var directorColumn = new TableColumn<Movie, String>("Director");
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));

        var writerColumn = new TableColumn<Movie, String>("Writer(s)");
        writerColumn.setCellValueFactory(new PropertyValueFactory<>("writer"));

        var castColumn = new TableColumn<Movie, String>("Cast");
        castColumn.setCellValueFactory(new PropertyValueFactory<>("cast"));

        movieTable.getColumns().addAll(idColumn, titleColumn, releaseDateColumn, genreColumn, runtimeColumn, ratingColumn, directorColumn, writerColumn, castColumn);
    }

    private void tryEnableRent() {
        if (isUserPicked && isMoviePicked)
            rentButton.setDisable(false);
        else
            rentButton.setDisable(true);
    }

    @FXML
    public void goBack() {
        Controller controller = new AdminRentalsController();

        try {
            controller.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rent() {
        Movie movie = movieTable.getSelectionModel().getSelectedItem();
        User user = userTable.getSelectionModel().getSelectedItem();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Are you sure you want to rent " +
                movie.title +
                " to " +
                user.username +
                "?" +
                "\nPrice per day: $5.00" +
                "\nRental Date: " + dtf.format(LocalDate.now()) +
                "\nTax: 11.5%"
        );

        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    RentalModel rentalModel = new RentalModel(new HikariCPDataSource());
                    rentalModel.save(new Rental(new Status("rented"), movie, user));
                    goBack();
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
        App.setRoot(view);
    }

    @Override
    public String getView() {
        return view;
    }
}
