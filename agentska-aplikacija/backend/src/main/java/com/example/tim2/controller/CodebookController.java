package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.CodebookDTO;
import com.example.tim2.service.CodebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping(value = "/codebook")
public class CodebookController {

    @Autowired
    private CodebookService codebookService;

    @PreAuthorize("hasAuthority('WRITE_CODE')")
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

    @PreAuthorize("hasAuthority('EDIT_CODE')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{id}")
    public ResponseEntity<CodebookDTO> editCb(@RequestBody CodebookDTO cb, @PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            CodebookDTO ok = codebookService.editCodebook(cb,id);
            if(ok == null) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(ok,HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('DELETE_CODE')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteCb(@PathVariable Long id){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            boolean ok = codebookService.deleteCodebook(id);
            if(ok){
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CodebookDTO>> getAll(){
        return new ResponseEntity<>(codebookService.getAll(), HttpStatus.OK);
    }
}
