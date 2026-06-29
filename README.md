# 校园社团管理系统（SSM + Vue3）

一个面向课程实训的前后端分离管理系统，后端采用 SSM（Spring MVC + Spring + MyBatis），前端采用 Vue3 + Vite，数据库使用 MySQL。项目主题为校园社团管理，覆盖社团、成员、活动、公告、经费和场地预约等常见业务。

## 技术栈

| 层次 | 技术 |
| --- | --- |
| 前端 | Vue3、Vite、Fetch API、Lucide Icons |
| 后端 | Spring MVC、Spring、MyBatis、PageHelper、Druid |
| 数据库 | MySQL 8.0 |
| 运行环境 | JDK 8、Maven、Tomcat 9、Node.js |

## 功能模块

- 登录认证：管理员、社团账号登录。
- 数据看板：统计社团数量、成员数量、活动数量和经费收支。
- 社团管理：维护社团名称、分类、负责人、联系方式和状态。
- 成员管理：维护成员学号、学院、专业、年级、所属社团和状态。
- 活动管理：维护活动名称、地点、时间、容量和活动状态。
- 公告管理：发布校级或社团级通知公告。
- 经费管理：登记社团收入、支出、经办人和备注。
- 场地管理：维护活动场地、位置、容量和可用状态。
- 场地预约：登记社团活动场地预约和审核状态。

## 项目结构

```text
ClubSystem
├─ backend                 # SSM 后端 WAR 项目
│  ├─ src/main/java/com/club
│  │  ├─ common            # 通用响应对象
│  │  ├─ config            # Spring、SpringMVC、MyBatis 配置
│  │  ├─ controller        # REST 接口
│  │  ├─ dto               # 请求 DTO
│  │  ├─ entity            # 实体类
│  │  ├─ exception         # 全局异常处理
│  │  ├─ mapper            # MyBatis Mapper 接口
│  │  └─ service           # 业务层
│  └─ src/main/resources
│     ├─ jdbc.properties   # 数据库连接配置
│     └─ mybatis           # Mapper XML
├─ frontend                # Vue3 + Vite 前端
│  ├─ src/App.vue
│  ├─ src/main.js
│  └─ src/styles.css
├─ database
│  └─ club_system.sql      # 建库建表和测试数据
└─ README.md
```

## 数据库说明

数据库名：`club_system`

主要数据表：

| 表名 | 说明 |
| --- | --- |
| `sys_user` | 系统用户表 |
| `club` | 社团表 |
| `member` | 成员表 |
| `activity` | 活动表 |
| `announcement` | 公告表 |
| `finance_record` | 经费记录表 |
| `venue` | 场地表 |
| `venue_booking` | 场地预约表 |

导入脚本：

```powershell
mysql -uroot -p114514 < database/club_system.sql
```

后端数据库配置文件：

```text
backend/src/main/resources/jdbc.properties
```

默认配置：

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/club_system?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=114514
```

## 默认账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `114514` |
| 社团账号 | `computer` | `123456` |
| 社团账号 | `photo` | `123456` |
| 成员账号 | `student01` | `123456` |

## 后端运行

进入后端目录：

```powershell
cd backend
mvn clean package
```

构建成功后生成：

```text
backend/target/club-system.war
```

将 `club-system.war` 部署到 Tomcat 9 的 `webapps` 目录，后端默认地址：

```text
http://localhost:8080/club-system
```

健康检查接口：

```text
http://localhost:8080/club-system/api/auth/health
```

## 前端运行

进入前端目录：

```powershell
cd frontend
npm install
npm run dev
```

默认访问地址：

```text
http://localhost:5173
```

前端代理配置在 `frontend/vite.config.js`：

```js
proxy: {
  '/api': {
    target: 'http://localhost:8080/club-system',
    changeOrigin: true
  }
}
```

## 接口示例

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| `POST` | `/api/auth/login` | 登录 |
| `GET` | `/api/dashboard/stats` | 看板统计 |
| `GET` | `/api/clubs` | 社团分页查询 |
| `POST` | `/api/clubs` | 新增社团 |
| `PUT` | `/api/clubs/{id}` | 修改社团 |
| `DELETE` | `/api/clubs/{id}` | 删除社团 |
| `GET` | `/api/members` | 成员分页查询 |
| `GET` | `/api/activities` | 活动分页查询 |
| `GET` | `/api/finance` | 经费分页查询 |
| `GET` | `/api/bookings` | 场地预约分页查询 |

## 构建验证

已完成以下验证：

```powershell
cd backend
mvn clean package
```

```powershell
cd frontend
npm run build
```

数据库脚本已验证可导入 MySQL，并包含基础测试数据。

## 项目特点

- 使用课程要求的 SSM 技术路线，不依赖 Spring Boot。
- 前后端分离，前端通过 Vite 代理访问后端接口。
- 后端采用 Controller、Service、Mapper、XML 的典型分层结构。
- 数据库脚本包含完整建表语句、外键关系和演示数据。
- 前端提供统一的列表、搜索、分页、新增、编辑、删除操作。