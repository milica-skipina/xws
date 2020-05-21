package com.example.adservice.controller;

import com.example.adservice.dto.CodebookDTO;
import com.example.adservice.dto.PricelistDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/codebook")
public class CodebookController {

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<HttpStatus> addC(@RequestBody CodebookDTO c) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<HttpStatus>deleteCodebook(@PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}",  produces = "application/json", consumes = "application/json")
    public ResponseEntity<HttpStatus>editCodebook(@RequestBody CodebookDTO cdto, @PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PricelistDTO> getOneCodebook(@Valid @Min(1) @Max(10000) @PathVariable Long id) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CodebookDTO>> getAllCodebooks() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
