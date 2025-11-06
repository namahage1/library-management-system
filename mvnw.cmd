@rem ----------------------------------------------------------------------------
@rem Licensed to the Apache Software Foundation (ASF) under one
@rem or more contributor license agreements.  See the NOTICE file
@rem distributed with this work for additional information
@rem regarding copyright ownership.  The ASF licenses this file
@rem to you under the Apache License, Version 2.0 (the
@rem "License"); you may not use this file except in compliance
@rem with the License.  You may obtain a copy of the License at
@rem
@rem    https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing,
@rem software distributed under the License is distributed on an
@rem "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@rem KIND, either express or implied.  See the License for the
@rem specific language governing permissions and limitations
@rem under the License.
@rem ----------------------------------------------------------------------------

@rem ----------------------------------------------------------------------------
@rem Apache Maven Wrapper startup batch script, version 3.2.0
@rem
@rem Required ENV vars:
@rem JAVA_HOME - location of a JDK home dir
@rem
@rem Optional ENV vars
@rem MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@rem MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@rem MAVEN_OPTS - parameters passed to the Java VM when running Maven
@rem     e.g. to debug Maven itself, use
@rem set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@rem MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@rem ----------------------------------------------------------------------------

@rem Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off
@setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@rem Extension to allow automatically downloading the maven-wrapper.jar from Maven-central
@rem This allows using the maven wrapper in projects that prohibit checking in binary data.
if exist %WRAPPER_JAR% (
    if "%MVNW_VERBOSE%" == "true" (
        echo Found %WRAPPER_JAR%
    )
) else (
    if not "%MVNW_REPOURL%" == "" (
        SET DOWNLOAD_URL="%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    )
    if "%MVNW_VERBOSE%" == "true" (
        echo Couldn't find %WRAPPER_JAR%, downloading it ...
        echo Downloading from: %DOWNLOAD_URL%
    )

    powershell -Command "&{"^
		"$webclient = new-object System.Net.WebClient;"^
		"if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
		"$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
		"}"^
		"[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')"^
		"}"
    if "%MVNW_VERBOSE%" == "true" (
        echo Finished downloading %WRAPPER_JAR%
    )
)
@rem End of extension

@rem Provide a "standardized" way to retrieve the CLI args that will
@rem work with both Windows and non-Windows executions.
set MAVEN_CMD_LINE_ARGS=%*

%MAVEN_JAVA_EXE% ^
  %MAVEN_OPTS% ^
  %MAVEN_DEBUG_OPTS% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=%ERRORLEVEL%
if %MAVEN_VERBOSE% == true (
    echo Failed to execute Maven with args: %MAVEN_CMD_LINE_ARGS%
)
goto end

:end
endlocal & set ERROR_CODE=%ERROR_CODE%
if %MAVEN_SKIP_RC% == "" (
    if exist "%USERPROFILE%\.m2\mavenrc.cmd" (
        call "%USERPROFILE%\.m2\mavenrc.cmd"
    )
) & set ERROR_CODE=%ERROR_CODE%
exit /b %ERROR_CODE%


