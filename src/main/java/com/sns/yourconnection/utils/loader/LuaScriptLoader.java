package com.sns.yourconnection.utils.loader;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class LuaScriptLoader {

    public static String load(String path) {
        try (InputStream in = LuaScriptLoader.class.getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(in))) {

            return reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT);
        }
    }
}
