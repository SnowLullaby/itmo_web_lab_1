package ru.web.io;

import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class IOHandler {
    private final FCGIInterface fcgiInt;

    public IOHandler(FCGIInterface fcgiInt) {
        this.fcgiInt = fcgiInt;
    }

    public String readRequest(String method) throws IOException {
        if ("POST".equalsIgnoreCase(method)) {
            return readPostBody();
        } else if ("GET".equalsIgnoreCase(method)) {
            return readGetQuery();
        }

        return "";
    }

    private String readPostBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        int contentLength = FCGIInterface.request.inStream.available();
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        var readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    private String readGetQuery() throws IOException {
        // in progress
        return "";
    }

    public void sendResponse(String response) {
        try {
            FCGIInterface.request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
            FCGIInterface.request.outStream.flush();
        } catch (IOException ignored) {
        }
    }
}