import { useState } from 'react';

function App() {
  const [input, setInput] = useState('');
  const [sql, setSql] = useState('');
  const [error, setError] = useState('');
  const [dbStructure, setDbStructure] = useState('');
  const [loading, setLoading] = useState(false);

  const compile = async () => {
    setError('');
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
      }
    } catch (err) {
      setError('No se pudo conectar con el backend');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#F2D2D5] px-4 py-8">
      <div className="mx-auto w-full max-w-6xl">
        {/* Header */}
        <div className="mb-8 text-center">
          <h1 className="mb-2 text-5xl font-bold text-rose-600">
            GeneradorBD
          </h1>
          <p className="text-rose-500">Compilador de esquemas a SQL con análisis de estructura</p>
        </div>

        {/* Primera fila: Editor (izquierda) y Consola + botón (derecha) */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Editor Section - altura fija 500px */}
          <div className="rounded-2xl border border-rose-200 bg-[#F2EEEC] p-5 shadow-md h-[500px] flex flex-col">
            <h2 className="mb-3 flex items-center gap-2 text-xl font-semibold text-gray-900">
              <span className="h-2 w-2 rounded-full bg-rose-500"></span>
              Compilador
            </h2>
            <textarea
              value={input}
              onChange={(event) => setInput(event.target.value)}
              placeholder="Escribe la definición de tu esquema aquí..."
              className="flex-1 w-full rounded-lg border border-rose-200 bg-white p-3 text-rose-700 placeholder-rose-300 outline-none transition focus:border-rose-400 focus:ring-1 focus:ring-rose-400/30 font-mono text-sm resize-none"
            />
          </div>

          {/* Columna derecha: Consola (altura fija 500px) + botón debajo */}
          <div>
            {/* Consola - ahora con h-[500px] fijo */}
            <div className="rounded-2xl border border-rose-200 bg-[#F2EEEC] p-5 shadow-md h-[500px] flex flex-col">
              <h2 className="mb-3 flex items-center gap-2 text-lg font-semibold text-gray-900">
                <span className="h-2 w-2 rounded-full bg-rose-500"></span>
                Consola
              </h2>
              <div className="flex-1 overflow-y-auto rounded-lg bg-white border border-rose-100 p-3">
                {/* Texto de consola ahora en rose-300 (igual que el placeholder) */}
                <p className="whitespace-pre-wrap text-rose-300 font-mono text-xs leading-relaxed">
                  {error || '> Compilador listo. Ingresa código para comenzar.'}
                </p>
              </div>
            </div>

            {/* Botón debajo de la consola */}
            <button
              onClick={compile}
              disabled={loading || !input.trim()}
              className="mt-4 mx-auto w-48 rounded-lg bg-gradient-to-r from-rose-500 to-rose-600 px-4 py-2 text-sm font-semibold text-white transition hover:from-rose-400 hover:to-rose-500 disabled:cursor-not-allowed disabled:opacity-50 disabled:from-neutral-400 disabled:to-neutral-400 shadow-md block"
            >
              {loading ? '⏳ Compilando...' : '▶ Compilar'}
            </button>
          </div>
        </div>

        {/* Segunda fila: SQL y Estructura - misma altura 500px */}
        <div className="mt-6 grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* SQL Generado */}
          <div className="rounded-2xl border border-rose-200 bg-[#F2EEEC] p-5 shadow-md h-[500px] flex flex-col">
            <h2 className="mb-3 flex items-center gap-2 text-lg font-semibold text-gray-900">
              <span className="h-2 w-2 rounded-full bg-rose-500"></span>
              SQL Generado
            </h2>
            <div className="flex-1 overflow-y-auto rounded-lg bg-white border border-rose-100 p-4">
              {/* Texto SQL ahora en rose-300 */}
              <pre className="text-rose-300 font-mono text-xs whitespace-pre-wrap leading-relaxed">
                {sql || '-- El SQL aparecerá aquí después de compilar'}
              </pre>
            </div>
          </div>

          {/* Estructura BD */}
          <div className="rounded-2xl border border-rose-200 bg-[#F2EEEC] p-5 shadow-md h-[500px] flex flex-col">
            <h2 className="mb-3 flex items-center gap-2 text-lg font-semibold text-gray-900">
              <span className="h-2 w-2 rounded-full bg-rose-500"></span>
              Estructura de BD
            </h2>
            <div className="flex-1 overflow-y-auto rounded-lg bg-white border border-rose-100 p-4">
              {/* Texto estructura ahora en rose-300 */}
              <pre className="text-rose-300 font-mono text-xs whitespace-pre-wrap leading-relaxed">
                {dbStructure || '// La estructura se mostrará aquí después de compilar'}
              </pre>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;