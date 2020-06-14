package com.iex.stocktrading.audit;

import com.iex.stocktrading.model.CustomHttpTrace;
import com.iex.stocktrading.model.Transaction;
import com.iex.stocktrading.repository.TraceRepository;
import com.iex.stocktrading.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class MessageReceiver {

    @Autowired
    private TraceRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    @JmsListener(destination = "auditlog.queue")
    public void receiveQueue(CustomHttpTrace httpTrace) {

        try {
            repository.save(httpTrace);
        } catch (Exception e) {
            log.error("Audit Event Sink Error:> {}", e.getMessage());
        }

    }

    @JmsListener(destination = "transaction.queue")
    public void receiveTransactionQueue(Transaction transaction) {

        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error("Transaction Event Sink Error:> {}", e.getMessage());
        }

    }

}