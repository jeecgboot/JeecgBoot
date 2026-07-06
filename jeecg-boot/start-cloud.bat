@echo off
setlocal EnableExtensions EnableDelayedExpansion
chcp 65001 >nul

rem =============================================================================
rem JeecgBoot 微服务启动脚本，适用于 Windows CMD / PowerShell。
rem
rem Maven Profiles: SpringCloud + dev
rem 启动顺序: Nacos(8848)，然后启动 System(7001) + Demo(7002) + Gateway(9999)
rem
rem 用法:
rem   start-cloud.bat              先构建，再启动所有微服务
rem   start-cloud.bat start        跳过构建，直接启动已有 jar
rem   start-cloud.bat build        仅构建，不启动
rem   start-cloud.bat stop         按端口停止服务
rem   start-cloud.bat restart      停止后重新启动
rem   start-cloud.bat status       查看端口占用状态
rem =============================================================================

set "PROJ_ROOT=%~dp0"
if "%PROJ_ROOT:~-1%"=="\" set "PROJ_ROOT=%PROJ_ROOT:~0,-1%"
set "CLOUD_DIR=%PROJ_ROOT%\jeecg-server-cloud"
set "MAVEN_PROFILES=SpringCloud,dev"

set "VERSION="
for /f "usebackq tokens=2 delims=>" %%v in (`findstr /r /c:"^[ 	]*<version>[^<]*</version>" "%PROJ_ROOT%\pom.xml" 2^>nul`) do (
    if not defined VERSION for /f "tokens=1 delims=<" %%x in ("%%v") do set "VERSION=%%x"
)
if not defined VERSION set "VERSION=3.9.2"

set "ACTION=%~1"
if "%ACTION%"=="" (
    call :do_build
    if errorlevel 1 exit /b 1
    call :do_start
    exit /b !ERRORLEVEL!
)

if /I "%ACTION%"=="build" (
    call :do_build
    exit /b !ERRORLEVEL!
)
if /I "%ACTION%"=="start" (
    call :do_start
    exit /b !ERRORLEVEL!
)
if /I "%ACTION%"=="stop" (
    call :do_stop
    exit /b !ERRORLEVEL!
)
if /I "%ACTION%"=="restart" (
    call :do_stop
    timeout /t 2 /nobreak >nul
    call :do_start
    exit /b !ERRORLEVEL!
)
if /I "%ACTION%"=="status" (
    call :do_status
    exit /b !ERRORLEVEL!
)

call :usage
exit /b 1

:info
echo [INFO]  %~1
exit /b 0

:warn
echo [WARN]  %~1
exit /b 0

:error
echo [ERROR] %~1
exit /b 0

:step
echo [STEP]  %~1
exit /b 0

:pid_by_port
set "PID_FOUND="
for /f "tokens=5" %%p in ('netstat -ano 2^>nul ^| findstr /C:":%~1 " ^| findstr /I "LISTENING"') do (
    set "PID_FOUND=%%p"
    goto :pid_by_port_done
)
:pid_by_port_done
exit /b 0

:wait_for_nacos
set "NACOS_PORT=%~1"
set "MAX_WAIT=%~2"
set /a WAITED=0

call :info "等待 Nacos (端口 %NACOS_PORT%) 启动..."
:wait_for_nacos_loop
set "HTTP_STATUS=000"
for /f "delims=" %%s in ('curl -s -o nul -w "%%{http_code}" "http://localhost:%NACOS_PORT%/nacos/v1/console/health/readiness" 2^>nul') do (
    set "HTTP_STATUS=%%s"
)

if "%HTTP_STATUS%"=="200" (
    echo.
    call :info "Nacos (端口 %NACOS_PORT%) 已就绪"
    exit /b 0
)

if %WAITED% GEQ %MAX_WAIT% (
    echo.
    call :error "Nacos (端口 %NACOS_PORT%) 启动超时 (%MAX_WAIT%s)"
    exit /b 1
)

<nul set /p ".=."
timeout /t 3 /nobreak >nul
set /a WAITED+=3
goto :wait_for_nacos_loop

:start_service
set "SVC_NAME=%~1"
set "SVC_PORT=%~2"
set "SVC_MODULE=%~3"
set "MODULE_DIR=%CLOUD_DIR%\%SVC_MODULE%"
set "JAR_FILE=%MODULE_DIR%\target\%SVC_MODULE%-%VERSION%.jar"

call :pid_by_port "%SVC_PORT%"
if defined PID_FOUND (
    call :warn "%SVC_NAME% (端口 %SVC_PORT%) 已在运行 (PID: %PID_FOUND%)，跳过"
    exit /b 0
)

if not exist "%JAR_FILE%" (
    call :error "%SVC_NAME% jar 不存在: %JAR_FILE%"
    call :error "请先执行: start-cloud.bat build"
    exit /b 1
)

