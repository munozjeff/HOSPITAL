import React from 'react'
import './Forms.style.css'

export const RegisterForm = () => {
  return (
    <>
        <div className='sesion-form register-form'>
            <h2>Registro de Usuario</h2>
            <form action="/register" method="POST">
                <label htmlFor="nombre">Nombre</label>
                <input type="text" id="nombre" name="nombre" placeholder="Ingresa tu nombre" required/>
                
                <label htmlFor="apellido">Apellido</label>
                <input type="text" id="apellido" name="apellido" placeholder="Ingresa tu apellido" required/>
                
                <label htmlFor="email">Correo Electrónico</label>
                <input type="email" id="email" name="email" placeholder="Ingresa tu correo" required/>
                
                <label htmlFor="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Crea una contraseña" required/>
                
                <button type="submit" className="register-btn">Registrarse</button>
                
                <p className="login-link">¿Ya tienes una cuenta? <a href="/login">Inicia Sesión</a></p>
            </form>
        </div>

    </>
  )
}
