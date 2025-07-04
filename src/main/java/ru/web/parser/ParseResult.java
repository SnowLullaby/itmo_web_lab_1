package ru.web.parser;

import ru.web.dto.RequestDTO;

public class ParseResult {
    private final RequestDTO dto;

    public ParseResult(RequestDTO dto) {
        this.dto = dto;
    }

    public double getX() { return dto.getX(); }
    public double getY() { return dto.getY(); }
    public double[] getR() { return dto.getR(); }
}