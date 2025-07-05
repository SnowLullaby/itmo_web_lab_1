package ru.web.validators;

import ru.web.parser.ParseResult;

public class JsonValidator {
    public static ValidationResult validate(ParseResult parsed) {
        if (parsed.getX() < -2 || parsed.getX() > 2) {
            return new ValidationResult(false, "X должен быть числом от -2 до 2");
        }

        if (parsed.getY() < -5 || parsed.getY() > 5) {
            return new ValidationResult(false, "Y должен быть числом от -5 до 5");
        }

        for (double r : parsed.getR()) {
            if (r < 1 || r > 3) {
                return new ValidationResult(false, "R должен быть числом от 1 до 3");
            }
        }

        return new ValidationResult(true, null);
    }
}