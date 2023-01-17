package com.myapps.vo;

import com.myapps.vo.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IPOReview {

    private String company;
    private List<Review> reviews;
    private String cmRating;
    private String usersRating;
    private String pageLink;
    private List<ReviewSummary> reviewSummary;
    private boolean mainboard = false;

}
