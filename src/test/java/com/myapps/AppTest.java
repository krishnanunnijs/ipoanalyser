package com.myapps;

import com.myapps.common.Constants;
import com.myapps.vo.IPOReview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppTest {

    private App app = new App();
    public static final String YEAR = "2023";

    @Test
    public void testPerformanceSummary(){
        app.performanceSummary(YEAR);
    }

    @Test
    public void testReviewAndRatings(){
        app.reviewAndRatings(YEAR);
    }

    @Test
    public void testIpoReviewSummary(){
        IPOReview i = new IPOReview();
        i.setPageLink("https://www.chittorgarh.com/ipo/sula-vineyards-ipo/1345/");
        app.getIpoReviewSummary(i);
        Assertions.assertEquals(2,i.getReviewSummary().size());
    }

}
