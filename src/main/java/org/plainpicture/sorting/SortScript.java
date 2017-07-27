
package org.plainpicture.sorting;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.index.fielddata.ScriptDocValues.Doubles;
import org.elasticsearch.index.fielddata.ScriptDocValues.Longs;
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
    long primaryRank = getPrimaryRank();

    if(primaryRank != -1)
      return (double)primaryRank;

    return getBase() - (getBoost(imageCountryId, "image_boost") * (1.0 - collectionScore) * getRand()) - (getBoost(creatorCountryId, "creator_boost") * (1.0 - collectionScore) * getRand());
  }

  private double getRange() {
    return ((Double)range).doubleValue();
  }

  private double getBoost(Long countryId, String kind) {
    Map map = (Map)boosts.get(countryId.toString());

    if(map == null)
      return 0.0;

    Object result = map.get(kind);

    if(result == null)
      return 0.0;

    return ((double)result) * getRange();
  }

  private double getBase() {
    Longs tmp = docFieldLongs(baseField);

    if(tmp.isEmpty())
      return 1.0;

    return (double)tmp.getValue();
  }

  private long getCountryId(String field) {
    Longs tmp = docFieldLongs(field);

    if(tmp.isEmpty())
      return -1;

    return tmp.getValue();
  }

  private double getCollectionScore() {
    Doubles tmp = docFieldDoubles("collection_score");

    if(tmp.isEmpty())
      return 0.0;

    return tmp.getValue();
  }

  private double getRand() {
    Doubles tmp = docFieldDoubles("rand");

    if(tmp.isEmpty())
      return 1.0;

    return tmp.getValue();
  }

  private long getPrimaryRank() {
    Longs tmp = docFieldLongs("primary_rank");

    if(tmp.isEmpty())
      return -1;

    return tmp.getValue();
  }
}

