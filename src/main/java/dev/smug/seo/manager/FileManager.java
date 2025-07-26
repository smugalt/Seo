package dev.smug.seo.manager;

import dev.smug.seo.Seo;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class FileManager {
    public static final File SEO_PATH = FabricLoader.getInstance().getGameDir().resolve("seo").toFile();
    public static final File MODULES = new File(SEO_PATH, "modules");
    public static final Logger LOGGER = Seo.getLogger();

    public FileManager() {
        createDirectories();
    }

    public void createDirectories() {
        if (!SEO_PATH.exists() && !SEO_PATH.mkdirs()) {
            LOGGER.error("Failed to create directory: {}", SEO_PATH);
        }

        if (!MODULES.exists() && !MODULES.mkdirs()) {
            LOGGER.error("Failed to create directory: {}", MODULES);
        }
    }
}
