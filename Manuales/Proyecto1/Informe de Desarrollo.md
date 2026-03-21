
# Informe de Desarrollo - BiblioSystem

## 1. Implementación del Proyecto

### 1.1 Estructura de Paquetes

El proyecto se organizó en 5 paquetes principales:

- bibliosystem.model: Contiene las clases del modelo de datos (Usuario, Estudiante, Libro, Prestamo)
- bibliosystem.data: Manejo de archivos y memoria central (DataManager)
- bibliosystem.utils: Utilidades: validaciones, fechas, generador de códigos
- bibliosystem.ui: Interfaz gráfica con JFrame y Swing
- bibliosystem: Clase principal (BiblioSystemApp)

### 1.2 Estructuras de Datos Utilizadas

- Arreglos estáticos: Para almacenar usuarios, libros y préstamos (máx. 100 usuarios, 100 libros, 500 préstamos)
- Lista simplemente enlazada: Para el historial de préstamos de cada estudiante

### 1.3 Persistencia

Se utilizaron archivos de texto plano:
- cuentas.txt: Almacena usuarios (formato: username;password;nombre;rol;carnet;carrera)
- prestamos.txt: Almacena préstamos (formato: codPrestamo;carnet;codLibro;fechaPrestamo;fechaLimite;fechaDevolucion;estado)
- bitacora.txt: Registro de operaciones

---

## 2. Problemas Encontrados y Soluciones

### Problema 1: Duplicación de préstamos al recargar sesión

Síntoma: Al cerrar sesión de estudiante y volver a entrar, los préstamos aparecían duplicados. Si el estudiante tenía 3 préstamos, al volver a entrar aparecían 6.

Causa: El método cargarPrestamos() agregaba los préstamos sin limpiar los arreglos anteriores.

Solución: Se modificó ArchivoPrestamos.cargarPrestamos() para:
- Limpiar el arreglo de préstamos antes de cargar
- Limpiar las listas enlazadas de los estudiantes
- Verificar que no existan préstamos duplicados

---

### Problema 2: Error de casting al iniciar sesión como admin

Síntoma: Al iniciar sesión como admin aparecía: ClassCastException: class Usuario cannot be cast to class Operador

Causa: Se intentaba convertir el admin (clase Usuario con rol ADMIN) a Operador.

Solución: Se modificó LoginJFrame para pasar un objeto Usuario a AdminMenuJFrame, y se cambió el constructor de AdminMenuJFrame para recibir Usuario en lugar de Operador.

---

### Problema 3: Ejemplares disponibles no se actualizaban al modificar libro

Síntoma: Al cambiar la cantidad total de ejemplares de un libro, los disponibles quedaban con el valor anterior.

Causa: Solo se actualizaba totalEjemplares, no se recalculaba disponibles.

Solución: En el método modificarLibro() se agregó:
int prestados = libro.getEjemplaresPrestados();
libro.setTotalEjemplares(ejemplares);
libro.setDisponibles(ejemplares - prestados);

---

### Problema 4: Estudiante no veía qué libros estaban disponibles

Síntoma: El estudiante tenía que adivinar qué código de libro ingresar para solicitar un préstamo.

Solución: Se rediseñó EstudianteMenuJFrame con pestañas, agregando:
- Pestaña "Libros Disponibles" con tabla completa
- Mini-tabla en la pestaña "Solicitar Préstamo"
- Botón "Refrescar Lista"

---

## 3. Capturas de Pantalla

### 3.1 Pantalla de Login

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/login.png" width="300">

### 3.2 Registro de Estudiante

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/registrarestudiante.png" width="500">

### 3.3 Menú de Administrador

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/paneladmin.png" width="500">

### 3.4 Gestión de Libros

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/gestionlibros.png" width="500">

### 3.5 Registro de Préstamo

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/controlprestamos.png" width="500">

### 3.6 Menú de Estudiante

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/panelestudiante.png" width="500">

### 3.7 Reporte HTML

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/reporteestudiantes.png" width="500">

### 3.8 Bitácora de Operaciones

<img src="https://github.com/jgmc0812-cyber/IPC1D_2S2025_202500317/blob/master/Manuales/Proyecto1/imagenes/bitacora.png" width="500">

---

## 4. Ejemplos de Entradas y Salidas

### Ejemplo 1: Registro de Estudiante

Entrada:
Carné: 202500317
Nombre: Josselyn Mendoza
Carrera: Ingeniería
Contraseña: 123
Confirmar: 123

Salida esperada:
"¡Estudiante registrado exitosamente! Ya puede iniciar sesión con usuario: 20240001"

Salida en bitacora.txt:
[REGISTRO_ESTUDIANTE][202500317][AUTENTICACION][20/03/26][10:15 AM]

---

### Ejemplo 2: Solicitud de Préstamo

Entrada:
Estudiante: 202500317
Código libro: L001

Validaciones:
- Estudiante existe: Sí
- No tiene vencidos: Sí
- Tiene menos de 3 activos: Sí
- Libro tiene ejemplares: Sí

Salida esperada:
"Préstamo registrado exitosamente! Código: PR-2026-1001, Libro: El Principito, Fecha límite: 04/04/2026, Ejemplares restantes: 4"

Salida en prestamos.txt:
PR-2026-1001;202500317;L001;20/03/2026;04/04/2026;null;ACTIVO

---

### Ejemplo 3: Intento de Préstamo con Límite Alcanzado

Entrada:
Estudiante: 202500317
Código libro: L002
(Estudiante ya tiene 3 préstamos activos)

Salida esperada:
"Error: El estudiante ya tiene 3 préstamos activos"

Salida en bitacora.txt:
[ERROR_PRESTAMO_3_ACTIVOS][202500317][ESTUDIANTE][20/03/26][10:20 AM]

---

### Ejemplo 4: Registro de Operador

Entrada:
Username: operador1
Nombre: Juan Perez
Contraseña: 111

Salida esperada:
"Operador registrado exitosamente"

Salida en cuentas.txt:
operador1;111;Juan Perez;OPERADOR;;

Salida en bitacora.txt:
[REGISTRO_OPERADOR][admin][ADMIN][20/03/26][10:25 AM]

---

### Ejemplo 5: Generación de Reporte

Entrada:
Usuario admin hace clic en "Reporte de Libros Disponibles"

Salida esperada:
"Reporte generado: reporte_libros_disponibles_2026-03-20.html"

Salida en archivo HTML:
Tabla con todos los libros que tienen al menos 1 ejemplar disponible

---

## 5. Conclusiones

- Se logró implementar un sistema funcional con tres roles de usuario: Administrador, Operador y Estudiante
- Se cumplieron todas las reglas de negocio: máximo 3 préstamos activos por estudiante, plazo de 15 días, validaciones de stock
- La persistencia mediante archivos de texto funciona correctamente, manteniendo los datos entre sesiones
- La lista enlazada para el historial de estudiantes permite recorrer los préstamos de forma eficiente
- La bitácora registra todas las operaciones importantes del sistema, facilitando la auditoría
- Los reportes en HTML se generan correctamente con la información solicitada

---

## 6. Recomendaciones para Mejoras Futuras

- Implementar encriptación de contraseñas para mayor seguridad
- Agregar ordenamiento de tablas por columnas
- Exportar reportes también en formato PDF
- Mejorar la interfaz con más información visual y gráficos
- Implementar recuperación de contraseña por correo
- Agregar estadísticas gráficas de préstamos por mes

---

Informe de Desarrollo - BiblioSystem v1.0
Marzo 2026

---
