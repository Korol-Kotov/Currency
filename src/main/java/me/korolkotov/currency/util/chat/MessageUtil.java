package me.korolkotov.currency.util.chat;

import lombok.AllArgsConstructor;
import me.korolkotov.currency.Main;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MessageUtil {

    private Main plugin;

    private String getTag() {
        if (!plugin.getMessages().contains("tag")) return "";
        return plugin.getMessages().getString("tag").replace("%plugin%", plugin.getName());
    }

    public Text getText(String path, Map<String, String> args, boolean needTag) {
        if (path == null) return Text.empty();

        String tag = (needTag) ? getTag() : "";

        if (plugin.getMessages().isList(path)) {
            Text text = new Text(plugin.getMessages().getStringList(path), tag);
            text.format(args);
            return text;
        } else if (plugin.getMessages().isString(path)) {
            Text text = new Text(tag + plugin.getMessages().getString(path));
            text.format(args);
            return text;
        } else return Text.empty();
    }

    public Text getText(String path) {
        return getText(path, new HashMap<>(), true);
    }

    public Text getText(String path, Map<String, String> args) {
        return getText(path, args, true);
    }

    public Text getText(String path, boolean needTag) {
        return getText(path, new HashMap<>(), needTag);
    }
}
