package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Movie;
import org.mana.db.entity.Rental;
import org.mana.db.entity.Status;
import org.mana.db.model.MovieModel;
import org.mana.db.model.RentalModel;
import org.mana.exception.EntityNotFoundException;
import org.mana.utils.CurrentUser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MovieDetailsController implements ParametrizedController<MovieDetailsController> {
    private final String view = "movie_details.fxml";
    private final int movieId;
    private Movie movie;

    @FXML
    ImageView posterView;
    @FXML
    Label titleLabel;
    @FXML
    Label ratingLabel;
    @FXML
    Label runtimeLabel;
    @FXML
    Label genreLabel;
    @FXML
    Label releaseDateLabel;
    @FXML
    Label directorLabel;
    @FXML
    Label writerLabel;
    @FXML
    Label castLabel;

    public MovieDetailsController(int id) {
        this.movieId = id;
    }


    @FXML
    public void initialize() {
        MovieModel movieModel = new MovieModel(new HikariCPDataSource());

        movie = null;
        try {
            movie = movieModel.findById(movieId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something went wrong! Try again later.");
            alert.showAndWait();
            goBack();
        }


        posterView.setFitWidth(200.0);
        posterView.setFitHeight(350.0);
        posterView.setImage(new Image(movie.poster));
        titleLabel.setText(movie.title);
        ratingLabel.setText(String.format("Rated %s", movie.getRatingName()));
        runtimeLabel.setText(movie.length.toString());
        genreLabel.setText(movie.getGenreName());
        releaseDateLabel.setText(movie.releaseDate.toString());
        directorLabel.setText(movie.director);
        writerLabel.setText(movie.writer);
        castLabel.setText(movie.cast);
    }

    @FXML
    public void rent() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Price per day: $5.00" +
                "\nReservation Date: " + dtf.format(LocalDate.now()) +
                "\nTax: 11.5%"
        );

        confirm.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    RentalModel rentalModel = new RentalModel(new HikariCPDataSource());
                    rentalModel.save(new Rental(new Status("reserved"), movie, CurrentUser.getInstance()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setHeaderText("Something went wrong! Try again later.");
                    error.show();
                    return;
                }

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText("Success! You can go pick up your reservation.");
                info.show();
            }
        });
    }

    @FXML
    public void goBack() {
        Controller controller = new UserMoviesController();

        try {
            controller.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showView(MovieDetailsController controller) throws IOException {
        App.setRoot(this.view, controller);
    }

    @Override
    public String getView() {
        return null;
    }
}
