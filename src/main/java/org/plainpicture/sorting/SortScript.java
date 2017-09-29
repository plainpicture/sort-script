
package org.plainpicture.sorting;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import java.util.Map;

public class SortScript extends AbstractDoubleSearchScript {
  private String baseField = null;
  private Map countryBoosts = null;

  private long offset = 0;
  private long maxCountryBenefit = 0;

  public SortScript(@Nullable Map<String, Object> params) {
    if(params != null) {
      baseField = (String)params.get("base_field");
      countryBoosts = (Map)params.get("country_boosts");

      offset = (long)params.get("offset");
      maxCountryBenefit = (long)params.get("max_country_benefit");
    }
  }

  @Override
  public double runAsDouble() {
    long primaryRank = getPrimaryRank();

    if(primaryRank != -1 && primaryRank < offset)
      return (double)primaryRank;

    long imageCountryId = getCountryId("keyword_country_id");
    long creatorCountryId = getCountryId("creator_country_id");
    double rand = getRand();

    return getBase() - (getCountryBoost(imageCountryId, "image_boost") * maxCountryBenefit * 0.5 * rand) - (getCountryBoost(creatorCountryId, "creator_boost") * maxCountryBenefit * 0.5 * rand);
  }

  private double getCountryBoost(Long countryId, String kind) {
    try {
      Map map = (Map)countryBoosts.get(countryId.toString());

      if(map == null)
        return 0.0;

      Object result = map.get(kind);

      if(result == null)
        return 0.0;

      return (double)result;
    } catch(Exception e) {
      return 0.0;
    }
  }

  private double getBase() {
    try {
      return (double)docFieldLongs(baseField).getValue();
    } catch(Exception e) {
      return 0.0;
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

  private double getRand() {
    try {
      return (double)docFieldDoubles("rand").getValue();
    } catch(Exception e) {
      return 1.0;
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

