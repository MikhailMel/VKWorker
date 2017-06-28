package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.AddResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import ru.scratty.action.common.VkApi;

public class VkLikes extends VkApi {

    public VkLikes(UserActor userActor) {
        super(userActor);
    }

    /**
     * Ставим лайк
     */
    public boolean addLike(LikesType likesType, int ownerId, int itemId) throws ClientException, ApiException {
        sleep();
        AddResponse response = vk.likes()
                .add(userActor, likesType, itemId)
                .ownerId(ownerId)
                .execute();

        return response != null && response.getLikes() != 0;
    }
}
