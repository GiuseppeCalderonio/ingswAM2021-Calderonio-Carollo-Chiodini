reg add hkcu\console /f /v VirtualTerminalLevel /t REG_DWORD /d 1
cd desktop
java -jar AM30.jar localClient 0 null -CLI