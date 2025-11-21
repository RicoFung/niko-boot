#!/bin/bash
# Niko Boot 编译脚本
# 使用Maven Toolchains自动配置Java 21

echo "========================================"
echo "Niko Boot 编译脚本"
echo "========================================"
echo

# 显示当前目录
echo "当前目录: $(pwd)"
echo

# 显示Maven版本
echo "检查Maven环境..."
mvn --version
echo

# 编译所有模块
echo "开始编译所有模块..."
echo
mvn clean compile -DskipTests

# 检查编译结果
if [ $? -eq 0 ]; then
    echo
    echo "========================================"
    echo "编译成功！"
    echo "========================================"
else
    echo
    echo "========================================"
    echo "编译失败！"
    echo "========================================"
    exit 1
fi

