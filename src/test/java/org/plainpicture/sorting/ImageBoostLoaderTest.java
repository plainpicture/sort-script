
package org.plainpicture.sorting;

import junit.framework.TestCase;

public class ImageBoostLoaderTest extends TestCase {
  public void setUp() {
    TestHelper.setUp();
  }

  public void tearDown() {
    TestHelper.tearDown();
  }

  public void testGetBoosts() {
    ImageBoost.createIt();
    ImageBoost.createIt();

    assertEquals(2, new ImageBoostLoader().getBoosts().size());
  }
}

