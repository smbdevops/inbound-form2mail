package com.example.inbound.controller;

import com.example.inbound.dto.AppointmentRequest;
import com.example.inbound.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class LeadController {

    public static final List<String> INTERNAL_RECIPIENT_LIST = List.of("you@example.com");
    private static final String TEMPLATE_FORM_SUBMITTED_INTERNAL_TEMPLATE = "mail/html/form-submitted-internal";
    private final EmailService emailService;

    @GetMapping
    public final String requestAppointment(final AppointmentRequest appointmentRequest) {
        return "form";
    }

    @PostMapping
    public final String processAppointmentRequest(@Valid final AppointmentRequest appointmentRequest, final BindingResult bindingResult) throws MessagingException {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        final Context context = new Context();
        context.setVariable("email", appointmentRequest.getEmail());
        context.setVariable("name", appointmentRequest.getName());
        context.setVariable("phone", appointmentRequest.getPhone());

        this.emailService.sendTemplateEmail(
                INTERNAL_RECIPIENT_LIST,
                "New Lead Submitted from " + appointmentRequest.getName(),
                TEMPLATE_FORM_SUBMITTED_INTERNAL_TEMPLATE,
                context
        );

        return "redirect:/success";
    }

    @GetMapping("/success")
    public final String success() {
        return "success";
    }
}
