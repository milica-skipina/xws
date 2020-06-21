package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.CodebookDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.CodebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/codebook")
public class CodebookController {

    @Autowired
    private CodebookService codebookService;

    @Autowired
    private TokenUtils tokenUtils;

    private static final Logger logger = LoggerFactory.getLogger(CodebookController.class);

    @PreAuthorize("hasAuthority('WRITE_CODE')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CodebookDTO> addC(@RequestBody CodebookDTO c, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        CodebookDTO codebook = codebookService.addCodebook(c);
        if(codebook == null) {
            // nije prosla validacija
            logger.error("user " + username + " tried to write code");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            logger.info("|ADD CODEBOOK|user " + username + " code id: " + codebook.getId());
            return new ResponseEntity<>(codebook, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('EDIT_CODE')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{id}")
    public ResponseEntity<CodebookDTO> editCb(@RequestBody CodebookDTO cb, @PathVariable Long id, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            CodebookDTO ok = codebookService.editCodebook(cb,id);
            if(ok == null) {
                // nije prosla validacija
                logger.error("user " + username + " tried to edit code id: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                logger.info("|EDIT CODEBOOK| user " + username + " code id: " + id);
                return new ResponseEntity<>(ok,HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('DELETE_CODE')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteCb(@PathVariable Long id, HttpServletRequest request){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            boolean ok = codebookService.deleteCodebook(id);
            if(ok){
                logger.info("|DELETE CODEBOOK|user " + username + " code id: " + id);
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }
            else{
                logger.error("user " + username + " tried to deleted code id: " + id);
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
