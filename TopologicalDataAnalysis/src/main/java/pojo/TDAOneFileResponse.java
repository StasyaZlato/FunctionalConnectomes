package pojo;

import java.util.LinkedHashMap;
import java.util.List;

public class TDAOneFileResponse {
    private String path;
    private LinkedHashMap<Integer, List<Interval>> intervals;
    private Integer numberOfSimplices;
    private String bettiNumbers;

    public TDAOneFileResponse() {
        intervals = new LinkedHashMap<>();
    }


    public void addInterval(Integer dimension, List<Interval> interval) {
        this.intervals.put(dimension, interval);
    }

    public LinkedHashMap<Integer, List<Interval>> getIntervals() {
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

    public String getBettiNumbers() {
        return bettiNumbers;
    }

    public void setBettiNumbers(String bettiNumbers) {
        this.bettiNumbers = bettiNumbers;
    }
}
