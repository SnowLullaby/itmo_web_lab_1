package ru.web;

import com.fastcgi.FCGIInterface;
import ru.web.handler.RequestHandler;

public class Main {
    public static void main(String[] args) {
        FCGIInterface fcgiInt = new FCGIInterface();
        RequestHandler requestHandler = new RequestHandler(fcgiInt);
        requestHandler.processRequests();
    }
}