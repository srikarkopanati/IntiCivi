# MongoDB Schema Design & Integration Guide

## Database Collections Structure

### 1. Users Collection
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "name": "Raj Kumar",
  "email": "raj@example.com",
  "password": "$2a$10$...", // BCrypt encoded
  "role": "USER"
}
```
**Indexes**: `email` (unique)

### 2. Complaints Collection
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439012"),
  "title": "Broken Streetlight",
  "description": "The streetlight is not working",
  "category": "Infrastructure",
  "location": "Main Road",
  "pincode": "560001",
  "latitude": 12.9716,
  "longitude": 77.5946,
  "imageUrl": "https://cdn.example.com/image.jpg",
  "priorityScore": 8.5,
  "status": "PENDING",
  "createdAt": ISODate("2024-04-14T10:30:00Z"),
  "updatedAt": ISODate("2024-04-14T10:30:00Z"),
  "createdByUserId": ObjectId("507f1f77bcf86cd799439011")
}
```
**Indexes**: 
- `status` (single field)
- `pincode` (single field)
- `createdByUserId` (single field)
- `createdAt` (single field)
- Compound index: `{pincode: 1, category: 1}` for priority calculation

### 3. Departments Collection
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439013"),
  "name": "Public Works Department",
  "description": "Handles roads and infrastructure",
  "email": "pw@gov.in",
  "phone": "1800-123-4567",
  "location": "Downtown",
  "category": "Infrastructure"
}
```

### 4. ComplaintImages Collection
```json
{
  "_id": ObjectId("507f1f77bcf86cd799439014"),
  "complaintId": ObjectId("507f1f77bcf86cd799439012"),
  "imageUrl": "https://cdn.example.com/image2.jpg",
  "uploadedBy": "ray@example.com",
  "uploadedAt": ISODate("2024-04-14T11:00:00Z")
}
```

## MongoDB Atlas Setup

### Prerequisites
- MongoDB Atlas Account (free tier available)
- Node.js (for npm packages)
- Java 17+
- Maven 3.6+

### Step-by-Step Setup

#### 1. Create MongoDB Atlas Cluster

```bash
# Navigate to https://www.mongodb.com/cloud/atlas
# 1. Sign in or create account
# 2. Create a new project
# 3. Click "Build a Database"
# 4. Select Shared (free tier)
# 5. Choose region closest to users
# 6. Click "Create Deployment"
# 7. Set up database credentials
```

#### 2. Configure Network Access

```bash
# In Atlas Dashboard:
# 1. Go to Security > Network Access
# 2. Click "Add IP Address"
# 3. For development: Add 0.0.0.0/0
# 4. For production: Add specific IPs
# 5. Click "Confirm"
```

#### 3. Get Connection String

```bash
# In your cluster view:
# 1. Click "Connect"
# 2. Select "Drivers"
# 3. Choose "Java" > "3.12 or higher"
# 4. Copy the connection string
```

Connection string format:
```
mongodb+srv://username:password@cluster.mongodb.net/databasename?retryWrites=true&w=majority
```

#### 4. Environment Configuration

Create `.env` file in backend directory:

```env
# MongoDB Atlas Connection
MONGODB_URI=mongodb+srv://inticivi_user:your_password@cluster.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi

# JWT Configuration
JWT_SECRET=your-very-secure-secret-key-at-least-32-characters-long

# Server Configuration
SERVER_PORT=8080
```

#### 5. Update application.properties

```properties
# MongoDB Configuration
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.database=${DATABASE_NAME}
spring.data.mongodb.auto-index-creation=true

# JWT Configuration
jwt.secret=${JWT_SECRET}
jwt.expiration-ms=86400000

# Server Configuration
server.port=${SERVER_PORT:8080}

# Logging
logging.level.spring.data.mongodb=INFO
logging.level.org.springframework.security=INFO
```

## Repository Query Methods

### UserRepository
```java
// Find user by email
Optional<User> findByEmail(String email);

// Check if email exists
boolean existsByEmail(String email);
```

### ComplaintRepository
```java
// Find all complaints by user (sorted by date)
List<Complaint> findByCreatedByUserIdOrderByCreatedAtDesc(String userId);

// Count similar complaints for priority calculation
long countByCategoryAndPincode(String category, String pincode);
```

### DepartmentRepository
```java
// Find department by name
Optional<Department> findByName(String name);

// Find all departments by category
List<Department> findByCategory(String category);
```

### ComplaintImageRepository
```java
// Find all images for a complaint
List<ComplaintImage> findByComplaintId(String complaintId);

// Delete all images for a complaint
void deleteByComplaintId(String complaintId);
```

## Seed Data Loader

The `DataLoader` component automatically loads sample data on application startup:

```
✓ Users (2):
  - admin@inticivi.com (ADMIN)
  - user@inticivi.com (USER)

✓ Departments (5):
  - Public Works
  - Sanitation
  - Water Supply
  - Electrical
  - Public Safety

