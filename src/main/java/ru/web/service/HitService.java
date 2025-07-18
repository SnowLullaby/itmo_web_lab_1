package ru.web.service;

import ru.web.dto.RequestDTO;
import ru.web.dto.ResponseDTO;
import ru.web.util.validator.HitValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HitService {
    public static List<ResponseDTO> processLogic(RequestDTO parsed, long startTime) {
        List<ResponseDTO> responses = new ArrayList<>();
        double[] rValues = parsed.r();

        for (double r : rValues) {
            long executionTime = (System.nanoTime() - startTime);
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            boolean hit = HitValidator.validateHit(parsed.x(), parsed.y(), r);
            responses.add(new ResponseDTO(parsed.x(), parsed.y(), r, hit, currentTime, executionTime));
        }
        return responses;
    }
}
