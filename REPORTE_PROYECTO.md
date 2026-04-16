# REPORTE TÉCNICO: GENERADOR DE BASES DE DATOS (GeneradorBD)

## 📋 TABLA DE CONTENIDOS
1. [Descripción General](#descripción-general)
2. [Herramientas y Tecnologías](#herramientas-y-tecnologías)
3. [Arquitectura del Sistema](#arquitectura-del-sistema)
4. [Gramática ANTLR](#gramática-antlr)
5. [Compilador Java](#compilador-java)
6. [Backend (Node.js/Express)](#backend-nodejs-express)
7. [Frontend (React/Vite)](#frontend-reactvite)
8. [Flujo de Compilación](#flujo-de-compilación)
9. [Cómo Funciona](#cómo-funciona)
10. [Instalación y Ejecución](#instalación-y-ejecución)
11. [Ejemplos de Uso](#ejemplos-de-uso)
12. [Limitaciones y Mejoras Futuras](#limitaciones-y-mejoras-futuras)

---

## 🎯 Descripción General

**GeneradorBD** es un compilador web para un Lenguaje Específico de Dominio (DSL - Domain Specific Language) que permite definir esquemas de bases de datos de forma simple y declarativa, generando automáticamente:

- **SQL**: Sentencias CREATE DATABASE y CREATE TABLE en estándar SQL
- **Análisis de estructura**: Documento detallado con tablas, campos, relaciones y claves primarias
- **Integración MySQL**: Posibilidad de crear la base de datos directamente en MySQL

**Objetivo académico**: Demostrar el uso de ANTLR 3.5.2 para crear compiladores DSL con análisis sintáctico y semántico.

---

## 🛠️ Herramientas y Tecnologías

### Backend (Compilación)
| Herramienta | Versión | Propósito |
|------------|---------|----------|
| **ANTLR** | 3.5.2 | Generación automática de lexer/parser desde gramática |
| **Java** | 8 o superior | Lenguaje para implementar el compilador |
| **Node.js** | 18+ | Runtime para servidor backend |
| **Express.js** | 4.18.3 | Framework web para API REST |
| **mysql2** | 3.22.0 | Conexión a bases de datos MySQL |
| **CORS** | 2.8.5 | Permitir comunicación frontend-backend |

### Frontend
| Herramienta | Versión | Propósito |
|------------|---------|----------|
| **React** | 18+ | Librería para interfaz de usuario |
| **Vite** | 5+ | Build tool y dev server |
| **Tailwind CSS** | 3+ | Framework CSS para estilos |

### Herramientas de Desarrollo
- **Windows PowerShell**: Gestión de procesos y compilación
- **DBEaver**: Verificación de bases de datos en MySQL
- **Laragon**: Stack de desarrollo local (Apache, MySQL, PHP)

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React/Vite)                    │
│                                                             │
│  - Editor de código (Textarea)                             │
│  - Botón Compilar                                          │
│  - Panel de Errores (Consola)                              │
│  - Panel de SQL Generado                                   │
│  - Panel de Estructura de BD                               │
│  - Botón Crear Base de Datos en MySQL                      │
│  - Botones Descargar (SQL y Estructura)                    │
└────────────────────────────┬────────────────────────────────┘
                             │ HTTP POST/GET
                             │
┌────────────────────────────▼────────────────────────────────┐
│                  BACKEND (Node.js/Express)                 │
│                                                             │
│  POST /compilar: Ejecuta Java Compilador                   │
│  POST /crear-bd-mysql: Crea BD en MySQL                    │
│                                                             │
└────────────────────────────┬────────────────────────────────┘
                             │ Java Process
                             │ MySQL Connection
                             │
   ┌─────────────────────────┼─────────────────────────────┐
   │                         │                             │
┌──▼──────────────────┐  ┌───▼──────────────┐  ┌──────────▼────┐
│  COMPILADOR JAVA    │  │  BASE DE DATOS   │  │  gramatica.g  │
│                     │  │                  │  │  + Lexer/Parser│
│  - Compilador.java  │  │   MySQL          │  │  (Auto-gen)   │
│  - Entrada: stdin   │  │                  │  │               │
│  - Salida: JSON     │  │  localhost:3306  │  │  ANTLR 3.5.2  │
│                     │  │                  │  │               │
└─────────────────────┘  └──────────────────┘  └───────────────┘
```

---

## 📝 Gramática ANTLR

### Archivo: `gramatica.g`

La gramática define la sintaxis del DSL para definición de esquemas de BD.

### Estructura Base

```antlr
grammar gramatica;

@header { /* imports Java */ }
@members { /* variables y métodos globales */ }

// Reglas gramaticales...
```

### Componentes de la Gramática

#### 1. **Regla Principal: `inicio`**
```
inicio : (crear)? (usar)? instrucciones (cerrar)?
```
- Define la estructura general del programa
- Valida que existan: CREATE, USE y CLOSE
- Genera la estructura de la BD si no hay errores

#### 2. **Definición de Base de Datos**

```
crear : 'crear' id1=ID
  { 
    sql += "CREATE DATABASE " + $id1.text + ";\n";
  }

usar : 'usar' id1=ID
  {
    sql += "USE " + $id1.text + ";\n";
  }
```

**Ejecución**: Define cuál base de datos se usará
**Generación SQL**: 
```sql
CREATE DATABASE nombre_bd;
USE nombre_bd;
```

#### 3. **Definición de Tablas**

```
tabla : 'tabla' id1=ID 'inicio'
        { /* inicializar tabla */ }
        campos
        'fin'
        { /* validar y cerrar tabla */ }

campos : (campo)+

campo : id1=ID tipo 'clave'          // Campo clave primaria
      | id1=ID tipo                   // Campo normal
      | id1=ID 'depende_de' id2=ID   // Llave foránea
```

**Ejecución**: Define cada tabla y sus campos
**Validaciones**:
- Cada tabla debe tener exactamente UNA clave primaria
- Los campos deben estar dentro de 'tabla ... fin'

#### 4. **Tipos de Datos Soportados**

```
tipo : 'numeros'   // → INT en SQL
     | 'letras'    // → VARCHAR(255) en SQL
     | 'fecha'     // → DATE en SQL
     | 'decimal'   // → DOUBLE en SQL
```

#### 5. **Llaves Foráneas (Relaciones)**

```
campo : id1=ID 'depende_de' id2=ID
  {
    llavesForaneas.get(tablaActual).add($id1.text + " -> " + $id2.text);
    sql += "  " + $id1.text + " INT,\n";
  }
```

**Uso**: Define relaciones entre tablas
**Ejemplo**: `cliente_id depende_de clientes`

### Palabras Clave Reservadas

| Palabra | Propósito |
|---------|-----------|
| `crear` | Inicia creación de BD |
| `usar` | Selecciona BD a usar |
| `tabla` | Comienza definición de tabla |
| `inicio` | Marca inicio de tabla o programa |
| `fin` | Marca fin de tabla |
| `cerrar` | Finaliza el programa |
| `clave` | Marca un campo como clave primaria |
| `depende_de` | Define llave foránea/relación |
| `numeros` | Tipo INT |
| `letras` | Tipo VARCHAR |
| `fecha` | Tipo DATE |
| `decimal` | Tipo DOUBLE |

### Generación de Código

#### Variables Globales en `@members`

```java
String sql = "";                              // SQL generado
String estructura = "";                       // Reporte de estructura
Map<String, Set<String>> camposPorTabla;     // Campos de cada tabla
Map<String, String> llavesPrimarias;         // Claves primarias
Map<String, List<String>> llavesForaneas;    // Relaciones
```

#### Método: `generarEstructura()`

Crea un reporte detallado con:
- Nombre de la BD
- Lista de tablas
- Campos de cada tabla con tipos
- Relaciones entre tablas
- Claves primarias

---

## ☕ Compilador Java

### Archivo: `Compilador.java`

#### Propósito
Ejecutar el análisis léxico y sintáctico usando ANTLR, y retornar JSON con resultados.

#### Proceso de Compilación

```
1. Lectura de stdin
   └─> entrada DSL

2. Análisis léxico
   └─> gramaticaLexer tokeniza la entrada

3. Análisis sintáctico
   └─> gramaticaParser procesa los tokens
   └─> Ejecuta acciones semánticas (@members)

4. Generación de salida
   └─> Crea SQL
   └─> Crea estructura
   └─> Lista errores

5. Serialización JSON
   └─> { "resultado": "SQL", "estructura": "texto", "errores": [...] }
```

#### Código Principal

```java
import org.antlr.runtime.*;

public class Compilador {
    public static void main(String[] args) throws Exception {
        // Leer desde stdin usando ANTLRInputStream
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        
        // Crear lexer
        gramaticaLexer lexer = new gramaticaLexer(input);
        
        // Crear stream de tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Crear parser
        gramaticaParser parser = new gramaticaParser(tokens);
        
        // Ejecutar la gramática desde la regla 'inicio'
        parser.inicio();
        
        // Obtener resultados
        String sql = parser.sql;
        String estructura = parser.estructura;
        List<String> errores = parser.errores;
        boolean error = parser.error;
        
        // Retornar como JSON
        String json = construirJSON(sql, estructura, errores, error);
        System.out.println(json);
    }
    
    private static String escaparJSON(String s) {
        // Escapar caracteres especiales para JSON válido
        return s.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
```

#### Entrada/Salida

**Entrada (stdin)**:
```
crear banco
usar banco

tabla clientes inicio
  cliente_id numeros clave
  nombre letras
fin

cerrar
```

**Salida (stdout - JSON)**:
```json
{
  "resultado": "CREATE DATABASE banco;\nUSE banco;\nCREATE TABLE clientes (\n  cliente_id INT PRIMARY KEY,\n  nombre VARCHAR(255),\n);\n",
  "estructura": "========================================\n[...]"
}
```

#### Compilación

```bash
# Compilar con ANTLR en classpath
javac -cp antlr-3.5.2-complete.jar gramaticaLexer.java gramaticaParser.java Compilador.java

# Ejecutar
echo "crear test\nusar test\ntabla t inicio\n  id numeros clave\nfin\ncerrar" | \
java -cp 'antlr-3.5.2-complete.jar;.' Compilador
```

---

## 🚀 Backend (Node.js/Express)

### Archivo: `server.js`

#### Dependencias
```json
{
  "express": "^4.18.3",
  "cors": "^2.8.5",
  "mysql2": "^3.22.0"
}
```

#### Endpoints

##### 1. `POST /compilar`

**Propósito**: Ejecutar el compilador Java y retornar JSON

**Request**:
```json
{
  "texto": "crear escuela\nusar escuela\n\ntabla estudiantes inicio\n  est_id numeros clave\n  nombre letras\nfin\n\ncerrar"
}
```

**Response (éxito)**:
```json
{
  "resultado": "CREATE DATABASE escuela;\n...",
  "estructura": "========================================\n...",
  "errores": []
}
```

**Response (error)**:
```json
{
  "errores": ["Error: la tabla 'estudiantes' no tiene clave."],
  "resultado": "",
  "estructura": ""
}
```

**Implementación**:
```javascript
app.post('/compilar', (req, res) => {
  const javaProcess = spawn('java', ['-cp', 'antlr-3.5.2-complete.jar;.', 'Compilador'], 
    { cwd: javaDir }
  );
  
  // Enviar entrada
  javaProcess.stdin.write(texto);
  javaProcess.stdin.end();
  
  // Leer salida
  let output = '';
  javaProcess.stdout.on('data', (data) => { output += data.toString(); });
  
  // Retornar resultado
  javaProcess.on('close', (code) => {
    const resultado = JSON.parse(output.trim());
    res.json(resultado);
  });
});
```

##### 2. `POST /crear-bd-mysql`

**Propósito**: Ejecutar SQL en MySQL para crear la BD y tablas

**Request**:
```json
{
  "sql": "CREATE DATABASE tienda;\nUSE tienda;\nCREATE TABLE clientes (...);",
  "nombreBD": "tienda"
}
```

**Response (éxito)**:
```json
{
  "success": true,
  "mensaje": "Base de datos 'tienda' creada exitosamente en MySQL!"
}
```

**Implementación**:
```javascript
app.post('/crear-bd-mysql', async (req, res) => {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    port: 3306
  });
  
  // Dividir SQL en sentencias
  const sentencias = sql.split(';\n');
  
  // Ejecutar cada sentencia
  for (const sentencia of sentencias) {
    if (sentencia.includes('CREATE DATABASE')) {
      await connection.query(sentencia);
      // Reconectar a la nueva BD
    } else if (!sentencia.includes('USE ')) {
      await connection.execute(sentencia);
    }
  }
  
  res.json({ success: true, mensaje: "..." });
});
```

#### Configuración MySQL

```javascript
const mysqlConfig = {
  host: 'localhost',      // Desde Laragon
  user: 'root',           // Usuario por defecto
  password: '',           // Sin contraseña por defecto
  port: 3306              // Puerto estándar
};
```

---

## 🎨 Frontend (React/Vite)

### Archivo: `src/App.jsx`

#### Estructura de Componentes

```
App (componente principal)
├── Área Compilador
│   ├── Textarea (input DSL)
│   └── Botón "Compilar"
├── Área SQL Generado
│   ├── Área de texto (SQL)
│   └── Botón "Descargar"
├── Área Consola/Errores
│   ├── Mensajes de compilación
│   ├── Mensajes MySQL
│   └── Botón "Crear Base de Datos"
└── Área Estructura BD
    ├── Análisis de tablas
    └── Botón "Descargar"
```

#### Estados (hooks) principales

```javascript
const [input, setInput] = useState('');           // Código DSL
const [sql, setSql] = useState('');               // SQL generado
const [error, setError] = useState('');           // Errores compilación
const [dbStructure, setDbStructure] = useState(''); // Estructura BD
const [loading, setLoading] = useState(false);    // Compilando...
const [successMessage, setSuccessMessage] = useState(''); // Éxito compilación
const [nombreBD, setNombreBD] = useState('');     // Nombre BD
const [creatingDB, setCreatingDB] = useState(false); // Creando BD en MySQL
const [mysqlMessage, setMysqlMessage] = useState(''); // Mensaje MySQL
```

#### Función: `compile()`

```javascript
const compile = async () => {
  // 1. Limpiar estados previos
  setError('');
  setSuccessMessage('');
  setLoading(true);
  
  // 2. POST a backend
  const response = await fetch('http://localhost:3000/compilar', {
    method: 'POST',
    body: JSON.stringify({ texto: input })
  });
  
  const data = await response.json();
  
  // 3. Procesar respuesta
  if (data.errores?.length > 0) {
    setError(data.errores.join('\n'));
  } else {
    setSuccessMessage('✓ Compilación exitosa!');
    setSql(data.resultado);
    setDbStructure(data.estructura);
    
    // Extraer nombre BD del SQL
    const match = data.resultado?.match(/CREATE DATABASE (\w+)/);
    if (match) setNombreBD(match[1]);
  }
};
```

#### Función: `crearBDEnMySQL()`

```javascript
const crearBDEnMySQL = async () => {
  // Enviar SQL a backend
  const response = await fetch('http://localhost:3000/crear-bd-mysql', {
    method: 'POST',
    body: JSON.stringify({ sql, nombreBD })
  });
  
  const data = await response.json();
  
  if (data.success) {
    // Mostrar confirmación
    setMysqlMessage(`Base de datos '${nombreBD}' creada exitosamente en MySQL!`);
    
    // Desaparecer después de 5 segundos
    setTimeout(() => setMysqlMessage(''), 5000);
  }
};
```

#### Estilos Tailwind CSS

- **Colores**: Rosa (#F2D2D5 fondo, rose-500 acentos)
- **Layout**: Grid 2 columnas, responsive
- **Tipografía**: Monospace para código, sans-serif para texto
- **Componentes**: Tarjetas redondeadas con sombra

---

## 🔄 Flujo de Compilación

### Paso 1: Usuario escribe DSL
```
crear banco
usar banco
tabla clientes inicio
  id numeros clave
fin
cerrar
```

### Paso 2: Frontend envía a backend
```
HTTP POST http://localhost:3000/compilar
Body: { "texto": "crear banco..." }
```

### Paso 3: Backend ejecuta Java
```
java -cp 'antlr-3.5.2-complete.jar;.' Compilador < entrada
```

### Paso 4: Java ejecuta gramática ANTLR
```
1. ANTLRInputStream lee entrada
2. gramaticaLexer tokeniza
   Tokens: crear[ID:banco] usar[ID:banco] tabla[...]
3. gramaticaParser procesa
   Ejecuta acciones @members
4. Genera SQL y estructura
5. Retorna JSON
```

### Paso 5: Backend retorna JSON
```json
{
  "resultado": "CREATE DATABASE banco;\n...",
  "estructura": "========================================\n...",
  "errores": []
}
```

### Paso 6: Frontend muestra resultados
```
✓ SQL en panel derecho (izquierda)
✓ Estructura en panel derecho (abajo)
✓ Botón "Crear Base de Datos" visible
```

### Paso 7 (Opcional): Crear en MySQL
```
HTTP POST http://localhost:3000/crear-bd-mysql
Body: { "sql": "...", "nombreBD": "banco" }

Backend:
1. Conecta a MySQL
2. Ejecuta CREATE DATABASE banco
3. Reconecta a BD 'banco'
4. Ejecuta CREATE TABLE
5. Retorna confirmación
```

---

## 📚 Cómo Funciona

### Sintaxis DSL

#### Estructura mínima
```
crear nombre_bd
usar nombre_bd

tabla nombre_tabla inicio
  campo tipo clave
fin

cerrar
```

#### Tipos de campos
- `campo numeros` → INT
- `campo letras` → VARCHAR(255)
- `campo fecha` → DATE
- `campo decimal` → DOUBLE

#### Con clave primaria
```
tabla usuarios inicio
  id numeros clave
  nombre letras
fin
```

#### Con llaves foráneas (relaciones)
```
tabla pedidos inicio
  pedido_id numeros clave
  cliente_id depende_de clientes
fin
```

### Validaciones del Compilador

| Validación | Error |
|-----------|-------|
| Falta `crear` | "Error: no se creó ninguna base de datos." |
| Falta `usar` | "Error: no se indicó 'usar' base de datos." |
| Falta `cerrar` | "Error: falta la palabra 'cerrar'." |
| Tabla sin clave | "Error: la tabla 'X' no tiene clave." |
| Dos claves en tabla | "Error: ya existe clave en la tabla." |
| `usar` antes de `crear` | "Error: debes crear primero." |

---

## 💻 Instalación y Ejecución

### Requisitos
- Java 8 o superior
- Node.js 18+
- MySQL (Laragon)
- npm

### 1. Descargar ANTLR 3.5.2

```bash
# Descargar antlr-3.5.2-complete.jar
# Copiar a: backend/java/

# Verificar
ls backend/java/antlr-3.5.2-complete.jar
```

### 2. Compilar Java

```bash
cd backend/java

# Compilar gramática y compilador
javac -cp antlr-3.5.2-complete.jar \
  gramaticaLexer.java \
  gramaticaParser.java \
  Compilador.java

# Verificar
ls -la *.class  # Debe haber 5 archivos .class
```

### 3. Instalar dependencias Node.js

```bash
cd backend
npm install

cd ../frontend
npm install
```

### 4. Iniciar servicios

**Terminal 1 - Backend**:
```bash
cd backend
npm run dev
# Output: Backend escuchando en http://localhost:3000
```

**Terminal 2 - Frontend**:
```bash
cd frontend
npm run dev
# Output: VITE v5.0.0 ready in XXX ms
# → Local: http://localhost:5173
```

**Terminal 3 - MySQL** (Laragon):
```bash
# En Laragon, activar Apache + MySQL desde GUI
# O línea de comandos:
mysql -u root  # Debería conectar sin contraseña
```

### 5. Usar la aplicación

Abrir http://localhost:5173 en navegador

---

## 📖 Ejemplos de Uso

### Ejemplo 1: BD Simple (Escuela)

**Entrada DSL**:
```
crear escuela
usar escuela

tabla estudiantes inicio
  est_id numeros clave
  nombre letras
  email letras
fin

cerrar
```

**SQL Generado**:
```sql
CREATE DATABASE escuela;
USE escuela;
CREATE TABLE estudiantes (
  est_id INT PRIMARY KEY,
  nombre VARCHAR(255),
  email VARCHAR(255),
);
```

### Ejemplo 2: BD con Relaciones (Tienda)

**Entrada DSL**:
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

tabla clientes inicio
  cliente_id numeros clave
  email letras
fin

tabla ordenes inicio
  orden_id numeros clave
  fecha fecha
  cliente_id depende_de clientes
fin

cerrar
```

**Estructura Generada**:
```
========================================
      RESUMEN DE LA BASE DE DATOS
========================================

Base de datos creada: tienda

----------------------------------------
TABLAS CREADAS
----------------------------------------

Tabla: categorias
Atributos:
 - cat_id (numeros)  CLAVE PRINCIPAL
Relaciones:
 - No depende de ninguna tabla

Tabla: productos
Atributos:
 - prod_id (numeros)  CLAVE PRINCIPAL
 - nombre (letras)
 - precio (decimal)
 - cat_id depende de categorias
Relaciones:
 - cat_id depende de categorias

Tabla: clientes
Atributos:
 - cliente_id (numeros)  CLAVE PRINCIPAL
 - email (letras)
Relaciones:
 - No depende de ninguna tabla

Tabla: ordenes
Atributos:
 - orden_id (numeros)  CLAVE PRINCIPAL
 - fecha (fecha)
 - cliente_id depende de clientes
Relaciones:
 - cliente_id depende de clientes

========================================
```

### Ejemplo 3: Error - Falta clave primaria

**Entrada DSL**:
```
crear test
usar test

tabla usuarios inicio
  nombre letras
  edad numeros
fin

cerrar
```

**Error**:
```
❌ Errores:
Error: la tabla 'usuarios' no tiene clave.
```

---

## 🚫 Limitaciones y Mejoras Futuras

### Limitaciones Actuales
1. **Sin soporte para índices**: No hay sintaxis para UNIQUE o INDEX
2. **Sin valores por defecto**: No se pueden especificar DEFAULT
3. **Sin constraints personalizados**: Solo PK y FK básicas
4. **Tipo DOUBLE**: Solo decimal, no se especifican precisión/escala
5. **Sin procedimientos almacenados**: Solo DDL, no lógica
6. **Sin modificar tablas**: No ALTER TABLE

### Mejoras Futuras

#### Corto Plazo
- [ ] Agregar `NOT NULL` a campos
- [ ] Soporte para `UNIQUE` constraints
- [ ] Especificar longitud en VARCHAR
- [ ] Valores DEFAULT

#### Mediano Plazo
- [ ] Upgrade a ANTLR 4.x
- [ ] Soporte para vistas
- [ ] Exportar a otros motores: PostgreSQL, SQLite, SQL Server
- [ ] Importar esquemas existentes

#### Largo Plazo
- [ ] Editor visual (drag-and-drop tablas)
- [ ] Generador de API REST automático
- [ ] Dashboard de estadísticas
- [ ] Versionamiento de esquemas
- [ ] Colaboración en tiempo real

---

## 📊 Estadísticas del Proyecto

| Métrica | Valor |
|---------|-------|
| Líneas de Gramática | ~200 |
| Líneas Java (Compilador) | ~50 |
| Líneas JS Backend | ~150 |
| Líneas JSX Frontend | ~300 |
| Palabras clave DSL | 12 |
| Tipos soportados | 4 |
| Endpoints API | 2 |
| Archivos generados (ANTLR) | 3 (Lexer, Parser, Listener) |

---

## 🎓 Conceptos Académicos

### Análisis Léxico
El **gramaticaLexer** (generado por ANTLR) tokeniza la entrada:
- Palabras clave: `crear`, `tabla`, `fin`, etc.
- Identificadores: nombres de BD, tablas, campos (ID)
- Números: literales (INT)
- Espacios en blanco: ignorados

### Análisis Sintáctico
El **gramaticaParser** (generado por ANTLR) valida estructura:
- Orden de instrucciones (`crear` → `usar` → `tabla`)
- Balanceo de bloques (`tabla inicio ... fin`)
- Tipos compatibles

### Análisis Semántico
El **Compilador.java** realiza validaciones:
- Cada tabla debe tener PKclave
- Las referencias a otras tablas existen
- Los tipos son válidos

### Generación de Código
En las acciones `@members` se genera SQL:
```java
sql += "CREATE TABLE " + tablaActual + " (\n";
sql += "  " + campo + " INT PRIMARY KEY,\n";
```

---

## 📞 Contacto y Soporte

Este proyecto fue desarrollado como parte de un curso de **Lenguajes y Autómatas II**.

**Herramientas usadas en la tarea**:
- ✅ ANTLR 3.5.2 para gramática
- ✅ Java para compilador
- ✅ Node.js/Express para API
- ✅ React/Vite para interfaz
- ✅ MySQL para persistencia

---

## 📄 Licencia

Proyecto académico. Uso libre para fines educativos.

**Autor**: [Tu nombre]  
**Fecha**: Abril 2026  
**Institución**: [Tu institución]

---

## 🔗 Referencias

- **ANTLR 3.5.2 Docs**: https://www.antlr3.org/
- **Express.js**: https://expressjs.com/
- **React**: https://react.dev/
- **MySQL**: https://www.mysql.com/
- **SQL Standard**: https://en.wikipedia.org/wiki/SQL

---

**FIN DEL REPORTE**
