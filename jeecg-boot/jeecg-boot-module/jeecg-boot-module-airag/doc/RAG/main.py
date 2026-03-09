#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MCP Stdio 工具 - 修复编码问题
确保所有输出都使用UTF-8编码
"""

import json
import sys
import os
from typing import Dict, Any
import logging

# 强制使用UTF-8编码
if sys.platform == "win32":
    # Windows需要特殊处理
    import io

    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
    sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')
else:
    # Unix-like系统
    sys.stdin.reconfigure(encoding='utf-8')
    sys.stdout.reconfigure(encoding='utf-8')
    sys.stderr.reconfigure(encoding='utf-8')

# 设置环境变量
os.environ['PYTHONIOENCODING'] = 'utf-8'
os.environ['PYTHONUTF8'] = '1'

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    encoding='utf-8'
)
logger = logging.getLogger("mcp-tool")


class FixedMCPServer:
    """修复编码问题的MCP服务器"""

    def __init__(self):
        self.tools = {}
        self.initialize_tools()

    def initialize_tools(self):
        """初始化工具集"""

        # 获取时间
        self.tools["get_time"] = {
            "name": "get_time",
            "description": "获取当前时间",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "format": {
                        "type": "string",
                        "description": "时间格式",
                        "enum": ["iso", "timestamp", "human", "chinese"],
                        "default": "iso"
                    }
                }
            }
        }

        # 文本处理工具
        self.tools["text_process"] = {
            "name": "text_process",
            "description": "文本处理工具",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "text": {
                        "type": "string",
                        "description": "输入文本"
                    },
                    "operation": {
                        "type": "string",
                        "description": "操作类型",
                        "enum": ["length", "upper", "lower", "reverse", "count_words"],
                        "default": "length"
                    }
                },
                "required": ["text"]
            }
        }

        # 数据格式工具
        self.tools["format_data"] = {
            "name": "format_data",
            "description": "格式化数据",
            "inputSchema": {
                "type": "object",
                "properties": {
                    "data": {
                        "type": "string",
                        "description": "原始数据"
                    },
                    "format": {
                        "type": "string",
                        "description": "格式类型",
                        "enum": ["json", "yaml", "xml"],
                        "default": "json"
                    }
                },
                "required": ["data"]
            }
        }

    def handle_request(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """处理请求"""
        try:
            method = request.get("method")
            params = request.get("params", {})

            if method == "tools/list":
                return self.handle_tools_list()
            elif method == "tools/call":
                return self.handle_tool_call(params)
            elif method == "ping":
                return {"result": "pong"}
            else:
                return self.create_error_response(
                    code=-32601,
                    message="Method not found"
                )

        except Exception as e:
            logger.error(f"Error handling request: {e}")
            return self.create_error_response(
                code=-32603,
                message=f"Internal error: {str(e)}"
            )

    def handle_tools_list(self) -> Dict[str, Any]:
        """列出所有工具 - 确保返回标准JSON"""
        return {
            "result": {
                "tools": list(self.tools.values())
            }
        }

    def handle_tool_call(self, params: Dict[str, Any]) -> Dict[str, Any]:
        """调用工具 - 修复响应格式"""
        name = params.get("name")
        arguments = params.get("arguments", {})

        if name not in self.tools:
            return self.create_error_response(
                code=-32602,
                message=f"Tool '{name}' not found"
            )

        try:
            if  name == "get_time":
                result = self.execute_get_time(arguments)
            elif name == "text_process":
                result = self.execute_text_process(arguments)
            elif name == "format_data":
                result = self.execute_format_data(arguments)
            else:
                return self.create_error_response(
                    code=-32602,
                    message="Tool not implemented"
                )

            # 确保返回正确的MCP响应格式
            return self.create_success_response(result)

        except Exception as e:
            logger.error(f"Tool execution error: {e}")
            return self.create_error_response(
                code=-32603,
                message=f"Tool execution failed: {str(e)}"
            )

    def execute_get_time(self, args: Dict[str, Any]) -> Dict[str, Any]:
        """获取时间 - 支持中文"""
        from datetime import datetime

        try:
            format_type = args.get("format", "iso")
            now = datetime.now()

            if format_type == "iso":
                result = now.isoformat()
            elif format_type == "timestamp":
                result = now.timestamp()
            elif format_type == "human":
                result = now.strftime("%Y-%m-%d %H:%M:%S")
            elif format_type == "chinese":
                result = now.strftime("%Y年%m月%d日 %H时%M分%S秒")
            else:
                result = now.isoformat()
            logger.info(f"当前系统时间：{result}")
            return {
                "status": "success",
                "format": format_type,
                "time": result,
                "timestamp": now.timestamp(),
                "date": now.strftime("%Y-%m-%d"),
                "time_12h": now.strftime("%I:%M:%S %p")
            }

        except Exception as e:
            return {
                "status": "error",
                "error": str(e)
            }

    def execute_text_process(self, args: Dict[str, Any]) -> Dict[str, Any]:
        """文本处理"""
        try:
            text = args.get("text", "")
            operation = args.get("operation", "length")

            if operation == "length":
                result = len(text)
                result_str = f"文本长度: {result} 个字符"
            elif operation == "upper":
                result = text.upper()
                result_str = f"大写: {result}"
            elif operation == "lower":
                result = text.lower()
                result_str = f"小写: {result}"
            elif operation == "reverse":
                result = text[::-1]
                result_str = f"反转: {result}"
            elif operation == "count_words":
                words = len(text.split())
                result = words
                result_str = f"单词数: {words}"
            else:
                raise ValueError(f"未知操作: {operation}")

            return {
                "status": "success",
                "operation": operation,
                "original_text": text,
                "result": result,
                "result_str": result_str,
                "text_length": len(text)
            }

        except Exception as e:
            return {
                "status": "error",
                "error": str(e),
                "operation": args.get("operation", "")
            }

    def execute_format_data(self, args: Dict[str, Any]) -> Dict[str, Any]:
        """格式化数据"""
        try:
            data_str = args.get("data", "")
            format_type = args.get("format", "json")

            # 尝试解析为JSON
            try:
                data = json.loads(data_str)
                is_json = True
            except:
                data = data_str
                is_json = False

            if format_type == "json":
                if is_json:
                    result = json.dumps(data, ensure_ascii=False, indent=2)
                else:
                    # 如果不是JSON，包装成JSON
                    result = json.dumps({"text": data}, ensure_ascii=False, indent=2)
            elif format_type == "yaml":
                import yaml
                result = yaml.dump(data, allow_unicode=True, default_flow_style=False)
            elif format_type == "xml":
                # 简单的XML格式化
                if isinstance(data, dict):
                    result = "<data>"
                    for k, v in data.items():
                        result += f"\n  <{k}>{v}</{k}>"
                    result += "\n</data>"
                else:
                    result = f"<text>{data}</text>"
            else:
                result = str(data)

            return {
                "status": "success",
                "format": format_type,
                "original": data_str,
                "formatted": result,
                "length": len(result)
            }

        except Exception as e:
            return {
                "status": "error",
                "error": str(e),
                "format": args.get("format", "")
            }

    def create_success_response(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """创建成功响应 - 确保符合MCP规范"""
        # 将数据转换为JSON字符串作为文本内容
        content_text = json.dumps(data, ensure_ascii=False, indent=2)

        return {
            "result": {
                "content": [
                    {
                        "type": "text",
                        "text": content_text
                    }
                ],
                "isError": False
            }
        }

    def create_error_response(self, code: int, message: str) -> Dict[str, Any]:
        """创建错误响应"""
        return {
            "error": {
                "code": code,
                "message": message
            }
        }


def safe_json_dump(data: Dict[str, Any]) -> str:
    """安全的JSON序列化，确保UTF-8编码"""
    try:
        return json.dumps(data, ensure_ascii=False, separators=(',', ':'))
    except:
        # 如果失败，使用ASCII转义
        return json.dumps(data, ensure_ascii=True, separators=(',', ':'))


def main():
    """主函数 - 修复Stdio通信"""
    logger.info("启动MCP Stdio服务器 (修复编码版)...")

    server = FixedMCPServer()

    # 初始握手消息
    init_message = {
        "jsonrpc": "2.0",
        "id": 1,
        "result": {
            "protocolVersion": "2024-11-05",
            "capabilities": {
                "tools": {}
            },
            "serverInfo": {
                "name": "fixed-mcp-server",
                "version": "1.0.0"
            }
        }
    }

    # 发送初始化响应
    try:
        sys.stdout.write(safe_json_dump(init_message) + "\n")
        sys.stdout.flush()
    except Exception as e:
        logger.error(f"发送初始化消息失败: {e}")
        return

    logger.info("MCP服务器已初始化")

    # 主循环
    line_num = 0
    while True:
        try:
            line = sys.stdin.readline()
            if not line:
                logger.info("输入流结束")
                break

            line = line.strip()
            line_num += 1

            if not line:
                continue

            logger.info(f"收到第 {line_num} 行: {line[:100]}...")

            try:
                request = json.loads(line)
                logger.info(f"解析请求: {request.get('method', 'unknown')}")

                # 处理请求
                response = server.handle_request(request)
                response["jsonrpc"] = "2.0"
                response["id"] = request.get("id")

                # 发送响应
                response_json = safe_json_dump(response)
                sys.stdout.write(response_json + "\n")
                sys.stdout.flush()

                logger.info(f"发送响应: {response.get('result', response.get('error', {}))}")

            except json.JSONDecodeError as e:
                logger.error(f"JSON解析错误: {e}")
                error_response = {
                    "jsonrpc": "2.0",
                    "error": {
                        "code": -32700,
                        "message": f"Parse error at line {line_num}"
                    },
                    "id": None
                }
                sys.stdout.write(safe_json_dump(error_response) + "\n")
                sys.stdout.flush()

        except KeyboardInterrupt:
            logger.info("接收到中断信号")
            break
        except Exception as e:
            logger.error(f"未处理的错误: {e}")
            break

    logger.info("MCP服务器已停止")


if __name__ == "__main__":
    main()