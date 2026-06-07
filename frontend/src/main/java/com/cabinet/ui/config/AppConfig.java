package com.cabinet.ui.config;

public class AppConfig {

    private static volatile AppConfig instance;

    private String apiBaseUrl = "http://localhost:8080/api";
    private String appName   = "CleMedice";
    private String version   = "1.0.0";
    private int    timeoutSeconds = 10;

    private AppConfig() {}

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    public String getApiBaseUrl()    { return apiBaseUrl; }
    public String getAppName()       { return appName; }
    public String getVersion()       { return version; }
    public int    getTimeoutSeconds(){ return timeoutSeconds; }

    public void setApiBaseUrl(String url)       { this.apiBaseUrl = url; }
    public void setTimeoutSeconds(int seconds)  { this.timeoutSeconds = seconds; }

    @Override
    public String toString() {
        return "AppConfig{app=" + appName + ", version=" + version +
               ", url=" + apiBaseUrl + ", timeout=" + timeoutSeconds + "s}";
    }
}
