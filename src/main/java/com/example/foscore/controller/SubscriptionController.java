package com.example.foscore.controller;

import com.example.foscore.model.User;
import com.example.foscore.model.dto.SubscriptionDto;
import com.example.foscore.model.request.SubscriptionRequest;
import com.example.foscore.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    @PostMapping
    public ResponseEntity<SubscriptionDto> create(
        @AuthenticationPrincipal User principal,
        @RequestBody @Valid SubscriptionRequest request
    ) {
        SubscriptionDto created = this.subscriptionService.create(principal, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    private final SubscriptionService subscriptionService;
}
