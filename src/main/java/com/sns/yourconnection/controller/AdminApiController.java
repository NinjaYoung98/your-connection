package com.sns.yourconnection.controller;

import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.param.users.admin.AdminCreateRequest;
import com.sns.yourconnection.service.admin.AdminCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminApiController {

    private final AdminCreationService adminCreationService;

    @PostMapping("create")
    public ResponseSuccess<Void> createAdmin(@RequestBody AdminCreateRequest adminCreateRequest) {
        log.info("[AdminApiController -> Called] Create admin account api has called ");
        adminCreationService.createAccount(adminCreateRequest.getEmail());

        log.info("[AdminApiController -> Completed] Admin account has been successfully created ");
        return ResponseSuccess.response();
    }
}