✓ Complaints (10):
  - Various categories and statuses
  - Distributed across different pincodes
  - Created by both admin and normal users
```

## React Website Integration

### API Service Setup

```javascript
// src/api/axiosConfig.js
import axios from 'axios';

const API_BASE_URL = process.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// JWT Token Interceptor
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### Environment Variables (.env)

```env
VITE_API_URL=http://localhost:8080
```

### Authentication Integration

```javascript
// Register
async function registerUser(userData) {
  const response = await api.post('/auth/register', {
    name: userData.name,
    email: userData.email,
    password: userData.password,
    role: 'USER'
  });
  return response.data;
}

// Login
async function loginUser(email, password) {
  const response = await api.post('/auth/login', {
    email,
    password
  });
  localStorage.setItem('authToken', response.data.token);
  localStorage.setItem('user', JSON.stringify(response.data.user));
  return response.data;
}
```

### Complaint Operations

```javascript
// Create complaint
async function createComplaint(complaintData) {
  return await api.post('/complaints', {
    title: complaintData.title,
    description: complaintData.description,
    category: complaintData.category,
    location: complaintData.location,
    pincode: complaintData.pincode,
    latitude: complaintData.latitude,
    longitude: complaintData.longitude,
    imageUrl: complaintData.imageUrl
  });
}

// Get user's complaints
async function getUserComplaints() {
  return await api.get('/complaints/my');
}

// Get complaint details
async function getComplaintDetails(complaintId) {
  return await api.get(`/complaints/${complaintId}`);
}
```

## React Native Mobile Integration

### API Configuration

```javascript
// api/apiClient.js
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

const API_BASE_URL = 'https://your-backend-domain.com:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000,
});

// JWT Token Interceptor
apiClient.interceptors.request.use(async (config) => {
  const token = await AsyncStorage.getItem('userToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
```

### Location Integration

```javascript
// For getting coordinates on mobile
import Geolocation from 'react-native-geolocation-service';
import { PermissionsAndroid } from 'react-native';

async function getDeviceLocation() {
  try {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
    );
    
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      return new Promise((resolve, reject) => {
        Geolocation.getCurrentPosition(
          (position) => {
            resolve({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
            });
          },
          (error) => reject(error)
        );
      });
    }
  } catch (error) {
    console.error('Location permission error:', error);
  }
}
```

### File Upload for Images

```javascript
// For both web and mobile
async function uploadComplaintImage(complaintId, imageUri) {
  const formData = new FormData();
  formData.append('complaintId', complaintId);
  formData.append('image', {
    uri: imageUri,
    type: 'image/jpeg',
    name: `complaint-${Date.now()}.jpg`,
  });

  return await api.post('/complaints/images/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}
```

## Monitoring & Performance

### MongoDB Atlas Monitoring

1. **Performance Advisor**: Automatically identifies slow queries
2. **Real-time Alerts**: Get notified of connection issues
3. **Query Profiler**: Analyze query execution times
4. **Index Recommendations**: See suggested indexes

### Backend Monitoring

```properties
# Enable MongoDB logging
logging.level.org.springframework.data.mongodb=DEBUG

# Enable query profiling
spring.data.mongodb.uri=mongodb+srv://.../?serverSelectionTimeoutMS=5000

# Monitor application metrics
management.endpoints.web.exposure.include=health,metrics
```

## Security Best Practices

1. **Never commit .env file** - Add to .gitignore
2. **Use strong passwords** - At least 16 characters with special characters
3. **Enable IP Whitelist** - Restrict access to known IPs in production
4. **Enable encryption** - Enable encryption at rest and in transit in Atlas
5. **Use VPC Peering** - For production environments
6. **Rotate credentials** - Regularly update database passwords
7. **Monitor access** - Use Atlas audit logs to monitor database access

## Troubleshooting

### Connection Issues

```bash
# Test MongoDB connection
mongo "mongodb+srv://username:password@cluster.mongodb.net/test"

# Check Spring Boot logs for connection errors
tail -f logs/spring.log | grep MongoDB
```

### Common Errors

| Error | Solution |
|-------|----------|
| `MongoTimeoutException` | Check IP whitelist in Atlas |
| `MongoAuthenticationException` | Verify username/password |
| `UnknownHostException` | Check internet connection |
| `Duplicate key error` | Check for existing email in users |

### Reset Sample Data

```javascript
// Clear all collections
db.users.deleteMany({})
db.complaints.deleteMany({})
db.departments.deleteMany({})
db.complaintImages.deleteMany({})

// Restart backend to reload sample data
```

## Next Steps

1. Create MongoDB Atlas account and cluster
2. Configure network access and get connection string
3. Set up .env file with credentials
4. Run `mvn spring-boot:run` to start backend
5. Verify sample data loaded in MongoDB
6. Test API endpoints with Postman collection
7. Integrate with React website and mobile app
