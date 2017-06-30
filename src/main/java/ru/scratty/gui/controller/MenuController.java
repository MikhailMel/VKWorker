package ru.scratty.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import ru.scratty.action.listener.OnMsgListener;
import ru.scratty.action.listener.OnStateBotListener;
import ru.scratty.bot.Bot;
import ru.scratty.bot.BotFather;
import ru.scratty.util.ListBots;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Контроллер меню
 * @author scratty
 */

public class MenuController implements OnMsgListener, OnStateBotListener, Initializable {

    @FXML
    ListView<Bot> listView;

    @FXML
    TextArea textArea;

    @FXML
    Button launch;

    @FXML
    Button edit;

    @FXML
    Button del;

    private BotFather botFather;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

    /**
     * Инициализируем данные при загрузке
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        botFather = BotFather.getInstance(new ListBots().getBots());
        botFather.setOnMsgListener(this);
        botFather.setOnStateBotListener(this);
        fillList();
    }

    /**
     * Нажатие на кнопку создания нового бота
     */
    @FXML
    void onClickCreate() {
        showDialog(null);
    }

    /**
     * Нажатие на кнопку редактирования бота
     */
    @FXML
    void onClickEdit() {
        showDialog(listView.getFocusModel().getFocusedItem());
    }

    /**
     * Нажатие на кнопку удаления бота
     */
    @FXML
    void onClickDel() {
        botFather.deleteBot(listView.getFocusModel().getFocusedItem());
    }


    /**
     * Создания диалога для создания/редактирования бота
     * При bot == null диалог создает бота
     * При bot != null диалог заполняет поля данными и редактирует бота
     * @param bot
     */
    private void showDialog(Bot bot) {
        System.out.println(bot);
        Dialog<Bot> dialog = new Dialog<>();
        dialog.setTitle(bot == null ? "Новый бот" : "Редактирование");

        ButtonType button = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(button, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField name = new TextField(bot != null ? bot.getNameBot() : "");
        name.setPromptText("Имя бота");
        TextField id = new TextField(bot != null ? String.valueOf(bot.getUserId()) : "");
        id.setPromptText("ID");
        id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                id.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        TextField token = new TextField(bot != null ? bot.getToken() : "");
        token.setPromptText("Токен");
        token.setMinWidth(350);

        grid.add(name, 0, 0);
        grid.add(id, 0, 1);
        grid.add(token, 0, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(param -> {
            if(param == button) {
                return new Bot(name.getText(), Integer.parseInt(id.getText()), token.getText());
            }
            return null;
        });

        Optional<Bot> result = dialog.showAndWait();
        result.ifPresent(res -> {
            if(bot == null) {
                botFather.addBot(res);
            } else {
                botFather.editBot(bot, res);
            }
        });
    }

    /**
     * Нажатие на кнопку остановки всех ботов
     */
    @FXML
    void onClickStopAll() {
        botFather.stopAll();
    }

    /**
     * Нажатие на кнопку запуска всех ботов
     */
    @FXML
    void onClickStartAll() {
        botFather.startAll();
    }

    /**
     * Нажатие на кнопку запуска/остановки оопределенного бота
     */
    @FXML
    void onClickLaunch() {
        if(listView.getFocusModel().getFocusedItem().isWorked()) {
            botFather.stopBot(listView.getFocusModel().getFocusedIndex());
        } else {
            botFather.startBot(listView.getFocusModel().getFocusedIndex());
        }
    }

    /**
     * При выборе бота из ListView утановка кнопки в зависимости от состояния
     */
    @FXML
    void selectItem() {
        if (listView.getFocusModel().getFocusedItem() != null) {
            if (listView.getFocusModel().getFocusedItem().isWorked()) {
                launch.setText("Стоп");
            } else {
                launch.setText("Старт");
            }
            launch.setVisible(true);
            edit.setVisible(true);
            del.setVisible(true);
        }
    }

    /**
     * Заполнение ListView
     */
    private void fillList() {
        listView.setItems(FXCollections.observableArrayList(botFather.getBots()));
        listView.setCellFactory(param -> new CellWithState());
    }

    /**
     * Печать переданного сообщения в TextArea
     */
    @Override
    public void sendMsg(String name, String text) {
        textArea.appendText("[" + sdf.format(new Date()) + "] " + name + ": " + text + "\n");
    }

    /**
     * Перезаполнение списка при изменениях
     */
    @Override
    public void onChangeState() {
        fillList();
    }

    /**
     * Внутренний класс, описывающий логику и вид элемента ListView
     */
    private class CellWithState extends ListCell<Bot> {
        @Override
        protected void updateItem(Bot item, boolean empty) {
            super.updateItem(item, empty);
            if(empty) {
                setGraphic(null);
            } else {
                setTextFill(item.isWorked() ? Color.GREEN : Color.RED);
                setText(item.getNameBot());
            }
        }
    }
}
