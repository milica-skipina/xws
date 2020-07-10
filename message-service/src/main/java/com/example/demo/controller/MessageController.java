package com.example.demo.controller;

import com.example.demo.dto.MessageDTO;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TokenUtils tokenUtils;

    @PreAuthorize("hasAuthority('CREATE_MESSAGE')")
    @GetMapping()
    public ResponseEntity<List<MessageDTO>> getUserMessages(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        return new ResponseEntity<>(messageService.getUserMessages(username),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_MESSAGE')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<HttpStatus> addMessages(@RequestBody MessageDTO messageDTO,HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        messageDTO.setSenderUsername(username);
        if(messageService.addMessage(messageDTO,token)!=null)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
