
package org.plainpicture.sorting;

import junit.framework.TestCase;
import org.elasticsearch.search.lookup.SearchLookup;
import org.elasticsearch.search.lookup.DocLookup;
import java.util.HashMap;
import java.util.Map;

public class SortScriptTest extends TestCase {
  public void testSortScript() {
    Map<String, Object> params = new HashMap<String, Object>();

    params.put("country_id", 1);
    params.put("creator_country_id", 1);

    assertNotNull(new SortScript(params));
  }
}

