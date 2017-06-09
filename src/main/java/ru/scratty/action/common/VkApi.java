package ru.scratty.action.common;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;


/**
 * Класс-родитель для утилит, взаимодействующих с vk API
 * @author scratty
 */
public abstract class VkApi {

    public static final int INT_ERR = -1;

    public static final String STRING_ERR = "err";

    protected VkApiClient vk;

    protected UserActor userActor;

    public VkApi(UserActor userActor) {
        this.userActor = userActor;

        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
    }
}
