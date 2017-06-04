package ru.scratty.vk;

import com.vk.api.sdk.client.actors.UserActor;
import ru.scratty.vk.common.VkApi;

import java.util.ArrayList;

/**
 * Утилита для работы с группами vk
 * @author scratty
 */

public class VkGroup extends VkApi {

    public VkGroup(UserActor userActor) {
        super(userActor);
    }

    public ArrayList<String> getListGroups() {

        return null;
    }
}
