package com.manuel.customer;

import com.manuel.clients.fraud.FraudCheckResponse;
import com.manuel.clients.fraud.FraudClient;
import com.manuel.clients.notification.NotificationClient;
import com.manuel.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final NotificationClient notificationClient;
    private final FraudClient fraudClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);
        // todo: check if fraudster

        FraudCheckResponse fraudCheckResponse =  fraudClient.isFraudster(customer.getId());

        assert fraudCheckResponse != null;
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        // todo: make it async. i.e add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi %s, welcome to Amigoscode...",
                                customer.getFirstName())
                )
        );
    }
}
