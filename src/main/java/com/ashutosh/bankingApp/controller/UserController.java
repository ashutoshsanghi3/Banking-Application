package com.ashutosh.bankingApp.controller;

import com.ashutosh.bankingApp.dto.request.EnquiryRequest;
import com.ashutosh.bankingApp.dto.request.UserRequest;
import com.ashutosh.bankingApp.dto.response.BankResponse;
import com.ashutosh.bankingApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("")
    public BankResponse createUser_Account(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }


}
