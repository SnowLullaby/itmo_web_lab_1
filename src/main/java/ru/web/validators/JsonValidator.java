package ru.web.validators;

import ru.web.Parser;

public class JsonValidator {
    public static class ValidationResult {
        public boolean isValid;
        public String errorMessage;

        public ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }
    }

    public static ValidationResult validate(Parser.ParseResult parsed) {
        if (parsed.x < -2 || parsed.x > 2) {
            return new ValidationResult(false, "X должен быть числом от -2 до 2");
        }

        if (parsed.y < -5 || parsed.y > 5) {
            return new ValidationResult(false, "Y должен быть числом от -5 до 5");
        }

        for (double r : parsed.r) {
            if (r < 1 || r > 3) {
                return new ValidationResult(false, "R должен быть числом от 1 до 3");
            }
        }

        return new ValidationResult(true, null);
    }
}