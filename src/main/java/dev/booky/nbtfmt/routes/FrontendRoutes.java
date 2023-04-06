package dev.booky.nbtfmt.routes;
// Created by booky10 in NbtFormatter (01:42 06.04.23)

import io.javalin.Javalin;
import io.javalin.http.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class FrontendRoutes {

    private static final byte[] HTML, SCRIPT, STYLE;

    static {
        try (InputStream htmlInput = FrontendRoutes.class.getResourceAsStream("/index.html");
             InputStream scriptInput = FrontendRoutes.class.getResourceAsStream("/script.js");
             InputStream styleInput = FrontendRoutes.class.getResourceAsStream("/style.css")) {
            HTML = Objects.requireNonNull(htmlInput).readAllBytes();
            SCRIPT = Objects.requireNonNull(scriptInput).readAllBytes();
            STYLE = Objects.requireNonNull(styleInput).readAllBytes();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private FrontendRoutes() {
    }

    public static void register(Javalin javalin) {
        javalin.get("/", ctx -> {
            ctx.contentType(ContentType.TEXT_HTML);
            ctx.result(HTML);
        });
        javalin.get("/script.js", ctx -> {
            ctx.contentType(ContentType.JAVASCRIPT_MODERN);
            ctx.result(SCRIPT);
        });
        javalin.get("/style.css", ctx -> {
            ctx.contentType(ContentType.TEXT_CSS);
            ctx.result(STYLE);
        });
    }
}
