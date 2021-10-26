package com.example.arrow;

import java.util.Comparator;

public class RateSorter implements Comparator<RecommendedHelperClass> {
    @Override
    public int compare(RecommendedHelperClass o1, RecommendedHelperClass o2) {
        if (o2.getRating() > o1.getRating())
            return 1;
        else if (o2.getRating() == o1.getRating())
            return 0;
        else
            return -1;
    }
}
