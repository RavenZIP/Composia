# Composia

<p align="center">
<img alt="Kotlin Multiplatform" src="https://img.shields.io/badge/Kotlin Multiplatform-F18E33">
 <img alt="Compose Multiplatform" src="https://img.shields.io/badge/Compose Multiplatform-39ad31">
<img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.2.10-A831F5">
<img alt="Static Badge" src="https://img.shields.io/badge/v0.0.1-red">
</p>

## 🧐 О проекте / About

##### [RUS]
**Composia - Kotlin Multiplatform Compose библиотека UI-компонентов с возможностью управления их состоянием при помощи форм.**

Поддерживаемые платформы:
 - Desktop (Windows, MacOs, Linux)
 - Web
 - Android

**IOS временно не поддерживается!**


Библиотека поделена на 3 части:
- `composia-core` — формы (`FormControl`, `ValueControl`, `StatusControl`) и валидаторы.
- `composia-ui` — готовые UI-компоненты на основе Jetpack Compose. Содержит в себе кастомные Compose компоненты как с использованием контролов, так и без них
- `composia-demo` — демонстрационная чясть библиотеки. Недоступна для подключения к своему проекту

##### [ENG]
// TODO

## 🚀 Установка / Installation

**settings.gradle.kts**

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven ("https://jitpack.io")
    }
}
```

**build.gradle.kts (composia-ui)**

```
dependencies {
      implementation("com.github.RavenZIP:composia-ui:$version")
      
      // Если вам не нужен UI / if you don't need a UI
      implementation("com.github.RavenZIP:composia-core:$version") 
}
```

## 🚬 Использование / Usage

Информация отсутствует

## 📜 Лицензия / License

##### [RUS]

Распространяется по лицензии Apache 2.0. Подробности смотрите в разделе [ЛИЦЕНЗИЯ](/LICENSE).

##### [ENG]

Distributed under the Apache 2.0 License. See [LICENSE](/LICENSE) for details.

## 👾 Разработчик / Developer

**Черных Александр**

- [Github](https://github.com/RavenZIP)
- [Telegram](https://t.me/RavenZIP)
