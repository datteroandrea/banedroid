package com.example.banedroid.connection.dispatcher;

import com.example.banedroid.connection.Connection;

public class DefaultDispatcher extends Dispatcher {

    public DefaultDispatcher(Connection connection) {

    }

    @Override
    public String dispatch(String command) {
        System.out.println(command);
        return "";
    }
}
