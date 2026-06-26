# Niko Boot Framework

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

Niko Boot 是一个基于 Spring Boot 的模块化企业级开发框架，采用独立模块架构设计，支持按需引入，便于项目快速搭建和扩展。

---

## 目录

- [一、项目简介](#一项目简介)
- [二、核心特性](#二核心特性)
- [三、技术栈](#三技术栈)
- [四、模块架构](#四模块架构)
- [五、快速开始](#五快速开始)
- [六、模块说明](#六模块说明)
- [七、使用示例](#七使用示例)
- [八、Maven 配置与编译](#八maven-配置与编译)
- [九、编译指南](#九编译指南)
- [十、验证与排查](#十验证与排查)
- [十一、IDE 集成](#十一ide-集成)
- [十二、快速参考](#十二快速参考)
- [十三、项目结构](#十三项目结构)
- [十四、模块对比](#十四模块对比)
- [十五、注意事项](#十五注意事项)
- [十六、优势总结](#十六优势总结)

---

## 一、项目简介

Niko Boot 是一个企业级 Java 开发框架，基于 Spring Boot 4.0.5，采用独立模块架构设计。框架的核心设计理念是：

- ✅ **模块独立**：各模块互不依赖，可单独使用
- ✅ **版本统一**：通过 BOM 统一管理依赖版本
- ✅ **按需引入**：根据需求选择性引入模块
- ✅ **最小依赖**：每个模块只依赖必要的第三方库

---

## 二、核心特性

### 1. 模块独立性
- 所有 `niko-boot-starter-*` 模块完全独立
- 可以单独使用任一模块
- 模块之间无依赖关系

### 2. 统一版本管理
- 通过 `niko-boot-dependencies` BOM 统一管理依赖版本
- 通过 `niko-boot-parent` 统一版本号
- 避免版本冲突问题

### 3. 按需引入
- 用户项目根据实际需求选择性引入模块
- 减少不必要的依赖
- 提高项目灵活性

### 4. 三层架构支持
- **Controller 层**：提供基础 Controller 类
- **Service 层**：提供 Service 基类
- **DAO 层**：提供 MyBatis 基础 DAO

---

## 三、技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 21 | 编程语言 |
| **Spring Boot** | 4.0.5 | 基础框架 |
| **Spring Cloud** | 2025.1.1 | 微服务依赖版本 |
| **Spring Cloud Alibaba** | 2025.1.0.0 | Alibaba 微服务依赖版本 |
| **MyBatis** | 4.0.1 | ORM 框架 |
| **Redis** | - | 缓存 |
| **Redisson** | 4.0.0 | 分布式锁 |
| **Thymeleaf** | - | 视图模板引擎 |
| **Apache POI** | 3.15 | Excel 处理 |
| **JasperReports** | 7.0.1 | 报表生成 |
| **SpringDoc** | 3.0.0 | OpenAPI 文档 |
| **Sa-Token** | 1.45.0 | 权限认证 |
| **Jackson** | 2.18.2 | JSON 处理 |
| **Commons Lang3** | 3.17.0 | 工具类库 |

---

## 四、模块架构

### 整体结构

```
niko-boot/
├── niko-boot-dependencies/              # 依赖版本管理（BOM）
├── niko-boot-parent/                    # 父模块（统一版本号）
│
├── niko-boot-starter-web-base/          # 基础版Web层（独立）
├── niko-boot-starter-web-plus/          # 加强版Web层（独立）
├── niko-boot-starter-service/           # Service层（独立）
├── niko-boot-starter-dao/               # Dao层（独立）
├── niko-boot-starter-model/             # 模型层（独立）
│
├── niko-boot-starter-cache/             # 缓存模块（独立，Redis）
├── niko-boot-starter-lock/              # 锁模块（独立，Redisson）
└── niko-boot-starter-component/         # 公共组件模块（独立）
```

### 模块职责

| 模块 | 职责 | 依赖关系 | 第三方依赖 |
|------|------|---------|-----------|
| `niko-boot-dependencies` | 统一管理依赖版本（BOM） | 无 | Spring Boot Parent |
| `niko-boot-parent` | 统一版本号、构建配置 | niko-boot-dependencies | Spring Boot Parent |
| `niko-boot-starter-web-base` | 基础Web层 | **独立** | Spring Boot Web |
| `niko-boot-starter-web-plus` | 加强Web层 | **独立** | Spring Boot Web + Thymeleaf + POI |
| `niko-boot-starter-service` | Service层 | **独立** | Spring Context |
| `niko-boot-starter-dao` | Dao层 | **独立** | MyBatis + JDBC |
| `niko-boot-starter-model` | 模型层 | **独立** | 无（纯POJO） |
| `niko-boot-starter-cache` | 缓存 | **独立** | Redis |
| `niko-boot-starter-lock` | 分布式锁 | **独立** | Redisson |
| `niko-boot-starter-component` | 公共组件 | **独立** | Redis（用于SnBuilder） |

**关键点**：
- ✅ 所有 `niko-boot-starter-*` 模块完全独立，互不依赖
- ✅ 用户项目可以根据需求组合使用多个模块
- ✅ 版本统一由 `niko-boot-dependencies` BOM 管理

---

## 五、快速开始

### 前置条件

- ✅ JDK 21 已安装
- ✅ Maven 3.6+ 已安装
- ✅ 推荐配置 Maven Toolchains（见 [Maven 配置](#maven-配置)）

### 1. 安装到本地仓库

```bash
# 进入父模块目录
cd niko-boot-parent

# 编译并安装到本地仓库
mvn clean install -DskipTests
```

### 2. 在你的项目中引入依赖

#### 2.1 配置父 POM

```xml
<parent>
    <groupId>com.niko.boot</groupId>
    <artifactId>niko-boot-parent</artifactId>
    <version>1.1.1-SNAPSHOT</version>
</parent>
```

#### 2.2 引入 BOM（推荐）

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.niko.boot</groupId>
            <artifactId>niko-boot-dependencies</artifactId>
            <version>1.1.1-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### 2.3 按需引入模块

```xml
<!-- 基础Web层 -->
<dependency>
    <groupId>com.niko.boot</groupId>
    <artifactId>niko-boot-starter-web-base</artifactId>
</dependency>

<!-- 模型层（包含NikoResult） -->
<dependency>
    <groupId>com.niko.boot</groupId>
    <artifactId>niko-boot-starter-model</artifactId>
</dependency>

<!-- Dao层 -->
<dependency>
    <groupId>com.niko.boot</groupId>
    <artifactId>niko-boot-starter-dao</artifactId>
</dependency>

<!-- 其他模块按需引入... -->
```

---

## 六、模块说明

### niko-boot-starter-web-base

**基础 Web 层模块**

- 提供 `BaseRestController` 基类
- 自动注入 `HttpServletRequest` 和 `HttpServletResponse`
- 适用于纯 API 项目

**使用方式**：
```java
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseRestController {
    // 可直接使用 request 和 response
}
```

### niko-boot-starter-web-plus

**加强版 Web 层模块**

- 继承 `web-base` 的功能
- 支持视图渲染（Thymeleaf）
- 支持 Excel 导入导出（POI）
- 支持报表生成（JasperReports）

**适用场景**：需要视图、文件处理、报表的项目

### niko-boot-starter-model

**模型层模块**

- 完全独立的 POJO 模块
- 包含 `NikoResult` 统一响应对象（参考 SaResult 实现）
- 提供静态方法：`ok()`, `ok(String msg)`, `data(Object data)`, `error(String msg)`, `error(int code, String msg)`
- 支持链式调用：`setCode()`, `setMsg()`, `setData()`, `set(key, value)`

**使用方式**：
```java
// 成功响应（无参数）
return NikoResult.ok();

// 成功响应（带消息）
return NikoResult.ok("操作成功");

// 成功响应（带数据）
return NikoResult.data(user);

// 错误响应
return NikoResult.error("操作失败");

// 错误响应（带错误码）
return NikoResult.error(500, "服务器错误");

// 链式调用
return NikoResult.ok("成功").setData(user).set("extra", "value");
```

### niko-boot-starter-dao

**DAO 层模块**

- 提供 `BaseDao` 基类
- 基于 MyBatis
- 提供常用数据库操作方法

### niko-boot-starter-service

**Service 层模块**

- 提供 `BaseService` 基类
- 基于 Spring Context 的 Service 层抽象
- 用户可继承 `BaseService` 实现业务逻辑

**使用方式**：
```java
import com.niko.boot.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserDao userDao;
    
    public User getById(Long id) {
        return userDao.findById(id);
    }
}
```

### niko-boot-starter-cache

**缓存模块**

- 基于 Redis
- 提供缓存工具类

### niko-boot-starter-lock

**分布式锁模块**

- 基于 Redisson
- 提供分布式锁注解和工具

**使用方式**：
```java
@RedissonLock(key = "order:#{#orderId}", waitTime = 10, leaseTime = 30)
public void createOrder(String orderId) {
    // 业务逻辑
}
```

### niko-boot-starter-component

**公共组件模块**

- 包含公共工具组件
- 如 `SnBuilder`（基于 Redis 的序列号生成器）

---

## 七、使用示例

### 示例 1：基础 Controller

```java
import com.niko.boot.web.controller.BaseRestController;
import com.niko.boot.model.result.NikoResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseRestController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public NikoResult getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return NikoResult.data(user);
    }
}
```

**说明**：
- 继承 `BaseRestController` 后可直接使用 `request` 和 `response`
- 使用 `NikoResult` 返回统一响应格式

### 示例 2：完整三层架构

```java
// Controller
import com.niko.boot.web.controller.BaseRestController;
import com.niko.boot.model.result.NikoResult;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseRestController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public NikoResult getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return NikoResult.data(user);
    }
}

// Service
import com.niko.boot.service.BaseService;
import com.niko.boot.dao.BaseDao;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserDao userDao;
    
    public User getById(Long id) {
        return userDao.findById(id);
    }
}

// Dao
import com.niko.boot.dao.BaseDao;

@Repository
public class UserDao extends BaseDao {
    // MyBatis 操作
}
```

### 示例 3：使用分布式锁

```java
@Service
public class OrderService {
    
    @RedissonLock(key = "order:#{#orderId}", waitTime = 10, leaseTime = 30)
    public void createOrder(String orderId) {
        // 业务逻辑
    }
}
```

### 示例 4：使用序列号生成器

```java
@Service
public class OrderService {
    
    @Autowired
    private SnBuilder snBuilder;
    
    public String createOrder() {
        // 生成订单号：ORDER000001
        String orderNo = snBuilder.build("ORDER", 6);
        return orderNo;
    }
}
```

---

## 八、Maven 配置与编译

### Maven Toolchains 配置（推荐）

Maven Toolchains 允许你在不改变系统默认 JDK 的情况下，为不同的 Maven 项目指定不同的 JDK 版本进行编译。这对于需要在多个 JDK 版本间切换的项目非常有用。

#### 1. 创建 Toolchains 配置文件

**Windows**：`C:\Users\<用户名>\.m2\toolchains.xml`

**Linux/macOS**：`~/.m2/toolchains.xml`

#### 2. Toolchains 配置示例

```xml
<?xml version="1.0" encoding="UTF8"?>
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0" 
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 
           http://maven.apache.org/xsd/toolchains-1.1.0.xsd">
  <!-- Java 21 (OpenLogic) -->
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>21</version>
      <vendor>openlogic</vendor>
    </provides>
    <configuration>
      <jdkHome>C:\Dev\java\openlogic-openjdk-21.0.3+9-windows-x64</jdkHome>
    </configuration>
  </toolchain>
  
  <!-- Java 17 (OpenLogic) -->
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>17</version>
      <vendor>openlogic</vendor>
    </provides>
    <configuration>
      <jdkHome>C:\Dev\java\openlogic-openjdk-17.0.11+9-windows-x64</jdkHome>
    </configuration>
  </toolchain>
  
  <!-- Java 8 (Oracle) -->
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>1.8</version>
      <vendor>oracle</vendor>
    </provides>
    <configuration>
      <jdkHome>C:\Dev\java\jdk1.8.0_181</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

#### 3. 配置说明

- **`<version>`**：JDK 版本号（如：21、17、1.8）
- **`<vendor>`**：JDK 供应商（如：openlogic、oracle、adoptium、azul）
- **`<jdkHome>`**：JDK 安装路径（完整路径）

#### 4. 项目中的 Toolchains 配置

项目已在 `niko-boot-parent/pom.xml` 中配置好 Toolchains，编译时会自动使用 Java 21。

```xml
<build>
    <plugins>
        <!-- Maven Compiler Plugin -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <release>${java.version}</release>
            </configuration>
        </plugin>
        
        <!-- Maven Toolchains Plugin -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-toolchains-plugin</artifactId>
            <version>3.1.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>toolchain</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <toolchains>
                    <jdk>
                        <version>${java.version}</version>
                        <vendor>openlogic</vendor>
                    </jdk>
                    <jdk>
                        <version>${java.version}</version>
                    </jdk>
                </toolchains>
            </configuration>
        </plugin>
    </plugins>
</build>
```

**项目 Java 版本配置**：
```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
```

### Maven Settings 与发布配置

项目已在 `niko-boot-parent/pom.xml` 和 `niko-boot-dependencies/pom.xml` 中配置发布仓库：

| 仓库类型 | server id | URL |
|------|------|------|
| Release | `epo-releases` | `http://nexus.moco.com/repository/maven-releases/` |
| Snapshot | `epo-snapshots` | `http://nexus.moco.com/repository/maven-snapshots/` |

发布前需要在 Maven settings 文件中配置同名 `server` 凭据。仓库提供了示例文件：

```text
maven_conf/niko-boot-settings.xml
```

本地发布命令示例：

```bash
cd niko-boot-parent
mvn deploy -s ../maven_conf/niko-boot-settings.xml -DskipTests
```

如果使用 Cursor / VS Code 的 Maven 面板执行 `deploy`，需要在工作区 `.vscode/settings.json` 中配置：

```json
{
    "maven.settingsFile": "C:\\Dev\\java\\maven-settings\\niko-boot-settings.xml",
    "maven.executable.options": "-s C:\\Dev\\java\\maven-settings\\niko-boot-settings.xml"
}
```

其中 `maven.executable.options` 会追加到 Maven 面板执行的所有 `mvn` 命令中，可确保 `deploy` 使用指定 settings 文件。

---

## 九、编译指南

### 前置条件

- ✅ JDK 21 已安装（通过 Maven Toolchains 配置）
- ✅ Maven 3.6+ 已安装
- ✅ Toolchains 已配置（`~/.m2/toolchains.xml`）

### 基本编译命令

#### 在父模块目录执行（推荐）

```bash
# 进入父模块目录
cd niko-boot-parent

# 清理并编译（跳过测试）
mvn clean compile -DskipTests

# 编译并打包
mvn clean package -DskipTests

# 编译并安装到本地仓库
mvn clean install -DskipTests
```

#### 在项目根目录执行

```bash
# 进入项目根目录
cd niko-boot

# 指定父模块POM执行
mvn -f niko-boot-parent/pom.xml clean compile -DskipTests
```

### 编译单个模块

```bash
# 方式一：在模块目录下执行
cd niko-boot-starter-web-base
mvn clean compile -DskipTests

# 方式二：从父模块执行（使用 -pl 参数）
cd niko-boot-parent
mvn -pl ../niko-boot-starter-web-base clean compile -DskipTests
```

### 从指定模块开始编译

如果某个模块编译失败，可以从该模块开始重新编译：

```bash
cd niko-boot-parent

# 从 web-base 模块开始编译（包括该模块及其后续模块）
mvn clean compile -DskipTests -rf :niko-boot-starter-web-base
```

### 常用编译命令速查

| 命令 | 说明 |
|------|------|
| `mvn clean` | 清理编译输出目录（target） |
| `mvn compile` | 编译源代码 |
| `mvn test` | 编译并运行测试 |
| `mvn package` | 编译并打包（生成jar文件） |
| `mvn install` | 编译、打包并安装到本地Maven仓库 |
| `mvn deploy` | 编译、打包并部署到 `distributionManagement` 配置的远程仓库 |
| `mvn clean compile` | 清理后编译 |
| `mvn clean package` | 清理后打包 |
| `mvn clean install` | 清理后安装 |
| `-DskipTests` | 跳过测试执行 |
| `-Dmaven.test.skip=true` | 跳过测试编译和执行 |
| `-pl <module>` | 编译指定模块 |
| `-rf <module>` | 从指定模块开始编译 |

### 编译场景

#### 场景1：日常开发（快速编译）

```bash
cd niko-boot-parent
mvn compile -DskipTests
```

#### 场景2：清理重新编译

```bash
cd niko-boot-parent
mvn clean compile -DskipTests
```

#### 场景3：打包发布

```bash
cd niko-boot-parent
mvn clean package -DskipTests
```

#### 场景4：安装到本地仓库（供其他项目使用）

```bash
cd niko-boot-parent
mvn clean install -DskipTests
```

### 编译输出位置

编译后的文件位置：

```
niko-boot-starter-web-base/
  └── target/
      ├── classes/          # 编译后的class文件
      └── niko-boot-starter-web-base-1.1.1-SNAPSHOT.jar  # 打包后的jar（package后）
```

### 使用编译脚本

**Windows**：
```bash
cd niko-boot-parent
compile.bat
```

**Linux/macOS**：
```bash
cd niko-boot-parent
./compile.sh
```

---

## 十、验证与排查

### 验证 Toolchains 配置

#### 方法1：查看 Toolchains 信息

```bash
# 查看Maven可用的Toolchains
mvn help:effective-pom | findstr toolchain
```

#### 方法2：验证 JDK 路径

**Windows PowerShell**：
```powershell
# 验证 Java 21
Test-Path "C:\Dev\java\openlogic-openjdk-21.0.3+9-windows-x64\bin\java.exe"
```

**Linux/macOS**：
```bash
# 验证 Java 21
test -f /path/to/java21/bin/java && echo "JDK exists"
```

#### 方法3：编译时查看日志

编译时，你应该能看到类似这样的日志：

```
[INFO] --- toolchains:3.1.0:toolchain (default) @ niko-boot-starter-web-base ---
[INFO] Required toolchain: jdk [ version='21' ]
[INFO] Found matching toolchain for type jdk: JDK[C:\Dev\java\openlogic-openjdk-21.0.3+9-windows-x64]

[INFO] --- compiler:3.13.0:compile (default-compile) @ niko-boot-starter-web-base ---
[INFO] Toolchain in maven-compiler-plugin: JDK[C:\Dev\java\openlogic-openjdk-21.0.3+9-windows-x64]
[INFO] Compiling 1 source file with javac [forked debug parameters release 21]
```

**关键信息**：
- ✅ `Found matching toolchain` - 表示找到了匹配的JDK
- ✅ `Toolchain in maven-compiler-plugin` - 表示编译器使用了指定的JDK
- ✅ `release 21` - 表示使用Java 21编译

### 验证编译成功

编译成功后，你应该看到：

```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for Niko Boot Parent 1.1.1-SNAPSHOT:
[INFO] 
[INFO] Niko Boot Dependencies ............................. SUCCESS
[INFO] Niko Boot Parent ................................... SUCCESS
[INFO] niko-boot-starter-web-base ......................... SUCCESS
[INFO] niko-boot-starter-web-plus ......................... SUCCESS
[INFO] niko-boot-starter-service .......................... SUCCESS
[INFO] niko-boot-starter-dao .............................. SUCCESS
[INFO] niko-boot-starter-model ............................ SUCCESS
[INFO] niko-boot-starter-cache ............................ SUCCESS
[INFO] niko-boot-starter-lock ............................. SUCCESS
[INFO] niko-boot-starter-component ........................ SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### 常见问题排查

#### 问题1：找不到 Toolchain

**错误信息**：
```
[WARNING] No toolchain found for type 'jdk' matching:
    version='21'
    vendor='openlogic'
```

**解决方法**：
1. 检查 `~/.m2/toolchains.xml` 文件是否存在
2. 检查配置中的 `<version>` 是否匹配（如：21 vs 1.21）
3. 检查 `<vendor>` 是否匹配
4. 检查 `<jdkHome>` 路径是否正确
5. 确认 XML 文件格式正确（注意编码为 UTF-8）

#### 问题2：JDK路径不存在

**错误信息**：
```
[ERROR] Cannot find JDK in path: C:\Dev\java\...
```

**解决方法**：
1. 确认JDK确实安装在该路径
2. 检查路径中是否有空格或特殊字符（需要转义）
3. 确认路径使用正斜杠 `/` 或双反斜杠 `\\`
4. Windows：使用 PowerShell `Test-Path` 验证路径
   ```powershell
   Test-Path "C:\Dev\java\openlogic-openjdk-21.0.3+9-windows-x64\bin\java.exe"
   ```

#### 问题3：版本不匹配

**错误信息**：
```
[ERROR] Fatal error compiling: 无效的目标发行版: 21
```

**解决方法**：
1. 确认Toolchains配置的JDK版本是正确的
2. 检查 `<java.version>` 属性是否与Toolchains中的version匹配
3. 使用 `java -version` 验证JDK版本（在JDK的bin目录下）
4. 确认 JDK 版本与项目要求的版本一致

#### 问题4：Vendor 不匹配

**问题**：Toolchains 配置的 vendor 是 `openlogic`，但项目 pom.xml 中指定了其他 vendor

**解决方法**：
1. 修改项目 pom.xml 中的 `<vendor>` 配置
2. 或修改 toolchains.xml 中的 `<vendor>` 配置
3. 或者不指定 vendor（推荐），只匹配 version
4. 在 toolchains.xml 中配置多个 toolchain（一个带 vendor，一个不带）

#### 问题5：XML 格式错误

**错误信息**：
```
[ERROR] Error parsing toolchains.xml
```

**解决方法**：
1. 检查 XML 文件格式是否正确
2. 确认 XML 声明和命名空间正确
3. 使用 XML 验证工具检查文件
4. 参考本文档中的配置示例

#### 查看详细错误信息

```bash
# 查看详细错误信息
mvn clean compile -DskipTests -X

# 查看有效POM
mvn help:effective-pom > effective-pom.xml

# 查看项目当前配置的 Java 版本
cd niko-boot-parent
mvn help:evaluate -Dexpression=java.version -q -DforceStdout
```

---

## 十一、IDE 集成

### IntelliJ IDEA

#### 1. 配置项目 SDK

- File → Project Structure → Project
- 设置 Project SDK 为 Java 21

#### 2. Maven 配置

- File → Settings → Build, Execution, Deployment → Build Tools → Maven
- Maven home path: 使用系统 Maven
- Maven settings file: 确保指向正确的 settings.xml

#### 3. 使用 Maven Toolchains

- IntelliJ IDEA 会自动读取 `~/.m2/toolchains.xml`
- 编译时会使用配置的 JDK

#### 4. Maven 编译

- 右键项目 → Maven → Reload Project
- 右键项目 → Maven → Compile

### Eclipse

#### 1. 配置 JDK

- Window → Preferences → Java → Installed JREs
- 添加 Java 21 JRE

#### 2. 配置编译器

- Window → Preferences → Java → Compiler
- 设置 Compiler compliance level 为 21

#### 3. Maven 编译

- 右键项目 → Run As → Maven build...
- Goals: `clean compile`

---

## 十二、快速参考

### 推荐编译流程

```bash
# 1. 进入父模块目录
cd niko-boot-parent

# 2. 清理并编译（推荐）
mvn clean compile -DskipTests

# 3. 如果编译成功，打包
mvn clean package -DskipTests

# 4. 安装到本地仓库（供其他项目使用）
mvn clean install -DskipTests
```

### 验证 Toolchains 是否工作

```bash
# 查看编译使用的 JDK（Windows PowerShell）
mvn clean compile -DskipTests 2>&1 | Select-String -Pattern "Toolchain|Found matching toolchain"

# 或使用详细日志
mvn clean compile -DskipTests -X | findstr /i "toolchain|JDK"
```

### 环境检查命令

```bash
# 检查Java版本（系统默认）
java -version

# 检查Maven版本
mvn -version

# 检查Maven配置
mvn help:effective-settings

# 查看依赖树
mvn dependency:tree

# 查看有效POM
mvn help:effective-pom
```

### Toolchains 工作原理

1. **Maven 读取 toolchains.xml**：从 `~/.m2/toolchains.xml` 读取配置
2. **匹配工具链**：根据 `<version>` 和 `<vendor>` 查找匹配的 JDK
3. **应用工具链**：`maven-toolchains-plugin` 在编译前设置 JDK
4. **编译器使用指定 JDK**：`maven-compiler-plugin` 使用指定的 JDK 进行编译

### 最佳实践

1. ✅ **保持 Toolchains 配置简洁**：只配置实际需要的 JDK 版本
2. ✅ **验证路径存在**：确保配置的 JDK 路径真实存在
3. ✅ **统一 Vendor**：在同一项目中统一使用同一个 vendor，或省略 vendor
4. ✅ **定期验证**：使用 `mvn clean compile -X` 验证 Toolchains 是否生效

---

## 十三、项目结构

```
niko-boot/
├── niko-boot-dependencies/              # 依赖版本管理（BOM）
│   └── pom.xml
│
├── niko-boot-parent/                    # 父模块
│   ├── pom.xml
│   ├── compile.bat                      # Windows 编译脚本
│   └── compile.sh                       # Linux/macOS 编译脚本
│
├── niko-boot-starter-web-base/          # 基础Web层
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/web/controller/
│       └── BaseRestController.java
│
├── niko-boot-starter-web-plus/          # 加强版Web层
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/web/
│       ├── controller/BaseRestController.java
│       └── util/                        # 工具类（POI、Jasper等）
│
├── niko-boot-starter-service/           # Service层
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/service/
│       └── BaseService.java
│
├── niko-boot-starter-dao/               # Dao层
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/dao/
│       └── BaseDao.java
│
├── niko-boot-starter-model/             # 模型层
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/model/result/
│       ├── NikoResult.java
│       ├── NikoResultInterface.java
│       └── NikoResultConstants.java
│
├── niko-boot-starter-cache/             # 缓存模块
│   └── pom.xml
│
├── niko-boot-starter-lock/              # 分布式锁模块
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/lock/
│       ├── annotation/RedissonLock.java
│       ├── aspect/RedissonLockAspect.java
│       └── RedissonLockType.java
│
├── niko-boot-starter-component/         # 公共组件模块
│   ├── pom.xml
│   └── src/main/java/com/niko/boot/component/
│       └── SnBuilder.java
│
├── maven_conf/                          # Maven 配置文件
│   ├── niko-boot-settings.xml           # Niko Boot 发布 settings 示例
│   ├── toolchains.xml                   # Toolchains 配置示例
│
├── java-formatter.xml                   # Java 代码格式化配置
└── README.md                            # 项目说明文档（本文件）
```

---

## 十四、模块对比

### web-base vs web-plus

| 特性 | web-base | web-plus |
|------|----------|----------|
| **基础Controller** | ✅ | ✅ |
| **HttpServletRequest/Response注入** | ✅ | ✅ |
| **视图支持** | ❌ | ✅（Thymeleaf） |
| **文件处理** | ❌ | ✅（POI） |
| **报表生成** | ❌ | ✅（JasperReports） |
| **适用场景** | 纯API项目 | 需要视图/报表的项目 |

**选择建议**：
- **纯API项目**：使用 `niko-boot-starter-web-base`
- **需要视图/报表**：使用 `niko-boot-starter-web-plus`
- **两者都需要**：可以同时引入（不会有冲突，功能互补）

---

## 十五、注意事项

### 1. 模块依赖原则

- ✅ 所有 `niko-boot-starter-*` 模块完全独立，互不依赖
- ✅ 如果模块需要组合，用户项目自己引入（如 `web-base` 和 `model` 可同时引入）
- ✅ 每个模块只依赖必要的第三方库

### 2. 版本统一

- ✅ 通过 `niko-boot-dependencies` BOM 统一管理依赖版本
- ✅ 子模块不指定版本
- ✅ 用户引入时也不指定版本（由 BOM 管理）

### 3. 包名统一

所有模块使用统一的包名前缀：`com.niko.boot.*`

- `web-base`: `com.niko.boot.web.controller.*`
- `web-plus`: `com.niko.boot.web.controller.*` / `com.niko.boot.web.util.*`
- `service`: `com.niko.boot.service.*`
- `dao`: `com.niko.boot.dao.*`
- `model`: `com.niko.boot.model.*`
- `lock`: `com.niko.boot.lock.*`
- `component`: `com.niko.boot.component.*`

---

## 十六、优势总结

### 1. 模块独立
- ✅ 各模块互不依赖，可单独使用
- ✅ 减少不必要的依赖
- ✅ 提高灵活性

### 2. 按需引入
- ✅ 用户根据需求选择性引入
- ✅ 减少项目体积
- ✅ 提高编译速度

### 3. 版本统一
- ✅ 所有模块版本统一管理
- ✅ 避免版本冲突
- ✅ 易于升级

### 4. 易于维护
- ✅ 模块职责清晰
- ✅ 修改一个模块不影响其他模块
- ✅ 便于测试

---

---

**提示**：所有编译命令都会自动使用 Toolchains 配置的 Java 21，无需手动设置 JAVA_HOME！

---

## License

Apache License 2.0

---

## 贡献

欢迎提交 Issue 和 Pull Request！

---

**最后更新**：2026-06-26  
**版本**：1.1.1-SNAPSHOT（未经测试的开发版本）
