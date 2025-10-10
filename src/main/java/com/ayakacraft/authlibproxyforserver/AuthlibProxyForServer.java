/*
 * This file is part of the AuthlibProxyForServer project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.authlibproxyforserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AuthlibProxyForServer {

    private static final Path configPath = Paths.get("./config/authlib.proxy.json").toAbsolutePath();

    public static final Logger LOGGER = LogManager.getLogger("AuthlibProxyForServer");

    public static final ProxyConfig config;

    public static Proxy proxy;

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        ProxyConfig c = new ProxyConfig();
        if (Files.isRegularFile(configPath)) {
            try {
                String str = readString(configPath);
                c = GSON.fromJson(str, ProxyConfig.class);
            } catch (Throwable e) {
                LOGGER.error("Failed to read config file for authproxy", e);
            }
        }
        try {
            saveConfig(c);
        } catch (IOException ignored) {
        }
        config = c;
    }

    public static void saveConfig(ProxyConfig config) throws IOException {
        writeString(configPath, GSON.toJson(config));
    }

    public static String readString(Path path) throws IOException {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(path))).toString();
    }

    public static void writeString(Path path, String str) throws IOException {
        if (Files.notExists(path)) {
            path.getParent().toFile().mkdirs();
            Files.createFile(path);
        }
        Files.write(path, str.getBytes(StandardCharsets.UTF_8));
    }

}
