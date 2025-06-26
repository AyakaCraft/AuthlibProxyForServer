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

package com.ayakacraft.authlibproxyforserver.mixin;

import com.ayakacraft.authlibproxyforserver.commands.AuthProxyCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegister(
            //#if MC>=11900
            CommandManager.RegistrationEnvironment commandSelection, net.minecraft.command.CommandRegistryAccess commandBuildContext,
            //#elseif MC>=11600
            //$$ CommandManager.RegistrationEnvironment commandSelection,
            //#else
            //$$ boolean isDedicated,
            //#endif
            CallbackInfo ci
    ) {
        if (
            //#if MC>=11600
                commandSelection == CommandManager.RegistrationEnvironment.DEDICATED
            //#else
            //$$ isDedicated
            //#endif
        ) {
            AuthProxyCommand.register(this.dispatcher);
        }
    }

}
