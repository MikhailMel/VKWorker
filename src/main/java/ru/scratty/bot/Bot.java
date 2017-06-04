package ru.scratty.bot;

import com.sun.istack.internal.NotNull;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.account.UserSettings;
import ru.scratty.action.Repost;
import ru.scratty.action.common.Action;
import ru.scratty.action.listener.OnActionListener;
import ru.scratty.action.listener.OnMsgListener;
import ru.scratty.action.listener.OnStateBotListener;
import ru.scratty.util.Config;

import java.util.ArrayList;

/**
 * Ядро самого бота
 * @author scratty
 */

public class Bot extends Thread implements OnActionListener {

    private boolean flag;

    private String name;

    private int userId;

    private String token;

    private OnMsgListener onMsgListener;

    private OnStateBotListener onStateBotListener;

    private Config config;

    private VkApiClient vk;

    private UserActor userActor;

    private final ArrayList<Action> actions = new ArrayList<>();

    public Bot(@NotNull String name, @NotNull int userId, @NotNull String token) {
        this.name = name;
        this.userId = userId;
        this.token = token;

        config = new Config(name);
        config.setString(Config.StringKey.NAME, name);
        config.setInt(Config.IntKey.USER_ID, userId);
        config.setString(Config.StringKey.TOKEN, token);

        initVkApi();
    }

    public Bot(@NotNull String name) {
        this.name = name;

        config = new Config(name);
        userId = config.getInt(Config.IntKey.USER_ID);
        token = config.getString(Config.StringKey.TOKEN);

        initVkApi();
    }

    private void initVkApi() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
    }

    @Override
    public void run() {
        auth();

        while(flag) {
            for (Action action : actions) {
                action.check();
            }
            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void auth() {
        userActor = new UserActor(userId, token);
        try {
            UserSettings userSettings = vk.account().getProfileInfo(userActor).execute();
            if (userSettings != null) {
                initActions();
                sendMsg("успешная авторизация под именем " + userSettings.getFirstName() + " " + userSettings.getLastName());

                flag = true;
                changeState();
            }
        } catch(ApiException | ClientException e) {
            e.printStackTrace();
            sendMsg(e.getMessage());
        }
    }

    /**
     * Инициализация всех возможных действий аккаунтом
     */
    private void initActions() {
        actions.add(new Repost(config, userActor, this));
    }

    private void sendMsg(String msg) {
        if(onMsgListener != null) {
            onMsgListener.sendMsg(name, msg);
        }
    }

    private void changeState() {
        if(onStateBotListener != null) {
            onStateBotListener.onChangeState();
        }
    }

    public boolean isWorked() {
        return flag;
    }

    void stopBot() {
        changeState();
        flag = false;
    }

    void delete() {
        stopBot();
        config.removeConfig();
    }

    public String getNameBot() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    void setOnMsgListener(OnMsgListener onMsgListener) {
        this.onMsgListener = onMsgListener;
    }

    void setOnStateBotListener(OnStateBotListener onStateBotListener) {
        this.onStateBotListener = onStateBotListener;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "flag=" + flag +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", onMsgListener=" + onMsgListener +
                ", onStateBotListener=" + onStateBotListener +
                ", config=" + config +
                ", vk=" + vk +
                ", userActor=" + userActor +
                ", actions=" + actions +
                '}';
    }
}
