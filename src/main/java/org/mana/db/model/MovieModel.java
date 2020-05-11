package org.mana.db.model;

import org.mana.db.datasource.DataSource;
import org.mana.db.entity.*;
import org.mana.exception.EntityNotFoundException;
import org.mana.exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class MovieModel implements Model<Movie> {
    private DataSource ds;

    public MovieModel(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void save(Movie movie) throws IllegalArgumentException, SQLException, NullPointerException, EntityNotFoundException {
        if (movie == null)
            throw new IllegalArgumentException();

        final String query = "{ call create_movie(?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setBinaryStream(1, movie.poster);
            statement.setString(2, movie.title);
            statement.setString(3, movie.director);
            statement.setString(4, movie.writer);
            statement.setDate(5, movie.releaseDate);
            statement.setTime(6, movie.length);
            statement.setString(7, movie.cast);
            statement.setString(8, movie.genre.name);
            statement.setString(9, movie.rating.name);

            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("Rating does not exist");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Movie movie) throws IllegalArgumentException, SQLException, NullPointerException, EntityNotFoundException {
        if (movie == null)
            throw new IllegalArgumentException();

        final String query = "{ call update_movie(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setInt(1, movie.id);
            statement.setBinaryStream(2, movie.poster);
            statement.setString(3, movie.title);
            statement.setString(4, movie.director);
            statement.setString(5, movie.writer);
            statement.setDate(6, movie.releaseDate);
            statement.setTime(7, movie.length);
            statement.setString(8, movie.cast);
            statement.setString(9, movie.genre.name);
            statement.setString(10, movie.rating.name);

            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("Rating does not exist");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<Movie> findAll() {

        final String query = "{ call get_movies() }";
        Collection<Movie> movies = new ArrayList<>();

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                movies.add(
                        new Movie(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getBinaryStream("poster"),
                                rs.getString("director"),
                                rs.getString("writer"),
                                rs.getDate("release_date"),
                                rs.getTime("length"),
                                rs.getString("cast"),
                                new Rating(
                                        rs.getInt("rating_id"),
                                        rs.getString("rating_name"),
                                        rs.getString("rating_description")
                                ),
                                new Genre(
                                        rs.getInt("genre_id"),
                                        rs.getString("genre_name")
                                )
                        )
                );
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return movies;
    }

    public Collection<Movie> findAllAvailable() {

        final String query = "{ call get_available_movies() }";
        Collection<Movie> movies = new ArrayList<>();

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                movies.add(
                        new Movie(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getBinaryStream("poster"),
                                rs.getString("director"),
                                rs.getString("writer"),
                                rs.getDate("release_date"),
                                rs.getTime("length"),
                                rs.getString("cast"),
                                new Rating(
                                        rs.getInt("rating_id"),
                                        rs.getString("rating_name"),
                                        rs.getString("rating_description")
                                ),
                                new Genre(
                                        rs.getInt("genre_id"),
                                        rs.getString("genre_name")
                                )
                        )
                );
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return movies;
    }

    public void delete(Movie movie) throws IllegalArgumentException, EntityNotFoundException {
        if (movie == null)
            throw new IllegalArgumentException();

        final String query = "{ call delete_movie(?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setInt(1, movie.id);

            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("Rating does not exist");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public Movie findById(int id) throws EntityNotFoundException {
        final String query = "{ call get_movie_by_id(?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                return new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getBinaryStream("poster"),
                        rs.getString("director"),
                        rs.getString("writer"),
                        rs.getDate("release_date"),
                        rs.getTime("length"),
                        rs.getString("cast"),
                        new Rating(
                                rs.getInt("rating_id"),
                                rs.getString("rating_name"),
                                rs.getString("rating_description")
                        ),
                        new Genre(
                                rs.getInt("genre_id"),
                                rs.getString("genre_name")
                        )
                );

            } else {
                throw new EntityNotFoundException();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return null;
    }
}
