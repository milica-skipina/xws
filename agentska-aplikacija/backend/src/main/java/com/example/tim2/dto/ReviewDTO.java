package com.example.tim2.dto;

import com.example.tim2.model.Review;

import java.util.Date;

public class ReviewDTO {
    private Long id;
    private int evaluation;
    private Long carId;
    private String text;
    private String state;
    private Date date;
    private String username;

    public ReviewDTO(){

    }

    public ReviewDTO(Review r){
        this.id = r.getId();
        this.evaluation = r.getEvaluation();
        this.carId = r.getCar().getId();
        this.state = r.getState();
        this.text = r.getText();
        this.date = r.getDate();
        this.username = r.getUsername();
    }

    public ReviewDTO(Long id, int evaluation, Long carId, String text, String state, Date date, String userName) {
        this.id = id;
        this.evaluation = evaluation;
        this.carId = carId;
        this.text = text;
        this.state = state;
        this.date = date;
        this.username = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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