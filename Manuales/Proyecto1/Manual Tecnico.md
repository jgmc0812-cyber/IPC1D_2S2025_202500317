# Manual Técnico - BiblioSystem

## 1. Requerimientos de la Aplicación

### Hardware
- Procesador: 1.5 GHz o superior
- Memoria RAM: 512 MB mínimo
- Espacio en disco: 100 MB libres

### Software
- Java JDK 8 o superior
- Sistema operativo: Windows / Linux / macOS

---

## 2. Variables Utilizadas

### 2.1 DataManager.java

| Variable | Tipo | Descripción |
|----------|------|-------------|
| `usuarios` | `Usuario[]` | Arreglo que almacena todos los usuarios del sistema (máx. 100) |
| `libros` | `Libro[]` | Arreglo que almacena todos los libros del catálogo (máx. 100) |
| `prestamos` | `Prestamo[]` | Arreglo que almacena todos los préstamos (máx. 500) |
| `totalUsuarios` | `int` | Contador de usuarios registrados |
| `totalLibros` | `int` | Contador de libros en el catálogo |
| `totalPrestamos` | `int` | Contador de préstamos realizados |

### 2.2 Clase Usuario

| Variable | Tipo | Descripción |
|----------|------|-------------|
| `username` | `String` | Nombre de usuario (para estudiantes es su carné) |
| `password` | `String` | Contraseña del usuario |
| `nombreCompleto` | `String` | Nombre real del usuario |
| `rol` | `String` | Rol del usuario: ADMIN, OPERADOR, ESTUDIANTE |

### 2.3 Clase Estudiante (hereda de Usuario)

| Variable | Tipo | Descripción |
|----------|------|-------------|
| `carne` | `String` | Carné universitario |
| `carrera` | `String` | Carrera que estudia |
| `cabezaHistorial` | `NodoPrestamo` | Cabeza de la lista enlazada de préstamos |

### 2.4 Clase Libro

| Variable | Tipo | Descripción |
|----------|------|-------------|
| `codigoInterno` | `String` | Código único del libro |
| `isbn` | `String` | ISBN (10 o 13 dígitos) |
| `titulo` | `String` | Título del libro |
| `autor` | `String` | Autor del libro |
| `genero` | `String` | Género literario |
| `anioPublicacion` | `int` | Año de publicación |
| `totalEjemplares` | `int` | Total de ejemplares disponibles |
| `disponibles` | `int` | Ejemplares disponibles para préstamo |

### 2.5 Clase Prestamo

| Variable | Tipo | Descripción |
|----------|------|-------------|
| `codigoPrestamo` | `String` | Código único del préstamo (formato: PR-YYYY-XXXX) |
| `carnetEstudiante` | `String` | Carné del estudiante que solicita el préstamo |
| `codigoLibro` | `String` | Código interno del libro prestado |
| `fechaPrestamo` | `LocalDate` | Fecha en que se realizó el préstamo |
| `fechaLimite` | `LocalDate` | Fecha límite de devolución (15 días después) |
| `fechaDevolucion` | `LocalDate` | Fecha real de devolución (null si está activo) |
| `estado` | `String` | Estado: ACTIVO o DEVUELTO |

### 2.6 Clase NodoPrestamo (Lista Enlazada)

| Variable | Tipo | Descripción |
|----------|------|-------------|
| `prestamo` | `Prestamo` | Datos del préstamo almacenado en el nodo |
| `siguiente` | `NodoPrestamo` | Apuntador al siguiente nodo de la lista |

---

## 3. Métodos Creados

### 3.1 DataManager.java

| Método | Descripción |
|--------|-------------|
| `inicializarSistema()` | Inicia el sistema, crea usuario admin y carga archivos |
| `buscarUsuario(String username)` | Busca un usuario por su username |
| `agregarUsuario(Usuario nuevo)` | Agrega un nuevo usuario al arreglo |
| `eliminarUsuario(String username)` | Elimina un usuario del arreglo |
| `buscarLibro(String codigoInterno)` | Busca un libro por su código interno |
| `buscarLibroPorIsbn(String isbn)` | Busca un libro por su ISBN |
| `agregarLibro(Libro nuevo)` | Agrega un nuevo libro al catálogo |
| `eliminarLibro(String codigoInterno)` | Elimina un libro del catálogo |
| `tienePrestamosActivos(String codigoLibro)` | Verifica si un libro tiene préstamos activos |
| `contarPrestamosActivosEstudiante(String carnet)` | Cuenta los préstamos activos de un estudiante |
| `tienePrestamosVencidos(String carnet)` | Verifica si un estudiante tiene préstamos vencidos |
| `getPrestamosActivos()` | Retorna un arreglo con todos los préstamos activos |

