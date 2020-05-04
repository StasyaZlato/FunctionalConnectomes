package pojo;

public class Interval {
    Double start;
    Double end;

    public Interval(Double start, Double end) {
        this.start = start;
        this.end = end;
    }

    public boolean isLeftInfinite() {
        return end == Double.POSITIVE_INFINITY;
    }

    public Double getEnd() {
        return end;
    }

    public Double getStart() {
        return start;
    }
}
