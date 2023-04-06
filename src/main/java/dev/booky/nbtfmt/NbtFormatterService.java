package dev.booky.nbtfmt;
// Created by booky10 in NbtFormatter (01:39 06.04.23)

import dev.booky.nbtfmt.config.ConfigLoader;
import dev.booky.nbtfmt.main.NbtFormatterConsole;
import dev.booky.nbtfmt.routes.ApiRoutes;
import dev.booky.nbtfmt.routes.FrontendRoutes;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.text.DecimalFormat;

public class NbtFormatterService {

    private static final Logger LOGGER = LogManager.getLogger("Service");

    private Javalin javalin;
    private boolean running = true;

    private final Path configPath;
    private NbtFormatterConfig config;

    public NbtFormatterService(Path configPath) {
        this.configPath = configPath;
        this.reloadConfig();
    }

    public void reloadConfig() {
        this.config = ConfigLoader.loadObject(this.configPath, NbtFormatterConfig.class);
    }

    public void start(long startTime) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stop0(null), "Shutdown Handling Thread"));

        LOGGER.info("Creating console reader");
        new NbtFormatterConsole(this).startThread();

        LOGGER.info("Creating javalin service...");
        this.javalin = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.plugins.enableRouteOverview("/api");
        });

        LOGGER.info("Configuring http routes...");
        ApiRoutes.register(this.javalin);
        FrontendRoutes.register(this.javalin);

        LOGGER.info("Launching java service on {}...", this.config.getBindAddress());
        String host = this.config.getBindAddress().getAddress().getHostAddress();
        this.javalin.start(host, this.config.getBindAddress().getPort());

        double bootTime = (System.currentTimeMillis() - startTime) / 1000d;
        String bootTimeStr = new DecimalFormat("#.##").format(bootTime);
        LOGGER.info("Done ({}s)! To shutdown type \"stop\"", bootTimeStr);
    }

    public void stop(boolean cleanExit) {
        stop0(cleanExit);
    }

    private void stop0(Boolean cleanExit) {
        if (!this.running) {
            return;
        }
        this.running = false;

        LOGGER.info("Closing javalin server...");
        if (this.javalin != null) {
            this.javalin.close();
        }

        LOGGER.info("Shutting down... Goodbye (°#°)");
        LogManager.shutdown();

        if (cleanExit != null) {
            System.exit(cleanExit ? 0 : 1);
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public NbtFormatterConfig getConfig() {
        return this.config;
    }
}
