
package org.plainpicture.sorting;

import junit.framework.TestCase;

public class SortScriptFactoryTest extends TestCase {
    public void testNewScript() {
        assertNotNull(new SortScriptFactory().newScript(null));
    }
}

