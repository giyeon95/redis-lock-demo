package com.example.redislockdemo;

import com.example.redislockdemo.domain.Ticket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RedisLockRepository redisLockRepository;

    private final RedissonClient redissonClient;

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

    public void insertTicketRedisson() {
        RLock lock = redissonClient.getLock("lock");

        try {
            if (!lock.tryLock(1, 3, TimeUnit.SECONDS)) {
                throw new RuntimeException("Lock 획득 실패");
            }

            if (ticketRepository.count() >= 50) {
                throw new RuntimeException("Ticket is not enough");
            }

            sleep(1);

            ticketRepository.save(Ticket.builder()
                .name(UUID.randomUUID().toString())
                .build());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
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
