package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import ru.scratty.action.common.VkApi;

import java.util.Random;

public class VkFriends extends VkApi {

    public VkFriends(UserActor userActor) {
        super(userActor);
    }

    public GetResponse getFriends() throws ClientException, ApiException {
        sleep();
        return vk.friends()
                .get(userActor)
                .execute();
    }

    public int getCountFriends() throws ClientException, ApiException {
        return getFriends().getCount();
    }

    public int getIdRandomFriend() throws ClientException, ApiException {
        GetResponse response = getFriends();

        if (response.getCount() > 0 && response.getItems().size() > 0) {
            response.getItems().get(new Random().nextInt(response.getItems().size()));
        }
        return INT_ERR;
    }
}
