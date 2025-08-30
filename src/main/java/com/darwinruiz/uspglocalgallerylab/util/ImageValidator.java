package com.darwinruiz.uspglocalgallerylab.util;

import jakarta.servlet.http.Part;

import java.util.Set;

public class ImageValidator {
    public static final long MAX_BYTES = 3L * 1024 * 1024;
    public static final Set<String> ALLOWED_EXT = Set.of(".png",".jpg",".jpeg",".gif",".webp");

    public static boolean isValid(Part part, String fileName) {
        // verificar si no es null
        if (part == null || fileName == null || fileName.isEmpty()) {
            return false;
        }

        // tamanio de la imagen
        if (part.getSize() > MAX_BYTES) {
            return false;
        }

        // verificar el contenido
        String contentType = part.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            return false;
        }

        // verificar extension
        String extension = "";
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            extension = fileName.substring(lastDotIndex).toLowerCase();
        }
        return ALLOWED_EXT.contains(extension);
    }
}