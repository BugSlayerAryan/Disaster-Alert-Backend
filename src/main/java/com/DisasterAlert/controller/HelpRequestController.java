//package com.DisasterAlert.controller;
//
//import com.DisasterAlert.model.HelpRequest;
//import com.DisasterAlert.service.HelpRequestService;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/help")
//public class HelpRequestController {
//
//    private final HelpRequestService helpRequestService;
//
//    public HelpRequestController(HelpRequestService helpRequestService) {
//        this.helpRequestService = helpRequestService;
//    }
//
//    // USER: create help request
//    @PreAuthorize("hasRole('USER')")
//    @PostMapping("/request")
//    public HelpRequest addHelpRequest(@RequestBody HelpRequest helpRequest) {
//        return helpRequestService.addHelpRequest(helpRequest);
//    }
//
//    // ADMIN: get all requests
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/all")
//    public List<HelpRequest> getAllRequests() {
//        return helpRequestService.getAllRequests();
//    }
//
//    // ADMIN: update request status
//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/{id}/status")
//    public HelpRequest updateStatus(@PathVariable Long id,
//                                    @RequestParam String status) {
//        return helpRequestService.updateStatus(id, status);
//    }
//
//    // USER: get own requests
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/my/{userId}")
//    public List<HelpRequest> getMyRequests(@PathVariable Long userId) {
//        return helpRequestService.getRequestsByUser(userId);
//    }
//}









