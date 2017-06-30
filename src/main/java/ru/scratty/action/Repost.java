package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiCaptchaException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkGroups;
import ru.scratty.action.util.VkWall;
import ru.scratty.captcha.Captcha;
import ru.scratty.util.Config;

import static ru.scratty.action.common.VkApi.INT_ERR;
import static ru.scratty.action.common.VkApi.STRING_ERR;

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
        VkGroups vkGroups = new VkGroups(userActor);
        VkWall vkWall = new VkWall(userActor);
        String topicId = STRING_ERR;
        try {
            int id = vkGroups.getRandomGroup(userActor.getId());
            if (id != INT_ERR) {
                topicId = vkWall.getRandomPost(-id);
                if (!topicId.equals(STRING_ERR)) {
                    if (vkWall.repost(topicId)) {
                        sendMsg("Успешный репост " + topicId + " из группы " + vkGroups.getGroupName(id));
                    } else {
                        sendMsg("Репост " + topicId + " не выполнен");
                    }

                } else {
                    sendMsg("Ошибка получения id записи");
                }
            }
        } catch (ApiCaptchaException e) {
            String finalTopicId = topicId;
            showCaptcha(new Captcha(e), captcha -> {
                if (vkWall.repostWithCaptcha(finalTopicId, captcha)) {
                    sendMsg("Запись репостнута");
                } else {
                    sendMsg("Ошибка повторного репоста");
                }
            });
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
