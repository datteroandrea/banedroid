package com.example.banedroid.dispatcher;

import com.bane.model.BaneObject;
import com.example.banedroid.connection.Connection;

public abstract class Dispatcher {

    public final Connection connection;

    public Dispatcher(Connection connection){
        this.connection = connection;
    }

    public abstract BaneObject dispatch(BaneObject command);

}
