package com.example.audit;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InquiryResponse create(@Valid @RequestBody InquiryRequest request) {
        return inquiryService.create(request);
    }
}
