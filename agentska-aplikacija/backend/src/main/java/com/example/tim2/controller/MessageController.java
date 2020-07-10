package com.example.tim2.controller;

import com.example.tim2.common.UserIdentifier;
import com.example.tim2.dto.MessageDTO;
import com.example.tim2.model.Message;
import com.example.tim2.model.Role;
import com.example.tim2.model.User;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.MessageService;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import java.util.List;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserIdentifier userIdentifier;

    @GetMapping()
    public ResponseEntity<List<MessageDTO>> getAllByUser(HttpServletRequest request) throws DatatypeConfigurationException {
        User user = userIdentifier.verifyUser(request);
        String role = ((Role) user.getRoles().toArray()[0]).getName();
        String token = tokenUtils.getToken(request);
        return new ResponseEntity<>(messageService.getUserMessages(token,role), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<HttpStatus> saveMessage(@RequestBody MessageDTO messageDTO,HttpServletRequest request){
        User user = userIdentifier.verifyUser(request);
        String role = ((Role) user.getRoles().toArray()[0]).getName();
        String token = tokenUtils.getToken(request);
        if(messageService.saveMessage(messageDTO,role))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
