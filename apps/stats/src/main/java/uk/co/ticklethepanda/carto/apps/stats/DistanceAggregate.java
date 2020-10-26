package uk.co.ticklethepanda.carto.apps.stats;

class DistanceAggregate {
    float sum = 0;
    float count = 0;
    String marker = "";

    float getAverage() {
        return sum / count;
    }

    @Override
    public String toString() {
        return "DistanceAggregate{" +
                "sum=" + sum +
                ", count=" + count +
                ", average=" + getAverage() +
                ", info=" + marker +
                '}';
    }
}
