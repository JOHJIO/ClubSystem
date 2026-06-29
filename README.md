# 校园社团管理系统（SSM + Vue3）

这是一个基于课程示例框架改造的前后端分离实训项目，业务主题为校园社团管理。

## 项目结构

- `backend`：SSM 后端，Spring MVC + Spring + MyBatis，打包为 WAR。
- `frontend`：Vue3 + Vite 前端。
- `database/club_system.sql`：MySQL 建库建表和测试数据。

## 核心模块

- 登录认证
- 数据看板
- 社团管理
- 成员管理
- 活动管理
- 公告管理
- 经费管理
- 场地管理
- 场地预约

## 默认账号

- 管理员：`admin / 114514`
- 社团账号：`computer / 123456`

## 数据库

先导入脚本：

```powershell
mysql -uroot -p114514 < database/club_system.sql
```

后端连接配置在：`backend/src/main/resources/jdbc.properties`

## 后端运行

```powershell
cd backend
mvn clean package
```

将 `target/club-system.war` 部署到 Tomcat 9。默认前端代理地址为：

```text
http://localhost:8080/club-system
```

## 前端运行

```powershell
cd frontend
npm install
npm run dev
```

默认访问：`http://localhost:5173`