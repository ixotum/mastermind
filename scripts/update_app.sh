#!/usr/bin/env bash

app_name=mastermind

tar xvf ${app_name}*.tar.gz &&
rm ${app_name}*.tar.gz &&
cd ${app_name}*/ &&
cp -rv * ../ &&
rm -rv * &&
cd ../ &&
rmdir ${app_name}*/ &&
java -jar ${app_name}.jar