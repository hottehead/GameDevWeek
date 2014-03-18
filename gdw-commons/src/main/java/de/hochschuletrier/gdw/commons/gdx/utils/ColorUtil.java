package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * Helper class to decode color values from string and hex
 *
 * @author Santo Pfingsten
 */
public class ColorUtil {

    public static Color decode(String encoded) {
        int value = Integer.decode(encoded).intValue();

        int r = (value & 0x00FF0000) >> 16;
        int g = (value & 0x0000FF00) >> 8;
        int b = (value & 0x000000FF);
        int a = (value & 0xFF000000) >> 24;

        if (a < 0) {
            a += 256;
        }
        if (a == 0) {
            a = 255;
        }

        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    public static Color fromHex32(long hex) {
        float a = (hex & 0xFF000000L) >> 24;
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);

        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color fromHex24(long hex) {
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);

        return new Color(r / 255f, g / 255f, b / 255f, 1.0f);
    }

    public static Color fromHexString(String string) {
        switch (string.length()) {
            case 8:
                return fromHex32(Long.parseLong(string, 16));
            case 6:
                return fromHex24(Long.parseLong(string, 16));
            default:
                throw new IllegalArgumentException("String must have the form [AA]RRGGBB");
        }
    }

}
