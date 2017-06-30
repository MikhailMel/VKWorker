package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkGroups;
import ru.scratty.util.Config;

import static ru.scratty.action.common.VkApi.INT_ERR;

public class LeaveFromGroup extends Action {

    private static final int MIN_COUNT_GROUPS = 4;

    public LeaveFromGroup(Config config, UserActor userActor, OnActionListener listener) {
        super(Config.TypeAction.LEAVE, config, userActor, listener);
    }

    @Override
    protected void doAction() {
        VkGroups vkGroups = new VkGroups(userActor);
        int groupId = INT_ERR;
        try {
            groupId = vkGroups.getRandomGroup(userActor.getId());
            if (groupId != INT_ERR) {
                if (vkGroups.leaveFromGroup(groupId)) {
                    sendMsg("Выход из группы " + vkGroups.getGroupName(groupId) + " (" + groupId + ")");
                } else {
                    sendMsg("Ошибка выхода из группы " + groupId);
                }
            } else {
                sendMsg("Кол-во групп меньше " + MIN_COUNT_GROUPS);
            }
        } catch (ApiException | ClientException e) {
            sendMsg("Ошибка выхода из группы " + groupId);
            sendMsg(e.getMessage());
            e.printStackTrace();
        }
    }
}
