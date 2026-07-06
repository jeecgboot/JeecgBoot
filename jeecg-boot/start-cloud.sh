#!/bin/bash
#=============================================================================
# JeecgBoot 微服务启动脚本
# 适用环境: Windows Git Bash / JDK 17+ / Maven
#
# Maven Profiles: SpringCloud + dev (对应 IDEA Maven 面板勾选的两项)
#
# 启动顺序: Nacos(8848) -> System(7001) + Demo(7002) + Gateway(9999)
#
# 用法:
#   ./start-cloud.sh              # 先构建再启动所有服务 (默认)
#   ./start-cloud.sh start        # 跳过构建，直接启动 (使用已有 jar)
#   ./start-cloud.sh build        # 仅构建，不启动
#   ./start-cloud.sh stop         # 停止所有服务 (按端口杀进程)
#   ./start-cloud.sh status       # 查看各端口占用状态
#=============================================================================

set -e

# 自动推断: 脚本所在目录即项目根目录
PROJ_ROOT="$(cd "$(dirname "$0")" && pwd)"
CLOUD_DIR="$PROJ_ROOT/jeecg-server-cloud"

# Maven 激活的 profile (对应 IDEA Maven 面板勾选的两项)
MAVEN_PROFILES="SpringCloud,dev"

# 从 pom.xml 读取版本号 (失败时用默认值)
VERSION=$(grep -oPm1 '<version>\K[^<]+' "$PROJ_ROOT/pom.xml" 2>/dev/null | head -1)
[ -z "$VERSION" ] && VERSION="3.9.2"

# 服务配置: 名称|端口|模块目录
SERVICES=(
	"Nacos|8848|jeecg-cloud-nacos"
	"System|7001|jeecg-system-cloud-start"
	"Demo|7002|jeecg-demo-cloud-start"
	"Gateway|9999|jeecg-cloud-gateway"
)

# ANSI 颜色
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m'

