package org.mana.db.model;

import org.mana.db.datasource.DataSource;
import org.mana.db.entity.City;
import org.mana.db.entity.Role;
import org.mana.db.entity.User;
import org.mana.exception.EntityAlreadyExistsException;
import org.mana.exception.UserNotFoundException;

import java.sql.*;

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

}
