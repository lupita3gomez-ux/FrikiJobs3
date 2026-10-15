package com.tuempresa.jobs.gui;

import com.tuempresa.jobs.JobsPlugin;
import com.tuempresa.jobs.model.Job;
import com.tuempresa.jobs.model.JobTier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobsGUI {

    public static void openJobsMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GREEN + "💼 Selección de Trabajos");

        String currentJob = JobsPlugin.getInstance().getPlayerJobManager().getPlayerJob(player.getUniqueId());

        inv.setItem(10, createJobItem(Material.DIAMOND_PICKAXE, "minero", currentJob));
        inv.setItem(11, createJobItem(Material.DIAMOND_HOE, "granjero", currentJob));
        inv.setItem(12, createJobItem(Material.ENCHANTING_TABLE, "hechicero", currentJob));
        inv.setItem(13, createJobItem(Material.FISHING_ROD, "pescador", currentJob));
        inv.setItem(14, createJobItem(Material.NETHERITE_SWORD, "guerrero", currentJob));
        inv.setItem(15, createJobItem(Material.NETHERITE_AXE, "lenador", currentJob));
        inv.setItem(16, createJobItem(Material.BREWING_STAND, "alquimista", currentJob));

        if (currentJob != null) {
            Job job = JobsPlugin.getInstance().getJobManager().getJob(currentJob);
            String jobName = job != null ? job.getName() : currentJob;
            double penalty = JobsPlugin.getInstance().getConfig().getDouble("quit_penalty", 50.0);

            ItemStack leaveItem = new ItemStack(Material.BARRIER);
            ItemMeta meta = leaveItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.RED + "❌ Abandonar Trabajo Actual");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Trabajo: " + ChatColor.YELLOW + jobName);
                lore.add("");
                lore.add(ChatColor.RED + "Multa por abandonar: $" + penalty);
                lore.add("");
                lore.add(ChatColor.YELLOW + "▶ Haz clic para renunciar");
                meta.setLore(lore);
                leaveItem.setItemMeta(meta);
            }
            inv.setItem(26, leaveItem);
        }

        player.openInventory(inv);
    }

    public static void openJobDetailMenu(Player player, String jobId) {
        Job job = JobsPlugin.getInstance().getJobManager().getJob(jobId);
        if (job == null) return;

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "📋 " + job.getName());

        int slot = 10;
        for (Map.Entry<String, JobTier> entry : job.getTiers().entrySet()) {
            if (slot > 16) break;
            String tierId = entry.getKey();
            JobTier tier = entry.getValue();

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GOLD + "Nivel " + tierId);
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Requisito XP: " + ChatColor.AQUA + tier.getRequiredXp());
                lore.add(ChatColor.GRAY + "Recompensa Dinero: " + ChatColor.GREEN + "$" + tier.getRewardMoney());
                lore.add(ChatColor.GRAY + "Recompensa XP: " + ChatColor.YELLOW + tier.getRewardXp() + " XP");
                lore.add("");
                lore.add(ChatColor.YELLOW + "Especialización:");
                lore.add(ChatColor.WHITE + tier.getInfo());
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            inv.setItem(slot, item);
            slot++;
        }

        String currentJob = JobsPlugin.getInstance().getPlayerJobManager().getPlayerJob(player.getUniqueId());
        ItemStack actionItem;
        ItemMeta actionMeta;

        if (jobId.equalsIgnoreCase(currentJob)) {
            actionItem = new ItemStack(Material.LIME_CONCRETE);
            actionMeta = actionItem.getItemMeta();
            if (actionMeta != null) {
                actionMeta.setDisplayName(ChatColor.GREEN + "✔ Ya tienes este trabajo");
                actionItem.setItemMeta(actionMeta);
            }
        } else {
            actionItem = new ItemStack(Material.EMERALD);
            actionMeta = actionItem.getItemMeta();
            if (actionMeta != null) {
                actionMeta.setDisplayName(ChatColor.GOLD + "✨ Unirse a " + job.getName());
                List<String> lore = new ArrayList<>();
                if (currentJob != null) {
                    double penalty = JobsPlugin.getInstance().getConfig().getDouble("quit_penalty", 50.0);
                    lore.add(ChatColor.RED + "⚠ Tienes un trabajo activo.");
                    lore.add(ChatColor.RED + "Cambiar te costará $" + penalty + " de multa.");
                }
                lore.add("");
                lore.add(ChatColor.GREEN + "▶ Haz clic para confirmar");
                actionMeta.setLore(lore);
                actionItem.setItemMeta(actionMeta);
            }
        }
        inv.setItem(22, actionItem);

        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.YELLOW + "⬅ Volver al Menú Principal");
            backItem.setItemMeta(backMeta);
        }
        inv.setItem(18, backItem);

        player.openInventory(inv);
    }

    private static ItemStack createJobItem(Material material, String jobId, String currentJob) {
        Job job = JobsPlugin.getInstance().getJobManager().getJob(jobId);
        String name = job != null ? job.getName() : jobId;
        String desc = job != null ? job.getDescription() : "";

        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "⚒ " + name);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + desc);
            lore.add("");
            if (jobId.equalsIgnoreCase(currentJob)) {
                lore.add(ChatColor.GREEN + "✔ Trabajo Actual");
            } else {
                lore.add(ChatColor.YELLOW + "Tier Actual: " + ChatColor.GREEN + "TI");
            }
            lore.add("");
            lore.add(ChatColor.AQUA + "▶ Haz clic para ver especificaciones y Tiers");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }
}
