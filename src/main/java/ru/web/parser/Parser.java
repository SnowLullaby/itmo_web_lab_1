package ru.web.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.web.dto.RequestDTO;

public class Parser {
    private static final Gson GSON = new Gson();

    public static ParseResult parse(String jsonString) throws IllegalArgumentException {
        try {
            RequestDTO dto = GSON.fromJson(jsonString, RequestDTO.class);
            if (dto == null) {
                throw new IllegalArgumentException("Пустой JSON");
            }
            return new ParseResult(dto);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Некорректный формат значений", e);
        }
    }
}