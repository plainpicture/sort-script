
package org.plainpicture.sorting;

import org.elasticsearch.script.ScriptModule;
import junit.framework.TestCase;

public class SortScriptPluginTest extends TestCase {
  public void onModule(ScriptModule module) {
    module.registerScript("sort-script", SortScriptFactory.class);
  }

  public void testName() {
    assertEquals("sort-script", new SortScriptPlugin().name());
  }

  public void testDescription() {
    assertEquals("sort-script", new SortScriptPlugin().description());
  }

  public void testOnModule() {
    // Can't be tested
  }
}

