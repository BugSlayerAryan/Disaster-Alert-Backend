//package com.DisasterAlert.controller;
//
//import com.DisasterAlert.model.Disaster;
//import com.DisasterAlert.service.DisasterService;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/disasters")
//public class DisasterController {
//
//    private final DisasterService disasterService;
//
//    public DisasterController(DisasterService disasterService) {
//        this.disasterService = disasterService;
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public Disaster addDisaster(@RequestBody Disaster disaster) {
//        return disasterService.addDisaster(disaster);
//    }
//
//    @GetMapping("/active")
//    public List<Disaster> getActiveDisasters() {
//        return disasterService.getActiveDisasters();
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/{id}/status")
//    public Disaster updateStatus(@PathVariable Long id,
//                                 @RequestParam String status) {
//        return disasterService.updateDisasterStatus(id, status);
//    }
//}



package com.DisasterAlert.controller;

import com.DisasterAlert.model.Disaster;
import com.DisasterAlert.service.DisasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disasters")
public class DisasterController {

    private final DisasterService disasterService;

    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    // ADMIN only (handled by SecurityConfig)
    @PostMapping
    public ResponseEntity<?> addDisaster(@RequestBody Disaster disaster) {
        return ResponseEntity.ok(disasterService.addDisaster(disaster));
    }

    // PUBLIC
    @GetMapping("/active")
    public List<Disaster> getActiveDisasters() {
        return disasterService.getActiveDisasters();
    }

    // ADMIN only
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestParam String status) {
        return ResponseEntity.ok(disasterService.updateDisasterStatus(id, status));
    }
}
