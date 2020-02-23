/*
 * Copyright (c) 2020, Wild Adventure
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 4. Redistribution of this software in source or binary forms shall be free
 *    of all charges or fees to the recipient of this software.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gmail.filoghost.worldborderplus;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.worldborderplus.file.Settings;
import com.gmail.filoghost.worldborderplus.file.WorldborderConfig;

import net.md_5.bungee.api.ChatColor;
import wild.api.command.CommandFramework.Permission;
import wild.api.command.SubCommandFramework;

@Permission("worldborderplus.admin")
public class CommandHandler extends SubCommandFramework {

	public CommandHandler(JavaPlugin plugin, String label) {
		super(plugin, label);
	}

	@Override
	public void noArgs(CommandSender sender) {
		for (SubCommandDetails subCommand : getSubCommands()) {
			sender.sendMessage(ChatColor.RED + "/worldborder " + subCommand.getName() + (subCommand.getUsage() != null ? " " + subCommand.getUsage() : ""));
		}
	}
	
	@SubCommand("set")
	@SubCommandMinArgs(4)
	@SubCommandUsage("<worldName> <centerX> <centerZ> <radius>")
	public void set(CommandSender sender, String label, String[] args) {
		World world = Bukkit.getWorld(args[0]);
		double centerX = CommandValidate.getDouble(args[1]);
		double centerZ = CommandValidate.getDouble(args[2]);
		double radius = CommandValidate.getDouble(args[3]);
		
		CommandValidate.notNull(world, "Mondo non trovato.");
		
		WorldborderConfig worldborderConfig = new WorldborderConfig(centerX, centerZ, radius);
		Settings.worlds.put(world.getName(), worldborderConfig);
		
		WorldborderPlus.updateWorldborder(world, worldborderConfig);
		WorldborderPlus.saveSettings(sender);
	}
	
	@SubCommand("reset")
	@SubCommandMinArgs(1)
	@SubCommandUsage("<worldName>")
	public void reset(CommandSender sender, String label, String[] args) {
		World world = Bukkit.getWorld(args[0]);
		
		CommandValidate.notNull(world, "Mondo non trovato.");
		
		Settings.worlds.remove(world.getName());

		WorldborderPlus.updateWorldborder(world, null);
		WorldborderPlus.saveSettings(sender);
	}

}
