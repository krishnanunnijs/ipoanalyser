package com.myapps.vo;

import com.myapps.enums.ReviewReport;
import com.myapps.enums.Reviewer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {

    private String name;
    private Reviewer type;
    private ReviewReport reviewReport;

}
