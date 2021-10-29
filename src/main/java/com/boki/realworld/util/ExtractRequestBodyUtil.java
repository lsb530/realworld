package com.boki.realworld.util;

import com.boki.realworld.common.servletwrapper.RereadableRequestWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.servlet.http.HttpServletRequest;

public class ExtractRequestBodyUtil {

    public static String extractPostRequestBody(HttpServletRequest request) throws Exception {
        RereadableRequestWrapper requestWrapper = new RereadableRequestWrapper(request);
        if ("POST".equalsIgnoreCase(requestWrapper.getMethod())) {
            Scanner s = null;
            try {
                s = new Scanner(requestWrapper.getInputStream(),
                    StandardCharsets.UTF_8).useDelimiter(
                    "\\A");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s.hasNext() ? s.next() : "";
        }
        return "";
    }
}