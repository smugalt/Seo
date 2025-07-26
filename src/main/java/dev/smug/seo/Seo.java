package dev.smug.seo;

import dev.smug.seo.manager.Manager;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Seo implements ModInitializer {

    private static Seo instance = null;

    public static final String NAME = "Seo";
    public static final String VERSION = "1.0.0";
    public static final boolean Dev = true;
    public static final String Build = Dev ? "dev" : "release";

    private static final Logger LOGGER = LogManager.getLogger(NAME);
    private long startTime;

    public static Seo getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        startTime = System.currentTimeMillis();
        instance = this;
        LOGGER.info("Loading Managers...");
        Manager.loadManagers();
        LOGGER.info("Managers Loaded!");

    }

    public void postInit() {
        LOGGER.info("{} {} ({}) has loaded in {}ms!", NAME,VERSION,Build, System.currentTimeMillis() - startTime);
    }
}
