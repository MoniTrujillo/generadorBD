# GUÍA COMPLETA: GENERADORBD
## Cómo explicar el proyecto al maestro

---

## 1. RESUMEN EJECUTIVO

**GeneradorBD** es un compilador web que traduce un **Lenguaje Específico de Dominio (DSL)** creado por nosotros a **SQL automáticamente**. 

El flujo es: Usuario escribe → Frontend envía → Backend compila con Java/ANTLR → SQL se genera → Se crea la BD en MySQL.

---

## 2. ARQUITECTURA DEL PROYECTO

```
┌─────────────────────┐
│  FRONTEND (React)   │  Puerto 5173
│  - Interfaz gráfica │
│  - 4 paneles        │
└──────────┬──────────┘
           │ HTTP POST
           ▼
┌─────────────────────┐
│ BACKEND (Express)   │  Puerto 3000
│ - API REST          │
│ - Orquestación      │
└──────────┬──────────┘
           │ spawn Java
           ▼
┌─────────────────────┐
│ JAVA COMPILADOR     │
│ - ANTLR Lexer       │
│ - ANTLR Parser      │
│ - Generador SQL     │
└─────────────────────┘
           │
           ▼
┌─────────────────────┐
│ MySQL (Laragon)     │  Puerto 3306
│ - Almacena BD       │
└─────────────────────┘
```

---

## 3. COMPONENTE 1: GRAMATICAL LEXER

### ¿Qué es?
El **Lexer** es la primera fase del compilador. Su trabajo es leer el texto que el usuario escribe y **convertirlo en tokens** (palabras pequeñas que entienden).

### Ejemplo:
```
Entrada: "crear empresa usar empresa tabla usuarios inicio id_usuario numeros clave fin cerrar"

Lexer produce tokens:
[CREAR, ID('empresa'), USAR, ID('empresa'), TABLA, ID('usuarios'), INICIO, 
 ID('id_usuario'), NUMEROS, CLAVE, FIN, CERRAR]
```

### ¿Dónde se define?
En **`backend/java/gramatica.g`** - Líneas 258-270:

```antlr
// DEFINICIÓN DE PALABRAS CLAVE COMO TOKENS
CREAR      : 'crear' ;
USAR       : 'usar' ;
TABLA      : 'tabla' ;
INICIO     : 'inicio' ;
FIN        : 'fin' ;
CERRAR     : 'cerrar' ;
DEPENDE_DE : 'depende_de' ;
CLAVE      : 'clave' ;
NUMEROS    : 'numeros' ;
LETRAS     : 'letras' ;
FECHA      : 'fecha' ;
DECIMAL    : 'decimal' ;
ID         : ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '0'..'9' | '_')* ;
WS         : (' ' | '\n' | '\r' | '\t')+ { $channel = HIDDEN; }
```

### Se genera automáticamente:
Cuando ejecutas: `java -jar antlr-3.5.2-complete.jar gramatica.g`
Se crea: **`gramaticaLexer.java`** (NO lo edites, es automático)

### Se invoca desde:
**`backend/java/Compilador.java`** - Línea 9:
```java
ANTLRInputStream input = new ANTLRInputStream(System.in);
gramaticaLexer lexer = new gramaticaLexer(input);  // ← Aquí se crea el Lexer
CommonTokenStream tokens = new CommonTokenStream(lexer);
```

---

## 4. COMPONENTE 2: GRAMATICAL PARSER

### ¿Qué es?
El **Parser** es la segunda fase. Recibe los tokens del Lexer y verifica que **sigan las reglas de la gramática**. Si todo es correcto, ejecuta **acciones semánticas** (código que genera SQL).

### ¿Dónde se define?
En **`backend/java/gramatica.g`** - Las 250+ líneas de reglas de gramática

