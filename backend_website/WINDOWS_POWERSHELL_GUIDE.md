# Windows PowerShell Setup Guide for IntiCivi Backend

## Prerequisites Check

Before starting, verify you have:

### 1. Java 17 Installed
```powershell
java -version
```

Expected output should show `java version "17"`

If not installed:
- Download from: https://www.oracle.com/java/technologies/downloads/#java17
- Or use: `choco install openjdk17` (if you have Chocolatey)

### 2. Maven Installed
```powershell
mvn --version
```

Expected output should show `Apache Maven 3.x.x`

If not installed:
- Download from: https://maven.apache.org/download.cgi
- Or use: `choco install maven` (if you have Chocolatey)

### 3. Git (Optional but recommended)
```powershell
git --version
```

If not installed:
- Download from: https://git-scm.com/download/win
- Or use: `choco install git`

---

## Environment Variables Setup (First Time Only)

### Check if JAVA_HOME is set:
```powershell
$env:JAVA_HOME
```

If empty, set it:
```powershell
# Replace with your Java 17 installation path
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.x"
$env:PATH += ";$env:JAVA_HOME\bin"

# Verify
java -version
```

### For Permanent Setup (Recommended)
1. Right-click "This PC" → "Properties"
2. Click "Advanced system settings"
3. Click "Environment Variables" button
4. Click "New" under "System variables"
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-17.0.x` (adjust version)
5. Click OK and restart PowerShell

---

## Step-by-Step Execution

### 1. Navigate to Backend Directory
```powershell
# Create convenience alias (optional)
Set-Alias -Name cbd -Value 'cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"'

# Or just change directory
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"

# Verify you're in the right place
ls

# You should see: pom.xml, src, target, .env, etc.
```

### 2. Verify Maven Can Find Dependencies
```powershell
# This downloads all dependencies (first time takes 2-5 mins)
mvn dependency:resolve

# If successful, you'll see: [INFO] BUILD SUCCESS
```

### 3. Test Build (Optional)
```powershell
# Compile without running
mvn clean compile

# Should show: [INFO] BUILD SUCCESS
```

### 4. Start Backend
```powershell
mvn clean spring-boot:run

# First run output:
# - Downloading dependencies...
# - Compiling source files...
# - Starting application...
# - Loading sample data...
# - Tomcat started on port 8080

# ⏳ First run takes 2-3 minutes
# ⏱️ Subsequent runs take 30-60 seconds
```

### 5. Keep Backend Running
```
✓ Keep this terminal window OPEN while backend runs
✓ You'll see log messages in real-time
✓ To stop: Press Ctrl+C
```

---

## Open New Terminal for Testing

### Start New PowerShell Window
```powershell
# Click on PowerShell icon or press WIN+X → I
# Or press Ctrl+Shift+T in VS Code
```

### Test Backend
```powershell
# Navigate to backend folder (same as before)
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"

# Run test script
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\test-api.ps1

# Should show: ✓ All tests completed successfully!
```

---

## Manual API Testing in PowerShell

### Test 1: Check Backend is Responding
```powershell
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/auth/me" -SkipHttpErrorCheck
    Write-Host "✓ Backend is running!"
} catch {
    Write-Host "✗ Backend is not running. Start with: mvn clean spring-boot:run"
}
```

### Test 2: Login and Get Token
```powershell
$loginResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body '{"email":"user@inticivi.com","password":"password"}'

$token = ($loginResponse.Content | ConvertFrom-Json).token
Write-Host "✓ Token received: $($token.Substring(0,20))..."
```

### Test 3: Get User's Complaints
```powershell
# First get token (from Test 2 above)
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

$complaints = Invoke-WebRequest -Uri "http://localhost:8080/api/complaints/my" `
    -Headers $headers

($complaints.Content | ConvertFrom-Json) | Format-List
```

### Test 4: Admin - Get All Complaints
```powershell
# Login as admin
$adminResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body '{"email":"admin@inticivi.com","password":"admin123"}'

$adminToken = ($adminResponse.Content | ConvertFrom-Json).token

# Get all complaints
$headers = @{
    "Authorization" = "Bearer $adminToken"
    "Content-Type" = "application/json"
}

$allComplaints = Invoke-WebRequest -Uri "http://localhost:8080/api/complaints/admin/all" `
    -Headers $headers

($allComplaints.Content | ConvertFrom-Json) | Format-List
```

---

## Useful PowerShell Commands

### View Current Directory
```powershell
pwd
# or
Get-Location
```

### List Files
```powershell
ls
# or
Get-ChildItem
```

### Clear Screen
```powershell
Clear
# or press Ctrl+L
```

### Kill Process on Port 8080
```powershell
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill by PID (replace XXXX with the PID shown above)
taskkill /PID XXXX /F

