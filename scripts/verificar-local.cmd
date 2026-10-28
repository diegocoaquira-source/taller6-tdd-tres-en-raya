@echo off
setlocal EnableDelayedExpansion
rem Alternativa JDK 17 sin cambiar politicas de PowerShell ni instalar software.
set "TASK_SELECTED="
if /I "%~1"=="--guiados" (
  set "TASK_SELECTED=com.ucb.tdd.TresEnRayaGuiadosTest"
  shift /1
)
if /I "%~1"=="--base" (
  set "TASK_SELECTED=com.ucb.tdd.TresEnRayaBaseTest"
  shift /1
)
if /I "%~1"=="--ia" (
  set "TASK_SELECTED=com.ucb.tdd.TresEnRayaIATest"
  shift /1
)
if /I "%~1"=="--ambas" (
  set "TASK_SELECTED=com.ucb.tdd.TresEnRayaBaseTest com.ucb.tdd.TresEnRayaIATest"
  shift /1
)
set "PROJECT=%~dp0.."
set "TASK_JDK=%JAVA_HOME%"
if "%TASK_JDK%"=="" for /d %%D in ("%ProgramFiles%\JetBrains\PyCharm*") do if exist "%%D\jbr\bin\javac.exe" set "TASK_JDK=%%D\jbr"
set "TASK_JUNIT=%JUNIT_JAR%"
if "%TASK_JUNIT%"=="" for /d %%D in ("%ProgramFiles%\JetBrains\PyCharm*") do if exist "%%D\lib\junit4.jar" set "TASK_JUNIT=%%D\lib\junit4.jar"
if not "%~1"=="" set "TASK_JDK=%~1"
if not "%~2"=="" set "TASK_JUNIT=%~2"
if not exist "%TASK_JDK%\bin\javac.exe" (
  echo No se encontro el JDK. Indique su carpeta como primer argumento.
  exit /b 1
)
if not exist "%TASK_JUNIT%" (
  echo No se encontro JUnit. Indique el JAR como segundo argumento.
  exit /b 1
)
set "BUILD=%PROJECT%\.local-build\cmd"
if not exist "%BUILD%" mkdir "%BUILD%"
> "%BUILD%\sources.txt" (
  for /R "%PROJECT%\src\main\java" %%F in (*.java) do (
    set "TASK_SOURCE=%%~fF"
    echo "!TASK_SOURCE:\=/!"
  )
  for /R "%PROJECT%\src\test\java" %%F in (*.java) do (
    set "TASK_SOURCE=%%~fF"
    echo "!TASK_SOURCE:\=/!"
  )
)
"%TASK_JDK%\bin\javac.exe" -version
echo JUnit local, distinto del 4.13 exacto solicitado en pom.xml:
"%TASK_JDK%\bin\java.exe" -cp "%TASK_JUNIT%" junit.runner.Version
if errorlevel 1 exit /b 1
"%TASK_JDK%\bin\javac.exe" --release 8 -encoding UTF-8 -cp "%TASK_JUNIT%" -d "%BUILD%" @"%BUILD%\sources.txt"
if errorlevel 1 exit /b 1
> "%BUILD%\junit-args.txt" (
  echo -cp
  echo "%BUILD:\=/%;%TASK_JUNIT:\=/%"
  echo org.junit.runner.JUnitCore
  if defined TASK_SELECTED (
    echo %TASK_SELECTED%
  ) else (
    for /R "%PROJECT%\src\test\java" %%F in (*Test.java) do echo com.ucb.tdd.%%~nF
  )
)
"%TASK_JDK%\bin\java.exe" @"%BUILD%\junit-args.txt"
if errorlevel 1 exit /b 1
echo Verificacion local completada con JDK 17 y JUnit de PyCharm. No se ejecuto Maven.
endlocal
