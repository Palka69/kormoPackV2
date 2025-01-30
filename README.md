Асистент для операторів Bizzerba, LeePack на підприємстві "KormoTech".

В застосунку відбувається авторизація оператором через робочу пошту(Google Play Services Auth, OAuth 2.0) та вказування свого ПІБ. По ПІБ із застосуванням GoogleSheet API відбувається пошук інфи у робочих таблицях,
графік працівника і його базова статистика (кількість відпрацьованих денних/нічних годин, премія-бонус). Користувач отримує доступ до навчального матеріалу, калькулятору ЗП та списку специфікацій, які  зберігаються
у локалькій SQLite БД. Створюється локальний Ktor-сервер через який ми отримуємо інтерфейс до Room-методів роботи із цими специфікаціями(Retrofit, okHttp, Room), отримуємо їх, фільтруємо по назві чи номеру, додаємо
нові у пункті "Налаштування". Використовувались практики Clean Arch та архітектурний патерн MVVM (ViewModels, LiveData, Coroutine).
