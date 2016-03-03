package xyz.civn.civncraft;

import net.md_5.bungee.api.ChatColor;

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
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CIVNCraft extends JavaPlugin implements Listener
{
	public static String prefix = Prefix.prefix;
	public static String[] contents = {"world", "location", "level", "address", "gamemode", "health"};
	public FileConfiguration cf = this.getConfig();

	@Override
	public void onEnable()
	{
		getLogger().info("Enable CIVNCraft! => Ver: " + this.getDescription().getVersion());
		getServer().getPluginManager().registerEvents(this, this);
		this.reloadConfig();
	}

	@Override
	public void onDisable()
	{
		getLogger().info("Disable CIVNCraft!");
		this.saveConfig();
	}

	public void onReload()
	{
		getServer().broadcastMessage(prefix + C.GREEN + C.B + "Reloading!");
		this.saveConfig();
		this.reloadConfig();
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("hub"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			Player p = (Player) sender;
			String hubW =  cf.getString("hub.world");
			double hubX =  cf.getDouble("hub.x");
			double hubY =  cf.getDouble("hub.y");
			double hubZ =  cf.getDouble("hub.z");
			float hubYAW = (float) cf.getDouble("hub.yaw");
			float hubPITCH = (float) cf.getDouble("hub.pitch");

			if (hubW == null)
			{
				SendErrorMessage.HubWasNotSet(sender);
				return false;
			}

			p.teleport(new Location (Bukkit.getServer().getWorld (hubW), hubX, hubY, hubZ, hubYAW, hubPITCH));
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("sethub"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			Player p = (Player) sender;

			if (args.length == 0)
			{
				cf.set("hub.world", p.getWorld().getName());
				cf.set("hub.x", p.getLocation().getX());
				cf.set("hub.y", p.getLocation().getY());
				cf.set("hub.z", p.getLocation().getZ());
				cf.set("hub.yaw", p.getLocation().getYaw());
				cf.set("hub.pitch", p.getLocation().getPitch());
				this.saveConfig();
				this.reloadConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			else if (args.length == 4)
			{
				World w = Bukkit.getServer().getWorld(args[0]);

				if (w != null)
				{
					cf.set("hub.world", args[0]);

					try
					{
						for (int i = 1; i <= 3; i++)
						{
							Double.parseDouble (args[i]);
						}
					}

					catch (Exception e)
					{
						SendErrorMessage.Failed(sender);
						return false;
					}

					cf.set("hub.x", Double.parseDouble (args[1]));
					cf.set("hub.y", Double.parseDouble (args[2]));
					cf.set("hub.z", Double.parseDouble (args[3]));
					cf.set("hub.yaw", 0);
					cf.set("hub.pitch", 0);
					this.saveConfig();
					this.reloadConfig();

					SendReportMessage.HubWasChanged(sender);
					return true;
				}

				Bukkit.getServer().broadcastMessage(prefix + C.GOLD + args[0] + " doesn't exist!");
				return false;
			}

			else if (args.length == 6)
			{
				World w = Bukkit.getServer().getWorld(args[0]);

				if (w != null)
				{
					cf.set("hub.world", args[0]);

					try
					{
						for (int i = 1; i <= 5; i++)
						{
							Double.parseDouble (args[i]);
						}
					}

					catch (Exception e)
					{
						SendErrorMessage.Failed(sender);
						return false;
					}

					cf.set("hub.x", Double.parseDouble (args[1]));
					cf.set("hub.y", Double.parseDouble (args[2]));
					cf.set("hub.z", Double.parseDouble (args[3]));
					cf.set("hub.yaw", Double.parseDouble (args[4]));
					cf.set("hub.pitch", Double.parseDouble (args[5]));
					this.saveConfig();
					this.reloadConfig();

					SendReportMessage.HubWasChanged(sender);
					return true;
				}

				SendErrorMessage.DoesNotExist(sender, args);
				return false;
			}

			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("hubloc"))
		{
			if (args.length == 0)
			{
				SendReportMessage.ShowHubLocation(sender, cf);
				return true;
			}

			SendErrorMessage.ExtraArguments(sender);
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("seppuku"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			if (args.length == 0)
			{
				Player p = (Player) sender;

				p.setHealth(0);
				return true;
			}

			SendErrorMessage.ExtraArguments(sender);
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("civn"))
		{
			if (args.length == 0)
			{
				SendReportMessage.ShowInfomation(sender, this);
				return true;
			}

			else if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("?"))
			{
				Player p = (Player) sender;

				if (p.isOp())
				{
					SendHelpMessage.ShowOpHelp(sender);
					return true;
				}

				SendHelpMessage.ShowHelp(sender);
				return true;
			}

			SendErrorMessage.ExtraArguments(sender);
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("asiba"))
		{
			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			if (args.length == 1 | args.length == 2)
			{
				Player p = (Player) sender;
				Location l = p.getLocation();

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
						String block = args[1].toUpperCase();
						Material m = Material.getMaterial(block);

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

				try
				{
					Integer.parseInt(args[0]);
				}

				catch (Exception e)
				{
					String block = args[0].toUpperCase();
					Material m = Material.getMaterial(block);

					if (m == null)
					{
						sender.sendMessage(prefix + C.RED + args[0] + C.GOLD + " doesn't exist!");
						return false;
					}

					if (args.length == 2)
					{
						try
						{
							Integer.parseInt(args[1]);
						}

						catch (Exception er)
						{
							SendErrorMessage.Failed(sender);
							return false;
						}

						Integer am = new Integer (args[1]);
						int amount = (int) am;

						for (int i = 1; i <= amount; i++)
						{
							l.setY(l.getY() - 1);
							l.getBlock().setType(m);
						}

						return false;
					}

					l.setY(l.getY() - 1);
					l.getBlock().setType(m);
					return true;
				}

				Integer b = new Integer (args[0]);
				int block = (int) b;
				Material m = Material.getMaterial(block);

				if (m == null)
				{
					sender.sendMessage(prefix + C.AQUA + "ItemID [" + C.RED + block + C.AQUA + "]" + C.GOLD + " doesn't exist!");
					return false;
				}

				if (args.length == 2)
				{
					try
					{
						Integer.parseInt(args[1]);
					}

					catch (Exception e)
					{
						SendErrorMessage.Failed(sender);
						return false;
					}

					Integer am = new Integer (args[1]);
					int amount = (int) am;

					for (int i = 1; i <= amount; i++)
					{
						l.setY(l.getY() - 1);
						l.getBlock().setType(m);
					}

					return false;
				}

				l.setY(l.getY() - 1);
				l.getBlock().setType(m);
				return true;
			}

			SendErrorMessage.ExtraArguments(sender);
			return false;
		}

		if (cmd.getName().equalsIgnoreCase("pdata"))
		{
			if (args.length == 0)
			{
				sender.sendMessage(prefix + C.GOLD + "/pdata command was made by " + C.RED + "" + C.B + "CIVN");
				return true;
			}

			else if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help"))
			{
				SendHelpMessage.ShowPdataHelp(sender);

				for (String c : contents)
				{
					sender.sendMessage(C.BLUE + "- " + C.AQUA + c);
				}

				return true;
			}

			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
				return false;
			}

			Player player = Bukkit.getServer().getPlayer(args[1]);

			//プレイヤー取得失敗
			if (player == null)
			{
				SendErrorMessage.PlayerIsNotIn(sender, args);
				return false;
			}

			if (args[0].equalsIgnoreCase("world"))
			{
				String world = player.getWorld().getName();

				sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + " is in " + C.D_GREEN + world + C.BLUE + "!");
				return true;
			}

			else if (args[0].equalsIgnoreCase("health"))
			{
				double health = player.getHealth();

				sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + "'s health is " + C.D_GREEN + health + C.BLUE + "!");
				return true;

			}

			else if (args[0].equalsIgnoreCase("location"))
			{
				Location location = player.getLocation();
				double[] l = {location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()};

				SendReportMessage.ShowPlayerLocation(sender, player, l, GetPlayerPrefix(player));
				return true;
			}

			else if (args[0].equalsIgnoreCase("level"))
			{
				int level = player.getLevel();

				sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + "'s level is " + C.D_GREEN + level + C.BLUE + "!");
				return true;
			}

			else if (args[0].equalsIgnoreCase("address") || args[0].equalsIgnoreCase("ip"))
			{
				String address = player.getAddress().getAddress().toString().replace("/", "");

				sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + "'s address is " + C.D_GREEN + address + C.BLUE + "!");
				return true;
			}

			else if (args[0].equalsIgnoreCase("gamemode"))
			{
				String gamemode = player.getGameMode().name();

				sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + "'s gamemode is " + C.D_GREEN + gamemode + C.BLUE + "!");
				return true;
			}

			SendErrorMessage.DoesNotExistInContents(sender, args);
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("ops"))
		{
			sender.sendMessage(prefix + C.BLUE + "Online Operators!");

			int i = 1;

			for (Player p : Bukkit.getServer().getOnlinePlayers())
			{
				if (p.isOp())
				{
					sender.sendMessage(C.D_GREEN + "" + i + ": " + GetPlayerPrefix(p) + p.getName());
					i++;
				}
			}

			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("rename"))
		{
			if (args.length == 1)
			{
				Player player = (Player) sender;

				if (player == null)
				{
					sender.sendMessage(prefix + C.BLUE + args[1] + C.RED + " isn't in this server!");
					return false;
				}

				try
				{
					player.getInventory().getItemInMainHand().getItemMeta().setDisplayName(args[0]);
				}

				catch (Exception e)
				{
					SendErrorMessage.Failed(sender);
					return false;
				}
			}

			return true;
		}

		return false;
	}


	public String GetPlayerPrefix(Player player)
	{
		PermissionUser user = PermissionsEx.getUser(player);
		String prefix = user.getPrefix();
		prefix = prefix.replace("%world", player.getWorld().getName());
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	@EventHandler
	public void onPlayerLevelChangeEvent (PlayerLevelChangeEvent e)
	{
		Player p = e.getPlayer();
		String pn = p.getName();
		int newl = e.getNewLevel();
		int oldl = e.getOldLevel();

		if (newl > oldl)
		{
			Bukkit.broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " leveled up " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}

		else if (newl < oldl)
		{
			Bukkit.broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " leveled down " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}
	}

	@EventHandler
	public void onPlayerExpChangeEvent (PlayerExpChangeEvent e)
	{
		Player p = e.getPlayer();
		String pn = p.getName();
		int exp = e.getAmount();

		Bukkit.broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " got " + C.RED + exp + C.GOLD + " EXP!");
	}

	@EventHandler
	public void onPlayerJoinEvent (PlayerJoinEvent e)
	{
		Player player = e.getPlayer();

		e.setJoinMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has joined.");

		for (Player players: Bukkit.getServer().getOnlinePlayers())
		{
			if (players.isOp())
			{
				players.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s IP Address is " + C.GREEN + player.getAddress().getAddress().toString().replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerQuitEvent (PlayerQuitEvent e)
	{
		Player player = e.getPlayer();

		e.setQuitMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has left.");
	}

	@EventHandler
	public void onPlayerChangedWorldEvent (PlayerChangedWorldEvent e)
	{
		Player player = e.getPlayer();
		String from = e.getFrom().getName();
		String to = player.getWorld().getName();

		Bukkit.getServer().broadcastMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " has moved from " + C.GREEN + from + C.GOLD + " to " + C.GREEN + to);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent (EntityDamageByEntityEvent e)
	{
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
				damager.sendMessage(prefix + C.RED + "You reset his name!");
				return;
			}

			damager.sendMessage(prefix + C.RED + "You changed his name! => " + name);
		}
	}
}