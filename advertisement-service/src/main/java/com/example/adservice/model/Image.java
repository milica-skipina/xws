package com.example.adservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

@Entity
public class Image {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false, length=10485760)
    private String imageUrl;

    @JsonBackReference(value = "image_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Car owner;

    public Image() {
    }

    public Image(Long id, String imageUrl, Car owner) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.owner = owner;
    }

    public Image(rs.ac.uns.ftn.xws_tim2.Image i, Long id){
        this.setImageUrl(i.getImageUrl());
        this.setOwner(new Car());
        this.getOwner().setId(id);
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
