package org.mana.db.entity;

import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;

public class Movie implements Entity {
    public int id;
    public InputStream poster;
    public String title;
    public String director;
    public String writer;
    public Date releaseDate;
    public Time length;
    public String cast;
    public Rating rating;
    public Genre genre;


    public Movie(int id, String title, InputStream poster, String director, String writer, Date releaseDate, Time length, String cast, Rating rating, Genre genre) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.director = director;
        this.writer = writer;
        this.releaseDate = releaseDate;
        this.length = length;
        this.cast = cast;
        this.rating = rating;
        this.genre = genre;
    }


    public Movie(String title, InputStream poster, String director, String writer, Date releaseDate, Time length, String cast, Rating rating, Genre genre) {
        this.title = title;
        this.poster = poster;
        this.director = director;
        this.writer = writer;
        this.releaseDate = releaseDate;
        this.length = length;
        this.cast = cast;
        this.rating = rating;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Time getLength() {
        return length;
    }

    public String getCast() {
        return cast;
    }

    public String getRatingName() {
        return rating.name;
    }

    public String getGenreName() {
        return genre.name;
    }
}