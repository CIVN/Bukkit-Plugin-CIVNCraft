package xyz.civn.civncraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

	EntityType Sheep = EntityType.SHEEP;

	static String prefix = GRAY + "[" + B + BLUE + "CIVNCraft" + R + GRAY + "] ";

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);

		if (!(this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp") | this.getConfig().getString("ChatLanguage").equalsIgnoreCase("en")))
		{
			this.getConfig().set("ChatLanguage", "en");

			/*コンフィグファイルを保存、リロード*/
			this.saveDefaultConfig();
			this.reloadConfig();
			return;
		}

		else
		{
			return;
		}
	}

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		/*「/hub」コマンド*/
		if (cmd.getName().equalsIgnoreCase("hub"))
		{
			if (sender instanceof Player)
			{
				if (args.length == 0)
				{
					String hubW =  this.getConfig().getString("hub.world");
					double hubX =  this.getConfig().getDouble("hub.x");
					double hubY =  this.getConfig().getDouble("hub.y");
					double hubZ =  this.getConfig().getDouble("hub.z");
					float hubYAW = (float) this.getConfig().getDouble("hub.yaw");
					float hubPITCH = (float) this.getConfig().getDouble("hub.pitch");

					/*プレイヤーとそのプレイヤーがいるワールドを取得*/
					Player p = (Player) sender;

					/*ハブが設定されていない（ワールドがnull）だったとき*/
					if (hubW == null)
					{
						/*言語設定が日本語（jp）だったとき*/
						if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
						{
							sender.sendMessage(prefix + RED + "ハブが設定されていません！");
							sender.sendMessage(prefix + RED + "ハブを設定してください！");
							return true;
						}
						sender.sendMessage(prefix + RED + "Hub wasn't set!");
						sender.sendMessage(prefix + RED + "Please set hub!");
						return true;
					}

					/*取得プレイヤーをテレポート*/
					Location HUB = new Location (Bukkit.getWorld (hubW), hubX, hubY, hubZ, hubYAW, hubPITCH);
					p.teleport(HUB);
					return true;
				}

				/*余計な引数がついているとき*/
				/*言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					sender.sendMessage(prefix + RED + "不要なパラメータ！");
				}

				sender.sendMessage(prefix + RED + "Unnecessary parameters!");
				return true;
			}

			/*コマンドの実行元がプレイヤーではないとき
			 *言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				sender.sendMessage(prefix + RED + "このコマンドはコンソールから実行できません！");
				return true;
			}

			sender.sendMessage(prefix + RED + "Can't run this command from console!");
			return true;
		}

		/*「/sethub」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("sethub"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき
				 *言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					sender.sendMessage(prefix + RED + "このコマンドはコンソールから実行できません！");
					return true;
				}

				sender.sendMessage(prefix + RED + "Can't run this command from console!");
				return true;
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
				this.getConfig().set("hub.world", p.getWorld().getName());
				this.getConfig().set("hub.x", rX);
				this.getConfig().set("hub.y", rY);
				this.getConfig().set("hub.z", rZ);
				this.getConfig().set("hub.yaw", rYAW);
				this.getConfig().set("hub.pitch", rPITCH);

				/*コンフィグファイルを保存、リロード*/
				this.saveConfig();
				this.reloadConfig();

				/*報告
				 *言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.getServer().broadcastMessage(prefix + AQUA + "ハブの位置が " + RED + sender.getName() + AQUA + " によって変更されました！");
					Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> ハブの座標を見ることができます！");
					return true;
				}

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
					this.getConfig().set("hub.world", args[0]);

					try
					{
						Double rX = Double.parseDouble (args[1]);
						Double rY = Double.parseDouble (args[2]);
						Double rZ = Double.parseDouble (args[3]);
					}

					catch (NumberFormatException e)
					{
						if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
						{
							sender.sendMessage(prefix + RED + "正しい数値を入力してください！");
							return true;
						}

						sender.sendMessage(prefix + RED + "Please enter the correct number!");
						return true;
					}

					Double rX = Double.parseDouble (args[1]);
					Double rY = Double.parseDouble (args[2]);
					Double rZ = Double.parseDouble (args[3]);

					this.getConfig().set("hub.x", rX);
					this.getConfig().set("hub.y", rY);
					this.getConfig().set("hub.z", rZ);
					this.getConfig().set("hub.yaw", 0);
					this.getConfig().set("hub.pitch", 0);
					this.saveConfig();
					this.reloadConfig();

					/*報告
					 *言語設定が日本語（jp）だったとき*/
					if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
					{
						Bukkit.getServer().broadcastMessage(prefix + AQUA + "ハブの位置が " + RED + sender.getName() + AQUA + " によって変更されました！");
						Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> ハブの座標を見ることができます！");
						return true;
					}

					Bukkit.getServer().broadcastMessage(prefix + AQUA + "Hub location has changed by " + RED + sender.getName() + AQUA + "!");
					Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> You can see now hub location!");
					return true;
				}

				/*入力したワールドが存在しないとき
				 *言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.getServer().broadcastMessage(prefix + GOLD + args[0] + " というワールドは存在しません！");
					return true;
				}

				Bukkit.getServer().broadcastMessage(prefix + GOLD + args[0] + " doesn't exist!");
				return true;
			}

			else if (args.length == 6)
			{
				World w = Bukkit.getWorld(args[0]);

				if (w != null)
				{
					/*入力した座標をコンフィグファイルに保存*/
					this.getConfig().set("hub.world", args[0]);

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
						if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
						{
							sender.sendMessage(prefix + RED + "正しい数値を入力してください！");
							return true;
						}

						sender.sendMessage(prefix + RED + "Please enter the correct number!");
						return true;
					}

					Double rX = Double.parseDouble (args[1]);
					Double rY = Double.parseDouble (args[2]);
					Double rZ = Double.parseDouble (args[3]);
					Double rYaw = Double.parseDouble (args[4]);
					Double rPitch = Double.parseDouble (args[5]);

					this.getConfig().set("hub.x", rX);
					this.getConfig().set("hub.y", rY);
					this.getConfig().set("hub.z", rZ);
					this.getConfig().set("hub.yaw", rYaw);
					this.getConfig().set("hub.pitch", rPitch);
					this.saveConfig();
					this.reloadConfig();

					/*報告
					 *言語設定が日本語（jp）だったとき*/
					if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
					{
						Bukkit.getServer().broadcastMessage(prefix + AQUA + "ハブの位置が " + RED + sender.getName() + AQUA + " によって変更されました！");
						Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> ハブの座標を見ることができます！");
						return true;
					}

					Bukkit.getServer().broadcastMessage(prefix + AQUA + "Hub location has changed by " + RED + sender.getName() + AQUA + "!");
					Bukkit.getServer().broadcastMessage(prefix + GREEN + "/hubloc " + AQUA + ">>> You can see now hub location!");
					return true;
				}

				/*入力したワールドが存在しないとき
				 *言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.getServer().broadcastMessage(prefix + GOLD + args[0] + " というワールドは存在しません！");
					return true;
				}

				Bukkit.getServer().broadcastMessage(prefix + GOLD + args[0] + " doesn't exist!");
				return true;
			}

			sender.sendMessage(prefix);
			sender.sendMessage(GOLD + "Example: " + AQUA + "/sethub <WorldName> <X> <Y> <Z> or");
			sender.sendMessage(         AQUA + "/sethub <WorldName> <X> <Y> <Z> <Yaw> <Pitch>");
			return true;

		}

		/*「/hubloc」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("hubloc"))
		{
			if (args.length == 0)
			{
				/*コンフィグファイルからハブのx~pitch座標を取得*/
				String W = this.getConfig().getString("hub.world");
				double X = this.getConfig().getDouble("hub.x");
				double Y = this.getConfig().getDouble("hub.y");
				double Z = this.getConfig().getDouble("hub.z");
				float YAW = (float) this.getConfig().getDouble("hub.yaw");
				float PITCH = (float) this.getConfig().getDouble("hub.pitch");

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

			/*余計な引数がついているとき
			 *言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				sender.sendMessage(prefix + RED + "不要なパラメータ！");
			}

			sender.sendMessage(prefix + RED + "Unnecessary parameters!");
			return true;

		}

		/*「/seppuku」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("seppuku"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					sender.sendMessage(prefix + RED + "このコマンドはコンソールから実行できません！");
					return true;
				}

				sender.sendMessage(prefix + RED + "Can't run this command from console!");
				return true;
			}

			if (args.length == 0)
			{
				/*プレイヤーを取得*/
				Player p = (Player) sender;

				/*取得したプレイヤーのヘルスを0にする（殺す）*/
				p.setHealth(0);
				return true;
			}

			/*余計な引数がついているとき
			 *言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				sender.sendMessage(prefix + RED + "不要なパラメータ！");
			}

			sender.sendMessage(prefix + RED + "Unnecessary parameters!");
			return true;

		}

		/*「/asiba」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("asiba"))
		{
			if (!(sender instanceof Player))
			{
				/*コマンドの実行元がプレイヤーではないとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					sender.sendMessage(prefix + RED + "このコマンドはコンソールから実行できません！");
					return true;
				}

				sender.sendMessage(prefix + RED + "Can't run this command from console!");
				return true;
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

						/*取得に失敗したらfalseを返す*/
						if (m == null)
						{
							sender.sendMessage(prefix + RED + args[1] + " doesn't exist!");
							sender.sendMessage(GREEN + "     >>>Is it a Block?");
							sender.sendMessage(GREEN + "     >>>Isn't it wrong Name?");
							return true;
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

					/*取得に失敗したらfalseを返す*/
					if (m == null)
					{
						sender.sendMessage(prefix + RED + args[1] + " doesn't exist!");
						sender.sendMessage(GREEN + "     >>>Is it a Block?");
						sender.sendMessage(GREEN + "     >>>Isn't it wrong ID?");
						return true;
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
						return true;
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
							if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
							{
								sender.sendMessage(prefix + RED + "無効なパラメータ！");
								return true;
							}

							sender.sendMessage(prefix + RED + "Invaild parameters!");
							return true;
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
					return true;
				}

				/*コマンドパラメータが2のとき*/
				if (args.length == 2)
				{
					/*args[1]が数値かチェック*/
					try
					{
						Integer.parseInt(args[1]);
					}

					/*数値ではなかったときfalseを返す*/
					catch (NumberFormatException e)
					{
						if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
							{
								sender.sendMessage(prefix + RED + "無効なパラメータ！");
								return true;
							}

						sender.sendMessage(prefix + RED + "Invaild parameters!");
						return true;
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

			/*余計な引数がついているとき
			 *言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				sender.sendMessage(prefix + RED + "不要なパラメータ！");
			}

			sender.sendMessage(prefix + RED + "Unnecessary parameters!");
			return true;

		}

		/*「/lang」コマンド*/
		else if (cmd.getName().equalsIgnoreCase("lang"))
		{
			if (args.length == 1)
			{
				/*引数がjpまたはenだったとき*/
				if (args[0].equalsIgnoreCase("jp") | args[0].equalsIgnoreCase("en"))
				{
					if (args[0].equalsIgnoreCase("jp"))
					{
						/*コンフィグファイルを書き換える*/
						this.getConfig().set("ChatLanguage", "jp");
						this.saveConfig();
						this.reloadConfig();

						/*報告*/
						sender.sendMessage(prefix + GOLD + "言語を日本語に変更しました！");
						return true;
					}

					else if (args[0].equalsIgnoreCase("en"))
					{
						/*コンフィグファイルを書き換える*/
						this.getConfig().set("ChatLanguage", "en");
						this.saveConfig();
						this.reloadConfig();

						/*報告*/
						sender.sendMessage(prefix + GOLD + "You changed language to English!");
						return true;
					}

					/*引数がjp、en以外だったとき
					 *言語設定が日本語（jp）だったとき*/
					if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
					{
						sender.sendMessage(prefix + RED + args[0] + " という言語は対応しておりません！");
						return true;
					}

					sender.sendMessage(prefix + RED + args[0] + " is not supported!");
					return true;
				}
			}

			/*余計な引数がついているとき
			 *言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				sender.sendMessage(prefix + RED + "不要なパラメータ！");
				return true;
			}

			sender.sendMessage(prefix + RED + "Unnecessary parameters!");
			return true;
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

			else if (args[0].equalsIgnoreCase("info"))
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
					sender.sendMessage(GOLD + "/lang <(en) or (jp)>: " + AQUA + "Set language!");
					sender.sendMessage(GOLD + "/seppuku: " + AQUA + "Die...");
					sender.sendMessage(GOLD + "/asiba <BlockID> <height>: " + AQUA + "Create asiba!");
					return true;
				}

				sender.sendMessage(GOLD + "/civn: " + AQUA + "Show infomation!");
				sender.sendMessage(GOLD + "/hub: " + AQUA + "Go to hub!");
				sender.sendMessage(GOLD + "/asiba <BlockID> <height>: " + AQUA + "Create asiba!");
				sender.sendMessage(GOLD + "/seppuku: " + AQUA + "Die...");
				return true;
			}

			/*余計な引数がついているとき
			 *言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				sender.sendMessage(prefix + RED + "不要なパラメータ！");
			}

			sender.sendMessage(prefix + RED + "Unnecessary parameters!");
			return true;
		}

		return false;

}

	/*プレイヤーが死んだときのイベント*/
	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event)
	{
		if (this.getConfig().getString("Event.PlayerDeath").equalsIgnoreCase("true"))
		{
			String name = event.getEntity().getName();

			Player p = event.getEntity().getPlayer();
			Player k = p.getKiller();

			/*殺した人がいない場合*/
			if (k == null)
			{
				/*言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					event.setDeathMessage(prefix + RED + name + " が死んでしまった！");
					return;
				}

				event.setDeathMessage(prefix + RED + name + GOLD + " died!");
				return;
			}

			/*言語設定が日本語（jp）だったとき*/
			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				event.setDeathMessage(prefix + RED + name + GOLD + " は " + RED + k.getName() + GOLD + " によって殺されてしまった！");
				return;
			}

			event.setDeathMessage(prefix + RED + name + GOLD + " was killed by " + RED + k.getName());
			return;
		}

		return;

	}

	/*エンティティにダメージを与えた時のイベント*/
	@EventHandler
	public void onDamage (EntityDamageByEntityEvent event)
	{
		if (this.getConfig().getString("Event.DamageToEntity").equalsIgnoreCase("true"))
		{
			if (event.getDamager().getType() == EntityType.PLAYER)
			{
				String damager = event.getDamager().getName();
				String damagee = event.getEntity().getName();
				double damage = event.getDamage();

				/*言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.broadcastMessage(prefix + RED + damager + GOLD + " は " + RED + damagee + GOLD + " に " + RED + damage + GOLD + " ポイントのダメージを与えた！");
					return;
				}

				Bukkit.broadcastMessage(prefix + RED + damager + GOLD + " gave damage to " + RED + damagee + " " + damage + GOLD + " points!");
				return;
			}

			return;

		}

		return;

	}

	/*プレイヤーのレベルが変化したときのイベント*/
	@EventHandler
	public void onLevelUp (PlayerLevelChangeEvent event)
	{
		if (this.getConfig().getString("Event.ChangeLevel").equalsIgnoreCase("true"))
		{
			Player p = event.getPlayer();
			String pn = p.getName();
			int newl = event.getNewLevel();
			int oldl = event.getOldLevel();

			/*レベルが上がった時の処理*/
			if (newl > oldl)
			{
				/*言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.broadcastMessage(prefix + RED + pn + GOLD + " は レベル" + RED + oldl + GOLD + "から、レベル" + RED + newl + GOLD + "に上がった！");
					return;
				}

				Bukkit.broadcastMessage(prefix + RED + pn + GOLD + " leveled up " + RED + "Lv." + oldl + GOLD + " to " + RED + "Lv." + newl + GOLD + "!");
				return;
			}

			/*レベルが下がった時の処理*/
			if (newl < oldl)
			{
				/*言語設定が日本語（jp）だったとき*/
				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.broadcastMessage(prefix + RED + pn + GOLD + " は レベル" + RED + oldl + GOLD + "から、レベル" + RED + newl + GOLD + "に下がった！");
					return;
				}

				Bukkit.broadcastMessage(prefix + RED + pn + GOLD + " leveled down " + RED + "Lv." + oldl + GOLD + " to " + RED + "Lv." + newl + GOLD + "!");
				return;
			}

			return;

		}

		return;

	}

	/*プレイヤーの経験値が変化した時のイベント*/
	@EventHandler
	public void onExpUp (PlayerExpChangeEvent event)
	{
		if (this.getConfig().getString("Event.ChangeEXP").equalsIgnoreCase("true"))
		{
			Player p = event.getPlayer();
			String pn = p.getName();
			int exp = event.getAmount();

			if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
			{
				Bukkit.broadcastMessage(prefix + RED + pn + GOLD + " は " + RED + exp + GOLD + " の経験値を獲得！");
				return;
			}

			Bukkit.broadcastMessage(prefix + RED + pn + GOLD + " got " + RED + exp + GOLD + " EXP!");
			return;
		}

		return;

	}

	/*プレイヤーがベッドで寝たときのイベント*/
	@EventHandler
	public void onHealth (PlayerBedLeaveEvent event)
	{
		if (this.getConfig().getString("Event.ChangeHealth").equalsIgnoreCase("true"))
		{
			if (this.getConfig().getString("RecoveryBed").equalsIgnoreCase("true"))
			{
				Player p = event.getPlayer();

				p.setHealth(20);
				p.setFoodLevel(20);

				if (this.getConfig().getString("ChatLanguage").equalsIgnoreCase("jp"))
				{
					Bukkit.broadcastMessage(prefix + RED + p.getName() + " の体力と空腹度が全回復した！");
					return;
				}

				Bukkit.broadcastMessage(prefix + RED + p.getName() + " has completely recovered!");
				return;
			}

			return;

		}

		return;

	}

	/*プレイヤーがゲームに参加した時のイベント*/
	@EventHandler
	public void onJoin (PlayerJoinEvent event)
	{
		Player p = event.getPlayer();

		event.setJoinMessage(prefix + D_GREEN + p.getName() + GOLD + " has joined.");
		return;
	}

	/*プレイヤーがゲームを離脱した時のイベント*/
	@EventHandler
	public void onLeave (PlayerQuitEvent event)
	{
		Player p = event.getPlayer();

		event.setQuitMessage(prefix + D_GREEN + p.getName() + GOLD + " has left.");
		return;
	}

	/*プレイヤーが移動した時のイベント*/
	@EventHandler
	public void onMove (PlayerMoveEvent event) throws InterruptedException
	{
		Player p = event.getPlayer();
		Location l = p.getLocation();
		Material b = l.getBlock().getType();

		if (p.getInventory().getHelmet().getType() == Material.PUMPKIN)
		{
			if (b == Material.AIR) {return;}

			l.getBlock().setType(Material.SNOW);
			return;
		}

		return;

	}

}