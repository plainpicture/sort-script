
package org.plainpicture.sorting;

import org.plainpicture.sorting.BoostLoader;
import org.plainpicture.sorting.CreatorBoost;
import org.javalite.activejdbc.Model;
import java.util.List;

public class CreatorBoostLoader implements BoostLoader {
  public List<Model> getBoosts() {
    return CreatorBoost.findAll();
  }
}

