package ru.web.handler;

import com.fastcgi.FCGIInterface;

import ru.web.dto.*;
import ru.web.io.IOHandler;
import ru.web.parser.JsonParser;
import ru.web.validators.*;
import java.util.List;
import java.util.Properties;

public class RequestHandler {
    private final FCGIInterface fcgiInt;
    private final IOHandler ioHandler;

    public RequestHandler(FCGIInterface fcgiInt) {
        this.fcgiInt = fcgiInt;
        this.ioHandler = new IOHandler(fcgiInt);
    }

    public void processRequests() {
        while (fcgiInt.FCGIaccept() >= 0) {
            try {
                long startTime = System.nanoTime();

                Properties paramsRaw = FCGIInterface.request.params;
                String method = paramsRaw.getProperty("REQUEST_METHOD");
                String uri = paramsRaw.getProperty("REQUEST_URI");

                if (!"POST".equalsIgnoreCase(method)) {
                    ioHandler.sendResponse(ResponseBuilder.buildErrorResponse(405, "Method Not Allowed", "Метод недоступен"));
                    continue;
                } else if (!"/fcgi-bin/fcgi-server.jar".equals(uri)) {
                    ioHandler.sendResponse(ResponseBuilder.buildErrorResponse(405, "Method Not Allowed", "Неправильный URI"));
                    continue;
                }

                String jsonString = ioHandler.readRequest(method);
                RequestDTO parsed = JsonParser.parse(jsonString);
                ValidationResult validation = JsonValidator.validate(parsed);

                if (!validation.isValid()) {
                    ioHandler.sendResponse(ResponseBuilder.buildErrorResponse(400, "Bad Request", validation.errorMessage()));
                    continue;
                }

                List<ResponseDTO> responses = LogicHandler.processLogic(parsed, startTime);
                ioHandler.sendResponse(ResponseBuilder.buildSuccessResponse(responses));

            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            ioHandler.sendResponse(ResponseBuilder.buildErrorResponse(400, "Bad Request", e.getMessage()));
        } else {
            ioHandler.sendResponse(ResponseBuilder.buildErrorResponse(500, "Internal Server Error", "Внутренняя ошибка сервера: " + e.getMessage()));
        }
    }
}
