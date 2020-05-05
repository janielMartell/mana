package org.mana.db.entity;

import org.mana.utils.FieldValidator;

public class City implements Entity {

    public int id;
    public String name;

    public City(String name) throws IllegalArgumentException {
        FieldValidator val = new FieldValidator();

        if (val.isNullOrWhitespace(name))
            throw new IllegalArgumentException();

        this.name = name;
    }

    public City(int id, String name) throws IllegalArgumentException {
        FieldValidator val = new FieldValidator();

        if (val.isNullOrWhitespace(name))
            throw new IllegalArgumentException();

        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
