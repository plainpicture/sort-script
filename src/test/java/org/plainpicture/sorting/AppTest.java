
package org.plainpicture.sorting;

import junit.framework.TestCase;

public class AppTest extends TestCase {
  public void testSetEnvironment() {
    App.setEnvironment("unknown");

    assertEquals("unknown", App.getEnvironment());
  }

  public void testGetEnvironment() {
    // Already tested
  }

  public void testGetConfig() {
    App.setEnvironment("test");

    assertNotNull(App.getConfig("jdbc.driver"));
    assertNotNull(App.getConfig("jdbc.url"));
    assertNotNull(App.getConfig("jdbc.username"));
    assertNotNull(App.getConfig("jdbc.password"));
  }
}

