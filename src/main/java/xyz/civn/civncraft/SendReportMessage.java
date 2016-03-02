package xyz.civn.civncraft;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SendReportMessage extends JavaPlugin implements Listener
{
	public static String prefix = Prefix.prefix;

	public static void HubWasChanged(CommandSender sender)
	{
		Bukkit.getServer().broadcastMessage(prefix + C.AQUA + "Hub location has changed by " + C.RED + sender.getName() + C.AQUA + "!");
		Bukkit.getServer().broadcastMessage(prefix + C.GREEN + "/hubloc " + C.AQUA + ">>> You can see now hub location!");
	}

	public static void ShowHubLocation(CommandSender sender, FileConfiguration cf)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GREEN + "World: " + C.AQUA + cf.getString("hub.world"));
		sender.sendMessage(C.GOLD + "X: " + C.AQUA + cf.getDouble("hub.x"));
		sender.sendMessage(C.GOLD + "Y: " + C.AQUA + cf.getDouble("hub.y"));
		sender.sendMessage(C.GOLD + "Z: " + C.AQUA + cf.getDouble("hub.z"));
		sender.sendMessage(C.GOLD + "Yaw: " + C.AQUA + (float) cf.getDouble("hub.yaw"));
		sender.sendMessage(C.GOLD + "Pitch: " + C.AQUA + (float) cf.getDouble("hub.pitch"));
	}

	public static void ShowInfomation(CommandSender sender, CIVNCraft civnCraft)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GOLD + "Author: " + C.RED + "" + C.B + "CIVN");
		sender.sendMessage(C.GOLD + "Version: " + C.AQUA + "" + C.B + civnCraft.getDescription().getVersion());
		sender.sendMessage(C.GOLD + "Website: " + C.BLUE + "" + C.B + civnCraft.getDescription().getWebsite());
	}

	public static void ShowPlayerLocation(CommandSender sender, Player player, double[] l, String string)
	{
		sender.sendMessage(prefix + string + player.getName() + C.D_GREEN + " is in");
		sender.sendMessage(C.GOLD + "X: " + C.AQUA + l[0]);
		sender.sendMessage(C.GOLD + "Y: " + C.AQUA + l[1]);
		sender.sendMessage(C.GOLD + "Z: " + C.AQUA + l[2]);
		sender.sendMessage(C.GOLD + "Yaw: " + C.AQUA + l[3]);
		sender.sendMessage(C.GOLD + "Pitch: " + C.AQUA + l[4]);
	}
}
