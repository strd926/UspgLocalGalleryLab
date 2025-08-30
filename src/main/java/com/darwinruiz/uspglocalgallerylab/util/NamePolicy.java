package com.darwinruiz.uspglocalgallerylab.util;

import java.time.LocalDate;

public class NamePolicy {
    public static String normalize(String original) {
        //manejar null o vacio
        if (original == null || original.isEmpty()) {
            return "";
        }

        // extrae el nombre de la extension
        String baseName = original.substring(original.lastIndexOf('/') + 1);
        baseName = baseName.substring(baseName.lastIndexOf('\\') + 1);

        // convertir a minusculas
        String normalized = baseName.toLowerCase().replaceAll("\\s+", "-");

        // mantener caracteres permitidos
        normalized = normalized.replaceAll("[^a-z0-9._-]", "");

        // limite de 80 caracteres
        if (normalized.length() > 80) {
            // encuentra el ultimo punto para verificar extension
            int lastDotIndex = normalized.lastIndexOf('.');
            if (lastDotIndex != -1 && lastDotIndex < normalized.length() - 1) {
                String name = normalized.substring(0, lastDotIndex);
                String ext = normalized.substring(lastDotIndex);
                // limite de nombre a 80 con la extension
                int maxNameLength = 80 - ext.length();
                if (maxNameLength > 0) {
                    normalized = name.substring(0, Math.min(name.length(), maxNameLength)) + ext;
                } else {
                    // condicion del nombre muy largo
                    normalized = normalized.substring(0, 80);
                }
            } else {
                // sin extension, no pasa
                normalized = normalized.substring(0, 80);
            }
        }

        return normalized;
    }

    /** subcarpeta por fecha: "imagenes/yyyy/MM/dd" */
    public static String datedSubdir(LocalDate d) {
        return String.format("imagenes/%04d/%02d/%02d", d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }
}