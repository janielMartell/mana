package org.mana.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.mana.App;
import org.mana.db.datasource.HikariCPDataSource;
import org.mana.db.entity.Movie;
import org.mana.db.model.MovieModel;
import org.mana.utils.CurrentUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class UserMoviesController implements Controller {

    private final String view = "user_movies.fxml";

    @FXML
    TilePane moviesContainer;

    @FXML
    private void initialize() {
        MovieModel movieModel = new MovieModel(new HikariCPDataSource());


        Collection<Movie> movies = movieModel.findAllAvailable();
        Collection<VBox> posters = new ArrayList<>();

        for (Movie movie : movies) {
            VBox poster = new VBox();
            poster.setAlignment(Pos.CENTER);
            poster.setSpacing(10.0);
            poster.setId(movie.title);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(200.0);
            imageView.setFitHeight(350.0);

            Image image = new Image(movie.poster);
            imageView.setImage(image);


            Button button = new Button("Details");
            button.setOnAction(actionEvent -> {
                showDetails(movie);
            });

            poster.getChildren().addAll(imageView, button);
            posters.add(poster);
        }

        moviesContainer.getChildren().addAll(posters);
    }

    private void showDetails(Movie movie) {
        MovieDetailsController controller = new MovieDetailsController(movie.id);

        try {
            controller.showView(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void goToHistory() {
        UserRentalsController userRentalsController = new UserRentalsController();

        try {
            userRentalsController.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editAccount() {
        ParametrizedController editUserController = new EditUserController(new UserMoviesController(), CurrentUser.getInstance());

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
    public void signOut() {
        CurrentUser.destroyInstance();

        LoginController controller = new LoginController();

        try {
            controller.showView();
        } catch (IOException e) {
            e.printStackTrace();
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
