import { useState } from 'react';

function App() {
  const [input, setInput] = useState('');
  const [sql, setSql] = useState('');
  const [error, setError] = useState('');
  const [dbStructure, setDbStructure] = useState('');
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [nombreBD, setNombreBD] = useState('');
  const [creatingDB, setCreatingDB] = useState(false);
  const [mysqlMessage, setMysqlMessage] = useState('');

  const compile = async () => {
    setError('');
    setSuccessMessage('');
    setMysqlMessage('');
    setSql('');
    setDbStructure('');
    setLoading(true);

    try {
      const response = await fetch('http://localhost:3000/compilar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ texto: input }),
      });

      const data = await response.json();

      if (!response.ok) {
        setError(data.error || 'Error inesperado');
      } else {
        setSql(data.resultado || 'No se generó SQL');
        setDbStructure(data.estructura || 'Estructura de BD no disponible');
        
        // Extraer nombre de BD del SQL (está después de "CREATE DATABASE")
        const match = data.resultado?.match(/CREATE DATABASE (\w+)/);
        if (match) {
          setNombreBD(match[1]);
        }
        
        if (data.errores && data.errores.length > 0) {
          // Habrá errores de compilación
          setError(data.errores.join('\n'));
        } else {
          // Si no hay errores, mostrar mensaje de éxito
          setSuccessMessage('✓ Compilación exitosa!\n\n✓ SQL generado correctamente\n✓ Estructura de BD analizada');
        }
      }
    } catch (err) {
      setError('❌ No se pudo conectar con el backend');
    } finally {
      setLoading(false);
    }
  };

  // Función para crear la BD en MySQL
  const crearBDEnMySQL = async () => {
    if (!sql) {
      setMysqlMessage('No hay SQL para ejecutar. Compila primero.');
      return;
    }

    setCreatingDB(true);
    setMysqlMessage('Conectando a MySQL...');

    try {
      const response = await fetch('http://localhost:3000/crear-bd-mysql', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          sql: sql, 
          nombreBD: nombreBD || 'base_de_datos'
        }),
      });

      const data = await response.json();

      if (response.ok && data.success) {
        const mensaje = `Base de datos '${nombreBD}' creada exitosamente en MySQL!`;
        setMysqlMessage(mensaje);
        
        // Limpiar el mensaje después de 5 segundos
        setTimeout(() => {
          setMysqlMessage('');
        }, 5000);
      } else {
        setMysqlMessage(`Error: ${data.error || 'Error al crear la BD'}`);
      }
    } catch (err) {
      setMysqlMessage('Error de conexión. ¿Está corriendo Laragon?');
    } finally {
      setCreatingDB(false);
    }
  };

  // Función para descargar SQL
  const descargarSQL = () => {
    if (!sql) {
      alert('No hay SQL para descargar');
      return;
    }
    const elemento = document.createElement('a');
    const archivo = new Blob([sql], { type: 'text/plain' });
    elemento.href = URL.createObjectURL(archivo);
    elemento.download = 'esquema.sql';
    document.body.appendChild(elemento);
    elemento.click();
    document.body.removeChild(elemento);
  };

  // Función para descargar Estructura
  const descargarEstructura = () => {
    if (!dbStructure) {
      alert('No hay estructura para descargar');
      return;
    }
    const elemento = document.createElement('a');
    const archivo = new Blob([dbStructure], { type: 'text/plain' });
    elemento.href = URL.createObjectURL(archivo);
    elemento.download = 'estructura_bd.txt';
    document.body.appendChild(elemento);
    elemento.click();
    document.body.removeChild(elemento);
  };

  return (
    <div className="h-screen flex flex-col bg-[#F2D2D5] px-4 py-4 overflow-hidden">
      <div className="flex flex-col h-full max-w-7xl mx-auto w-full">
        {/* Header fijo */}
        <div className="mb-4 text-center flex-shrink-0">
          <h1 className="mb-1 text-4xl font-bold text-rose-600">GeneradorBD</h1>
          <p className="text-rose-500 text-sm">Compilador de esquemas a SQL con análisis de estructura</p>
        </div>

        {/* Dos columnas que ocupan todo el alto restante */}
        <div className="flex flex-1 min-h-0 gap-4">
          {/* Columna izquierda: Compilador (con botón interno) y SQL */}
          <div className="flex-1 flex flex-col min-h-0 gap-3">
            {/* Tarjeta Compilador con botón dentro */}
            <div className="flex-1 rounded-2xl border border-rose-200 bg-[#F2EEEC] p-4 shadow-md flex flex-col min-h-0">
              <h2 className="mb-2 flex items-center gap-2 text-lg font-semibold text-gray-900">
                <span className="h-2 w-2 rounded-full bg-rose-500"></span>
                Compilador
              </h2>
              <textarea
                value={input}
                onChange={(event) => setInput(event.target.value)}
                placeholder="Escribe la definición de tu esquema aquí..."
                className="flex-1 w-full rounded-lg border border-rose-200 bg-white p-3 text-rose-700 placeholder-rose-300 outline-none transition focus:border-rose-400 focus:ring-1 focus:ring-rose-400/30 font-mono text-sm resize-none"
              />
              {/* Botón dentro de la tarjeta, debajo del textarea, alineado a la izquierda */}
              <div className="mt-3 flex justify-start">
                <button
                  onClick={compile}
                  disabled={loading || !input.trim()}
                  className="w-40 rounded-lg bg-gradient-to-r from-rose-500 to-rose-600 px-4 py-2 text-sm font-semibold text-white transition hover:from-rose-400 hover:to-rose-500 disabled:cursor-not-allowed disabled:opacity-50 shadow-md"
                >
                  {loading ? 'Compilando...' : 'Compilar'}
                </button>
              </div>
            </div>

            {/* Tarjeta SQL Generado */}
            <div className="flex-1 rounded-2xl border border-rose-200 bg-[#F2EEEC] p-4 shadow-md flex flex-col min-h-0">
              <div className="flex justify-between items-center mb-2">
                <h2 className="flex items-center gap-2 text-lg font-semibold text-gray-900">
                  <span className="h-2 w-2 rounded-full bg-rose-500"></span>
                  SQL Generado
                </h2>
                <button
                  onClick={descargarSQL}
                  disabled={!sql}
                  className="px-3 py-1 text-sm rounded-lg bg-rose-500 text-white hover:bg-rose-600 disabled:opacity-50 disabled:cursor-not-allowed transition"
                >
                  Descargar
                </button>
              </div>
              <div className="flex-1 overflow-y-auto rounded-lg bg-white border border-rose-100 p-3">
                <pre className="whitespace-pre-wrap text-rose-300 font-mono text-xs leading-relaxed">
                  {sql || '-- El SQL aparecerá aquí después de compilar'}
                </pre>
              </div>
            </div>
          </div>

          {/* Columna derecha: Consola y Estructura */}
          <div className="flex-1 flex flex-col min-h-0 gap-3">
            {/* Tarjeta Consola */}
            <div className="flex-1 rounded-2xl border border-rose-200 bg-[#F2EEEC] p-4 shadow-md flex flex-col min-h-0">
              <h2 className="mb-2 flex items-center gap-2 text-lg font-semibold text-gray-900">
                <span className="h-2 w-2 rounded-full bg-rose-500"></span>
                Consola / Errores
              </h2>
              <div className="flex-1 overflow-y-auto rounded-lg bg-white border border-rose-100 p-3 mb-3">
                <pre className="whitespace-pre-wrap font-mono text-xs leading-relaxed">
                  {error ? (
                    <span className="text-red-500">❌ Errores:\n\n{error}</span>
                  ) : successMessage ? (
                    <span className="text-green-500">{successMessage}</span>
                  ) : (
                    <span className="text-rose-400">✓ Compilador listo. Ingresa código para comenzar.</span>
                  )}
                </pre>
              </div>

              {/* Área de mensajes MySQL */}
              {mysqlMessage && (
                <div className="rounded-lg bg-green-50 border border-green-200 p-2 mb-2">
                  <pre className="whitespace-pre-wrap font-mono text-xs leading-relaxed text-green-600">
                    {mysqlMessage}
                  </pre>
                </div>
              )}

              {/* Botón crear BD en MySQL */}
              {sql && !error && (
                <div className="mt-3 flex justify-start">
                  <button
                    onClick={crearBDEnMySQL}
                    disabled={creatingDB}
                    className="w-40 rounded-lg bg-gradient-to-r from-rose-500 to-rose-600 px-4 py-2 text-sm font-semibold text-white transition hover:from-rose-400 hover:to-rose-500 disabled:cursor-not-allowed disabled:opacity-50 shadow-md"
                  >
                    {creatingDB ? 'Creando...' : 'Crear Base de Datos'}
                  </button>
                </div>
              )}
            </div>

            {/* Tarjeta Estructura BD */}
            <div className="flex-1 rounded-2xl border border-rose-200 bg-[#F2EEEC] p-4 shadow-md flex flex-col min-h-0">
              <div className="flex justify-between items-center mb-2">
                <h2 className="flex items-center gap-2 text-lg font-semibold text-gray-900">
                  <span className="h-2 w-2 rounded-full bg-rose-500"></span>
                  Estructura de BD
                </h2>
                <button
                  onClick={descargarEstructura}
                  disabled={!dbStructure}
                  className="px-3 py-1 text-sm rounded-lg bg-rose-500 text-white hover:bg-rose-600 disabled:opacity-50 disabled:cursor-not-allowed transition"
                >
                  Descargar
                </button>
              </div>
              <div className="flex-1 overflow-y-auto rounded-lg bg-white border border-rose-100 p-3">
                <pre className="whitespace-pre-wrap text-rose-300 font-mono text-xs leading-relaxed">
                  {dbStructure || '// La estructura se mostrará aquí después de compilar'}
                </pre>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;