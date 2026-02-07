insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('Comment_1 for Book_1', 1),
       ('Comment_2 for Book_1', 1),
       ('Comment_3 for Book_2', 2);

insert into users(username, password, enabled)
values ('admin', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true),
       ('user', '$2a$10$LxJ1WylxVAzGAvybjoFDwOASuftK8ROZFM/411kLpLnAaXmHM7lYq', true);
