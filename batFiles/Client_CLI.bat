reg add hkcu\console /f /v VirtualTerminalLevel /t REG_DWORD /d 1
cd desktop
java -jar AM30.jar client 1234 151.20.45.27 -CLI