@echo off

for /f %%a in (RUNNING_PID) do (
  taskkill /F /PID %%a
  DEL /F RUNNING_PID
  play start
  exit /b
)


