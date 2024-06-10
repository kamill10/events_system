##!/bin/bash
#
#docker compose down
#
#rm -rf ./html
#mkdir ./html
#rm ./ssbd01.war
#
#mvn -f ../pom.xml clean install -DskipTests
#cp ../target/ssbd01.war ./ssbd01.war
#
#echo 'VITE_API_URL=http://localhost:80/api' > ../ssbd202401web/.env && npm install ../ssbd202401web && npm run build ../ssbd202401web
#
#echo 'VITE_API_URL=https://team-1.proj-sum.it.p.lodz.pl/api' > ../ssbd202401web/.env
#
#cp -r ../ssbd202401web/build ../docker-local/html
#
#docker compose up --build




