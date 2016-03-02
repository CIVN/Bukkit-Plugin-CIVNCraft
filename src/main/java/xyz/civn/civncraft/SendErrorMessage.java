package xyz.civn.civncraft;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SendErrorMessage extends JavaPlugin implements Listener
{
	public static String prefix = Prefix.prefix;

	public static void SenderIsNotPlayer(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "You shouldn't execute this command from console!");
	}

	public static void ExtraArguments(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "Execute this command again after you removed extra arguments!");
	}

	public static void HubWasNotSet(CommandSender sender)
	{
		sender.sendMessage(Prefix.prefix + C.RED + "Hub wasn't set!");
		sender.sendMessage(Prefix.prefix + C.RED + "Please set hub!");
	}

	public static void DoesNotExist(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.BLUE + args[0] + C.RED + " doesn't exist!");
	}

	public static void DoesNotExistInContents(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.BLUE + args[0] + C.RED + " doesn't exist in Contents!");
	}

	public static void Failed(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "Failed!");
	}

	public static void PlayerIsNotIn(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.BLUE + args[1] + C.RED + " isn't in this server!");
	}
}
