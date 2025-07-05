package ru.web;

import com.google.gson.JsonObject;
import com.fastcgi.FCGIInterface;
import ru.web.dto.ResponseDTO;
import ru.web.parser.ParseResult;
import ru.web.parser.Parser;
import ru.web.validators.HitValidator;
import ru.web.validators.JsonValidator;
import ru.web.validators.ValidationResult;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();
    private static final String RESPONSE_TEMPLATE = """
            Status: %d %s
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    public static void main(String[] args) {
        FCGIInterface fcgiInt = new FCGIInterface();
        while (fcgiInt.FCGIaccept() >= 0) {
            long startTime = System.nanoTime();
            try {
                String jsonString = readRequestBody();
                LOGGER.info("Получен запрос: " + jsonString);

                handleRequest(jsonString, startTime);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        int contentLength = FCGIInterface.request.inStream.available();
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        var readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, contentLength);
        var requestBodyRaw = new byte[readBytes];
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    private static void handleRequest(String jsonString, long startTime) {
        ParseResult parsed = Parser.parse(jsonString);
        ValidationResult validation = JsonValidator.validate(parsed);

        if (!validation.isValid()) {
            sendErrorResponse(400, "Bad Request", validation.errorMessage());
            return;
        }

        List<ResponseDTO> responses = processLogic(parsed, startTime);
        sendSuccessResponse(responses);
    }

    private static List<ResponseDTO> processLogic(ParseResult parsed, long startTime) {
        List<ResponseDTO> responses = new ArrayList<>();
        double[] rValues = parsed.getR();

        for (double r : rValues) {
            long executionTime = (System.nanoTime() - startTime) / 1_000_000;
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            boolean hit = HitValidator.validateHit(parsed.getX(), parsed.getY(), r);
            responses.add(new ResponseDTO(parsed.getX(), parsed.getY(), r, hit, currentTime, executionTime));
        }
        return responses;
    }

    private static void sendSuccessResponse(List<ResponseDTO> responses) {
        String jsonResponse = GSON.toJson(responses);
        String response = String.format(RESPONSE_TEMPLATE, 200, "OK", jsonResponse.getBytes(StandardCharsets.UTF_8).length, jsonResponse);
        LOGGER.info("Отправлен ответ: " + response);
        System.out.println(response);
    }

    private static void sendErrorResponse(int status, String statusText, String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("error", errorMessage);
        String jsonError = GSON.toJson(errorResponse);
        String response = String.format(RESPONSE_TEMPLATE, status, statusText, jsonError.getBytes(StandardCharsets.UTF_8).length, jsonError);
        LOGGER.info("Ошибка: " + response);
        System.out.println(response);
    }

    private static void handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            sendErrorResponse(400, "Bad Request", e.getMessage());
        } else {
            sendErrorResponse(500, "Internal Server Error", "Внутренняя ошибка сервера: " + e.getMessage());
        }
        LOGGER.severe("Ошибка обработки: " + e.getMessage());
    }
}