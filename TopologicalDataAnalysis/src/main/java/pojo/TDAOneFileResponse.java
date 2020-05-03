package pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDAOneFileResponse {
    private String path;
    private Map<Integer, List<Interval>> intervals;
    private Integer numberOfSimplices;

    public TDAOneFileResponse() {
        intervals = new HashMap<>();
    }


    public void addInterval(Integer dimension, List<Interval> interval) {
        this.intervals.put(dimension, interval);
    }

    public Map<Integer, List<Interval>> getIntervals() {
        return intervals;
    }

    public void setNumberOfSimplices(Integer numberOfSimplices) {
        this.numberOfSimplices = numberOfSimplices;
    }

    public Integer getNumberOfSimplices() {
        return numberOfSimplices;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
