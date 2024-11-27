import React, { useEffect } from 'react'
import './Forms.style.css'
import { Link, useNavigate } from 'react-router-dom'
import useApi from '../../customHooks/useApi'
import { Alert } from 'react-bootstrap'

export const LoginForm = () => {
  const navigate = useNavigate()
  const {error,loading,fetchData} = useApi("http://localhost:8080/HolaMundo/")

  useEffect(()=>{
    const fetch = async () => {
      try {
        const response = await fetchData('GET', 'verificarCookies', {credentials: "include"});
        console.log(response);
        if (response.success) {
            navigate('/paciente', { state: { data: response.data } });
        }
        
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
    fetch();
  },[])

  const senData = async (e) => {
    e.preventDefault()
    const formData = new FormData(e.target)
    const data = {
      email: formData.get('email'),
      password: formData.get('password')
    }
    const fetch = async () => {
      try {
        const response = await fetchData('POST', 'login', data , {credentials: "include"}); // Esto asegura que las cookies se envíen/reciban;
        console.log(response)
        if (response.success) {
          if (response.data.rol === 'paciente') {
            navigate('/paciente', { state: { data: response.data } });
          }
        }
        alert(response.message)
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
    fetch();
  }

  return (
    <>
        <div className='sesion-form login-form'>
            <h2>Inicio de Sesión</h2>
            <form action="/login" onSubmit={senData}>
                <label htmlFor="email">Correo Electrónico</label>
                <input type="email" id="email" name="email" placeholder="Ingresa tu correo" required/>
                
                <label htmlFor="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required/>
                
                <button type="submit" className="login-btn">Iniciar Sesión</button>
                
                <p className="register-link">¿No tienes una cuenta? <Link to="/register">Registrar</Link></p>
            </form>
        </div>

    </>
  )
}