### Ejemplo de regla:
```antlr
// Regla: Una tabla debe tener inicio, campos, y fin
tabla  
    : TABLA id1=ID INICIO    // Debe empezar con: tabla <nombre> inicio
    {
        tablaActual = $id1.text;
        tienePK = false;
        // ... código que prepara para recibir campos
    }
    campos                     // Luego debe haber campos
    FIN                        // Y terminar con fin
    {
        // Validar que tiene clave primaria
        if(!tienePK){
            errores.add("Error: la tabla '" + tablaActual + "' no tiene clave.");
            error = true;
        }
        sql += ");\n";         // ← AQUÍ SE GENERA PARTE DEL SQL
    }
;
```

### Se genera automáticamente:
Cuando ejecutas: `java -jar antlr-3.5.2-complete.jar gramatica.g`
Se crea: **`gramaticaParser.java`** (NO lo edites, es automático)

### Se invoca desde:
**`backend/java/Compilador.java`** - Línea 11:
```java
CommonTokenStream tokens = new CommonTokenStream(lexer);
gramaticaParser parser = new gramaticaParser(tokens);  // ← Aquí se crea el Parser
parser.inicio();  // ← Aquí se ejecuta la compilación
```

---

## 5. GENERACIÓN DE ARCHIVOS SQL Y TXT

### ¿Cómo se crean?

**NO se crean en el disco**. Se generan en **memoria** durante la compilación:

1. **El SQL** se construye en la variable `String sql` dentro de la gramática
2. **La estructura** se construye en la variable `String estructura` dentro de la gramática
3. Se devuelven como **JSON al Frontend**

### En `gramatica.g` - Líneas 14-16:
```java
@members {
    String sql = "";           // ← Aquí se ACUMULA el SQL
    String estructura = "";    // ← Aquí se genera el reporte
    
    // Cuando defines una tabla:
    // sql += "CREATE TABLE " + tablaActual + " (\n";
    
    // Cuando defines un campo numérico:
    // sql += "  " + $id1.text + " INT,\n";
}
```

### Ejemplo de cómo se acumula:
```
1. User entra: "crear empresa usar empresa tabla usuarios inicio id_usuario numeros clave fin cerrar"

2. Parser encuentra "crear empresa":
   sql = "CREATE DATABASE empresa;\n"

3. Parser encuentra "usar empresa":
   sql += "USE empresa;\n"

4. Parser encuentra "tabla usuarios":
   sql += "CREATE TABLE usuarios (\n"

5. Parser encuentra "id_usuario numeros clave":
   sql += "  id_usuario INT PRIMARY KEY,\n"

6. Parser encuentra "fin":
   sql += ");\n"

RESULTADO FINAL:
sql = "CREATE DATABASE empresa;
       USE empresa;
       CREATE TABLE usuarios (
         id_usuario INT PRIMARY KEY,
       );"
```

### Se devuelve en Compilador.java - Líneas 29-30:
```java
} else {
    // Sin errores - agregar SQL y estructura
    json += "\"resultado\": \"" + escaparJSON(parser.sql) + "\", ";
    json += "\"estructura\": \"" + escaparJSON(parser.estructura) + "\"";
}
```

### Se descarga desde Frontend:
**`frontend/src/App.jsx`** - Funciones `descargarSQL()` y `descargarEstructura()`:
```javascript
const descargarSQL = () => {
    const elemento = document.createElement('a');
    const archivo = new Blob([sql], { type: 'text/plain' });  // ← Crea archivo en memoria
    elemento.href = URL.createObjectURL(archivo);
    elemento.download = 'esquema.sql';  // ← Nombre del archivo
    document.body.appendChild(elemento);
    elemento.click();  // ← Descarga el archivo
    document.body.removeChild(elemento);
};
```

---

## 6. CONEXIÓN BACKEND-FRONTEND

### El flujo HTTP completo:

```
FRONTEND → BACKEND → JAVA → FRONTEND
```

### Paso 1: Frontend envía el código DSL

