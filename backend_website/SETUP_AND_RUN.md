# IntiCivi Backend MongoDB - Complete Setup & Run Guide

## 📋 What's Ready

✅ **MongoDB Spring Boot Backend (Java 17)**
- Spring Data MongoDB integration
- JWT authentication with BCrypt
- Complete REST API
- 4 MongoDB collections with sample data
- Automatic index creation
- Global exception handling

✅ **Sample Data Preloaded**
- 1 Admin user: `admin@inticivi.com` / `admin123`
- 1 Normal user: `user@inticivi.com` / `password`
- 5 departments (Public Works, Sanitation, Water, Electrical, Safety)
- 10 sample complaints with various statuses
- Priority engine for complaint sorting

## 🚀 Quick Setup (Follow in Order)

### Step 1: Create MongoDB Atlas Cluster (5 minutes)

**Visit:** https://www.mongodb.com/cloud/atlas

```
1. Click "Try Free"
2. Sign up with email
3. Click "Build a Database"
4. Select "Shared" (Free tier)
5. Choose your region
6. Click "Create Deployment"
⏳ Wait 5-10 minutes...
```

### Step 2: Create Database User

```
In Atlas Dashboard:
1. Go to "Security" → "Database Access"
2. Click "Add New Database User"
3. Username: inticivi_user
4. Password: (generate strong one - SAVE IT!)
5. Role: "Read and write to any database"
6. Click "Add User"
```

### Step 3: Allow Network Access

```
In Atlas Dashboard:
1. Go to "Security" → "Network Access"
2. Click "Add IP Address"
3. Select "Allow access from anywhere" (0.0.0.0/0)
   [For production, add specific IP]
4. Click "Confirm"
```

### Step 4: Get Connection String

```
In Atlas Dashboard:
1. Click your cluster
2. Click "Connect" button
3. Select "Drivers"
4. Choose "Java" → "3.12 or higher"
5. Copy the connection string
6. Replace <username> and <password>
```

**Format:**
```
mongodb+srv://inticivi_user:YOUR_PASSWORD@cluster0.mongodb.net/?retryWrites=true&w=majority
```

### Step 5: Update .env File

**File:** `backend_website/.env`

Replace with your credentials:
```env
MONGODB_URI=mongodb+srv://inticivi_user:YOUR_PASSWORD@cluster0.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi
JWT_SECRET=inticivi-dev-secret-key-change-in-production-12345678
```

### Step 6: Run the Backend

**PowerShell:**
```powershell
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"
mvn clean spring-boot:run
```

**Expected Output:**
```
✓ Users loaded: 1 admin + 1 normal user
✓ Departments loaded: 5 departments
✓ Complaints loaded: 10 sample complaints
✓ MongoDB sample data loaded successfully!

Tomcat started on port(s): 8080 (http)
Started IntiCiviApplication in XX.XXX seconds
```

### Step 7: Test Backend

**Run the helper script:**
```powershell
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\test-api.ps1
```

**Or manually test:**
```powershell
# Login test
$body = '{"email":"user@inticivi.com","password":"password"}'
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body $body
```

## 📊 API Endpoints

### Authentication
```
POST   /api/auth/register                - Register new user
POST   /api/auth/login                   - Login user
GET    /api/auth/me                      - Get current user
```

### Complaints (User)
```
POST   /api/complaints                   - Create complaint
GET    /api/complaints/my                - Get my complaints
GET    /api/complaints/{id}              - Get complaint details
DELETE /api/complaints/{id}              - Delete complaint
```

### Complaints (Admin Only)
```
GET    /api/complaints/admin/all         - Get all complaints
PUT    /api/complaints/admin/{id}/status - Update status
```

### Users (Admin Only)
```
GET    /api/admin/users                  - List all users
GET    /api/admin/users/{id}             - Get user details
PUT    /api/admin/users/{id}/role        - Update user role
DELETE /api/admin/users/{id}             - Delete user
```

## 🔐 Sample Credentials

**Admin User:**
- Email: `admin@inticivi.com`
- Password: `admin123`
- Role: ADMIN (full access)

**Normal User:**
- Email: `user@inticivi.com`
- Password: `password`
- Role: USER (limited access)

## 🗄️ MongoDB Collections

### users
```json
{
  "_id": ObjectId,
  "name": "string",
  "email": "string (unique)",
  "password": "BCrypt hash",
  "role": "ADMIN | USER"
}
```

