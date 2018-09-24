#!/bin/sh
TOMCAT_PATH=/usr/local/share/apache-tomcat-8.0.53/webapps
APP_NAME=platform-framework
rm -rf ${APP_PATH}/${APP_NAME}.war
rm -rf ${APP_PATH}/${APP_NAME}
cp ./platform-shop/target/platform-shop-1.0.0.war ${TOMCAT_PATH}/${APP_NAME}.war