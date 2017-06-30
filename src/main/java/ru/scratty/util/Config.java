package ru.scratty.util;

import ru.scratty.bot.Bot;

/**
 * Класс для работы с конфигом каждого бота
 * @author scratty
 */

public class Config extends ConfigFileWorker {

    private static final String FILE_TEMPLATE = "bots/%s_config.bot";

    public Config(Bot bot) {
        super(String.format(FILE_TEMPLATE, bot.getNameBot()));
    }

    public String getString(StringKey key) {
        return getData(key.name());
    }

    public int getInt(IntKey key) {
        String data = getData(key.name());
        if(!data.equals("")) {
            return Integer.parseInt(data);
        }
        return 0;
    }

    public long getLong(TypeAction key) {
        String data = getData(key.name());
        if(!data.equals("")) {
            return Long.parseLong(data);
        }
        return 0;
    }

    public void setString(StringKey key, String data) {
        setData(key.name(), data);
    }

    public void setInt(IntKey key, int data) {
        setData(key.name(), String.valueOf(data));
    }

    public void setLong(TypeAction key, long data) {
        setData(key.name(), String.valueOf(data));
    }

    public long getThreshold(TypeAction key) {
        String data = getData(key.name() + "_THRESHOLD");
        if(!data.equals("")) {
            return Long.parseLong(data);
        }
        return 3600000;
    }

    public void setThreshold(TypeAction key, long data) {
        setData(key.name() + "_THRESHOLD", String.valueOf(data));
    }

    public enum StringKey {
        NAME, TOKEN
    }

    public enum IntKey {
        USER_ID
    }

    public enum TypeAction {
        REPOST, JOIN, LEAVE, LIKE, SET_AVATAR, ADD_FRIEND
    }
}
