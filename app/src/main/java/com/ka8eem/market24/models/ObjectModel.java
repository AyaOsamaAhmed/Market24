package com.ka8eem.market24.models;

import com.ka8eem.market24.adapters.ObjectAdapter;

import java.io.Serializable;

public class ObjectModel implements Serializable {

    private Object object;

    private boolean isPaid;

    public ObjectModel(Object object, boolean isPaid) {
        this.object = object;
        this.isPaid = isPaid;
    }

    public Object getObject() {
        return object;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
