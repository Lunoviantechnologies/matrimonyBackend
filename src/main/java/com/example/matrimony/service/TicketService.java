package com.example.matrimony.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matrimony.dto.NotificationDto;
import com.example.matrimony.dto.TicketRequest;
import com.example.matrimony.entity.Ticket;
import com.example.matrimony.repository.TicketRepository;

import jakarta.validation.Valid;

@Service
public class TicketService {
	
	@Autowired
	private Notificationadminservice adminNotificationService;
	
	@Autowired
	private NotificationService notiservice;

    private final TicketRepository repo;
    private final EmailService emailService;

    public TicketService(TicketRepository repo, EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;
    }

    @Transactional
    public Ticket createTicket(@Valid TicketRequest req) {

        Ticket t = new Ticket();
        t.setIssueCategory(req.getIssueCategory());
        t.setName(req.getName());
        t.setEmail(req.getEmail());
        t.setPhoneNumber(req.getPhoneNumber());
        t.setMemberId(req.getMemberId());
        t.setDescription(req.getDescription());
        t.setDocument(req.getDocument());

        Ticket savedTicket = repo.save(t);

        // 🔔 SEND NOTIFICATION (LIKE CHAT)
        adminNotificationService.sendTicketNotification(
                savedTicket.getMemberId(),   
                "New Support Ticket",
                "Ticket raised by " + savedTicket.getName()
                        + " (" + savedTicket.getIssueCategory() + ")",
                savedTicket.getId()
        );

        return savedTicket;
    }



    public List<Ticket> findAll() {
        return repo.findAll();
    }

    public Optional<Ticket> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * Resolve ticket → send email → delete from DB
     */
    
    @Transactional
    public void resolveAndDelete(Long ticketId) {

        Ticket ticket = repo.findById(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found with id: " + ticketId)
                );

        // 1️⃣ Send email BEFORE delete
        emailService.sendTicketResolvedEmail(
                ticket.getEmail(),
                ticket.getName(),
                ticket.getId()
        );

        // 2️⃣ 🔔 Send Notification (same pattern as friend request)
        NotificationDto notif = new NotificationDto();     
        notif.setType("TICKET_RESOLVED");
        notif.setMessage(
                "Your ticket #" + ticket.getId() + " has been resolved successfully."
        );
        notif.setData(
                Map.of(
                        "ticketId", ticket.getId(),
                        "status", "RESOLVED"
                )
        );

        notiservice.sendToUserAndSave(notif);

        // 3️⃣ Delete ticket
        repo.delete(ticket);
    }

    
//    @Transactional
//    public void resolveAndDelete(Long ticketId) {
//
//        Ticket ticket = repo.findById(ticketId)
//                .orElseThrow(() ->
//                        new RuntimeException("Ticket not found with id: " + ticketId)
//                );
//
//        // 1️⃣ Send email BEFORE delete
//        emailService.sendTicketResolvedEmail(
//                ticket.getEmail(),
//                ticket.getName(),
//                ticket.getId()
//        );
//
//        // 2️⃣ Delete ticket
//        repo.delete(ticket);
//    }
}