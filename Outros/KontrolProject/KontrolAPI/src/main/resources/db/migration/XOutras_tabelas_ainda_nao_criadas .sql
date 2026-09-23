CREATE TABLE users (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(120) NOT NULL,
 email VARCHAR(180) NOT NULL UNIQUE,
 password VARCHAR(255) NOT NULL,
 created_at DATETIME NOT NULL
);
CREATE TABLE categories (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 name VARCHAR(120) NOT NULL,
 type VARCHAR(30) NOT NULL,
 icon VARCHAR(80),
 CONSTRAINT fk_category_user FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE financial_accounts (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 name VARCHAR(120) NOT NULL,
 type VARCHAR(30) NOT NULL,
 initial_balance DOUBLE NOT NULL,
 CONSTRAINT fk_account_user FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE transactions (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 category_id BIGINT NULL,
 account_id BIGINT NULL,
 type VARCHAR(30) NOT NULL,
 amount DOUBLE NOT NULL,
 description VARCHAR(255),
 date DATE NOT NULL,
 created_at DATETIME NOT NULL,
 CONSTRAINT fk_transaction_user FOREIGN KEY(user_id) REFERENCES users(id),
 CONSTRAINT fk_transaction_category FOREIGN KEY(category_id) REFERENCES categories(id),
 CONSTRAINT fk_transaction_account FOREIGN KEY(account_id) REFERENCES financial_accounts(id)
);
CREATE TABLE work_schedules (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 name VARCHAR(120) NOT NULL,
 daily_minutes INT NOT NULL,
 start_time TIME NULL,
 end_time TIME NULL,
 CONSTRAINT fk_schedule_user FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE work_records (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 date DATE NOT NULL,
 entry_time TIME NULL,
 break_start TIME NULL,
 break_end TIME NULL,
 exit_time TIME NULL,
 worked_minutes INT NOT NULL,
 expected_minutes INT NOT NULL,
 created_at DATETIME NOT NULL,
 CONSTRAINT uq_work_user_date UNIQUE(user_id,date),
 CONSTRAINT fk_work_user FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE notes (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 title VARCHAR(255) NOT NULL,
 content LONGTEXT NOT NULL,
 favorite BOOLEAN NOT NULL,
 created_at DATETIME NOT NULL,
 updated_at DATETIME NOT NULL,
 CONSTRAINT fk_note_user FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE shopping_lists (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 name VARCHAR(160) NOT NULL,
 created_at DATETIME NOT NULL,
 CONSTRAINT fk_list_user FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE shopping_items (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 shopping_list_id BIGINT NOT NULL,
 name VARCHAR(160) NOT NULL,
 quantity VARCHAR(80),
 completed BOOLEAN NOT NULL,
 CONSTRAINT fk_item_list FOREIGN KEY(shopping_list_id) REFERENCES shopping_lists(id) ON DELETE CASCADE
);
CREATE TABLE tasks (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 title VARCHAR(255) NOT NULL,
 description VARCHAR(500),
 date DATE NOT NULL,
 priority VARCHAR(30) NOT NULL,
 completed BOOLEAN NOT NULL,
 created_at DATETIME NOT NULL,
 updated_at DATETIME NOT NULL,
 CONSTRAINT fk_task_user FOREIGN KEY(user_id) REFERENCES users(id)
);
