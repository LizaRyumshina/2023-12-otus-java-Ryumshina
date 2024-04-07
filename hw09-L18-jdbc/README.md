# JDBC 
Цель:
  Научиться работать с jdbc.
  На практике освоить многоуровневую архитектуру приложения.

Задача: 
  Работа должна использовать базу данных в docker-контейнере.
  В модуле homework реализуйте классы:

  EntityClassMetaData
  EntitySQLMetaData
  DataTemplateJdbc
  Метод main в классе HomeWork должен работать без ошибок.


Комментарий к работе:
  Необходимо изменить имеющееся приложение, так чтобы оно могло динамически создавать sql запросы для классов.
  Для работы требуется запустить docker c базой данных PostgreSQL командой из файла docker/runDb.src.

  EntityClassMetaDataImpl - разделяет java класс на составные части (поля, конструктор, наименование класса).
  EntitySQLMetaDataImpl - подготавливает sql команды (select, insert, update) на основе EntityClassMetaDataImpl.
  DataTemplateJdbcImpl - получение значение из базы (выполнение команд из EntitySQLMetaDataImpl).
