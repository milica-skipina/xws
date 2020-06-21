package com.example.adservice.soap.methods;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(namespace = "http://ftn.uns.ac.rs/xws_tim2", name = "newAdvertisementResponse")
public class NewAdvertisementResponse {

    @XmlElement(required = true)
    private String response;

    // dodati id ko je kreira oglas, ili username/name

    public NewAdvertisementResponse(){

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

