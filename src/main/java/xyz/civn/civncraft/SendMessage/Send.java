package xyz.civn.civncraft.SendMessage;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.civn.civncraft.CIVNCraft;
import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;

public class Send extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;
	private static CIVNCraft main;

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
		sender.sendMessage(prefix + C.RED + "Hub wasn't set!");
		sender.sendMessage(prefix + C.RED + "Please set hub!");
	}

	/*存在しない*/
	public static void WorldDoesNotExistInServer(CommandSender sender, String[] args)
	{
		sender.sendMessage(prefix + C.GREEN + args[0] + C.RED + " doesn't exist!");
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
		sender.sendMessage(prefix + C.GREEN + world.getName() + C.RED + " doesn't exist in this server!");
	}

	public static void FailedCreateFile()
	{
		main.getLogger().info(main.getDescription().getPrefix() + "ファイルの生成に失敗しました。");
		main.getLogger().info(main.getDescription().getPrefix() + "Failed creating a file.");
	}
}