package me.poilet66.customitems.Command;

import me.poilet66.customitems.CustomItems;
import me.poilet66.customitems.Items.CustomItemBase;
import me.poilet66.customitems.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
        Player player = (Player) sender;
        if(!main.getIR().newItemMap.containsKey(args[0].toUpperCase())) {
            sender.sendMessage(ChatColor.RED + "That item does not exist.");
            return true;
        }
        int amount = 1;
        try {
            amount = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The amount you have entered is not a number");
        }

        for(Map.Entry<String, CustomItemBase> entry : main.getIR().newItemMap.entrySet()) {
            sender.sendMessage(String.format("%s : %s", entry.getKey(), entry.getValue().getItem().getType()));
        }

        ItemStack itemToSet = main.getIR().newItemMap.get(args[0].toUpperCase()).getItem();

        player.getInventory().setItem(0, itemToSet);

        //itemToSet.setAmount(amount);
        
        /*if(Utils.setItemAtFirstSlot(player, itemToSet)) { //set slot, send message if couldnt be done TODO: this dont work
            sender.sendMessage(ChatColor.RED + "Target inventory full.");
            return true;
        }*/


        return true;
    }


}
