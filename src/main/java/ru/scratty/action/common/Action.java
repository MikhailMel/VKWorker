package ru.scratty.action.common;

import com.vk.api.sdk.client.actors.UserActor;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.util.Config;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс-родитель для дейсвий ботов
 * @author scratty
 */

public abstract class Action {

    protected static final long TIME_HALF_HOUR = 1800000;

    protected static final long TIME_HOUR = 36000000;

    protected static final long TIME_HALF_DAY = 432000000;

    protected static final long TIME_DAY = 864000000;

    protected UserActor userActor;

    protected Config config;

    protected OnActionListener listener;

    protected Action(Config config, UserActor userActor, OnActionListener listener) {
        this.config = config;
        this.userActor = userActor;
        this.listener = listener;
    }

    protected long getNextDate(long lowerRange, long upperRange) {
        return new Date().getTime() + ThreadLocalRandom.current().nextLong(lowerRange, upperRange);
    }

    public void check() {
        if(config.getLong(Config.LongKey.ACTION_REPOST) <= new Date().getTime()) {
            doAction();
        }
    }

    protected abstract void doAction();

    protected abstract void setDelay();

    protected abstract void sendMsg(String msg);

}
