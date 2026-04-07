# generadorBD

Proyecto de ejemplo con frontend en React + Tailwind CSS y backend en Express.

## Qué incluye

- `frontend/`
  - React + Vite + Tailwind CSS
  - Interfaz con input, botón "Compilar", caja de errores y SQL generado
- `backend/`
  - Express server
  - Endpoint `POST /compilar`
  - Ejecuta un programa Java y devuelve el resultado

## Cómo usar

1. Instalar dependencias:
   - `cd frontend && npm install`
   - `cd backend && npm install`
2. Iniciar backend:
   - `cd backend && npm run dev`
3. Iniciar frontend:
   - `cd frontend && npm run dev`

## Estructura importante

- `frontend/src/App.jsx` -> UI principal
- `backend/server.js` -> endpoint Express
- `backend/java/Compilador.java` -> programa Java de ejemplo
