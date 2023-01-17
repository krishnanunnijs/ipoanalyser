package com.myapps.enums;

import java.util.Arrays;

public enum Reviewer {

    BROKERS("Brokers"),MEMBERS("Members");

    private String type;

    Reviewer(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Reviewer getReviewerByValue(String value) {
        //System.out.println("ReviewerByValue :" + value);
        return Arrays.stream(Reviewer.values())
                .filter(rr -> rr.type.equals(value))
                .findFirst().get();
    }

}
