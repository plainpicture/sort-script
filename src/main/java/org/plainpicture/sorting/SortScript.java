
package org.plainpicture.sorting;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.plainpicture.sorting.Boosts;
import org.plainpicture.sorting.CreatorBoostLoader;
import org.plainpicture.sorting.ImageBoostLoader;

import java.util.Map;
import java.lang.Math;

public class SortScript extends AbstractDoubleSearchScript {
  private static Boosts creatorBoosts = new Boosts(new CreatorBoostLoader());
  private static Boosts imageBoosts = new Boosts(new ImageBoostLoader());

  private String baseField = null;
  private long countryId = -1;

  public SortScript(@Nullable Map<String,Object> params) {
    if(params != null) {
      countryId = ((Integer)params.get("country_id")).intValue();
      baseField = (String)params.get("base_field");
    }
  }

  @Override
  public double runAsDouble() {
    long imageCountryId = getCountryId("country_id");
    long creatorCountryId = getCountryId("creator_country_id");

    return (double)getBase() / (double)imageBoosts.get(countryId, imageCountryId) * (double)creatorBoosts.get(countryId, creatorCountryId);
  }

  private long getBase() {
    if(doc().containsKey(baseField))
      return docFieldLongs(baseField).getValue();

    return 1;
  }

  private long getCountryId(String field) {
    if(!doc().containsKey(field))
      return -1;

    ScriptDocValues.Longs tmp = (ScriptDocValues.Longs)doc().get(field);

    if(tmp.isEmpty())
      return -1;

    return tmp.getValue();
  }
}

