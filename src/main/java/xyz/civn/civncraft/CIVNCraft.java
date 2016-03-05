package xyz.civn.civncraft;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
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

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.civn.civncraft.Constant.C;
import xyz.civn.civncraft.Constant.Prefix;
import xyz.civn.civncraft.SendMessage.SendErrorMessage;
import xyz.civn.civncraft.SendMessage.SendHelpMessage;
import xyz.civn.civncraft.SendMessage.SendReportMessage;

public class CIVNCraft extends JavaPlugin implements Listener
{
	private static String prefix = Prefix.prefix;
	private static String[] contents = {"world", "location", "level", "address", "gamemode", "health", "enderchest", "inventory"};

	private FileConfiguration cf = getConfig();
	private CIVNCraft main = this;

	private String hubW ;
	private double hubX;
	private double hubY;
	private double hubZ;
	private float hubYAW;
	private float hubPITCH;

	private String JoinMessage;
	private String QuitMessage;

	@Override
	public void onEnable()
	{
		getLogger().info("Enable CIVNCraft! => Ver: " + getDescription().getVersion());
		getServer().getPluginManager().registerEvents(this, this);

		Reload();
	}

	@Override
	public void onDisable()
	{
		getLogger().info("Disable CIVNCraft!");
		saveConfig();
	}

	public void onReload()
	{
		getServer().broadcastMessage(prefix + C.GREEN + C.B + "Reloading!");
		saveConfig();
		reloadConfig();
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

			if (hubW == null)
			{
				SendErrorMessage.HubWasNotSet(sender);
				return false;
			}

			World world = getServer().getWorld (hubW);

			if (world == null)
			{
				SendErrorMessage.WorldDoesNotExist(world, sender);
				return false;
			}

			p.teleport(new Location (world, hubX, hubY, hubZ, hubYAW, hubPITCH));
			return true;
		}

		else if (cmd.getName().equalsIgnoreCase("sethub"))
		{
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
				reloadConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			else if (args.length == 4)
			{
				World w = getServer().getWorld(args[0]);

				if (w == null)
				{
					getServer().broadcastMessage(prefix + C.GOLD + args[0] + " doesn't exist!");
					return false;
				}

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
				saveConfig();
				reloadConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			else if (args.length == 6)
			{
				World w = getServer().getWorld(args[0]);

				if (w == null)
				{
					SendErrorMessage.DoesNotExist(sender, args);
					return false;
				}

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
				saveConfig();
				reloadConfig();

				SendReportMessage.HubWasChanged(sender);
				return true;
			}

			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("hubloc"))
		{
			if (args.length != 0)
			{
				SendErrorMessage.ExtraArguments(sender);
				return false;
			}

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

			if (args.length == 0)
			{
				SendErrorMessage.ExtraArguments(sender);
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
				SendReportMessage.ShowInfomation(sender, this);
				return true;
			}

			else if (args[0].equalsIgnoreCase("help") | args[0].equalsIgnoreCase("?"))
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
				Reload();
			}

			SendErrorMessage.ExtraArguments(sender);
			return false;
		}

		else if (cmd.getName().equalsIgnoreCase("asiba"))
		{
			if (args.length == 0)
			{
				SendHelpMessage.ShowAsibaHelp(sender);
				return true;
			}

			if (!(sender instanceof Player))
			{
				SendErrorMessage.SenderIsNotPlayer(sender);
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

					if (setMaterial(m, l, sender, args) == false)
					{
						return false;
					}

					return true;
				}

				Material m = Material.getMaterial(id);

				if (setMaterial(m, l, sender, args) == false)
				{
					return false;
				}

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
				int amount;

				try
				{
					amount = Integer.parseInt(args[1]);
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

					if (setMaterial(m, l, sender, args) == false)
					{
						return false;
					}

					return true;
				}

				Material m = Material.getMaterial(id);

				if (m == null)
				{
					sender.sendMessage(prefix + C.RED + args[1] + C.GOLD + " doesn't exist!");
					return false;
				}

				for (int i = 1; i <= amount; i++)
				{
					l.setY(l.getY() - 1);
					l.getBlock().setType(m);
				}

				return true;
			}

