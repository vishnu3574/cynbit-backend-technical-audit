package com.example.audit;
import java.util.UUID;
import org.springframework.stereotype.Service;
@Service
public class InquiryService {
    public InquiryResponse create(InquiryRequest request) {
        String opaqueId = "inq_" + UUID.randomUUID();
        return new InquiryResponse(opaqueId, "received",
                "Your inquiry has been received.");
    }
}
