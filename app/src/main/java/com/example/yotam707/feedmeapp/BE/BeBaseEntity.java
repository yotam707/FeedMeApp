package com.example.yotam707.feedmeapp.BE;

import java.io.Serializable;

public class BeBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    public BeBaseEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BeBaseEntity{" +
                "id='" + id + '\'' +
                '}';
    }
}
