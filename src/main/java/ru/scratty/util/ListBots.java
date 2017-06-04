package ru.scratty.util;

import ru.scratty.bot.Bot;

import java.io.File;
import java.util.ArrayList;

/**
 * Класс для получения списка всех ботов
 * @author scratty
 */

public class ListBots {

    private static final String PATH = "bots/";

    private final File file;

    public ListBots() {
        file = new File(PATH);
    }

    public ArrayList<Bot> getBots() {
        ArrayList<Bot> bots = new ArrayList<>();
        if(getNames() != null) {
            for(String name : getNames()) {
                bots.add(new Bot(name.replace("_config.bot", "")));
            }
        }
        return bots;
    }

    private String[] getNames() {
        return file.list((dir, name) -> name.contains("_config.bot"));
    }
}
