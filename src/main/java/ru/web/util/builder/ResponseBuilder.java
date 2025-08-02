package ru.web.util.builder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.web.dto.ResponseDTO;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseBuilder {
    private static final Gson GSON = new Gson();
    private static final String RESPONSE_TEMPLATE = """
            Status: %d %s
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    public static String buildSuccessResponse(List<ResponseDTO> responses) {
        String jsonResponse = GSON.toJson(responses);
        return String.format(RESPONSE_TEMPLATE, 200, "OK", jsonResponse.getBytes(StandardCharsets.UTF_8).length, jsonResponse);
    }

    public static String buildErrorResponse(int status, String statusText, String errorMessage) {
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("error", errorMessage);
        String jsonError = GSON.toJson(errorResponse);
        return String.format(RESPONSE_TEMPLATE, status, statusText, jsonError.getBytes(StandardCharsets.UTF_8).length, jsonError);
    }

    private ResponseBuilder(){}
}
