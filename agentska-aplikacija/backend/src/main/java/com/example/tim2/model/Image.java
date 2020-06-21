package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.context.annotation.ScopeMetadata;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false, length=10485760)
    private String imageUrl;

    @JsonBackReference(value = "image_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Car owner;

    public Image() {
    }

    public Image(String imageUrl, Car owner) {
        this.imageUrl = imageUrl;
        this.owner = owner;
    }

    public com.example.tim2.soap.gen.Image getGenerated(){
        com.example.tim2.soap.gen.Image image = new com.example.tim2.soap.gen.Image();
        image.setCarId(owner.getMicroId());
        image.setImageUrl(getImageUrl());
        return  image;
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
