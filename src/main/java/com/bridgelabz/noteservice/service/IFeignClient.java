package com.bridgelabz.noteservice.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "userservice-eureka-client", url = "http://localhost:2001")
@Service
public interface IFeignClient {
    @GetMapping(value = "users/allusers")
    public List<?> getAllUsers();
}