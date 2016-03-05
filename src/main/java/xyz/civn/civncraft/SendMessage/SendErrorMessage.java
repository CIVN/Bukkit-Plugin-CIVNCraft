package xyz.civn.civncraft.SendMessage;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;

public class SendErrorMessage extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;

	/*コマンド実行元がプレイヤーではない*/
	public static void SenderIsNotPlayer(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "You shouldn't execute this command from console!");
	}

	/*余分な引数がある*/
	public static void ExtraArguments(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "Execute this command again after you removed extra arguments!");
	}

	/*不足している引数がある*/
	public static void MissingArguments(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "Execute this command again after you added some arguments!");
	}

	/*ハブがセットされていない*/
	public static void HubWasNotSet(CommandSender sender)
	{
		sender.sendMessage(Prefix.prefix + C.RED + "Hub wasn't set!");
		sender.sendMessage(Prefix.prefix + C.RED + "Please set hub!");
	}

	/*存在しない*/
	public static void DoesNotExist(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.BLUE + args[0] + C.RED + " doesn't exist!");
	}

	/*pdataコンテンツ内に存在しない*/
	public static void DoesNotExistInContents(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.BLUE + args[0] + C.RED + " doesn't exist in Contents!");
	}

	/*失敗*/
	public static void Failed(CommandSender sender)
	{
		sender.sendMessage(prefix + C.RED + "Failed!");
	}

	/*プレイヤーがサーバーに存在しない*/
	public static void PlayerIsNotIn(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.BLUE + args[1] + C.RED + " isn't in this server!");
	}

	/*ワールドがサーバーに存在しない*/
	public static void WorldDoesNotExist(World world, CommandSender sender)
	{
		sender.sendMessage(prefix + C.BLUE + world.getName() + C.RED + " doesn't exist in this server!");
	}
}
