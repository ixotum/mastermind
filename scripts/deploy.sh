#!/usr/bin/env bash

cd ../target/jfx/app/ &&
cp -v $(< app_full_name).tar.gz /srv/ftp/pub/
