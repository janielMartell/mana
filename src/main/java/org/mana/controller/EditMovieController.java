package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Genre;
import org.mana.db.entity.Movie;
import org.mana.db.entity.Rating;
import org.mana.db.model.MovieModel;
import org.mana.exception.EntityNotFoundException;
import org.mana.utils.FieldValidator;


import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class EditMovieController implements ParametrizedController<EditMovieController> {
    private final String view = "edit_movie.fxml";
    private InputStream posterField;
    private Movie movie;

    @FXML
    TextField titleField;
    @FXML
    TextField genreField;
    @FXML
    TextField directorField;
    @FXML
    TextField writerField;
    @FXML
    TextField runtimeField;
    @FXML
    TextArea castArea;
    @FXML
    ChoiceBox ratingChoice;
    @FXML
    DatePicker releaseDatePicker;

    public EditMovieController(Movie movie) {
        this.movie = movie;
    }

    @FXML
    private void initialize() {
        ratingChoice.getItems().addAll("G", "PG", "PG-13", "R", "NC-17");

        titleField.setText(movie.title);
        genreField.setText(movie.genre.name);
        directorField.setText(movie.director);
        writerField.setText(movie.writer);
        runtimeField.setText(movie.length.toString());
        castArea.setText(movie.cast);
        ratingChoice.setValue(movie.rating.name);
        releaseDatePicker.setValue(movie.releaseDate.toLocalDate());
        posterField = movie.poster;
    }

    @FXML
    private void update() {
        String title = titleField.getText();
        String genreName = genreField.getText();
        String director = directorField.getText();
        String writer = writerField.getText();
        String runtime = runtimeField.getText();
        String cast = castArea.getText();
        LocalDate releaseDate = releaseDatePicker.getValue();
        String ratingName;

        try {
            ratingName = ratingChoice.getValue().toString();
        } catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Rating is required.");
            alert.show();
            return;
        }

        FieldValidator validator = new FieldValidator();

        if (validator.isNullOrWhitespace(title) || validator.isNullOrWhitespace(genreName) || validator.isNullOrWhitespace(director) || validator.isNullOrWhitespace(writer) || validator.isNullOrWhitespace(runtime) || validator.isNullOrWhitespace(cast) || posterField == null || releaseDate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("All fields are required, please fill out the form.");
            alert.show();
            return;
        }

        if (!Pattern.matches(FieldValidator.timeRegex, runtime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Runtime doesn't match the hh:mm:ss format.");
            alert.show();
            return;
        }

        Rating rating = new Rating(ratingName);
        Genre genre = new Genre(genreName);
        Movie updatedMovie = new Movie(movie.id, title, posterField, director, writer, Date.valueOf(releaseDate), Time.valueOf(runtime), cast, rating, genre);

        try {
            MovieModel movieModel = new MovieModel(new HikariCPDataSource());
            movieModel.update(updatedMovie);
        } catch (EntityNotFoundException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("That's not a valid rating");
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
    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(FieldValidator.imageFilter);
        File posterImg = chooser.showOpenDialog(new Stage());

        if (posterImg != null) {
            try {
                posterField = new FileInputStream(posterImg);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    private void goBack() {
        Controller movies = new AdminMoviesController();
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

    @Override
    public String getView() {
        return this.view;
    }

    @Override
    public void showView(EditMovieController controller) throws IOException {
        App.setRoot(this.view, controller);
    }
}
