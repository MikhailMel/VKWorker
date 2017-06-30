package ru.scratty.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileWorker {

    private String path;

    private Map<String, String> map = new HashMap<>();

    ConfigFileWorker(String path) {
        this.path = path;
        if (checkExists(path)) {
            init();
        }
    }

    /**
     * Проверка существования файла
     */
    private boolean checkExists(String patch) {
        File file = new File(patch);
        if(!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                return file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * Заполнение данных
     */
    private void init() {
        try {
            Files.lines(Paths.get(path), StandardCharsets.UTF_8)
                    .forEach(s -> map.put(s.split(":::")[0], s.split(":::")[1]));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение данных по ключу
     */
    String getData(String field) {
        if (!map.isEmpty() && map.containsKey(field) && !map.get(field).isEmpty()) {
            return map.get(field);
        }
        return "";
    }

    /**
     * Сохранение пары ключ-значение в файле
     */
    void setData(String name, String data) {
        map.put(name, data);

        try(FileWriter writer = new FileWriter(path, false)) {
            for(String k : map.keySet()) {
                writer.write(k + ":::" + map.get(k) + "\n");
            }
            writer.flush();
        } catch(IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    /**
     * Удаление конфиг файла
     */
    public void removeConfig() {
        File file = new File(path);
        file.delete();
    }

    /**
     * Получение резмера
     */
    int getCount() {
        return map.size();
    }
}
