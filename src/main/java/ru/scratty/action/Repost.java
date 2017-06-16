package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.RepostResponse;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkGroup;
import ru.scratty.action.util.VkWall;
import ru.scratty.util.Config;

/**
 * Класс для совершения репостов
 * @author scratty
 */

public class Repost extends Action {

    public Repost(Config config, UserActor userActor, OnActionListener listener) {
        super(Config.TypeAction.REPOST, config, userActor, listener);
    }

    @Override
    protected void doAction() {
        VkGroup vkGroup = new VkGroup(userActor);
        try {
            int groupId = vkGroup.getRandomGroup(userActor.getId());
            System.out.println("GI " + groupId);
            String topicId = new VkWall(userActor).getRandomTopic(groupId);
            System.out.println("TI " + topicId);
            if (!topicId.equals(STRING_ERR)) {
                RepostResponse response = vk.wall()
                        .repost(userActor, topicId)
                        .execute();

                if (response.getSuccess().getValue() == 1) {
                    sendMsg("Успешный репост" + topicId + " из группы " + vkGroup.getGroupName(groupId));
                } else {
                    sendMsg("Репост " + topicId + " не выполнен");
                }
            } else {
                sendMsg("Ошибка получения id записи");
            }
        } catch (ClientException | ApiException e) {
            sendMsg("Ошибка репоста");
            sendMsg(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected long getDelay() {
        return getNextDate(TIME_MINUTE * 10, TIME_DAY);
    }
}
