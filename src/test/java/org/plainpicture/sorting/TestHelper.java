
package org.plainpicture.sorting;

import org.plainpicture.sorting.App;
import org.javalite.activejdbc.Base;

public class TestHelper {
  public static void setUp() {
    App.setEnvironment("test");

    Base.open(App.getConfig("jdbc.driver"), App.getConfig("jdbc.url"), App.getConfig("jdbc.username"), App.getConfig("jdbc.password"));
    Base.openTransaction();
  }

  public static void tearDown() {
    Base.rollbackTransaction();
    Base.close();
  }
}

