package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import ru.scratty.action.common.VkApi;

import java.util.Random;

public class VkWall extends VkApi {

    private static final int DEFAULT_COUNT_POSTS = 20;

    public VkWall(UserActor userActor) {
        super(userActor);
    }

    /**
     * Получить DEFAULT_COUNT_POSTS постов
     */
    public GetResponse getPost(int id) throws ClientException, ApiException {
        return vk.wall()
                .get(userActor)
                .ownerId(id)
                .count(DEFAULT_COUNT_POSTS)
                .execute();
    }

    /**
     * Получить рандомный пост в виде wall-<id_group>_<id_post>
     */
    public String getRandomPost(int id) throws ClientException, ApiException {
        int topicId = getRandomPostId(id);
        if (topicId != -1) {
            return String.format("wall%d_%d", id, topicId);
        }
        return STRING_ERR;
    }

    /**
     * Получить id рандомного поста
     */
    public int getRandomPostId(int id) throws ClientException, ApiException {
        sleep();
        if (id != INT_ERR) {
            GetResponse response = getPost(id);

            if (response.getCount() > 0 && response.getItems().size() == DEFAULT_COUNT_POSTS) {
                return response.getItems().get(new Random().nextInt(DEFAULT_COUNT_POSTS)).getId();
            }
        }
        return INT_ERR;
    }
}
