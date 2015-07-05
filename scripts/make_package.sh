#!/bin/bash

app_name=mastermind

pwd &&
cd ../target/jfx/app/ &&
mv -v ${app_name}-jfx.jar ${app_name}.jar &&
java -jar ${app_name}.jar -version &&
mkdir $(< app_full_name) &&
cp -rv ${app_name}.jar ../../../scripts/update_app.sh lib/ $(< app_full_name)/ &&
tar zcvf $(< app_full_name).tar.gz $(< app_full_name)/ &&

cd ../../../scripts/ &&
echo $(< ~/pass) | sudo -S ./deploy.sh
