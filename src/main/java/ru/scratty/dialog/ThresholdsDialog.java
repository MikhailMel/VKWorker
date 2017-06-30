package ru.scratty.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.scratty.bot.Bot;
import ru.scratty.util.Config;

public class ThresholdsDialog extends Dialog<Void> {

    private Config config;

    private TextField[][] thresholds;

    public ThresholdsDialog(Bot bot) {
        super();
        config = new Config(bot);

        makeDialog();
    }

    private void makeDialog() {
        setTitle("Введите капчу");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        thresholds = new TextField[Config.TypeAction.values().length][3];

        for (int i = 0; i < thresholds.length; i++) {
            Label label = new Label(getLabel(Config.TypeAction.values()[i]));

            thresholds[i][0] = createTextField(getDays(config.getThreshold(Config.TypeAction.values()[i])));
            thresholds[i][1] = createTextField(getHours(config.getThreshold(Config.TypeAction.values()[i])));
            thresholds[i][2] = createTextField(getMinutes(config.getThreshold(Config.TypeAction.values()[i])));

            grid.add(label, 0, i);
            grid.add(new Label("дни"), 1, i);
            grid.add(thresholds[i][0], 2, i);
            grid.add(new Label("часы"), 3, i);
            grid.add(thresholds[i][1], 4, i);
            grid.add(new Label("минуты"), 5, i);
            grid.add(thresholds[i][2], 6, i);
        }

        getDialogPane().setContent(grid);
        setResultConverter(param -> {
            setThresholds();
            return null;
        });

        show();
    }

    private int getDays(long time) {
        return (int) (time / 24 / 60 / 60 / 1000);
    }

    private int getHours(long time) {
        return (int) (time / 60 / 60 / 1000 - getDays(time) * 24);
    }

    private int getMinutes(long time) {
        return (int) (time / 60 / 1000 - getHours(time) * 60 - getDays(time) * 24 * 60);
    }

    private TextField createTextField(int time) {
        TextField textField = new TextField();
        textField.setText(String.valueOf(time));
        textField.setMaxWidth(35);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        return textField;
    }

    private void setThresholds() {
        for (int i = 0; i < thresholds.length; i++) {
            long time = getLong(thresholds[i][0]) * 24 * 60 * 60 * 1000
                    + getLong(thresholds[i][1]) * 60 * 60 * 1000
                    + getLong(thresholds[i][2]) * 60 * 1000;
            config.setThreshold(Config.TypeAction.values()[i], time);
        }
    }

    private long getLong(TextField textField) {
        if (!textField.getText().equals("")) {
            return Long.parseLong(textField.getText());
        }
        return 0;
    }

    private String getLabel(Config.TypeAction action) {
        switch (action) {
            case JOIN:
                return "Вступление в гр.";
            case LEAVE:
                return "Выход из гр.";
            case LIKE:
                 return "Лайк";
            case REPOST:
                return "Репост";
            case ADD_FRIEND:
                return "Заявка в друзья";
            case SET_AVATAR:
                return "Установка аватара";
            default:
                return "Неизвестный параметр";
        }
    }
}
