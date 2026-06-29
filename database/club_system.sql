CREATE DATABASE IF NOT EXISTS club_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE club_system;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS venue_booking;
DROP TABLE IF EXISTS venue;
DROP TABLE IF EXISTS finance_record;
DROP TABLE IF EXISTS announcement;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS club;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE club (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL COMMENT '社团名称',
  category VARCHAR(40) NOT NULL COMMENT '社团分类',
  leader VARCHAR(40) NOT NULL COMMENT '负责人',
  contact VARCHAR(30) COMMENT '联系方式',
  description VARCHAR(300) COMMENT '社团简介',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1正常 0停用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团表';

CREATE TABLE sys_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL COMMENT 'admin/club/member',
  ref_id INT DEFAULT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE member (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  student_no VARCHAR(30) UNIQUE NOT NULL,
  gender VARCHAR(10),
  phone VARCHAR(20),
  college VARCHAR(80),
  major VARCHAR(80),
  grade VARCHAR(20),
  club_id INT NOT NULL,
  join_date DATE,
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1在团 0退出',
  CONSTRAINT fk_member_club FOREIGN KEY (club_id) REFERENCES club(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团成员表';

CREATE TABLE activity (
  id INT PRIMARY KEY AUTO_INCREMENT,
  club_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  location VARCHAR(100),
  start_time DATETIME NOT NULL,
  end_time DATETIME,
  capacity INT DEFAULT 0,
  status TINYINT DEFAULT 0 COMMENT '0筹备 1进行中 2已结束 3取消',
  description VARCHAR(500),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_activity_club FOREIGN KEY (club_id) REFERENCES club(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社团活动表';

CREATE TABLE announcement (
  id INT PRIMARY KEY AUTO_INCREMENT,
  club_id INT DEFAULT NULL,
  title VARCHAR(120) NOT NULL,
  content TEXT NOT NULL,
  publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  status TINYINT DEFAULT 1 COMMENT '1发布 0草稿',
  CONSTRAINT fk_announcement_club FOREIGN KEY (club_id) REFERENCES club(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告通知表';

CREATE TABLE finance_record (
  id INT PRIMARY KEY AUTO_INCREMENT,
  club_id INT NOT NULL,
  type TINYINT NOT NULL COMMENT '1收入 2支出',
  amount DECIMAL(10,2) NOT NULL,
  item VARCHAR(100) NOT NULL,
  handler VARCHAR(40),
  record_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  remark VARCHAR(200),
  CONSTRAINT fk_finance_club FOREIGN KEY (club_id) REFERENCES club(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='经费记录表';

CREATE TABLE venue (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  location VARCHAR(120),
  capacity INT DEFAULT 0,
  status TINYINT DEFAULT 1 COMMENT '1可用 0停用',
  remark VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地表';

CREATE TABLE venue_booking (
  id INT PRIMARY KEY AUTO_INCREMENT,
  venue_id INT NOT NULL,
  club_id INT NOT NULL,
  activity_id INT DEFAULT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  status TINYINT DEFAULT 0 COMMENT '0待审核 1通过 2拒绝',
  remark VARCHAR(200),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_booking_venue FOREIGN KEY (venue_id) REFERENCES venue(id),
  CONSTRAINT fk_booking_club FOREIGN KEY (club_id) REFERENCES club(id),
  CONSTRAINT fk_booking_activity FOREIGN KEY (activity_id) REFERENCES activity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地预约表';

INSERT INTO club (name, category, leader, contact, description, status) VALUES
('计算机协会', '科技创新', '张晨', '13800010001', '组织编程、算法、开源项目和技术分享活动。', 1),
('摄影社', '文化艺术', '李梦', '13800010002', '开展校园摄影、后期修图和主题影展。', 1),
('篮球社', '体育竞技', '王磊', '13800010003', '组织日常训练、院系友谊赛和校级比赛。', 1),
('志愿服务队', '公益实践', '赵晴', '13800010004', '参与校园志愿、社区服务和公益宣传。', 1);

INSERT INTO sys_user (username, password, role, ref_id) VALUES
('admin', '114514', 'admin', NULL),
('computer', '123456', 'club', 1),
('photo', '123456', 'club', 2),
('student01', '123456', 'member', 1);

INSERT INTO member (name, student_no, gender, phone, college, major, grade, club_id, join_date, status) VALUES
('陈一鸣', '20240001', '男', '13900000001', '信息工程学院', '软件工程', '2024级', 1, '2024-09-12', 1),
('周雨薇', '20240002', '女', '13900000002', '艺术学院', '视觉传达', '2024级', 2, '2024-09-15', 1),
('刘浩', '20230011', '男', '13900000003', '体育学院', '运动训练', '2023级', 3, '2023-10-08', 1),
('孙悦', '20230018', '女', '13900000004', '公共管理学院', '行政管理', '2023级', 4, '2023-11-03', 1);

INSERT INTO activity (club_id, title, location, start_time, end_time, capacity, status, description) VALUES
(1, 'Vue3 入门实战', '教学楼 A305', '2026-07-02 19:00:00', '2026-07-02 21:00:00', 60, 0, '完成一个前端管理页面原型。'),
(2, '校园夏日摄影采风', '图书馆广场', '2026-07-04 16:00:00', '2026-07-04 18:00:00', 35, 0, '围绕校园建筑和人物纪实开展拍摄。'),
(3, '三人篮球友谊赛', '东区篮球场', '2026-07-06 18:30:00', '2026-07-06 20:30:00', 40, 0, '社团内部组队比赛。'),
(4, '社区公益宣传', '阳光社区', '2026-07-08 09:00:00', '2026-07-08 11:30:00', 25, 0, '垃圾分类与文明宣传志愿活动。');

INSERT INTO announcement (club_id, title, content, publish_time, status) VALUES
(NULL, '社团系统试运行通知', '校园社团管理系统进入试运行阶段，请各社团维护基础资料。', NOW(), 1),
(1, '计算机协会招新说明', '欢迎对编程、Web 开发和算法竞赛感兴趣的同学报名。', NOW(), 1),
(2, '摄影社作品征集', '本月主题为校园一角，请成员提交 3 张作品。', NOW(), 1);

INSERT INTO finance_record (club_id, type, amount, item, handler, record_time, remark) VALUES
(1, 1, 1200.00, '学院活动经费', '张晨', '2026-06-20 10:00:00', '用于技术沙龙物料'),
(1, 2, 238.50, '打印海报', '陈一鸣', '2026-06-22 14:20:00', '招新海报'),
(2, 1, 800.00, '社团专项经费', '李梦', '2026-06-21 09:00:00', '摄影展布置'),
(3, 2, 420.00, '购买训练用球', '王磊', '2026-06-23 16:00:00', '篮球 6 个');

INSERT INTO venue (name, location, capacity, status, remark) VALUES
('多功能报告厅', '综合楼一层', 180, 1, '适合讲座、发布会'),
('教学楼 A305', '教学楼 A 区三层', 70, 1, '带投影设备'),
('东区篮球场', '体育馆东侧', 80, 1, '户外场地'),
('社团活动室 2', '大学生活动中心二层', 35, 1, '适合小型会议');

INSERT INTO venue_booking (venue_id, club_id, activity_id, start_time, end_time, status, remark) VALUES
(2, 1, 1, '2026-07-02 18:30:00', '2026-07-02 21:30:00', 1, 'Vue3 入门实战使用'),
(3, 3, 3, '2026-07-06 18:00:00', '2026-07-06 21:00:00', 0, '篮球友谊赛预约');