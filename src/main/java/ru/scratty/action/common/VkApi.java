package ru.scratty.action.common;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;


/**
 * Класс-родитель для утилит, взаимодействующих с vk API
 * @author scratty
 */
public class VkApi {

    public static final int INT_ERR = -1;

    public static final String STRING_ERR = "err";

    private static final long DEFAULT_SLEEP = 334L;

    protected VkApiClient vk;

    protected UserActor userActor;

    public VkApi(UserActor userActor) {
        this.userActor = userActor;

        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
    }

    protected void sleep() {
        try {
            Thread.sleep(DEFAULT_SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
