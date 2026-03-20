
---

## Tecnologías Utilizadas

- **Lenguaje**: Java 
- **Interfaz Gráfica**: Swing/AWT
- **Persistencia**: Archivos de texto plano (.txt)
- **Reportes**: HTML
- **IDE**: NetBeans

---

## Problemas Conocidos y Soluciones

### Problema 1: Duplicación de préstamos
- **Síntoma**: Al cerrar sesión y volver a entrar, los préstamos aparecían duplicados
- **Solución**: Se limpiaron los arreglos y listas enlazadas antes de cargar desde archivo

### Problema 2: Error de casting al iniciar sesión como admin
- **Síntoma**: `ClassCastException`
- **Solución**: Se cambió el tipo de parámetro en `AdminMenuJFrame` de `Operador` a `Usuario`

### Problema 3: Los ejemplares disponibles no se actualizaban al modificar un libro
- **Síntoma**: Al cambiar `totalEjemplares`, `disponibles` quedaba igual
- **Solución**: Se calcula `disponibles = totalEjemplares - prestados`

---

## Mejoras Futuras

- Encriptación de contraseñas
- Ordenamiento de tablas por columnas
- Exportar reportes en PDF
- Gráficos estadísticos de préstamos
- Recuperación de contraseña

---

## Autor

Proyecto desarrollado para el curso de **Introducción a la Programación y Computación 1**  
Universidad San Carlos de Guatemala  
Facultad de Ingeniería - Ingeniería en Ciencias y Sistemas

---

*Marzo 2026*
