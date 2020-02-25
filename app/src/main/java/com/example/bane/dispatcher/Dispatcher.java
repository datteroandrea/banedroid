package com.example.bane.dispatcher;

import com.bane.model.BaneObject;
import com.example.bane.connection.Connection;

public abstract class Dispatcher {

    public final Connection connection;

    public Dispatcher(Connection connection){
        this.connection = connection;
    }

    public abstract BaneObject dispatch(BaneObject command);

}
