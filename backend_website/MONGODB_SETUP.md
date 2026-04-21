# IntiCivi Backend - MongoDB Atlas Configuration Guide

## Overview
This guide provides step-by-step instructions to configure MongoDB Atlas for the IntiCivi backend.

## MongoDB Collections

### 1. Users Collection
Stores user profiles with authentication data.

```json
{
  "_id": "ObjectId",
  "name": "string",
  "email": "string (unique index)",
  "password": "string (BCrypt encoded)",
  "role": "USER | ADMIN"
}
```

**Indexes:**
- `email` (unique)

---

### 2. Complaints Collection
Core collection for civic grievances.

```json
{
  "_id": "ObjectId",
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
  "createdAt": "ISO 8601 DateTime",
  "updatedAt": "ISO 8601 DateTime",
  "createdByUserId": "ObjectId (reference to users._id)"
}
```

**Indexes:**
- `status`
- `pincode`
- `createdByUserId`
- `createdAt`

---

### 3. Departments Collection
Government departments handling different complaint categories.

```json
{
  "_id": "ObjectId",
  "name": "string",
  "description": "string",
  "email": "string",
  "phone": "string",
  "location": "string",
  "category": "string"
}
```

---

### 4. ComplaintImages Collection
Stores additional images uploaded for complaints.

```json
{
  "_id": "ObjectId",
  "complaintId": "ObjectId (reference to complaints._id)",
  "imageUrl": "string",
  "uploadedBy": "string (userId)",
  "uploadedAt": "ISO 8601 DateTime"
}
```

---

## MongoDB Atlas Setup Steps

### Step 1: Create a MongoDB Atlas Cluster

1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Sign up or log in to your account
3. Click "Create a Deployment"
4. Choose "Shared" cluster (free tier available)
5. Select your region (closest to your users)
6. Click "Create Deployment"
7. Wait for the cluster to be created (usually 5-10 minutes)

### Step 2: Create a Database User

1. In Atlas Dashboard, go to "Security" → "Database Access"
2. Click "Add New Database User"
3. Choose "Password" authentication
4. Set Username: `inticivi_user`
5. Set Password: Generate a secure password
6. Under "Roles", select "Read and write to any database"
7. Click "Add User"

**Save the username and password - you'll need them for the connection string.**

### Step 3: Configure IP Whitelist

1. Go to "Security" → "Network Access"
2. Click "Add IP Address"
3. For development: Click "Allow access from anywhere" (0.0.0.0/0)
4. For production: Add your specific IP addresses
5. Click "Confirm"

### Step 4: Get Connection String

1. Go to your cluster overview
2. Click "Connect"
3. Select "Drivers"
4. Choose "Java" and version "3.12 or higher"
5. Copy the connection string
6. Replace `<username>`, `<password>`, and database name

The connection string will look like:
```
mongodb+srv://inticivi_user:password@cluster.mongodb.net/inticivi?retryWrites=true&w=majority
```

### Step 5: Create Indexes

MongoDB Spring Data auto-creates indexes when `spring.data.mongodb.auto-index-creation=true` is set.

The application automatically creates indexes for:
- `users.email` (unique)
- `complaints.status`
- `complaints.pincode`
- `complaints.createdByUserId`
- `complaints.createdAt`

---

## Environment Configuration

### Create .env file in project root:

```env
MONGODB_URI=mongodb+srv://inticivi_user:your_password@cluster.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi
JWT_SECRET=your-secure-jwt-secret-key-at-least-32-characters
```

### Update application.properties:

```properties
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=${DATABASE_NAME}
spring.data.mongodb.auto-index-creation=true

jwt.secret=${JWT_SECRET}
jwt.expiration-ms=86400000
server.port=8080
```

---

## Connecting React Website

### Environment Variables (.env)

```env
VITE_API_URL=http://localhost:8080
VITE_API_BASE=http://localhost:8080/api
```

### Axios Configuration

```javascript
import axios from 'axios';

const API = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add JWT token to requests
API.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default API;
```

