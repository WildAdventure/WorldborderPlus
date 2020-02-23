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

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.worldborderplus.file.Settings;
import com.gmail.filoghost.worldborderplus.file.WorldborderConfig;

import net.cubespace.yamler.YamlerConfigurationException;
import net.md_5.bungee.api.ChatColor;

public class WorldborderPlus extends JavaPlugin {
	
	public static WorldborderPlus plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		try {
			new Settings(this, "config.yml").init();
		} catch (Exception ex) {
			getLogger().log(Level.SEVERE, "Impossibile caricare config.yml!", ex);
			this.setEnabled(false);
			return;
		}
		
		Bukkit.getPluginManager().registerEvents(new EventListener(), this);
		new CommandHandler(this, "worldborder");
	}
	
	
	public static void saveSettings(CommandSender sender) {
		try {
			new Settings(plugin, "config.yml").save();
			sender.sendMessage(ChatColor.GREEN + "Configurazione salvata con successo.");
		} catch (YamlerConfigurationException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "Errore durante il salvataggio della configurazione.");
		}
	}
	
	public static void updateWorldborder(World world, WorldborderConfig config) {
		plugin.getLogger().info("Setting border of " + world.getName() + " to " + (config != null ? config.toString() : "default"));
		WorldBorder worldBorder = world.getWorldBorder();
		if (config != null) {
			worldBorder.setCenter(config.getCenterX(), config.getCenterX());
			worldBorder.setSize(config.getRadius() * 2);
			worldBorder.setDamageBuffer(0); // Damage instantly outside
			worldBorder.setDamageAmount(10);
		} else {
			worldBorder.reset();
		}
	}
	
	public static boolean isOutsideWorldborder(WorldBorder worldBorder, Location checkLocation, double restrictBlocks) {
		double radius = (worldBorder.getSize() / 2) - restrictBlocks;
		Location center = worldBorder.getCenter();
		return
			checkLocation.getX() > center.getX() + radius ||
			checkLocation.getX() < center.getX() - radius ||
			checkLocation.getZ() > center.getZ() + radius ||
			checkLocation.getZ() < center.getZ() - radius;
	}
}
