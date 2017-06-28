package ru.scratty.action.util;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.responses.AddResponse;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import ru.scratty.action.common.VkApi;
import ru.scratty.captcha.Captcha;

import java.util.Random;

public class VkFriends extends VkApi {

    public VkFriends(UserActor userActor) {
        super(userActor);
    }


    /**
     * Получить список друзей
     */
    public GetResponse getFriends() throws ClientException, ApiException {
        sleep();
        return vk.friends()
                .get(userActor)
                .execute();
    }

    /**
     * Получить кол-во друзей
     */
    public int getCountFriends() throws ClientException, ApiException {
        return getFriends().getCount();
    }

    /**
     * Получить рандомного друга
     */
    public int getIdRandomFriend() throws ClientException, ApiException {
        GetResponse response = getFriends();

        if (response.getCount() > 0 && response.getItems().size() > 0) {
            response.getItems().get(new Random().nextInt(response.getItems().size()));
        }
        return INT_ERR;
    }

    /**
     * Отправить заявку в друзья
     */
    public boolean addFriend(int id) throws ClientException, ApiException {
        sleep();
        AddResponse response = vk.friends()
                .add(userActor, id)
                .execute();
        return response != null && (response.getValue() == 1 || response.getValue() == 2);
    }

    /**
     * Отправить заявку в друзья (с капчей)
     */
    public boolean addFriendWithCaptcha(int id, Captcha captcha) {
        sleep();
        try {
            AddResponse response = vk.friends()
                    .add(userActor, id)
                    .captchaSid(captcha.getSid())
                    .captchaKey(captcha.getText())
                    .execute();
            if (response != null) {
                return response.getValue() == 1 || response.getValue() == 2;
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
