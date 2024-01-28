dependencies {
    testImplementation ("org.junit.jupiter:junit-jupiter-api") // основная библиотека JUnit. Включает аннотации и поддержку параметризованных тестов.
    testImplementation ("org.junit.jupiter:junit-jupiter-engine") // движок для выполнения тестов и сбора результатов.
    testImplementation ("org.assertj:assertj-core") // набор методов для проверки условий и значений
    testImplementation ("org.mockito:mockito-core") // для создания макетов (mock objects). Фиктивные объекты
    testImplementation ("org.mockito:mockito-junit-jupiter") // позволяет использовать Mockito вместе с JUnit API
}

