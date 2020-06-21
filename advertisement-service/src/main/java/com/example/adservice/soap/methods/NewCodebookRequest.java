package com.example.adservice.soap.methods;

import com.example.adservice.model.Codebook;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = "codebook")
@XmlRootElement(namespace = "http://www.ftn.uns.ac.rs/xws_tim2" ,name = "newCodebookRequest")
public class NewCodebookRequest {


    @XmlElement(required = true, namespace = "http://www.ftn.uns.ac.rs/xws_tim2")
    private Codebook codebook;

    public NewCodebookRequest(){

    }

    public Codebook getCodebook() {
        return codebook;
    }

    public void setCodebook(Codebook codebook) {
        this.codebook = codebook;
    }
}
