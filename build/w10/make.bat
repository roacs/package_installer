javac -d . ../../*.java
jar cmf Manifest package_installer.jar *.class
del *.class