package me.poilet66.customitems.Command;

import me.poilet66.customitems.CustomItems;
import me.poilet66.customitems.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItemCommand implements CommandExecutor {

    private final CustomItems main;

    public GiveItemCommand(CustomItems main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        if(!(sender.hasPermission("customitems.give"))) {
            sender.sendMessage(ChatColor.RED + "You dont have permission to use that command.");
            return true;
        }
        Player player = (Player) sender;
        if(args.length < 2) {
            player.sendMessage(ChatColor.RED + "You have not provided enough arguments");
            return true;
        }
        if(!main.getIR().newItemMap.containsKey(args[0].toUpperCase())) {
            sender.sendMessage(ChatColor.RED + "That item does not exist.");
            return true;
        }
        ItemStack itemToSet = main.getIR().newItemMap.get(args[0].toUpperCase()).getItem();
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The amount you have entered is not a number");
            return true;
        }
        if(amount < 1 || amount > itemToSet.getType().getMaxStackSize()) {
            player.sendMessage(String.format(ChatColor.RED + "Invalid amount entered, max stack size for %s is %s", args[0], itemToSet.getType().getMaxStackSize()));
            return true;
        }
        itemToSet.setAmount(amount);
        Utils.setAtFirst(player, itemToSet);

        return true;
    }


}