### 3.2 Clase Estudiante

| Método | Descripción |
|--------|-------------|
| `agregarPrestamoAlHistorial(Prestamo prestamo)` | Agrega un préstamo al historial del estudiante (lista enlazada) |
| `contarPrestamosActivos()` | Cuenta los préstamos activos del estudiante |
| `tienePrestamosVencidos()` | Verifica si el estudiante tiene préstamos vencidos |

### 3.3 Clase Libro

| Método | Descripción |
|--------|-------------|
| `prestarEjemplar()` | Reduce en 1 los ejemplares disponibles |
| `devolverEjemplar()` | Aumenta en 1 los ejemplares disponibles |
| `tieneEjemplaresDisponibles()` | Verifica si hay ejemplares disponibles |
| `getEjemplaresPrestados()` | Calcula cuántos ejemplares están prestados |

### 3.4 Clase Prestamo

| Método | Descripción |
|--------|-------------|
| `estaVencido()` | Verifica si el préstamo está vencido |
| `getDiasAtraso()` | Calcula los días de atraso (si está vencido) |
| `registrarDevolucion()` | Marca el préstamo como DEVUELTO y registra fecha |
| `toArchivoString()` | Convierte el préstamo a formato para archivo |
| `fromArchivoString(String linea)` | Crea un préstamo desde una línea de archivo |

### 3.5 ArchivoCuentas.java

| Método | Descripción |
|--------|-------------|
| `cargarCuentas()` | Lee el archivo cuentas.txt al iniciar el sistema |
| `guardarCuenta(Usuario usuario)` | Agrega un nuevo usuario al archivo |
| `reescribirArchivo()` | Reescribe todo el archivo con los datos actuales |

### 3.6 ArchivoPrestamos.java

| Método | Descripción |
|--------|-------------|
| `cargarPrestamos()` | Lee el archivo prestamos.txt al iniciar el sistema |
| `guardarPrestamo(Prestamo prestamo)` | Agrega un nuevo préstamo al archivo |
| `actualizarPrestamo(Prestamo prestamo)` | Actualiza un préstamo existente en el archivo |
| `getPrestamosVencidos()` | Retorna todos los préstamos vencidos |

### 3.7 Bitacora.java

| Método | Descripción |
|--------|-------------|
| `registrar(String operacion, String usuario, String modulo)` | Registra una operación en bitácora |
| `loginExitoso(String usuario)` | Registra un inicio de sesión exitoso |
| `loginFallido(String usuario)` | Registra un inicio de sesión fallido |
| `cierreSesion(String usuario)` | Registra un cierre de sesión |
| `operacionExitosa(String operacion, String usuario, String modulo)` | Registra una operación exitosa |
| `operacionErronea(String operacion, String usuario, String modulo, String motivo)` | Registra una operación fallida |

### 3.8 Validaciones.java

| Método | Descripción |
|--------|-------------|
| `isbnValido(String isbn)` | Valida formato de ISBN (10 o 13 dígitos) |
| `anioValido(int anio)` | Valida que el año sea razonable |
| `validarPrestamoEstudiante(String carnet, String codigoLibro)` | Valida si un estudiante puede solicitar préstamo |
| `libroEliminable(String codigoInterno)` | Verifica si un libro puede ser eliminado |
| `estudianteEliminable(String carnet)` | Verifica si un estudiante puede ser eliminado |

### 3.9 GeneradorCodigos.java

| Método | Descripción |
|--------|-------------|
| `generarCodigoPrestamo()` | Genera código único para préstamo (PR-YYYY-XXXX) |
| `generarCodigoLibro()` | Genera código interno para libro (LIBXXX) |

---

## 4. Estructuras de Datos Implementadas

### 4.1 Arreglos Estáticos (Vectores)

```java
public static Usuario[] usuarios = new Usuario[100];
public static Libro[] libros = new Libro[100];
public static Prestamo[] prestamos = new Prestamo[500];
```

- Capacidad máxima definida por constantes
- Control mediante contadores (`totalUsuarios`, `totalLibros`, `totalPrestamos`)
- Búsqueda lineal para operaciones CRUD

---

### 4.2 Lista Simplemente Enlazada

