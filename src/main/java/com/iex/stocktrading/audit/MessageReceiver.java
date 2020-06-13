package com.iex.stocktrading.audit;

import com.iex.stocktrading.model.CustomHttpTrace;
import com.iex.stocktrading.repository.TraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
class MessageReceiver {

    @Autowired
    private TraceRepository repository;

    @JmsListener(destination = "auditlog.queue")
    public void receiveQueue(CustomHttpTrace httpTrace) {

        try {
            repository.save(httpTrace);
        } catch (Exception e) {
            System.out.println("Error:> " + e.getMessage());
        }

    }

}