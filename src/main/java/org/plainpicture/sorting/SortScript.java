
package org.plainpicture.sorting;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import java.util.Map;

public class SortScript extends AbstractDoubleSearchScript {
  private String baseField = null;
  private Map boosts = null;

  public SortScript(@Nullable Map<String, Object> params) {
    if(params != null) {
      baseField = (String)params.get("base_field");
      boosts = (Map)params.get("boosts");
    }
  }

  @Override
  public double runAsDouble() {
    long imageCountryId = getCountryId("country_id");
    long creatorCountryId = getCountryId("creator_country_id");

    System.out.println((double)getBase() / (getBoost(imageCountryId, "image_boost") * getBoost(creatorCountryId, "creator_boost")));

    return (double)getBase() / (getBoost(imageCountryId, "image_boost") * getBoost(creatorCountryId, "creator_boost"));
  }

  private double getBoost(Long countryId, String kind) {
    try {
      Object result = ((Map)boosts.get(countryId.toString())).get(kind);

      if(result == null)
        return 1.0;

      return (double)result;
    } catch(Exception e) {
      return 1.0;
    }
  }

  private long getBase() {
    try {
      return docFieldLongs(baseField).getValue();
    } catch(Exception e) {
      return 1;
    }
  }

  private long getCountryId(String field) {
    try {
      ScriptDocValues tmp = (ScriptDocValues)doc().get(field);

      if(tmp.isEmpty())
        return -1;

      return ((ScriptDocValues.Longs)tmp).getValue();
    } catch(Exception e) {
      return -1;
    }
  }
}