**`frontend/src/App.jsx`** - Línea 14-25 (Función `compile()`):
```javascript
const compile = async () => {
    const response = await fetch('http://localhost:3000/compilar', {  // ← Envía POST
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ texto: input })  // ← Envía el DSL del usuario
    });
    
    const data = await response.json();  // ← Recibe JSON
    setSql(data.resultado);              // ← Guarda SQL
    setDbStructure(data.estructura);     // ← Guarda estructura
};
```

### Paso 2: Backend recibe y ejecuta Java

**`backend/server.js`** - Líneas 24-72 (Endpoint `/compilar`):
```javascript
app.post('/compilar', (req, res) => {
    const { texto } = req.body;  // ← Recibe el DSL
    
    // Ejecuta Java como proceso externo
    const javaProcess = spawn('java', 
        ['-cp', 'antlr-3.5.2-complete.jar;.', 'Compilador'],  // ← Comando Java
        { cwd: javaDir }
    );
    
    javaProcess.stdin.write(texto);  // ← Envía DSL por stdin
    javaProcess.stdin.end();          // ← Señala fin de entrada
    
    javaProcess.stdout.on('data', (data) => {
        output += data.toString();  // ← Recibe JSON de Java
    });
    
    javaProcess.on('close', (code) => {
        const resultado = JSON.parse(output.trim());  // ← Parsea JSON
        res.json(resultado);  // ← Envía al Frontend
    });
});
```

### Paso 3: Frontend recibe y muestra

```javascript
setSql(data.resultado);              // Muestra SQL en panel
setDbStructure(data.estructura);     // Muestra estructura en panel
```

### Diagrama visual:

```
Usuario escribe en textarea
      ↓
✓ Compilación exitosa!
      ↓
Envía POST a http://localhost:3000/compilar
      │ {"texto": "crear empresa..."}
      ↓
Backend recibe con spawn('java', ...)
      │
      ├─ Ejecuta Java
      │ stdin: "crear empresa..."
      │ stdout: JSON con SQL y estructura
      │
      ↓
Backend devuelve JSON al Frontend
      │ {"resultado": "CREATE DATABASE...", "estructura": "..."}
      ↓
Frontend muestra en pantalla
      ↓
Usuario puede ver SQL y descargar archivos
```

---

## 7. CREACIÓN DE BASE DE DATOS EN MYSQL

### El endpoint `/crear-bd-mysql`

**`backend/server.js`** - Líneas 77-150:
```javascript
app.post('/crear-bd-mysql', async (req, res) => {
    const { sql, nombreBD } = req.body;  // ← Recibe SQL y nombre
    
    // Conectar a MySQL
    let connection = await mysql.createConnection(mysqlConfig);
    
    // Dividir SQL en sentencias individuales
    const sentencias = sql.split(';\n').filter(s => s.trim().length > 0);
    
    for (let sentencia of sentencias) {
        // 1. CREATE DATABASE: usar query()
        if (sentencia.toUpperCase().includes('CREATE DATABASE')) {
            await connection.query(sentencia);
            await connection.end();
            
            // 2. Reconectar a la BD específica
            connection = await mysql.createConnection({
                ...mysqlConfig,
                database: nombreBD  // ← Se conecta a la BD creada
            });
        }
        // 3. CREATE TABLE: usar execute()
        else {
            await connection.execute(sentencia);
        }
    }
    
    await connection.end();
    res.json({ success: true, mensaje: '✓ BD creada' });
});
```

### Flujo de creación:

```
1. User presiona "Crear Base de Datos"
   ↓
2. Frontend envía: POST http://localhost:3000/crear-bd-mysql
   { sql: "CREATE DATABASE...\nCREATE TABLE...", nombreBD: "empresa" }
   ↓
3. Backend se conecta a MySQL (localhost, user: root, password: '')
   ↓
4. Ejecuta: CREATE DATABASE empresa;
   (si funciona, se desconecta y reconecta a esa BD)
   ↓
5. Ejecuta: CREATE TABLE usuarios (...)
   ↓
6. Ejecuta: CREATE TABLE productos (...)
   ↓
7. Desconecta de MySQL
   ↓
8. Devuelve JSON: {"success": true, "mensaje": "✓ BD creada"}
   ↓
9. Frontend muestra mensaje verde por 5 segundos
```

