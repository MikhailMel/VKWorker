package ru.scratty.util;

/**
 * Глобальный конфиг
 * @author scratty
 */

public class GlobalData {

    private static final String PATH_GROUPS = "groups.list";

    private static volatile GlobalData instance;

    private GlobalData() {

    }

    public static synchronized GlobalData getInstance() {
        GlobalData globalData = instance;
        if(globalData == null) {
            synchronized(GlobalData.class) {
                globalData = instance;
                if(globalData == null) {
                    instance = globalData = new GlobalData();
                }
            }
        }
        return globalData;
    }


}
