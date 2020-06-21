package com.example.adservice.soap.methods;

import com.example.adservice.model.Advertisement;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "advertisement"})
@XmlRootElement(namespace = "http://www.ftn.uns.ac.rs/xws_tim2" , name = "newAdvertisementRequest")
public class NewAdvertisementRequest {

    @XmlElement(required = true)
    private Advertisement advertisement;

    // dodati id ko je kreira oglas, ili username/name

    public NewAdvertisementRequest(){

    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