call :info "启动 %SVC_NAME% (端口 %SVC_PORT%) ..."
start "%SVC_NAME%:%SVC_PORT%" /D "%MODULE_DIR%" cmd /k "title %SVC_NAME%:%SVC_PORT% && java -jar target\%SVC_MODULE%-%VERSION%.jar & pause"
if errorlevel 1 exit /b 1

timeout /t 1 /nobreak >nul
exit /b 0

:stop_service
set "SVC_NAME=%~1"
set "SVC_PORT=%~2"

call :pid_by_port "%SVC_PORT%"
if defined PID_FOUND (
    call :info "停止 %SVC_NAME% (端口 %SVC_PORT%, PID: %PID_FOUND%)..."
    taskkill /F /PID %PID_FOUND% >nul 2>nul
    timeout /t 1 /nobreak >nul
) else (
    call :info "%SVC_NAME% (端口 %SVC_PORT%) 未运行"
)
exit /b 0

:preflight
where java >nul 2>nul
if errorlevel 1 (
    call :error "未找到 java，请安装 JDK 17+ 并配置 JAVA_HOME"
    exit /b 1
)

where mvn >nul 2>nul
if errorlevel 1 (
    call :error "未找到 mvn，请安装 Maven 并配置 PATH"
    exit /b 1
)

set "JAVA_VERSION="
for /f "delims=" %%j in ('java -version 2^>^&1 ^| findstr /I "version"') do (
    if not defined JAVA_VERSION set "JAVA_VERSION=%%j"
)

set "MAVEN_VERSION="
for /f "delims=" %%m in ('mvn --version 2^>^&1') do (
    if not defined MAVEN_VERSION set "MAVEN_VERSION=%%m"
)

call :info "Java: %JAVA_VERSION%"
call :info "Maven: %MAVEN_VERSION%"
exit /b 0

:do_build
call :preflight
if errorlevel 1 exit /b 1

call :step "开始 Maven 构建 (profiles: %MAVEN_PROFILES%)..."
pushd "%PROJ_ROOT%"
if errorlevel 1 exit /b 1

call :step "[1/2] 构建 Nacos ..."
call mvn clean package -DskipTests -f "%CLOUD_DIR%\jeecg-cloud-nacos\pom.xml"
if errorlevel 1 (
    set "BUILD_RC=!ERRORLEVEL!"
    popd
    exit /b !BUILD_RC!
)

call :step "[2/2] 构建云模块 ..."
call mvn clean package -P "%MAVEN_PROFILES%" -DskipTests
if errorlevel 1 (
    set "BUILD_RC=!ERRORLEVEL!"
    popd
    exit /b !BUILD_RC!
)

popd
call :info "构建完成"
exit /b 0

:do_start
call :step "启动微服务..."

call :start_service "Nacos" "8848" "jeecg-cloud-nacos"
if errorlevel 1 exit /b 1

call :wait_for_nacos "8848" "120"
if errorlevel 1 (
    call :error "Nacos 启动失败，终止后续服务"
    exit /b 1
)

call :start_service "System" "7001" "jeecg-system-cloud-start"
if errorlevel 1 exit /b 1
timeout /t 1 /nobreak >nul

call :start_service "Demo" "7002" "jeecg-demo-cloud-start"
if errorlevel 1 exit /b 1
timeout /t 1 /nobreak >nul

call :start_service "Gateway" "9999" "jeecg-cloud-gateway"
if errorlevel 1 exit /b 1

echo.
call :info "============================================"
call :info "  Nacos   -> http://localhost:8848/nacos"
call :info "  System  -> http://localhost:7001"
call :info "  Demo    -> http://localhost:7002"
call :info "  Gateway -> http://localhost:9999"
call :info "============================================"
exit /b 0

:do_stop
call :step "停止所有微服务..."
call :stop_service "Nacos" "8848"
call :stop_service "System" "7001"
call :stop_service "Demo" "7002"
call :stop_service "Gateway" "9999"
call :info "已全部停止"
exit /b 0

:print_status
set "STATUS_PORT=%~1"
set "STATUS_NAME=%~2"
call :pid_by_port "%STATUS_PORT%"
if defined PID_FOUND (
    echo   %STATUS_PORT%	%STATUS_NAME%	RUNNING	%PID_FOUND%
) else (
    echo   %STATUS_PORT%	%STATUS_NAME%	STOPPED	-
)
exit /b 0

:do_status
echo.
echo   端口	服务	状态	PID
echo   ----------	----------	----------	--------
call :print_status "8848" "Nacos"
call :print_status "7001" "System"
call :print_status "7002" "Demo"
call :print_status "9999" "Gateway"
echo.
exit /b 0

:usage
echo 用法: start-cloud.bat [build^|start^|stop^|restart^|status]
echo.
echo   ^(无参数^)  构建并启动所有微服务
echo   build     仅构建, 不启动
echo   start     跳过构建, 直接启动 ^(使用已有 jar^)
echo   stop      停止所有服务 ^(按端口杀进程^)
echo   restart   重启所有服务
echo   status    查看各端口占用状态
exit /b 0
