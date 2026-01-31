//package com.DisasterAlert.service;
//
//import com.DisasterAlert.model.HelpRequest;
//import com.DisasterAlert.model.User;
//import com.DisasterAlert.model.Disaster;
//import com.DisasterAlert.repository.HelpRequestRepository;
//import com.DisasterAlert.repository.UserRepository;
//import com.DisasterAlert.repository.DisasterRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class HelpRequestService {
//
//    private final HelpRequestRepository helpRequestRepository;
//    private final UserRepository userRepository;
//    private final DisasterRepository disasterRepository;
//    private final EmailService emailService;
//
//    public HelpRequestService(
//            HelpRequestRepository helpRequestRepository,
//            UserRepository userRepository,
//            DisasterRepository disasterRepository,
//            EmailService emailService
//    ) {
//        this.helpRequestRepository = helpRequestRepository;
//        this.userRepository = userRepository;
//        this.disasterRepository = disasterRepository;
//        this.emailService = emailService;
//    }
//
//    // ✅ USER creates help request using Clerk ID
//    public HelpRequest addHelpRequest(HelpRequest helpRequest, String clerkUserId) {
//
//        // 1️⃣ Find user using Clerk ID
//        User user = userRepository.findByClerkUserId(clerkUserId)
//                .orElseThrow(() -> new RuntimeException("User not found for Clerk ID: " + clerkUserId));
//
//        // 2️⃣ Find disaster
//        Disaster disaster = disasterRepository.findById(
//                helpRequest.getDisaster().getDisasterId()
//        ).orElseThrow(() -> new RuntimeException("Disaster not found"));
//
//        // 3️⃣ Set relations
//        helpRequest.setUser(user);
//        helpRequest.setDisaster(disaster);
//        helpRequest.setStatus("RAISED");
//
//        return helpRequestRepository.save(helpRequest);
//    }
//
//
//    // ✅ ADMIN / USER workflow status update
//    public HelpRequest updateStatus(Long id, String newStatus, String role) {
//
//        HelpRequest hr = helpRequestRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("HelpRequest not found"));
//
//        String oldStatus = hr.getStatus();
//
//        if ("ADMIN".equals(role)) {
//
//            if ("RAISED".equals(oldStatus) && "IN_PROGRESS".equals(newStatus)) {
//                hr.setStatus("IN_PROGRESS");
//
//            } else if ("USER_CONFIRMED".equals(oldStatus) && "RESOLVED".equals(newStatus)) {
//                hr.setStatus("RESOLVED");
//
//            } else {
//                throw new RuntimeException("Invalid ADMIN status transition");
//            }
//
//        } else if ("USER".equals(role)) {
//
//            if ("IN_PROGRESS".equals(oldStatus) && "USER_CONFIRMED".equals(newStatus)) {
//                hr.setStatus("USER_CONFIRMED");
//            } else {
//                throw new RuntimeException("Invalid USER status transition");
//            }
//
//        } else {
//            throw new RuntimeException("Invalid role");
//        }
//
//        HelpRequest saved = helpRequestRepository.save(hr);
//
//        // 📧 Professional HTML Email Notification
//        // ===================== PROFESSIONAL HTML EMAIL =====================
//        String email = saved.getUser().getEmail();
//        String disasterName = saved.getDisaster().getType() + " at " + saved.getDisaster().getLocation();
//        String userName = saved.getUser().getName();
//
//        String htmlMessage = "";
//
//        switch (saved.getStatus()) {
//
//            case "IN_PROGRESS":
//                htmlMessage =
//                        "<div style='font-family:Arial,sans-serif; padding:20px; background:#f1f5f9'>" +
//                                "<div style='max-width:600px; margin:auto; background:#ffffff; padding:30px; border-radius:10px; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>" +
//                                "<h2 style='color:#0b3d91; margin-bottom:10px;'>Help Request Update</h2>" +
//                                "<p>Dear <strong>" + userName + "</strong>,</p>" +
//                                "<p>Your help request for <strong>\"" + disasterName + "\"</strong> is now <span style='color:#ff9900; font-weight:bold;'>IN PROGRESS</span>.</p>" +
//                                "<p>Our disaster response team is actively working to assist you. You will receive updates as soon as there is progress.</p>" +
//                                "<hr style='margin:20px 0; border:none; border-top:1px solid #e0e0e0;'/>" +
//                                "<p style='font-size:12px; color:#6b7280;'>This is an official notification from Disaster Alert Team. Please do not reply to this email.</p>" +
//                                "</div>" +
//                                "</div>";
//                break;
//
//            case "USER_CONFIRMED":
//                htmlMessage =
//                        "<div style='font-family:Arial,sans-serif; padding:20px; background:#f1f5f9'>" +
//                                "<div style='max-width:600px; margin:auto; background:#ffffff; padding:30px; border-radius:10px; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>" +
//                                "<h2 style='color:#0b3d91; margin-bottom:10px;'>Action Required: Confirm Help</h2>" +
//                                "<p>Dear <strong>" + userName + "</strong>,</p>" +
//                                "<p>Your help request for <strong>\"" + disasterName + "\"</strong> has been addressed by our team.</p>" +
//                                "<p>Please confirm if the assistance provided has resolved your issue. Once confirmed, your request will be marked <span style='color:#28a745; font-weight:bold;'>RESOLVED</span>.</p>" +
//                                "<p style='margin-top:20px; text-align:center;'>" +
//                                "<a href='https://yourfrontend.com/confirm/" + saved.getRequestId() + "' " +
//                                "style='display:inline-block; padding:12px 25px; background:#0b3d91; color:#ffffff; text-decoration:none; border-radius:6px; font-weight:bold;'>Confirm Now</a>" +
//                                "</p>" +
//                                "<hr style='margin:20px 0; border:none; border-top:1px solid #e0e0e0;'/>" +
//                                "<p style='font-size:12px; color:#6b7280;'>This is an official notification from Disaster Alert Team. Please do not reply to this email.</p>" +
//                                "</div>" +
//                                "</div>";
//                break;
//
//            case "RESOLVED":
//                htmlMessage =
//                        "<div style='font-family:Arial,sans-serif; padding:20px; background:#f1f5f9'>" +
//                                "<div style='max-width:600px; margin:auto; background:#ffffff; padding:30px; border-radius:10px; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>" +
//                                "<h2 style='color:#0b3d91; margin-bottom:10px;'>Help Request Resolved</h2>" +
//                                "<p>Dear <strong>" + userName + "</strong>,</p>" +
//                                "<p>Your help request for <strong>\"" + disasterName + "\"</strong> has been successfully <span style='color:#28a745; font-weight:bold;'>RESOLVED</span>.</p>" +
//                                "<p>We hope the assistance provided met your needs. Thank you for using Disaster Alert Team services.</p>" +
//                                "<hr style='margin:20px 0; border:none; border-top:1px solid #e0e0e0;'/>" +
//                                "<p style='font-size:12px; color:#6b7280;'>This is an official notification from Disaster Alert Team. Please do not reply to this email.</p>" +
//                                "</div>" +
//                                "</div>";
//                break;
//
//            default:
//                htmlMessage =
//                        "<div style='font-family:Arial,sans-serif; padding:20px; background:#f1f5f9'>" +
//                                "<div style='max-width:600px; margin:auto; background:#ffffff; padding:30px; border-radius:10px; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>" +
//                                "<h2 style='color:#0b3d91; margin-bottom:10px;'>Help Request Update</h2>" +
//                                "<p>Dear <strong>" + userName + "</strong>,</p>" +
//                                "<p>The status of your help request has been updated to: <strong>" + saved.getStatus() + "</strong>.</p>" +
//                                "<hr style='margin:20px 0; border:none; border-top:1px solid #e0e0e0;'/>" +
//                                "<p style='font-size:12px; color:#6b7280;'>This is an official notification from Disaster Alert Team. Please do not reply to this email.</p>" +
//                                "</div>" +
//                                "</div>";
//                break;
//        }
//
//
//        // Send professional HTML email
//        emailService.sendHtmlEmail(email, "Help Request Status Update", htmlMessage);
//
//
//        return saved;
//    }
//
//
//    // ✅ ADMIN: Get all help requests
//    public List<HelpRequest> getAllRequests() {
//        return helpRequestRepository.findAll();
//    }
//
//    // ✅ USER: Get help requests by Clerk ID
//    public List<HelpRequest> getRequestsByUser(String clerkUserId) {
//        return helpRequestRepository.findAllByUser_ClerkUserId(clerkUserId);
//    }
//}



