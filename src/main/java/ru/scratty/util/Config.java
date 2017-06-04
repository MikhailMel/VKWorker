package ru.scratty.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы с конфигом каждого бота
 * @author scratty
 */

public class Config {

    private static final String FILE_TEMPLATE = "bots/%s_config.bot";

    private final String patch;

    private final Map<String, String> map = new HashMap<>();

    public Config(String botName) {
        patch = String.format(FILE_TEMPLATE, botName);
        if(checkExists()) {
            init();
        }
    }

    private boolean checkExists() {
        File file = new File(patch);
        if(!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                return file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            return true;
        }
        return false;
    }

    private void init() {
        try {
            Files.lines(Paths.get(patch), StandardCharsets.UTF_8).forEach(s -> map.put(s.split(":::")[0], s.split(":::")[1]));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(StringKey key) {
        return map.get(key.name());
    }

    public int getInt(IntKey key) {
        if(map.containsKey(key.name()) && !map.get(key.name()).isEmpty()) {
            return Integer.parseInt(map.get(key.name()));
        }
        return 0;
    }

    public long getLong(LongKey key) {
        if(map.containsKey(key.name()) && !map.get(key.name()).isEmpty()) {
            return Long.parseLong(map.get(key.name()));
        }
        return 0;
    }

    public void setString(StringKey key, String data) {
        setData(key.name(), data);
    }

    public void setInt(IntKey key, int data) {
        setData(key.name(), String.valueOf(data));
    }

    public void setLong(LongKey key, long data) {
        setData(key.name(), String.valueOf(data));
    }

    private void setData(String name, String data) {
        map.put(name, data);

        try(FileWriter writer = new FileWriter(patch, false)) {
            for(String k : map.keySet()) {
                writer.write(k + ":::" + map.get(k) + "\n");
            }
            writer.flush();
        } catch(IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    public void removeConfig() {
        File file = new File(patch);
        file.delete();
    }

    public enum StringKey {
        NAME, TOKEN
    }

    public enum IntKey {
        USER_ID
    }

    public enum LongKey {
        ACTION_REPOST, ACTION_COMMENT
    }
}
