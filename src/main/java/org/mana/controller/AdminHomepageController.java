package org.mana.controller;

import javafx.fxml.FXML;
import org.mana.App;

import java.io.IOException;

public class AdminHomepageController implements Controller {

    private final String view = "admin_homepage.fxml";

    @FXML
    private void openAdminMovies() throws IOException {
        Controller movies = new AdminMoviesController();

        try {
            movies.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void openAdminCustomers() throws IOException {
        Controller controller = new AdminCustomersController();

        try {
            controller.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void openAdminRentals() throws IOException {
        AdminRentalsController controller = new AdminRentalsController();

        try {
            controller.showView();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
