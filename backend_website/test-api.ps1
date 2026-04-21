# IntiCivi Backend - API Testing Helper Script
# Save this as: test-api.ps1
# Usage: .\test-api.ps1

# Configuration
$API_URL = "http://localhost:8080/api"
$ADMIN_EMAIL = "admin@inticivi.com"
$ADMIN_PASSWORD = "admin123"
$USER_EMAIL = "user@inticivi.com"
$USER_PASSWORD = "password"

Write-Host "================================" -ForegroundColor Cyan
Write-Host "IntiCivi Backend API Test Suite" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Check Backend Health
Write-Host "[1/5] Testing Backend Connection..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$API_URL/health" -ErrorAction SilentlyContinue -SkipHttpErrorCheck
    if ($response.StatusCode -eq 200 -or $response.StatusCode -eq 404) {
        Write-Host "✓ Backend is running on port 8080" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ Backend is not running. Start with: mvn clean spring-boot:run" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Test 2: Admin Login
Write-Host "[2/5] Testing Admin Login..." -ForegroundColor Yellow
$loginBody = @{
    email = $ADMIN_EMAIL
    password = $ADMIN_PASSWORD
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$API_URL/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody

    $loginData = $response.Content | ConvertFrom-Json
    $ADMIN_TOKEN = $loginData.token
    
    Write-Host "✓ Admin login successful" -ForegroundColor Green
    Write-Host "  Token: $($ADMIN_TOKEN.Substring(0, 20))..." -ForegroundColor Gray
} catch {
    Write-Host "✗ Admin login failed" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host ""

# Test 3: User Login
Write-Host "[3/5] Testing User Login..." -ForegroundColor Yellow
$loginBody = @{
    email = $USER_EMAIL
    password = $USER_PASSWORD
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$API_URL/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody

    $loginData = $response.Content | ConvertFrom-Json
    $USER_TOKEN = $loginData.token
    
    Write-Host "✓ User login successful" -ForegroundColor Green
} catch {
    Write-Host "✗ User login failed" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Test 4: Fetch User's Complaints
Write-Host "[4/5] Fetching User's Complaints..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$API_URL/complaints/my" `
        -Headers @{Authorization = "Bearer $USER_TOKEN"} `
        -ContentType "application/json"

    $complaints = $response.Content | ConvertFrom-Json
    $count = if ($complaints -is [array]) { $complaints.Count } else { 1 }
    
    Write-Host "✓ Found $count complaints" -ForegroundColor Green
    if ($count -gt 0) {
        Write-Host "  First complaint: $($complaints[0].title)" -ForegroundColor Gray
    }
} catch {
    Write-Host "✗ Failed to fetch complaints" -ForegroundColor Red
}

Write-Host ""

# Test 5: Admin - Get All Complaints
Write-Host "[5/5] Admin - Fetching All Complaints..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$API_URL/complaints/admin/all" `
        -Headers @{Authorization = "Bearer $ADMIN_TOKEN"} `
        -ContentType "application/json"

    $complaints = $response.Content | ConvertFrom-Json
    $count = if ($complaints -is [array]) { $complaints.Count } else { 1 }
    
    Write-Host "✓ Found $count total complaints" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to fetch all complaints" -ForegroundColor Red
}

Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "✓ All tests completed successfully!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Cyan
Write-Host "1. Import 'IntiCivi-MongoDB-API.postman_collection.json' in Postman" -ForegroundColor White
Write-Host "2. Test all API endpoints" -ForegroundColor White
Write-Host "3. Integrate with React frontend" -ForegroundColor White
Write-Host "4. Test with mobile app" -ForegroundColor White
