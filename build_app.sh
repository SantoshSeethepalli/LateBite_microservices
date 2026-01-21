#!/bin/bash

set -e

mvn clean package -DskipTests

docker build -t discovery-server ./discovery-server
docker build -t api-gateway ./api-gateway
docker build -t auth-service ./auth-service
docker build -t user-service ./user-service
docker build -t restaurant-service ./restaurant-service
docker build -t cart-service ./cart-service
docker build -t order-service ./order-service
docker build -t payment-service ./payment-service

docker compose up
