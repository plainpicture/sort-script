
package org.plainpicture.sorting;

import org.elasticsearch.script.NativeScriptFactory;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class SortScriptPluginTest extends TestCase {
  public void testGetNativeScripts() {
    assertNotNull(new SortScriptPlugin().getNativeScripts());
  }
}
