
package org.plainpicture.sorting;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.ScriptModule;

public class SortScriptPlugin extends Plugin {
  @Override public String name() {
    return "sort-script";
  }

  @Override public String description() {
    return "sort-script";
  }

  public void onModule(ScriptModule module) {
    module.registerScript("sort-script", SortScriptFactory.class);
  }
}

