package dev.booky.nbtfmt.main;
// Created by booky10 in NbtFormatter (13:17 20.08.22)

import dev.booky.nbtfmt.NbtFormatterService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;

import java.nio.file.Path;

public class NbtFormatterMain {

    private static final Logger LOGGER = LogManager.getLogger("Main");

    static {
        System.setProperty("java.util.logging.manager", org.apache.logging.log4j.jul.LogManager.class.getName());
        System.setProperty("java.awt.headless", "true");

        System.setOut(IoBuilder.forLogger("STDOUT").setLevel(Level.INFO).buildPrintStream());
        System.setErr(IoBuilder.forLogger("STDERR").setLevel(Level.ERROR).buildPrintStream());
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Thread.currentThread().setName("Startup Thread");

        LOGGER.info("Reading configuration...");
        Path configPath = Path.of("config.yml").toAbsolutePath();

        NbtFormatterService service = new NbtFormatterService(configPath);
        service.start(startTime);
    }
}
