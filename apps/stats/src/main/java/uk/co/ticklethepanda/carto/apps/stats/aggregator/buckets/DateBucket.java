package uk.co.ticklethepanda.carto.apps.stats.aggregator.buckets;

import java.time.LocalDate;

public interface DateBucket {
    
    public String getName();
    public float getLength();
    public String getLengthUnit();

}
