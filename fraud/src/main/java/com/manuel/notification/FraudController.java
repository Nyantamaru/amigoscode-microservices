package com.manuel.notification;

import com.manuel.clients.fraud.FraudCheckResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudController {

    private final FraudCheckHistoryService fraudCheckHistoryService;

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(
            @PathVariable("customerId") Integer customerId
    ) {

        boolean isFraudulentCustomer = fraudCheckHistoryService.
                isFraudulentCustomer(customerId);
        log.info("fraud check request for {}", customerId);
        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