### Configuración de MySQL:

**`backend/server.js`** - Líneas 14-19:
```javascript
const mysqlConfig = {
  host: 'localhost',      // ← Laragon en local
  user: 'root',           // ← Usuario por defecto
  password: '',           // ← Sin contraseña (configuración Laragon)
  port: 3306              // ← Puerto MySQL estándar
};
```

**IMPORTANTE**: Laragon debe estar corriendo antes de crear la BD.

---

## 8. FRONTEND - LA INTERFAZ GRÁFICA

### Archivos del Frontend:

- **`frontend/src/App.jsx`** - Componente principal (250 líneas)
- **`frontend/src/index.css`** - Estilos globales
- **`frontend/package.json`** - Dependencias (React, Vite)

### Estructura de 4 paneles:

**`frontend/src/App.jsx`** - Las 4 tarjetas principales:

```javascript
// Panel 1: COMPILADOR (textarea + botón)
<textarea
    value={input}
    onChange={(e) => setInput(e.target.value)}
    placeholder="Escribe tu DSL aquí..."
/>
<button onClick={compile}>Compilar</button>

// Panel 2: SQL GENERADO (muestra el SQL)
<pre>{sql || '-- SQL aparecerá aquí'}</pre>
<button onClick={descargarSQL}>Descargar</button>

// Panel 3: CONSOLA / ERRORES
<pre>
    {error ? <span style={{color: 'red'}}>❌ {error}</span> 
           : <span style={{color: 'green'}}>✓ {successMessage}</span>}
</pre>
<button onClick={crearBDEnMySQL}>Crear Base de Datos</button>

// Panel 4: ESTRUCTURA DE BD
<pre>{dbStructure}</pre>
<button onClick={descargarEstructura}>Descargar</button>
```

### Estados (hooks):

```javascript
const [input, setInput] = useState('');              // Lo que escribe el user
const [sql, setSql] = useState('');                  // SQL generado
const [error, setError] = useState('');              // Errores de compilación
const [dbStructure, setDbStructure] = useState('');  // Análisis de estructura
const [loading, setLoading] = useState(false);       // "Compilando..."
const [successMessage, setSuccessMessage] = useState(''); // "✓ Compilación exitosa"
const [nombreBD, setNombreBD] = useState('');        // Nombre extraído
const [creatingDB, setCreatingDB] = useState(false); // "Creando BD..."
const [mysqlMessage, setMysqlMessage] = useState(''); // Mensaje de MySQL
```

### Colores y estilos:

- **Fondo**: `#F2D2D5` (Rosa suave)
- **Acentos**: `rose-500` (Rosa)
- **Éxito**: Verde
- **Errores**: Rojo
- **Framework CSS**: Tailwind CSS

---

## 9. CICLO DE VIDA COMPLETO

### Desde que el user abre la web hasta que crea la BD:

