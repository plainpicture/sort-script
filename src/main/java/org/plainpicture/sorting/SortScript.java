
package org.plainpicture.sorting;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.script.AbstractDoubleSearchScript;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.search.lookup.IndexLookup;
import org.elasticsearch.search.lookup.TermPosition;
import org.elasticsearch.search.lookup.IndexFieldTerm;
import org.elasticsearch.search.lookup.IndexField;
import org.elasticsearch.search.lookup.PositionIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SortScript extends AbstractDoubleSearchScript {
  private Map<Long, List<Double>> countryBoosts;
  private List<String> keywords;

  private int offset;

  private double baseRand, baseWeight;
  private double ageRand, ageWeight;
  private double keywordRand, keywordWeight;
  private String keywordField;
  private double countryRand, countryWeight;
  private double collectionRand, collectionWeight;
  private double supplierRand, supplierWeight;
  private boolean ignorePrimaryRank;
  private Random random;

  private long now;

  public SortScript(Map<String, Object> params) {
    countryBoosts = new HashMap();

    Map<String, Map> mapping = (Map<String, Map>)params.get("country_boosts");

    for(Map.Entry<String, Map> entry : mapping.entrySet()) {
      Map<String, Double> boostsMap = (Map<String, Double>)entry.getValue();
      List<Double> boosts = new ArrayList(2);

      boosts.add(boostsMap.get("image_boost"));
      boosts.add(boostsMap.get("creator_boost"));

      countryBoosts.put(new Long(entry.getKey()), boosts);
    }

    keywords = (List<String>)params.get("keywords");

    offset = (int)params.get("offset");

    keywordRand = (double)params.get("keyword_rand");
    keywordWeight = (double)params.get("keyword_weight");
    keywordField = (String)params.get("keyword_field");

    ageRand = (double)params.get("age_rand");
    ageWeight = (double)params.get("age_weight");

    baseRand = (double)params.get("base_rand");
    baseWeight = (double)params.get("base_weight");

    countryRand = (double)params.get("country_rand");
    countryWeight = (double)params.get("country_weight");

    collectionRand = (double)params.get("collection_rand");
    collectionWeight = (double)params.get("collection_weight");

    supplierRand = (double)params.get("supplier_rand");
    supplierWeight = (double)params.get("supplier_weight");

    ignorePrimaryRank = (boolean)params.get("ignore_primary_rank");

    now = System.currentTimeMillis();
    random = new Random();
  }

  public double runAsDouble() {
    long primaryRank = ignorePrimaryRank ? -1 : getPrimaryRank();
    double baseScore = 0.0;

    random.setSeed((int)(getRand() * 100000));

    if(primaryRank != -1) {
      if(primaryRank < offset)
        return offset + (offset - (double)primaryRank);
      else
        baseScore = 1.0 - ((double)primaryRank - offset) / offset;
    } else {
      baseScore = baseWeight > 0 ? baseScore() : 0.0;
    }

    return offset
      + baseScore * (1.0 - baseRand * random.nextDouble()) * baseWeight
      + ageScore() * (1.0 - ageRand * random.nextDouble()) * ageWeight
      + keywordScore() * (1.0 - keywordRand * random.nextDouble()) * keywordWeight
      + (
          countryWeight <= 0 ? 0.0 :
            getCountryBoost(getCountryId("keyword_country_id"), 0) * (1.0 - countryRand * random.nextDouble()) * countryWeight * 0.5 +
            getCountryBoost(getCountryId("creator_country_id"), 1) * (1.0 - countryRand * random.nextDouble()) * countryWeight * 0.5
        )
      + (collectionWeight > 0 ? collectionScore() * (1.0 - collectionRand * random.nextDouble()) * collectionWeight : 0.0)
      + (supplierWeight > 0 ? supplierScore() * (1.0 - supplierRand * random.nextDouble()) * supplierWeight : 0.0);
  }

  private double baseScore() {
    return getDouble("score", 0.0);
  }

  private Double getCountryBoost(Long countryId, int index) {
    List<Double> list = countryBoosts.get(countryId);

    if(list == null)
      return 0.0;

    return list.get(index);
  }

  private double collectionScore() {
    return getDouble("collection_score", 0.0);
  }

  private double supplierScore() {
    return getDouble("supplier_score", 0.0);
  }

  private double keywordScore() {
    double res = 0.0;
    int n = 0;

    if(keywords.size() == 0)
      return 0.0;

    IndexField termScores = termScores(keywordField);

    for(String keyword : keywords) {
      for(TermPosition termPosition : termScores.get(keyword, IndexLookup.FLAG_PAYLOADS | IndexLookup.FLAG_CACHE)) {
        res += termPosition.payloadAsFloat(0.0f);

        n++;
      }
    }

    if(n == 0)
      return 0.0;

    return res / (double)n;
  }

  protected IndexField termScores(String fieldName) {
    return indexLookup().get(fieldName);
  }

  private double ageScore() {
    Long value = getLong("batched_at", -1l);

    if(value == -1)
      return 0.0;

    return Math.pow(1.0 - Math.pow(((double)now - Math.min((double)now, (double)value)) / ((double)now - 978307200000.0), 0.75), 2.0);
  }

  private long getCountryId(String field) {
    return getLong(field, -1l);
  }

  private double getRand() {
    return getDouble("rand", 0.0d);
  }

  private long getPrimaryRank() {
    return getLong("primary_rank", -1l);
  }

  protected double getDouble(String fieldName, double defaultValue) {
    ScriptDocValues.Doubles value = docFieldDoubles(fieldName);

    if(value == null || value.isEmpty())
      return defaultValue;

    return value.getValue();
  }

  protected long getLong(String fieldName, long defaultValue) {
    ScriptDocValues.Longs value = docFieldLongs(fieldName);

    if(value == null || value.isEmpty())
      return defaultValue;

    return value.getValue();
  }
}
