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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AuthlibProxyForServer {

    private static final Path configPath = Paths.get("config/authlib.proxy.json");

    public static final Logger LOGGER = LogManager.getLogger("AuthlibProxyForServer");

    public static final ProxyConfig config;

    static {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ProxyConfig c = new ProxyConfig(false, "HTTP", "127.0.0.1", 7897);
        if (Files.isRegularFile(configPath)) {
            try {
                String str = readString(configPath);
                c = gson.fromJson(str, ProxyConfig.class);
            } catch (Throwable e) {
                LOGGER.error("Failed to read config file for authproxy", e);
            }
        }
        try {
            Files.write(configPath, gson.toJson(c).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored) {
        }
        config = c;
    }

    public static String readString(Path path) throws IOException {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(path))).toString();
    }

}