```
┌─ 1. USER VE LA INTERFAZ ─────────────────────────────┐
│ • 4 paneles rosa                                       │
│ • Textarea vacío listo para código DSL                │
│ • Botón "Compilar" deshabilitado                      │
└──────────────────────────────────────────────────────┘

┌─ 2. USER ESCRIBE EN TEXTAREA ──────────────────────┐
│ crear empresa                                        │
│ usar empresa                                         │
│ tabla usuarios inicio                               │
│   id_usuario numeros clave                          │
│   nombre letras                                      │
│ fin                                                  │
│ cerrar                                              │
│ [Button "Compilar" ahora HABILITADO]               │
└──────────────────────────────────────────────────────┘

┌─ 3. USER PRESIONA "COMPILAR" ──────────────────────┐
│ Button dice "Compilando..." (deshabilitado)         │
│ Frontend envía POST a /compilar                     │
│ Paneles SQL, Consola y Estructura se limpian       │
└──────────────────────────────────────────────────────┘

┌─ 4. BACKEND EJECUTA JAVA ──────────────────────────┐
│ spawn('java', ['-cp', '...', 'Compilador'])        │
│ stdin recibe: "crear empresa usar empresa..."      │
│ Java ejecuta: ANTLRInputStream → Lexer → Parser    │
│ Parser valida y genera SQL + Estructura            │
│ stdout devuelve JSON                               │
└──────────────────────────────────────────────────────┘

┌─ 5. FRONTEND RECIBE RESPUESTA ──────────────────────┐
│ setSql(data.resultado)                             │
│ setDbStructure(data.estructura)                    │
│ Panel "SQL Generado" muestra:                      │
│ CREATE DATABASE empresa;                           │
│ USE empresa;                                        │
│ CREATE TABLE usuarios (...)                        │
│                                                     │
│ Panel "Consola" muestra:                           │
│ ✓ Compilación exitosa!                            │
│ ✓ SQL generado correctamente                      │
│ ✓ Estructura de BD analizada                      │
│                                                     │
│ Panel "Estructura" muestra:                        │
│ Tabla: usuarios                                     │
│   - id_usuario (numeros) CLAVE PRINCIPAL          │
│   - nombre (letras)                                │
│   No depende de ninguna tabla                      │
│                                                     │
│ Button "Crear Base de Datos" habilitado            │
└──────────────────────────────────────────────────────┘

┌─ 6. USER PRESIONA "CREAR BASE DE DATOS" ───────────┐
│ Consola muestra: "Conectando a MySQL..."           │
│ Button deshabilitado, dice "Creando BD..."         │
│ Frontend envía POST a /crear-bd-mysql              │
│ { sql: "CREATE DATABASE...", nombreBD: "empresa" }│
└──────────────────────────────────────────────────────┘

┌─ 7. BACKEND CREA EN MYSQL ─────────────────────────┐
│ mysql.createConnection(config)                     │
│ connection.query("CREATE DATABASE empresa;")       │
│ connection.end()  // desconecta                    │
│ connection = mysql.createConnection({database: 'empresa'}) // reconecta
│ connection.execute("CREATE TABLE usuarios (...)")  │
│ connection.end()  // desconecta                    │
│ Devuelve: {"success": true, "mensaje": "..."}     │
└──────────────────────────────────────────────────────┘

┌─ 8. FRONTEND MUESTRA ÉXITO ────────────────────────┐
│ Consola muestra en VERDE:                          │
│ "✓ Base de datos 'empresa' creada exitosamente!" │
│ Mensaje desaparece después de 5 segundos           │
│ BD está ahora en MySQL (visible en DBEaver)       │
└──────────────────────────────────────────────────────┘

┌─ 9. USER PUEDE DESCARGAR ARCHIVOS ────────────────┐
│ • Botón "Descargar" en SQL → esquema.sql          │
│ • Botón "Descargar" en Estructura → estructura_bd.txt
│ • Archivos se descargan a la carpeta Downloads    │
└──────────────────────────────────────────────────────┘
```

---

## 10. ARCHIVOS PRINCIPALES QUE REVISAR

### Frontend (React):
- **`frontend/src/App.jsx`** - Toda la interfaz (250 líneas)
- **`frontend/src/main.jsx`** - Punto de entrada
- **`frontend/vite.config.js`** - Configuración Vite (puerto 5173)

### Backend (Express + Node.js):
- **`backend/server.js`** - API REST completa (150 líneas)
- **`backend/package.json`** - Dependencias (express, cors, mysql2)

### Java (ANTLR):
- **`backend/java/gramatica.g`** - Definición de la gramática (250+ líneas)
- **`backend/java/Compilador.java`** - Punto de entrada Java (55 líneas)
- **`backend/java/gramaticaLexer.java`** - Auto-generado, NO EDITAR
- **`backend/java/gramaticaParser.java`** - Auto-generado, NO EDITAR
- **`backend/java/antlr-3.5.2-complete.jar`** - Librería ANTLR

---

## 11. COMANDOS IMPORTANTES

