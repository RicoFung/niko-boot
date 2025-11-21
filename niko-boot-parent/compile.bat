@echo off
REM Niko Boot 编译脚本
REM 使用Maven Toolchains自动配置Java 21

echo ========================================
echo Niko Boot 编译脚本
echo ========================================
echo.

REM 显示当前目录
echo 当前目录: %CD%
echo.

REM 显示Maven版本
echo 检查Maven环境...
call mvn --version
echo.

REM 编译所有模块
echo 开始编译所有模块...
echo.
call mvn clean compile -DskipTests

REM 检查编译结果
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo 编译成功！
    echo ========================================
) else (
    echo.
    echo ========================================
    echo 编译失败！
    echo ========================================
    exit /b %ERRORLEVEL%
)

pause

