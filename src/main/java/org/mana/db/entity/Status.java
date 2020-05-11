package org.mana.db.entity;

public class Status implements Entity {
    public int id;
    public String name;

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Status(String name) {
        this.name = name;
    }
}
