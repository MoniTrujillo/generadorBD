import express from 'express';
import cors from 'cors';
import { execFile } from 'node:child_process';
import { fileURLToPath } from 'node:url';
import path from 'node:path';

const app = express();
const PORT = process.env.PORT || 3000;
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

app.use(cors());
app.use(express.json());

app.post('/compilar', (req, res) => {
  const { texto } = req.body;

  if (!texto || typeof texto !== 'string') {
    return res.status(400).json({ error: 'El campo texto es obligatorio.' });
  }

  const javaProgram = path.join(__dirname, 'java', 'Compilador.java');

  execFile('javac', [javaProgram], (compileError, stdout, stderr) => {
    if (compileError) {
      return res.status(500).json({ error: `Error al compilar Java:\n${stderr}` });
    }

    execFile('java', ['-cp', path.join(__dirname, 'java'), 'Compilador'], { input: texto }, (runError, runStdout, runStderr) => {
      if (runError) {
        return res.status(500).json({ error: `Error al ejecutar Java:\n${runStderr}` });
      }

      res.json({ resultado: runStdout.trim() });
    });
  });
});

app.listen(PORT, () => {
  console.log(`Backend escuchando en http://localhost:${PORT}`);
});
