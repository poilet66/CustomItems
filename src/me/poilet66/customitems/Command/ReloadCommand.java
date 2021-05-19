package me.poilet66.customitems.Command;

import me.poilet66.customitems.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final CustomItems main;

    public ReloadCommand(CustomItems main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        if(sender.hasPermission("customitems.reload")) {
           main.reloadConfig();
           sender.sendMessage(ChatColor.GREEN + "Config reloaded.");
           return true;
        }
        sender.sendMessage(ChatColor.RED + "You dont have permission to do that.");
        return true;
    }
}
