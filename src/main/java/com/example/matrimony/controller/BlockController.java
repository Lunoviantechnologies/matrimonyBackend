package com.example.matrimony.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matrimony.service.BlockService;

@RestController
@RequestMapping("/api/block")
@CrossOrigin("*")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    /* =====================================================
       BLOCK USER
       ===================================================== */
    @PostMapping("/user/{blockerId}/{blockedId}")
    public ResponseEntity<Map<String, Object>> blockUser(
            @PathVariable Long blockerId,
            @PathVariable Long blockedId
    ) {
        boolean blocked = blockService.blockUser(blockerId, blockedId);

        Map<String, Object> response = new HashMap<>();
        response.put("blocked", blocked);
        response.put("blockerId", blockerId);
        response.put("blockedId", blockedId);

        return ResponseEntity.ok(response);
    }

    /* =====================================================
       UNBLOCK USER (ONLY BLOCKER CAN UNBLOCK)
       ===================================================== */
    @PostMapping("/unblock/{blockerId}/{blockedId}")
    public ResponseEntity<Map<String, Object>> unblockUser(
            @PathVariable Long blockerId,
            @PathVariable Long blockedId
    ) {
        blockService.unblockUser(blockerId, blockedId);

        Map<String, Object> response = new HashMap<>();
        response.put("blocked", false);
        response.put("blockerId", blockerId);
        response.put("blockedId", blockedId);

        return ResponseEntity.ok(response);
    }

    /* =====================================================
       BLOCK STATUS (FOR FRONTEND UI LOGIC)
       ===================================================== */
    @GetMapping("/status/{blockerId}/{blockedId}")
    public ResponseEntity<Map<String, Object>> getBlockStatus(
            @PathVariable Long blockerId,
            @PathVariable Long blockedId
    ) {
        boolean blocked = blockService.isBlocked(blockerId, blockedId);
        boolean iBlocked = blockService.iBlocked(blockerId, blockedId);

        Map<String, Object> response = new HashMap<>();
        response.put("blocked", blocked);
        response.put("iBlocked", iBlocked);
        //response.put("blockerId", blockerId);
        //response.put("blockedId", blockedId);

        return ResponseEntity.ok(response);
    }
}
