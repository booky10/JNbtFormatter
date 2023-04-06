package dev.booky.nbtfmt;
// Created by booky10 in NbtFormatter (01:40 06.04.23)

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.net.InetSocketAddress;

@SuppressWarnings("FieldMayBeFinal") // configurate
@ConfigSerializable
public class NbtFormatterConfig {

    @Comment("The address where the http server will be bound to")
    private InetSocketAddress bindAddress = new InetSocketAddress("localhost", 7072);

    @SuppressWarnings("unused") // configurate
    private NbtFormatterConfig() {
    }

    public InetSocketAddress getBindAddress() {
        return this.bindAddress;
    }
}
