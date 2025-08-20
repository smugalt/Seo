package dev.smug.seo.util.cyber.base;

import dev.smug.seo.util.cyber.Table;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Base64 extends Table {

    private static final int[] DECODE_TABLE = new int[256];

    static {
        Arrays.fill(DECODE_TABLE, -1);
        for (int i = 0; i < BASE64_STANDARD.length; i++) {
            DECODE_TABLE[BASE64_STANDARD[i]] = i;
        }
        DECODE_TABLE['='] = 0;
    }

    /**
     * Encode a string to Base64
     * @param string input string
     * @return Base64 encoded string
     */
    public static String encode(String string) {
        if (string == null) {
            return null;
        }
        byte[] data = string.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder((data.length * 4 + 2) / 3);

        int i = 0;
        while (i < data.length) {
            int b1 = data[i++] & 0xFF;
            int b2 = (i < data.length) ? data[i++] & 0xFF : 0;
            int b3 = (i < data.length) ? data[i++] & 0xFF : 0;

            int combined = (b1 << 16) | (b2 << 8) | b3;

            sb.append(BASE64_STANDARD[(combined >> 18) & 0x3F]);
            sb.append(BASE64_STANDARD[(combined >> 12) & 0x3F]);
            sb.append(i - 1 < data.length ? BASE64_STANDARD[(combined >> 6) & 0x3F] : '=');
            sb.append(i < data.length ? BASE64_STANDARD[combined & 0x3F] : '=');
        }
        return sb.toString();
    }

    /**
     * Decode a Base64 string
     * @param string Base64 encoded string
     * @return decoded plain string, or null if invalid
     */
    public static String decode(String string) {
        if (string == null) {
            return null;
        }
        char[] chars = string.toCharArray();
        int len = chars.length;

        if (len % 4 != 0) {
            return null;
        }

        byte[] buffer = new byte[len * 3 / 4];
        int pos = 0;

        for (int i = 0; i < len; i += 4) {
            int c1 = DECODE_TABLE[chars[i]];
            int c2 = DECODE_TABLE[chars[i + 1]];
            int c3 = DECODE_TABLE[chars[i + 2]];
            int c4 = DECODE_TABLE[chars[i + 3]];

            if (c1 == -1 || c2 == -1 || c3 == -1 || c4 == -1) {
                return null;
            }

            int combined = (c1 << 18) | (c2 << 12) | (c3 << 6) | c4;

            buffer[pos++] = (byte) ((combined >> 16) & 0xFF);
            if (chars[i + 2] != '=') {
                buffer[pos++] = (byte) ((combined >> 8) & 0xFF);
            }
            if (chars[i + 3] != '=') {
                buffer[pos++] = (byte) (combined & 0xFF);
            }
        }

        return new String(buffer, 0, pos, StandardCharsets.UTF_8);
    }
}
