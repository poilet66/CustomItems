package me.poilet66.customitems.Command;

import me.poilet66.customitems.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiveItemTabCompleter implements TabCompleter {

    private final CustomItems main;

    public GiveItemTabCompleter(CustomItems main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("customitems.give")) {
            return null;
        }
        if(args.length == 1) {
            return (main.getIR().newItemMap.keySet()).stream()
                    .filter(s -> s.startsWith(args[0].toUpperCase()))
                    .map(s -> s.toUpperCase())
                    .collect(Collectors.toList());
        }
        return null;
    }
}
