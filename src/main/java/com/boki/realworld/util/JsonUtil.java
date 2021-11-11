package com.boki.realworld.util;

import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@NoArgsConstructor
@Component
public class JsonUtil {

    // json 형식으로 유입된 HttpServletRequest를 string 형태로 return
    public JSONObject readJSONStringFromRequestBody(HttpServletRequest request) {
        StringBuilder json = new StringBuilder();
        String line;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

        } catch (Exception e) {
            log.info("Error reading JSON string: " + e.toString());
        }

        return new JSONObject(json.toString());
    }
}