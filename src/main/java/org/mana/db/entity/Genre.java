package org.mana.db.entity;

public class Genre implements Entity {
    public int id;
    public String name;

    public Genre(String name) {
        this.name = name;
    }

    public Genre(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
