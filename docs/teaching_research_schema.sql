-- FILE: docs/teaching_research_schema.sql
-- teaching_project: 教研/科研项目主表
CREATE TABLE IF NOT EXISTS teaching_project (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_code VARCHAR(64) UNIQUE,
  title VARCHAR(255) NOT NULL,
  project_type VARCHAR(50), -- 教学/科研/教改/横向等
  applicant_id BIGINT NOT NULL,
  dept_id BIGINT,
  apply_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  start_date DATE,
  end_date DATE,
  status VARCHAR(50), -- 草稿/申报中/初审通过/终审通过/立项/进行中/结题/驳回
  budget DECIMAL(12,2),
  description TEXT,
  attachments TEXT, -- json array of attachment ids
  created_by BIGINT,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_time DATETIME ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_applicant (applicant_id),
  INDEX idx_dept (dept_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- project_review: 项目审批/评审记录
CREATE TABLE IF NOT EXISTS project_review (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  node_name VARCHAR(100),
  approver_id BIGINT,
  result VARCHAR(50), -- 通过/驳回/退回
  score DECIMAL(5,2),
  comments TEXT,
  review_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_project (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- research_output: 成果表
CREATE TABLE IF NOT EXISTS research_output (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT,
  title VARCHAR(500) NOT NULL,
  output_type VARCHAR(50), -- 论文/专利/教材/获奖
  authors VARCHAR(500),
  journal VARCHAR(255),
  publish_date DATE,
  impact_factor DECIMAL(5,2),
  doi VARCHAR(128),
  patent_no VARCHAR(128),
  attachment_ids TEXT,
  created_by BIGINT,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_project (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- fund: 经费记录（预算/报销/拨款）
CREATE TABLE IF NOT EXISTS fund (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT,
  fund_type VARCHAR(50), -- 预算/报销/拨款
  amount DECIMAL(12,2),
  currency VARCHAR(10) DEFAULT 'CNY',
  description TEXT,
  status VARCHAR(50), -- 待审批/已通过/已支付/驳回
  created_by BIGINT,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_project (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- activity: 学术活动/培训/大赛
CREATE TABLE IF NOT EXISTS activity (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  activity_type VARCHAR(50), -- 讲座/会议/培训/大赛
  hold_time DATETIME,
  location VARCHAR(255),
  organizer VARCHAR(255),
  participants TEXT, -- json list
  description TEXT,
  created_by BIGINT,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- message: 通知/消息
CREATE TABLE IF NOT EXISTS message (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255),
  content TEXT,
  receiver_id BIGINT, -- 若为0表示广播
  sender_id BIGINT,
  read_flag TINYINT DEFAULT 0,
  sent_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- file_attachment: 附件元数据
CREATE TABLE IF NOT EXISTS file_attachment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  filename VARCHAR(255),
  file_path VARCHAR(1024),
  file_size BIGINT,
  mime_type VARCHAR(100),
  uploader_id BIGINT,
  version INT DEFAULT 1,
  uploaded_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- project_archive: 项目归档记录
CREATE TABLE IF NOT EXISTS project_archive (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  archive_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  archive_by BIGINT,
  archive_location VARCHAR(255),
  notes TEXT,
  INDEX idx_project (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- enterprise: 合作企业表
CREATE TABLE IF NOT EXISTS enterprise (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  industry VARCHAR(128),
  contact_person VARCHAR(128),
  phone VARCHAR(64),
  address VARCHAR(255),
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- cooperation: 校企合作关联表
CREATE TABLE IF NOT EXISTS cooperation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  enterprise_id BIGINT NOT NULL,
  contract_no VARCHAR(128),
  contract_amount DECIMAL(12,2),
  sign_date DATE,
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_project (project_id),
  INDEX idx_enterprise (enterprise_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- optional: fulltext index suggestion for research_output (if using MySQL 5.7+ or use Elasticsearch)
-- ALTER TABLE research_output ADD FULLTEXT INDEX idx_fulltext_title_authors (title, authors);


-- Foreign key constraints are optional due to Jeecg default schema; add if desired
