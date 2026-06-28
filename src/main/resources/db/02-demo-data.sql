-- Demo seed data for Productivity Tracker.
-- Login: demo@example.com
-- Password: demo123

SET @demo_email = 'demo@example.com';
SET @demo_password = 'pbkdf2$120000$Tok8aQ1Ad9ReilmG0pktLw==$zrjvw8ChT1GspPdoHqtQAbebqbaLT6RtG5b2Ktl89NQ=';

INSERT INTO users (name, email, password, email_verified, email_verified_at, onboarded, created_at)
SELECT 'Demo User', @demo_email, @demo_password, TRUE, CURRENT_TIMESTAMP, TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = @demo_email
);

UPDATE users
SET
    name = 'Demo User',
    password = @demo_password,
    email_verified = TRUE,
    email_verified_at = COALESCE(email_verified_at, CURRENT_TIMESTAMP),
    onboarded = TRUE
WHERE email = @demo_email;

SET @demo_user_id = (SELECT id FROM users WHERE email = @demo_email LIMIT 1);

INSERT INTO user_settings (user_id, email_notifications, browser_notifications, reminder_lead_minutes, timezone)
SELECT @demo_user_id, TRUE, TRUE, 15, 'Asia/Kolkata'
WHERE NOT EXISTS (
    SELECT 1 FROM user_settings WHERE user_id = @demo_user_id
);

INSERT INTO habits (user_id, habit_name, frequency, frequency_rule, category, streak, last_done_date, created_at)
SELECT
    @demo_user_id,
    CONCAT('Demo Habit ', LPAD(n, 2, '0')),
    CASE
        WHEN n % 5 = 0 THEN 'WEEKLY'
        WHEN n % 3 = 0 THEN 'CUSTOM'
        ELSE 'DAILY'
    END,
    CASE
        WHEN n % 5 = 0 THEN 'MON,WED,FRI'
        WHEN n % 3 = 0 THEN '3_TIMES_PER_WEEK'
        ELSE 'EVERY_DAY'
    END,
    CASE
        WHEN n % 6 = 0 THEN 'Health'
        WHEN n % 6 = 1 THEN 'Study'
        WHEN n % 6 = 2 THEN 'Work'
        WHEN n % 6 = 3 THEN 'Fitness'
        WHEN n % 6 = 4 THEN 'Mindfulness'
        ELSE 'Personal'
    END,
    ((n * 3) % 31) + 1,
    CURRENT_DATE - INTERVAL (n % 4) DAY,
    CURRENT_TIMESTAMP - INTERVAL n DAY
FROM (
    SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
    UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25
    UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30
    UNION ALL SELECT 31 UNION ALL SELECT 32 UNION ALL SELECT 33 UNION ALL SELECT 34 UNION ALL SELECT 35
    UNION ALL SELECT 36 UNION ALL SELECT 37 UNION ALL SELECT 38 UNION ALL SELECT 39 UNION ALL SELECT 40
    UNION ALL SELECT 41 UNION ALL SELECT 42 UNION ALL SELECT 43 UNION ALL SELECT 44 UNION ALL SELECT 45
    UNION ALL SELECT 46 UNION ALL SELECT 47 UNION ALL SELECT 48 UNION ALL SELECT 49 UNION ALL SELECT 50
) numbers
WHERE NOT EXISTS (
    SELECT 1
    FROM habits h
    WHERE h.user_id = @demo_user_id
      AND h.habit_name = CONCAT('Demo Habit ', LPAD(n, 2, '0'))
);

INSERT INTO habit_streaks (
    habit_id,
    user_id,
    current_streak,
    longest_streak,
    weekly_streak,
    freeze_tokens,
    last_completed_on,
    created_at,
    updated_at
)
SELECT
    h.id,
    h.user_id,
    h.streak,
    h.streak + (h.id % 9),
    (h.id % 7) + 1,
    (h.id % 4),
    h.last_done_date,
    h.created_at,
    CURRENT_TIMESTAMP
FROM habits h
WHERE h.user_id = @demo_user_id
  AND h.habit_name LIKE 'Demo Habit %'
  AND NOT EXISTS (
      SELECT 1 FROM habit_streaks hs WHERE hs.habit_id = h.id
  );

INSERT IGNORE INTO habit_completion_log (user_id, habit_id, completed_on, freeze_used)
SELECT h.user_id, h.id, CURRENT_DATE, FALSE
FROM habits h
WHERE h.user_id = @demo_user_id
  AND h.habit_name LIKE 'Demo Habit %'
  AND h.last_done_date = CURRENT_DATE;

INSERT INTO tasks (user_id, name, description, priority, status, category, due_date, tags, created_date, completed_at)
SELECT
    @demo_user_id,
    CONCAT('Demo Task ', LPAD(n, 2, '0')),
    CONCAT('Sample productivity task generated for dashboard demonstration number ', n),
    CASE WHEN n % 3 = 0 THEN 1 WHEN n % 3 = 1 THEN 2 ELSE 3 END,
    CASE WHEN n % 4 = 0 THEN 'Completed' ELSE 'Pending' END,
    CASE WHEN n % 4 = 0 THEN 'Academic' WHEN n % 4 = 1 THEN 'Work' WHEN n % 4 = 2 THEN 'Personal' ELSE 'Health' END,
    CURRENT_DATE + INTERVAL (n % 14) DAY,
    'demo,seed',
    CURRENT_DATE - INTERVAL (n % 10) DAY,
    CASE WHEN n % 4 = 0 THEN CURRENT_TIMESTAMP - INTERVAL (n % 5) DAY ELSE NULL END
FROM (
    SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
) numbers
WHERE NOT EXISTS (
    SELECT 1
    FROM tasks t
    WHERE t.user_id = @demo_user_id
      AND t.name = CONCAT('Demo Task ', LPAD(n, 2, '0'))
);

INSERT INTO activities (user_id, activity_name, category, duration, mood_score, notes, activity_date)
SELECT
    @demo_user_id,
    CONCAT('Demo Activity ', LPAD(n, 2, '0')),
    CASE WHEN n % 4 = 0 THEN 'Study' WHEN n % 4 = 1 THEN 'Coding' WHEN n % 4 = 2 THEN 'Planning' ELSE 'Fitness' END,
    20 + (n * 5),
    (n % 5) + 1,
    CONCAT('Seed activity entry for productivity analytics ', n),
    CURRENT_DATE - INTERVAL (n % 7) DAY
FROM (
    SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
) numbers
WHERE NOT EXISTS (
    SELECT 1
    FROM activities a
    WHERE a.user_id = @demo_user_id
      AND a.activity_name = CONCAT('Demo Activity ', LPAD(n, 2, '0'))
);
