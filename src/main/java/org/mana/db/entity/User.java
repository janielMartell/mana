package org.mana.db.entity;

import org.mana.utils.FieldValidator;

import java.sql.Date;

public class User implements Entity {

    public int id;
    public String username;
    public String password;

    public String firstName;
    public String lastName;

    public Date dob;

    public City city;
    public Role role;

    public User(String username, String password, String firstName, String lastName, Date dob, City city, Role role) throws IllegalArgumentException {
        FieldValidator val = new FieldValidator();

        if (val.isNullOrWhitespace(username) || val.isNullOrWhitespace(password) || val.isNullOrWhitespace(firstName) || val.isNullOrWhitespace(lastName) || city == null || dob == null || role == null) {
            throw new IllegalArgumentException();
        }

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.city = city;
        this.role = role;
    }

    public User(int id, String username, String password, String firstName, String lastName, Date dob, City city, Role role) throws IllegalArgumentException {
        FieldValidator val = new FieldValidator();

        if (val.isNullOrWhitespace(username) || val.isNullOrWhitespace(password) || val.isNullOrWhitespace(firstName) || val.isNullOrWhitespace(lastName) || city == null || dob == null || role == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.city = city;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getCityName() {
        return city.name;
    }

    public String getRoleName() {
        return role.name;
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }
}