# Or use this one-liner
Get-NetTCPConnection -LocalPort 8080 | `
  Select-Object -ExpandProperty OwningProcess | `
  ForEach-Object { Stop-Process -Id $_ -Force }
```

### Edit .env File
```powershell
# Open in Notepad
notepad .\.env

# Or open in default editor
.\\.env

# Or view contents
Get-Content .\.env
```

### View Maven Build Log
```powershell
# Maven automatically creates a build.log
# View it while building
Get-Content log.txt -Wait

# Or save the output to file
mvn clean spring-boot:run > build.log 2>&1
```

---

## Troubleshooting Common Windows Issues

### Issue: "Java command not found"
```powershell
# Verify Java is installed
java -version

# If not found:
# 1. Install Java 17 from https://www.oracle.com/java/technologies/downloads/
# 2. Set JAVA_HOME environment variable
# 3. Restart PowerShell
```

### Issue: "Maven command not found"
```powershell
# Verify Maven is installed
mvn --version

# If not found:
# 1. Install Maven from https://maven.apache.org/download.cgi
# 2. Add Maven to PATH in environment variables
# 3. Restart PowerShell
```

### Issue: "Port 8080 already in use"
```powershell
# Kill process using port 8080
Get-NetTCPConnection -LocalPort 8080 | 
  Select-Object -ExpandProperty OwningProcess | 
  ForEach-Object { Stop-Process -Id $_ -Force }

# Then start backend again
mvn clean spring-boot:run
```

### Issue: "Permission denied" running scripts
```powershell
# Temporarily allow script execution
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process

# Then run your script
.\test-api.ps1
```

### Issue: "MongoDB connection timeout"
```
1. Verify cluster is initialized (check Atlas website)
2. Verify IP whitelist includes 0.0.0.0/0
3. Check .env has correct MONGODB_URI
4. Restart backend: Ctrl+C, then mvn clean spring-boot:run
```

### Issue: "Special characters in password cause issues"
```
If your MongoDB password contains special characters like @, #, $, %
URL encode them in the connection string:

@ = %40
# = %23
$ = %24
% = %25
: = %3A
/ = %2F

Example:
Password: p@ss#word123
Encoded: p%40ss%23word123

Connection string:
mongodb+srv://username:p%40ss%23word123@cluster.mongodb.net/
```

---

## Performance Tips

### Speed Up Maven Builds
```powershell
# Use parallel compilation
$env:MAVEN_OPTS = "-DskipITs"
mvn -T 1C clean spring-boot:run

# Skip tests (faster)
mvn clean spring-boot:run -DskipTests
```

### Monitor System Resources During Build
```powershell
# Open Task Manager
taskmgr

# Or use PowerShell to monitor
Get-Process | Where-Object {$_.ProcessName -eq 'java'} | Select-Object Name, CPU, WorkingSet
```

---

## Create Convenience Script (Optional)

Create a file `start-backend.ps1`:

```powershell
# Starting IntiCivi Backend

Write-Host "================================" -ForegroundColor Cyan
Write-Host "IntiCivi Backend Startup" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

# Check Java
Write-Host ""
Write-Host "Checking Java..." -ForegroundColor Yellow
java -version

# Check Maven
Write-Host ""
Write-Host "Checking Maven..." -ForegroundColor Yellow
mvn --version

# Navigate to backend
Write-Host ""
Write-Host "Navigating to backend..." -ForegroundColor Yellow
cd "C:\Users\aravi\Downloads\IntiCiviProject\IntiCivi\backend_website"

# Start backend
Write-Host ""
Write-Host "Starting backend..." -ForegroundColor Green
Write-Host "(First run will take 2-3 minutes, subsequent runs 30-60 seconds)" -ForegroundColor Gray
Write-Host ""

mvn clean spring-boot:run
```

Then run:
```powershell
.\start-backend.ps1
```

---

## Next Steps

1. ✅ Verify Java 17 installed
2. ✅ Verify Maven installed
3. ✅ Create MongoDB Atlas cluster
4. ✅ Update .env with credentials
5. ✅ Run: `mvn clean spring-boot:run`
6. ✅ Test with: `.\test-api.ps1`
7. → Integrate with React frontend
8. → Deploy to production

---

## Quick Reference

| Command | Purpose |
|---------|---------|
| `cd backend_website` | Change to backend folder |
| `mvn clean spring-boot:run` | Start backend |
| `.\test-api.ps1` | Test all APIs |
| `Ctrl+C` | Stop backend |
| `java -version` | Check Java version |
| `mvn --version` | Check Maven version |
| `Get-Content .\.env` | View .env file |

---

## Success!

When you see this, you're ready:
```
✓ Tomcat started on port(s): 8080
✓ MongoDB sample data loaded successfully!
✓ Started IntiCiviApplication
```

You're all set! 🚀
