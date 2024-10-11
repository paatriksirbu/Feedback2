# **Feedback2 - Aplicación de Gestión de Novelas con Reseñas**

## **Descripción General**

**Feedback2** es una aplicación para gestionar novelas y sus reseñas. Los usuarios pueden crear, visualizar y gestionar novelas, así como agregar reseñas a las mismas. Esta aplicación se enfoca en ofrecer una experiencia de usuario fluida con una interfaz simple y un diseño modular para una gestión eficiente de novelas y reseñas.

La app está desarrollada en **Kotlin** y utiliza **Room** como base de datos local para almacenar los datos de forma persistente. Además, sigue el patrón de arquitectura **MVVM** (Model-View-ViewModel) para asegurar una separación clara entre la lógica de negocio y la UI, facilitando su mantenimiento y escalabilidad. También cuenta con integración de **Firebase** para analíticas, autenticación y base de datos en tiempo real.

## **Arquitectura y Componentes**

El proyecto sigue la arquitectura **MVVM**, con una base de datos local implementada en **Room** y el uso de **Firebase** para servicios en la nube. A continuación, se describen los principales componentes del proyecto:

---

## **Clases Principales**

### 1. **`Novel`**
Esta clase representa una **entidad** en la base de datos que modela las novelas en la aplicación.

- **Atributos**:
    - `id`: Identificador único de la novela (Primary Key).
    - `title`: El título de la novela.
    - `author`: El autor de la novela.
    - `description`: Descripción breve de la novela.

- **Uso**: Almacena y gestiona la información de las novelas dentro de la app.

### 2. **`Review`**
Clase que representa una **entidad** para las reseñas asociadas a cada novela.

- **Atributos**:
    - `id`: Identificador único de la reseña (Primary Key).
    - `novelId`: ID de la novela asociada a la reseña (Foreign Key).
    - `rating`: Calificación numérica de la reseña.
    - `description`: Descripción de la reseña.

- **Uso**: Los usuarios pueden agregar opiniones y valoraciones a las novelas seleccionadas.

---

## **Capas del Proyecto**

### **1. Capa de Datos (Data Layer)**

#### 1.1. **`NovelDAO`**
Interfaz **DAO** que define las operaciones de acceso a la base de datos para las novelas.

- **Funciones principales**:
    - `insert(novel: Novel)`: Inserta una novela en la base de datos.
    - `delete(novelId: Int)`: Elimina una novela por su ID.
    - `getAllNovels()`: Obtiene todas las novelas almacenadas como un `Flow` para manejo reactivo.

#### 1.2. **`ReviewDAO`**
Interfaz DAO que define las operaciones de acceso a la base de datos para las reseñas.

- **Funciones principales**:
    - `insertReview(review: Review)`: Inserta una reseña.
    - `getReviewsByNovelId(novelId: Int)`: Obtiene todas las reseñas asociadas a una novela específica.

#### 1.3. **`NovelDatabase`**
Clase que implementa la base de datos **Room** y gestiona las entidades `Novel` y `Review`.

- **Uso**:
    - Define las tablas `Novel` y `Review`.
    - Gestiona las instancias singleton para evitar múltiples conexiones a la base de datos.

### **2. Capa de Dominio (Domain Layer)**

#### 2.1. **`NovelRepository`**
El repositorio actúa como el puente entre los DAOs y la capa de presentación.

- **Funciones principales**:
    - `insert(novel: Novel)`: Inserta una novela en segundo plano.
    - `delete(novelId: Int)`: Elimina una novela en segundo plano.
    - `getAllNovels()`: Recupera todas las novelas en un flujo de datos reactivo (usando **Kotlin Coroutines**).

#### 2.2. **`ReviewRepository`**
Gestor de datos para las reseñas de las novelas.

- **Funciones principales**:
    - `insertReview(review: Review)`: Inserta una reseña en segundo plano.
    - `getReviewsByNovelId(novelId: Int)`: Recupera las reseñas de una novela específica.

### **3. Capa de Presentación (Presentation Layer)**

#### 3.1. **`NovelViewModel`**
Clase **ViewModel** que gestiona la lógica de presentación de las novelas, siguiendo el patrón MVVM.

- **Funciones principales**:
    - `insert(novel: Novel)`: Inserta una novela llamando al repositorio.
    - `delete(novelId: Int)`: Elimina una novela llamando al repositorio.
    - `getAllNovels()`: Recupera todas las novelas de la base de datos.

#### 3.2. **`ReviewViewModel`**
ViewModel para la gestión de las reseñas de novelas.

- **Funciones principales**:
    - `insertReview(review: Review)`: Inserta una reseña llamando al repositorio.
    - `getReviewsByNovelId(novelId: Int)`: Obtiene las reseñas de una novela específica.

### **4. Capa de Interfaz de Usuario (UI Layer)**

#### 4.1. **`MainActivity`**
Pantalla principal donde los usuarios pueden ver y gestionar novelas.

- **Funciones principales**:
    - Muestra una lista de novelas desde la base de datos.
    - Permite seleccionar una novela para ver o agregar reseñas.

#### 4.2. **`ReviewsActivity`**
Pantalla secundaria para gestionar las reseñas de una novela seleccionada.

- **Funciones principales**:
    - Muestra las reseñas asociadas a la novela seleccionada.
    - Permite a los usuarios añadir nuevas reseñas.

#### 4.3. **Temas de UI personalizados**:
La interfaz de usuario sigue un diseño minimalista con colores suaves y una interfaz intuitiva.

---

## **Características Clave**

1. **Persistencia de Datos**: Las novelas y sus reseñas se almacenan localmente con **Room**, garantizando que los datos persistan incluso si la aplicación se cierra.
  
2. **Soporte para Firebase**: Integración de **Firebase** para autenticación de usuarios, base de datos en tiempo real y analíticas.
  
3. **Operaciones en Segundo Plano**: Las inserciones y eliminaciones de datos se manejan en segundo plano utilizando **Kotlin Coroutines**, mejorando la fluidez de la UI.
  
4. **Arquitectura MVVM**: Diseño modular que separa la lógica de negocio de la interfaz de usuario, facilitando el mantenimiento y escalabilidad.
  
5. **Interfaz de Usuario Simple**: La UI está diseñada para ser limpia y fácil de usar, con botones destacados y un diseño minimalista que mejora la experiencia del usuario.

---

## **Conclusión**

**Feedback2** es una solución completa para la gestión de novelas y reseñas, combinando una base de datos local con operaciones asincrónicas para garantizar un rendimiento fluido. La integración con Firebase añade robustez para futuras funcionalidades en la nube.

---

## **Contacto**

Patrik Paul Sirbu - https://github.com/paatriksirbu

Mail: patrikpsirbu@gmail.com

Enlace del proyecto: https://github.com/paatriksirbu/Feedback2.git
