
package org.plainpicture.sorting;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.common.settings.Settings;
import org.plainpicture.sorting.SortScript;

import java.util.Map;

public class SortScriptFactory implements NativeScriptFactory {
  @Override public ExecutableScript newScript(@Nullable Map<String, Object> params) {
    return new SortScript(params);
  }

  @Override public boolean needsScores() {
    return false;
  }

  @Override public String getName() {
    return "sort_script";
  }
}

