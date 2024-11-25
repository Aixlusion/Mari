package net.aixlusion.mari.functions;

import org.bukkit.Bukkit;
import net.aixlusion.mari.main;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class monitor {

    private final main plugin;
    private final OperatingSystemMXBean osBean;

    public monitor(main plugin) {
        this.plugin = plugin;
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        startMonitoring();
    }

    private void startMonitoring() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkResources();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20 * 60); // Run every minute
    }

    private void checkResources() {
        int ramThreshold = plugin.getConfigManager().getConfig().getInt("thresholds.ram");
        int tpsThreshold = plugin.getConfigManager().getConfig().getInt("thresholds.tps");
        int cpuThreshold = plugin.getConfigManager().getConfig().getInt("thresholds.cpu");
        int lagTpsThreshold = plugin.getConfigManager().getConfig().getInt("thresholds.lag_tps");
        boolean notificationsEnabled = plugin.getConfigManager().getConfig().getBoolean("notifications.enabled");
        String permission = plugin.getConfigManager().getConfig().getString("notifications.permission");

        // Check RAM usage
        double maxMemory = Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0); // MB
        double usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024.0 * 1024.0); // MB
        double ramUsage = (usedMemory / maxMemory) * 100;

        if (ramUsage > ramThreshold) {
            String message = plugin.getMessageManager().get("notifications.ram_exceeded")
                    .replace("{usage}", String.format("%.2f", ramUsage));
            notify(message, permission, notificationsEnabled);
        }

        // Check TPS
        double tps = Bukkit.getServer().getTPS()[0]; // 1-minute average TPS
        if (tps < tpsThreshold) {
            String message = plugin.getMessageManager().get("notifications.tps_low")
                    .replace("{tps}", String.format("%.2f", tps));
            notify(message, permission, notificationsEnabled);

            if (tps < lagTpsThreshold) { // Severe lag detection
                generateHeapDump();
            }
        }

        // Check CPU usage
        double cpuUsage = getCpuUsage();
        if (cpuUsage > cpuThreshold) {
            String message = plugin.getMessageManager().get("notifications.cpu_exceeded")
                    .replace("{usage}", String.format("%.2f", cpuUsage));
            notify(message, permission, notificationsEnabled);
        }
    }

    private double getCpuUsage() {
        return osBean.getSystemLoadAverage() * 100; // System-wide CPU usage as a percentage
    }

    private void generateHeapDump() {
        File heapDumpFile = new File(plugin.getDataFolder(), "heapdump.hprof");
        try {
            ManagementFactory.getPlatformMXBean(com.sun.management.HotSpotDiagnosticMXBean.class)
                    .dumpHeap(heapDumpFile.getAbsolutePath(), true);
            String message = plugin.getMessageManager().get("notifications.heap_dump")
                    .replace("{file}", heapDumpFile.getAbsolutePath());
            plugin.getLogger().info(message);
        } catch (IOException e) {
            String message = plugin.getMessageManager().get("notifications.heap_dump_failed")
                    .replace("{error}", e.getMessage());
            plugin.getLogger().severe(message);
        }
    }

    private void notify(String message, String permission, boolean enabled) {
        if (!enabled) return;

        plugin.getLogger().info(message); // Log to console
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(message));
    }
}