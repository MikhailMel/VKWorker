package ru.scratty.bot;

import ru.scratty.action.listener.OnMsgListener;
import ru.scratty.action.listener.OnStateBotListener;

import java.util.ArrayList;

/**
 * Класс, управляющий всеми ботами
 * @author scratty
 */

public class BotFather {

    private static BotFather instance;

    private ArrayList<Bot> bots;

    private OnMsgListener onMsgListener;

    private OnStateBotListener onStateBotListener;

    private BotFather() {
    }

    public static BotFather getInstance(ArrayList<Bot> bots) {
        if(instance == null) {
            instance = new BotFather();
        }

        instance.setBots(bots);
        return instance;
    }

    private void setBots(ArrayList<Bot> bots) {
        this.bots = bots;
    }

    /**
     * Установка листенера сообщений для всех ботов
     * @param onMsgListener
     */
    public void setOnMsgListener(OnMsgListener onMsgListener) {
        this.onMsgListener = onMsgListener;
        for(Bot bot: bots) {
            bot.setOnMsgListener(onMsgListener);
        }
    }

    /**
     * Установка листенера состояния для всех ботов
     * @param onStateBotListener
     */
    public void setOnStateBotListener(OnStateBotListener onStateBotListener) {
        this.onStateBotListener = onStateBotListener;
        for(Bot bot: bots) {
            bot.setOnStateBotListener(onStateBotListener);
        }
    }

    public void addBot(Bot bot) {
        bot.setOnMsgListener(onMsgListener);
        bot.setOnStateBotListener(onStateBotListener);
        bots.add(bot);
    }

    public void deleteBot(Bot bot) {
        int i = findBot(bot);
        if(i != -1) {
            bots.remove(i);
            bot.delete();
        }
    }

    public void editBot(Bot oldBot, Bot newBot) {
        deleteBot(oldBot);
        addBot(newBot);
        onStateBotListener.onChangeState();
    }

    private int findBot(Bot bot) {
        for(int i = 0; i < bots.size(); i++) {
            if(bots.get(i).getNameBot().equals(bot.getNameBot())
                    && bots.get(i).getId() == bot.getId() && bots.get(i).getToken().equals(bot.getToken())) {
                return i;
            }
        }
        return -1;
    }

    public void startBot(int i) {
        if(i >= 0 && i < getCount()) {
            bots.get(i).start();
        }
    }

    public void startAll() {
        for(int i = 0; i < bots.size(); i++) {
            startBot(i);
        }
    }

    public void stopBot(int i) {
        if(i >= 0 && i < getCount()) {
            bots.get(i).stopBot();

            Bot bot = new Bot(bots.get(i).getNameBot());
            bot.setOnMsgListener(onMsgListener);
            bot.setOnStateBotListener(onStateBotListener);
            bots.set(i, bot);
        }
    }

    public void stopAll() {
        for(int i = 0; i < bots.size(); i++) {
            stopBot(i);
        }
    }

    public int getCount() {
        return bots.size();
    }

    public ArrayList<Bot> getBots() {
        return bots;
    }

}
