package com.comze_instancelabs.rulesrankup;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	public void onEnable(){
		getConfig().addDefault("rules.rule1", "&4<player>, here's the password: <password>");
		getConfig().addDefault("help.line1", "&4Yo <player>, enjoy the help section:");
		getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	HashMap<Player, String> ppass = new HashMap<Player, String>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("rules")){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player) sender;
			String pw = getRandomPassword();
			for (String s : getConfig().getConfigurationSection("rules.").getKeys(true)){
				sender.sendMessage(getConfig().getString("rules." + s).replaceAll("&", "�").replaceAll("<player>", sender.getName()).replaceAll("<password>", pw));
			}
			ppass.put(p, pw);
			return true;
		}if(cmd.getName().equalsIgnoreCase("help")){
			if(!(sender instanceof Player)){
				return true;
			}
			Player p = (Player) sender;
			for (String s : getConfig().getConfigurationSection("help.").getKeys(true)){
				sender.sendMessage(getConfig().getString("help." + s).replaceAll("&", "�").replaceAll("<player>", sender.getName()));
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("apply")){
			if(args.length > 0){
				if(!(sender instanceof Player)){
					return true;
				}
				if(!sender.hasPermission("rulesrankup.apply")){
					return true;
				}
				Player p = (Player) sender;
				String givenpw = args[0];
				if(!ppass.containsKey(p)){
					p.sendMessage(ChatColor.RED + "You supplied a wrong password! Check /rules ;)");
					return true;
				}
				if(ppass.get(p).equalsIgnoreCase(givenpw)){
					//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bungeeperms user " + p.getName() + " setgroup Friendly");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group set Friendly");
					p.sendMessage(ChatColor.GREEN + "You just ranked up to Friendly!");
					getServer().broadcastMessage(ChatColor.GREEN + p.getName() + ChatColor.GOLD + " joined the server for the first time!");
					return true;
				}
				p.sendMessage(ChatColor.RED + "You supplied a wrong password! Check /rules again ;)");
			}else{
				sender.sendMessage(ChatColor.RED + "Usage: /apply password");
			}
			return true;
		}
		return false;
	}
	
	public String getRandomPassword(){
		Random r = new Random();
		//String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String abc = "abcdefghijklmnopqrstuvwxyz";
		String ret = "";
		for(int i = 0; i < 4; i++){
			if(r.nextBoolean()){
				ret += r.nextInt(9);
			}else{
				ret += abc.charAt(r.nextInt(abc.length() - 1));
			}
		}
		return ret;
	}
}
