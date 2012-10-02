@echo off
SET LIBRARY_PATH=-Djava.library.path=lib\native
SET CLIENT=betaclient.jar
SET UPDATER=updater.jar

IF EXIST "%ProgramFiles%\Java\jre6" goto Java1.6
IF EXIST "%ProgramFiles(x86)%\Java\jre6" goto Java1.6x32
IF EXIST "%ProgramFiles%\Java\jre7" goto Java1.7
IF EXIST "%ProgramFiles(x86)%\Java\jre7" goto Java1.76x32

:Java1.7	
"%ProgramFiles%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %UPDATER%
"%ProgramFiles%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %CLIENT%
Goto End

:Java1.6
"%ProgramFiles%\Java\jre6\bin\java.exe" %LIBRARY_PATH% -jar %UPDATER%
"%ProgramFiles%\Java\jre6\bin\java.exe" %LIBRARY_PATH% -jar %CLIENT%
Goto End

:Java1.7x32
"%ProgramFiles(x86)%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %UPDATER%
"%ProgramFiles(x86)%\Java\jre7\bin\java.exe" %LIBRARY_PATH% -jar %CLIENT%
Goto End

:Java1.6x32
"%ProgramFiles(x86)%\Java\jre6\bin\java.exe" %LIBRARY_PATH% -jar %UPDATER%
"%ProgramFiles(x86)%\Java\jre6\bin\java.exe" %LIBRARY_PATH% -jar %CLIENT%
Goto End

:End
Pause