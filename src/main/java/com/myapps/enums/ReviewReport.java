package com.myapps.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ReviewReport {

    SUBSCRIBE("Subscribe"),
    APPLY("Apply"),
    NEUTRAL("Neutral"),
    MAY_APPLY("May apply"),
    AVOID("Avoid"),
    NOT_RATED("Not Rated");

    private String report;

    ReviewReport(String report){
        this.report = report;
    }

    // Reverse lookup methods
    public static ReviewReport getReportByValue(String value) {
        //System.out.println("ReportByValue :" + value);
        return Arrays.stream(ReviewReport.values())
                .filter(rr -> rr.report.equals(value))
                .findFirst().get();
    }

    public String getReport() {
        return report;
    }
}
