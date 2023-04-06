package dev.booky.nbtfmt.main;
// Created by booky10 in MinceraftWeb (00:59 14.10.22)

import dev.booky.nbtfmt.NbtFormatterService;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NbtFormatterConsole extends SimpleTerminalConsole {

    private static final Logger LOGGER = LogManager.getLogger("Console");
    private static final Executor CMD_EXECUTOR = Executors.newCachedThreadPool();

    private final NbtFormatterService service;

    public NbtFormatterConsole(NbtFormatterService service) {
        this.service = service;
    }

    public void startThread() {
        new Thread(this::start, "Service Console Thread").start();
    }

    @Override
    protected boolean isRunning() {
        return this.service.isRunning();
    }

    @Override
    protected void runCommand(String command) {
        command = command.trim();
        int spaceIndex = command.indexOf(' ');

        String label;
        if (spaceIndex < 0) {
            label = command.toLowerCase(Locale.ROOT);
        } else {
            label = command.substring(0, spaceIndex).toLowerCase(Locale.ROOT);
        }

        CMD_EXECUTOR.execute(() -> runCommand0(label));
    }

    protected void runCommand0(String label) {
        switch (label) { // not a great command "system", but works
            case "stop", "shutdown", "end" -> {
                LOGGER.info("Shutting down...");
                this.service.stop(true);
            }
            case "help" -> {
                LOGGER.info("NbtFormatter console commands:");
                LOGGER.info(" - help (shows this message)");
                LOGGER.info(" - stop (stops the service)");
            }
            default -> LOGGER.warn("Unknown command, type \"help\" for help");
        }
    }

    @Override
    protected void shutdown() {
        this.service.stop(true);
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder builder) {
        return super.buildReader(builder.appName("NbtFormatter")
                .variable(LineReader.HISTORY_FILE, Path.of(".console_history")));
    }
}
