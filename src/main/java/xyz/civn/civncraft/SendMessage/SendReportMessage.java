package xyz.civn.civncraft.SendMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.civn.civncraft.CIVNCraft;
import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;

public class SendReportMessage extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;
	private static String[] location = {"X: ", "Y: ", "Z: ", "Yaw: ", "Pitch: "};

	/*ハブの位置が変更された*/
	public static void HubWasChanged(CommandSender sender)
	{
		Bukkit.getServer().broadcastMessage(prefix + C.GOLD + "Hub location has changed by " + C.AQUA + sender.getName() + C.GOLD + ".");
	}

	/*プレイヤーがワールドを移動した*/
	public static void MovedWorld(Player player, String from, String to, String playerprefix)
	{
		Bukkit.getServer().broadcastMessage(prefix + playerprefix + player.getName() + C.GOLD + " has moved from " + C.GREEN + from + C.GOLD + " to " + C.GREEN + to + C.GOLD + ".");
	}

	/*ハブの位置を表示*/
	public static void ShowHubLocation(CommandSender sender, FileConfiguration cf)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GOLD + "World: " + C.AQUA + cf.getString("hub.world"));
		sender.sendMessage(C.GOLD + "X: " + C.AQUA + cf.getDouble("hub.x"));
		sender.sendMessage(C.GOLD + "Y: " + C.AQUA + cf.getDouble("hub.y"));
		sender.sendMessage(C.GOLD + "Z: " + C.AQUA + cf.getDouble("hub.z"));
		sender.sendMessage(C.GOLD + "Yaw: " + C.AQUA + (float) cf.getDouble("hub.yaw"));
		sender.sendMessage(C.GOLD + "Pitch: " + C.AQUA + (float) cf.getDouble("hub.pitch"));
	}

	/*情報が表示*/
	public static void ShowInfomation(CommandSender sender, CIVNCraft main)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GOLD + "Author: " + C.RED + "" + C.B + "CIVN");
		sender.sendMessage(C.GOLD + "Version: " + C.AQUA + main.getDescription().getVersion());
		sender.sendMessage(C.GOLD + "Website: " + C.BLUE + main.getDescription().getWebsite());
	}

	/*プレイヤーの位置を表示*/
	public static void ShowPlayerLocation(CommandSender sender, Player player, double[] l, String playerprefix)
	{
		sender.sendMessage(prefix + playerprefix + player.getName() + C.GOLD + " is in");

		for (int i = 0; i <= 4; i++)
		{
			sender.sendMessage(C.GOLD + location[i] + C.AQUA + l[i]);
		}
	}

	/*TODO: 他の情報も表示できるようにする*/
	/*インベントリのアイテムと個数を表示*/
	public static void ShowItem(CommandSender sender, ItemStack is, int i)
	{
		sender.sendMessage(C.GOLD + String.valueOf(i) + ": " + C.AQUA + is.getType().name() + C.GREEN + " -" + is.getAmount());
	}

	/*プレイヤーがアイテムを一つも持っていない*/
	public static void HasNoItems(CommandSender sender, Player player, String playerprefix)
	{
		sender.sendMessage(playerprefix + player.getName() + C.GOLD + " has no items.");
	}

	/*エンティティの名前をリセットした*/
	public static void ResetName(Player damager)
	{
		damager.sendMessage(prefix + C.GOLD + "You reset his name.");
	}

	/*エンティティの名前を変更した*/
	public static void ChangedName(String name, Player damager)
	{
		damager.sendMessage(prefix + C.GOLD + "You changed his name. => " + C.AQUA + name);
	}

	/*エンティティの名前を変更した => Grumm or Dinnerbone*/
	public static void FlippedEntity(Player damager)
	{
		damager.sendMessage(prefix + C.GOLD + "You flipped over him.");
	}
}