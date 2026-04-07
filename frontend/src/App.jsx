import { useState } from 'react';

function App() {
  const [input, setInput] = useState('');
  const [sql, setSql] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const compile = async () => {
    setError('');
    setSql('');
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
      }
    } catch (err) {
      setError('No se pudo conectar con el backend');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 px-4 py-8 text-slate-100">
      <div className="mx-auto w-full max-w-3xl rounded-3xl border border-slate-700 bg-slate-900/90 p-8 shadow-xl shadow-slate-900/40">
        <h1 className="mb-6 text-4xl font-semibold text-white">GeneradorBD</h1>
        <p className="mb-8 text-slate-400">Ingresa texto, presiona Compilar y obtén SQL generado desde el backend.</p>

        <textarea
          value={input}
          onChange={(event) => setInput(event.target.value)}
          rows={8}
          placeholder="Escribe el texto aquí..."
          className="w-full rounded-2xl border border-slate-700 bg-slate-950 p-4 text-slate-100 outline-none transition focus:border-cyan-500"
        />

        <div className="mt-4 flex flex-col gap-3 sm:flex-row sm:items-center">
          <button
            onClick={compile}
            disabled={loading}
            className="inline-flex items-center justify-center rounded-2xl bg-cyan-500 px-6 py-3 font-semibold text-slate-950 transition hover:bg-cyan-400 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {loading ? 'Compilando...' : 'Compilar'}
          </button>
          <span className="text-sm text-slate-400">Endpoint: POST /compilar</span>
        </div>

        <div className="mt-8 grid gap-6 md:grid-cols-2">
          <div className="rounded-2xl border border-red-500/30 bg-red-500/5 p-5">
            <h2 className="mb-3 text-lg font-semibold text-red-300">Caja de errores</h2>
            <p className="min-h-[4rem] whitespace-pre-wrap text-slate-200">{error || 'Sin errores'}</p>
          </div>

          <div className="rounded-2xl border border-cyan-500/30 bg-cyan-500/5 p-5">
            <h2 className="mb-3 text-lg font-semibold text-cyan-300">SQL generado</h2>
            <pre className="min-h-[4rem] whitespace-pre-wrap text-slate-200">{sql || 'Aquí aparecerá el SQL generado'}</pre>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
