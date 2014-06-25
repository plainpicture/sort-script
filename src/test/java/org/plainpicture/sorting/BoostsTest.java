
package org.plainpicture.sorting;

import junit.framework.TestCase;

public class BoostsTest extends TestCase {
  public void setUp() {
    TestHelper.setUp();
  }

  public void tearDown() {
    TestHelper.tearDown();
  }

  public void testGetImageBoost() {
    Boosts imageBoosts = new Boosts(new ImageBoostLoader());

    assertEquals(1.0f, imageBoosts.get(1L, 1L));

    ImageBoost.createIt("primary_country_id", "1", "secondary_country_id", "1", "boost", "2");

    imageBoosts.runOnce();

    assertEquals(2.0f, imageBoosts.get(1L, 1L));
  }

  public void testGetCreatorBoost() {
    Boosts creatorBoosts = new Boosts(new CreatorBoostLoader());

    assertEquals(1.0f, creatorBoosts.get(1L, 1L));

    CreatorBoost.createIt("primary_country_id", "1", "secondary_country_id", "1", "boost", "2");

    creatorBoosts.runOnce();

    assertEquals(2.0f, creatorBoosts.get(1L, 1L));
  }

  public void testFetch() {
    // Already tested
  }

  public void testRunOnce() {
    // Already tested
  }
}

