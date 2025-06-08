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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

public class ProxyConfig {

    private final boolean enabled;

    private final String type;

    private final String host;

    private final int port;

    public ProxyConfig(boolean enabled, String type, String host, int port) {
        this.enabled = enabled;
        this.type = type;
        this.host = host;
        this.port = port;
    }

    public void validate() throws IllegalArgumentException {
        if (!enabled || "DIRECT".equalsIgnoreCase(type)) {
            return;
        }
        if (!"HTTP".equalsIgnoreCase(type) && !"SOCKS".equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("Invalid proxy type: " + type);
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Unknown proxy host: " + host, e);
        }
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid proxy port: " + port);
        }
    }

    public Proxy createProxy() throws IllegalArgumentException {
        validate();
        if (enabled && !"DIRECT".equals(type)) {
            return new Proxy(Proxy.Type.valueOf(type.toUpperCase()), new InetSocketAddress(host, port));
        } else {
            return Proxy.NO_PROXY;
        }
    }

}
