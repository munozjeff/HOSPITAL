import React, { useEffect, useState } from 'react'
import NavBarComponent from '../../components/NavBar/NavBar';
import { Outlet, useLocation } from 'react-router-dom';

const navLinks = [
    {to: '/paciente', title: 'citas'},
    {to: 'historial', title: 'Historial'},
    {to: 'diagnosticos', title: 'Diagnosticos'},
]

export const Paciente = () => {
    const [userEmail, setUserEmail] = useState('user')

    const location = useLocation();
    const data = location.state;

    useEffect(()=>{
        console.log(data.data);
        
    },[data])
    return (
        <div className="layout-home">
            {/* Header */}
            <header className="layout-home-header">
                {/* <div className="clinic-name">Clínica Salud</div>
                <div className="user-info">
                    <span>{userEmail}</span>
                    <button className="logout-button">Cerrar Sesión</button>
                </div> */}
                <NavBarComponent navLinks={navLinks} user={"user"} id={"1"}/>
            </header>

            {/* Body */}
            <main className="layout-home-body">
                <Outlet/>
            </main>

            {/* Footer */}
            <footer className="layout-home-footer">
                <p>&copy; {new Date().getFullYear()} Clínica Salud. Todos los derechos reservados.</p>
            </footer>
        </div>
    );
}
