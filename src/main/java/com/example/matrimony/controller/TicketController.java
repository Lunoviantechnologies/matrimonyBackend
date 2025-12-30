package com.example.matrimony.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.dto.TicketRequest;
import com.example.matrimony.entity.Ticket;
import com.example.matrimony.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin("*")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TicketRequest req) {
        Ticket saved = service.createTicket(req);
        return ResponseEntity.created(URI.create("/api/tickets/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/resolve")
    public ResponseEntity<?> markAsResolved(@PathVariable Long id) {

        service.resolveAndDelete(id);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Ticket resolved, email sent, and ticket deleted",
                        "ticketId", id
                )
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(service.findAll());
    }
}