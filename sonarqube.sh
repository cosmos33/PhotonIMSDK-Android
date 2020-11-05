#!/usr/bin/env bash
#! 调用该脚本扫描代码
./gradlew  sonarqube \
-Dsonar.projectKey=android_im_demo \
-Dsonar.host.url=https://console.wemomo.com/sonar \
-Dsonar.login=88d65bb75a2389417422570b0668621ffcb8083b \
-Dsonar.projectName=android_im_demo