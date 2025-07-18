package ru.web.model;

public record ResponseDTO(double x, double y, double r, boolean hit, String currentTime, long executionTime) {
}
