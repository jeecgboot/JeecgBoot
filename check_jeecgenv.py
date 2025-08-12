import os
import subprocess
import re
import sys
from typing import Tuple, Optional

def run_command(cmd: str) -> Tuple[int, str]:
    """执行命令并返回退出码和输出"""
    try:
        result = subprocess.run(cmd, shell=True, check=False, 
                              stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                              text=True)
        return result.returncode, result.stdout.strip()
    except Exception as e:
        return -1, str(e)

def check_java() -> bool:
    """检查JDK 17+是否安装"""
    print("\n检查JDK 17+...")
    rc, output = run_command("java -version 2>&1")
    if rc != 0:
        print("❌ 未检测到Java，请安装JDK 17+")
        return False
    
    version_pattern = r'"(\d+)(?:\.\d+)*(?:_\d+)?'
    match = re.search(version_pattern, output)
    if not match:
        print("❌ 无法解析Java版本")
        return False
    
    version = int(match.group(1))
    if version >= 17:
        print(f"✅ JDK版本 {version} (满足17+要求)")
        return True
    else:
        print(f"❌ JDK版本 {version} (需要17+)")
        return False

def check_maven() -> bool:
    """检查Maven是否安装"""
    print("\n检查Maven...")
    rc, output = run_command("mvn -v")
    if rc == 0:
        print("✅ Maven已安装")
        return True
    else:
        print("❌ Maven未安装")
        return False

def check_node() -> bool:
    """检查Node.js 20+是否安装"""
    print("\n检查Node.js 20+...")
    rc, output = run_command("node -v")
    if rc != 0:
        print("❌ Node.js未安装")
        return False
    
    version_pattern = r'v(\d+)\.\d+\.\d+'
    match = re.search(version_pattern, output)
    if not match:
        print("❌ 无法解析Node.js版本")
        return False
    
    version = int(match.group(1))
    if version >= 20:
        print(f"✅ Node.js版本 {version} (满足20+要求)")
        return True
    else:
        print(f"❌ Node.js版本 {version} (需要20+)")
        return False

def check_pnpm() -> bool:
    """检查PNPM 9+是否安装"""
    print("\n检查PNPM 9+...")
    rc, output = run_command("pnpm -v")
    if rc != 0:
        print("❌ PNPM未安装")
        return False
    
    try:
        # 处理可能的版本号格式：v9.0.0 或 9.0.0 或 9
        version_str = output.strip().lstrip('v').split('.')[0]
        version = int(version_str)
        
        if version >= 9:
            print(f"✅ PNPM版本 {output.strip()} (满足9+要求)")
            return True
        else:
            print(f"❌ PNPM版本 {output.strip()} (需要9+)")
            return False
    except (ValueError, IndexError):
        print(f"❌ 无法解析PNPM版本: {output.strip()}")
        return False

def check_redis_connection() -> bool:
    """检查Redis连接"""
    print("\n检查Redis连接...")
    print("⚠️ 请确保已配置Redis连接信息并在jeecg-boot项目中正确配置")
    print("⚠️ 此检查需要根据实际项目配置进行验证")
    print("⚠️ 配置文件位置: jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/application-dev.yml")
    return True

def check_mysql_connection() -> bool:
    """检查MySQL连接"""
    print("\n检查MySQL连接...")
    print("⚠️ 请确保已配置MySQL连接信息并在jeecg-boot项目中正确配置")
    print("⚠️ 此检查需要根据实际项目配置进行验证")
    print("⚠️ 配置文件位置: jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/application-dev.yml")
    return True

def print_mysql_config():
    """打印MySQL配置并提示需要修改的位置"""
    print("\nMySQL配置参考 (请检查以下配置是否正确):")
    print("""
spring.datasource.dynamic.datasource:
  master:
    url: jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
    username: root       # ← 可能需要修改
    password: root       # ← 可能需要修改
    driver-class-name: com.mysql.cj.jdbc.Driver
""")

def check_ai_vector_db() -> bool:
    """检查AI向量库(pgvector)配置"""
    print("\n检查AI知识库向量库配置...")
    print("⚠️ 如果需要使用AI知识库功能，请配置pgvector向量库")
    print("⚠️ 配置文件位置: jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/application-dev.yml")
    print("\n配置参考:")
    print("""
jeecg.ai-rag:
    embed-store:
      host: 127.0.0.1       # ← 可能需要修改
      port: 5432            # ← 可能需要修改
      database: postgres    # ← 可能需要修改
      user: postgres        # ← 可能需要修改
      password: postgres    # ← 可能需要修改
      table: embeddings     # ← 可能需要修改
""")
    print("⚠️ 注意: 请确保已安装PostgreSQL并添加pgvector扩展！docker安装参考：https://help.jeecg.com/aigc/config")
    return True

def check_ai_config() -> bool:
    """检查AI账号配置"""
    print("\n检查AI功能配置...")
    print("⚠️ 如果需要使用AI聊天功能，请配置AI账号信息")
    print("⚠️ 配置文件位置: jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/application-dev.yml")
    print("\n配置参考:")
    print("""
jeecg:
  # AI集成
  ai-chat:
    enabled: true           # ← 启用AI功能
    model: deepseek-chat    # ← 模型名称
    apiKey: ??              # ← 必须修改为您的API Key
    apiHost: https://api.deepseek.com/v1  # ← API地址
    timeout: 60             # ← 超时时间(秒)
""")
    print("⚠️ 注意: 请确保已获取有效的API Key并正确配置！AI账号注册获取参考： https://help.jeecg.com/java/deepSeekSupport")
    return True


def print_redis_config():
    """打印Redis配置并提示需要修改的位置"""
    print("\nRedis配置参考 (请检查以下配置是否正确):")
    print("""
spring.redis:
  database: 0
  host: 127.0.0.1        # ← 可能需要修改
  port: 6379             # ← 可能需要修改
  password: ''           # ← 如果需要密码请修改
""")

def main():
    print("="*50)
    print("JeecgBoot 运行环境检查脚本")
    print("="*50)
    
    all_checks_passed = True
    
    # 检查各项依赖
    if not check_java():
        all_checks_passed = False
    
    if not check_maven():
        all_checks_passed = False
    
    if not check_node():
        all_checks_passed = False
    
    if not check_pnpm():
        all_checks_passed = False
    
    # 数据库提示
    print("="*50)
    check_redis_connection()
    print_redis_config()
    print("="*50)
    check_mysql_connection()
    print_mysql_config()
    print("="*50)
    check_ai_config()
    print("="*50)
    check_ai_vector_db()
    
    print("\n" + "="*50)
    if all_checks_passed:
        print("✅ 所有基础环境检查通过")
        print("⚠️ 注意: 请确保Redis和MySQL、AI账号、向量库pgvector 已正确配置并连接成功")
    else:
        print("❌ 部分环境检查未通过，请根据上述提示解决问题")
    
    print("="*50)

if __name__ == "__main__":
    main()
    input("\n按回车键退出...")  # 等待用户输入