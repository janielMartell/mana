package org.mana.db.entity;

public class Rating implements Entity {
    public int id;
    public String name;
    public String description;

    public Rating(String name) {
        this.name = name;
    }

    public Rating(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
