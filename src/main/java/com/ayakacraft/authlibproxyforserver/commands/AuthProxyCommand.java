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
import com.ayakacraft.authlibproxyforserver.utils.NetworkUtils;
import com.ayakacraft.authlibproxyforserver.utils.preprocess.PreprocessPattern;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import static com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer.LOGGER;
import static com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer.config;
import static com.ayakacraft.authlibproxyforserver.AuthlibProxyForServer.proxy;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

//#if MC>=12111
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permission;
//#endif

public final class AuthProxyCommand {

    private static final int TCPING_TIMES = 5;

    @PreprocessPattern
    private static Component li(String str) {
        //#if MC>=11900
        return Component.literal(str);
        //#else
        //$$ return new net.minecraft.network.chat.TextComponent(str);
        //#endif
    }

    private static boolean hasOpPermissionLevel(CommandSourceStack source) {
        //#if MC>=12111
        if (source.getServer() == null) { // I DONT UNDERSTAND WHY... MOJANG ALWAYS GIVES US SOME SURPRISES
            return Commands.hasPermission(Commands.LEVEL_GAMEMASTERS).test(source);
        }
        return source.permissions().hasPermission(new Permission.HasCommandLevel(source.getServer().operatorUserPermissions().level()));
        //#else
        //$$ return source.hasPermission(source.getServer().operatorUserPermissionLevel());
        //#endif
    }

    private static int display(CommandContext<CommandSourceStack> context) {
        sendFeedback(
                context.getSource(),
                proxyText(),
                false
        );

        return 1;
    }

    private static int enable(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (config.enabled) {
            sendFeedback(
                    source,
                    Component.literal("Proxy already enabled"),
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
                Component.literal("Proxy enabled, restart the server to apply"),
                true
        );
        return 1;
    }

    private static int disable(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!config.enabled) {
            sendFeedback(
                    source,
                    Component.literal("Proxy already disabled"),
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
                Component.literal("Proxy disabled, restart the server to apply"),
                true
        );
        return 1;
    }

    private static int port(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        int                port   = IntegerArgumentType.getInteger(context, "port");
        config.port = (short) port;
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Component.literal("Proxy port set to " + port),
                true
        );
        sendFeedback(
                source,
                proxyText(),
                true
        );
        return 1;
    }

    private static int host(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String             host   = StringArgumentType.getString(context, "host");
        config.host = host;
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Component.literal("Proxy host set to " + host),
                true
        );
        sendFeedback(
                source,
                proxyText(),
                true
        );
        return 1;
    }

    private static int type(CommandContext<CommandSourceStack> context, String type) {
        CommandSourceStack source = context.getSource();
        config.type = ProxyConfig.ProxyType.valueOf(type);
        if (saveConfig(source)) {
            return 0;
        }
        sendFeedback(
                source,
                Component.literal("Proxy type set to " + type),
                true
        );
        sendFeedback(
                source,
                proxyText(),
                true
        );
        return 1;
    }

    private static int ping(CommandContext<CommandSourceStack> context) {
        Set<String> hosts = Sets.newHashSet();
        //#if MC>=12109
        com.mojang.authlib.Environment env = com.ayakacraft.authlibproxyforserver.mixin.YggdrasilAuthenticationServiceAccessor.determineEnvironment();
        hosts.add(env.sessionHost());
        hosts.add(env.servicesHost());
        hosts.add(env.profilesHost());
        //#elseif MC>=12006
        //$$ com.mojang.authlib.Environment env = com.ayakacraft.authlibproxyforserver.mixin.YggdrasilAuthenticationServiceAccessor.determineEnvironment();
        //$$ hosts.add(env.sessionHost());
        //$$ hosts.add(env.servicesHost());
        //#elseif MC>=11600
        //$$ com.mojang.authlib.Environment env = com.ayakacraft.authlibproxyforserver.mixin.YggdrasilAuthenticationServiceAccessor.determineEnvironment();
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
    private static boolean saveConfig(CommandSourceStack source) {
        try {
            AuthlibProxyForServer.saveConfig(config.validate());
        } catch (IOException e) {
            source.sendFailure(Component.literal("Error saving config"));
            LOGGER.error("Error saving config", e);
            return true;
        } catch (ProxyConfig.InvalidProxyConfigException e) {
            source.sendFailure(Component.literal(e.getMessage()));
            LOGGER.error(e);
            return true;
        }
        return false;
    }

    private static Component proxyText() {
        return Component.literal("Proxy for authlib: " + proxy.toString());
    }

    private static void sendFeedback(CommandSourceStack source, Component txt, boolean broadcastToOps) {
        //#if MC>=12000
        source.sendSuccess(() -> txt, broadcastToOps);
        //#else
        //$$ source.sendSuccess(txt, broadcastToOps);
        //#endif
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("authproxy")
                        .requires(AuthProxyCommand::hasOpPermissionLevel)
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

        private static Component packetLossStatusText(int packetsReceived) {
            double         packetLoss = 1D - (double) packetsReceived / AuthProxyCommand.TCPING_TIMES;
            ChatFormatting colour;
            if (packetLoss > 0.8D) {
                colour = ChatFormatting.RED;
            } else if (packetLoss > 0.2D) {
                colour = ChatFormatting.YELLOW;
            } else {
                colour = ChatFormatting.GREEN;
            }
            return Component.literal(String.format("%d packets transmitted, %d packets received, ", TCPING_TIMES, packetsReceived))
                    .append(Component.literal(String.format("%.1f%%", packetLoss * 100)).withStyle(colour))
                    .append(Component.literal(" packet loss"));
        }

        private static Component averagePingText(long ping) {
            ChatFormatting colour;
            if (ping > 1000) {
                colour = ChatFormatting.LIGHT_PURPLE;
            } else if (ping >= 800) {
                colour = ChatFormatting.RED;
            } else if (ping >= 300) {
                colour = ChatFormatting.YELLOW;
            } else {
                colour = ChatFormatting.GREEN;
            }
            return Component.literal("Average ping: ")
                    .append(Component.literal(ping + "ms").withStyle(colour));
        }

        private final Set<String> hosts;

        private final CommandSourceStack source;

        private final Map<String, Pair<Long, Integer>> results;

        private long startTimeMillis;

        public TcpingThread(Set<String> hosts, CommandSourceStack source) {
            super("TCPing Thread");
            this.hosts = hosts;
            this.source = source;
            this.results = Maps.newHashMap();
        }

        private synchronized void saveResult(String host, Pair<Long, Integer> res) {
            results.put(host, res);
            if (results.size() >= hosts.size()) {
                results.forEach(this::sendResult);
                sendFeedback(source, Component.literal("Ping finished after " + (System.currentTimeMillis() - startTimeMillis) + "ms"), false);
            }
        }

        private void sendResult(String h, Pair<Long, Integer> r) {
            sendFeedback(
                    source,
                    Component.literal(String.format("Ping for '%s':", h)),
                    false
            );
            sendFeedback(
                    source,
                    packetLossStatusText(r.getSecond()),
                    false
            );
            if (r.getSecond() > 0) {
                sendFeedback(
                        source,
                        averagePingText(r.getFirst() / r.getSecond()),
                        false
                );
            }
        }

        @Override
        public void run() {
            sendFeedback(source, Component.literal("Ping started"), false);
            startTimeMillis = System.currentTimeMillis();
            hosts.forEach(host -> new Thread(() ->
                    saveResult(host, NetworkUtils.tcpingMultiple(URI.create(host), proxy, TCPING_TIMES))).start()
            );
        }

    }

}
