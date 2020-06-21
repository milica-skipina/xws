package com.example.adservice.soap.methods;

import com.example.adservice.model.Pricelist;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "pricelist"})
@XmlRootElement(namespace = "http://ftn.uns.ac.rs/xws_tim2", name = "newPricelistRequest")
public class NewPricelistRequest {


    @XmlElement(required = true)
    private Pricelist pricelist;

    public NewPricelistRequest(){

    }

    public Pricelist getPricelist() {
        return pricelist;
    }

    public void setPricelist(Pricelist pricelist) {
        this.pricelist = pricelist;
    }
}
