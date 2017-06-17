package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkGroups;
import ru.scratty.util.Config;

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
                vk.groups().join(userActor).groupId(groupId).execute();
                sendMsg("Вступление в группу " + vkGroups.getGroupName(groupId) + " (" + groupId + ") успешно");
            } else {
                sendMsg("Список групп пуст");
            }
        } catch (ClientException | ApiException e) {
            sendMsg("Ошибка вступления в группу " + groupId);
            sendMsg(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected long getDelay() {
        return getNextDate(TIME_HALF_DAY, TIME_DAY * 4);
    }
}
