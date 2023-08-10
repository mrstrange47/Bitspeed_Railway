package com.example.bitespeed.backend.controller;

import com.example.bitespeed.backend.dto.IdentifyRequest;
import com.example.bitespeed.backend.dto.IdentifyResponse;
import com.example.bitespeed.backend.service.ContactService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<IdentifyResponse> listOfContact(@RequestBody IdentifyRequest identifyRequest){
        if(identifyRequest.getEmail() == null && identifyRequest.getPhoneNumber() == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contactService.getContactDetails(identifyRequest),HttpStatus.OK);
    }

    @GetMapping("/getContact/by/id/{id}")
    public IdentifyResponse getContacts(@PathVariable Long id){
        return contactService.getContactsById(id);
    }
}
