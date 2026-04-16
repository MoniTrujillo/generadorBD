import express from 'express';
import cors from 'cors';
import { spawn } from 'node:child_process';
import mysql from 'mysql2/promise';
import { fileURLToPath } from 'node:url';
import path from 'node:path';

const app = express();
const PORT = process.env.PORT || 3000;
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Configuración de MySQL 
const mysqlConfig = {
  host: 'localhost',
  user: 'root',
  password: '', 
  port: 3306
};

app.use(cors());
app.use(express.json());

app.post('/compilar', (req, res) => {
  const { texto } = req.body;

  if (!texto || typeof texto !== 'string') {
    return res.status(400).json({ error: 'El campo texto es obligatorio.' });
  }

  const javaDir = path.join(__dirname, 'java');
  
  // Usar spawn en lugar de execFile para mejor control de streams
  const javaProcess = spawn('java', ['-cp', 'antlr-3.5.2-complete.jar;.', 'Compilador'], 
    { 
      cwd: javaDir
    }
  );

  let output = '';
  let errorOutput = '';

  // Enviar el texto como stdin
  javaProcess.stdin.write(texto);
  javaProcess.stdin.end(); // Cerrar stdin para señalar EOF

  // Leer stdout
  javaProcess.stdout.on('data', (data) => {
    output += data.toString();
  });

  // Leer stderr
  javaProcess.stderr.on('data', (data) => {
    errorOutput += data.toString();
  });

  // Manejar proceso completado
  javaProcess.on('close', (code) => {
    if (code !== 0) {
      return res.status(500).json({ error: `Error al ejecutar Java:\n${errorOutput}` });
    }

    try {
      // Parsear el JSON retornado por Java
      const resultado = JSON.parse(output.trim());
      res.json(resultado);
    } catch (jsonError) {
      res.json({ 
        resultado: output.trim(),
        error: 'No se pudo parsear la respuesta'
      });
    }
  });

  // Manejar errores del proceso
  javaProcess.on('error', (err) => {
    res.status(500).json({ error: `Error al iniciar Java: ${err.message}` });
  });
});

// Nuevo endpoint para ejecutar SQL en MySQL
app.post('/crear-bd-mysql', async (req, res) => {
  const { sql, nombreBD } = req.body;

  if (!sql || typeof sql !== 'string') {
    return res.status(400).json({ error: 'El SQL es obligatorio.' });
  }

  if (!nombreBD || typeof nombreBD !== 'string') {
    return res.status(400).json({ error: 'El nombre de la BD es obligatorio.' });
  }

  try {
    // Primera conexión: crear la BD
    let connection = await mysql.createConnection(mysqlConfig);

    // Dividir el SQL en sentencias individuales
    const sentencias = sql.split(';\n').filter(s => s.trim().length > 0);
    
    let bdCreada = false;

    for (let i = 0; i < sentencias.length; i++) {
      let sentencia = sentencias[i].trim();
      
      // Saltar if empty
      if (sentencia.length === 0) continue;
      
      // Agregar punto y coma si no lo tiene
      if (!sentencia.endsWith(';')) {
        sentencia += ';';
      }

      try {
        // Para CREATE DATABASE, USE y comentarios usar query()
        if (sentencia.toUpperCase().includes('CREATE DATABASE') || 
            sentencia.toUpperCase().includes('USE ')) {
          
          if (sentencia.toUpperCase().includes('CREATE DATABASE')) {
            await connection.query(sentencia);
            bdCreada = true;
            // Cerrar y reconectar a la nueva BD
            await connection.end();
            connection = await mysql.createConnection({
              ...mysqlConfig,
              database: nombreBD
            });
          } else if (sentencia.toUpperCase().includes('USE ')) {
            // Ya estamos conectados a la BD correcta, saltar USE
            continue;
          }
        } else {
          // Para CREATE TABLE y otros comandos, usar execute()
          // Pero limpiar comas finales extras
          sentencia = sentencia.replace(/,(\s*\);)/g, ');');
          await connection.execute(sentencia);
        }
      } catch (err) {
        await connection.end();
        return res.status(400).json({ 
          error: `Error al ejecutar SQL: ${err.message}` 
        });
      }
    }

    await connection.end();

    res.json({ 
      success: true, 
      mensaje: `✓ Base de datos '${nombreBD}' creada exitosamente en MySQL! Todas las tablas fueron creadas.` 
    });

  } catch (err) {
    res.status(500).json({ 
      error: `Error de conexión a MySQL: ${err.message}. ¿Está corriendo Laragon?` 
    });
  }
});

app.listen(PORT, () => {
  console.log(`Backend escuchando en http://localhost:${PORT}`);
});
