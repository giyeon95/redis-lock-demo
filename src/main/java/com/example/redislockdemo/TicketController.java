package com.example.redislockdemo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/ticket-count")
    public Map<String, Object> getTicketCount() {
        Long ticketCount = ticketService.getTicketCount();

        return Map.of(
            "count", ticketCount
        );
    }

    @PostMapping("/tickets")
    public void insertTicket() {
        ticketService.insertTicketRedisson();
    }



    @DeleteMapping("/tickets")
    public void deleteTickets() {
        ticketService.deleteTickets();
    }
}
