package xyz.civn.civncraft;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SendReportMessage extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;
	private static String[] location = {"X: ", "Y: ", "Z: ", "Yaw: ", "Pitch: "};

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
		sender.sendMessage(C.GOLD + "Version: " + C.AQUA + civnCraft.getDescription().getVersion());
		sender.sendMessage(C.GOLD + "Website: " + C.BLUE + civnCraft.getDescription().getWebsite());
	}

	public static void ShowPlayerLocation(CommandSender sender, Player player, double[] l, String playerprefix)
	{
		sender.sendMessage(prefix + playerprefix + player.getName() + C.D_GREEN + " is in");

		for (int i = 0; i <= 4; i++)
		{
			sender.sendMessage(C.GOLD + location[i] + C.AQUA + l[i]);
		}
	}
}
