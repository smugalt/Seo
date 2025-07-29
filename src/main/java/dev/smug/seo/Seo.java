package dev.smug.seo;

import dev.smug.seo.gui.SeoClickGui;
import dev.smug.seo.manager.Manager;
import dev.smug.seo.util.check.BuildCheck;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Seo implements ModInitializer {

    private static Seo instance = null;

    public static final String NAME = "Seo";
    public static final String VERSION = "1.0.0";
    public static final String Build = BuildCheck.checkVer(VERSION);

    private static final Logger LOGGER = LogManager.getLogger(NAME);
    private long startTime;
    public static SeoClickGui ClickGui;

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
        ClickGui = new SeoClickGui();
        LOGGER.info("{} {} ({}) has loaded in {}ms!", NAME,VERSION,Build, System.currentTimeMillis() - startTime);
    }
}
