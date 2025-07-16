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

package com.ayakacraft.authlibproxyforserver.commands;

import com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer;
import com.ayakacraft.authlibproxyforserver.ProxyConfig;
import com.ayakacraft.authlibproxyforserver.mixin.YggdrasilAuthenticationServiceAccessor;
import com.ayakacraft.authlibproxyforserver.utils.NetworkUtils;
import com.ayakacraft.authlibproxyforserver.utils.preprocess.PreprocessPattern;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import static com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer.LOGGER;
import static com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer.config;
import static com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer.proxy;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class AuthProxyCommand {

    private static final int TCPING_TIMES = 5;

    private static int display(CommandContext<ServerCommandSource> context) {
        sendFeedback(
                context.getSource(),
                proxyText(),
                false
        );

        return 1;
    }

    private static int enable(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (config.enabled) {
            sendFeedback(
                    source,
                    Text.literal("Proxy already enabled"),
                    false
            );
            return 1;
        }
        config.enabled = true;
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Text.literal("Proxy enabled, restart the server to apply"),
                true
        );
        return 1;
    }

    private static int disable(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (!config.enabled) {
            sendFeedback(
                    source,
                    Text.literal("Proxy already disabled"),
                    false
            );
            return 1;
        }
        config.enabled = false;
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Text.literal("Proxy disabled, restart the server to apply"),
                true
        );
        return 1;
    }

    private static int port(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        int                 port   = IntegerArgumentType.getInteger(context, "port");
        config.port = (short) port;
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Text.literal("Proxy port set to " + port),
                true
        );
        sendFeedback(
                source,
                proxyText(),
                true
        );
        return 1;
    }

    private static int host(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String              host   = StringArgumentType.getString(context, "host");
        config.host = host;
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Text.literal("Proxy host set to " + host),
                true
        );
        sendFeedback(
                source,
                proxyText(),
                true
        );
        return 1;
    }

    private static int type(CommandContext<ServerCommandSource> context, String type) {
        ServerCommandSource source = context.getSource();
        config.type = ProxyConfig.ProxyType.valueOf(type);
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Text.literal("Proxy type set to " + type),
                true
        );
        sendFeedback(
                source,
                proxyText(),
                true
        );
        return 1;
    }

    private static int ping(CommandContext<ServerCommandSource> context) {
        List<String> hosts = new LinkedList<>();
        //#if MC>=12006
        com.mojang.authlib.Environment env = YggdrasilAuthenticationServiceAccessor.determineEnvironment();
        hosts.add(env.sessionHost());
        hosts.add(env.servicesHost());
        //#elseif MC>=11600
        //$$ com.mojang.authlib.Environment env = YggdrasilAuthenticationServiceAccessor.determineEnvironment();
        //$$ hosts.add(env.getAuthHost());
        //$$ hosts.add(env.getAccountsHost());
        //$$ hosts.add(env.getSessionHost());
        //$$ hosts.add(env.getServicesHost());
        //#else
        //$$ hosts.add(com.ayakacraft.authlibproxyforserver.mixin.YggdrasilGameProfileRepositoryAccessor.getBaseUrl());
        //$$ hosts.add(com.ayakacraft.authlibproxyforserver.mixin.YggdrasilMinecraftSessionServiceAccessor.getBaseUrl());
        //$$ hosts.add(com.ayakacraft.authlibproxyforserver.mixin.YggdrasilUserAuthenticationAccessor.getBaseUrl());
        //#endif
        new TcpingThread(hosts, context.getSource()).start();
        return 1;
    }

    /**
     * @return true if failed
     */
    private static boolean saveConfig(ServerCommandSource source) {
        try {
            AuthlibProxyForServer.saveConfig(config.validate());
        } catch (IOException e) {
            source.sendError(Text.literal("Error saving config"));
            LOGGER.error("Error saving config", e);
            return true;
        } catch (ProxyConfig.InvalidProxyConfigException e) {
            source.sendError(Text.literal(e.getMessage()));
            LOGGER.error(e);
            return true;
        }
        return false;
    }

    private static Text proxyText() {
        return Text.literal("Proxy for authlib: " + proxy.toString());
    }

    @PreprocessPattern
    private static Text li(String str) {
        //#if MC>=11900
        return Text.literal(str);
        //#else
        //$$ return new net.minecraft.text.LiteralText(str);
        //#endif
    }

    private static void sendFeedback(ServerCommandSource source, Text txt, boolean broadcastToOps) {
        //#if MC>=12000
        source.sendFeedback(() -> txt, broadcastToOps);
        //#else
        //$$ source.sendFeedback(txt, broadcastToOps);
        //#endif
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("authproxy")
                        .requires(source -> source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .executes(AuthProxyCommand::display)
                        .then(literal("enable").executes(AuthProxyCommand::enable))
                        .then(literal("disable").executes(AuthProxyCommand::disable))
                        .then(literal("port").then(
                                argument("port", IntegerArgumentType.integer(0, 65535))
                                        .executes(AuthProxyCommand::port)
                        ))
                        .then(literal("host").then(
                                argument("host", StringArgumentType.greedyString())
                                        .executes(AuthProxyCommand::host)
                        ))
                        .then(literal("type")
                                .then(literal("http").executes(it -> AuthProxyCommand.type(it, "HTTP")))
                                .then(literal("socks").executes(it -> AuthProxyCommand.type(it, "SOCKS")))
                        )
                        .then(literal("ping").executes(AuthProxyCommand::ping))
        );
    }

    private static class TcpingThread extends Thread {

        private static Text packetLossStatusText(int packetsReceived) {
            double     packetLoss = 1d - (double) packetsReceived / AuthProxyCommand.TCPING_TIMES;
            Formatting colour;
            if (packetLoss >= 0.8d) {
                colour = Formatting.RED;
            } else if (packetLoss >= 0.3d) {
                colour = Formatting.YELLOW;
            } else {
                colour = Formatting.GREEN;
            }
            return Text.literal(String.format("%d packets transmitted, %d packets received, ", TCPING_TIMES, packetsReceived))
                    .append(Text.literal(String.format("%.1f%%", packetLoss * 100)).formatted(colour))
                    .append(Text.literal(" packet loss"));
        }

        private static Text averagePingText(long ping) {
            Formatting colour;
            if (ping >= 800) {
                colour = Formatting.RED;
            } else if (ping >= 400) {
                colour = Formatting.YELLOW;
            } else {
                colour = Formatting.GREEN;
            }
            return Text.literal("Average ping: ")
                    .append(Text.literal(ping + "ms").formatted(colour));
        }

        private final List<String> hosts;

        private final ServerCommandSource source;

        public TcpingThread(List<String> hosts, ServerCommandSource source) {
            this.hosts = hosts;
            this.source = source;
        }

        private void tcping(String host) {
            Pair<Long, Integer> res = NetworkUtils.tcpingMultiple(URI.create(host), proxy, TCPING_TIMES);
            sendFeedback(
                    source,
                    Text.literal(String.format("Ping for '%s':", host)),
                    false
            );
            sendFeedback(
                    source,
                    packetLossStatusText(res.getRight()),
                    false
            );
            if (res.getRight() > 0) {
                sendFeedback(
                        source,
                        averagePingText(res.getLeft() / TCPING_TIMES),
                        false
                );
            }
        }

        public void run() {
            sendFeedback(source, Text.literal("Ping started"), false);
            hosts.forEach(this::tcping);
            sendFeedback(source, Text.literal("Ping finished"), false);
        }

    }

}
