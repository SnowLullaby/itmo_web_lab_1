package ru.web;

import com.fastcgi.FCGIInterface;
import ru.web.controller.RequestController;

public class Main {
    public static void main(String[] args) {
        FCGIInterface fcgiInt = new FCGIInterface();
        RequestController requestController = new RequestController(fcgiInt);
        requestController.processRequests();
    }
}