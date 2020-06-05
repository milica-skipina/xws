package com.example.adservice.controller;

import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.CodebookDTO;
import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.service.CodebookService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CodebookService codebookService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CodebookDTO> addC(@RequestBody CodebookDTO c) {

        CodebookDTO codebook = codebookService.addCodebook(c);
        if(codebook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(codebook, HttpStatus.OK);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteCb(@PathVariable Long id){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            boolean ok = codebookService.deleteCodebook(id);
            if(ok){
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{id}")
    public ResponseEntity<CodebookDTO> editCb(@RequestBody CodebookDTO cb, @PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            CodebookDTO ok = codebookService.editCodebook(cb,id);
            if(ok == null) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(ok,HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<List<CodebookDTO>> getAllCodebooks() {
        return new ResponseEntity<>(codebookService.getAll(), HttpStatus.OK);
    }

}
