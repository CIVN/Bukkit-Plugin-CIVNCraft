package xyz.civn.civncraft;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SendHelpMessage extends JavaPlugin implements Listener
{
	public static String prefix = Prefix.prefix;

	public static void ShowHelp(CommandSender sender)
	{
		sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + "Show infomation!");
		sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + "Go to hub!");
		sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + "Create asiba!");
		sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + "Create asiba to hit the bottom!");
		sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + "Die...");
	}

	public static void ShowOpHelp(CommandSender sender)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + "Show infomation!");
		sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + "Go to hub!");
		sender.sendMessage(C.GOLD + "/sethub: " + C.AQUA + "Set hub at your location!");
		sender.sendMessage(C.GOLD + "/sethub <World> <X> <Y> <Z> or");
		sender.sendMessage(C.GOLD + "/sethub <World> <X> <Y> <Z> <YAW> <PITCH>: " + C.AQUA + "Set hub at that location!");
		sender.sendMessage(C.GOLD + "/hubl: " + C.AQUA + "Show now hub location!");
		sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + "Die...");
		sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + "Create asiba!");
		sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + "Create asiba to hit the bottom!");
	}

	public static void ShowPdataHelp(CommandSender sender)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.D_GREEN + "/pdata help: " + C.BLUE + "Show help.");
		sender.sendMessage(C.D_GREEN + "/ops: " + C.BLUE + "Show online operators.");
		sender.sendMessage(C.D_GREEN + "/pdata <Content> <PlayerName>: " + C.BLUE + "Show players data");
		sender.sendMessage(C.GRAY + "====================");
		sender.sendMessage(C.D_AQUA + "" + C.B + "Contents");
	}
}
