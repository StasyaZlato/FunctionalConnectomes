package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;

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

    @JsonGetter("intervals")
    public LinkedHashMap<Integer, List<Interval>> getIntervals() {
        return intervals;
    }

    @JsonGetter("numberOfSimplices")
    public Integer getNumberOfSimplices() {
        return numberOfSimplices;
    }

    public void setNumberOfSimplices(Integer numberOfSimplices) {
        this.numberOfSimplices = numberOfSimplices;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonGetter("bettiNumbers")
    public String getBettiNumbers() {
        return bettiNumbers;
    }

    public void setBettiNumbers(String bettiNumbers) {
        this.bettiNumbers = bettiNumbers;
    }
}
