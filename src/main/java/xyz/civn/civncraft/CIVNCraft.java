package xyz.civn.civncraft;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;
import xyz.civn.civncraft.SendMessage.SendErrorMessage;
import xyz.civn.civncraft.SendMessage.SendHelpMessage;
import xyz.civn.civncraft.SendMessage.SendReportMessage;

public class CIVNCraft extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;
	private static String[] contents = {"location", "level", "address", "gamemode", "health", "enderchest", "inventory"};

	private CIVNCraft main = this;
	private FileConfiguration cf = getConfig();

	@Override
	public void onEnable()
	{
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable()
	{
		reloadConfig();
		saveConfig();
	}

	/*
	private static void WriteIP(String ip)
	{
		File file = new File("IPAdress.txt");
		FileWriter fw;

		try
		{
			if (CheckFile(file))
			{
				fw = new FileWriter(file);
				fw.write("\n" + ip);
				fw.close();
			}
		}

		catch (IOException e)
		{
			return;
		}
	}
	*/

	/*
	private static boolean CheckFile(File file)
	{
	    if (file.exists())
	    {
	      if (file.isFile() && file.canWrite())
	      {
	        return true;
	      }
	    }

	    return false;
	}
	*/

	private static String GetPlayerPrefix(Player player)
	{
		String playerprefix;

		try
		{
			playerprefix = PermissionsEx.getUser(player).getPrefix();
		}

		catch (Exception e)
		{
			return "";
		}

		return ChatFormatter(playerprefix, player);
	}

	private static String ChatFormatter(String text, Player player)
	{
		try
		{
			text = text.replace("%world", player.getWorld().getName());
		}

		catch (Exception e)
		{
			return "";
		}

		return ChatColor.translateAlternateColorCodes('&', text);
	}

	@EventHandler
	private void onPlayerLevelChangeEvent (PlayerLevelChangeEvent e)
	{
		/*
		if (cf.getBoolean("eventMessage.changedlevel") == false)
		{
			return;
		}
		*/

		Player p = e.getPlayer();
		String pn = p.getName();
		int newl = e.getNewLevel();
		int oldl = e.getOldLevel();

		if (newl > oldl)
		{
			getServer().broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " leveled up " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}

		else
		{
			getServer().broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " leveled down " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}
	}

	@EventHandler
	private void onPlayerExpChangeEvent (PlayerExpChangeEvent e)
	{
		/*
		if (cf.getBoolean("eventMessage.changedexp") == false)
		{
			return;
		}
		*/

		Player p = e.getPlayer();
		String pn = p.getName();
		int exp = e.getAmount();

		getServer().broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " got " + C.RED + exp + C.GOLD + " EXP!");
	}

	@EventHandler
	private void onPlayerJoinEvent (PlayerJoinEvent e)
	{
		/*
		if (cf.getBoolean("eventMessage.joinmessage") == false)
		{
			return;
		}
		*/

		Player player = e.getPlayer();

		e.setJoinMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has joined.");

		for (Player players: getServer().getOnlinePlayers())
		{
			if (players.isOp())
			{
				players.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s IP: " + C.GREEN + player.getAddress().getAddress().toString().replace("/", ""));
			}
		}

		//WriteIP(player.getAddress().getAddress().toString().replace("/", ""));
	}

	@EventHandler
	private void onPlayerQuitEvent (PlayerQuitEvent e)
	{
		/*
		if (cf.getBoolean("eventMessage.quitmessage") == false)
		{
			return;
		}
		*/

		Player player = e.getPlayer();

		e.setQuitMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has left.");
	}

	@EventHandler
	private void onPlayerChangedWorldEvent (PlayerChangedWorldEvent e)
	{
		/*
		if (cf.getBoolean("eventMessage.movedworld") == false)
		{
			return;
		}
		*/

		Player player = e.getPlayer();
		String from = e.getFrom().getName();
		String to = player.getWorld().getName();

		SendReportMessage.MovedWorld(player, from, to, GetPlayerPrefix(player));
	}

	@EventHandler
	@SuppressWarnings("deprecation")
	private void onEntityDamageByEntityEvent (EntityDamageByEntityEvent e)
	{
		/*
		if (cf.getBoolean("eventMessage.easynamechanger") == false)
		{
			return;
		}
		*/

		if (!(e.getDamager() instanceof Player))
		{
			return;
		}

		Player damager = (Player) e.getDamager();
		Entity damagee = e.getEntity();

		if (damager.getInventory().getItemInHand().getType() == Material.STICK)
		{
			e.setDamage(0);

			String name = damager.getInventory().getItemInHand().getItemMeta().getDisplayName();

			damagee.setCustomName(name);

			if (name == null)
			{
				SendReportMessage.ResetName(damager);
			}

			else
			{
				SendReportMessage.ChangedName(name, damager);
			}

			return;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("hub"))
		{
			if (args.length != 0)
			{
				SendErrorMessage.ExtraArguments(sender);
				return false;
			}

			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			Player p = (Player) sender;

			try
			{
				cf.getString("hub.world");
				cf.getDouble("hub.x");
				cf.getDouble("hub.y");
				cf.getDouble("hub.z");
				cf.getDouble("hub.yaw");
				cf.getDouble("hub.pitch");
			}

			//ハブ未設定
			catch (Exception e)
			{
				SendErrorMessage.HubWasNotSet(sender);

				cf.set("hub.world", "");
				cf.set("hub.x", 0);
				cf.set("hub.y", 0);
				cf.set("hub.z", 0);
				cf.set("hub.yaw", 0);
				cf.set("hub.pitch", 0);
				saveConfig();
				return false;
			}

			String hubW =  cf.getString("hub.world");
			double hubX =  cf.getDouble("hub.x");
			double hubY =  cf.getDouble("hub.y");
			double hubZ =  cf.getDouble("hub.z");
			float hubYAW = (float) cf.getDouble("hub.yaw");
			float hubPITCH = (float) cf.getDouble("hub.pitch");

			World world = getServer().getWorld (hubW);

			//サーバーにワールドが無い
			if (world == null)
			{
				SendErrorMessage.WorldDoesNotExist(world, sender);
				return false;
			}

			//プレイヤーをテレポート
			p.teleport(new Location (world, hubX, hubY, hubZ, hubYAW, hubPITCH));
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("sethub"))
		{
			// [/sethub]
			if (args.length == 0)
			{
				if (!(sender instanceof Player))
				{
					SendErrorMessage.SenderIsNotPlayer(sender);
					return false;
				}

				Player p = (Player) sender;

				cf.set("hub.world", p.getWorld().getName());
				cf.set("hub.x", p.getLocation().getX());
				cf.set("hub.y", p.getLocation().getY());
				cf.set("hub.z", p.getLocation().getZ());
				cf.set("hub.yaw", p.getLocation().getYaw());
				cf.set("hub.pitch", p.getLocation().getPitch());
				saveConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			// [/sethub *** *** *** ***]
			else if (args.length == 4)
			{
				World w = getServer().getWorld(args[0]);

				//サーバーにワールドが無い
				if (w == null)
				{
					SendErrorMessage.WorldDoesNotExistInServer(sender, args);
					return false;
				}

				cf.set("hub.world", args[0]);

				//引数をDouble型に変換
				try
				{
					for (int i = 1; i <= 3; i++)
					{
						Double.parseDouble (args[i]);
					}
				}

				//変換に失敗
				catch (Exception e)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}

				//コンフィグに座標を保存
				cf.set("hub.x", Double.parseDouble (args[1]));
				cf.set("hub.y", Double.parseDouble (args[2]));
				cf.set("hub.z", Double.parseDouble (args[3]));
				saveConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			// [/sethub *** *** *** *** *** ***]
			else if (args.length == 6)
			{
				World w = getServer().getWorld(args[0]);

				//サーバーにワールドが無い
				if (w == null)
				{
					SendErrorMessage.WorldDoesNotExistInServer(sender, args);
					return false;
				}

				cf.set("hub.world", args[0]);

				//引数をDouble型に変換
				try
				{
					for (int i = 1; i <= 5; i++)
					{
						Double.parseDouble (args[i]);
					}
				}

				//変換に失敗
				catch (Exception e)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}

				//コンフィグに座標を保存
				cf.set("hub.x", Double.parseDouble (args[1]));
				cf.set("hub.y", Double.parseDouble (args[2]));
				cf.set("hub.z", Double.parseDouble (args[3]));
				cf.set("hub.yaw", Double.parseDouble (args[4])); //ホントはFloat
				cf.set("hub.pitch", Double.parseDouble (args[5])); //ホントはFloat
				saveConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("hubloc"))
		{
			SendReportMessage.ShowHubLocation(sender, cf);
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("seppuku"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			Player p = (Player) sender;
			p.setHealth(0);
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("civn"))
		{
			if (args.length == 0)
			{
				SendReportMessage.ShowInfomation(sender, main);
				return true;
			}

			if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("?"))
			{
				Player p = (Player) sender;

				if (p.isOp())
				{
					SendHelpMessage.ShowOpHelp(sender, main);
					return true;
				}

				SendHelpMessage.ShowHelp(sender, main);
				return true;
			}

			else if (args[0].equalsIgnoreCase("reload"))
			{
				saveConfig();
				reloadConfig();

				sender.sendMessage(prefix + C.GOLD + "CIVNCraft Reloaded!");
				return true;
			}
		}

		else if (cmd.getName().equalsIgnoreCase("asiba"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			if (args.length != 1 && args.length != 2)
			{
				SendErrorMessage.Failed(sender);
				return false;
			}

			Player p = (Player) sender;
			Location l = p.getLocation();

			/*[/asiba *****]*/
			if (args.length == 1)
			{
				int id;

				try
				{
					id = Integer.parseInt(args[0]);
				}

				catch (Exception e)
				{
					Material m = Material.getMaterial(args[0].toUpperCase());

					if (m == null)
					{
						SendErrorMessage.Failed(sender);
						return false;
					}

					l.setY(l.getY() - 1);
					l.getBlock().setType(m);
					return true;
				}

				Material m = Material.getMaterial(id);

				if (m == null)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}

				l.setY(l.getY() - 1);
				l.getBlock().setType(m);
				return true;
			}

			/*[/asiba ***** *****]*/
			else if (args.length == 2)
			{
				/*[/asiba to *****]*/
				if (args[0].equalsIgnoreCase("to"))
				{
					if (args[1] == null)
					{
						SendErrorMessage.ExtraArguments(sender);
						return false;
					}

					try
					{
						Integer.parseInt(args[1]);
					}

					catch (Exception e)
					{
						Material m = Material.getMaterial(args[1].toUpperCase());

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

					Material m = Material.getMaterial((int) new Integer (args[1]));

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

				/*[/asiba ***** *****]*/
				int height;

				try
				{
					height = Integer.parseInt(args[1]);
				}

				catch (Exception e)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}

				int id;

				try
				{
					id = Integer.parseInt(args[0]);
				}

				catch (Exception e)
				{
					Material m = Material.getMaterial(args[0].toUpperCase());

					if (m == null)
					{
						SendErrorMessage.Failed(sender);
						return false;
					}

					for (int i = 1; i <= height; i++)
					{
						l.setY(l.getY() - 1);
						l.getBlock().setType(m);
					}

					return true;
				}

				Material m = Material.getMaterial(id);

				if (m == null)
				{
					sender.sendMessage(prefix + C.RED + args[1] + C.GOLD + " doesn't exist!");
					return false;
				}

				for (int i = 1; i <= height; i++)
				{
					l.setY(l.getY() - 1);
					l.getBlock().setType(m);
				}

				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("pdata"))
		{
			/* [/pdata *****]*/
			if (args.length == 1)
			{
				/* [/pdata help]*/
				if (args[0].equalsIgnoreCase("?") | args[0].equalsIgnoreCase("help"))
				{
					SendHelpMessage.ShowPdataHelp(sender, contents, main);
					return true;
				}

				else
				{
					SendErrorMessage.Failed(sender);
					return false;
				}
			}

			/* [/pdata ***** *****]*/
			else if (args.length == 2)
			{
				Player player = getServer().getPlayer(args[1]);

				if (player == null)
				{
					SendErrorMessage.PlayerIsNotIn(sender, args);
					return false;
				}

				else
				{
					switch (args[0].toLowerCase())
					{
						case "health":
						{
							double health = player.getHealth();

							sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s health is " + C.D_GREEN + health + C.BLUE + "!");
							return true;
						}

						case "location":
						{
							Location location = player.getLocation();
							double[] l = {location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()};

							SendReportMessage.ShowPlayerLocation(sender, player, l, GetPlayerPrefix(player));
							return true;
						}

						case "level":
						{
							int level = player.getLevel();

							sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s level is " + C.D_GREEN + level + C.BLUE + "!");
							return true;
						}

						case "address":
						{
							String address = player.getAddress().getAddress().toString().replace("/", "");

							sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s address is " + C.D_GREEN + address + C.BLUE + "!");
							return true;
						}

						case "ip":
						{
							String address = player.getAddress().getAddress().toString().replace("/", "");

							sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s address is " + C.D_GREEN + address + C.BLUE + "!");
							return true;
						}

						case "gamemode":
						{
							String gamemode = player.getGameMode().name();

							sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s gamemode is " + C.D_GREEN + gamemode + C.BLUE + "!");
							return true;
						}

						case "enderchest":
						{
							int i = 1;

							sender.sendMessage(prefix);
							sender.sendMessage(GetPlayerPrefix (player) + player.getName() + C.GOLD + "'s EnderChest.");

							for (ItemStack is: player.getEnderChest())
							{
								try
								{
									SendReportMessage.ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								SendReportMessage.HasNoItems(sender, player, GetPlayerPrefix(player));
							}

							return true;
						}

						case "ec":
						{
							int i = 1;

							sender.sendMessage(prefix);
							sender.sendMessage(GetPlayerPrefix (player) + player.getName() + C.GOLD + "'s EnderChest.");

							for (ItemStack is: player.getEnderChest())
							{
								try
								{
									SendReportMessage.ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								SendReportMessage.HasNoItems(sender, player, GetPlayerPrefix(player));
							}

							return true;
						}

						case "inventory":
						{
							int i = 1;

							sender.sendMessage(prefix);
							sender.sendMessage(GetPlayerPrefix (player) + player.getName() + C.GOLD + "'s Inventory.");

							for (ItemStack is: player.getInventory())
							{
								try
								{
									SendReportMessage.ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								SendReportMessage.HasNoItems(sender, player, GetPlayerPrefix(player));
							}

							return true;
						}

						case "inv":
						{
							int i = 1;

							sender.sendMessage(prefix);
							sender.sendMessage(GetPlayerPrefix (player) + player.getName() + C.GOLD + "'s Inventory.");

							for (ItemStack is: player.getInventory())
							{
								try
								{
									SendReportMessage.ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								SendReportMessage.HasNoItems(sender, player, GetPlayerPrefix(player));
							}

							return true;
						}

						default:
						{
							SendErrorMessage.DoesNotExistInContents(sender, args);
							return false;
						}
					}
				}
			}

			sender.sendMessage(prefix + C.GOLD + "/pdata command was made by " + C.RED + "" + C.B + "CIVN");
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("copyinventory"))
		{
			if (args.length == 0)
			{
				//TODO: コマンドのヘルプとか
				return false;
			}

			else if (args.length == 1)
			{
				Player from = getServer().getPlayer(args[0]);
				Player to = (Player)sender;

				if (to == from)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}

				else if (from == null)
				{
					SendErrorMessage.PlayerIsNotIn(sender, args);
					return false;
				}

				//TODO: 片っ端から追加されちゃうのでそこは考えよう
				to.getInventory().clear();

				for (ItemStack is: from.getInventory())
				{
					try
					{
						to.getInventory().addItem(is);
					}

					catch (Exception e)
					{
						continue;
					}
				}

				return true;
			}

			else if (args.length == 2)
			{
				Player from = getServer().getPlayer(args[0]);
				Player to = getServer().getPlayer(args[1]);

				if (to == from)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}

				else if (from == null)
				{
					sender.sendMessage(prefix + C.BLUE + args[0] + C.RED + " isn't in this server!");
					return false;
				}

				else if (to == null)
				{
					sender.sendMessage(prefix + C.BLUE + args[1] + C.RED + " isn't in this server!");
					return false;
				}

				//TODO: 片っ端から追加されちゃうのでそこは考えよう
				to.getInventory().clear();

				for (ItemStack is: from.getInventory())
				{
					try
					{
						to.getInventory().addItem(is);
					}

					catch (Exception e)
					{
						continue;
					}
				}

				return true;
			}

			else
			{
				//TODO
				return false;
			}
		}

		else if (cmd.getName().equalsIgnoreCase("ops"))
		{
			sender.sendMessage(prefix + C.BLUE + "Operators!");

			int i = 1;

			for (OfflinePlayer p : getServer().getOfflinePlayers())
			{
				if (p.isOp())
				{
					sender.sendMessage(C.D_GREEN + "" + i + ": " + p.getName());
					i++;
				}
			}

			return true;
		}

		/*
		else if (cmd.getName().equalsIgnoreCase("rename"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			if (args.length == 1)
			{
				Player player = (Player) sender;

				try
				{
					player.getItemInHand().getItemMeta().setDisplayName(args[0]);
				}

				catch (Exception e)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}
			}

			SendErrorMessage.Failed(sender);
			return false;
		}
		*/

		return false;
	}
}