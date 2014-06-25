
package org.plainpicture.sorting;

import org.plainpicture.sorting.BoostLoader;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Boosts implements Runnable {
  private BoostLoader boostLoader = null;
  private Map boosts = null;

  public Boosts(BoostLoader boostLoader) {
    this.boostLoader = boostLoader;

    new Thread(this).start();
  }

  public Float get(Long primaryCountryId, Long secondaryCountryId) {
    try {
      Object result = ((Map)boosts.get(primaryCountryId)).get(secondaryCountryId);

      if(result == null)
        return 1.0f;

      return (Float)result;
    } catch(NullPointerException e) {
      return 1.0f;
    }
  }

  private synchronized void setBoosts(Map boosts) {
    this.boosts = boosts;
  }

  public void fetch() {
    List<Model> list = boostLoader.getBoosts();

    Map newBoosts = new HashMap();

    for(Model model: list) {
      Long primaryCountryId = model.getLong("primary_country_id");
      Long secondaryCountryId = model.getLong("secondary_country_id");
      Float boost = model.getFloat("boost");

      if(newBoosts.get(primaryCountryId) == null)
        newBoosts.put(primaryCountryId, new HashMap());

      ((Map)newBoosts.get(primaryCountryId)).put(secondaryCountryId, boost);
    }

    setBoosts(newBoosts);
  }

  public void run() {
    while(true) {
      runOnce();

      sleep(300 + (int)(Math.random() * 60));
    }
  }

  public void runOnce() {
    try {
      connect();
      fetch();
    } catch(Exception e) {
      // Nothing
    }
  }

  private void connect() {
    if(!Base.hasConnection())
      Base.open(App.getConfig("jdbc.driver"), App.getConfig("jdbc.url"), App.getConfig("jdbc.username"), App.getConfig("jdbc.password"));
  }

  private void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch(InterruptedException e) {
      // Nothing
    }
  }
}

