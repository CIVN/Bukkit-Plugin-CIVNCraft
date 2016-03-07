package xyz.civn.civncraft;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;
import xyz.civn.civncraft.SendMessage.Send;

public class CIVNCraft extends JavaPlugin implements Listener
{
	private static File file = new File("plugins/CIVNCraft/ips.yml");
	private static String prefix = Prefix.prefix;

	private CIVNCraft main = this;
	private FileConfiguration cf = getConfig();
	private YamlConfiguration ips = YamlConfiguration.loadConfiguration(file);

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);

		try
		{
			CreateIPS();
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable()
	{
		reloadConfig();
		saveConfig();
	}

	public void onReload()
	{
		reloadConfig();
		saveConfig();

		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);

		try
		{
			CreateIPS();
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void CreateIPS() throws IOException
	{
		if (file.exists())
		{
			try
			{
				file.createNewFile();
			}

			catch (Exception e)
			{
				Send.FailedCreateFile();
				return;
			}
		}
	}

	private void WriteIP(Player player, String ip, String hostname) throws IOException
	{
		try
		{
			ips.save(file);
		}

		catch(Exception e)
		{
			Send.FailedCreateFile();
			return;
		}

		ips.createSection(player.getName() + "." + "IPAddress");
		ips.set(player.getName() + "." + "IPAddress", ip);
		ips.set(player.getName() + "." + "HostName", hostname);
		ips.save(file);
	}

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

	/*インベントリのアイテムと個数を表示*/
	@SuppressWarnings("deprecation")
	private static void ShowItem(CommandSender sender, ItemStack is, int i)
	{
		String count = String.valueOf(i);
		String materialname = is.getType().name();
		int materialid = is.getTypeId();
		int amount = is.getAmount();

		sender.sendMessage(C.GOLD + count + ": " + C.AQUA + materialname + C.D_GREEN + "   -ID: " + C.GREEN + materialid + C.D_GREEN + "   -Amount: " + C.GREEN + amount);
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
	private void onPlayerJoinEvent (PlayerJoinEvent e) throws IOException
	{
		/*
		if (cf.getBoolean("eventMessage.joinmessage") == false)
		{
			return;
		}
		*/

		Player player = e.getPlayer();

		e.setJoinMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has joined.");

		String ip = player.getAddress().getAddress().toString().replace("/", "");

		for (Player players: getServer().getOnlinePlayers())
		{
			if (players.isOp())
			{
				players.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s IP: " + C.GREEN + player.getAddress().getAddress().toString().replace("/", ""));
			}
		}


		WriteIP(player, ip, player.getAddress().getHostName());
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

		getServer().broadcastMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has moved from " + C.GREEN + from + C.GOLD + " to " + C.GREEN + to + C.GOLD + ".");
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
				damager.sendMessage(prefix + C.GOLD + "You reset his name.");
			}

			else
			{
				damager.sendMessage(prefix + C.GOLD + "You changed his name. => " + C.AQUA + name);
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
				Send.ExtraArguments(sender);
				return false;
			}

			if (!(sender instanceof Player))
			{
				Send.SenderIsNotPlayer(sender);
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
				Send.HubWasNotSet(sender);

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
				Send.WorldDoesNotExist(world, sender);
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
					Send.SenderIsNotPlayer(sender);
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

				getServer().broadcastMessage(prefix + C.GOLD + "Hub location has changed by " + C.AQUA + sender.getName() + C.GOLD + ".");
				return true;
			}

			// [/sethub *** *** *** ***]
			else if (args.length == 4)
			{
				World w = getServer().getWorld(args[0]);

				//サーバーにワールドが無い
				if (w == null)
				{
					Send.WorldDoesNotExistInServer(sender, args);
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
					Send.Failed(sender);
					return false;
				}

				//コンフィグに座標を保存
				cf.set("hub.x", Double.parseDouble (args[1]));
				cf.set("hub.y", Double.parseDouble (args[2]));
				cf.set("hub.z", Double.parseDouble (args[3]));
				saveConfig();

				getServer().broadcastMessage(prefix + C.GOLD + "Hub location has changed by " + C.AQUA + sender.getName() + C.GOLD + ".");
				return true;
			}

			// [/sethub *** *** *** *** *** ***]
			else if (args.length == 6)
			{
				World w = getServer().getWorld(args[0]);

				//サーバーにワールドが無い
				if (w == null)
				{
					Send.WorldDoesNotExistInServer(sender, args);
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
					Send.Failed(sender);
					return false;
				}

				//コンフィグに座標を保存
				cf.set("hub.x", Double.parseDouble (args[1]));
				cf.set("hub.y", Double.parseDouble (args[2]));
				cf.set("hub.z", Double.parseDouble (args[3]));
				cf.set("hub.yaw", Double.parseDouble (args[4])); //ホントはFloat
				cf.set("hub.pitch", Double.parseDouble (args[5])); //ホントはFloat
				saveConfig();

				getServer().broadcastMessage(prefix + C.GOLD + "Hub location has changed by " + C.AQUA + sender.getName() + C.GOLD + ".");
				return true;
			}

			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("hubloc"))
		{
			sender.sendMessage(prefix);
			sender.sendMessage(C.GOLD + "World: " + C.AQUA + cf.getString("hub.world"));
			sender.sendMessage(C.GOLD + "X: " + C.AQUA + cf.getDouble("hub.x"));
			sender.sendMessage(C.GOLD + "Y: " + C.AQUA + cf.getDouble("hub.y"));
			sender.sendMessage(C.GOLD + "Z: " + C.AQUA + cf.getDouble("hub.z"));
			sender.sendMessage(C.GOLD + "Yaw: " + C.AQUA + (float) cf.getDouble("hub.yaw"));
			sender.sendMessage(C.GOLD + "Pitch: " + C.AQUA + (float) cf.getDouble("hub.pitch"));
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("seppuku"))
		{
			if (!(sender instanceof Player))
			{
				Send.SenderIsNotPlayer(sender);
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
				sender.sendMessage(prefix);
				sender.sendMessage(C.GOLD + "Author: " + C.RED + "" + C.B + "CIVN");
				sender.sendMessage(C.GOLD + "Version: " + C.AQUA + main.getDescription().getVersion());
				sender.sendMessage(C.GOLD + "Website: " + C.BLUE + main.getDescription().getWebsite());
				return true;
			}

			if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("?"))
			{
				Player p = (Player) sender;

				if (p.isOp())
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
					return true;
				}

				sender.sendMessage(prefix);
				sender.sendMessage(C.GOLD + "/civn: " + C.AQUA + main.getDescription().getCommands().get("civn").get("description"));
				sender.sendMessage(C.GOLD + "/hub: " + C.AQUA + main.getDescription().getCommands().get("hub").get("description"));
				sender.sendMessage(C.GOLD + "/asiba <BlockID> <height>: " + C.AQUA + main.getDescription().getCommands().get("asiba").get("description"));
				sender.sendMessage(C.GOLD + "/asiba to <BlockID>: " + C.AQUA + main.getDescription().getCommands().get("asiba").get("description"));
				sender.sendMessage(C.GOLD + "/seppuku: " + C.AQUA + main.getDescription().getCommands().get("seppuku").get("description"));
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
				Send.SenderIsNotPlayer(sender);
				return false;
			}

			if (args.length != 1 && args.length != 2)
			{
				Send.Failed(sender);
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
						Send.Failed(sender);
						return false;
					}

					l.setY(l.getY() - 1);
					l.getBlock().setType(m);
					return true;
				}

				Material m = Material.getMaterial(id);

				if (m == null)
				{
					Send.Failed(sender);
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
						Send.ExtraArguments(sender);
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
					Send.Failed(sender);
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
						Send.Failed(sender);
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
					String[] contents = {"location", "level", "address", "gamemode", "health", "enderchest", "inventory"};

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

					return true;
				}

				else
				{
					Send.Failed(sender);
					return false;
				}
			}

			/* [/pdata ***** *****]*/
			else if (args.length == 2)
			{
				Player player = getServer().getPlayer(args[1]);

				if (player == null)
				{
					Send.PlayerIsNotIn(sender, args);
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
							String[] loc = {"X: ", "Y: ", "Z: ", "Yaw: ", "Pitch: "};
							double[] l = {location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()};

							sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " is in");

							for (int i = 0; i <= 4; i++)
							{
								sender.sendMessage(C.GOLD + loc[i] + C.AQUA + l[i]);
							}

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
									ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								sender.sendMessage(GetPlayerPrefix(player) + player.getName() + C.GOLD + " has no items.");
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
									ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								sender.sendMessage(GetPlayerPrefix(player) + player.getName() + C.GOLD + " has no items.");
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
									ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								sender.sendMessage(GetPlayerPrefix(player) + player.getName() + C.GOLD + " has no items.");
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
									ShowItem(sender, is, i);
								}

								catch (Exception e)
								{
									continue;
								}

								i++;
							}

							if (i == 1)
							{
								sender.sendMessage(GetPlayerPrefix(player) + player.getName() + C.GOLD + " has no items.");
							}

							return true;
						}

						default:
						{
							Send.DoesNotExistInContents(sender, args);
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
				sender.sendMessage(prefix);
				sender.sendMessage(C.GOLD + "/copyinventory <From>");
				sender.sendMessage(C.GOLD + "/copyinventory <From> <To>");
				return true;
			}

			else if (args.length == 1)
			{
				Player from = getServer().getPlayer(args[0]);
				Player to = (Player)sender;

				if (to == from)
				{
					Send.Failed(sender);
					return false;
				}

				else if (from == null)
				{
					Send.PlayerIsNotIn(sender, args);
					return false;
				}

				//TODO: 修正済。未確認。
				to.getInventory().clear();

				Inventory frominv = from.getInventory();
				int i = 0;

				for (ItemStack is: frominv)
				{
					try
					{
						to.getInventory().setItem(i, is);
					}

					catch (Exception e)
					{
						continue;
					}

					finally
					{
						i++;
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
					Send.Failed(sender);
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

				//TODO: 修正済。未確認。
				to.getInventory().clear();

				Inventory frominv = from.getInventory();
				int i = 0;

				for (ItemStack is: frominv)
				{
					try
					{
						to.getInventory().setItem(i, is);
					}

					catch (Exception e)
					{
						continue;
					}

					finally
					{
						i++;
					}
				}

				return true;
			}

			else
			{
				Send.Failed(sender);
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
					sender.sendMessage(C.D_GREEN + "" + i + ": " + C.BLUE + p.getName());
					i++;
				}
			}

			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("rename"))
		{
			if (!(sender instanceof Player))
			{
				Send.SenderIsNotPlayer(sender);
				return false;
			}

			if (args.length == 1)
			{
				Player player = (Player) sender;
				ItemStack is = player.getInventory().getItemInMainHand();
				ItemMeta im = is.getItemMeta();

				try
				{
					im.setDisplayName(args[0]);
					is.setItemMeta(im);
				}

				catch (Exception e)
				{
					Send.Failed(sender);
					return false;
				}

				sender.sendMessage(prefix + C.GOLD + "You changed the item name. => " + args[0]);
				return true;
			}

			Send.ExtraArguments(sender);
			return false;
		}

		return false;
	}
}