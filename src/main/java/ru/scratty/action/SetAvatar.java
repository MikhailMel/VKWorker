package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.util.VkPhotos;
import ru.scratty.util.BotPhotos;
import ru.scratty.util.Config;

import java.io.File;
import java.io.IOException;

public class SetAvatar extends Action {

    private BotPhotos botPhotos;

    public SetAvatar(Config config, UserActor userActor, OnActionListener listener, String botName) {
        super(Config.TypeAction.SET_AVATAR, config, userActor, listener);

        botPhotos = new BotPhotos(botName);
    }

    @Override
    protected void doAction() {
        VkPhotos vkPhotos = new VkPhotos(userActor);
        File photo = botPhotos.getRandomAvatar();
        if (photo != null) {
            try {
                if (vkPhotos.saveOwnerPhoto(photo)) {
                    sendMsg("Аватар успешно установлен (" + photo.getName() + ")");
                } else {
                    sendMsg("Ошибка установки аватара (" + photo.getName() + ")");
                }
            } catch (ClientException | ApiException | IOException e) {
                sendMsg("Ошибка установки аватара (" + photo.getName() + ")");
                sendMsg(e.getMessage());
                e.printStackTrace();
            }
        } else {
            sendMsg("Аватаров не найдено");
        }
    }
}
