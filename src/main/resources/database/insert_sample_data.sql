DROP TABLE IF EXISTS guestbookentries;

CREATE TABLE guestbookentries(
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  author_name TEXT NOT NULL,
  message TEXT NOT NULL,
  date_added INTEGER
);

INSERT INTO guestbookentries(author_name, message, date_added)
	VALUES('Ania', 'has cooked some cookies', 7),
		('Jarek', 'has played keyboard', 600000000),
		('Michał', 'cannot into programming', 1313131313);

.quit
