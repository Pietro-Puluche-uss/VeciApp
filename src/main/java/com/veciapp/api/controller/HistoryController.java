package com.veciapp.api.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.HistoryItemResponse;
import com.veciapp.api.security.SecurityUtils;
import com.veciapp.api.service.HistoryService;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/mine")
    public List<HistoryItemResponse> listMine(Authentication authentication) {
        return historyService.listMine(SecurityUtils.currentUserId(authentication));
    }
}

