# ✅ Compilador ANTLR - Setup Completado

## 🎉 Estado del Proyecto

**Todos los archivos están compilados y listos para funcionar**

```
✅ gramaticaLexer.java → gramaticaLexer.class
✅ gramaticaParser.java → gramaticaParser.class  
✅ Compilador.java → Compilador.class
✅ antlr-3.5.2-complete.jar (copiado desde tu carpeta personal)
✅ server.js (configurado para Windows)
```

---

## 🚀 Próximos pasos

### 1️⃣ **Instalar dependencias Node.js**

```bash
# Backend
cd backend
npm install

# Frontend  
cd frontend
npm install
```

### 2️⃣ **Ejecutar dos terminales**

**Terminal 1 - Backend:**
```bash
cd backend
npm run dev
# Escucha en http://localhost:3000
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
# Abre http://localhost:5173 en navegador
```

---

## 📝 Ejemplo para probar

En la página web, pega esto en la caja de código DSL:

```
crear miTienda
usar miTienda

tabla productos inicio
  id numeros clave
  nombre letras
  precio decimal
fin

tabla clientes inicio
  cliente_id numeros clave
  nombre_cliente letras
fin

tabla pedidos inicio
  pedido_id numeros clave
  cliente_id depende_de clientes
  producto_id depende_de productos
  fecha_pedido fecha
fin

cerrar
```

Presiona **"Compilar"** y verás:
- ✅ **SQL generado**
- ✅ **Estructura de la base de datos**
- ❌ **Errores** (si existen)

---

## 🔧 Archivos importantes

| Archivo | Ubicación | Descripción |
|---------|-----------|-------------|
| `antlr-3.5.2-complete.jar` | `backend/java/` | Librería ANTLR necesaria |
| `gramaticaLexer.class` | `backend/java/` | Tokenizador compilado |
| `gramaticaParser.class` | `backend/java/` | Parser compilado |
| `Compilador.class` | `backend/java/` | Compilador principal |
| `Compilador.java` | `backend/java/` | Programa que usa ANTLR |
| `server.js` | `backend/` | API Express (Node.js) |

---

## ❓ Casos de uso del lenguaje DSL

### Crear base de datos sencilla
```
crear store
usar store
tabla items inicio
  id numeros clave
  description letras
fin
cerrar
```

### Con relaciones (Foreign Keys)
```
crear empresa
usar empresa

tabla departamentos inicio
  dept_id numeros clave
  nombre letras
fin

tabla empleados inicio
  emp_id numeros clave
  nombre letras
  dept_id depende_de departamentos
fin

cerrar
```

### Tipos de datos soportados
- **numeros** → INT
- **letras** → VARCHAR(255)
- **fecha** → DATE
- **decimal** → DOUBLE

---

## 📊 Fluj de ejecución

```
Frontend (React)
    ↓
POST /compilar { texto: "..." }
    ↓
Backend (Node.js/server.js)
    ↓
Ejecuta: java -cp antlr-3.5.2-complete.jar;. Compilador
    ↓
Compilador.java (código DSL → SQL + Estructura)
    ↓
Retorna JSON con: { resultado, estructura, errores }
    ↓
Frontend muestra resultados
```

---

## ✨ Listo para usar

El compilador está completamente funcional. Solo falta instalar dependencias y ejecutar. 🎊
