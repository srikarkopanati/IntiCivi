# IntiCivi — Civic Complaint Tracking System

## 1. Overview
IntiCivi is a full-stack civic complaint management system designed to streamline complaint submission, tracking, and resolution. It improves transparency and reduces processing time by enabling structured interaction between citizens and administrators.

Users can:
- Register and log in securely
- Submit complaints with details
- Track complaint status

Admins can:
- View all complaints
- Update complaint status

---

## 2. Features

### User Features
- User registration and login
- JWT-based authentication
- Submit complaints
- View complaint history
- Track complaint status

### Admin Features
- View all complaints
- Update complaint status

### System Features
- REST API-based backend
- Component-based frontend UI
- Scalable architecture

---

## 3. Tech Stack

### Frontend
- React (Vite)
- CSS / Basic Styling

### Backend
- Java 17
- Spring Boot
- Spring Security + JWT

### Database
- H2 (default for testing)
- PostgreSQL (production)
- Optional: MongoDB / Firebase

### Storage
- MinIO (file/image storage)

### Tools
- GitHub
- npm / yarn

---

## 4. Project Structure

project-root/
│
├── frontend_website/     # React frontend
├── backend_website/      # Spring Boot backend
├── README.md

---

## 5. Requirements

### Backend
- Java 17+
- Maven 3.6+

### Frontend
- Node.js 18+
- npm 10+ or yarn

### Optional
- PostgreSQL

---

## 6. Setup & Run

### 6.1 Clone Repository
git clone <repo_link>
cd project-root

---

### 6.2 Run Frontend
cd frontend_website
npm install
npm run dev

Frontend runs at:
http://localhost:5173

Ensure API base URL in:
src/api/authApi.js

is:
http://localhost:8080

---

### 6.3 Run Backend
cd backend_website
mvn clean install
mvn spring-boot:run

Backend runs at:
http://localhost:8080

---

### H2 Console
http://localhost:8080/h2-console

Credentials:
JDBC URL: jdbc:h2:mem:testdb  
Username: sa  
Password: (empty)

---

### 6.4 Android App

1. Open Android Studio  
2. Select: IntiCivi/InticiviApp  
3. Sync Gradle  
4. Start Emulator or connect device  
5. Click Run  

---

## 7. API Endpoints

### Auth
POST /api/auth/register  
POST /api/auth/login  
GET  /api/auth/me  

### Complaints
POST /api/complaints  
GET  /api/complaints  
GET  /api/complaints/{id}  

### Admin
GET /api/complaints/admin/all  
PUT /api/complaints/{id}/status  

### JWT Header
Authorization: Bearer <TOKEN>

---

## 8. Notes
- Uses H2 database by default
- PostgreSQL configurable in application.properties
- Start backend before frontend
- Dependencies:
  - pom.xml (backend)
  - package.json (frontend)

---

## 9. Summary
IntiCivi is a scalable full-stack civic complaint tracking system built with React and Spring Boot, suitable for academic and real-world applications.
