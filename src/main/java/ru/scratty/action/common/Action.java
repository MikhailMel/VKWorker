package ru.scratty.action.common;

import com.vk.api.sdk.client.actors.UserActor;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.dialog.captcha.Captcha;
import ru.scratty.dialog.captcha.CaptchaDialog;
import ru.scratty.dialog.captcha.CaptchaListener;
import ru.scratty.util.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс-родитель для дейсвий ботов
 * @author scratty
 */

public abstract class Action {

    protected static final long TIME_MINUTE = 60000;

    protected static final long TIME_HALF_HOUR = 1800000;

    protected static final long TIME_HOUR = 3600000;

    protected static final long TIME_HALF_DAY = 43200000;

    protected static final long TIME_DAY = 86400000;

    protected UserActor userActor;

    private Config config;

    protected OnActionListener listener;

    private Config.TypeAction typeAction;

    private boolean captchaFlag;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

    protected Action(Config.TypeAction typeAction, Config config, UserActor userActor, OnActionListener listener) {
        this.typeAction = typeAction;
        this.config = config;
        this.userActor = userActor;
        this.listener = listener;
    }

    /**
     * Проверка наступления нужного момента для совершения действия
     */
    public void check() {
        if(config.getLong(typeAction) <= new Date().getTime() && !captchaFlag) {
            doAction();
            setDelay();
//            setDelay(10000);
        }
    }

    /**
     * Само действие
     */
    protected abstract void doAction();

    /**
     * Установка отсрочки
     */
    private void setDelay() {
        long date = new Date().getTime() + ThreadLocalRandom.current().nextLong(0, config.getThreshold(typeAction));
        config.setLong(typeAction, date);
        sendMsg("Следующее действие будет " + sdf.format(new Date(date)));
    }

    protected void showCaptcha(Captcha captcha, CaptchaListener listener) {
        sendMsg("Требуется ввод капчи");
        captchaFlag = true;

        CaptchaDialog dialog = new CaptchaDialog(captcha);
        if (dialog.init()) {
            dialog.setCaptchaListener(c -> {
                sendMsg("Капча " + c.getText());
                listener.sendCaptcha(c);
                captchaFlag = false;
            });
            dialog.show();
        } else {
            captchaFlag = false;
        }
    }

    /**
     * Вывод сообщения в меню
     */
    protected void sendMsg(String msg) {
        listener.sendAction(typeAction.name(), msg);
    }
}