info()  { echo -e "${GREEN}[INFO]${NC}  $1"; }
warn()  { echo -e "${YELLOW}[WARN]${NC}  $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; }
step()  { echo -e "${CYAN}[STEP]${NC}  $1"; }

#-----------------------------------------------------------------------------
# 路径转换: F:/xxx/yyy -> F:\xxx\yyy (cmd.exe 格式)
#-----------------------------------------------------------------------------
to_win_path() {
	echo "$1" | sed 's|/|\\|g'
}

#-----------------------------------------------------------------------------
# 通过端口查进程 PID
#-----------------------------------------------------------------------------
pid_by_port() {
	local port=$1
	netstat -ano 2>/dev/null | grep ":$port " | grep -i LISTENING | awk '{print $NF}' | head -1
}

#-----------------------------------------------------------------------------
# 等待 Nacos 健康检查就绪
#-----------------------------------------------------------------------------
wait_for_nacos() {
	local port=${1:-8848}
	local max_wait=${2:-90}
	local count=0

	info "等待 Nacos (端口 $port) 启动..."
	while [ $count -lt $max_wait ]; do
		local status
		status=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:$port/nacos/v1/console/health/readiness" 2>/dev/null || echo "000")
		if [ "$status" = "200" ]; then
			info "Nacos (端口 $port) 已就绪"
			return 0
		fi
		sleep 3
		count=$((count + 3))
		echo -n "."
	done
	echo ""
	error "Nacos (端口 $port) 启动超时 (${max_wait}s)"
	return 1
}

#-----------------------------------------------------------------------------
# 启动单个服务 (在独立 cmd 窗口中运行)
#-----------------------------------------------------------------------------
start_service() {
	local name=$1
	local port=$2
	local module=$3
	local module_dir="$CLOUD_DIR/$module"

	# 检查是否已在运行
	local existing_pid
	existing_pid=$(pid_by_port "$port")
	if [ -n "$existing_pid" ]; then
		warn "$name (端口 $port) 已在运行 (PID: $existing_pid)，跳过"
		return 0
	fi

	# 检查 jar 是否存在
	local jar_file="$module_dir/target/${module}-${VERSION}.jar"
	if [ ! -f "$jar_file" ]; then
		error "$name jar 不存在: $jar_file"
		error "请先执行: ./start-cloud.sh build"
		return 1
	fi

	local win_dir
	win_dir=$(to_win_path "$module_dir")

	info "启动 $name (端口 $port) ..."
	# 在独立 cmd 窗口中启动 java -jar
	cmd //c start "$name:$port" cmd /k "title $name:$port && cd /d $win_dir && java -jar target\\${module}-${VERSION}.jar && pause" 2>/dev/null

	sleep 1
}

#-----------------------------------------------------------------------------
# 停止单个服务 (按端口杀进程)
#-----------------------------------------------------------------------------
stop_service() {
	local name=$1
	local port=$2
	local pid

	pid=$(pid_by_port "$port")
	if [ -n "$pid" ]; then
		info "停止 $name (端口 $port, PID: $pid)..."
		taskkill //F //PID "$pid" 2>/dev/null || true
		sleep 1
	else
		info "$name (端口 $port) 未运行"
	fi
}

#-----------------------------------------------------------------------------
# 预检: Java + Maven
#-----------------------------------------------------------------------------
preflight() {
	if ! command -v java &>/dev/null; then
		error "未找到 java，请安装 JDK 17+ 并配置 JAVA_HOME"
		exit 1
	fi
	if ! command -v mvn &>/dev/null; then
		error "未找到 mvn，请安装 Maven 并配置 PATH"
		exit 1
	fi
	local jver
	jver=$(java -version 2>&1 | head -1 | grep -oP '"\K[^"]+')
	info "Java: $jver"
	info "Maven: $(mvn --version 2>&1 | head -1)"
}

#-----------------------------------------------------------------------------
# 构建
#-----------------------------------------------------------------------------
do_build() {
	preflight
	step "开始 Maven 构建 (profiles: $MAVEN_PROFILES)..."

	cd "$PROJ_ROOT"

	# 1. 先单独构建 Nacos (Spring Boot 2.7.18, 与主项目不同)
	step "[1/2] 构建 Nacos ..."
	mvn clean package -DskipTests -f "$CLOUD_DIR/jeecg-cloud-nacos/pom.xml"

	# 2. 构建其余微服务模块 (Spring Boot 3.5.5)
	step "[2/2] 构建云模块 ..."
	mvn clean package -P "$MAVEN_PROFILES" -DskipTests

	info "构建完成"
}

#-----------------------------------------------------------------------------
# 启动所有服务
#-----------------------------------------------------------------------------
do_start() {
	step "启动微服务..."

	# 1. 先启动 Nacos
	start_service "Nacos" "8848" "jeecg-cloud-nacos"

	# 2. 等待 Nacos 就绪 (其他服务依赖 Nacos 注册中心)
	wait_for_nacos "8848" 120 || {
		error "Nacos 启动失败，终止后续服务"
		exit 1
	}

	# 3. Nacos 就绪后启动其余服务
	start_service "System"  "7001" "jeecg-system-cloud-start"
	sleep 1
	start_service "Demo"    "7002" "jeecg-demo-cloud-start"
	sleep 1
	start_service "Gateway" "9999" "jeecg-cloud-gateway"

	echo ""
	info "============================================"
	info "  Nacos   -> http://localhost:8848/nacos"
	info "  System  -> http://localhost:7001"
	info "  Demo    -> http://localhost:7002"
	info "  Gateway -> http://localhost:9999"
	info "============================================"
}

#-----------------------------------------------------------------------------
# 停止所有服务
#-----------------------------------------------------------------------------
do_stop() {
	step "停止所有微服务..."
	for svc in "${SERVICES[@]}"; do
		IFS='|' read -r name port module <<< "$svc"
		stop_service "$name" "$port"
	done
	info "已全部停止"
}

#-----------------------------------------------------------------------------
# 查看状态
#-----------------------------------------------------------------------------
do_status() {
	echo ""
	printf "  %-10s %-10s %-10s %s\n" "端口" "服务" "状态" "PID"
	printf "  %-10s %-10s %-10s %s\n" "----------" "----------" "----------" "--------"
	for svc in "${SERVICES[@]}"; do
		IFS='|' read -r name port module <<< "$svc"
		local pid
		pid=$(pid_by_port "$port")
		if [ -n "$pid" ]; then
			printf "  %-10s %-10s ${GREEN}%-10s${NC} %s\n" "$port" "$name" "RUNNING" "$pid"
		else
			printf "  %-10s %-10s ${RED}%-10s${NC} -\n" "$port" "$name" "STOPPED"
		fi
	done
	echo ""
}

#=============================================================================
# 主入口
#=============================================================================
case "${1:-}" in
	build)
		do_build
		;;
	start)
		do_start
		;;
	stop)
		do_stop
		;;
	status)
		do_status
		;;
	restart)
		do_stop
		sleep 2
		do_start
		;;
	"")
		# 默认: 构建 + 启动
		do_build
		do_start
		;;
	*)
		echo "用法: $0 [build|start|stop|restart|status]"
		echo ""
		echo "  (无参数)  构建并启动所有微服务"
		echo "  build     仅构建, 不启动"
		echo "  start     跳过构建, 直接启动 (使用已有 jar)"
		echo "  stop      停止所有服务 (按端口杀进程)"
		echo "  restart   重启所有服务"
		echo "  status    查看各端口占用状态"
		exit 1
		;;
esac
