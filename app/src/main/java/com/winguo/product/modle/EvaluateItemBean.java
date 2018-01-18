package com.winguo.product.modle;

import java.io.Serializable;

/**
 * @author hcpai
 * @desc 商品评论item
 */
public class EvaluateItemBean implements Serializable{
    private int review_id;
    private double rating_votes;
    private String title;
    private String detail;
    private String created_at;
    private String nickname;
    private String color_name;
    private String size;

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public double getRating_votes() {
        return rating_votes;
    }

    public void setRating_votes(double rating_votes) {
        this.rating_votes = rating_votes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
