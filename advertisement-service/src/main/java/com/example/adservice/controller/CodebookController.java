package com.example.adservice.controller;

import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.CodebookDTO;
import com.example.adservice.service.CodebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
            logger.error("user " + username + " tried to write code");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            logger.info("|ADD CODEBOOK|user " + username + " code id: " + codebook.getId());
            return new ResponseEntity<>(codebook, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('DELETE_CODE')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteCb(@PathVariable Long id, HttpServletRequest request){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            boolean ok = codebookService.deleteCodebook(id);
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            if(ok){
                logger.info("|DELETE CODEBOOK|user " + username + " code id: " + id);
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }
            else{
                logger.error("user " + username + " tried to deleted code id: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('EDIT_CODE')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{id}")
    public ResponseEntity<CodebookDTO> editCb(@RequestBody CodebookDTO cb, @PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            CodebookDTO ok = codebookService.editCodebook(cb,id);
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            if(ok == null) {
                // nije prosla validacija
                logger.error("user " + username + " tried to edit code id: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                logger.info("|EDIT CODEBOOK| user " + username + " code id: " + id);
                return new ResponseEntity<>(ok,HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

   // @PreAuthorize("hasAuthority('READ_CODE')")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CodebookDTO>> getAllCodebooks() {
        return new ResponseEntity<>(codebookService.getAll(), HttpStatus.OK);
    }

}
