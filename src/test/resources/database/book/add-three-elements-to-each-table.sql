insert into categories (id, name, description, is_deleted)
values (1, 'horror', 'Horror', 0);
insert into categories (id, name, description, is_deleted)
values (2, 'comedy', 'Comedy', 0);
insert into categories (id, name, description, is_deleted)
values (3, 'roman', 'Roman', 0);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
values (1,'Kobzar', 'Taras', '1331421', 100, 'book kobzar', 'coverImage', 0);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
values (2,'Azbuka', 'Babas', '54121', 90, 'book azbuka', 'coverImage', 0);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted)
values (3,'Table', 'Cufal', '12512', 110, 'book table', 'coverImage', 0);
insert into books_categories (book_id, categories_id)
values (1, 1);
insert into books_categories (book_id, categories_id)
values (2, 2);
insert into books_categories (book_id, categories_id)
values (3, 3);