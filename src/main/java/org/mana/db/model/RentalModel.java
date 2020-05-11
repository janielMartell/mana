package org.mana.db.model;

import org.mana.db.datasource.DataSource;
import org.mana.db.entity.*;
import org.mana.exception.EntityException;
import org.mana.exception.EntityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class RentalModel implements Model<Rental> {
    private DataSource ds;

    public RentalModel(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void save(Rental rental) throws IllegalArgumentException, NullPointerException, EntityException {
        if (rental == null)
            throw new IllegalArgumentException();
        if (rental.movie == null || rental.user == null || rental.status == null)
            throw new IllegalArgumentException();


        final String query = "{ call insert_rental(?, ?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setInt(1, rental.movie.id);
            statement.setInt(2, rental.user.id);
            statement.setString(3, rental.status.name);


            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("Movie or User not found.");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<Rental> findAll() {
        final String query = "{ call get_rentals() }";
        Collection<Rental> rentals = new ArrayList<>();

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                Rating rating = new Rating(rs.getInt("rating_id"), rs.getString("rating_name"), rs.getString("rating_description"));

                Movie movie = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getBinaryStream("poster"), rs.getString("director"), rs.getString("writer"), rs.getDate("release_date"), rs.getTime("length"), rs.getString("cast"), rating, genre);

                Status status = new Status(rs.getInt("status_id"), rs.getString("status_name"));

                City city = new City(rs.getInt("city_id"), rs.getString("city_name"));

                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));

                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob"), city, role);

                Rental rental = new Rental(rs.getInt("rental_id"), rs.getDate("date_created"), status, movie, user);

                rentals.add(rental);
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return rentals;
    }

    public Collection<Rental> findByUserId(int id) throws EntityNotFoundException {
        final String query = "{ call get_rental_by_user_id(?) }";

        Collection<Rental> rentals = new ArrayList<>();

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                Rating rating = new Rating(rs.getInt("rating_id"), rs.getString("rating_name"), rs.getString("rating_description"));

                Movie movie = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getBinaryStream("poster"), rs.getString("director"), rs.getString("writer"), rs.getDate("release_date"), rs.getTime("length"), rs.getString("cast"), rating, genre);

                Status status = new Status(rs.getInt("status_id"), rs.getString("status_name"));

                rentals.add(new Rental(rs.getInt("rental_id"), rs.getDate("date_created"), status, movie));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

            throw new EntityNotFoundException();
        }

        return rentals;
    }

    public void updateStatus(int id, String statusName) throws IllegalArgumentException, EntityNotFoundException {
        if (statusName == null)
            throw new IllegalArgumentException();

        final String query = "{ call update_rental_status(?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setInt(1, id);
            statement.setString(2, statusName);

            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("Rental or Status does not exist");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
