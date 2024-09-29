package fr.niixoz.survivalcore.tasks;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class WaitingTeleportTask extends BukkitRunnable {

	public static List<WaitingTeleportTask> tasks = new ArrayList<>();
	private static List<Player> playersInTask = new ArrayList<>();
	protected Player player;
	protected Player target;
	protected int waiting;
	protected Callable<Boolean> callable;

	public WaitingTeleportTask(Player player, Player target, int second) {
		this(player, target, second, null);
	}

	/**
	 * @param player The player basically.
	 * @param waiting Max time to wait before auto deny.
	 * @param callable Call after the teleportation success.
	 */
	public WaitingTeleportTask(Player player, Player target, int waiting, Callable<Boolean> callable) {
		this.waiting = waiting;
		this.player = player;
		this.target = target;
		this.callable = callable;
	}
	
	public void start() {
		playersInTask.add(this.player);
		tasks.add(this);
		this.runTaskTimer(SurvivalCore.getInstance(), 0, 20);
	}

	public void stop(boolean isCancel) {
		this.stop(isCancel, false);
	}
	
	public void stop(boolean isCancel, boolean autoCancel) {
		this.cancel();
		tasks.remove(this);
		Bukkit.getScheduler().cancelTask(this.getTaskId());
		playersInTask.remove(this.player);

		if(autoCancel) {
			this.player.sendMessage("§6[ §eTéléportation §6] §eLa demande de téléportation a expirée.");
			return;
		}

		if(isCancel) {
			this.player.sendMessage("§6[ §eTéléportation §6] §e" + this.target.getName() + " §ea refusé la téléportation.");
			return;
		}

		this.player.sendMessage("§6[ §eTéléportation §6] §e" + this.target.getName() + " §ea accepté la téléportation.");

		try {
			if(callable != null)
				callable.call();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void accept() {
		TeleportationTask teleport = new TeleportationTask(this.player, this.target.getLocation(), Config.tpaTeleportationDelay, () -> {
			try {
				if(callable != null)
					callable.call();
			} catch(Exception e) {
				e.printStackTrace();
			}
			return true;
		});
		teleport.setFreeMove(Config.tpaFreeMove);
		if(TeleportationTask.isInTeleportation(player)) {
			TeleportationTask pTask = TeleportationTask.getTask(player);
			if(pTask != null) {
				pTask.stop(true);
			}
		}
		teleport.start();
		this.stop(false);
	}

	public void deny() {
		this.stop(true);
	}

	@Override
	public void run() {
		if(this.waiting == 0) {
			this.stop(false, true);
			return;
			
		}
		this.waiting--;
	}

	public int getWaiting() {
		return waiting;
	}

	public Player getTarget() {
		return target;
	}

	public Player getPlayer() {
		return player;
	}

	public static boolean isInTeleportation(Player player) {
		return playersInTask.contains(player);
	}

	public static WaitingTeleportTask getTaskPlayer(Player player) {
		for(WaitingTeleportTask task : tasks) {
			if(task.getPlayer().equals(player))
				return task;
		}
		return null;
	}

	public static WaitingTeleportTask getTaskTarget(Player player) {
		for(WaitingTeleportTask task : tasks) {
			if(task.getTarget().equals(player))
				return task;
		}
		return null;
	}

	public static WaitingTeleportTask getTask(Player player, Player target) {
		for(WaitingTeleportTask task : tasks) {
			if(task.getPlayer().equals(player) && task.getTarget().equals(target))
				return task;
		}
		return null;
	}

	public static List<WaitingTeleportTask> getTargetTasks(Player player) {
		List<WaitingTeleportTask> tasks = new ArrayList<>();
		for(WaitingTeleportTask task : WaitingTeleportTask.tasks) {
			if(task.getTarget().equals(player))
				tasks.add(task);
		}
		return tasks;
	}

}
