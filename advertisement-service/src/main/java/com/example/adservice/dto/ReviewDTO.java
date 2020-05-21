package com.example.adservice.dto;

public class ReviewDTO {
    private Long id;
    private int evaluation;
    private Long userId;
    private Long advertisementId;
    private String text;
    private String state;

    public ReviewDTO(){

    }

    public ReviewDTO(Long id, int evaluation, Long userId, Long advertisementId, String text, String state) {
        this.id = id;
        this.evaluation = evaluation;
        this.userId = userId;
        this.advertisementId = advertisementId;
        this.text = text;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
