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

package com.ayakacraft.authlibproxyforserver.utils;

import net.minecraft.util.Pair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;

public final class NetworkUtils {

    public static long tcping(URI uri, Proxy proxy) {
        try {
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection(proxy);
            connection.setConnectTimeout(1000);
            connection.setUseCaches(false);
            long l = System.currentTimeMillis();
            connection.connect();
            return System.currentTimeMillis() - l;
        } catch (IOException e) {
            return -1L;
        }
    }

    public static Pair<Long, Integer> tcpingMultiple(URI uri, Proxy proxy, int times) {
        long sum = 0L;
        int i = 0;
        for (int j = 0; j < times; j++) {
            long l = tcping(uri, proxy);
            if (l >= 0) {
                sum += l;
                i++;
            }
        }
        return new Pair<>(sum, i);
    }

}
