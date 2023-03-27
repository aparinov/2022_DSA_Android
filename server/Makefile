create_db:
	cat sql/create_db.sql | psql -U user1 testdb

test: create_db
	pytest -vv

.PHONY: create_db test
