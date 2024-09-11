[БД](https://github.com/Mur-Muring/java-filmorate/blob/add_database/%D0%91%D0%94.png)

film - таблица содержит информацию о фильме.

user - таблица содержит информацию о пользователе.

friends - таблица содержит информацию о статусе дружбы между двумя пользователями.

film_genres - содержит информацию о жанрах и фильмах.

genre - содержит информацию о жанрах.

rating - содержит информацию о рейтинге.

film_users - содержит информацию о пользователях и фильмах, которые им понравились

#Примеры запросов к БД:

##Получение списка пользователей

SELECT * FROM user;

____
##Добавление нового пользователя

INSERT INTO user (email, login, name, birthday)

VALUES ('mail@mail.ru', 'user', 'Иванов', '2000-01-01');
____
##Получение списка друзей пользователя с id 1

SELECT u.login

FROM user as u

WHERE u.id = (SELECT friend_id FROM friends

WHERE user_id = 1 AND status = 'Added');

___
##Получение списка фильмов

SELECT * FROM film;

___
##Удаление фильма с id 1

DELETE FROM film WHERE id = 1;

___
##Получение списка 10 самых залайканных фильма

SELECT name

FROM film

WHERE id IN (SELECT film_id

FROM film_likes

GROUP BY film_id

ORDER BY COUNT(user_id) desc

LIMIT 10);

