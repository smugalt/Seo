package dev.smug.seo.util.cyber;

import dev.smug.seo.util.cyber.base.Base64;
import dev.smug.seo.util.cyber.base.Base91;

public class Table {

    // Base91 encoding table
    protected static final char[] BASE91_EXTENDED_ASCII = (
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!#$%&()*+,./:;<=>?@[]^_`{|}~\"'"
    ).toCharArray();

    // Standard Base64 encoding table
    protected static final char[] BASE64_STANDARD = (
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789+/"
    ).toCharArray();

    // Base64 URL-safe encoding table
    protected static final char[] BASE64_URL_SAFE = (
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789-_"
    ).toCharArray();

    public static String encodeBase64(String string) {
        return Base64.encode(string);
    }

    public static String decodeBase64(String string) {
        return Base64.decode(string);
    }

    public static String encodeBase91(String string) {
        return Base91.encode(string);
    }

    public static String decodeBase91(String string) {
        return Base91.decode(string);
    }

}