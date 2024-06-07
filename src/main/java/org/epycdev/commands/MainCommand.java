package org.epycdev.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.epycdev.EpycOffhand;
import org.epycdev.utils.MessageUtils;

public class MainCommand implements CommandExecutor {
    private EpycOffhand plugin;
    public MainCommand(EpycOffhand plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("get")){
                getSubCommand(sender, args);
            }else if(args[0].equalsIgnoreCase("reload")){
                subCommandReload(sender);
            }else{
                help(sender);
            }
        }else {
            help(sender);
        }
        return true;
    }

    public void help (CommandSender sender) {
        sender.sendMessage(MessageUtils.getColoredMessage(" "));
        sender.sendMessage(MessageUtils.getColoredMessage(" &#00A2FF&lEPYC OFFHAND HELP"));
        sender.sendMessage(MessageUtils.getColoredMessage(" &fAll general commands."));
        sender.sendMessage(MessageUtils.getColoredMessage(" "));
        sender.sendMessage(MessageUtils.getColoredMessage(" &8• &#68C8FF/epycoffhand get &3<author/version> &8- &7Get plugin author or version."));
        sender.sendMessage(MessageUtils.getColoredMessage(" &8• &#68C8FF/epycoffhand reload &8- &7Reload the plugin."));
        sender.sendMessage(MessageUtils.getColoredMessage(" "));
    }

    public void getSubCommand(CommandSender sender, String[] args){

        if(args.length == 1){
            sender.sendMessage(MessageUtils.getColoredMessage(EpycOffhand.prefix+"&cUsage: &7/epycoffhand get <author/version>"));
            return;
        }

        if(args[1].equalsIgnoreCase("author")){
            sender.sendMessage(MessageUtils.getColoredMessage(
                    EpycOffhand.prefix+"&fPlugin author:&#68C8FF epycdev &7(Discord)"));
        }else if(args[1].equalsIgnoreCase("version")){
            sender.sendMessage(MessageUtils.getColoredMessage(
                    EpycOffhand.prefix+"&fPlugin version:&#68C8FF v"+plugin.getDescription().getVersion()));
        }else{
            sender.sendMessage(MessageUtils.getColoredMessage(EpycOffhand.prefix+"&cUsage: &7/epycoffhand get <author/version>"));
        }
    }

    public void subCommandReload(CommandSender sender) {
        if (!sender.hasPermission("epycoffhand.commands.reload")) {
            sender.sendMessage(MessageUtils.getColoredMessage(EpycOffhand.prefix + "&#FF0000You don't have permissions for that."));
            return;
        }
        plugin.getMainConfigManager().reloadConfig();
        sender.sendMessage(MessageUtils.getColoredMessage(EpycOffhand.prefix + "&#00FF00Successfully reloaded. &fUsing method: &#68C8FF" + plugin.getMainConfigManager().getMethod()));
    }
}
