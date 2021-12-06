package com.haidousm.rona.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

// todo: rename
public class MiscUtils {

    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static String encodeFileToBase64(Path path) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void decodeBase64ToFile(String base64Code, Path savePath) throws Exception {
        if (!savePath.toFile().getParentFile().exists() && !savePath.toFile().getParentFile().mkdirs()) {
            throw new Exception("Failed to create save path");
        }
        byte[] bytes = Base64.getDecoder().decode(base64Code);
        Files.write(savePath, bytes);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Type clazz) {
        return gson.fromJson(json, clazz);
    }
}
