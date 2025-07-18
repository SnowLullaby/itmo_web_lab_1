package ru.web.util.validator;

import ru.web.model.RequestDTO;

public class JsonValidator {
    public static ValidationResult validate(RequestDTO parsed) {
        if (parsed.x() < -2 || parsed.x() > 2) {
            return new ValidationResult(false, "X должен быть числом от -2 до 2");
        }

        if (parsed.y() < -5 || parsed.y() > 5) {
            return new ValidationResult(false, "Y должен быть числом от -5 до 5");
        }

        for (double r : parsed.r()) {
            if (r < 1 || r > 3) {
                return new ValidationResult(false, "R должен быть числом от 1 до 3");
            }
        }

        return new ValidationResult(true, null);
    }
}