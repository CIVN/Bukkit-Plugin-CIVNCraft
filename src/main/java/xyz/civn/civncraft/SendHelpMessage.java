package xyz.civn.civncraft;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SendHelpMessage extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;

	public static void ShowHelp(CommandSender sender, CIVNCraft main)
	{
		sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + main.getDescription().getCommands().get("civn"));
		sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + main.getDescription().getCommands().get("hub"));
		sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + main.getDescription().getCommands().get("asiba"));
		sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + main.getDescription().getCommands().get("asiba"));
		sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + main.getDescription().getCommands().get("seppuku"));
	}

	public static void ShowOpHelp(CommandSender sender, CIVNCraft main)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + main.getDescription().getCommands().get("civn"));
		sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + main.getDescription().getCommands().get("hub"));
		sender.sendMessage(C.GOLD + "/sethub: " + C.AQUA + main.getDescription().getCommands().get("sethub"));
		sender.sendMessage(C.GOLD + "/sethub <World> <X> <Y> <Z> or");
		sender.sendMessage(C.GOLD + "/sethub <World> <X> <Y> <Z> <YAW> <PITCH>: " + C.AQUA + main.getDescription().getCommands().get("sethub"));
		sender.sendMessage(C.GOLD + "/hubl: " + C.AQUA + main.getDescription().getCommands().get("hubloc"));
		sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + main.getDescription().getCommands().get("seppuku"));
		sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + main.getDescription().getCommands().get("asiba"));
		sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + main.getDescription().getCommands().get("asiba"));
	}

	public static void ShowPdataHelp(CommandSender sender, String[] contents, CIVNCraft main)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.D_GREEN + "/pdata help: " + C.BLUE + main.getDescription().getCommands().get("pdata"));
		sender.sendMessage(C.D_GREEN + "/ops: " + C.BLUE + main.getDescription().getCommands().get("ops"));
		sender.sendMessage(C.D_GREEN + "/pdata <Content> <PlayerName>: " + C.BLUE + main.getDescription().getCommands().get("pdata"));
		sender.sendMessage(C.GRAY + "====================");
		sender.sendMessage(C.D_AQUA + "" + C.B + "Contents");

		for (String c : contents)
		{
			sender.sendMessage(C.BLUE + "- " + C.AQUA + c);
		}
	}
}
