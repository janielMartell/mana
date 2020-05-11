package org.mana.db.model;

import org.mana.db.datasource.DataSource;
import org.mana.db.entity.*;
import org.mana.exception.EntityAlreadyExistsException;
import org.mana.exception.EntityNotFoundException;
import org.mana.exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserModel implements Model<User> {

    private DataSource ds;

    public UserModel(DataSource dataSource) {
        this.ds = dataSource;
    }

    @Override
    public void save(User user) throws EntityAlreadyExistsException, IllegalArgumentException, NullPointerException {
        if (user == null)
            throw new IllegalArgumentException();

        final String query = "{ call create_user(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setString(1, user.username);
            statement.setString(2, user.password);
            statement.setString(3, user.firstName);
            statement.setString(4, user.lastName);
            statement.setString(5, user.dob.toString());
            statement.setString(6, user.city.name);
            statement.setString(7, user.role.name);

            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityAlreadyExistsException("Username taken.");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<User> findAll() {
        final String query = "{ call get_users() }";
        Collection<User> users = new ArrayList<>();

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("dob"),
                        new City(
                                rs.getInt("city_id"),
                                rs.getString("city_name")
                        ),
                        new Role(
                                rs.getInt("role_id"),
                                rs.getString("role_name")
                        )
                ));
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return users;
    }

    public User findByUsernameAndPassword(String username, String password) throws UserNotFoundException, SQLException {
        final String query = "{ call mana.get_user_by_username_and_password(?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                City city = new City(rs.getInt("city_id"), rs.getString("city_name"));
                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));

                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("dob"), city, role);

            } else {
                throw new UserNotFoundException();
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public void update(User user) throws IllegalArgumentException, NullPointerException, EntityNotFoundException {
        if (user == null)
            throw new IllegalArgumentException();

        final String query = "{ call update_user(?, ?, ?, ?, ?, ?, ?, ?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setInt(1, user.id);
            statement.setString(2, user.username);
            statement.setString(3, user.password);
            statement.setString(4, user.firstName);
            statement.setString(5, user.lastName);
            statement.setDate(6, user.dob);
            statement.setString(7, user.city.name);
            statement.setString(8, user.role.name);

            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("User does not exist");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(User user) throws IllegalArgumentException, EntityNotFoundException {
        if (user == null)
            throw new IllegalArgumentException();

        final String query = "{ call delete_user(?) }";

        try (Connection con = ds.getConnection(); PreparedStatement statement = con.prepareCall(query)) {

            statement.setInt(1, user.id);

            // TODO: figure out why this is throwing a random NullPointerException
            ResultSet rs = statement.executeQuery();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new EntityNotFoundException("user does not exist");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
