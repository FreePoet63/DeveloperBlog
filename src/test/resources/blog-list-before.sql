delete from post;

insert into post(id, anons, text, title, views, user_id) values
(1, 'О любви', 'Я люблю этот мир', 'Любовь', 0, 1),
(2, 'О жизни', 'Я люблю эту жизнь', 'Жизнь', 0, 1),
(3, 'О футболе', 'Я люблю футбол', 'Футбол', 0, 2);

alter sequence hibernate_sequence restart with 10;