package com.example.adservice.soap.methods;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "microId"
})
@XmlRootElement(namespace = "http://ftn.uns.ac.rs/xws_tim2", name = "newPricelistResponse")
public class NewPricelistResponse {


    @XmlElement(required = true)
    protected Long microId;

    public Long getMicroId() {
        return microId;
    }

    public void setMicroId(Long microId) {
        this.microId = microId;
    }

}
