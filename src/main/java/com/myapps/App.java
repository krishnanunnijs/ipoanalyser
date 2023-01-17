package com.myapps;

import com.myapps.client.JSoupClient;
import com.myapps.common.Constants;
import com.myapps.enums.ReviewReport;
import com.myapps.enums.Reviewer;
import com.myapps.vo.IPOReview;
import com.myapps.vo.Review;
import com.myapps.vo.ReviewSummary;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.myapps.client.JSoupClient.fetchDocument;
import static com.myapps.common.Constants.*;
import static com.myapps.common.Util.*;

public class App {

    public void performanceSummary(String year) {
        try {
            Document document = fetchDocument(PERFORMANCE_TRACKER_URL + year);

            // IPO Performance Tracker
            System.out.println("IPO Performance Tracker");
            //Elements ipoTrackerTable = mainElement.getElementsByClass("table table-bordered table-striped w-auto");
            Elements ipoTrackerTable = document.selectXpath("//*[@id=\"main\"]/div[4]/div/table");

            Elements tHead = ipoTrackerTable.first()
                    .getElementsByTag(TAG_THEAD);

            tHead.first()
                    .getElementsByTag(TAG_B)
                    .forEach(a -> System.out.print(a.text() + Constants.SEPARATOR));
//            System.out.print(Constants.SEPARATOR + "Valuation" + Constants.SEPARATOR + "Listinday-Current" + Constants.SEPARATOR + "Discounted" + Constants.SEPARATOR + "NetValue"+ Constants.SEPARATOR + "NetProfit/Loss");
            System.out.println();

            Elements tBody = ipoTrackerTable.first()
                    .getElementsByTag(TAG_TBODY);
            tBody.stream()
                    .filter(t -> t.getElementsByTag(TAG_TR)
                            .size() > 0)
                    .map(tb -> tb.getElementsByTag(TAG_TR)
                            .first())
                    .forEach(tb -> {
                        Element[] ele = tb.getElementsByTag("td")
                                .toArray(Element[]::new);
                        //Company Name	Listed On	Issue Price	Listing Day Close	Listing Day Gain	Current Price	Profit/Loss
                        String data = ele[0].text();
                        System.out.print(data.substring(0, data.indexOf("IPO Detail") - 1) + Constants.SEPARATOR);
//                        double issuePrice = Double.valueOf(ele[2].text());
//                        double day1ClosingPrice = Double.valueOf(ele[3].text());
//                        double cmp = Double.valueOf(ele[5].text());
                        //netValue
//                        double valuation = cmp-issuePrice;
//                        double listinday_Current = day1ClosingPrice-cmp;
//                        char discounted = day1ClosingPrice > cmp ? 'Y' : 'N';
//                        based on issue price
//                        double netValue = Double.valueOf(ele[6].text().replace("%",""))-Double.valueOf(ele[4].text().replace("%",""));
//                        double netProfitLoss = ((cmp-day1ClosingPrice)/day1ClosingPrice)*100;
//                        double netProfitLoss = roundOff(cmp-day1ClosingPrice,day1ClosingPrice);
                                //Profit/Loss is based on issue price
                        System.out.print(ele[1].text() + Constants.SEPARATOR + ele[2].text() + Constants.SEPARATOR + ele[3].text() + Constants.SEPARATOR + ele[4].text() + Constants.SEPARATOR + ele[5].text() + Constants.SEPARATOR + ele[6].text());
//                        System.out.print(Constants.SEPARATOR + valuation + Constants.SEPARATOR + listinday_Current + Constants.SEPARATOR + discounted + Constants.SEPARATOR + netValue + Constants.SEPARATOR + netProfitLoss);

                        System.out.println();
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reviewAndRatings(String year) {
        try {
            Document document = fetchDocument(IPO_REVIEW_URL + year);

            //Element reportData = document.getElementById("report_data");
            // IPO Reviews and Rating 2023 India
            System.out.println("IPO Reviews and Rating 2023 India");
            //Elements reportTable = reportData.getElementsByClass("table table-bordered table-striped table-hover w-auto");
            Elements reportTable = document.selectXpath("//*[@id=\"report_data\"]/div/table");
            Elements tHead = reportTable.first()
                    .getElementsByTag(TAG_THEAD);
            //Elements tHead = document.selectXpath("//*[@id=\"main\"]/div[7]/div/table/thead");
            tHead.first()
                    .getElementsByTag(TAG_TH)
                    .forEach(a -> System.out.print(a.text() + Constants.SEPARATOR));

            System.out.println();
            List<IPOReview> ipoReviews = new ArrayList<>();
            Elements tBody = reportTable.first()
                    .getElementsByTag(TAG_TBODY);
            tBody.first()
                    .children()
                    .forEach(tr ->
                    {
                        Elements td = tr.children();
                        IPOReview ipoReview = new IPOReview();
                        Element element = td.get(0);
                        String link = element.select(TAG_A)
                                .attr(HREF);
                        ipoReview.setPageLink(link);
                        ipoReview.setCompany(element.text());
                        List<Review> reviews = td.get(1)
                                .children()
                                .stream()
                                .filter(t -> t.tagName()
                                        .equals(TAG_A))
                                .map(mapReviewReport())
                                .collect(Collectors.toList());
                        ipoReview.setReviews(reviews);
                        ipoReview.setCmRating(td.get(2)
                                .text()
                                .isBlank() ? ZERO : td.get(2)
                                .text());
                        ipoReview.setUsersRating(td.get(3)
                                .text());
                        ipoReviews.add(ipoReview);

                    });

            ipoReviews.forEach(i -> {
                getIpoReviewSummary(i);
            });
            ipoReviews.forEach(a -> {

                int subscribeCount = 0, totalReviews = 0;
                List<ReviewSummary> reviewSummary = a.getReviewSummary();
                if (!isNullOrEmpty(reviewSummary)) {
                    subscribeCount = reviewSummary
                            .stream()
                            .mapToInt(rs -> rs.getSubscribe())
                            .sum();
                    totalReviews = reviewSummary
                            .stream()
                            .mapToInt(rs -> rs.getSubscribe() + rs.getAvoid() + rs.getNeutral())
                            .sum();
                }
                //double buyRating = Math.round((subscribeCount/totalReviews)* 100) / 100;
                double buyRating = roundOff((double) subscribeCount, (double) totalReviews);
                System.out.println(a.getCompany() + Constants.SEPARATOR + buyRating + Constants.SEPARATOR + a.getCmRating() + Constants.SEPARATOR + a.getUsersRating() + Constants.SEPARATOR + (a.isMainboard() ? "Y" : "N") + Constants.SEPARATOR + totalReviews);
            });

            System.out.println(ipoReviews.size());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected void getIpoReviewSummary(IPOReview i) {
        String url = i.getPageLink();
        try {
            Document subDocument = fetchDocument(url);
            //System.out.println(i.getCompany());
            if (subDocument.title()
                    .endsWith("Details")) {
                //return;

                Elements listingAt = subDocument.selectXpath("//*[@id=\"main\"]/div[2]/div[6]/div/div[1]/div/table/tbody");
                listingAt.stream()
                        .map(a -> a.getElementsContainingText("Listing At"))
                        .forEach(l -> {
                            //l.get(1).getElementsByTag("td").get(1).text().contains("SME");
                            i.setMainboard(l.get(1)
                                    .getElementsByTag("td")
                                    .get(1)
                                    .text()
                                    .contains("SME") ? false : true);
                            //filter<tr>
                        });

                Elements recommendationSummary = subDocument.selectXpath("//*[@id=\"main\"]/div[2]/div[10]/div[4]/div[1]/div/div[2]/div/table");
                if (recommendationSummary.size() < 1) {
                    recommendationSummary = subDocument.selectXpath("//*[@id=\"main\"]/div[2]/div[11]/div[4]/div[1]/div/div[2]/div/table");
                }
                List<ReviewSummary> summaryList = new ArrayList<>();
                recommendationSummary.get(0)
                        .children()
                        .stream()
                        .filter(a -> a instanceof Element)
                        .flatMap(e -> e.getElementsByClass("text-center")
                                .stream())
                        .forEach(
                                s -> {
                                    Element[] reviewData = s.children()
                                            .toArray(Element[]::new);
                                    ReviewSummary reviewSummary = new ReviewSummary();
                                    reviewSummary.setReviewer(Reviewer.getReviewerByValue(reviewData[0].text()));
                                    reviewSummary.setSubscribe(Integer.valueOf(reviewData[1].text()));
                                    reviewSummary.setNeutral(Integer.valueOf(reviewData[2].text()));
                                    reviewSummary.setAvoid(Integer.valueOf(reviewData[3].text()));
                                    summaryList.add(reviewSummary);
                                });
                i.setReviewSummary(summaryList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Function<Element, Review> mapReviewReport() {
        return s -> {
            String[] reviewData = s.text()
                    .split("\\s-\\s");
            Review review = new Review();
            review.setName(reviewData[0]);
            review.setReviewReport(ReviewReport.getReportByValue(reviewData[1].strip()));
            //review.setType();
            return review;
        };
    }

}


