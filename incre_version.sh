#!/usr/bin/env bash
mvn versions:set -DnewVersion=$1
git add pom.xml
git commit -m "build: version $1"
git push origin master
git tag -a $1 -m $1
git push origin $1