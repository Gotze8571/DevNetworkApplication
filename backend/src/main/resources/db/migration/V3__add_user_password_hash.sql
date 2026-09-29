-- Users now sign in with a password. Stored as a BCrypt hash (60 characters).
-- Accounts created before this migration have no password, so they are removed.
DELETE FROM users;

ALTER TABLE users ADD COLUMN password_hash VARCHAR(100) NOT NULL;
