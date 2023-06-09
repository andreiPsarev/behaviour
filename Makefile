build:
	mvn package
migrate:
	cmd /c "bin\sqlite3.exe universe.db < src\main\resources\migrations\2023_02_20_000000_create_tables.sql"
run:
	java -jar target\universe.jar application.properties
