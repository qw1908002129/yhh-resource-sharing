#!/bin/bash

# 设置上传目录
UPLOAD_DIR="D:/yhh_game/uploads"

# 创建目录（如果不存在）
mkdir -p "$UPLOAD_DIR"

# 在Windows系统中设置目录权限
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    # 使用icacls设置Windows权限
    icacls "$UPLOAD_DIR" /grant "Users:(OI)(CI)F" /T
    icacls "$UPLOAD_DIR" /grant "SYSTEM:(OI)(CI)F" /T
else
    # 在Linux/Unix系统中设置权限
    chmod -R 755 "$UPLOAD_DIR"
    # 假设应用运行在tomcat用户下
    chown -R tomcat:tomcat "$UPLOAD_DIR"
fi

echo "Upload directory setup completed: $UPLOAD_DIR" 