```java
public class NodoPrestamo {
    private Prestamo prestamo;
    private NodoPrestamo siguiente;
}
```

- Implementada en el historial de préstamos del estudiante
- Inserción al inicio para mostrar préstamos más recientes primero
- Recorrido secuencial para consultas

---

## 5. Formato de Archivos

### 5.1 cuentas.txt

**Formato:**

```txt
username;password;nombreCompleto;rol;carnet;carrera
```

**Ejemplo:**

```txt
admin;admin;Administrador del Sistema;ADMIN;;
operador1;123;Operador Uno;OPERADOR;;
20240001;123;Juan Perez;ESTUDIANTE;20240001;Sistemas
```

---

### 5.2 prestamos.txt

**Formato:**

```txt
codPrestamo;carnet;codLibro;fechaPrestamo;fechaLimite;fechaDevolucion;estado
```

**Ejemplo:**

```txt
PR-2026-1001;20240001;L001;19/03/2026;03/04/2026;null;ACTIVO
PR-2026-1002;20240002;L003;18/03/2026;02/04/2026;20/03/2026;DEVUELTO
```

---

### 5.3 bitacora.txt

**Formato:**

```txt
[OPERACION][USUARIO][MODULO][DD/MM/AA][HH:MM AM/PM]
```

**Ejemplo:**

```txt
[LOGIN_EXITOSO][admin][AUTENTICACION][19/03/26][10:15 AM]
[REGISTRO_OPERADOR][admin][ADMIN][19/03/26][10:16 AM]
[PRESTAMO_EXITOSO][20240001][ESTUDIANTE][19/03/26][10:20 AM]
[CIERRE_SESION][20240001][ESTUDIANTE][19/03/26][12:30 PM]
```

---

### 5.4 Reportes HTML

Los reportes se generan con el siguiente formato de nombre:

| Reporte | Nombre de archivo |
|---------|-------------------|
| Préstamos vencidos | `reporte_prestamos_vencidos_YYYY-MM-DD.html` |
| Libros disponibles | `reporte_libros_disponibles_YYYY-MM-DD.html` |
| 5 libros más prestados | `reporte_libros_mas_prestados_YYYY-MM-DD.html` |
| Estudiantes con préstamos activos | `reporte_estudiantes_activos_YYYY-MM-DD.html` |

Cada reporte incluye:
- Título descriptivo
- Fecha de generación
- Tabla con los datos solicitados
- Total de registros encontrados

---

## 6. Validaciones Implementadas

| Módulo | Validación |
|--------|------------|
| **Login** | Campos vacíos, credenciales incorrectas |
| **Registro estudiante** | Carné único, campos obligatorios, contraseñas coinciden |
| **Registro operador** | Username único, campos obligatorios |
| **Libros** | ISBN válido (10 o 13 dígitos), año válido, código único |
| **Préstamos** | Estudiante existe, ≤ 3 préstamos activos, sin vencidos, libro disponible |
| **Eliminaciones** | Libro sin préstamos activos, estudiante sin préstamos |

---

## 7. Problemas Conocidos y Soluciones

### Problema 1: Duplicación de préstamos al recargar

- **Síntoma:** Al cerrar sesión y volver a entrar, los préstamos aparecían duplicados
- **Solución:** Limpiar arreglos y listas enlazadas antes de cargar desde archivo

### Problema 2: Error de casting con admin

- **Síntoma:** `ClassCastException` al iniciar sesión como admin
- **Solución:** Cambiar tipo de parámetro en `AdminMenuJFrame` de `Operador` a `Usuario`

### Problema 3: Disponibles no se actualizaban al modificar ejemplares

- **Síntoma:** Al cambiar `totalEjemplares`, `disponibles` quedaba igual
- **Solución:** Calcular `disponibles = totalEjemplares - prestados`

### Problema 4: Estudiante no veía libros disponibles

- **Síntoma:** Al solicitar préstamo, el estudiante no sabía qué libros pedir
- **Solución:** Agregar pestaña "Libros Disponibles" en el menú del estudiante con tabla completa

---

## 8. Mejoras Posibles

- Implementar encriptación de contraseñas
- Agregar búsqueda por múltiples criterios simultáneamente
- Permitir ordenamiento de tablas por columnas
- Agregar confirmación antes de cerrar sesión
- Implementar recuperación de contraseña
- Agregar estadísticas gráficas de préstamos
- Exportar reportes en otros formatos (PDF, CSV)

---

*Documentación técnica - Marzo 2026*
```

---
