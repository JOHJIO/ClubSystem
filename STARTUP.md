# 校园社团管理系统启动说明

本文档用于说明从 GitHub 拉取项目后，如何导入数据库、启动 SSM 后端和 Vue3 前端。

## 一、环境要求

启动前需要本机已安装：

| 工具 | 建议版本 | 说明 |
| --- | --- | --- |
| JDK | 8 | 后端 SSM 项目运行环境 |
| Maven | 3.x | 后端打包工具 |
| Tomcat | 9.x | 部署后端 WAR 包 |
| MySQL | 8.x | 数据库 |
| Node.js | 18+ | 前端运行环境 |

本机当前使用路径示例：

```text
Tomcat: D:\Dev\Tomcat\apache-tomcat-9.0.119
项目目录: F:\ClubSystem\ClubSystem
```

如果换电脑运行，需要按实际安装路径调整命令。

## 二、从 GitHub 拉取项目

```powershell
git clone 你的仓库地址
cd ClubSystem
```

如果是下载 ZIP 压缩包，解压后进入项目根目录即可。

## 三、导入数据库

项目数据库脚本位置：

```text
database\club_system.sql
```

在项目根目录执行：

```powershell
cmd /c "mysql -uroot -p114514 < database\club_system.sql"
```

说明：

- 数据库名为 `club_system`。
- 如果 MySQL 密码不是 `114514`，需要把命令里的密码改成自己的 MySQL 密码。
- 如果系统提示找不到 `mysql` 命令，需要把 MySQL 的 `bin` 目录配置到系统环境变量 `Path`。

后端数据库配置文件：

```text
backend\src\main\resources\jdbc.properties
```

默认配置：

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/club_system?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=114514
```

如果数据库账号或密码不同，需要同步修改这里。

## 四、启动后端

进入后端目录并打包：

```powershell
cd backend
mvn clean package
```

打包成功后会生成：

```text
backend\target\club-system.war
```

把 WAR 包复制到 Tomcat 的 `webapps` 目录：

```powershell
Copy-Item .\target\club-system.war D:\Dev\Tomcat\apache-tomcat-9.0.119\webapps\club-system.war -Force
```

启动 Tomcat：

```powershell
D:\Dev\Tomcat\apache-tomcat-9.0.119\bin\startup.bat
```

后端默认地址：

```text
http://localhost:8080/club-system
```

健康检查地址：

```text
http://localhost:8080/club-system/api/auth/health
```

如果访问健康检查接口能看到返回内容，说明后端启动成功。

## 五、启动前端

打开新的 PowerShell 窗口，进入前端目录：

```powershell
cd frontend
npm install
npm run dev
```

前端默认访问地址：

```text
http://localhost:5173
```

前端会通过 Vite 代理访问后端接口，代理配置在：

```text
frontend\vite.config.js
```

默认代理目标：

```js
target: 'http://localhost:8080/club-system'
```

## 六、默认登录账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `114514` |
| 社团账号 | `computer` | `123456` |
| 社团账号 | `photo` | `123456` |
| 成员账号 | `student01` | `123456` |

## 七、本机日常启动

如果数据库已经导入、前端依赖已经安装，以后本机日常启动只需要：

启动 Tomcat：

```powershell
D:\Dev\Tomcat\apache-tomcat-9.0.119\bin\startup.bat
```

启动前端：

```powershell
cd F:\ClubSystem\ClubSystem\frontend
npm run dev
```

然后访问：

```text
http://localhost:5173
```

## 八、常见问题

### 1. 前端页面打不开

检查 `npm run dev` 是否正常运行，终端里一般会显示：

```text
Local: http://localhost:5173
```

### 2. 登录失败或接口报错

优先检查后端健康检查地址：

```text
http://localhost:8080/club-system/api/auth/health
```

如果健康检查打不开，说明 Tomcat 或后端 WAR 包没有正常启动。

### 3. 数据库连接失败

检查：

- MySQL 服务是否启动。
- 数据库 `club_system` 是否已经导入。
- `backend\src\main\resources\jdbc.properties` 里的账号密码是否正确。

### 4. 端口被占用

默认端口：

| 服务 | 端口 |
| --- | --- |
| 后端 Tomcat | `8080` |
| 前端 Vite | `5173` |
| MySQL | `3306` |

如果端口被占用，需要关闭占用程序，或修改对应配置。

