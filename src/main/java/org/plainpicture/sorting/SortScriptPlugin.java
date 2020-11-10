
package org.plainpicture.sorting;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.script.NativeScriptFactory;
import java.util.Arrays;
import java.util.List;

public class SortScriptPlugin extends Plugin implements ScriptPlugin {
  @Override public List<NativeScriptFactory> getNativeScripts() {
    return Arrays.asList((NativeScriptFactory)new SortScriptFactory());
  }
}
