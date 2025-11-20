package com.example;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PluginBoob extends JavaPlugin implements TabExecutor {

    private static final List<Double> ALLOWED_HEIGHTS = Arrays.asList(0.65, 0.75, 0.85, 1.0);

    @Override
    public void onEnable() {
        getLogger().info("PluginBoob включен!");
        getCommand("size").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginBoob отключен!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("size")) {
            if (args.length != 2) {
                sender.sendMessage("Использование: /size <игрок> <высота>");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("Игрок не найден!");
                return false;
            }

            try {
                double height = Double.parseDouble(args[1]);
                if (ALLOWED_HEIGHTS.contains(height) && (height == 1.0 || hasPermissionToSetHeight(sender, height))) {
                    AttributeInstance attribute = target.getAttribute(Attribute.GENERIC_SCALE);
                    if (attribute != null) {
                        attribute.setBaseValue(height);
                        sender.sendMessage("Высота " + target.getName() + " установлена в " + height);
                    } else {
                        sender.sendMessage("Не удалось установить высоту для " + target.getName()); // no big boob and very small boob
                    }
                } else {
                    sender.sendMessage("У вас нет прав для установки этой высоты или значение некорректно!");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Неверное значение высоты!");
            }

            return true;
        }
        return false;
    }

    private boolean hasPermissionToSetHeight(CommandSender sender, double height) {
        return (height == 0.65 && sender.hasPermission("pluginboob.setheight.065")) ||
                (height == 0.75 && sender.hasPermission("pluginboob.setheight.075")) ||
                (height == 0.85 && sender.hasPermission("pluginboob.setheight.085"));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("size")) {
            if (args.length == 1) {
                return null; 
            }
            if (args.length == 2) {
                return Arrays.asList("0.65", "0.75", "0.85", "1.0");
            }
        }
        return Collections.emptyList();
    }
}

