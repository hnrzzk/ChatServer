@echo off
java -Xmx2g -cp %~dp0/../libs/* com.prefect.chatserver.server.ChatServer
pause