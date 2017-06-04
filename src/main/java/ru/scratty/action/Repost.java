package ru.scratty.action;

import com.vk.api.sdk.client.actors.UserActor;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.util.Config;

/**
 * Класс для совершения репостов
 * @author scratty
 */

public class Repost extends Action {

    public Repost(Config config, UserActor userActor, OnActionListener listener) {
        super(config, userActor, listener);
    }

    @Override
    protected void doAction() {

    }

    @Override
    protected void setDelay() {

    }

    @Override
    protected void sendMsg(String msg) {

    }
}
