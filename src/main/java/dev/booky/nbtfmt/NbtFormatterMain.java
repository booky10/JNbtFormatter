package dev.booky.nbtfmt;
// Created by booky10 in NbtFormatter (13:17 20.08.22)

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;

import java.io.IOException;

public class NbtFormatterMain {

    public static void main(String[] args) throws IOException {
        StringBuilder nbt = new StringBuilder();
        if (args.length == 0) {
            if (System.in.available() == 0) {
                System.out.print("Please enter nbt string: ");
            }

            char c;
            while ((c = (char) System.in.read()) != '\n') {
                nbt.append(c);
            }
        } else {
            nbt.append(args[0]);
        }

        CompoundBinaryTag tag = TagStringIO.get().asCompound(nbt.toString());
        String indention = System.getenv().getOrDefault("indention", "2");
        System.out.println(TagStringIO.builder().indent(Integer.parseInt(indention)).build().asString(tag));
    }
}
