# Bodega Cat Fundraiser

Art Deco-inspired platform for supporting NYC's beloved bodega cats. Nominate, vote for, and donate to your favorites through Stripe payments. Features include QR codes for in-store voting and a cat sponsorship lottery. Built with React and Spring Boot, blending modern functionality with classic aesthetics.

## Tech Stack

- **Frontend**: React, CSS (no frameworks)
- **Backend**: Java Spring Boot
- **Database**: PostgreSQL
- **Deployment**: Vercel (frontend), Render (backend)

## Setup Instructions

### Prerequisites
- Java 17
- Node.js 14+
- PostgreSQL

### Backend Setup
1. Navigate to the server directory: `cd server`
2. Build the project: `mvn clean install`
3. Run the application: `mvn spring-boot:run`

### Frontend Setup
1. Install dependencies: `npm install`
2. Start the development server: `npm start`

## Features (Planned)
- User authentication
- Cat nomination system
- Donation processing with Stripe
- QR code generation for in-store voting
- Lottery system for cat sponsorships

## Project Timeline
- Week 1: ERD + wireframes, scaffold React + Spring Boot app, deploy a blank page
- Week 2: Implement authentication system (signup/login + navbar)
- Week 3: User profile page, create + display cat listings
- Week 4: Edit/delete listings, add donation functionality
- Week 5: Implement payment simulation, polish, and present

# Payment Processing Integration

This document outlines the payment processing integration for the LKHN E-commerce platform, focusing on the Stripe integration for both regular payments and EBT/benefits payments.

## Overview

The payment system supports:
- Regular credit/debit card payments
- EBT/SNAP/WIC benefits payments
- Mixed payment methods (split payments)
- Tokenization of payment methods for future use
- Benefits eligibility verification

## Key Components

### StripeService

The `StripeService` class handles all interactions with the Stripe API, including:
- Customer management
- Payment method tokenization
- Payment processing
- Benefits verification

### PaymentProfileController

The `PaymentProfileController` exposes REST endpoints for:
- Creating/updating payment profiles
- Adding payment methods
- Processing payments
- Verifying benefits eligibility
- Tokenizing EBT cards

### StripeWebhookController

The `StripeWebhookController` handles asynchronous events from Stripe, such as:
- Successful payments
- Failed payments
- Refunds
- Disputes

## Security Considerations

1. **API Key Protection**: Stripe API keys are stored as environment variables, not in code.
2. **Data Masking**: Sensitive payment data is masked in logs.
3. **Input Validation**: All payment requests are validated before processing.
4. **Authentication**: All payment endpoints require authentication.
5. **HTTPS**: All API calls must be made over HTTPS.

## Testing

The payment integration includes:
- Unit tests for service methods
- Controller tests for API endpoints
- Validation tests for payment requests

## Webhook Setup

To set up Stripe webhooks:

1. Create a webhook endpoint in the Stripe dashboard pointing to `/api/webhook/stripe`
2. Set the webhook secret in your environment variables
3. Configure the events you want to receive (payment_intent.succeeded, payment_intent.payment_failed, etc.)

## Error Handling

The payment integration includes comprehensive error handling for:
- Invalid payment data
- Failed payments
- Network errors
- Stripe API errors

## Benefits Processing

For EBT/SNAP/WIC benefits:
1. Verify eligibility using the `verifyBenefitsEligibility` method
2. Tokenize the EBT card using the `tokenizeEbtCard` method
3. Process benefits payments using the `processBenefitsPayment` method

## Mixed Payments

For orders that include both EBT-eligible and non-eligible items:
1. Calculate the split between benefits-eligible and regular amounts
2. Use the `processMixedPayment` method to handle both payment types in a single transaction
