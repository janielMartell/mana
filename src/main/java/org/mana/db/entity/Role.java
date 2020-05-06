package org.mana.db.entity;

import org.mana.utils.FieldValidator;

public class Role implements Entity {
    public int id;
    public String name;

    public Role(String name) throws IllegalArgumentException {
        FieldValidator val = new FieldValidator();

        if (val.isNullOrWhitespace(name))
            throw new IllegalArgumentException();

        this.name = name;
    }

    public Role(int id, String name) throws IllegalArgumentException {
        FieldValidator val = new FieldValidator();

        if (val.isNullOrWhitespace(name))
            throw new IllegalArgumentException();

        this.id = id;
        this.name = name;
    }
}
