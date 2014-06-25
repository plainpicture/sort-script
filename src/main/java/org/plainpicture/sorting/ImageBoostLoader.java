
package org.plainpicture.sorting;

import org.plainpicture.sorting.BoostLoader;
import org.plainpicture.sorting.ImageBoost;
import org.javalite.activejdbc.Model;
import java.util.List;

public class ImageBoostLoader implements BoostLoader {
  public List<Model> getBoosts() {
    return ImageBoost.findAll();
  }
}

