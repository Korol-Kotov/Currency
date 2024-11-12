package me.korolkotov.currency.util.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ChatUtil {
    public static Component format(String text) {
        if (text == null) return Component.empty();
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static List<Component> format(List<String> list) {
        return list.stream().map(ChatUtil::format).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender sender, Component text) {
        sender.sendMessage(text);
    }

    public static void sendMessage(CommandSender sender, List<Component> list) {
        for (Component text : list) sendMessage(sender, text);
    }

    public static void sendTitle(Player player, Component title, Component subtitle) {
        title = (title == null) ? Component.empty() : title;
        subtitle = (subtitle == null) ? Component.empty() : subtitle;
        player.showTitle(Title.title(title, subtitle));
    }
}
