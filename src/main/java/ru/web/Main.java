package ru.web;

import com.google.gson.JsonObject;
import com.fastcgi.FCGIInterface;
import ru.web.dto.*;
import ru.web.parser.*;
import ru.web.validators.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
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
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    private static void handleRequest(String jsonString, long startTime) {
        RequestDTO parsed = Parser.parse(jsonString);
        ValidationResult validation = JsonValidator.validate(parsed);

        if (!validation.isValid()) {
            sendErrorResponse(400, "Bad Request", validation.errorMessage());
            return;
        }

        List<ResponseDTO> responses = processLogic(parsed, startTime);
        sendSuccessResponse(responses);
    }

    private static List<ResponseDTO> processLogic(RequestDTO parsed, long startTime) {
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

    private static void sendSuccessResponse(List<ResponseDTO> responses) {
        String jsonResponse = GSON.toJson(responses);
        String response = String.format(RESPONSE_TEMPLATE, 200, "OK", jsonResponse.getBytes(StandardCharsets.UTF_8).length, jsonResponse);
        System.out.println(response);
    }

    private static void sendErrorResponse(int status, String statusText, String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("error", errorMessage);
        String jsonError = GSON.toJson(errorResponse);
        String response = String.format(RESPONSE_TEMPLATE, status, statusText, jsonError.getBytes(StandardCharsets.UTF_8).length, jsonError);
        System.out.println(response);
    }

    private static void handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            sendErrorResponse(400, "Bad Request", e.getMessage());
        } else {
            sendErrorResponse(500, "Internal Server Error", "Внутренняя ошибка сервера: " + e.getMessage());
        }
    }
}