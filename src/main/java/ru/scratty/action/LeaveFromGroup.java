package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.responses.GetResponse;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkGroup;
import ru.scratty.util.Config;

import java.util.List;
import java.util.Random;

public class LeaveFromGroup extends Action {

    private static final int MIN_COUNT_GROUPS = 4;

    public LeaveFromGroup(Config config, UserActor userActor, OnActionListener listener) {
        super(Config.TypeAction.LEAVE, config, userActor, listener);
    }

    @Override
    protected void doAction() {
        try {
            GetResponse getResponse = vk.groups().get(userActor).execute();
            if (getResponse.getCount() > MIN_COUNT_GROUPS) {
                List<Integer> list = getResponse.getItems();
                int id = list.get(new Random().nextInt(list.size()));

                vk.groups().leave(userActor, id).execute();
                sendMsg("Выход из группы " + VkGroup.getInstance(userActor).getGroupName(id) + " (" + id + ")");
            } else {
                sendMsg("Кол-во групп меньше " + MIN_COUNT_GROUPS);
            }
        } catch (ApiException | ClientException e) {
            sendMsg("Ошибка выхода из группы");
            sendMsg(e.getMessage());
        }
    }

    @Override
    protected long getDelay() {
        return getNextDate(TIME_HALF_DAY, TIME_DAY * 5);
    }
}
