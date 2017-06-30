package ru.scratty.dialog.captcha;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class CaptchaDialog {

    private Task<Void> task;

    private Captcha captcha;

    private CaptchaListener captchaListener;

    public CaptchaDialog(Captcha captcha) {
        this.captcha = captcha;
    }

    public boolean init() {
        try {
            Image image = loadImage(captcha.getUrl());

            task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(() -> new Dialog(image));
                    return null;
                }
            };
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setCaptchaListener(CaptchaListener captchaListener) {
        this.captchaListener = captchaListener;
    }

    public void show() {
        new Thread(task).start();
    }

    private void sendResult(String result) {
        captcha.setText(result);
        if (captchaListener != null) {
            captchaListener.sendCaptcha(captcha);
        }
    }

    private Image loadImage(String captchaUrl) throws IOException {
        BufferedImage image;
        URL url = new URL(captchaUrl);
        image = ImageIO.read(url);
        if (image != null){
            ImageIO.write(image, "jpg",new File("captcha.jpg"));
            return SwingFXUtils.toFXImage(image, null);
        }
        return null;
    }

    private class Dialog extends javafx.scene.control.Dialog<String> {

        Dialog(Image image) {
            super();
            setTitle("Введите капчу");

            getDialogPane().getButtonTypes().addAll(ButtonType.OK);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 10, 10, 10));

            ImageView imageView = new ImageView();
            imageView.setImage(image);

            TextField text = new TextField();

            grid.add(imageView, 0, 0);
            grid.add(text, 0, 1);

            getDialogPane().setContent(grid);
            setResultConverter(param -> text.getText());

            Optional<String> result = showAndWait();
            result.ifPresent(CaptchaDialog.this::sendResult);
        }
    }
}
