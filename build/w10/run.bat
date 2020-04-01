@echo off

set VERSION=w10
set PACKAGE_NAME=suite
set DEPENDENCIES=databases gps_leap_seconds
set INSTALL_DEFAULT=C:\Users\archo\Documents\Work\CM\rt\install_location

java -jar package_installer.jar "%VERSION%" "%PACKAGE_NAME%" "%DEPENDENCIES%" "%INSTALL_DEFAULT%"