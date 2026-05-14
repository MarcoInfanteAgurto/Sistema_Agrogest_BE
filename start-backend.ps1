# =============================================
# AgroGest - Script de inicio del Backend
# Carga el .env global y lanza todos los servicios
# =============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Iniciando Backend AgroGest..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$base = Split-Path -Parent $MyInvocation.MyCommand.Path

# ─── Cargar .env global ───────────────────────────────────────────────────────
$envFile = Join-Path $base ".env"
if (Test-Path $envFile) {
    Write-Host ""
    Write-Host "Cargando credenciales desde .env global..." -ForegroundColor DarkGray
    Get-Content $envFile | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#")) {
            $parts = $line -split "=", 2
            if ($parts.Count -eq 2) {
                $key   = $parts[0].Trim()
                $value = $parts[1].Trim()
                [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
            }
        }
    }
    Write-Host "Credenciales cargadas correctamente." -ForegroundColor Green
} else {
    Write-Host "ADVERTENCIA: No se encontro el archivo .env en la raiz del proyecto." -ForegroundColor Red
    Write-Host "  Crea el archivo: $envFile" -ForegroundColor Yellow
    Write-Host "  (Usa .env.example como plantilla)" -ForegroundColor Yellow
    pause
    exit 1
}

# ─── Variables compartidas ────────────────────────────────────────────────────
$dbHost = $env:DB_HOST
$dbUser = $env:SPRING_DATASOURCE_USERNAME
$dbPass = $env:SPRING_DATASOURCE_PASSWORD

# ─── Función para iniciar un microservicio ────────────────────────────────────
function Start-MicroService {
    param(
        [string]$Name,
        [string]$Path,
        [int]$Port,
        [string]$DbName = ""
    )

    Write-Host ""
    Write-Host ">> Iniciando $Name (puerto $Port)..." -ForegroundColor Green

    # Construir URL específica para este servicio
    $datasourceUrl = ""
    if ($DbName -ne "") {
        $datasourceUrl = "jdbc:postgresql://${dbHost}/${DbName}?sslmode=require&channel_binding=require"
    }

    $envBlock = @"
`$env:SPRING_DATASOURCE_USERNAME = '$dbUser'
`$env:SPRING_DATASOURCE_PASSWORD = '$dbPass'
`$env:KAFKA_BOOTSTRAP_SERVERS    = '$($env:KAFKA_BOOTSTRAP_SERVERS)'
`$env:WEATHER_API_KEY             = '$($env:WEATHER_API_KEY)'
`$env:WEATHER_API_URL             = '$($env:WEATHER_API_URL)'
"@
    if ($datasourceUrl -ne "") {
        $envBlock += "`n`$env:SPRING_DATASOURCE_URL = '$datasourceUrl'"
    }

    $script = @"
Write-Host '=== $Name ===' -ForegroundColor Cyan
Set-Location '$Path'
$envBlock
Write-Host 'Compilando $Name...' -ForegroundColor Yellow
mvn clean package -DskipTests=true
if (`$LASTEXITCODE -eq 0) {
    Write-Host 'Iniciando en puerto $Port...' -ForegroundColor Green
    mvn spring-boot:run
} else {
    Write-Host 'ERROR: Fallo en compilacion de $Name' -ForegroundColor Red
    pause
}
"@

    Start-Process powershell -ArgumentList "-NoExit", "-Command", $script
    Start-Sleep -Seconds 3
}

# ─── Iniciar cada microservicio ───────────────────────────────────────────────
Write-Host ""
Write-Host "Iniciando microservicios..." -ForegroundColor Yellow

Start-MicroService "API Gateway"           "$base\api-gateway\api-gateway"          8080
Start-MicroService "User Service"          "$base\user-service\user-service"         8081  "userdb"
Start-MicroService "Parcel Service"        "$base\parcel-service\parcel-service"     8082  "parceldb"
Start-MicroService "Crop Service"          "$base\crop-service"                      8083  "cropdb"
Start-MicroService "Activity Service"      "$base\activity-service"                  8084  "activitydb"
Start-MicroService "Notification Service"  "$base\notification-service"              8085  "notificationdb"
Start-MicroService "Calendar Service"      "$base\calendar-service"                  8086  "calendardb"
Start-MicroService "Weather Service"       "$base\weather-service"                   8087  "weatherdb"

# ─── Resumen ──────────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Todos los servicios iniciandose..." -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "URLs disponibles:" -ForegroundColor Yellow
Write-Host "  API Gateway:          http://localhost:8080" -ForegroundColor White
Write-Host "  User Service:         http://localhost:8081" -ForegroundColor White
Write-Host "  Parcel Service:       http://localhost:8082" -ForegroundColor White
Write-Host "  Crop Service:         http://localhost:8083" -ForegroundColor White
Write-Host "  Activity Service:     http://localhost:8084" -ForegroundColor White
Write-Host "  Notification Service: http://localhost:8085" -ForegroundColor White
Write-Host "  Calendar Service:     http://localhost:8086" -ForegroundColor White
Write-Host "  Weather Service:      http://localhost:8087" -ForegroundColor White
Write-Host ""