			else
			{
				SendErrorMessage.ExtraArguments(sender);
				return false;
			}
		}

		if (cmd.getName().equalsIgnoreCase("pdata"))
		{
			/* [/pdata]*/
			if (args.length == 0)
			{
				sender.sendMessage(prefix + C.GOLD + "/pdata command was made by " + C.RED + "" + C.B + "CIVN");
				return true;
			}

			/* [/pdata *****]*/
			else if (args.length == 1)
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
					if (args[0].equalsIgnoreCase("world"))
					{
						String world = player.getWorld().getName();

						sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + " is in " + C.D_GREEN + world + C.BLUE + "!");
						return true;
					}

					else if (args[0].equalsIgnoreCase("health"))
					{
						double health = player.getHealth();

						sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s health is " + C.D_GREEN + health + C.BLUE + "!");
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

						sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s level is " + C.D_GREEN + level + C.BLUE + "!");
						return true;
					}

					else if (args[0].equalsIgnoreCase("address") | args[0].equalsIgnoreCase("ip"))
					{
						String address = player.getAddress().getAddress().toString().replace("/", "");

						sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s address is " + C.D_GREEN + address + C.BLUE + "!");
						return true;
					}

					else if (args[0].equalsIgnoreCase("gamemode"))
					{
						String gamemode = player.getGameMode().name();

						sender.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s gamemode is " + C.D_GREEN + gamemode + C.BLUE + "!");
						return true;
					}

					else if (args[0].equalsIgnoreCase("enderchest") | (args[0].equalsIgnoreCase("ec")))
					{
						int i = 1;

						sender.sendMessage(prefix);

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
							SendReportMessage.HasNoItems(sender, player);
						}

						return true;
					}

					else if (args[0].equalsIgnoreCase("inventory") | (args[0].equalsIgnoreCase("inv")))
					{
						int i = 1;

						sender.sendMessage(prefix);

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
							SendReportMessage.HasNoItems(sender, player);
						}

						return true;
					}
				}
			}

			else
			{
				SendErrorMessage.Failed(sender);
				return false;
			}
		}

		else if (cmd.getName().equalsIgnoreCase("copyinventory"))
		{
			if (args.length == 0)
			{
				SendErrorMessage.MissingArguments(sender);
				return false;
			}

			else if (args.length == 1)
			{
				Player to = (Player)sender;
				Player from = getServer().getPlayer(args[0]);

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

		/*機能しない*/
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
					player.getInventory().getItemInHand().getItemMeta().setDisplayName(args[0]);
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

		return false;
	}

	private void Reload()
	{
		this.saveConfig();
		this.reloadConfig();

		hubW =  cf.getString("hub.world");
		hubX =  cf.getDouble("hub.x");
		hubY =  cf.getDouble("hub.y");
		hubZ =  cf.getDouble("hub.z");
		hubYAW = (float) cf.getDouble("hub.yaw");
		hubPITCH = (float) cf.getDouble("hub.pitch");

		JoinMessage = cf.getString("Message.JoinMessage");
		QuitMessage = cf.getString("Message.QuitMessage");
	}

	private String GetPlayerPrefix(Player player)
	{
		PermissionUser user = PermissionsEx.getUser(player);
		String prefix = user.getPrefix();

		prefix = ChatFormatter(prefix, player);

		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	private String ChatFormatter(String text, Player player)
	{
		text = text.replace("%player", player.getName());
		text = text.replace("%world", player.getWorld().getName());
		text = text.replace("%prefix", GetPlayerPrefix(player));
		text = text.replace("%world", player.getWorld().getName());
		text = text.replace("%level", String.valueOf(player.getLevel()));

		return ChatColor.translateAlternateColorCodes('&', text);
	}

	private boolean setMaterial(Material m, Location l, CommandSender sender, String[] args)
	{
		if (m == null)
		{
			SendErrorMessage.DoesNotExist(sender, args);
			return false;
		}

		l.setY(l.getY() - 1);
		l.getBlock().setType(m);
		return true;
	}

	@EventHandler
	private void onPlayerLevelChangeEvent (PlayerLevelChangeEvent e)
	{
		if (cf.getString("EventMessage.ChangedLevel") != "true")
		{
			return;
		}

		Player p = e.getPlayer();
		String pn = p.getName();
		int newl = e.getNewLevel();
		int oldl = e.getOldLevel();

		if (newl > oldl)
		{
			getServer().broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " leveled up " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}

		else if (newl < oldl)
		{
			getServer().broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " leveled down " + C.RED + "Lv." + oldl + C.GOLD + " to " + C.RED + "Lv." + newl + C.GOLD + "!");
		}
	}

	@EventHandler
	private void onPlayerExpChangeEvent (PlayerExpChangeEvent e)
	{
		if (cf.getString("EventMessage.ChangedExp") != "true")
		{
			return;
		}

		Player p = e.getPlayer();
		String pn = p.getName();
		int exp = e.getAmount();

		getServer().broadcastMessage(prefix + GetPlayerPrefix(p) + pn + C.GOLD + " got " + C.RED + exp + C.GOLD + " EXP!");
	}

	@EventHandler
	private void onPlayerJoinEvent (PlayerJoinEvent e)
	{
		Player player = e.getPlayer();

		ChatFormatter(JoinMessage, player);

		e.setJoinMessage(prefix + JoinMessage);

		for (Player players: Bukkit.getServer().getOnlinePlayers())
		{
			if (players.isOp())
			{
				players.sendMessage(prefix + GetPlayerPrefix(player) + player.getName() + C.GOLD + "'s IP: " + C.GREEN + player.getAddress().getAddress().toString().replace("/", ""));
			}
		}
	}

	@EventHandler
	private void onPlayerQuitEvent (PlayerQuitEvent e)
	{
		Player player = e.getPlayer();

		ChatFormatter(QuitMessage, player);

		e.setQuitMessage(prefix + QuitMessage);
	}

	@EventHandler
	private void onPlayerChangedWorldEvent (PlayerChangedWorldEvent e)
	{
		if (cf.getString("EventMessage.MovedWorld") != "true")
		{
			return;
		}

		Player player = e.getPlayer();
		String from = e.getFrom().getName();
		String to = player.getWorld().getName();

		SendReportMessage.MovedWorld(player, from, to, GetPlayerPrefix(player));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	private void onEntityDamageByEntityEvent (EntityDamageByEntityEvent e)
	{
		if (cf.getString("EventMessage.EasyNameChanger") != "true")
		{
			return;
		}

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
				return;
			}

			SendReportMessage.ChangedName(name, damager);
		}
	}
}