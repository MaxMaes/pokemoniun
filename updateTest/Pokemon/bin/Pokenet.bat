@ECHO OFF
SET BINDIR=%~dp0
CD /D "%BINDIR%"
"%ProgramFiles%\Java\jre6\bin\java.exe" -Djava.library.path=lib/native -jar Pokenet.jar
