#!/bin/bash

docker-compose up -d

sleep 5

chmod +x /scripts/rs-init.sh

docker exec mongo-db-primary /scripts/rs-init.sh