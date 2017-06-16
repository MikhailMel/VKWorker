package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import ru.scratty.action.common.VkApi;

import java.util.Random;

public class VkWall extends VkApi {

    private static final int RANDOM_RANGE_FOR_TOPICS = 100;

    public VkWall(UserActor userActor) {
        super(userActor);
    }

    public String getRandomTopic(int groupId) throws ClientException, ApiException {
        sleep();
        int count = new VkGroup(userActor).getCountTopics(groupId);

        if (groupId != INT_ERR && count != INT_ERR) {
            int i;
            while ((i = new Random().nextInt(RANDOM_RANGE_FOR_TOPICS)) < count) {
            }

            GetResponse response = vk.wall()
                    .get(userActor)
                    .ownerId(-groupId)
                    .count(1)
                    .offset(i)
                    .execute();

            if (response.getCount() > 0) {
                return String.format("wall%d_%d", response.getItems().get(0).getOwnerId(), response.getItems().get(0).getId());
            }
        }
        return STRING_ERR;
    }

}
