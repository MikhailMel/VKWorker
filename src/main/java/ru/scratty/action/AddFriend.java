package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiCaptchaException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkFriends;
import ru.scratty.action.util.VkGroups;
import ru.scratty.action.util.VkUsers;
import ru.scratty.dialog.captcha.Captcha;
import ru.scratty.util.Config;

import static ru.scratty.action.common.VkApi.INT_ERR;

public class AddFriend extends Action {

    public AddFriend(Config config, UserActor userActor, OnActionListener listener) {
        super(Config.TypeAction.ADD_FRIEND, config, userActor, listener);
    }

    @Override
    protected void doAction() {
        VkGroups vkGroups = new VkGroups(userActor);
        VkFriends vkFriends = new VkFriends(userActor);
        int id = INT_ERR;
        try {
            id = vkGroups.getRandomUserFromGroup(String.valueOf(vkGroups.getRandomGroupFromRandomUser()));

            if (vkFriends.addFriend(id)) {
                sendMsg("Заявка в друзья отправлена, " + new VkUsers(userActor).getName(id) + " (" + id + ")");
            } else {
                sendMsg("Ошибка отправления заявки в друзья, " + new VkUsers(userActor).getName(id) + " (" + id + ")");
            }
        } catch (ApiCaptchaException e) {
            int finalId = id;

            showCaptcha(new Captcha(e), captcha -> {
                if (vkFriends.addFriendWithCaptcha(finalId, captcha)) {
                    sendMsg("Повторная заявка в друзья отправлена");
                } else {
                    sendMsg("Ошибка отправки повторной заявки в друзья");
                }
            });
        } catch (ClientException | ApiException e) {
            sendMsg("Ошибка отправки заявки в друзья (" + id + ")");
            sendMsg(e.getMessage());
            e.printStackTrace();
        }
    }
}
