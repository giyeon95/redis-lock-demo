package com.example.redislockdemo;

import com.example.redislockdemo.domain.Ticket;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RedisLockRepository redisLockRepository;

    private static final Long key = 1L;

    public void insertTicket() {
        while (!redisLockRepository.lock(key)) {
            sleep(100);
        }

        try {
            if (ticketRepository.count() >= 50) {
                throw new RuntimeException("Ticket is not enough");
            }

            sleep(1);

            ticketRepository.save(Ticket.builder()
                .name(UUID.randomUUID().toString())
                .build());
        } finally {
            redisLockRepository.unlock(key);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public Long getTicketCount() {
        return ticketRepository.count();
    }

    public void deleteTickets() {
        ticketRepository.deleteAll();
    }
}
