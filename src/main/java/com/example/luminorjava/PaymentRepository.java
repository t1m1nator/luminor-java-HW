package com.example.luminorjava;



import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    Payment findPaymentById(Integer id);
    Payment findPaymentsByDebtorIban(String debtorIban);


}

