
package org.plainpicture.sorting;

import junit.framework.TestCase;

public class CreatorBoostLoaderTest extends TestCase {
  public void setUp() {
    TestHelper.setUp();
  }

  public void tearDown() {
    TestHelper.tearDown();
  }

  public void testGetBoosts() {
    CreatorBoost.createIt();
    CreatorBoost.createIt();

    assertEquals(2, new CreatorBoostLoader().getBoosts().size());
  }
}

