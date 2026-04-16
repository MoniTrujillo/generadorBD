# 📋 GeneradorBD - Compilador ANTLR

## 🎯 ¿Qué hace este proyecto?

Compilador de lenguaje de alto nivel para crear esquemas de Base de Datos en SQL.

**Flujo:**
```
Código DSL → ANTLR Lexer/Parser → SQL + Descripción de Estructura
```

---

## 📁 Estructura de archivos

### Backend Java (en `backend/java/`)
- **gramaticaLexer.java**: Tokenizador ANTLR
- **gramaticaParser.java**: Parser ANTLR
- **Compilador.java**: Programa principal que usa ANTLR
- **gramatica.g**: Archivo de gramática ANTLR (referencia)

### Frontend (en `frontend/`)
- React + Vite
- Muestra: Editor, Errores, SQL generado, Estructura de BD

### Backend Node.js
- `server.js`: API Express que ejecuta el compilador Java

---

## 🚀 Instalación paso a paso

### 1️⃣ **Descargar biblioteca ANTLR Runtime** (IMPORTANTE)

El compilador Java necesita `antlr-3.5.2-runtime.jar` para funcionar.

**Opción A: Descargar manual**
```
1. Ir a: https://www.antlr3.org/download/
2. Descargar: antlr-3.5.2-runtime.jar
3. Copiar a: backend/java/
```

**Opción B: Con Maven (si tienes Maven instalado)**
```bash
cd backend/java
mvn dependency:copy -Dartifact=org.antlr:antlr-runtime:3.5.2:jar -DoutputDirectory=.
```

### 2️⃣ **Instalar dependencias Frontend**
```bash
cd frontend
npm install
```

### 3️⃣ **Instalar dependencias Backend**
```bash
cd backend
npm install
```

---

## ▶️ Ejecutar

### Terminal 1: Iniciar Backend
```bash
cd backend
npm run dev
# Escucha en http://localhost:3000
```

### Terminal 2: Iniciar Frontend
```bash
cd frontend
npm run dev
# Abre en http://localhost:5173
```

---

## 📝 Ejemplo de código DSL

```
crear miBD
usar miBD

tabla usuarios inicio
  id numeros clave
  nombre letras
  fecha_registro fecha
fin

tabla ordenes inicio
  orden_id numeros clave
  usuario_id depende_de usuarios
  total_venta decimal
  fecha_orden fecha
fin

cerrar
```

---

## 🔴 Solución de problemas

### ❌ Error: "Cannot find symbol: symbol: class ANTLRStringStream"
**Solución**: Descargar e instalar antlr-3.5.2-runtime.jar (ver paso 1)

### ❌ Error: "class not found: gramaticaLexer"
**Solución**: Asegurar que archivos .class estén compilados:
```bash
cd backend/java
javac -cp . gramaticaLexer.java
javac -cp . gramaticaParser.java
javac -cp . Compilador.java
```

### ❌ Frontend no responde
**Solución**: Verificar que server.js está corriendo:
```bash
# Ver si puerto 3000 está ocupado
netstat -ano | grep :3000
```

---

## 🛠️ Compilación manual de Java

Si necesitas recompilar los archivos ANTLR:

```bash
cd backend/java

# Compilar lexer y parser
javac -cp . gramaticaLexer.java
javac -cp . gramaticaParser.java

# Compilar compilador
javac -cp . Compilador.java

# Ejecutar prueba
java -cp . Compilador
# (escribe código DSL y presiona Ctrl+D)
```

---

## 📊 Flujo de ejecución

```
┌─────────────────┐
│  Frontend (React)
│  - Editor DSL
│  - Botón "Compilar"
└────────┬────────┘
         │ POST /compilar
         │ JSON { texto: "..." }
         ▼
┌─────────────────────────┐
│  Backend (Node.js)
│  server.js
│  - Recibe texto
│  - Compila Compilador.java
│  - Ejecuta Java
└────────┬────────────────┘
         │ spawn java -cp . Compilador
         │ stdin: texto
         ▼
┌────────────────────────────────┐
│  Compilador.java
│  1. ANTLRStringStream
│  2. gramaticaLexer
│  3. CommonTokenStream
│  4. gramaticaParser
│  5. parser.inicio()
│  6. Retorna JSON
└────────┬─────────────────────┘
         │ JSON response
         │ { resultado, estructura, errores }
         ▼
┌──────────────────────┐
│  Frontend muestra
│  - SQL generado
│  - Estructura de BD
│  - Errores (si hay)
└──────────────────────┘
```

---

## ✅ Checklist de instalación

- [ ] Descargar antlr-3.5.2-runtime.jar
- [ ] Copiar a backend/java/
- [ ] Compilar archivos Java (javac)
- [ ] npm install en frontend/
- [ ] npm install en backend/
- [ ] Ejecutar backend (node server.js)
- [ ] Ejecutar frontend (npm run dev)
- [ ] Probar con ejemplo DSL

---

## 📞 Soporte

Si hay problemas, envía:
1. Mensaje de error exacto
2. Qué código DSL estás probando
3. Salida de: `java -version`
