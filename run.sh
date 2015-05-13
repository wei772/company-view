#!/bin/sh
mvn clean
mvn compile
mvn war:war
mvn jetty:run-war -Djetty.port=3389