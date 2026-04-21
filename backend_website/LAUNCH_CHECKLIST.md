# ✅ IntiCivi Backend - Pre-Launch Checklist

## 📋 Your Backend is Ready!

Everything needed for the IntiCivi MongoDB backend is complete and configured. This checklist will get you running in 15 minutes.

---

## ⏱️ QUICK START (15 minutes)

### 1️⃣ Create MongoDB Atlas Account (3 mins)
- [ ] Visit https://www.mongodb.com/cloud/atlas
- [ ] Click "Try Free" → Sign up with email
- [ ] Verify email
- [ ] Click "Build a Database"

### 2️⃣ Create Cluster (5 mins)
- [ ] Select "Shared" (Free tier)
- [ ] Pick your region (closest to you)
- [ ] Click "Create Deployment"
- [ ] ⏳ **Wait 5-10 minutes for cluster to initialize**

### 3️⃣ Create Database User (2 mins)
- [ ] Go to "Security" → "Database Access"
- [ ] Click "Add New Database User"
- [ ] Username: `inticivi_user`
- [ ] Password: **Generate strong password - SAVE IT!**
- [ ] Role: "Read and write to any database"
- [ ] Click "Add User"

### 4️⃣ Allow Network Access (1 min)
- [ ] Go to "Security" → "Network Access"
- [ ] Click "Add IP Address"
- [ ] Select "Allow access from anywhere" (0.0.0.0/0)
- [ ] Click "Confirm"

### 5️⃣ Get Connection String (1 min)
- [ ] Click "Connect" →  "Drivers" → "Java"
- [ ] Copy the connection string
- [ ] Replace `<username>` with `inticivi_user`
- [ ] Replace `<password>` with YOUR PASSWORD

**Format:**
```
mongodb+srv://inticivi_user:YOUR_PASSWORD@cluster0.mongodb.net/?retryWrites=true&w=majority
```

### 6️⃣ Update .env File (1 min)
- [ ] Open: `backend_website/.env`
- [ ] Replace `MONGODB_URI` with your connection string (from Step 5)
- [ ] Save file

**File should look like:**
```env
MONGODB_URI=mongodb+srv://inticivi_user:YOUR_PASSWORD@cluster0.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi
JWT_SECRET=inticivi-dev-secret-key-change-in-production-12345678
```

### 7️⃣ Run Backend (2 mins)
- [ ] Open PowerShell in `backend_website` folder
- [ ] Run: `mvn clean spring-boot:run`
- [ ] ⏳ Wait for build (30-60 seconds first time)

**Expected Output:**
```
✓ Users loaded: 1 admin + 1 normal user
✓ Departments loaded: 5 departments
✓ Complaints loaded: 10 sample complaints
✓ MongoDB sample data loaded successfully!

Tomcat started on port(s): 8080
Started IntiCiviApplication
```

### 8️⃣ Test Backend (1 min)
- [ ] Open new PowerShell terminal
- [ ] Run: `.\test-api.ps1` from backend_website folder
- [ ] Verify all tests pass ✓

---

## 📊 Status Check

After completing above, you should have:

| Item | Status |
|------|--------|
| MongoDB Atlas cluster | ✓ Running |
| Database user created | ✓ inticivi_user |
| Network access configured | ✓ 0.0.0.0/0 |
| .env updated | ✓ Real credentials |
| Backend running | ✓ Port 8080 |
| Sample data loaded | ✓ 10 complaints |
| APIs responding | ✓ Test script passed |

---

## 🔐 Test User Credentials

**Admin User:**
```
Email: admin@inticivi.com
Password: admin123
```

**Regular User:**
```
Email: user@inticivi.com
Password: password
```

---

## 🧪 Manual API Test (Optional)

**Test Login:**
```powershell
$body = '{"email":"user@inticivi.com","password":"password"}'
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body $body

$response.Content | ConvertFrom-Json | Format-List
```

**Expected Response:**
```
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": "...",
    "name": "Demo User",
    "email": "user@inticivi.com",
    "role": "USER"
  }
}
```

---

## 📚 Documentation

| File | Purpose |
|------|---------|
| **SETUP_AND_RUN.md** | Complete setup guide you're reading now |
| **MONGODB_ATLAS_SETUP.md** | Detailed step-by-step for Atlas |
| **MONGODB_SETUP.md** | Schema and entity design |
| **MONGODB_SCHEMA_INTEGRATION.md** | React & React Native integration |
| **QUICKSTART.md** | API quick reference |
| **test-api.ps1** | Automated testing script |
| **IntiCivi-MongoDB-API.postman_collection.json** | Postman collection |

---

## ⚠️ Common Issues & Solutions

### Problem: "MongoTimeoutException"
```
✓ Check cluster is fully initialized (wait 10 mins)
✓ Verify IP whitelist is set to 0.0.0.0/0
✓ Check internet connection
```

### Problem: "MongoAuthenticationException"
```
✓ Verify username: inticivi_user
✓ Verify password matches cluster password
✓ Check password doesn't have special chars needing URL encoding
```

### Problem: "Port 8080 already in use"
```powershell
# Kill existing process
Get-NetTCPConnection -LocalPort 8080 | `
  Select-Object -ExpandProperty OwningProcess | `
  ForEach-Object { Stop-Process -Id $_ -Force }
```

### Problem: "mvn: command not found"
```powershell
# Make sure you're in correct directory
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"
```

### Problem: Build takes forever (first time)
```
✓ This is normal - Maven downloads all dependencies
✓ First run: 2-5 minutes
✓ Subsequent runs: 30-60 seconds
```

---

## 🚀 What's Next?

After confirming backend is running:

1. **Test all APIs:** Import Postman collection
2. **Frontend Integration:** Connect React frontend (port 5173)
3. **Mobile Integration:** Set up React Native app
4. **Deployment:** Move to production server

---

## 🔒 Production Checklist (Before Deploy)

- [ ] Change JWT_SECRET to strong random string (32+ chars)
- [ ] Update MONGODB_URI to production cluster
- [ ] Restrict IP whitelist to specific IPs (not 0.0.0.0/0)
- [ ] Enable MongoDB encryption at rest
- [ ] Set up automated backups
- [ ] Enable HTTPS for API
- [ ] Update CORS_ORIGINS
- [ ] Test with real mobile devices
- [ ] Set up monitoring and alerts

---

## 📞 Need Help?

**Check Documentation:**
- Detailed setup: `MONGODB_ATLAS_SETUP.md`
- API endpoints: `QUICKSTART.md`
- Schema details: `MONGODB_SETUP.md`

**Test Your Setup:**
```powershell
.\test-api.ps1
```

**Verify Connection:**
```powershell
# Check if backend is responding
curl http://localhost:8080/api/auth/me
```

---

## ✅ Success Indicators

Your backend is ready when you see:

```
✓ No compilation errors
✓ Tomcat started on port 8080
✓ "MongoDB sample data loaded successfully!"
✓ API test script shows: All tests completed successfully!
✓ Can login with admin or user credentials
```

---

## 🎯 You're All Set!

Your IntiCivi backend is fully configured and ready to go. 

**Next command to run:**
```powershell
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"
mvn clean spring-boot:run
```

**Then visit:** http://localhost:8080

Good luck! 🚀
