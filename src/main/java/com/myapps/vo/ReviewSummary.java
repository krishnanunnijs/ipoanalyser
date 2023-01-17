package com.myapps.vo;

import com.myapps.enums.Reviewer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSummary {

    private Reviewer reviewer;
    private Integer subscribe;
    private Integer neutral;
    private Integer avoid;
}
