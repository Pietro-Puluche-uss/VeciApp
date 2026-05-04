package com.veciapp.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veciapp.api.dto.ReportCategoryResponse;
import com.veciapp.api.model.ReportCategory;

@RestController
public class ReportCategoryController {

    @GetMapping("/api/report-categories")
    public List<ReportCategoryResponse> getCategories() {
        return Arrays.stream(ReportCategory.values())
                .map(ReportCategoryResponse::from)
                .toList();
    }
}

