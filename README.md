# ColorBook 🌈

App Android liviana estilo "social lite" con **letras de colores**: feed con posts en arcoíris, degradado o color fijo, chat de demo y temas de color. Todo funciona offline (sin servidores).

## Generar el APK sin instalar nada (GitHub Actions)

1. Sube esta carpeta a un repositorio de GitHub (o entra a la carpeta en tu proyecto y haz push).
2. Ve a la pestaña **Actions** → workflow **Build APK** → **Run workflow**.
3. Cuando termine, entra al run y descarga el artifact **ColorBook-debug-apk**.
4. Copia el APK a tu teléfono e instálalo (permite "instalar apps de orígenes desconocidos").

## Compilar localmente

Con Android Studio: abre la carpeta y presiona **Run**.

Con SDK instalado y Java 17:

```bash
gradle assembleDebug        # el APK queda en app/build/outputs/apk/debug/
```

## Qué incluye

- **Feed**: posts con letras de colores (arcoíris, degradado, color fijo), nombres con color según el usuario, likes falsos y mini-datos demo.
- **Publicar**: escribe tu texto, elige estilo y color, y mira la vista previa en vivo.
- **Chat de demo**: conversación offline con respuestas automáticas y burbujas de color.
- **Temas**: 6 temas (Claro, Oscuro, Rosa, Azul noche, Selva, Atardecer) + cambio de nombre.

## Estructura

```
app/src/main/java/com/colorbook/app/
├── MainActivity.kt      Feed
├── PostAdapter.kt       Tarjetas del feed
├── ComposeActivity.kt   Publicar con vista previa
├── ChatActivity.kt      Chat de demo
├── ThemesActivity.kt    Temas y nombre
├── ColorText.kt         Motor de letras de colores
├── PostStore.kt         Guardado local de posts
├── ThemePrefs.kt        Definición de temas
└── Profile.kt           Nombre del usuario
```

## Nota

App de demostración inspirada en redes sociales livianas. No está afiliada a Facebook ni solicita datos de ninguna cuenta. Toda la información se guarda solo en tu teléfono.
