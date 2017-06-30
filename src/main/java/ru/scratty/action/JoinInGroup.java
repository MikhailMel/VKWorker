package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkGroups;
import ru.scratty.util.Config;

import static ru.scratty.action.common.VkApi.INT_ERR;

public class JoinInGroup extends Action {

    public JoinInGroup(Config config, UserActor userActor, OnActionListener listener) {
        super(Config.TypeAction.JOIN, config, userActor, listener);
    }

    @Override
    protected void doAction() {
        VkGroups vkGroups = new VkGroups(userActor);
        int groupId = INT_ERR;

        try {
            groupId = vkGroups.getRandomGroupFromRandomUser();
            if (groupId != INT_ERR) {
                if (vkGroups.joinInGroup(groupId)) {
                    sendMsg("Вступление в группу " + vkGroups.getGroupName(groupId) + " (" + groupId + ") успешно");
                } else {
                    sendMsg("Ошибка вступления в группу " + groupId);
                }
            } else {
                sendMsg("Список групп пуст");
            }
        } catch (ClientException | ApiException e) {
            sendMsg("Ошибка вступления в группу " + groupId);
            sendMsg(e.getMessage());
            e.printStackTrace();
        }
    }
}
