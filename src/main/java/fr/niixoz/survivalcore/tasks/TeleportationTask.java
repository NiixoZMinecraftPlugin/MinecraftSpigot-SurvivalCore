package fr.niixoz.survivalcore.tasks;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class TeleportationTask extends BukkitRunnable {

	private static List<TeleportationTask> tasks = new ArrayList<>();
	private static List<Player> playersInTask = new ArrayList<>();
	private int second;
	private Player player;
	private Location oldLocation;

	private Location loc;
	private boolean isTeleport = false;
	private Callable<Boolean> callable;
	private boolean freeMove = false;
	private boolean instantTp = false;

	public TeleportationTask(Player player, Location loc, int second) {
		this(player, loc, second, null);
	}

	/**
	 * @param player The player basically.
	 * @param loc Location to teleport the player at.
	 * @param second Time in second for the teleportation.
	 * @param callable Call after the teleportation success.
	 */
	public TeleportationTask(Player player, Location loc, int second, Callable<Boolean> callable) {
		second = (player.hasPermission(PermissionEnum.INSTANT_TP.getPermission()) ? 0 : second);
		this.player = player;
		this.loc = loc;
		this.second = second;
		this.callable = callable;
		instantTp = (second == 0);
	}
	
	public void start() {
		playersInTask.add(this.player);
		tasks.add(this);
		oldLocation = player.getLocation();
		if(instantTp) {
			player.teleport(loc);
			stop(false);
			return;
		}
		this.runTaskTimer(SurvivalCore.getInstance(), 0, 20);
	}
	
	public void stop(boolean isCancel) {
		if(!instantTp)
			this.cancel();
		tasks.remove(this);
		if(!instantTp)
			Bukkit.getScheduler().cancelTask(this.getTaskId());
		playersInTask.remove(this.player);
		isTeleport = !isCancel;

		if(isCancel) {
			this.player.sendMessage("§6[ §eTéléportation §6] §eTéléportation annulée.");
			return;
		}

		try {
			if(callable != null)
				callable.call();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if(!freeMove && (player.getLocation().getBlockX() != oldLocation.getBlockX()
				|| player.getLocation().getBlockY() != oldLocation.getBlockY()
				|| player.getLocation().getBlockZ() != oldLocation.getBlockZ())
		) {
			stop(true);
			return;
		}

		if(this.second == 0) {
			player.sendMessage("§6[ §eTéléportation §6] §eTéléportation en cours...");
			if(loc != null)
				player.teleport(this.loc);
			this.stop(false);
			
			return;
			
		}
		if(this.second == 1) {
			this.player.sendMessage("§6[ §eTéléportation §6] §eTéléportation dans " + this.second + " seconde.");
		}
		else {
			this.player.sendMessage("§6[ §eTéléportation §6] §eTéléportation dans " + this.second + " secondes.");
		}
		this.second--;
	}

	public boolean isTeleport() {
		return isTeleport;
	}

	public Location getOldLocation() {
		return oldLocation;
	}

	public Location getNewLocation() {
		return loc;
	}

	public void setFreeMove(boolean freeMove) {
		this.freeMove = freeMove;
	}

	public boolean isFreeMove() {
		return freeMove;
	}

	public static boolean isInTeleportation(Player player) {
		return playersInTask.contains(player);
	}

	public static TeleportationTask getTask(Player player) {
		for(TeleportationTask task : tasks) {
			if(task.player == player) {
				return task;
			}
		}
		return null;
	}

}
