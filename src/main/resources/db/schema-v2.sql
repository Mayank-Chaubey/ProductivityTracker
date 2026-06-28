-- Productivity Tracker schema upgrade (Servlet + JSP + JDBC compatible)
-- MySQL 8+

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    email_verified_at TIMESTAMP NULL,
    session_version INT NOT NULL DEFAULT 0,
    onboarded BOOLEAN NOT NULL DEFAULT FALSE,
    avatar_path VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_settings (
    user_id INT PRIMARY KEY,
    email_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    browser_notifications BOOLEAN NOT NULL DEFAULT TRUE,
    reminder_lead_minutes INT NOT NULL DEFAULT 10,
    timezone VARCHAR(80) NOT NULL DEFAULT 'UTC',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token_hash VARCHAR(100) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_password_reset_lookup (token_hash, expires_at)
);

CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token_hash VARCHAR(100) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_email_verification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_email_verification_lookup (token_hash, expires_at)
);

CREATE TABLE IF NOT EXISTS remember_me_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    selector VARCHAR(80) NOT NULL UNIQUE,
    token_hash VARCHAR(100) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_remember_me_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_remember_me_lookup (selector, expires_at)
);

CREATE TABLE IF NOT EXISTS security_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    event_type VARCHAR(60) NOT NULL,
    details VARCHAR(500) NULL,
    ip_address VARCHAR(80) NULL,
    user_agent VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_security_events_user_date (user_id, created_at),
    CONSTRAINT fk_security_events_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(160) NOT NULL,
    description VARCHAR(500) NULL,
    priority TINYINT NOT NULL DEFAULT 2, -- 1 high, 2 medium, 3 low
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    category VARCHAR(60) NULL,
    due_date DATE NULL,
    tags VARCHAR(255) NULL,
    is_recurring BOOLEAN NOT NULL DEFAULT FALSE,
    recurrence_rule VARCHAR(40) NULL,
    created_date DATE NOT NULL,
    completed_at TIMESTAMP NULL,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_tasks_user_status (user_id, status),
    INDEX idx_tasks_user_created (user_id, created_date)
);

CREATE TABLE IF NOT EXISTS task_subtasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(180) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    position_index INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_subtasks_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_subtasks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_task_subtasks_task (task_id)
);

CREATE TABLE IF NOT EXISTS habits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    habit_name VARCHAR(160) NOT NULL,
    frequency VARCHAR(20) NOT NULL,
    frequency_rule VARCHAR(80) NULL,
    category VARCHAR(60) NULL,
    streak INT NOT NULL DEFAULT 0,
    last_done_date DATE NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_habits_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_habits_user (user_id)
);

CREATE TABLE IF NOT EXISTS habit_tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    tag_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_habit_tags_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_habit_tag_user (user_id, tag_name)
);

CREATE TABLE IF NOT EXISTS habit_tag_map (
    habit_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (habit_id, tag_id),
    CONSTRAINT fk_habit_tag_map_habit FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    CONSTRAINT fk_habit_tag_map_tag FOREIGN KEY (tag_id) REFERENCES habit_tags(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS habit_streaks (
    habit_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    current_streak INT NOT NULL DEFAULT 0,
    longest_streak INT NOT NULL DEFAULT 0,
    weekly_streak INT NOT NULL DEFAULT 0,
    freeze_tokens INT NOT NULL DEFAULT 2,
    last_completed_on DATE NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_habit_streaks_habit FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    CONSTRAINT fk_habit_streaks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_habit_streaks_user (user_id)
);

CREATE TABLE IF NOT EXISTS habit_completion_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    habit_id INT NOT NULL,
    completed_on DATE NOT NULL,
    freeze_used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_habit_completion_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_habit_completion_habit FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    UNIQUE KEY uk_habit_completion_daily (user_id, habit_id, completed_on),
    INDEX idx_habit_completion_user_date (user_id, completed_on)
);

CREATE TABLE IF NOT EXISTS activities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    activity_name VARCHAR(160) NOT NULL,
    category VARCHAR(60) NULL,
    duration INT NOT NULL,
    mood_score TINYINT NULL, -- 1-5
    notes VARCHAR(500) NULL,
    activity_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activities_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_activities_user_date (user_id, activity_date)
);

CREATE TABLE IF NOT EXISTS journal_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(160) NOT NULL,
    content TEXT NOT NULL,
    mood_score TINYINT NULL,
    entry_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_journal_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_journal_user_date (user_id, entry_date)
);

CREATE TABLE IF NOT EXISTS time_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    reference_type VARCHAR(30) NOT NULL,
    reference_id INT NOT NULL,
    duration_minutes INT NOT NULL,
    log_date DATE NOT NULL,
    CONSTRAINT fk_time_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_time_logs_user_date (user_id, log_date)
);

CREATE TABLE IF NOT EXISTS pomodoro_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    work_minutes INT NOT NULL,
    break_minutes INT NOT NULL,
    cycles INT NOT NULL DEFAULT 1,
    total_focus_minutes INT NOT NULL,
    completed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pomodoro_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_pomodoro_user_date (user_id, completed_at)
);

CREATE TABLE IF NOT EXISTS goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(160) NOT NULL,
    goal_type VARCHAR(20) NOT NULL, -- MONTHLY | QUARTERLY
    target_value INT NOT NULL,
    current_value INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE | COMPLETED | ARCHIVED
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_goals_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_goals_user_status (user_id, status),
    INDEX idx_goals_user_end_date (user_id, end_date)
);

CREATE TABLE IF NOT EXISTS user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    achievement_code VARCHAR(50) NOT NULL,
    unlocked_on DATE NOT NULL,
    CONSTRAINT fk_user_achievements_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_achievement (user_id, achievement_code),
    INDEX idx_user_achievements_user_date (user_id, unlocked_on)
);

CREATE TABLE IF NOT EXISTS reminders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    reminder_type VARCHAR(30) NOT NULL, -- TASK | HABIT | GOAL | GENERAL
    reference_id INT NULL,
    title VARCHAR(160) NOT NULL,
    remind_at TIMESTAMP NOT NULL,
    channel VARCHAR(20) NOT NULL DEFAULT 'IN_APP',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    sent_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reminders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_reminders_user_time (user_id, remind_at)
);
