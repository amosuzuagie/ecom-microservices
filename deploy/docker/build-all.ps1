# build-all.ps1

$services = @(
    "configserver",
    "notification",
    "product",
    "gateway",
    "eureka",
    "order",
    "user"
)

cd ../..

foreach ($service in $services) {
    Write-Host "Building $service..."
    Push-Location $service
    ./mvnw clean package -DskipTests
    Pop-Location
}

# Open PowerShell as Administrator
# Run: ./build-all.ps1
# cd /mnt/c/Users/YourUsername/Desktop