
package org.plainpicture.sorting;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;

public class SortScriptTest extends TestCase {
  public void testSortScript() {
    Map params = new HashMap();

    params.put("base_field", "rank");
    params.put("offset", (long)1);
    params.put("max_country_benefit", (long)100);

    Map creatorBoosts = new HashMap();

    creatorBoosts.put(1, 1.0);

    Map imageBoosts = new HashMap();

    imageBoosts.put(1, 1.0);

    params.put("creator_boosts", creatorBoosts);
    params.put("image_boosts", imageBoosts);

    assertNotNull(new SortScript(params));
  }
}

