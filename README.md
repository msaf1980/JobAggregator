﻿# JobAggregator

Пример веб-приложения (извлечение резюме с http://zarplata.ru). Резюме сохраняются в БД Postgres версии 9

Работа протестированна на:
1) Glasshfish 4.1.1 (требуется обновить встроенный пакет jboss-logging до версии 3.3.0 простой заменой JAR-пакета). Ветка glassfish-postgresql
2) Apache Tomcat 8.0.27. Ветка tomcat-postgresql

Для работы потребуется создать пользователя (реквизиты для подключения задаются в src/main/resources/META-INF/persistence.xml)

Скрипт создания схемы БД в src/main/db/schema_postgresql.sql

Для сборки используется maven (комманда mvn package)
