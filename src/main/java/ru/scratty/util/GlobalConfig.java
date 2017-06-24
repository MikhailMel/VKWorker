package ru.scratty.util;

/**
 * Глобальный конфиг
 * @author scratty
 */

public class GlobalConfig extends ConfigFileWorker {

    private static final String START_USER = "119576462";

    private static final String PATH_CONFIG = "global.cfg";

    private static volatile GlobalConfig instance;

    private GlobalConfig() {
        super(PATH_CONFIG);

        if (getCount() < Field.values().length) {
            firstFilling();
        }
    }

    public static synchronized GlobalConfig getInstance() {
        GlobalConfig globalConfig = instance;
        if(globalConfig == null) {
            synchronized(GlobalConfig.class) {
                globalConfig = instance;
                if(globalConfig == null) {
                    instance = globalConfig = new GlobalConfig();
                }
            }
        }
        return globalConfig;
    }

    private void firstFilling() {
        setData(Field.START_USER.name(), START_USER);
    }

    public String getField(Field field) {
        return getData(field.name());
    }

    public enum Field {
        START_USER
    }
}
