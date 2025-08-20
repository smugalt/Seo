package dev.smug.seo.util.check;

public class BuildCheck {
    private static String version;

    public static String checkVer(String buildVer) {
        String ver = buildVer == null ? "" : buildVer.toLowerCase();

        if (ver.indexOf('a') != -1) return version = "alpha";
        if (ver.indexOf('n') != -1) return version = "nightly";
        if (ver.indexOf('b') != -1) return version = "beta";
        if (ver.indexOf('e') != -1) return version = "experimental";
        if (ver.indexOf('d') != -1) return version = "dev";

        return version = "stable";
    }

    public static String getVersion() {
        return version;
    }
}