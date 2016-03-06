package xyz.civn.civncraft.SendMessage;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.civn.civncraft.CIVNCraft;
import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;

public class SendHelpMessage extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;

	public static void ShowHelp(CommandSender sender, CIVNCraft main)
	{
		sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + main.getDescription().getCommands().get("civn").get("description"));
		sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + main.getDescription().getCommands().get("hub").get("description"));
		sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + main.getDescription().getCommands().get("asiba").get("description"));
		sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + main.getDescription().getCommands().get("asiba").get("description"));
		sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + main.getDescription().getCommands().get("seppuku").get("description"));
	}

	public static void ShowOpHelp(CommandSender sender, CIVNCraft main)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + main.getDescription().getCommands().get("civn").get("description"));
		sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + main.getDescription().getCommands().get("hub").get("description"));
		sender.sendMessage(C.GOLD + "/sethub: " + C.AQUA + main.getDescription().getCommands().get("sethub").get("description"));
		sender.sendMessage(C.GOLD + "/sethub <World> <X> <Y> <Z> or");
		sender.sendMessage(C.GOLD + "/sethub <World> <X> <Y> <Z> <YAW> <PITCH>: " + C.AQUA + main.getDescription().getCommands().get("sethub").get("description"));
		sender.sendMessage(C.GOLD + "/hubl: " + C.AQUA + main.getDescription().getCommands().get("hubloc").get("description"));
		sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + main.getDescription().getCommands().get("seppuku").get("description"));
		sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + main.getDescription().getCommands().get("asiba").get("description"));
		sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + main.getDescription().getCommands().get("asiba").get("description"));
	}

	public static void ShowPdataHelp(CommandSender sender, String[] contents, CIVNCraft main)
	{
		sender.sendMessage(prefix);
		sender.sendMessage(C.D_GREEN + "/pdata help: " + C.BLUE + "Show pdata help.");
		sender.sendMessage(C.D_GREEN + "/ops: " + C.BLUE + main.getDescription().getCommands().get("ops").get("description"));
		sender.sendMessage(C.D_GREEN + "/pdata <Content> <PlayerName>: " + C.BLUE + main.getDescription().getCommands().get("pdata").get("description"));
		sender.sendMessage(C.GRAY + "====================");
		sender.sendMessage(C.D_AQUA + "" + C.B + "Contents");

		for (String c : contents)
		{
			sender.sendMessage(C.BLUE + "- " + C.AQUA + c);
		}
	}
}
