
package org.plainpicture.sorting;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SortScriptTest extends TestCase {
  public void testSortScript() {
    Map params = new HashMap();

    params.put("offset", 100);
    params.put("base_weight", 1.0);
    params.put("base_rand", 1.0);
    params.put("keyword_weight", 1.0);
    params.put("keyword_rand", 1.0);
    params.put("keywords", new ArrayList());
    params.put("age_weight", 1.0);
    params.put("age_rand", 1.0);
    params.put("country_weight", 1.0);
    params.put("country_rand", 1.0);
    params.put("collection_weight", 1.0);
    params.put("collection_rand", 1.0);
    params.put("supplier_weight", 1.0);
    params.put("supplier_rand", 1.0);

    Map countryBoost = new HashMap();
    countryBoost.put("image_boost", 1.0);
    countryBoost.put("creator_boost", 1.0);

    Map countryBoosts = new HashMap();
    countryBoosts.put("1", countryBoost);

    params.put("country_boosts", countryBoosts);

    assertNotNull(new SortScript(params));
  }
}

