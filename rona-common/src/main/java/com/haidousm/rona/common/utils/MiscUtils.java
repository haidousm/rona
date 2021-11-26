package com.haidousm.rona.common.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

// todo: rename
public class MiscUtils {

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
}
