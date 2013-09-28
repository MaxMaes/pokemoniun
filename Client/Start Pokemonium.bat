@echo off
SET LIBRARY_PATH=-Djava.library.path=lib\native
SET CLIENT=betaclient.jar
SET UPDATER=updater.jar

IF EXIST "%ProgramFiles%\Java\jre7" goto Java1.7
IF EXIST "%ProgramFiles(x86)%\Java\jre7" goto Java1.7x32
IF EXIST "%ProgramFiles%\Java\jre6" goto Unsupported
IF EXIST "%ProgramFiles(x86)%\Java\jre6" goto Unsupported

:Java1.7	
"%ProgramFiles%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %UPDATER%
msg * "The updater has been updated, please restart the game!"
IF errorlevel 1 Goto Stop
"%ProgramFiles%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %CLIENT%
Goto Wait

:Java1.7x32
"%ProgramFiles(x86)%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %UPDATER%
msg * "The updater has been updated, please restart the game!"
IF errorlevel 1 Goto Stop
"%ProgramFiles(x86)%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %CLIENT%
Goto Wait

:Unsupported
msg * "This version of Java is unsupported, please download Java 7!"
Goto Stop

:Wait
Pause

:Stop