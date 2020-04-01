javac -d . ../../*.java
jar cmf Manifest package_installer.jar *.class
del *.class

copy /Y package_installer.jar C:\Users\archo\Documents\Work\CM\rt\releases\suite
copy /Y run.bat C:\Users\archo\Documents\Work\CM\rt\releases\suite\install.bat