### Para regenerar Lexer y Parser:
```bash
cd backend/java
java -jar antlr-3.5.2-complete.jar gramatica.g
```

### Para compilar Java:
```bash
cd backend/java
javac -cp "antlr-3.5.2-complete.jar;." *.java
```

### Para ejecutar todo:
```bash
# Terminal 1: Backend
cd backend
npm run dev  # Puerto 3000

# Terminal 2: Frontend
cd frontend
npm run dev  # Puerto 5173

# Asegúrate que Laragon está corriendo para MySQL
```

---

## 12. ERRORES COMUNES Y SOLUCIONES

### Error: "Cannot find symbol: class gramaticaLexer"
**Solución**: Regenerar con ANTLR
```bash
java -jar antlr-3.5.2-complete.jar gramatica.g
javac -cp "antlr-3.5.2-complete.jar;." *.java
```

### Error: "Port 3000 already in use"
**Solución**: Matar el proceso o usar otro puerto
```bash
taskkill /F /PID $(Get-NetTCPConnection -LocalPort 3000 | % OwningProcess)
```

### Error: "Connection refused (MySQL)"
**Solución**: Asegúrate que Laragon está corriendo y MySQL está iniciado

### Error: "Names don't match" (usar ≠ crear)
**Solución**: Usar el mismo nombre en ambas líneas
```
✓ crear empresa      ← nombre correcto
  usar empresa       ← mismo nombre

✗ crear empresa      ← error
  usar empresa2      ← diferente
```

---

## 13. PUNTOS CLAVE A EXPLICAR

Cuando expongas, enfatiza:

1. **El DSL**: "Es nuestro propio lenguaje de programación simple para definir BDs"

2. **Lexer vs Parser**:
   - Lexer: Convierte texto en tokens (palabras)
   - Parser: Verifica que los tokens sigan las reglas y genera SQL

3. **ANTLR**: "Es una herramienta que genera automáticamente Lexer y Parser desde una gramática"

4. **Generación de SQL**: "Mientras el Parser valida, simultáneamente construye el SQL en memoria"

5. **Arquitectura de 3 capas**:
   - Frontend: Interfaz bonita
   - Backend: API que orquesta
   - Java/ANTLR: Motor de compilación

6. **Validaciones**: "Si el usuario no sigue las reglas, mostramos errores específicos"

7. **Creación real en MySQL**: "El SQL no es solo texto, se ejecuta de verdad en una BD"

---

## 14. CÓDIGO DE EJEMPLO COMPLETO

**Entrada del usuario**:
```
crear mibase
usar mibase

tabla usuarios inicio
  id_usuario numeros clave
  nombre letras
  email letras
fin

tabla productos inicio
  id_producto numeros clave
  usuario_propietario depende_de usuarios
fin

cerrar
```

**SQL que genera**:
```sql
CREATE DATABASE mibase;
USE mibase;
CREATE TABLE usuarios (
  id_usuario INT PRIMARY KEY,
  nombre VARCHAR(255),
  email VARCHAR(255)
);
CREATE TABLE productos (
  id_producto INT PRIMARY KEY,
  usuario_propietario INT,
  FOREIGN KEY (usuario_propietario) REFERENCES usuarios(id_usuario)
);
```

**Estructura que genera**:
```
========================================
      RESUMEN DE LA BASE DE DATOS
========================================

Base de datos creada: mibase

TABLAS CREADAS

Tabla: usuarios

Atributos:
 - id_usuario (numeros)  CLAVE PRINCIPAL
 - nombre (letras)
 - email (letras)

Relaciones:
 - No depende de ninguna tabla

Tabla: productos

Atributos:
 - id_producto (numeros)  CLAVE PRINCIPAL
 - usuario_propietario (numeros)

Relaciones:
 - usuario_propietario depende de usuarios

========================================
```

---

## FIN DE LA GUÍA

¡Ahora tienes todo lo que necesitas para explicar el proyecto! Suerte con la presentación 🚀
