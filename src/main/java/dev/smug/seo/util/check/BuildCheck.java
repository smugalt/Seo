package dev.smug.seo.util.check;

public class BuildCheck {
    public static String checkVer(String buildVer) {
        String ver = buildVer.toLowerCase();

        if (ver.indexOf('a') != -1) return "alpha";
        if (ver.indexOf('n') != -1) return "nightly";
        if (ver.indexOf('b') != -1) return "beta";
        if (ver.indexOf('e') != -1) return "experimental";
        if (ver.indexOf('d') != -1) return "dev";

        return "stable";
    }
}