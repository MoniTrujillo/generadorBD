# 🧪 COMO PROBAR EL COMPILADOR EN LA PÁGINA WEB

## ▶️ Paso 1: Ejecutar Backend y Frontend

**Abre DOS terminales:**

### Terminal 1 - Backend
```bash
cd D:\Lenguajes_Automatas_II\ANTLR\frontend-generadorBD\generadorBD\backend
npm run dev
```
Debería mostrar:
```
Backend escuchando en http://localhost:3000
```

### Terminal 2 - Frontend  
```bash
cd D:\Lenguajes_Automatas_II\ANTLR\frontend-generadorBD\generadorBD\frontend
npm run dev
```
Debería mostrar algo como:
```
  ➜  Local:   http://localhost:5173/
```

---

## 🌐 Paso 2: Abrir la página web

Abre tu navegador en: **http://localhost:5173**

Deberías ver:
- Caja de texto para escribir código DSL
- Botón "Compilar" 
- Caja para mostrar errores
- Caja para mostrar SQL
- Caja para mostrar estructura de BD

---

## 📝 Paso 3: Copiar código de prueba SENCILLO

En la caja de texto principal, copia y pega esto:

```
crear escuela
usar escuela

tabla estudiantes inicio
  est_id numeros clave
  nombre letras
fin

cerrar
```

Presiona el botón **"Compilar"**

---

## ✅ Resultado esperado

### ❌ Caja de ERRORES (debe estar VACÍA si todo está bien)
```
(vacío - sin errores)
```

### ✅ Caja de SQL GENERADO
```sql
CREATE DATABASE escuela;
USE escuela;
CREATE TABLE estudiantes (
  est_id INT PRIMARY KEY,
  nombre VARCHAR(255),
);
```

### ✅ Caja de ESTRUCTURA DE BD
```
========================================
      RESUMEN DE LA BASE DE DATOS
========================================

Base de datos creada: escuela

----------------------------------------
TABLAS CREADAS
----------------------------------------

Tabla: estudiantes

Atributos:
 - est_id (numeros) ← CLAVE PRINCIPAL
 - nombre (letras)

Relaciones:
 - No depende de ninguna tabla

----------------------------------------

========================================
```

---

## 🧪 Otros códigos para probar

### Código CON RELACIONES (Foreign Keys)
```
crear tienda
usar tienda

tabla categorias inicio
  cat_id numeros clave
  nombre letras
fin

tabla productos inicio
  prod_id numeros clave
  nombre letras
  precio decimal
  cat_id depende_de categorias
fin

cerrar
```

**Resultado esperado:** SQL con FOREIGN KEY

```sql
CREATE TABLE productos (
  prod_id INT PRIMARY KEY,
  nombre VARCHAR(255),
  precio DOUBLE,
  cat_id INT,
  FOREIGN KEY (cat_id) REFERENCES categorias(id),
);
```

---

### Código con ERROR (para probar manejo de errores)

Esto debe dar ERROR porque falta "cerrar":

```
crear mibd
usar mibd

tabla usuarios inicio
  id numeros clave
fin
```

**Resultado esperado en caja de ERRORES:**
```
Error: falta la palabra 'cerrar'.
```

---

## 📁 ¿Dónde se guardan los archivos?

### Actualmente (Versión 1 - En pantalla)
Los archivos **NO se guardan** automáticamente en disco.  
Se muestran en la pantalla como texto que puedes:
- **Copiar y pegar** en un editor
- **Guardar manualmente** como .txt o .sql

### Cómo guardar manualmente:
1. Compila el código DSL
2. Copia el SQL de la caja "SQL Generado"
3. Abre Notepad y pega
4. Guarda como: `esquema.sql`

O para la estructura:
1. Copia el contenido de "Estructura de BD"
2. Guarda como: `estructura.txt`

---

## 🚀 Próximo paso - Agregar botones de descarga

Para automatizar la descarga, necesitamos agregar botones en el frontend.  
**Esto se puede hacer después de verificar que el compilador funciona.**

Opciones:
- A: Botón "Descargar SQL" → genera `esquema.sql`
- B: Botón "Descargar Estructura" → genera `estructura.txt`  
- C: Botón "Descargar Todo" → genera ZIP con ambos

---

## 🔍 Checklist de prueba

- [ ] Backend corriendo en http://localhost:3000 ✅
- [ ] Frontend corriendo en http://localhost:5173 ✅
- [ ] Puedo ver la página web con las cajas de texto ✅
- [ ] Escribo código DSL sencillo ✅
- [ ] Presiono "Compilar" ✅
- [ ] Aparece SQL generado ✅
- [ ] Aparece estructura de BD ✅
- [ ] No hay errores (o los muestra correctamente) ✅

---

## ❓ Si hay problemas

### Frontend no carga
```bash
# Verifica que está en puerto 5173
# URL: http://localhost:5173
```

### Backend no responde
```bash
# Verifica que está en puerto 3000
# Ve a Terminal 1 backend
# Debe decir: "Backend escuchando en http://localhost:3000"
```

### SQL vacío
1. Verifica el código DSL esté bien escrito
2. Comprueba que tiene las palabras clave: crear, usar, tabla, cerrar
3. Mira la caja de ERRORES para pistas

---

## 📚 Sintaxis de la gramática DSL

| Elemento | Sintaxis | Ejemplo |
|----------|----------|---------|
| **Crear BD** | `crear NOMBRE` | `crear mibd` |
| **Usar BD** | `usar NOMBRE` | `usar mibd` |
| **Tabla** | `tabla NOMBRE inicio ... fin` | `tabla usuarios inicio ... fin` |
| **Campo clave** | `NOMBRE TIPO clave` | `id numeros clave` |
| **Campo normal** | `NOMBRE TIPO` | `nombre letras` |
| **Relación** | `NOMBRE depende_de TABLA` | `usuario_id depende_de usuarios` |
| **Cerrar** | `cerrar` | `cerrar` |

### Tipos soportados
- `numeros` → INT
- `letras` → VARCHAR(255)
- `fecha` → DATE
- `decimal` → DOUBLE

---

¡Intenta con el código de prueba! 🚀
