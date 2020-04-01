#!/bin/bash

VERSION=el6
PACKAGE_NAME=suite
DEPENDENCIES=databases gps_leap_seconds
INSTALL_DEFAULT=/opt/rt

java -jar package_installer.jar "%VERSION%" "%PACKAGE_NAME%" "%DEPENDENCIES%" "%INSTALL_DEFAULT%"