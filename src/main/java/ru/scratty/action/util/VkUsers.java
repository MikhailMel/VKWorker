package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import ru.scratty.action.common.VkApi;

import java.util.List;

public class VkUsers extends VkApi {

    public VkUsers(UserActor userActor) {
        super(userActor);
    }

    /**
     * Получить имя пользователя
     */
    public String getName(int id) throws ClientException, ApiException {
        List<UserXtrCounters> list = vk.users()
                .get(userActor)
                .userIds(String.valueOf(id))
                .execute();

        if (list != null && list.size() == 1) {
            return String.format("%s %s", list.get(0).getFirstName(), list.get(0).getLastName());
        }
        return STRING_ERR;
    }

}
