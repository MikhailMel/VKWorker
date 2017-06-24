package ru.scratty.action.util;

import com.google.gson.Gson;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.GetOwnerPhotoUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.SaveOwnerPhotoResponse;
import ru.scratty.action.common.VkApi;
import ru.scratty.action.model.OwnerPhoto;
import ru.scratty.util.MultipartUtility;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VkPhotos extends VkApi {

    public VkPhotos(UserActor userActor) {
        super(userActor);
    }

    /**
     * Получение URL для загрузки фотографии на сервер
     */
    private String getOwnerPhotoUploadURL() throws ClientException, ApiException {
        sleep();
        GetOwnerPhotoUploadServerResponse response = vk.photos()
                .getOwnerPhotoUploadServer(userActor)
                .ownerId(userActor.getId())
                .execute();

        if (response != null) {
            return response.getUploadUrl();
        }
        return STRING_ERR;
    }

    /**
     * Загрузка фотографии на сервер
     */
    private OwnerPhoto uploadOwnerPhoto(File photo) throws ClientException, ApiException, IOException {
        MultipartUtility multipartUtility = new MultipartUtility(getOwnerPhotoUploadURL(), "UTF-8");
        multipartUtility.addFilePart("photo", photo);

        List<String> responce = multipartUtility.finish();
        if (responce.size() != 0) {
            return new Gson().fromJson(responce.get(0), OwnerPhoto.class);
        }
        return null;
    }

    /**
     * Установка аватара
     */
    public boolean saveOwnerPhoto(File photo) throws ClientException, ApiException, IOException {
        sleep();
        OwnerPhoto ownerPhoto = uploadOwnerPhoto(photo);

        if (ownerPhoto != null) {
            SaveOwnerPhotoResponse response = vk.photos()
                    .saveOwnerPhoto(userActor)
                    .hash(ownerPhoto.getHash())
                    .photo(ownerPhoto.getPhoto())
                    .server(String.valueOf(ownerPhoto.getServer()))
                    .execute();

            if (response != null) {
                return true;
            }
        }
        return false;
    }
}
