@echo off
:: JEECG Boot 一键启动脚本 (Windows CMD 版)
chcp 65001 > nul
set RED=31
set GREEN=32

echo.
echo [1/5] 检查必要工具...
where docker > nul 2>&1 || (
    echo [错误] 未安装 docker，请先安装 Docker Desktop
    pause
    exit /b 1
)
where docker-compose > nul 2>&1 || (
    echo [错误] 未安装 docker-compose
    pause
    exit /b 1
)
where mvn > nul 2>&1 || (
    echo [错误] 未安装 Maven
    pause
    exit /b 1
)
where pnpm > nul 2>&1 || (
    echo [错误] 未安装 pnpm
    pause
    exit /b 1
)

echo [2/5] 设置 hosts 文件...
set "entry1=127.0.0.1   jeecg-boot-gateway"
set "entry2=127.0.0.1   jeecg-boot-mysql"
set "hostsFile=C:\Windows\System32\drivers\etc\hosts"

rem 检查第一个条目是否存在
findstr /c:"%entry1%" "%hostsFile%" >nul
if errorlevel 1 (
    echo %entry1% >> "%hostsFile%"
    echo 已添加: %entry1%
) else (
    echo 已存在: %entry1%
)

rem 检查第二个条目是否存在
findstr /c:"%entry2%" "%hostsFile%" >nul
if errorlevel 1 (
    echo %entry2% >> "%hostsFile%"
    echo 已添加: %entry2%
) else (
    echo 已存在: %entry2%
)

if %errorlevel% neq 0 (
    echo [错误] 设置 hosts 文件失败，请检查权限！
    pause
)

echo [3/5] 编译后端项目...
cd jeecg-boot
call mvn clean install -Pdev,SpringCloud
if %errorlevel% neq 0 (
    echo [错误] 后端编译失败！
    pause
    exit /b 1
)
cd ..

echo [4/5] 编译前端项目...
cd jeecgboot-vue3
call pnpm install
if %errorlevel% neq 0 (
    echo [错误] 前端依赖安装失败！
    pause
    exit /b 1
)
call pnpm run build:dockercloud
if %errorlevel% neq 0 (
    echo [错误] 前端编译失败！
    pause
    exit /b 1
)
cd ..

echo [5/5] 启动Docker容器...
docker-compose -f docker-compose-cloud.yml up -d

echo.
echo ========================================
echo   JEECG Boot 启动成功 (请等待1分钟，待所有容器启动成功）
echo ========================================
echo 前端访问:      http://localhost
echo 后端API:      http://localhost:9999
echo.
pause