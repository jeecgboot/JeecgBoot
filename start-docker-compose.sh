#!/bin/bash
# JEECG Boot 一键启动脚本 (Linux Bash 版)

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo
echo -e "[1/5] 检查必要工具..."

# 检查必要工具
command -v docker > /dev/null 2>&1 || { echo -e "${RED}[错误] 未安装 docker，请先安装 Docker Desktop${NC}"; exit 1; }
command -v docker-compose > /dev/null 2>&1 || { echo -e "${RED}[错误] 未安装 docker-compose${NC}"; exit 1; }
command -v mvn > /dev/null 2>&1 || { echo -e "${RED}[错误] 未安装 Maven${NC}"; exit 1; }
command -v pnpm > /dev/null 2>&1 || { echo -e "${RED}[错误] 未安装 pnpm${NC}"; exit 1; }

echo -e "[2/5] 设置 hosts 文件..."
entry1="127.0.0.1   jeecg-boot-system"
entry2="127.0.0.1   jeecg-boot-mysql"
hostsFile="/etc/hosts"

# 检查第一个条目是否存在
if ! grep -q "$entry1" "$hostsFile"; then
    echo "$entry1" | sudo tee -a "$hostsFile" > /dev/null
    echo "已添加: $entry1"
else
    echo "已存在: $entry1"
fi

# 检查第二个条目是否存在
if ! grep -q "$entry2" "$hostsFile"; then
    echo "$entry2" | sudo tee -a "$hostsFile" > /dev/null
    echo "已添加: $entry2"
else
    echo "已存在: $entry2"
fi

echo -e "[3/5] 编译后端项目..."
cd jeecg-boot
mvn clean install -Pdocker

echo -e "[4/5] 编译前端项目..."
cd ../jeecgboot-vue3
pnpm install
pnpm run build:docker

echo -e "[5/5] 启动Docker容器..."
docker-compose up -d

echo
echo "========================================"
echo "  JEECG Boot 启动成功 (请等待1分钟，待所有容器启动成功）"
echo "========================================"
echo "前端访问:      http://localhost"
echo "后端API:      http://localhost:8080/jeecg-boot"
echo