//package com.DisasterAlert.controller;
//
//import com.DisasterAlert.model.HelpRequest;
//import com.DisasterAlert.service.HelpRequestService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//
//import java.util.Base64;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/help")
//public class HelpRequestController {
//
//    private final HelpRequestService helpRequestService;
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final String CLERK_API_KEY = System.getenv("CLERK_API_KEY"); // Clerk API key
//
//    public HelpRequestController(HelpRequestService helpRequestService) {
//        this.helpRequestService = helpRequestService;
//    }
//
//    private String extractUserIdFromJwt(String token) {
//        String payload = new String(Base64.getDecoder().decode(token.split("\\.")[1]));
//        return payload.split("\"sub\":\"")[1].split("\"")[0];
//    }
//
//    private boolean isAdmin(String authHeader) {
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            String userId = extractUserIdFromJwt(token);
//
//            String url = "https://api.clerk.dev/v1/users/" + userId;
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + CLERK_API_KEY);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//
//            Map<String, Object> publicMetadata = (Map<String, Object>) response.getBody().get("public_metadata");
//            if (publicMetadata == null) return false;
//
//            Object rolesObj = publicMetadata.get("roles");
//            return rolesObj != null && rolesObj.toString().contains("ADMIN");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private boolean isUser(String authHeader) {
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            String userId = extractUserIdFromJwt(token);
//
//            String url = "https://api.clerk.dev/v1/users/" + userId;
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + CLERK_API_KEY);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//
//            Map<String, Object> publicMetadata = (Map<String, Object>) response.getBody().get("public_metadata");
//            if (publicMetadata == null) return false;
//
//            Object rolesObj = publicMetadata.get("roles");
//            return rolesObj != null && rolesObj.toString().contains("USER");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // USER: create help request
//    @PostMapping("/request")
//    public ResponseEntity<?> addHelpRequest(@RequestHeader("Authorization") String authHeader,
//                                            @RequestBody HelpRequest helpRequest) {
//        if (!isUser(authHeader)) return ResponseEntity.status(403).body("Forbidden: User only");
//        return ResponseEntity.ok(helpRequestService.addHelpRequest(helpRequest));
//    }
//
//    // ADMIN: get all requests
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllRequests(@RequestHeader("Authorization") String authHeader) {
//        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body("Forbidden: Admin only");
//        return ResponseEntity.ok(helpRequestService.getAllRequests());
//    }
//
//    // ADMIN: update request status
//    @PutMapping("/{id}/status")
//    public ResponseEntity<?> updateStatus(@RequestHeader("Authorization") String authHeader,
//                                          @PathVariable Long id,
//                                          @RequestParam String status) {
//        if (!isAdmin(authHeader)) return ResponseEntity.status(403).body("Forbidden: Admin only");
//        return ResponseEntity.ok(helpRequestService.updateStatus(id, status));
//    }
//
//    // USER: get own requests
//    @GetMapping("/my/{userId}")
//    public ResponseEntity<?> getMyRequests(@RequestHeader("Authorization") String authHeader,
//                                           @PathVariable Long userId) {
//        if (!isUser(authHeader)) return ResponseEntity.status(403).body("Forbidden: User only");
//        return ResponseEntity.ok(helpRequestService.getRequestsByUser(userId));
//    }
//}
//


//package com.DisasterAlert.controller;
//
//import com.DisasterAlert.model.HelpRequest;
//import com.DisasterAlert.service.HelpRequestService;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/help")
//public class HelpRequestController {
//
//    private final HelpRequestService helpRequestService;
//
//    public HelpRequestController(HelpRequestService helpRequestService) {
//        this.helpRequestService = helpRequestService;
//    }
//
//    // 👤 USER: create help request
//    @PostMapping("/request")
//    public ResponseEntity<?> addHelpRequest(
//            @RequestBody HelpRequest helpRequest,
//            HttpServletRequest request
//    ) {
//        String clerkUserId = (String) request.getAttribute("clerkUserId");
//
//        if (clerkUserId == null) {
//            return ResponseEntity.status(401).body("Unauthorized");
//        }
//
//        return ResponseEntity.ok(
//                helpRequestService.addHelpRequest(helpRequest, clerkUserId)
//        );
//    }
//
//    // 🔐 ADMIN
//    @GetMapping("/all")
//    public ResponseEntity<List<HelpRequest>> getAllRequests() {
//        return ResponseEntity.ok(helpRequestService.getAllRequests());
//    }
//
//    // 🔄 ADMIN / USER update
//    @PutMapping("/{id}/status")
//    public ResponseEntity<?> updateStatus(
//            @PathVariable Long id,
//            @RequestParam String status,
//            Authentication authentication
//    ) {
//        String role = authentication.getAuthorities().iterator().next().getAuthority()
//                .replace("ROLE_", "");
//
//        return ResponseEntity.ok(
//                helpRequestService.updateStatus(id, status, role)
//        );
//    }
//
//    // 👤 USER: own requests
//    @GetMapping("/my")
//    public ResponseEntity<?> getMyRequests(HttpServletRequest request) {
//
//        String clerkUserId = (String) request.getAttribute("clerkUserId");
//
//        return ResponseEntity.ok(
//                helpRequestService.getRequestsByUser(clerkUserId)
//        );
//    }
//}


package com.DisasterAlert.controller;

import com.DisasterAlert.model.HelpRequest;
import com.DisasterAlert.service.HelpRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help")
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    public HelpRequestController(HelpRequestService helpRequestService) {
        this.helpRequestService = helpRequestService;
    }

    // 👤 USER: create help request
    @PostMapping("/request")
    public ResponseEntity<?> addHelpRequest(
            @RequestBody HelpRequest helpRequest,
            HttpServletRequest request
    ) {
        String clerkUserId = (String) request.getAttribute("clerkUserId");
        if (clerkUserId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(
                helpRequestService.addHelpRequest(helpRequest, clerkUserId)
        );
    }

    // 🔐 ADMIN: get all requests
    @GetMapping("/all")
    public ResponseEntity<List<HelpRequest>> getAllRequests() {
        return ResponseEntity.ok(helpRequestService.getAllRequests());
    }

    // 🔄 ADMIN / USER: update status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication
    ) {
        String role = authentication.getAuthorities().iterator().next().getAuthority()
                .replace("ROLE_", "");

        return ResponseEntity.ok(
                helpRequestService.updateStatus(id, status, role)
        );
    }

    // 👤 USER: get own requests
    @GetMapping("/my")
    public ResponseEntity<?> getMyRequests(HttpServletRequest request) {
        String clerkUserId = (String) request.getAttribute("clerkUserId");
        if (clerkUserId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(
                helpRequestService.getRequestsByUser(clerkUserId)
        );
    }
}
