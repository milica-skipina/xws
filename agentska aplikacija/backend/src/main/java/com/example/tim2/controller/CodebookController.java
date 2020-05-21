package com.example.tim2.controller;

import com.example.tim2.dto.CodebookDTO;
import com.example.tim2.model.Codebook;
import com.example.tim2.service.CodebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/codebook")
public class CodebookController {

    @Autowired
    private CodebookService codebookService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CodebookDTO> addC(@RequestBody CodebookDTO c) {
        // provjera ulogovanog

        CodebookDTO codebook = codebookService.addCodebook(c);
        if(codebook == null) {
            // nije prosla validacija
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(codebook, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CodebookDTO>> getAll(){
        return new ResponseEntity<>(codebookService.getAll(), HttpStatus.OK);
    }
}
