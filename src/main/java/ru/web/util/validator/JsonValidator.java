package ru.web.util.validator;

import ru.web.dto.RequestDTO;

public class JsonValidator {
    public static ValidationResult validate(RequestDTO parsed) {
        if (parsed == null) {
            return new ValidationResult(false, "JSON не может быть пустым");
        }

        if (parsed.x() == null) {
            return new ValidationResult(false, "X не задан");
        }

        if (parsed.x() < -2 || parsed.x() > 2) {
            return new ValidationResult(false, "X должен быть числом от -2 до 2");
        }

        if (parsed.y() == null) {
            return new ValidationResult(false, "Y не задан");
        }

        if (parsed.y() < -5 || parsed.y() > 5) {
            return new ValidationResult(false, "Y должен быть числом от -5 до 5");
        }

        if (parsed.r() == null || parsed.r().length == 0) {
            return new ValidationResult(false, "R не задан");
        }

        for (Double r : parsed.r()) {
            if (r == null) {
                return new ValidationResult(false, "Значение R не может быть null");
            }
            if (r < 1 || r > 3) {
                return new ValidationResult(false, "R должен быть числом от 1 до 3");
            }
        }

        return new ValidationResult(true, null);
    }

    private JsonValidator(){}
}