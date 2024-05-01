dependencies {
    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    implementation("ch.qos.logback:logback-classic")
    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.flywaydb:flyway-core")

    implementation("org.postgresql:postgresql")

    implementation("org.ehcache:ehcache")

    implementation("org.eclipse.jetty:jetty-server") // Основная библиотека Jetty для запуска веб-сервера. Обрабатывает входящие сетевые соединения и управляет жизненным циклом веб-приложения
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet") // Сервлеты используются для обработки HTTP-запросов и формирования HTTP-ответов
    implementation("org.eclipse.jetty.ee10:jetty-ee10-webapp") // Предоставляет возможность работать с веб-приложениями

    implementation("org.eclipse.jetty:jetty-io") // Предоставляет поддержку ввода/вывода в Jetty, что необходимо для обработки HTTP-запросов и ответов
    implementation("org.eclipse.jetty:jetty-http") // Предоставляет основу для выполнения HTTP запросов и ответов

    implementation("org.eclipse.jetty:jetty-util") //  Содержит утилиты для работы с URL, строками, коллекциями, потоками и т.д. Включает в себя инструменты для работы с асинхронными операциями и парсинга данных.
    implementation("org.eclipse.jetty:jetty-security") // Добавляет поддержку безопасности в Jetty
    implementation("org.freemarker:freemarker") // Помогает в создании динамических веб-страниц или шаблонов

}