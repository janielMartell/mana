package org.mana.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Movie;
import org.mana.db.model.MovieModel;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;

public class AdminMoviesController implements Controller {
    private final String view = "admin_movies.fxml";
    private final MovieModel movieModel = new MovieModel(new HikariCPDataSource());
    private Collection<Movie> movies;

    @FXML
    private TableView<Movie> moviesTable;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private void showAddMovie() {
        AddMoviesController addMovies = new AddMoviesController();

        try {
            addMovies.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void showAdminHomepage() {
        Controller adminHomepageController = new AdminHomepageController();

        try {
            adminHomepageController.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void showEditMovie() {
        Movie movie = moviesTable.getSelectionModel().getSelectedItem();

        EditMovieController controller = new EditMovieController(movie);

        try {
            controller.showView(controller);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void deleteMovie() throws IOException {
        Movie movie = moviesTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure you want to delete %s?", movie.title));
        confirmation.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    movieModel.delete(movie);
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Something went wrong! Try again later.");
                    alert.show();

                    ex.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void initialize() {
        moviesTable.setPlaceholder(new Label("No movies found"));
        var selectionModel = moviesTable.getSelectionModel();
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

        movies = movieModel.findAll();

        movies.forEach(movie -> {
            moviesTable.getItems().add(movie);
        });

    }

    private void setColumns() {
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

        moviesTable.getColumns().addAll(idColumn, titleColumn, releaseDateColumn, genreColumn, runtimeColumn, ratingColumn, directorColumn, writerColumn, castColumn);

    }

    @FXML
    private void refresh() {
        try {
            this.showView();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
