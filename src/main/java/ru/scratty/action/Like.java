package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.likes.LikesType;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkFriends;
import ru.scratty.action.util.VkGroups;
import ru.scratty.action.util.VkUsers;
import ru.scratty.action.util.VkWall;
import ru.scratty.util.Config;

import java.util.Random;

public class Like extends Action {

    private static final int CHANCE_RANDOM_USER = 10;

    private static final int CHANCE_MY_GROUPS = 50;

    private static final int CHANCE_FRIENDS = 75;

    public Like(Config config, UserActor userActor, OnActionListener listener) {
        super(Config.TypeAction.LIKE, config, userActor, listener);
    }

    @Override
    protected void doAction() {
        try {
            int id = getId();
            if (id != -1) {
                int post = new VkWall(userActor).getRandomPostId(id);
                if (post != -1) {
                    vk.likes()
                            .add(userActor, LikesType.POST, post)
                            .ownerId(id)
                            .execute();

                    sendSuccessLikeMsg(id, post);
                } else {
                    sendMsg("Ошибка получения id записи");
                }
            } else {
                sendMsg("Ошибка получения id группы/пользователя");
            }
        } catch (ClientException | ApiException e) {
            sendMsg(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected long getDelay() {
        return getNextDate(TIME_MINUTE, TIME_HOUR * 8);
    }

    /**
     * Выбор сообщения в зависимости от лайка
     */
    private void sendSuccessLikeMsg(int id, int post) throws ClientException, ApiException {
        if (id < 0) {
            sendMsg(String.format("Лайкнута запись %d в сообществе %s (%d)",
                    post, new VkGroups(userActor).getGroupName(id), -id));
        } else {
            sendMsg(String.format("Лайнута запись %d пользователя %s (%d)",
                    post, new VkUsers(userActor).getName(id), id));
        }
    }

    /**
     * Распределение вероятностей лайканья своих/чужих сообществ, друзей и рандомных юзеров
     */
    private int getId() throws ClientException, ApiException {
        VkGroups vkGroups = new VkGroups(userActor);
        VkFriends vkFriends = new VkFriends(userActor);
        int i = new Random().nextInt(100);
        if (i < CHANCE_RANDOM_USER) {
            return vkGroups.getRandomUserFromGroup(String.valueOf(vkGroups.getRandomGroupFromUserFromConfig()));
        } else if (i < CHANCE_MY_GROUPS) {
            return -vkGroups.getRandomGroup(userActor.getId());
        } else if (i < CHANCE_FRIENDS && vkFriends.getCountFriends() > 0) {
            return vkFriends.getIdRandomFriend();
        } else {
            return -vkGroups.getRandomGroupFromRandomUser();
        }
    }
}
