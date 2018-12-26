#!/bin/sh
git pull
mvn clean package -Dmaven.test.skip=true
APP_PATH=/root/data
APP_NAME=platform-framework
rm -rf ${APP_PATH}/${APP_NAME}.jar
echo "rm ${TOMCAT_PATH}/${APP_NAME}.jar"
rm -rf ${APP_PATH}/${APP_NAME}
echo "rm ${APP_PATH}/${APP_NAME}"
cp ./platform-shop/target/platform-shop-1.0.0.jar ${APP_PATH}/${APP_NAME}.jar
