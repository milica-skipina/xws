package com.example.adservice.controller;

import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.model.Pricelist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/pricelist")
public class PricelistController {

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<HttpStatus> addP(@RequestBody PricelistDTO p) {
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<HttpStatus> editPricelist(@Valid @RequestBody PricelistDTO p, @PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<HttpStatus>deletePricelist(@PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PricelistDTO> getOnePricelist(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PricelistDTO>> getAllPricelists() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
