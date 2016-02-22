package xyz.civn.civncraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CIVNCraft extends JavaPlugin implements Listener
{
	static String prefix = C.GRAY + "[" + C.B + C.BLUE + C.B + "CIVNCraft" + C.R + C.GRAY + "] ";

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		FileConfiguration cf = this.getConfig();

		if (cmd.getName().equalsIgnoreCase("hub"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(prefix + ErrorMessage.FC);
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

				Player p = (Player) sender;

				/*ハブが設定されていない（ワールドがnull）だったとき*/
				if (hubW == null)
				{
					sender.sendMessage(prefix + C.RED + "Hub wasn't set!");
					sender.sendMessage(prefix + C.RED + "Please set hub!");
					return false;
				}

				/*configから取得した座標をロケーションにし、そこにプレイヤーをテレポート*/
				Location HUB = new Location (Bukkit.getWorld (hubW), hubX, hubY, hubZ, hubYAW, hubPITCH);
				p.teleport(HUB);
				return true;
			}


			sender.sendMessage(prefix + ErrorMessage.UP);
			return false;

		}

		else if (cmd.getName().equalsIgnoreCase("sethub"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(prefix + ErrorMessage.FC);
				return false;
			}

			if (args.length == 0)
			{
				/*プレイヤーの座標を取得*/
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
				Bukkit.getServer().broadcastMessage(prefix + C.AQUA + "Hub location has changed by " + C.RED + sender.getName() + C.AQUA + "!");
				Bukkit.getServer().broadcastMessage(prefix + C.GREEN + "/hubloc " + C.AQUA + ">>> You can see now hub location!");
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
						sender.sendMessage(prefix + C.RED + "Please enter the coordinates!");
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
					Bukkit.getServer().broadcastMessage(prefix + C.AQUA + "Hub location has changed by " + C.RED + sender.getName() + C.AQUA + "!");
					Bukkit.getServer().broadcastMessage(prefix + C.GREEN + "/hubloc " + C.AQUA + ">>> You can see now hub location!");
					return true;
				}

				/*入力したワールドが存在しないとき*/
				Bukkit.getServer().broadcastMessage(prefix + C.GOLD + args[0] + " doesn't exist!");
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
						sender.sendMessage(prefix + C.RED + "Please enter the coordinates!");
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
					Bukkit.getServer().broadcastMessage(prefix + C.AQUA + "Hub location has changed by " + C.RED + sender.getName() + C.AQUA + "!");
					Bukkit.getServer().broadcastMessage(prefix + C.GREEN + "/hubloc " + C.AQUA + ">>> You can see now hub location!");
					return true;
				}

				/*入力したワールドが存在しないとき*/
				Bukkit.getServer().broadcastMessage(prefix + C.GOLD + args[0] + " doesn't exist!");
				return false;
			}

			sender.sendMessage(prefix);
			sender.sendMessage(C.GOLD + "Example: " + C.AQUA + "/sethub <WorldName> <X> <Y> <Z> or");
			sender.sendMessage(         C.AQUA + "/sethub <WorldName> <X> <Y> <Z> <Yaw> <Pitch>");
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
				sender.sendMessage(C.GREEN + "World: " + C.AQUA + W);
				sender.sendMessage(C.GOLD + "X: " + C.AQUA + X);
				sender.sendMessage(C.GOLD + "Y: " + C.AQUA + Y);
				sender.sendMessage(C.GOLD + "Z: " + C.AQUA + Z);
				sender.sendMessage(C.GOLD + "Yaw: " + C.AQUA + YAW);
				sender.sendMessage(C.GOLD + "Pitch: " + C.AQUA + PITCH);
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + ErrorMessage.UP);
			return false;

		}

		/*「/seppuku」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("seppuku"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき*/
				sender.sendMessage(prefix + ErrorMessage.FC);
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
			sender.sendMessage(prefix + ErrorMessage.UP);
			return false;

		}

		/*「/civn」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("civn"))
		{
			if (args.length == 0)
			{
				sender.sendMessage(prefix);
				sender.sendMessage(C.GOLD + "Author: " + C.D_RED + "CIVN");
				sender.sendMessage(C.GOLD + "Version: " + C.D_BLUE + "1.0");
				return true;
			}

			else if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("?"))
			{
				Player p = (Player) sender;

				if (p.isOp())
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
					return true;
				}

				sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + "Show infomation!");
				sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + "Go to hub!");
				sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + "Create asiba!");
				sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + "Create asiba to hit the bottom!");
				sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + "Die...");
				return true;
			}

			/*余計な引数がついているとき*/
			sender.sendMessage(prefix + ErrorMessage.UP);
			return false;
		}

		/*「/asiba」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("asiba"))
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(prefix + ErrorMessage.FC);
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
					if (args[1] == null)
					{
						sender.sendMessage(prefix + ErrorMessage.UP);
						return false;
					}

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
							sender.sendMessage(prefix + C.RED + args[1] + C.GOLD + " doesn't exist!");
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
						sender.sendMessage(prefix + C.RED + args[1] + C.GOLD + " doesn't exist!");
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
						sender.sendMessage(prefix + C.RED + args[0] + C.GOLD + " doesn't exist!");
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
							sender.sendMessage(prefix + C.RED + "Invaild parameters!");
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
					sender.sendMessage(prefix + C.AQUA + "ItemID [" + C.RED + block + C.AQUA + "]" + C.GOLD + " doesn't exist!");
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
						sender.sendMessage(prefix + C.RED + "Invaild parameters!");
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

			sender.sendMessage(prefix + ErrorMessage.UP);
			return false;

		}

		return false;

	}

	/*プレイヤーが死んだときのイベント*/
	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event)
	{
		if (!(this.getConfig().getString("EventReport.PlayerDeath").equalsIgnoreCase("true")))
		{
			return;
		}

		String name = event.getEntity().getName();

		Player p = event.getEntity().getPlayer();
		Player k = p.getKiller();

		/*殺した人がいない場合*/
		if (k == null)
		{
			event.setDeathMessage(prefix + C.D_GREEN + name + C.GOLD + " died!");
			return;
		}

		event.setDeathMessage(prefix + C.D_GREEN + name + C.GOLD + " was killed by " + C.RED + k.getName());
	}

	/*プレイヤーのレベルが変化したときのイベント*/
	@EventHandler
	public void onLevelUp (PlayerLevelChangeEvent event)
	{
		if (!(this.getConfig().getString("EventReport.ChangeLevel").equalsIgnoreCase("true")))
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
			Bukkit.broadcastMessage(prefix + C.D_GREEN + pn + C.GOLD + " leveled up " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}

		/*レベルが下がった時の処理*/
		else if (newl < oldl)
		{
			Bukkit.broadcastMessage(prefix + C.D_GREEN + pn + C.GOLD + " leveled down " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}
	}

	/*プレイヤーの経験値が変化した時のイベント*/
	@EventHandler
	public void onExpUp (PlayerExpChangeEvent event)
	{
		if (!(this.getConfig().getString("EventReport.ChangeEXP").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();
		String pn = p.getName();
		int exp = event.getAmount();

		Bukkit.broadcastMessage(prefix + C.D_GREEN + pn + C.GOLD + " got " + C.RED + exp + C.GOLD + " EXP!");

	}

	/*プレイヤーがベッドで寝たときのイベント*/
	@EventHandler
	public void onHealth (PlayerBedLeaveEvent event)
	{
		if (!(this.getConfig().getString("BedHeal").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();

		p.setHealth(20);
		p.setFoodLevel(20);

		Bukkit.broadcastMessage(prefix + C.D_GREEN + p.getName() + C.GOLD + " has completely recovered!");
	}

	/*プレイヤーがゲームに参加した時のイベント*/
	@EventHandler
	public void onJoin (PlayerJoinEvent event)
	{
		if (!(this.getConfig().getString("EventReport.JoinMsg").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();

		event.setJoinMessage(prefix + C.D_GREEN + p.getName() + C.GOLD + " has joined.");

		for (Player players: Bukkit.getServer().getOnlinePlayers())
		{
			if (players.isOp())
			{
				players.sendMessage(prefix + C.GOLD + "This server's IP Address is " + C.GREEN + Bukkit.getServer().getIp());
				players.sendMessage(prefix + C.RED + p.getName() + C.GOLD + "'s IP Address is " + C.GREEN + p.getAddress());
			}
		}
	}

	/*プレイヤーがゲームを離脱した時のイベント*/
	@EventHandler
	public void onLeave (PlayerQuitEvent event)
	{
		if (!(this.getConfig().getString("EventReport.LeaveMsg").equalsIgnoreCase("true")))
		{
			return;
		}

		Player p = event.getPlayer();

		event.setQuitMessage(prefix + C.D_GREEN + p.getName() + C.GOLD + " has left.");
	}

	@EventHandler
	public void Attack (EntityDamageByEntityEvent e)
	{
		if (!(e.getDamager() instanceof Player))
		{
			return;
		}

		Player damager = (Player) e.getDamager();
		Entity damagee = e.getEntity();

		damager.sendMessage(prefix + C.RED + "You Attacked!");

		if (damager.getInventory().getItemInHand().getType() == Material.STICK)
		{
			e.setDamage(0);

			String name = damager.getInventory().getItemInHand().getItemMeta().getDisplayName();

			damagee.setCustomName(name);

			if (name == null)
			{
				damager.sendMessage(prefix + C.RED + "You reset his name!");
			}

			damager.sendMessage(prefix + C.RED + "You changed his name! => " + name);
		}
	}
}