# MongoDB Atlas Setup - Complete Step-by-Step Guide

## Step 1: Create MongoDB Atlas Account

1. Visit https://www.mongodb.com/cloud/atlas
2. Click "Try Free" or "Sign Up"
3. Create account with email and password
4. Complete email verification
5. Accept terms and create account

## Step 2: Create Your First Cluster

1. After login, you'll see "Build a Database" button
2. Click "Build a Database"
3. Choose **"Shared"** (Free tier)
4. Select region closest to you (e.g., Singapore, Mumbai, or US regions)
5. Click "Create Deployment"
6. Wait 5-10 minutes for cluster creation

## Step 3: Create Database User

1. In left sidebar, go to **"Security"** > **"Database Access"**
2. Click **"Add New Database User"**
3. Fill in:
   - Username: `inticivi_user`
   - Password: Generate secure password (save it!)
   - Or use auto-generated password
4. Under "Built-in Roles", select:
   - ✓ "Read and write to any database"
5. Click **"Add User"**

**IMPORTANT:** Save the full username and password combination!

## Step 4: Configure Network Access

1. Go to **"Security"** > **"Network Access"**
2. Click **"Add IP Address"**
3. Choose one:
   - For **Development**: Click "Allow access from anywhere" (0.0.0.0/0)
   - For **Production**: Add your specific IP address
4. Click **"Confirm"**

## Step 5: Get Connection String

1. Go back to **"Databases"** view
2. Find your cluster (e.g., "Cluster0")
3. Click **"Connect"**
4. Click **"Drivers"**
5. Select **"Java"** and version **"3.12 or higher"**
6. You'll see connection string like:
   ```
   mongodb+srv://<username>:<password>@cluster0.mongodb.net/?retryWrites=true&w=majority
   ```

## Step 6: Update Your .env File

Replace placeholders in the .env file:

```env
# Replace <username> and <password> with your credentials from Step 3
MONGODB_URI=mongodb+srv://inticivi_user:YOUR_PASSWORD_HERE@cluster0.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi
JWT_SECRET=inticivi-dev-secret-key-change-in-production-12345678
```

**Example:**
```env
MONGODB_URI=mongodb+srv://inticivi_user:SecurePass123@cluster0.mongodb.net/?retryWrites=true&w=majority
DATABASE_NAME=inticivi
JWT_SECRET=inticivi-dev-secret-key-change-in-production-12345678
```

## Step 7: Verify MongoDB Connection (Windows PowerShell)

```powershell
# Install MongoDB CLI tools (optional but helpful for testing)
# choco install mongocli  # If you have Chocolatey installed

# Or test directly with the Java backend
```

## Step 8: Run Your Backend

### Option A: Using Maven (Recommended)

```powershell
# Navigate to backend directory
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"

# Clean and run
mvn clean spring-boot:run
```

### Option B: Build JAR and Run

```powershell
# Build the project
mvn clean package

# Run the JAR (look for target/inti-civi-backend-0.0.1-SNAPSHOT.jar)
java -jar target/inti-civi-backend-0.0.1-SNAPSHOT.jar
```

### Option C: If Using IDE (IntelliJ IDEA / VSCode)

1. Open project in IDE
2. Configure SDK: Java 17
3. Right-click IntiCiviApplication.java
4. Select "Run"

## Step 9: Verify Backend Started Successfully

You should see in console:

```
✓ MongoDB sample data loaded successfully!
✓ Users loaded: 1 admin + 1 normal user
✓ Departments loaded: 5 departments
✓ Complaints loaded: 10 sample complaints

Started IntiCiviApplication in X seconds
Tomcat started on port(s): 8080
```

## Step 10: Quick API Test

Open a new PowerShell terminal:

```powershell
# Test login API
$body = @{
    email = "user@inticivi.com"
    password = "password"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body $body

# Display response
$response.Content | ConvertFrom-Json | Format-List
```

Expected response:
```json
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

## Troubleshooting

### "MongoTimeoutException" - Connection Timeout
- ✓ Check IP whitelist (should be 0.0.0.0/0 for development)
- ✓ Verify internet connection
- ✓ Wait for cluster to be fully initialized (5-10 minutes)

### "MongoAuthenticationException" - Wrong Credentials
- ✓ Double-check username and password in .env
- ✓ Make sure to use the correct database user (not admin account)
- ✓ Verify password doesn't have special characters causing issues (URL encode if needed)

### "UnknownHostException"
- ✓ Check internet connectivity
- ✓ Verify MongoDB Atlas cluster name in URI

### Port 8080 Already in Use
```powershell
# Kill process using port 8080
Get-NetTCPConnection -LocalPort 8080 | Select-Object -ExpandProperty OwningProcess | ForEach-Object { Stop-Process -Id $_ -Force }
```

### Build Failed - Dependency Issues
```powershell
# Force update dependencies
mvn clean install -U
```

## Next Steps

1. ✅ Backend running on http://localhost:8080
2. Frontend integration (React)
3. Mobile integration (React Native)
4. Deploy to production

## Useful MongoDB Atlas Links

- Docs: https://docs.mongodb.com/atlas/
- Connection Troubleshooting: https://docs.mongodb.com/atlas/troubleshoot-connection/
- Security Settings: https://docs.mongodb.com/atlas/security-checklist/

## Environment Variables Summary

| Variable | Purpose | Example |
|----------|---------|---------|
| `MONGODB_URI` | Database connection string | `mongodb+srv://user:pass@cluster.mongodb.net/?retryWrites=true&w=majority` |
| `DATABASE_NAME` | Database name | `inticivi` |
| `JWT_SECRET` | JWT signing secret (min 32 chars) | `inticivi-dev-secret-key-...` |

## Important Notes

⚠️ **Security:**
- Never commit `.env` to Git (already in .gitignore)
- Change JWT_SECRET in production
- Use strong passwords for MongoDB
- In production, restrict IP whitelist to specific IPs

⚠️ **Free Tier Limits:**
- 512 MB storage
- 3 nodes shared
- Suitable for development and testing
- Upgrade to Premium for production

---

**You're ready to go!** Run `mvn clean spring-boot:run` and see your backend come to life! 🚀
