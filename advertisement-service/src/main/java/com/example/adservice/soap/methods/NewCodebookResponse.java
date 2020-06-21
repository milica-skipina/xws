package com.example.adservice.soap.methods;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = "response")
@XmlRootElement(namespace = "http://www.ftn.uns.ac.rs/xws_tim2" ,name = "newCodebookResponse")
public class NewCodebookResponse {

    @XmlElement(required = true)
    private String response;

    public NewCodebookResponse(){

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
