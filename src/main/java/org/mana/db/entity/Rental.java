package org.mana.db.entity;

import java.sql.Date;

public class Rental implements Entity{
    public int id;
    public Date rentedDate;
    public Status status;
    public Movie movie;
    public User user;

    public Rental(int id, Date rentedDate, Status status, Movie movie) {
        this.id = id;
        this.movie = movie;
        this.status = status;
        this.rentedDate = rentedDate;
    }

    public Rental(int id, Date rentedDate, Status status, Movie movie, User user) {
        this.id = id;
        this.movie = movie;
        this.user = user;
        this.status = status;
        this.rentedDate = rentedDate;
    }

    public Rental(Status status, Movie movie, User user) {
        this.movie = movie;
        this.user = user;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Date getRentedDate() {
        return rentedDate;
    }

    public String getStatusName() {
        return status.name;
    }

    public String getMovieTitle() {
        return movie.title;
    }

    public String getUserUsername() {
        return this.user.username;
    }

    public String getUserFullName() {
        return this.user.getFullName();
    }
}
