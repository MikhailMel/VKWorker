package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import ru.scratty.action.common.VkApi;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Утилита для работы с группами vk
 * @author scratty
 */

public class VkGroup extends VkApi {

    private static final String PATH_GROUPS = "groups.list";

    private static volatile VkGroup instance;

    private final ArrayList<Integer> list = new ArrayList<>();

    private VkGroup(UserActor userActor) {
        super(userActor);
        if (checkExists()) {
            init();
        }
    }

    public static synchronized VkGroup getInstance(UserActor userActor) {
        VkGroup vkGroup = instance;
        if (vkGroup == null) {
            synchronized (VkGroup.class) {
                vkGroup = instance;
                if (vkGroup == null) {
                    instance = vkGroup = new VkGroup(userActor);
                }
            }
        }
        return vkGroup;
    }

    private void init() {
        try {
            Files.lines(Paths.get(PATH_GROUPS), StandardCharsets.UTF_8).forEach(s -> list.add(Integer.valueOf(s)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkExists() {
        File file = new File(PATH_GROUPS);
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

    public synchronized Integer getRandomGroupId() {
        return list.get(new Random().nextInt(list.size()));
    }

    public synchronized ArrayList<String> getListGroups() {
        return null;
    }
}