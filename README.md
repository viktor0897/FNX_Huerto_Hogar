# 🌱 Huerto Hogar

## 📱 Descripción
**Huerto Hogar** es una aplicación móvil Android orientada a la gestión y compra de productos vegetales y orgánicos.  
Permite a los usuarios registrarse, iniciar sesión, gestionar su carrito de compras, visualizar productos por categoría y consultar información climática según su ubicación para retirar tu compra.

---

## 👥 Integrantes
- **Víctor Alvarado**

---

## ⚙️ Funcionalidades

### 👤 Usuarios
- Registro de usuarios
- Inicio de sesión
- Búsqueda de usuario por correo
- Verificación de correo electrónico
- Actualización de contraseña
- Actualización de correo electrónico
- Eliminación de cuenta

### 🛒 Productos
- Listar todos los productos
- Ver detalle de un producto
- Filtrar productos por categoría
- Buscar productos por nombre
- Listar productos con stock disponible
- Crear, actualizar y eliminar productos (API propia)

### 🧺 Carrito de Compras
- Obtener carrito por usuario
- Agregar productos al carrito
- Actualizar cantidad de productos
- Eliminar productos del carrito
- Vaciar carrito completo

### 🌦️ Clima
- Consulta del clima actual según coordenadas geográficas (latitud y longitud)
- Visualización de temperatura en grados Celsius
- Información climática en español

---

## 🔗 Endpoints Utilizados

### 🧩 API Propia (Backend)

#### Usuarios
- `GET /api/usuario/buscar?email=`
- `POST /api/usuario/registrar`
- `POST /api/usuario/login`
- `PUT /api/usuario/{id}/contrasenna`
- `PUT /api/usuario/{id}/correo`
- `DELETE /api/usuario/{id}`
- `GET /api/usuario/verificar-email?email=`

#### Productos
- `GET /api/products`
- `GET /api/products/{id}`
- `GET /api/products/category/{category}`
- `GET /api/products/search?name=`
- `GET /api/products/stock`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

#### Carrito
- `GET /api/cart/{userId}`
- `POST /api/cart/{userId}/add`
- `PUT /api/cart/{userId}/update/{productId}`
- `DELETE /api/cart/{userId}/remove/{productId}`
- `DELETE /api/cart/{userId}/clear`

---

### 🌐 API Externa

#### OpenWeatherMap
- `GET /weather`
  - Parámetros:
    - `lat`: Latitud
    - `lon`: Longitud
    - `appid`: API Key
    - `units=metric`
    - `lang=es`

---

## ▶️ Instrucciones para Ejecutar el Proyecto

### 🖥️ Backend (Microservicios)
1. Abrir el proyecto del backend en el IDE correspondiente
2. Configurar la base de datos
3. Ejecutar el servidor (por ejemplo, usando XAMPP o Docker)
4. Verificar que la API esté accesible desde el dispositivo móvil

### 📲 App Móvil (Android)
1. Abrir el proyecto en **Android Studio**
2. Sincronizar Gradle
3. Configurar la URL base de la API en Retrofit
4. Conectar un dispositivo físico o emulador
5. Ejecutar la aplicación


---

## 🔐 APK Firmado y JKS

- **APK firmado**:  
  Ubicación:
  https://drive.google.com/drive/folders/1AJDFBDwI9hu8ldgE71l4oKPS5S_bRNBT?usp=drive_link


---

## 📂 Código Fuente

- 📱 **Aplicación móvil Android**: https://github.com/viktor0897/FNX_Huerto_Hogar.git

- 🧩 **Microservicios Backend**: https://github.com/viktor0897/FNX_Backend_Huerto_Hogar.git

---

## 🛠️ Tecnologías Utilizadas
- Android Studio
- Kotlin
- Jetpack Compose
- Retrofit
- API REST
- OpenWeatherMap API
- Git & GitHub

---


