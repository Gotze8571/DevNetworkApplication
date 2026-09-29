-- Developer profile: one row per user.
CREATE TABLE profiles (
    user_id      UUID PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    headline     VARCHAR(150),
    bio          TEXT,
    location     VARCHAR(100),
    avatar_url   VARCHAR(500),
    github_url   VARCHAR(500),
    linkedin_url VARCHAR(500),
    website_url  VARCHAR(500),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Skills are shared tags (e.g. "Java", "React") linked to users.
CREATE TABLE skills (
    id   UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_skills (
    user_id  UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, skill_id)
);

CREATE INDEX idx_user_skills_skill_id ON user_skills (skill_id);

CREATE TABLE experiences (
    id          UUID PRIMARY KEY,
    user_id     UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    company     VARCHAR(150) NOT NULL,
    title       VARCHAR(150) NOT NULL,
    location    VARCHAR(100),
    start_date  DATE NOT NULL,
    end_date    DATE,
    description TEXT,
    CONSTRAINT chk_experiences_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

CREATE INDEX idx_experiences_user_id ON experiences (user_id);

CREATE TABLE educations (
    id             UUID PRIMARY KEY,
    user_id        UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    school         VARCHAR(150) NOT NULL,
    degree         VARCHAR(150),
    field_of_study VARCHAR(150),
    start_date     DATE NOT NULL,
    end_date       DATE,
    CONSTRAINT chk_educations_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

CREATE INDEX idx_educations_user_id ON educations (user_id);

-- Connection requests between two users.
CREATE TABLE connections (
    id           UUID PRIMARY KEY,
    requester_id UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    addressee_id UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status       VARCHAR(20) NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    responded_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT chk_connections_status CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED')),
    CONSTRAINT chk_connections_not_self CHECK (requester_id <> addressee_id)
);

-- At most one connection per pair of users, whichever direction it was requested in.
CREATE UNIQUE INDEX uq_connections_pair
    ON connections (LEAST(requester_id, addressee_id), GREATEST(requester_id, addressee_id));
CREATE INDEX idx_connections_addressee_id ON connections (addressee_id);

CREATE TABLE posts (
    id         UUID PRIMARY KEY,
    author_id  UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    content    TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_posts_author_id_created_at ON posts (author_id, created_at DESC);

CREATE TABLE post_comments (
    id         UUID PRIMARY KEY,
    post_id    UUID NOT NULL REFERENCES posts (id) ON DELETE CASCADE,
    author_id  UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    content    TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_post_comments_post_id ON post_comments (post_id, created_at);

CREATE TABLE post_likes (
    post_id    UUID NOT NULL REFERENCES posts (id) ON DELETE CASCADE,
    user_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (post_id, user_id)
);

CREATE INDEX idx_post_likes_user_id ON post_likes (user_id);
