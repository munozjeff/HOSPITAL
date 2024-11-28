import { useState } from 'react';
import axios from 'axios';

const useApi = (baseUrl) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchData = async (method, endpoint, body = null) => {
    setLoading(true);
    setError(null);

    try {
      const fullUrl = `${baseUrl}${endpoint}`;
      let response;

      if (method === 'POST') {
        response = await axios.post(fullUrl, body, {
          headers: { 'Content-Type': 'application/json' },credentials: true,
        });
      } else if (method === 'GET') {
        response = await axios.get(fullUrl, {
          headers: { 'Content-Type': 'application/json' },withCredentials: true,
        });
      } else if (method === 'PUT') {
        response = await axios.put(fullUrl, body, {
          headers: { 'Content-Type': 'application/json' }
        });
      } else if (method === 'DELETE') {
        response = await axios.delete(fullUrl, {
          headers: { 'Content-Type': 'application/json' }
        });
      } else {
        throw new Error('Método HTTP no válido');
      }

      return response.data; // Devuelve la respuesta de la API directamente
    } catch (err) {
      setError(err.response ? err.response.data : err.message || 'Error al realizar la solicitud');
      return null; // Devuelve null en caso de error
    } finally {
      setLoading(false);
    }
  };

  return { loading, error, fetchData };
};

export default useApi;
