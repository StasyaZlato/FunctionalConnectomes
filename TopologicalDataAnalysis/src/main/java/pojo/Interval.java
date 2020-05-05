package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.jetbrains.annotations.NotNull;

public class Interval implements Comparable<Interval> {
    Double start;
    Double end;

    public Interval(Double start, Double end) {
        this.start = start;
        this.end = end;
    }

    public boolean isRightInfinite() {
        return end == Double.POSITIVE_INFINITY;
    }

    @JsonGetter("end")
    public Double getEnd() {
        return end;
    }

    @JsonGetter("start")
    public Double getStart() {
        return start;
    }


    @Override
    public int compareTo(@NotNull Interval o) {
        int endComparison = 0;

        if (isRightInfinite() && o.isRightInfinite()) {
            endComparison = 0;
        } else if (isRightInfinite() && !o.isRightInfinite()) {
            endComparison = 1;
        } else if (!isRightInfinite() && o.isRightInfinite()) {
            endComparison = -1;
        } else {
            // both right-finite
            endComparison = end.compareTo(o.end);
        }

        if (endComparison != 0) {
            return endComparison;
        }

        return this.start.compareTo(o.start);
    }
}
