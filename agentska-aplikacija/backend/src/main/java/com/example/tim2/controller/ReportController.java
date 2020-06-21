package com.example.tim2.controller;

import com.example.tim2.dto.ReportDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TokenUtils tokenUtils;

    private static final Logger logger = LoggerFactory.getLogger(CodebookController.class);

    @PreAuthorize("hasAuthority('CREATE_REPORT')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value= "/{id}")
    public ResponseEntity<HttpStatus> addReport(@RequestBody ReportDTO report, HttpServletRequest request, @PathVariable Long id) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        ReportDTO reportDTO = reportService.createReport(report, id);
        if(reportDTO == null) {
            // nije prosla validacija
            logger.error("user " + username + " tried to write report");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            logger.info("|ADD REPORT|user " + username + ", report id: " + reportDTO.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


}
