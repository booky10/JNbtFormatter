package dev.booky.nbtfmt.routes;
// Created by booky10 in NbtFormatter (01:42 06.04.23)

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import net.kyori.adventure.nbt.TagStringIO;

import java.io.IOException;

public final class ApiRoutes {

    private static final String PREFIX = "api/v1/";

    private ApiRoutes() {
    }

    public static void register(Javalin javalin) {
        javalin.post(PREFIX + "format", ApiRoutes::handleFormatReq);
    }

    private static void handleFormatReq(Context ctx) {
        String indentStr = ctx.queryParam("indent");
        int indent = indentStr == null ? 2 : Integer.parseInt(indentStr);

        try {
            TagStringIO processor = TagStringIO.builder().indent(indent).build();
            ctx.result(processor.asString(processor.asCompound(ctx.body())));
            ctx.status(HttpStatus.OK);
        } catch (IOException exception) {
            Throwable cause = exception.getCause();
            if (cause == null || !"StringTagParseException".equals(cause.getClass().getSimpleName())) {
                throw exception;
            }

            ctx.result(exception.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }
}
