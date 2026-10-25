package com.servicehub.util;

import org.apache.commons.text.StringEscapeUtils;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return StringEscapeUtils.escapeXml11(value.trim());
    }
}