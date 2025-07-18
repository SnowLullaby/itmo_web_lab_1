package ru.web.util.parser;

import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParser {
    private static final Gson GSON = new Gson();

    public static String parseToJsonString(String queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return "{}";
        }

        Map<String, Object> params = new HashMap<>();
        String[] pairs = queryParams.split("&");

        for (String pair : pairs) {
            if (pair.isEmpty()) continue;
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], java.nio.charset.StandardCharsets.UTF_8);

                if (key.equals("r")) {
                    String[] rValues = value.split(",");
                    List<String> rList = new ArrayList<>();
                    for (String r : rValues) {
                        rList.add(r.trim());
                    }
                    params.put(key, rList);
                } else {
                    params.put(key, value);
                }
            }
        }

        return GSON.toJson(params);
    }
}
