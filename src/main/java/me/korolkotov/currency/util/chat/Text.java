package me.korolkotov.currency.util.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Text {
    public static Text empty() {
        return new Text();
    }

    private final List<String> texts = new ArrayList<>();

    private Text() {}

    public Text(String text) {
        texts.add(text);
    }

    public Text(List<String> list, String inStart) {
        inStart = (inStart == null) ? "" : inStart;
        for (String text : list) texts.add(inStart + text);
    }

    public String asString() { return texts.get(0); }
    public List<String> asList() { return List.copyOf(texts); }

    public Component asComponent() { return ChatUtil.format(asString()); }
    public List<Component> asComponentList() { return ChatUtil.format(asList()); }

    public void format(Map<String, String> args) {
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            for (String key : args.keySet())
                text = text.replace(key, args.get(key));
            texts.set(i, text);
        }
    }

    public boolean isEmpty() { return texts.isEmpty(); }
    public boolean isSingle() { return texts.size() == 1; }
    public boolean isMulti() { return texts.size() > 1; }
}
