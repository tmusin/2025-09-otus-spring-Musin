-- Test Users
INSERT INTO users (username, password, role, enabled) VALUES
    ('admin', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', 'ADMIN', true),
    ('editor1', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', 'EDITOR', true),
    ('editor2', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', 'EDITOR', true),
    ('user1', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', 'USER', true),
    ('user2', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', 'USER', true),
    ('user3', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', 'USER', true);

-- Test Authors
INSERT INTO authors (full_name) VALUES
    ('Test Author 1'),
    ('Test Author 2'),
    ('Test Author 3');

-- Test Genres
INSERT INTO genres (name) VALUES
    ('Test Genre 1'),
    ('Test Genre 2'),
    ('Test Genre 3');

-- Test Books
INSERT INTO books (title, author_id, genre_id) VALUES
    ('Test Book 1', 1, 1),
    ('Test Book 2', 2, 2),
    ('Test Book 3', 3, 3);

-- Test Comments
INSERT INTO comments (text, book_id, user_id) VALUES
    ('Test comment 1', 1, 4),
    ('Test comment 2', 1, 5),
    ('Test comment 3', 2, 6);

-- ACL SID
INSERT INTO acl_sid (principal, sid) VALUES
    (0, 'ROLE_ADMIN'),
    (1, 'editor1'),
    (1, 'editor2'),
    (1, 'user1'),
    (1, 'user2'),
    (1, 'user3');

-- ACL Class
INSERT INTO acl_class (class) VALUES
    ('ru.musintimur.hw13.models.Book'),
    ('ru.musintimur.hw13.models.Comment');

-- ACL Object Identity
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
    (1, 1, NULL, 2, 0),  -- Book 1, owner = editor1
    (1, 2, NULL, 3, 0),  -- Book 2, owner = editor2
    (1, 3, NULL, 3, 0),  -- Book 3, owner = editor2
    (2, 1, NULL, 4, 0),  -- Comment 1, owner = user1
    (2, 2, NULL, 5, 0),  -- Comment 2, owner = user2
    (2, 3, NULL, 6, 0);  -- Comment 3, owner = user3

-- ACL Entries
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
    -- Books - ADMIN имеет все права
    ( 1, 0, 1, 31, 1, 0, 0),
    ( 2, 0, 1, 31, 1, 0, 0),
    ( 3, 0, 1, 31, 1, 0, 0),
    -- Book 1 - editor1 владелец
    ( 1, 1, 2, 11, 1, 0, 0),
    -- Book 2 - editor1 может читать
    ( 2, 1, 2, 1, 1, 0, 0),
    -- Book 3 - editor1 может читать
    ( 3, 1, 2, 1, 1, 0, 0),
    -- Book 1 - editor2 может читать
    ( 1, 2, 3, 1, 1, 0, 0),
    -- Book 2 - editor2 владелец
    ( 2, 2, 3, 11, 1, 0, 0),
    -- Book 3 - editor2 владелец
    ( 3, 2, 3, 11, 1, 0, 0),
    -- Comments
    ( 4, 0, 1, 1, 1, 0, 0),
    ( 5, 0, 1, 1, 1, 0, 0),
    ( 6, 0, 1, 1, 1, 0, 0),
    -- Владельцы могут редактировать/удалять
    ( 4, 1, 4, 11, 1, 0, 0),
    ( 5, 1, 5, 11, 1, 0, 0),
    ( 6, 1, 6, 11, 1, 0, 0);