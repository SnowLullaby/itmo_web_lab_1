package ru.web.util.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.web.dto.RequestDTO;

public class JsonParser {
    private static final Gson GSON = new Gson();

    public static RequestDTO parse(String jsonString) throws IllegalArgumentException {
        try {
            RequestDTO dto = GSON.fromJson(jsonString, RequestDTO.class);
            if (dto == null) {
                throw new IllegalArgumentException("Пустой JSON");
            }
            return dto;
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Некорректный формат запроса", e);
        }
    }
}