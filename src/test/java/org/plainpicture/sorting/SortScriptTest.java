
package org.plainpicture.sorting;

import junit.framework.TestCase;
import junit.framework.Assert;
import org.mockito.Mockito;
import org.junit.runner.RunWith;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.search.lookup.IndexField;
import org.elasticsearch.search.lookup.IndexFieldTerm;
import org.elasticsearch.search.lookup.TermPosition;
import static org.mockito.Matchers.any;

class TestSortScript extends SortScript {
  private Map doc;

  public TestSortScript(Map<String, Object> params) {
    super(params);
  }

  public void setDoc(Map<String, Object> doc) {
    this.doc = doc;
  }

  @Override
  protected long getLong(String fieldName, long defaultValue) {
    Long res = (Long)doc.get(fieldName);

    if(res == null)
      return defaultValue;

    return res;
  }

  @Override
  protected double getDouble(String fieldName, double defaultValue) {
    Double res = (Double)doc.get(fieldName);

    if(res == null)
      return defaultValue;

    return res;
  }

  @Override
  protected IndexField termScores(String fieldName) {
    IndexField indexField = Mockito.mock(IndexField.class);

    if(!doc.containsKey("keywords"))
      return indexField;

    for(Map.Entry<String, Float> entry : ((Map<String, Float>)doc.get("keywords")).entrySet()) {
      TermPosition termPosition = Mockito.mock(TermPosition.class);
      Mockito.when(termPosition.payloadAsFloat(0.0f)).thenReturn(entry.getValue());

      Iterator iterator = Mockito.mock(Iterator.class);
      Mockito.when(iterator.hasNext()).thenReturn(true, false);
      Mockito.when(iterator.next()).thenReturn(termPosition);

      IndexFieldTerm indexFieldTerm = Mockito.mock(IndexFieldTerm.class);
      Mockito.when(indexFieldTerm.iterator()).thenReturn(iterator);

      Mockito.when(indexField.get(Mockito.eq(entry.getKey()), Mockito.anyInt())).thenReturn(indexFieldTerm);
    }

    return indexField;
  }
}

public class SortScriptTest extends TestCase {
  private Map defaultParams() {
    Map params = new HashMap();

    params.put("offset", 0);
    params.put("base_weight", 1.0);
    params.put("base_rand", 0.0);
    params.put("keyword_weight", 1.0);
    params.put("keyword_rand", 0.0);
    params.put("keywords", new ArrayList());
    params.put("age_weight", 1.0);
    params.put("age_rand", 0.0);
    params.put("country_weight", 1.0);
    params.put("country_rand", 0.0);
    params.put("collection_weight", 1.0);
    params.put("collection_rand", 0.0);
    params.put("supplier_weight", 1.0);
    params.put("supplier_rand", 0.0);
    params.put("ignore_primary_rank", false);

    Map countryBoost = new HashMap();
    countryBoost.put("image_boost", 1.0);
    countryBoost.put("creator_boost", 1.0);

    Map countryBoosts = new HashMap();
    countryBoosts.put("1", countryBoost);

    params.put("country_boosts", countryBoosts);

    return params;
  }

  private Map defaultDoc() {
    Map doc = new HashMap();
    doc.put("rand", 0.0);
    doc.put("score", 0.0);
    doc.put("batched_at", 978307200001l);
    doc.put("supplier_score", 0.0);
    doc.put("collection_score", 0.0);

    return doc;
  }

  public void testBlank() throws Exception {
    TestSortScript testSortScript = new TestSortScript(defaultParams());
    testSortScript.setDoc(new HashMap());

    assertEquals(0.0, testSortScript.runAsDouble(), 0.0001);
  }

  public void testBaseScore() throws Exception {
    TestSortScript testSortScript = new TestSortScript(defaultParams());

    Map doc = defaultDoc();
    doc.put("score", 1.0);

    testSortScript.setDoc(doc);

    assertEquals(1.0, testSortScript.runAsDouble(), 0.0001);
  }

  public void testAgeScore() {
    TestSortScript testSortScript = new TestSortScript(defaultParams());

    Map doc = defaultDoc();
    doc.put("batched_at", System.currentTimeMillis());

    testSortScript.setDoc(doc);

    assertEquals(1.0, testSortScript.runAsDouble(), 0.0001);
  }

  public void testKeywordScore() {
    List paramKeywords = new ArrayList();
    paramKeywords.add("forrest");
    paramKeywords.add("tree");

    Map params = defaultParams();
    params.put("keywords", paramKeywords);

    Map<String, Float> docKeywords = new HashMap<String, Float>();
    docKeywords.put("forrest", 0.5f);
    docKeywords.put("tree", 0.3f);

    Map doc = defaultDoc();
    doc.put("keywords", docKeywords);

    TestSortScript testSortScript = new TestSortScript(params);
    testSortScript.setDoc(doc);

    assertEquals(0.4, testSortScript.runAsDouble(), 0.0001);
  }

  public void testKeywordCountryScore() {
    TestSortScript testSortScript = new TestSortScript(defaultParams());

    Map doc = defaultDoc();
    doc.put("keyword_country_id", 1l);

    testSortScript.setDoc(doc);

    assertEquals(0.5, testSortScript.runAsDouble(), 0.0001);
  }

  public void testCreatorCountryScore() {
    TestSortScript testSortScript = new TestSortScript(defaultParams());

    Map doc = defaultDoc();
    doc.put("creator_country_id", 1l);

    testSortScript.setDoc(doc);

    assertEquals(0.5, testSortScript.runAsDouble(), 0.0001);
  }

  public void testCollectionScore() {
    TestSortScript testSortScript = new TestSortScript(defaultParams());

    Map doc = defaultDoc();
    doc.put("collection_score", 1.0);

    testSortScript.setDoc(doc);

    assertEquals(1.0, testSortScript.runAsDouble(), 0.0001);
  }

  public void testSupplierScore() {
    TestSortScript testSortScript = new TestSortScript(defaultParams());

    Map doc = defaultDoc();
    doc.put("supplier_score", 1.0);

    testSortScript.setDoc(doc);

    assertEquals(1.0, testSortScript.runAsDouble(), 0.0001);
  }

  public void testAll() {
    Map params = defaultParams();
    params.put("base_weight", 0.3);
    params.put("keyword_weight", 0.5);
    params.put("age_weight", 0.9);
    params.put("country_weight", 0.2);
    params.put("collection_weight", 0.3);
    params.put("supplier_weight", 0.3);

    TestSortScript testSortScript = new TestSortScript(params);

    Map doc = defaultDoc();

    testSortScript.setDoc(doc);
    doc.put("score", 0.8);
    doc.put("batched_at", 978307200000l + (System.currentTimeMillis() - 978307200000l) / 2);
    doc.put("collection_score", 0.6);
    doc.put("supplier_score", 0.4);
    doc.put("keyword_country_id", 1l);
    doc.put("creator_country_id", 1l);

    assertEquals(0.88791, testSortScript.runAsDouble(), 0.0001);
  }
}
