package com.lkhn.ecommerce.controllers;

import com.lkhn.ecommerce.services.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private OrderService orderService;

    @PostMapping("/api/webhook/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        // Verify the webhook signature
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Invalid Stripe webhook signature", e);
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        // Handle the event based on its type
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            logger.error("Failed to deserialize Stripe event object");
            return ResponseEntity.badRequest().body("Failed to deserialize event object");
        }

        // Handle different event types
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                logger.info("Payment succeeded: {}", paymentIntent.getId());

                // Update order status
                String orderId = paymentIntent.getMetadata().get("orderId");
                if (orderId != null) {
                    orderService.updateOrderStatus(orderId, "PAID");
                }
                break;

            case "payment_intent.payment_failed":
                paymentIntent = (PaymentIntent) stripeObject;
                logger.error("Payment failed: {}", paymentIntent.getId());

                // Update order status
                orderId = paymentIntent.getMetadata().get("orderId");
                if (orderId != null) {
                    orderService.updateOrderStatus(orderId, "PAYMENT_FAILED");
                }
                break;

            case "charge.refunded":
                logger.info("Charge refunded: {}", event.getId());
                break;

            default:
                logger.info("Unhandled event type: {}", event.getType());
        }

        return ResponseEntity.ok("Webhook processed successfully");
    }
}