package com.DisasterAlert.service;

import com.DisasterAlert.model.HelpRequest;
import com.DisasterAlert.model.User;
import com.DisasterAlert.model.Disaster;
import com.DisasterAlert.repository.HelpRequestRepository;
import com.DisasterAlert.repository.UserRepository;
import com.DisasterAlert.repository.DisasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpRequestService {

    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;
    private final DisasterRepository disasterRepository;
    private final EmailService emailService;

    private final String GOVT_EMAIL = "ariyanbehera7@gmail.com"; // Official email

    public HelpRequestService(
            HelpRequestRepository helpRequestRepository,
            UserRepository userRepository,
            DisasterRepository disasterRepository,
            EmailService emailService
    ) {
        this.helpRequestRepository = helpRequestRepository;
        this.userRepository = userRepository;
        this.disasterRepository = disasterRepository;
        this.emailService = emailService;
    }

    // ================= USER CREATES HELP REQUEST =================
    public HelpRequest addHelpRequest(HelpRequest helpRequest, String clerkUserId) {

        // 1️⃣ Find user
        User user = userRepository.findByClerkUserId(clerkUserId)
                .orElseThrow(() -> new RuntimeException("User not found for Clerk ID: " + clerkUserId));

        // 2️⃣ Find disaster
        Disaster disaster = disasterRepository.findById(
                helpRequest.getDisaster().getDisasterId()
        ).orElseThrow(() -> new RuntimeException("Disaster not found"));

        // 3️⃣ Set relations
        helpRequest.setUser(user);
        helpRequest.setDisaster(disaster);
        helpRequest.setStatus("RAISED");

        HelpRequest saved = helpRequestRepository.save(helpRequest);

        // 4️⃣ Send GOVT official email
        String html = generateGovtEmailHtml(saved);
        emailService.sendHtmlEmail(GOVT_EMAIL, "OFFICIAL ALERT: New Disaster Help Request Received", html);

        return saved;
    }

    // ================= ADMIN / USER STATUS UPDATE =================
    public HelpRequest updateStatus(Long id, String newStatus, String role) {

        HelpRequest hr = helpRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found"));

        String oldStatus = hr.getStatus();

        // Validate transition
        if ("ADMIN".equals(role)) {
            if ("RAISED".equals(oldStatus) && "IN_PROGRESS".equals(newStatus)) {
                hr.setStatus("IN_PROGRESS");
            } else if ("USER_CONFIRMED".equals(oldStatus) && "RESOLVED".equals(newStatus)) {
                hr.setStatus("RESOLVED");
            } else {
                throw new RuntimeException("Invalid ADMIN status transition");
            }
        } else if ("USER".equals(role)) {
            if ("IN_PROGRESS".equals(oldStatus) && "USER_CONFIRMED".equals(newStatus)) {
                hr.setStatus("USER_CONFIRMED");
            } else {
                throw new RuntimeException("Invalid USER status transition");
            }
        } else {
            throw new RuntimeException("Invalid role");
        }

        HelpRequest saved = helpRequestRepository.save(hr);

        // Send professional user email
        String htmlMessage = generateUserEmailHtml(saved);
        emailService.sendHtmlEmail(saved.getUser().getEmail(), "Help Request Status Update", htmlMessage);

        return saved;
    }

    // ================= GET ALL REQUESTS (ADMIN) =================
    public List<HelpRequest> getAllRequests() {
        return helpRequestRepository.findAll();
    }

    // ================= GET USER REQUESTS =================
    public List<HelpRequest> getRequestsByUser(String clerkUserId) {
        return helpRequestRepository.findAllByUser_ClerkUserId(clerkUserId);
    }

    // ==================== HELPER METHODS ====================

    // GOVT Email HTML
    private String generateGovtEmailHtml(HelpRequest hr) {
        User user = hr.getUser();
        Disaster disaster = hr.getDisaster();

        return "<div style='font-family:Arial,sans-serif; padding:20px; background:#f4f6f8'>" +
                "<div style='max-width:650px; margin:auto; background:#ffffff; padding:30px; border:1px solid #e5e7eb'>" +
                "<h2 style='text-align:center; color:#1f2937;'>Official Disaster Help Request Notification</h2>" +
                "<p>Respected Authority,</p>" +
                "<p>A new <strong>disaster help request</strong> has been submitted through the " +
                "<strong>Disaster Alert System</strong>. Details are provided below for immediate action:</p>" +
                "<table style='width:100%; border-collapse:collapse; margin-top:15px;'>" +
                "<tr><td style='padding:8px; border:1px solid #d1d5db;'><strong>Citizen Name</strong></td>" +
                "<td style='padding:8px; border:1px solid #d1d5db;'>" + user.getName() + "</td></tr>" +
                "<tr><td style='padding:8px; border:1px solid #d1d5db;'><strong>Citizen Email</strong></td>" +
                "<td style='padding:8px; border:1px solid #d1d5db;'>" + user.getEmail() + "</td></tr>" +
                "<tr><td style='padding:8px; border:1px solid #d1d5db;'><strong>Disaster Type</strong></td>" +
                "<td style='padding:8px; border:1px solid #d1d5db;'>" + disaster.getType() + "</td></tr>" +
                "<tr><td style='padding:8px; border:1px solid #d1d5db;'><strong>Location</strong></td>" +
                "<td style='padding:8px; border:1px solid #d1d5db;'>" + disaster.getLocation() + "</td></tr>" +
                "<tr><td style='padding:8px; border:1px solid #d1d5db;'><strong>Message</strong></td>" +
                "<td style='padding:8px; border:1px solid #d1d5db;'>" + hr.getMessage() + "</td></tr>" +
                "</table>" +
                "<div style='margin-top:30px; text-align:center;'>" +
                "<a href='https://disaster-alert-omega.vercel.app/"  +
                "style='display:inline-block; padding:14px 36px; background:#0b3d91; color:#ffffff; " +
                "text-decoration:none; border-radius:8px; font-size:15px; font-weight:600;'>" +
                "Confirm Assistance Received" +
                "</a>" +
                "</div>" +
                "<p style='margin-top:20px;'>Kindly review the request and initiate necessary action at the earliest.</p>" +
                "<p>Regards,<br/><strong>Disaster Alert System</strong></p>" +
                "<hr/><p style='font-size:12px; color:#6b7280; text-align:center;'>This is a system-generated email. Do not reply.</p>" +
                "</div></div>";
    }

    // User Email HTML
    private String generateUserEmailHtml(HelpRequest hr) {
        String userName = hr.getUser().getName();
        String disasterName = hr.getDisaster().getType() + " at " + hr.getDisaster().getLocation();
        String requestId = hr.getRequestId().toString();
        String status = hr.getStatus();

        switch (status) {
            case "IN_PROGRESS":
                return "<div style='font-family:Segoe UI, Arial, sans-serif; padding:24px; background:#f3f6fb;'>" +
                        "<div style='max-width:620px; margin:auto; background:#ffffff; padding:32px; " +
                        "border-radius:12px; box-shadow:0 6px 18px rgba(0,0,0,0.08);'>" +

                        "<h2 style='color:#0b3d91; margin-top:0; margin-bottom:8px;'>Disaster Assistance Update</h2>" +

                        "<p style='color:#374151; font-size:15px;'>Dear <strong>" + userName + "</strong>,</p>" +

                        "<p style='color:#374151; font-size:15px; line-height:1.6;'>" +
                        "This is to inform you that your help request related to <strong>\"" + disasterName + "\"</strong> " +
                        "is currently being handled by our response team.</p>" +

                        "<div style='margin:18px 0; padding:14px; background:#fff7ed; border-left:5px solid #f59e0b;'>" +
                        "<p style='margin:0; font-size:14px; color:#92400e;'>" +
                        "<strong>Status:</strong> IN PROGRESS — Our team has been deployed and is actively working on your request." +
                        "</p>" +
                        "</div>" +

                        "<p style='color:#374151; font-size:15px; line-height:1.6;'>" +
                        "We kindly request you to confirm whether the assistance has successfully reached you. " +
                        "Your confirmation will help us close the request and maintain accurate emergency records.</p>" +

                        "<div style='margin-top:30px; text-align:center;'>" +
                        "<a href='https://disaster-alert-omega.vercel.app/" + requestId + "' " +
                        "style='display:inline-block; padding:14px 36px; background:#0b3d91; color:#ffffff; " +
                        "text-decoration:none; border-radius:8px; font-size:15px; font-weight:600;'>" +
                        "Confirm Assistance Received" +
                        "</a>" +
                        "</div>" +

                        "<p style='margin-top:24px; font-size:13px; color:#6b7280; line-height:1.5;'>" +
                        "If the assistance has not yet reached you, no action is required at this time. " +
                        "Our team will continue follow-up efforts.</p>" +

                        "<hr style='margin:28px 0; border:none; border-top:1px solid #e5e7eb;'/>" +

                        "<p style='font-size:12px; color:#6b7280; text-align:center;'>" +
                        "This is an official system-generated notification from the Disaster Alert Authority.<br/>" +
                        "Please do not reply to this email." +
                        "</p>" +

                        "</div>" +
                        "</div>";

            case "USER_CONFIRMED":
                return "<div style='font-family:Segoe UI, Arial, sans-serif; padding:24px; background:#f3f6fb;'>" +
                            "<div style='max-width:620px; margin:auto; background:#ffffff; padding:34px; " +
                            "border-radius:12px; box-shadow:0 6px 18px rgba(0,0,0,0.08);'>" +

                            "<h2 style='color:#0b3d91; margin-top:0;'>Confirmation Required: Assistance Delivered</h2>" +

                            "<p style='font-size:15px; color:#374151;'>Dear <strong>" + userName + "</strong>,</p>" +

                            "<p style='font-size:15px; color:#374151; line-height:1.6;'>" +
                            "We hope you are safe. Our records indicate that assistance related to " +
                            "<strong>\"" + disasterName + "\"</strong> has been successfully provided by the response team.</p>" +

                            "<div style='margin:20px 0; padding:16px; background:#ecfdf5; border-left:5px solid #22c55e;'>" +
                            "<p style='margin:0; font-size:14px; color:#065f46;'>" +
                            "<strong>Status Update:</strong> Assistance Delivered — Case ready for closure upon your confirmation." +
                            "</p>" +
                            "</div>" +

                            "<p style='font-size:15px; color:#374151; line-height:1.6;'>" +
                            "To ensure accurate documentation and proper closure of your request, " +
                            "we kindly ask you to confirm whether the assistance has fully resolved your issue.</p>" +

                            "<div style='margin-top:30px; text-align:center;'>" +
                            "<a href='https://yourfrontend.com/confirm/" + requestId + "' " +
                            "style='display:inline-block; padding:14px 38px; background:#0b3d91; color:#ffffff; " +
                            "text-decoration:none; border-radius:8px; font-size:15px; font-weight:600;'>" +
                            "Confirm & Close Request" +
                            "</a>" +
                            "</div>" +

                            "<p style='margin-top:24px; font-size:13px; color:#6b7280; line-height:1.5;'>" +
                            "If the issue has not been fully resolved, please do not confirm at this time. " +
                            "Our team will continue to provide necessary support.</p>" +

                            "<hr style='margin:30px 0; border:none; border-top:1px solid #e5e7eb;'/>" +

                            "<p style='font-size:12px; color:#6b7280; text-align:center;'>" +
                            "This is an official system-generated message from the Disaster Alert Authority.<br/>" +
                            "Please do not reply to this email." +
                            "</p>" +

                            "</div>" +
                            "</div>";


            case "RESOLVED":
                return "<div style='font-family:Segoe UI, Arial, sans-serif; padding:24px; background:#f3f6fb;'>" +
                        "<div style='max-width:620px; margin:auto; background:#ffffff; padding:34px; " +
                        "border-radius:12px; box-shadow:0 6px 18px rgba(0,0,0,0.08);'>" +

                        "<h2 style='color:#0b3d91; margin-top:0;'>Help Request Successfully Resolved</h2>" +

                        "<p style='font-size:15px; color:#374151;'>Dear <strong>" + userName + "</strong>,</p>" +

                        "<p style='font-size:15px; color:#374151; line-height:1.6;'>" +
                        "We are pleased to inform you that your help request related to " +
                        "<strong>\"" + disasterName + "\"</strong> has been successfully " +
                        "<span style='color:#16a34a; font-weight:600;'>RESOLVED</span>.</p>" +

                        "<div style='margin:20px 0; padding:16px; background:#ecfdf5; border-left:5px solid #22c55e;'>" +
                        "<p style='margin:0; font-size:14px; color:#065f46;'>" +
                        "<strong>Status:</strong> Case Closed — Assistance has been completed and confirmed." +
                        "</p>" +
                        "</div>" +

                        "<p style='font-size:15px; color:#374151; line-height:1.6;'>" +
                        "We sincerely hope that the support provided addressed your situation effectively. " +
                        "Your safety and well-being remain our highest priority.</p>" +

                        "<p style='font-size:15px; color:#374151; line-height:1.6;'>" +
                        "Should you require further assistance in the future, please do not hesitate to raise a new request. " +
                        "Our response teams are always ready to help.</p>" +

                        "<hr style='margin:30px 0; border:none; border-top:1px solid #e5e7eb;'/>" +

                        "<p style='font-size:12px; color:#6b7280; text-align:center;'>" +
                        "This is an official system-generated notification from the Disaster Alert Authority.<br/>" +
                        "Please do not reply to this email." +
                        "</p>" +

                        "</div>" +
                        "</div>";


            default:
                return "<div style='font-family:Arial,sans-serif; padding:20px; background:#f1f5f9'>" +
                        "<div style='max-width:600px; margin:auto; background:#ffffff; padding:30px; border-radius:10px; box-shadow:0 4px 10px rgba(0,0,0,0.1);'>" +
                        "<h2 style='color:#0b3d91; margin-bottom:10px;'>Help Request Update</h2>" +
                        "<p>Dear <strong>" + userName + "</strong>,</p>" +
                        "<p>The status of your help request has been updated to: <strong>" + status + "</strong>.</p>" +
                        "<hr style='margin:20px 0; border:none; border-top:1px solid #e0e0e0;'/>" +
                        "<p style='font-size:12px; color:#6b7280;'>This is an official notification from Disaster Alert Team. Please do not reply to this email.</p>" +
                        "</div></div>";
        }
    }
}
