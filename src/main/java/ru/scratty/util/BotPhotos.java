package ru.scratty.util;

import java.io.File;
import java.util.Random;

public class BotPhotos {

    private static final String PATH_TEMPLATE = "bots/%s_photos/";

    private static final String PHOTO_PATH_TEMPLATE = "%s/%s";

    private File path;

    public BotPhotos(String botName) {
        path = new File(String.format(PATH_TEMPLATE, botName));
        createDir();
    }

    /**
     * Проверка существования папки и ее создание
     */
    private void createDir() {
        if(!path.exists()) {
            path.mkdirs();
        }
    }

    /**
     * Получение рандомной аватарки
     */
    public File getRandomAvatar() {
        String[] avatars = path.list((dir, name) -> name.contains("avatar_"));

        if (avatars != null && avatars.length > 0) {
            return new File(String.format(PHOTO_PATH_TEMPLATE, path.getPath(), avatars[new Random().nextInt(avatars.length)]));
        }
        return null;
    }

}
