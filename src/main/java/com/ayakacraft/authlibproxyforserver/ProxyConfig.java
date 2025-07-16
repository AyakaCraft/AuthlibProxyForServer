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

    public boolean enabled = false;

    public ProxyType type = ProxyType.HTTP;

    public String host = "127.0.0.1";

    public short port = 7897;

    public ProxyConfig() {
    }

    public ProxyConfig validate() throws InvalidProxyConfigException {
        if (!enabled) {
            return this;
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            throw new InvalidProxyConfigException(InvalidReason.HOST, host, e);
        }
        if (port < 0) {
            throw new InvalidProxyConfigException(InvalidReason.PORT, port);
        }
        return this;
    }

    public Proxy createProxy() throws InvalidProxyConfigException {
        validate();
        if (enabled) {
            return new DynamicProxy(this);
        } else {
            return Proxy.NO_PROXY;
        }
    }

    private static class DynamicProxy extends Proxy {

        private final ProxyConfig config;

        public DynamicProxy(ProxyConfig config) {
            super(config.type.real, new InetSocketAddress(config.host, config.port));
            this.config = config;
        }

        @Override
        public Proxy.Type type() {
            return config.type.real;
        }

        @Override
        public InetSocketAddress address() {
            return new InetSocketAddress(config.host, config.port);
        }

    }

    public static class InvalidProxyConfigException extends Exception {

        public final InvalidReason reason;

        public final Object value;

        public InvalidProxyConfigException(InvalidReason reason, Object value) {
            this.reason = reason;
            this.value = value;
        }

        public InvalidProxyConfigException(InvalidReason reason, Object value, Throwable cause) {
            super(cause);
            this.reason = reason;
            this.value = value;
        }

        @Override
        public String getMessage() {
            return reason.message + ": " + value;
        }

    }

    public enum InvalidReason {

        TYPE("Invalid proxy type"),
        HOST("Unknown proxy host"),
        PORT("Invalid proxy port");

        public final String message;

        InvalidReason(String message) {
            this.message = message;
        }

    }

    public enum ProxyType {

        HTTP(Proxy.Type.HTTP),

        SOCKS(Proxy.Type.SOCKS);

        public final Proxy.Type real;

        ProxyType(Proxy.Type real) {
            this.real = real;
        }

    }

}