### Authentication Flow

```javascript
// Register
POST /api/auth/register
{
  "name": "User Name",
  "email": "user@example.com",
  "password": "secure_password"
}

// Login
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "secure_password"
}

// Response includes JWT token - store in localStorage
localStorage.setItem('token', response.data.token);
```

---

## Connecting React Native Mobile App

### Environment Configuration

```javascript
const API_BASE_URL = 'http://your-backend-url:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000
});

// JWT Interceptor
api.interceptors.request.use(async config => {
  const token = await AsyncStorage.getItem('userToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

### Mobile API Integration

```javascript
// Example: Create Complaint
import { PermissionsAndroid } from 'react-native';
import Geolocation from 'react-native-geolocation-service';

const createComplaint = async (complaintData) => {
  try {
    // Get current location if needed
    const location = await getCurrentLocation();
    
    const response = await api.post('/complaints', {
      title: complaintData.title,
      description: complaintData.description,
      category: complaintData.category,
      location: complaintData.location,
      pincode: complaintData.pincode,
      latitude: location.latitude,
      longitude: location.longitude,
      imageUrl: complaintData.imageUrl
    });
    
    return response.data;
  } catch (error) {
    console.error('Error creating complaint:', error);
    throw error;
  }
};
```

### Important Note for Mobile

For React Native apps accessing the backend:
- Update CORS settings in `SecurityConfig.java` to accept requests from your mobile app
- Use absolute URLs when running on physical devices (not localhost)
- Consider using environment-specific base URLs

---

## Sample Data

The application loads sample data on first run including:

- **1 Admin User**: admin@inticivi.com / admin123
- **1 Normal User**: user@inticivi.com / password
- **5 Departments**: Public Works, Sanitation, Water Supply, Electrical, Public Safety
- **10 Sample Complaints**: Various categories with different statuses

---

## MongoDB Query Examples

### Get all complaints by status

```javascript
db.complaints.find({ status: "PENDING" })
```

### Get complaints by location (pincode)

```javascript
db.complaints.find({ pincode: "560001" })
```

### Get user's complaints

```javascript
db.complaints.find({ createdByUserId: ObjectId("...") })
```

### Update complaint priority score

```javascript
db.complaints.updateOne(
  { _id: ObjectId("...") },
  { $set: { priorityScore: 8.5 } }
)
```

### Create index for faster queries

```javascript
db.complaints.createIndex({ status: 1 })
db.complaints.createIndex({ pincode: 1 })
db.complaints.createIndex({ createdByUserId: 1 })
```

---

## Troubleshooting

### Connection Issues

1. **"MongoTimeoutException"**: Check IP whitelist in Atlas
2. **"MongoAuthenticationException"**: Verify username and password
3. **"UnknownHostException"**: Check internet connectivity

### Data Issues

1. **Duplicate key error**: Check for existing email in users collection
2. **Missing indexes**: Restart the application to trigger index creation
3. **Data not loading**: Verify DataLoader is running (check console logs)

---

## Performance Tips

1. **Always query with indexes**: Use indexed fields in conditions
2. **Projection**: Fetch only required fields
3. **Pagination**: Implement pagination for large result sets
4. **Connection Pooling**: Already configured in Spring Data MongoDB
5. **Monitor Atlas**: Use Atlas Dashboard to monitor queries and performance

---

## Production Considerations

1. Use a dedicated production database user with minimal privileges
2. Enable encryption at rest and in transit
3. Set up automated backups
4. Use VPC peering for private connections
5. Monitor query performance using MongoDB Atlas performance advisor
6. Implement rate limiting on API endpoints
7. Use HTTPS only for all communications

---

## Useful Links

- [MongoDB Atlas Documentation](https://docs.mongodb.com/atlas/)
- [Spring Data MongoDB Guide](https://spring.io/projects/spring-data-mongodb)
- [MongoDB Java Driver](https://mongodb.github.io/mongo-java-driver/)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
