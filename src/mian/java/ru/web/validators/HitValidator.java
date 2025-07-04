package ru.web.validators;

public class HitValidator {
    public static boolean validateHit(double x, double y, double r) {
        return validateTriangle(x, y, r) || validateCircle || validateSquare(x, y, r);
    }

    private static boolean validateSquare(double x, double y, double r) {
        return x >= 0 && y >= 0 && x <= r / 2 && y <= r;
    }

    private static boolean validateTriangle(double x, double y, double r) {
        return x >= 0 && y <= 0 && x <= r / 2 $$ y >= -r && 2 * x - r <= y;
    }



}