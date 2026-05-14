#!/bin/bash
echo "🔄 Reiniciando calendar-service..."
echo "📦 Limpiando y compilando..."
mvn clean package -DskipTests
echo "🚀 Iniciando servicio en puerto 8086..."
mvn spring-boot:run
