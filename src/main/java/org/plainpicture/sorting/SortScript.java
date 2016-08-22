
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
  private double range = 0;
  private double shift = 0;

  public SortScript(@Nullable Map<String, Object> params) {
    if(params != null) {
      baseField = (String)params.get("base_field");
      boosts = (Map)params.get("boosts");
      range = ((Double)params.get("range")).doubleValue();
      shift = ((Double)params.get("shift")).doubleValue();
    }
  }

  @Override
  public double runAsDouble() {
    long imageCountryId = getCountryId("country_id");
    long creatorCountryId = getCountryId("creator_country_id");
    long primaryRank = getPrimaryRank();

    if(primaryRank != -1)
      return (double)primaryRank;

    return getBase() - getBoost(imageCountryId, "image_boost") - getBoost(creatorCountryId, "creator_boost") + (range * shift * (getRand() - 0.5));
  }

  private double getRand() {
    try {
      return (double)docFieldDoubles("rand").getValue();
    } catch(Exception e) {
      return 0.0;
    }
  }

  private double getBoost(Long countryId, String kind) {
    try {
      Map map = (Map)boosts.get(countryId.toString());

      if(map == null)
        return 0.0;

      Object result = map.get(kind);

      if(result == null)
        return 0.0;

      return ((double)result) * range;
    } catch(Exception e) {
      return 0.0;
    }
  }

  private double getBase() {
    try {
      return (double)docFieldLongs(baseField).getValue();
    } catch(Exception e) {
      return 1.0;
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

  private long getPrimaryRank() {
    try {
      ScriptDocValues tmp = (ScriptDocValues)doc().get("primary_rank");

      if(tmp.isEmpty())
        return -1;

      return ((ScriptDocValues.Longs)tmp).getValue();
    } catch(Exception e) {
      return -1;
    }
  }
}

