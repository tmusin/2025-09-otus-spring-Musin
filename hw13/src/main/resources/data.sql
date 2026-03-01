insert into users(username, password, enabled, role)
values ('admin', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true, 'ADMIN'),
       ('editor1', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true, 'EDITOR'),
       ('editor2', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true, 'EDITOR'),
       ('user1', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true, 'USER'),
       ('user2', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true, 'USER'),
       ('user3', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true, 'USER');

insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id, user_id)
values ('Comment_1 for Book_1', 1, 4),
       ('Comment_2 for Book_1', 1, 5),
       ('Comment_3 for Book_2', 2, 6);

INSERT INTO acl_sid (principal, sid) VALUES
     (0, 'ROLE_ADMIN'),
     (1, 'editor1'),
     (1, 'editor2'),
     (1, 'user1'),
     (1, 'user2'),
     (1, 'user3');

INSERT INTO acl_class (class) VALUES
    ('ru.musintimur.hw13.models.Book'),
    ('ru.musintimur.hw13.models.Comment');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
    (1, 1, NULL, 2, 0),  -- Book 1, owner = editor1 (sid id=2)
    (1, 2, NULL, 3, 0),  -- Book 2, owner = editor2 (sid id=3)
    (1, 3, NULL, 3, 0),  -- Book 3, owner = editor2 (sid id=3)
    (2, 1, NULL, 4, 0),  -- Comment 1, owner = user1 (sid id=4)
    (2, 2, NULL, 5, 0),  -- Comment 2, owner = user2 (sid id=5)
    (2, 3, NULL, 6, 0);  -- Comment 3, owner = user3 (sid id=6)

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
-- Books - ADMIN имеет READ(1) + WRITE(2) + CREATE(4) + DELETE(8) + ADMIN(16) = 31
(1, 0, 1, 31, 1, 0, 0),  -- Book 1 - ADMIN
(2, 0, 1, 31, 1, 0, 0),  -- Book 2 - ADMIN
(3, 0, 1, 31, 1, 0, 0),  -- Book 3 - ADMIN

-- Book 1 - editor1 (владелец) может READ(1) + WRITE(2) + DELETE(8) = 11
(1, 1, 2, 11, 1, 0, 0),
-- Book 2 - editor1 может READ
(2, 1, 2, 1, 1, 0, 0),
-- Book 3 - editor1 может READ
(3, 1, 2, 1, 1, 0, 0),

-- Book 1 - editor2 может READ
(1, 2, 3, 1, 1, 0, 0),
-- Book 2 - editor2 (владелец) может READ + WRITE + DELETE = 11
(2, 2, 3, 11, 1, 0, 0),
-- Book 3 - editor2 (владелец) может READ + WRITE + DELETE = 11
(3, 2, 3, 11, 1, 0, 0),

-- Comments - READ для всех
-- Comment 1 - user1 (владелец)
(4, 0, 4, 1, 1, 0, 0),
-- Comment 2 - user2 (владелец)
(5, 0, 5, 1, 1, 0, 0),
-- Comment 3 - user3 (владелец)
(6, 0, 6, 1, 1, 0, 0),

-- Владельцы комментариев могут редактировать/удалять (READ(1) + WRITE(2) + DELETE(8) = 11)
(4, 1, 4, 11, 1, 0, 0),  -- Comment 1 - user1
(5, 1, 5, 11, 1, 0, 0),  -- Comment 2 - user2
(6, 1, 6, 11, 1, 0, 0);  -- Comment 3 - user3

