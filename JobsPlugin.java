package com.tuempresa.jobs;

import com.tuempresa.jobs.gui.JobsGUI;
import com.tuempresa.jobs.listener.GUIListener;
import com.tuempresa.jobs.listener.JobActionListener;
import com.tuempresa.jobs.manager.EconomyManager;
import com.tuempresa.jobs.manager.JobManager;
import com.tuempresa.jobs.manager.PlayerJobManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class JobsPlugin extends JavaPlugin {

    private static JobsPlugin instance;
    private JobManager jobManager;
    private EconomyManager economyManager;
    private PlayerJobManager playerJobManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.economyManager = new EconomyManager();
        if (!economyManager.setupEconomy()) {
            getLogger().warning("Vault no encontrado. La integración económica se deshabilitó.");
        }

        this.jobManager = new JobManager(this);
        jobManager.loadJobs();

        this.playerJobManager = new PlayerJobManager(this);

        // Registrar eventos, menú y protección anti-exploit
        getServer().getPluginManager().registerEvents(new JobActionListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);

        getLogger().info("JobsPlugin inicializado correctamente con sistema de Tiers avanzados y penalización.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("jobs")) {
            if (sender instanceof Player player) {
                JobsGUI.openJobsMenu(player);
                return true;
            } else {
                sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            }
        }
        return false;
    }

    public static JobsPlugin getInstance() {
        return instance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public PlayerJobManager getPlayerJobManager() {
        return playerJobManager;
    }
}
