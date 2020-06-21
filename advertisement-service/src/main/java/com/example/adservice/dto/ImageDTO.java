package com.example.adservice.dto;

import com.example.adservice.model.Car;
import com.example.adservice.model.Image;

import javax.xml.bind.annotation.*;

public class ImageDTO {

    private Long id;
    private String imageUrl;
    private Car owner;

    public ImageDTO(){

    }

    public ImageDTO(Long id, String imageUrl, Car owner){
      this.id = id;
      this.imageUrl = imageUrl;
      this.owner = owner;
    }

    public ImageDTO(rs.ac.uns.ftn.xws_tim2.Image i, Long id){
        this.setImageUrl(i.getImageUrl());
        Car o = new Car();
        o.setId(id);
        this.setOwner(o);
    }

    public ImageDTO(Image i){
        this(i.getId(), i.getImageUrl(), i.getOwner());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Car getOwner() {
        return owner;
    }

    public void setOwner(Car owner) {
        this.owner = owner;
    }
}
