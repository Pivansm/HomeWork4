1. Создать класс Product со следующими полями

id: UUID
name: String
description: String
category: ENUM // one of COSMETICS, FOOD, CHEMICAL, TECHNIC
manufactureDateTime: LocalDateTime
manufacturer: String
hasExpiryTime: boolean
stock: int

   Реализовать метод toString

2. Создать класс ProductRepositoryImpl, который представляет CRUD репозиторий к таблице product

Класс ProductRepositoryImpl реализует интерфейс ProductRepository 


Конструктор класса ProductRepositoryImpl:

ProductRepositoryImpl(Connection connection)

Ссылка на интерфейс ProductRepository: link

Критерии приемки

Предоставить PR, в котором присутствует реализация класса ProductRepositoryImpl.

Каждый публичный метод должен быть покрыт unit тестом.