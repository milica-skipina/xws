package com.example.adservice.dto;

import com.example.adservice.model.Car;
import com.example.adservice.model.Image;

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

    public ImageDTO(Image i){
        this(i.getId(), i.getImageUrl(), i.getOwner());
    }

}
