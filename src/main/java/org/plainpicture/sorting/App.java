
package org.plainpicture.sorting;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;

public class App {
  private static String environment = "production";
  private static Properties properties = readProperties();

  private static Properties readProperties() {
    Properties properties = new Properties();

    try {
      if(environment == "production")
        properties.load(new FileInputStream("/etc/sort-script.properties"));
      else
        properties.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/sort-script.properties"));
    } catch(IOException e) {
      // Nothing
    }

    return properties;
  }

  public static synchronized void setEnvironment(String environment) {
    App.environment = environment;
    App.properties = readProperties();
  }

  public static String getEnvironment() {
    return environment;
  }

  public static synchronized String getConfig(String key) {
    return properties.getProperty(key);
  }
}

