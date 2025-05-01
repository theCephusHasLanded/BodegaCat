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
