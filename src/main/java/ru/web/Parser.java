package ru.web;

import com.google.gson.*;

public class Parser {
    public static class ParseResult {
        public double x;
        public double y;
        public double[] r;

        public ParseResult(double x, double y, double[] r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
    }

    public static ParseResult parse(String jsonString) throws IllegalArgumentException {
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

            double x = parseValue(json, "x", "X отсутствует или задан некорректно");
            double y = parseValue(json, "y", "Y отсутствует или задан некорректно");
            double[] r = parseRArray(json, "r", "R отсутствует или задан некорректно");

            return new ParseResult(x, y, r);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга: " + e.getMessage(), e);
        }
    }

    private static double parseValue(JsonObject json, String fieldName, String errorMessage) throws IllegalArgumentException {
        if (!json.has(fieldName) || json.get(fieldName).isJsonNull()) {
            throw new IllegalArgumentException(errorMessage);
        }

        try {
            return json.get(fieldName).getAsDouble();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static double[] parseRArray(JsonObject json, String fieldName, String errorMessage) throws IllegalArgumentException {
        if (!json.has(fieldName) || json.get(fieldName).isJsonNull()) {
            throw new IllegalArgumentException(errorMessage);
        }

        JsonArray rArray = json.get(fieldName).getAsJsonArray();
        if (rArray.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }

        double[] rValues = new double[rArray.size()];
        for (int i = 0; i < rArray.size(); i++) {
            try {
                rValues[i] = rArray.get(i).getAsDouble();
            } catch (Exception e) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
        return rValues;
    }
}