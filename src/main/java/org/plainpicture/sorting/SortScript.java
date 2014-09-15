
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
  private Object range = null;

  public SortScript(@Nullable Map<String, Object> params) {
    if(params != null) {
      baseField = (String)params.get("base_field");
      boosts = (Map)params.get("boosts");
      range = params.get("range");
    }
  }

  @Override
  public double runAsDouble() {
    long imageCountryId = getCountryId("country_id");
    long creatorCountryId = getCountryId("creator_country_id");
    double collectionScore = getCollectionScore();

    return getBase() - (getBoost(imageCountryId, "image_boost") * (1.0 - collectionScore) * getRand()) - (getBoost(creatorCountryId, "creator_boost") * (1.0 - collectionScore) * getRand());
  }

  private double getRange() {
    try {
      return ((Double)range).doubleValue();
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

      return ((double)result) * getRange();
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

  private double getCollectionScore() {
    try {
      return (double)docFieldDoubles("collection_score").getValue();
    } catch(Exception e) {
      return 0.0;
    }
  }

  private double getRand() {
    try {
      return (double)docFieldDoubles("rand").getValue();
    } catch(Exception e) {
      return 1.0;
    }
  }
}

