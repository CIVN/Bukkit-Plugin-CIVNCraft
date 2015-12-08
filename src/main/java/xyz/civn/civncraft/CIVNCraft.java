package xyz.civn.civncraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CIVNCraft extends JavaPlugin implements Listener
{
	static final ChatColor B        = ChatColor.BOLD;
	static final ChatColor I        = ChatColor.ITALIC;
	static final ChatColor M        = ChatColor.MAGIC;
	static final ChatColor R        = ChatColor.RESET;
	static final ChatColor S        = ChatColor.STRIKETHROUGH;
	static final ChatColor U        = ChatColor.UNDERLINE;
	static final ChatColor AQUA     = ChatColor.AQUA;
	static final ChatColor BLACK    = ChatColor.BLACK;
	static final ChatColor BLUE     = ChatColor.BLUE;
	static final ChatColor D_AQUA   = ChatColor.DARK_AQUA;
	static final ChatColor D_BLUE   = ChatColor.DARK_BLUE;
	static final ChatColor D_GRAY   = ChatColor.DARK_GRAY;
	static final ChatColor D_GREEN  = ChatColor.DARK_GREEN;
	static final ChatColor D_PURPLE = ChatColor.DARK_PURPLE;
	static final ChatColor D_RED    = ChatColor.DARK_RED;
	static final ChatColor GOLD     = ChatColor.GOLD;
	static final ChatColor GRAY     = ChatColor.GRAY;
	static final ChatColor GREEN    = ChatColor.GREEN;
	static final ChatColor L_PURPLE = ChatColor.LIGHT_PURPLE;
	static final ChatColor RED      = ChatColor.RED;
	static final ChatColor WHITE    = ChatColor.WHITE;
	static final ChatColor YELOW    = ChatColor.YELLOW;

	static String prefix = GRAY + "[" + B + BLUE + "CIVNCraft" + R + GRAY + "] ";

	static String UP = RED + "Unnecessary parameters!";
	static String FC = RED + "Can't run this command from console!";

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		return;
	}

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		FileConfiguration cf = this.getConfig();

		/*「/hub」コマンド*/
		if (cmd.getName().equalsIgnoreCase("hub"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき*/
				sender.sendMessage(prefix + FC);
				return false;
			}

			if (args.length == 0)
			{
				String hubW =  cf.getString("hub.world");
				double hubX =  cf.getDouble("hub.x");
				double hubY =  cf.getDouble("hub.y");
				double hubZ =  cf.getDouble("hub.z");
				float hubYAW = (float) cf.getDouble("hub.yaw");
				float hubPITCH = (float) cf.getDouble("hub.pitch");

				/*プレイヤーを取得*/
				Player p = (Player) sender;

				/*ハブが設定されていない（ワールドがnull）だったとき*/
				if (hubW == null)
				{
					sender.sendMessage(prefix + RED + "Hub wasn't set!");
					sender.sendMessage(prefix + RED + "Please set hub!");
					return false;
				}

				/*configから取得した座標をロケーションにし、そこにプレイヤーをテレポート*/
				Location HUB = new Location (Bukkit.getWorld (hubW), hubX, hubY, hubZ, hubYAW, hubPITCH);
				p.teleport(HUB);
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + UP);
			return false;

		}

		/*「/sethub」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("sethub"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき*/
				sender.sendMessage(prefix + FC);
				return false;
			}

			if (args.length == 0)
			{
				/*プレイヤーのx~pitch座標を取得*/
				Player p = (Player) sender;

				double rX = p.getLocation().getX();
				double rY = p.getLocation().getY();
				double rZ = p.getLocation().getZ();
				double rYAW = p.getLocation().getYaw();
				double rPITCH = p.getLocation().getPitch();

				/*取得した座標をコンフィグファイルに保存*/
				cf.set("hub.world", p.getWorld().getName());
				cf.set("hub.x", rX);
				cf.set("hub.y", rY);
				cf.set("hub.z", rZ);
				cf.set("hub.yaw", rYAW);
				cf.set("hub.pitch", rPITCH);

				/*コンフィグファイルを保存、リロード*/
				this.saveConfig();
				this.reloadConfig();

				/*報告*/
				Bukkit.getServer().broadcastMessage(prefix + AQUA + "Hub location has changed by " + RED + sender.getName() + AQUA + "!");
				Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> You can see now hub location!");
				return true;
			}

			else if (args.length == 4)
			{
				World w = Bukkit.getWorld(args[0]);

				if (w != null)
				{
					/*入力した座標をコンフィグファイルに保存*/
					cf.set("hub.world", args[0]);

					try
					{
						Double rX = Double.parseDouble (args[1]);
						Double rY = Double.parseDouble (args[2]);
						Double rZ = Double.parseDouble (args[3]);
					}

					catch (NumberFormatException e)
					{
						sender.sendMessage(prefix + RED + "Please enter the coordinates!");
						return false;
					}

					Double rX = Double.parseDouble (args[1]);
					Double rY = Double.parseDouble (args[2]);
					Double rZ = Double.parseDouble (args[3]);

					/*取得した座標をコンフィグファイルに保存*/
					cf.set("hub.x", rX);
					cf.set("hub.y", rY);
					cf.set("hub.z", rZ);
					cf.set("hub.yaw", 0);
					cf.set("hub.pitch", 0);
					this.saveConfig();
					this.reloadConfig();

					/*報告*/
					Bukkit.getServer().broadcastMessage(prefix + AQUA + "Hub location has changed by " + RED + sender.getName() + AQUA + "!");
					Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> You can see now hub location!");
					return true;
				}

				/*入力したワールドが存在しないとき*/
				Bukkit.getServer().broadcastMessage(prefix + GOLD + args[0] + " doesn't exist!");
				return false;
			}

			else if (args.length == 6)
			{
				World w = Bukkit.getWorld (args[0]);

				if (w != null)
				{
					/*入力した座標をコンフィグファイルに保存*/
					cf.set("hub.world", args[0]);

					try
					{
						Double rX = Double.parseDouble (args[1]);
						Double rY = Double.parseDouble (args[2]);
						Double rZ = Double.parseDouble (args[3]);
						Double rYaw = Double.parseDouble (args[4]);
						Double rPicth = Double.parseDouble (args[5]);
					}

					catch (NumberFormatException e)
					{
						sender.sendMessage(prefix + RED + "Please enter the coordinates!");
						return false;
					}

					Double rX = Double.parseDouble (args[1]);
					Double rY = Double.parseDouble (args[2]);
					Double rZ = Double.parseDouble (args[3]);
					Double rYaw = Double.parseDouble (args[4]);
					Double rPitch = Double.parseDouble (args[5]);

					cf.set("hub.x", rX);
					cf.set("hub.y", rY);
					cf.set("hub.z", rZ);
					cf.set("hub.yaw", rYaw);
					cf.set("hub.pitch", rPitch);
					this.saveConfig();
					this.reloadConfig();

					/*報告*/
					Bukkit.getServer().broadcastMessage(prefix + AQUA + "Hub location has changed by " + RED + sender.getName() + AQUA + "!");
					Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> You can see now hub location!");
					return true;
				}

				/*入力したワールドが存在しないとき*/
				Bukkit.getServer().broadcastMessage(prefix + GOLD + args[0] + " doesn't exist!");
				return false;
			}

			sender.sendMessage(prefix);
			sender.sendMessage(GOLD + "Example: " + AQUA + "/sethub <WorldName> <X> <Y> <Z> or");
			sender.sendMessage(         AQUA + "/sethub <WorldName> <X> <Y> <Z> <Yaw> <Pitch>");
			return false;

		}

		/*「/hubloc」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("hubloc"))
		{
			if (args.length == 0)
			{
				/*コンフィグファイルからハブのx~pitch座標を取得*/
				String W = cf.getString("hub.world");
				double X = cf.getDouble("hub.x");
				double Y = cf.getDouble("hub.y");
				double Z = cf.getDouble("hub.z");
				float YAW = (float) cf.getDouble("hub.yaw");
				float PITCH = (float) cf.getDouble("hub.pitch");

				/*報告*/
				sender.sendMessage(prefix);
				sender.sendMessage(GREEN + "World: " + AQUA + W);
				sender.sendMessage(GOLD + "X: " + AQUA + X);
				sender.sendMessage(GOLD + "Y: " + AQUA + Y);
				sender.sendMessage(GOLD + "Z: " + AQUA + Z);
				sender.sendMessage(GOLD + "Yaw: " + AQUA + YAW);
				sender.sendMessage(GOLD + "Pitch: " + AQUA + PITCH);
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + UP);
			return false;

		}

		/*「/seppuku」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("seppuku"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき*/
				sender.sendMessage(prefix + FC);
				return false;
			}

			if (args.length == 0)
			{
				/*プレイヤーを取得*/
				Player p = (Player) sender;

				/*取得したプレイヤーのヘルスを0にする（殺す）*/
				p.setHealth(0);
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + UP);
			return false;

		}

		/*「/asiba」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("asiba"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(prefix + FC);
				return false;
			}

			/*コマンドのパラメータが1か2だったとき*/
			if (args.length == 1 | args.length == 2)
			{
				/*プレイヤーとプレイヤーの位置を取得*/
				Player p = (Player) sender;
				Location l = p.getLocation();

				if (args[0].equalsIgnoreCase("to"))
				{
					try
					{
						Integer.parseInt(args[1]);
					}

					/*数値ではなかったとき*/
					catch (NumberFormatException e)
					{
						String block = args[1].toUpperCase();

						/*args[1]からマテリアルを取得*/
						Material m = Material.getMaterial(block);

						/*取得に失敗したら終了*/
						if (m == null)
						{
							sender.sendMessage(prefix + RED + args[1] + " doesn't exist!");
							sender.sendMessage(GREEN + "     >>>Is it a Block?");
							sender.sendMessage(GREEN + "     >>>Isn't it wrong Name?");
							return false;
						}

						for (;;)
						{
							l.setY(l.getY() - 1);

							if (l.getBlock().getType() != Material.AIR)
							{
								return true;
							}

							l.getBlock().setType(m);
						}
					}

					Integer b = new Integer (args[1]);
					int block = (int) b;

					Material m = Material.getMaterial(block);

					/*取得に失敗したら終了*/
					if (m == null)
					{
						sender.sendMessage(prefix + RED + args[1] + " doesn't exist!");
						sender.sendMessage(GREEN + "     >>>Is it a Block?");
						sender.sendMessage(GREEN + "     >>>Isn't it wrong ID?");
						return false;
					}

					for (;;)
					{
						l.setY(l.getY() - 1);

						if (l.getBlock().getType() != Material.AIR)
						{
							return true;
						}

						l.getBlock().setType(m);
					}
				}

				/*args[0]が数値かチェック*/
				try
				{
					Integer.parseInt(args[0]);
				}

				/*数値ではなかったときStringとして進行する*/
				catch (NumberFormatException e)
				{
					String block = args[0].toUpperCase();

					/*args[0]からマテリアルを取得*/
					Material m = Material.getMaterial(block);

					/*取得に失敗したらfalseを返す*/
					if (m == null)
					{
						sender.sendMessage(prefix + RED + args[0] + " doesn't exist!");
						sender.sendMessage(GREEN + "     >>>Is it a Block?");
						sender.sendMessage(GREEN + "     >>>Isn't it wrong Name?");
						return false;
					}

					/*コマンドパラメータが2のとき*/
					if (args.length == 2)
					{
						/*args[1]が数値かチェック*/
						try
						{
							Integer.parseInt(args[1]);
						}

						/*数値ではなかったとき*/
						catch (NumberFormatException er)
						{
							sender.sendMessage(prefix + RED + "Invaild parameters!");
							return false;
						}

						/*args[1]をint型に変換*/
						Integer am = new Integer (args[1]);
						int amount = (int) am;

						/*ブロックをamount(args[1])の高さでセットする*/
						for (int i = 1; i <= amount; i++)
						{
							l.setY(l.getY() - 1);
							l.getBlock().setType(m);
						}

						return true;

					}

					/*ブロックをプレイヤーのY座標 - 1の位置にセットする*/
					l.setY(l.getY() - 1);
					l.getBlock().setType(m);
					return true;

				}

				/*args[0]をint型に変換*/
				Integer b = new Integer (args[0]);
				int block = (int) b;

				Material m = Material.getMaterial(block);

				/*マテリアルの取得に失敗したとき*/
				if (m == null)
				{
					sender.sendMessage(prefix + RED + "ItemID [" + AQUA + block + RED + "]" + " doesn't exist!");
					sender.sendMessage(GREEN + "     >>>Is it a Block?");
					sender.sendMessage(GREEN + "     >>>Isn't it wrong ID?");
					return false;
				}

				/*コマンドパラメータが2のとき*/
				if (args.length == 2)
				{
					/*args[1]が数値かチェック*/
					try
					{
						Integer.parseInt(args[1]);
					}

					/*数値ではなかったとき終了*/
					catch (NumberFormatException e)
					{
						sender.sendMessage(prefix + RED + "Invaild parameters!");
						return false;
					}

					/*args[1]をint型に変換*/
					Integer am = new Integer (args[1]);
					int amount = (int) am;

					/*ブロックをamount(args[1])の高さでセットする*/
					for (int i = 1; i <= amount; i++)
					{
						l.setY(l.getY() - 1);
						l.getBlock().setType(m);
					}

					return false;

				}

				/*ブロックをプレイヤーのY座標 - 1の位置にセットする*/
				l.setY(l.getY() - 1);
				l.getBlock().setType(m);
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + UP);
			return false;

		}

		/*「/civn」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("civn"))
		{
			if (args.length == 0)
			{
				sender.sendMessage(prefix);
				sender.sendMessage(GOLD + "Author: " + D_RED + "CIVN");
				sender.sendMessage(GOLD + "Version: " + D_BLUE + "1.0");
				return true;
			}

			else if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("?"))
			{
				Player p = (Player) sender;

				if (p.isOp())
				{
					sender.sendMessage(prefix);
					sender.sendMessage(GOLD + "/civn: " + AQUA + "Show infomation!");
					sender.sendMessage(GOLD + "/hub: " + AQUA + "Go to hub!");
					sender.sendMessage(GOLD + "/sethub: " + AQUA + "Set hub at your location!");
					sender.sendMessage(GOLD + "/sethub <World> <X> <Y> <Z> or");
					sender.sendMessage(GOLD + "/sethub <World> <X> <Y> <Z> <YAW> <PITCH>: " + AQUA + "Set hub at that location!");
					sender.sendMessage(GOLD + "/hubl: " + AQUA + "Show now hub location!");
					sender.sendMessage(GOLD + "/seppuku: " + AQUA + "Die...");
					sender.sendMessage(GOLD + "/asiba <BlockID> <height>: " + AQUA + "Create asiba!");
					sender.sendMessage(GOLD + "/asiba to <BlockID>: " + AQUA + "Create asiba to hit the bottom!");
					return true;
				}

				sender.sendMessage(GOLD + "/civn: " + AQUA + "Show infomation!");
				sender.sendMessage(GOLD + "/hub: " + AQUA + "Go to hub!");
				sender.sendMessage(GOLD + "/asiba <BlockID> <height>: " + AQUA + "Create asiba!");
				sender.sendMessage(GOLD + "/asiba to <BlockID>: " + AQUA + "Create asiba to hit the bottom!");
				sender.sendMessage(GOLD + "/seppuku: " + AQUA + "Die...");
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + UP);
			return false;
		}

		return false;

}

	/*プレイヤーが死んだときのイベント*/
	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event)
	{
		if (!(this.getConfig().getString("Event.PlayerDeath").equalsIgnoreCase("true")))
		{
			return;
		}

		String name = event.getEntity().getName();

		Player p = event.getEntity().getPlayer();
		Player k = p.getKiller();

		/*殺した人がいない場合*/
		if (k == null)
		{
			event.setDeathMessage(prefix + D_GREEN + name + GOLD + " died!");
			return;
		}

		event.setDeathMessage(prefix + D_GREEN + name + GOLD + " was killed by " + RED + k.getName());
		return;

	}

	/*プレイヤーのレベルが変化したときのイベント*/
	@EventHandler
	public void onLevelUp (PlayerLevelChangeEvent event)
	{
		if (!(this.getConfig().getString("Event.ChangeLevel").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();
		String pn = p.getName();
		int newl = event.getNewLevel();
		int oldl = event.getOldLevel();

		/*レベルが上がった時の処理*/
		if (newl > oldl)
		{
			Bukkit.broadcastMessage(prefix + D_GREEN + pn + GOLD + " leveled up " + RED + "Lv." + oldl + GOLD + " to " + RED + "Lv." + newl + GOLD + "!");
		}

		/*レベルが下がった時の処理*/
		else if (newl < oldl)
		{
			Bukkit.broadcastMessage(prefix + D_GREEN + pn + GOLD + " leveled down " + RED + "Lv." + oldl + GOLD + " to " + RED + "Lv." + newl + GOLD + "!");
		}

		return;

	}

	/*プレイヤーの経験値が変化した時のイベント*/
	@EventHandler
	public void onExpUp (PlayerExpChangeEvent event)
	{
		if (!(this.getConfig().getString("Event.ChangeEXP").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();
		String pn = p.getName();
		int exp = event.getAmount();

		Bukkit.broadcastMessage(prefix + D_GREEN + pn + GOLD + " got " + RED + exp + GOLD + " EXP!");
		return;

	}

	/*プレイヤーがベッドで寝たときのイベント*/
	@EventHandler
	public void onHealth (PlayerBedLeaveEvent event)
	{
		if (!(this.getConfig().getString("Event.BedHeal").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();

		p.setHealth(20);
		p.setFoodLevel(20);

		Bukkit.broadcastMessage(prefix + D_GREEN + p.getName() + GOLD + " has completely recovered!");
		return;
	}

	/*プレイヤーがゲームに参加した時のイベント*/
	@EventHandler
	public void onJoin (PlayerJoinEvent event)
	{
		if (!(this.getConfig().getString("Event.JoinMsg").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();

		event.setJoinMessage(prefix + D_GREEN + p.getName() + GOLD + " has joined.");
		return;
	}

	/*プレイヤーがゲームを離脱した時のイベント*/
	@EventHandler
	public void onLeave (PlayerQuitEvent event)
	{
		if (!(this.getConfig().getString("Event.LeaveMsg").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();

		event.setQuitMessage(prefix + D_GREEN + p.getName() + GOLD + " has left.");
		return;
	}
}