### complaints
```json
{
  "_id": ObjectId,
  "title": "string",
  "description": "string",
  "category": "string",
  "location": "string",
  "pincode": "string",
  "latitude": double,
  "longitude": double,
  "imageUrl": "string",
  "priorityScore": double,
  "status": "PENDING | IN_PROGRESS | RESOLVED",
  "createdAt": ISODate,
  "updatedAt": ISODate,
  "createdByUserId": ObjectId
}
```

### departments
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

### complaintImages
```json
{
  "_id": ObjectId,
  "complaintId": ObjectId,
  "imageUrl": "string",
  "uploadedBy": "string",
  "uploadedAt": ISODate
}
```

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `MONGODB_ATLAS_SETUP.md` | Detailed MongoDB Atlas setup steps |
| `MONGODB_SETUP.md` | Complete schema design guide |
| `MONGODB_SCHEMA_INTEGRATION.md` | Integration examples for React & React Native |
| `QUICKSTART.md` | Quick reference guide |
| `IntiCivi-MongoDB-API.postman_collection.json` | Postman collection for API testing |
| `test-api.ps1` | PowerShell script to test APIs |

## 🧪 Testing Tools

### Option 1: Postman (Recommended)
1. Open Postman
2. Click "Import"
3. Upload `IntiCivi-MongoDB-API.postman_collection.json`
4. Set variables: `token`, `adminToken`, `complaintId`, `userId`
5. Test endpoints

### Option 2: PowerShell Script
```powershell
.\test-api.ps1
```

### Option 3: cURL Commands
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@inticivi.com","password":"password"}'

# Get My Complaints
curl -X GET http://localhost:8080/api/complaints/my \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## ⚠️ Troubleshooting

| Issue | Solution |
|-------|----------|
| `MongoTimeoutException` | Check IP whitelist (0.0.0.0/0), verify internet |
| `MongoAuthenticationException` | Verify username/password in .env |
| `Port 8080 in use` | Kill process: `netstat -ano \| findstr :8080` |
| `ConnectionRefused` | Cluster not initialized (wait 5-10 min) |
| `Data not loading` | Check DataLoader logs, restart backend |

## 🔄 Typical Development Flow

```
1. Backend running on port 8080 ✓
2. Test APIs with Postman ✓
3. Integrate with React frontend (port 5173)
4. Test with React Native mobile app
5. Deploy to production
```

## 📱 Frontend Integration

**React (.env):**
```env
VITE_API_URL=http://localhost:8080
```

**React (API Setup):**
```javascript
const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL + '/api'
});

// Add token to requests
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

## 🌐 Mobile Integration

**React Native:**
```javascript
const API = axios.create({
  baseURL: 'http://your-server-ip:8080/api' // Use IP, not localhost
});

// Store token in AsyncStorage
const token = await AsyncStorage.getItem('userToken');
if (token) {
  config.headers.Authorization = `Bearer ${token}`;
}
```

## ✅ Pre-Launch Checklist

- [ ] MongoDB Atlas cluster created
- [ ] Database user created
- [ ] IP whitelist configured
- [ ] .env file updated with credentials
- [ ] Backend starts successfully
- [ ] Sample data loads (check logs)
- [ ] APIs test successfully
- [ ] JWT token generation works
- [ ] Frontend can connect and authenticate
- [ ] Mobile app can connect and authenticate

## 🚢 Production Deployment

Before going live:

1. **Security:**
   - Change JWT_SECRET to strong random string
   - Update MONGODB_URI to production cluster
   - Restrict IP whitelist to specific IPs
   - Enable encryption at rest and in transit

2. **Performance:**
   - Monitor MongoDB queries
   - Set up automated backups
   - Enable replica set for high availability

3. **Monitoring:**
   - Set up alerts for errors
   - Monitor API response times
   - Track database performance

4. **Browser Support:**
   - Update CORS_ORIGINS in SecurityConfig
   - Test across devices

## 📞 Support Resources

- MongoDB Docs: https://docs.mongodb.com/atlas/
- Spring Data: https://spring.io/projects/spring-data-mongodb
- Java Driver: https://mongodb.github.io/mongo-java-driver/
- Spring Security: https://spring.io/projects/spring-security

## 🎯 Next Steps

1. ✅ Set up MongoDB Atlas (this page)
2. ✅ Run backend with `mvn clean spring-boot:run`
3. ✅ Test APIs with Postman
4. → Integrate React frontend
5. → Test React Native mobile app
6. → Deploy to production

---

**Ready?** Start with Step 1 above and follow in order! 🚀

If you get stuck, check the detailed guides in the documentation files or review the troubleshooting section.
