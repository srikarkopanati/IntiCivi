# IntiCivi MongoDB Backend - Quick Start Guide

## 📋 Prerequisites

- Java 17+
- Maven 3.6+
- MongoDB Atlas Account (free at https://www.mongodb.com/cloud/atlas)
- Git
- VSCode or IntelliJ IDEA (optional but recommended)

## 🚀 Quick Setup (5 Minutes)

### 1. MongoDB Atlas Setup (2 minutes)

```bash
# Visit https://www.mongodb.com/cloud/atlas
1. Create a free account
2. Click "Build a Database" → Shared Cluster
3. Choose your region
4. Click "Create Deployment"
5. Create a database user (username: inticivi_user)
6. Add IP: 0.0.0.0/0 (for development)
7. Click "Connect" → "Drivers" → Copy connection string
```

### 2. Environment Configuration (1 minute)

```bash
# In backend_website folder, create .env file:
MONGODB_URI=mongodb+srv://inticivi_user:YOUR_PASSWORD@cluster.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi
JWT_SECRET=inticivi-super-secret-key-change-in-production
```

### 3. Start Backend (2 minutes)

```bash
# Navigate to backend_website directory
cd backend_website

# Build and run
mvn clean spring-boot:run

# You should see:
# ✓ Users loaded: 1 admin + 1 normal user
# ✓ Departments loaded: 5 departments
# ✓ Complaints loaded: 10 sample complaints
# ✓ MongoDB sample data loaded successfully!
```

## 🧪 Test the API

### Quick Test Commands

```bash
# 1. Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123"
  }'

# 2. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@inticivi.com",
    "password": "password"
  }'

# 3. Get auth token and save it
export TOKEN="<token_from_login_response>"

# 4. Create a complaint
curl -X POST http://localhost:8080/api/complaints \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Complaint",
    "description": "This is a test",
    "category": "Infrastructure",
    "location": "Test Location",
    "pincode": "560001",
    "latitude": 12.9716,
    "longitude": 77.5946
  }'
```

### Using Postman

1. Import `IntiCivi-MongoDB-API.postman_collection.json` in Postman
2. Set variables:
   - `token`: Get from login response
   - `adminToken`: Login with admin@inticivi.com / admin123
3. Test all endpoints

## 📚 Sample Data Loaded

### Users
- **Admin**: admin@inticivi.com / admin123
- **User**: user@inticivi.com / password

### Collections
- **Users**: 2 users (1 admin, 1 normal)
- **Departments**: 5 departments (Public Works, Sanitation, etc.)
- **Complaints**: 10 sample complaints
- **ComplaintImages**: References for uploads

## 🔑 Key Features

### Authentication
- JWT tokens (24-hour expiration by default)
- BCrypt password encryption
- Admin and User roles

### Complaints API
```
POST   /api/complaints              - Create complaint (requires auth)
GET    /api/complaints/my           - Get my complaints
GET    /api/complaints/{id}         - Get complaint details
DELETE /api/complaints/{id}         - Delete complaint

ADMIN ONLY:
GET    /api/complaints/admin/all    - Get all complaints
PUT    /api/complaints/admin/{id}/status - Update status
```

### Users API (ADMIN ONLY)
```
GET    /api/admin/users             - List all users
GET    /api/admin/users/{id}        - Get user details
PUT    /api/admin/users/{id}/role   - Update user role
DELETE /api/admin/users/{id}        - Delete user
```

## 📱 Mobile Integration

### Android/iOS Setup
```javascript
// Update API base URL for mobile
const API_BASE_URL = 'https://your-server-ip:8080/api';

// Add location permission in mobile app
// Android: 
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

// iOS:
NSLocationWhenInUseUsageDescription in Info.plist
```

### React Native Example
```javascript
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const api = axios.create({
  baseURL: 'http://your-backend-ip:8080/api'
});

api.interceptors.request.use(async (config) => {
  const token = await AsyncStorage.getItem('userToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

## 🌐 React Website Integration

### Installation
```bash
cd frontend_website
npm install
```

### Environment Setup (.env)
```env
VITE_API_URL=http://localhost:8080
```

### API Integration Example
```javascript
import axios from 'axios';

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL + '/api'
});

// Login
const loginResponse = await API.post('/auth/login', {
  email: 'user@inticivi.com',
  password: 'password'
});

localStorage.setItem('token', loginResponse.data.token);

// Add token to all requests
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

## 🗄️ MongoDB Collections Reference

### Users
```json
{
  "_id": ObjectId,
  "name": "string",
  "email": "string",
  "password": "BCrypt hash",
  "role": "ADMIN | USER"
}
```

### Complaints
```json
{
  "_id": ObjectId,
  "title": "string",
  "description": "string",
  "category": "string",
  "location": "string",
  "pincode": "string",
  "latitude": "double",
  "longitude": "double",
  "imageUrl": "string",
  "priorityScore": "double",
  "status": "PENDING | IN_PROGRESS | RESOLVED",
  "createdAt": "ISO Date",
  "updatedAt": "ISO Date",
  "createdByUserId": "ObjectId"
}
```

### Departments
```json
{
  "_id": ObjectId,
  "name": "string",
  "description": "string",
  "email": "string",
  "phone": "string",
  "location": "string",
  "category": "string"
}
```

## 🔧 Troubleshooting

| Issue | Solution |
|-------|----------|
| Connection timeout | Check IP whitelist in Atlas (0.0.0.0/0 for dev) |
| Authentication failed | Verify username/password in .env |
| Data not loading | Check DataLoader logs, restart application |
| CORS error on frontend | Update SecurityConfig with frontend URL |
| Port 8080 already in use | `kill $(lsof -t -i :8080)` or change port in application.properties |

## 📖 Documentation Files

- `MONGODB_SETUP.md` - Complete MongoDB Atlas setup guide
- `MONGODB_SCHEMA_INTEGRATION.md` - Detailed schema design and integration examples
- `IntiCivi-MongoDB-API.postman_collection.json` - API endpoints for testing

## 🚢 Deployment Checklist

Before going live:

- [ ] Change JWT_SECRET to a strong random string
- [ ] Update MONGODB_URI to production cluster
- [ ] Set IP whitelist to specific IPs (not 0.0.0.0/0)
- [ ] Enable encryption at rest in MongoDB Atlas
- [ ] Set up automated backups
- [ ] Update CORS_ORIGINS in SecurityConfig
- [ ] Enable HTTPS on frontend
- [ ] Set up monitoring and alerts
- [ ] Configure rate limiting
- [ ] Test all API endpoints thoroughly

## 📞 Support

For issues or questions:
1. Check MongoDB Atlas documentation: https://docs.mongodb.com/atlas/
2. Check Spring Data MongoDB: https://spring.io/projects/spring-data-mongodb
3. Review MONGODB_SETUP.md for detailed guides
4. Check application logs: Check console or log files

## 🎯 Next Steps

1. ✅ Start backend with MongoDB
2. ✅ Test APIs with Postman collection
3. ✅ Integrate with React frontend
4. ✅ Test with React Native mobile app
5. ✅ Deploy to production

---

**Ready to start?** Run `mvn clean spring-boot:run` in the backend_website directory!
