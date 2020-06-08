package org.pk.library.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Klasa reprezentująca ustawienia aplikacji.
 */
public class Settings {
    private static final Settings instance = new Settings();
    private final Properties appSettings;

    public static Settings getInstance() {
        return instance;
    }

    private Settings() {
        appSettings = new Properties();
        try {
            appSettings.loadFromXML(new FileInputStream("app.properties"));
        } catch (IOException e) {
            createDefaultFileSetting();
        }
    }

    private void createDefaultFileSetting(){
        appSettings.setProperty("appIcon", String.valueOf(App.class.getResource("icon.png")));
        appSettings.setProperty("appTitle", "Biblioteka");
        appSettings.setProperty("darkTheme", "true");
        appSettings.setProperty("hostname", "srv31876.seohost.com.pl");
        appSettings.setProperty("port", "3306");
        appSettings.setProperty("databaseName", "srv31876_library");
        appSettings.setProperty("username", "srv31876_pklibrary");
        appSettings.setProperty("password", "WBbYzm0SJC");
        appSettings.setProperty("serverTimezone", "Europe/Warsaw");
        try {
            appSettings.storeToXML(new FileOutputStream("app.properties"),"Plik konfiguracyjny aplikacji");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getIconPath(){
        return appSettings.getProperty("appIcon");
    }

    public void setIconPath(String iconPath){
        appSettings.setProperty("appIcon",iconPath);
        saveAppProp();
    }

    public String getAppTitle(){
        return appSettings.getProperty("appTitle");
    }

    public Boolean getDarkThemeProp(){
        return Boolean.valueOf(appSettings.getProperty("darkTheme"));
    }

    public void setDarkThemeProp(Boolean darkTheme){
        appSettings.setProperty("darkTheme",darkTheme.toString());
        saveAppProp();
    }

    private void saveAppProp() {
        try {
            FileOutputStream fs = new FileOutputStream("app.properties");
            appSettings.storeToXML(fs,"Plik konfiguracyjny aplikacji");
            fs.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getDatabaseURL(){
        return "jdbc:mysql://" + appSettings.getProperty("hostname") +":" +
                appSettings.getProperty("port") + "/" +
                appSettings.getProperty("databaseName") + "?serverTimezone=" +
                appSettings.getProperty("serverTimezone");
    }

    public String getDatabaseUsername(){
        return appSettings.getProperty("username");
    }

    public String getDatabasePassword(){
        return appSettings.getProperty("password");
    }
}
