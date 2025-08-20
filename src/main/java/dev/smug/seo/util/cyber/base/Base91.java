package dev.smug.seo.util.cyber.base;

import dev.smug.seo.util.cyber.Table;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Base91 extends Table {

    // Pre-computed lookup table for faster decoding
    private static final byte[] DECODE_TABLE = new byte[256];
    static {
        Arrays.fill(DECODE_TABLE, (byte) -1);
        for (int i = 0; i < BASE91_EXTENDED_ASCII.length; i++) {
            DECODE_TABLE[BASE91_EXTENDED_ASCII[i] & 0xFF] = (byte) i;
        }
    }

    private static final String[][] URL_PATTERNS = {
            {"https://", "\u0001"},
            {"http://", "\u0002"},
            {"www.", "\u0003"},
            {".com", "\u0004"},
            {".org", "\u0005"},
            {".net", "\u0006"},
            {".io", "\u0007"},
            {".co", "\u0008"},
            {"://", "\t"},
            {"/", "\u000F"},
            {"?", "\u000B"},
            {"&", "\u000C"},
            {"=", "\u000E"}
    };

    public static String encode(String string) {
        // Apply URL optimization
        String optimized = optimizeUrl(string);
        byte[] input = optimized.getBytes(StandardCharsets.UTF_8);

        // Skip compression for short strings (compression overhead makes them larger)
        if (input.length < 50) {
            return base91Encode(input);
        }

        // Try compression
        byte[] compressed = compress(input);

        // Use compressed only if it's actually smaller
        byte[] flagged;
        if (compressed.length < input.length) {
            // Add compression flag
            flagged = new byte[compressed.length + 1];
            flagged[0] = 1; // compression flag
            System.arraycopy(compressed, 0, flagged, 1, compressed.length);
        } else {
            // Add no-compression flag
            flagged = new byte[input.length + 1];
            flagged[0] = 0; // no compression flag
            System.arraycopy(input, 0, flagged, 1, input.length);
        }
        return base91Encode(flagged);
    }

    public static String decode(String string) {
        byte[] decoded = base91Decode(string);
        if (decoded.length == 0) return "";

        // Check compression flag
        boolean isCompressed = decoded[0] == 1;
        byte[] data = Arrays.copyOfRange(decoded, 1, decoded.length);

        if (isCompressed) {
            data = decompress(data);
        }

        String result = new String(data, StandardCharsets.UTF_8);
        return deoptimizeUrl(result);
    }

    private static String optimizeUrl(String url) {
        String result = url;
        // Apply patterns in order of frequency/length
        for (String[] pattern : URL_PATTERNS) {
            result = result.replace(pattern[0], pattern[1]);
        }
        return result;
    }

    private static String deoptimizeUrl(String optimized) {
        String result = optimized;
        // Apply patterns in reverse order
        for (int i = URL_PATTERNS.length - 1; i >= 0; i--) {
            result = result.replace(URL_PATTERNS[i][1], URL_PATTERNS[i][0]);
        }
        return result;
    }

    private static byte[] compress(byte[] data) {
        try {
            // Use LZ4 or similar for better speed/compression ratio
            // For now, using built-in GZIP with optimal settings
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.util.zip.GZIPOutputStream gzip = new java.util.zip.GZIPOutputStream(baos) {
                {
                    // Reduce GZIP header overhead
                    def.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);
                }
            };
            gzip.write(data);
            gzip.close();
            return baos.toByteArray();
        } catch (Exception e) {
            return data; // Return original on error
        }
    }

    private static byte[] decompress(byte[] data) {
        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
            java.util.zip.GZIPInputStream gzip = new java.util.zip.GZIPInputStream(bais);
            byte[] buffer = new byte[1024];
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            int len;
            while ((len = gzip.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            gzip.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Decompression failed", e);
        }
    }

    private static String base91Encode(byte[] data) {
        if (data.length == 0) return "";

        StringBuilder output = new StringBuilder((data.length * 123) / 100); // Pre-allocate
        int ebq = 0;
        int en = 0;

        for (byte b : data) {
            ebq |= (b & 0xFF) << en;
            en += 8;

            if (en > 13) {
                int ev = ebq & 8191;

                if (ev > 88) {
                    ebq >>= 13;
                    en -= 13;
                } else {
                    ev = ebq & 16383;
                    ebq >>= 14;
                    en -= 14;
                }

                output.append(BASE91_EXTENDED_ASCII[ev % 91]);
                output.append(BASE91_EXTENDED_ASCII[ev / 91]);
            }
        }

        if (en > 0) {
            output.append(BASE91_EXTENDED_ASCII[ebq % 91]);
            if (en > 7 || ebq > 90) {
                output.append(BASE91_EXTENDED_ASCII[ebq / 91]);
            }
        }

        return output.toString();
    }

    private static byte[] base91Decode(String data) {
        if (data.isEmpty()) return new byte[0];

        ByteArrayOutputStream output = new ByteArrayOutputStream(data.length());
        int dbq = 0;
        int dn = 0;
        int dv = -1;

        for (int i = 0; i < data.length(); i++) {
            byte c = DECODE_TABLE[data.charAt(i) & 0xFF];
            if (c == -1) continue;

            if (dv < 0) {
                dv = c;
            } else {
                dv += c * 91;
                dbq |= dv << dn;

                if ((dv & 8191) > 88) {
                    dn += 13;
                } else {
                    dn += 14;
                }

                do {
                    output.write(dbq & 0xFF);
                    dbq >>= 8;
                    dn -= 8;
                } while (dn > 7);

                dv = -1;
            }
        }

        if (dv >= 0) {
            output.write((dbq | dv << dn) & 0xFF);
        }

        return output.toByteArray();
    }
}