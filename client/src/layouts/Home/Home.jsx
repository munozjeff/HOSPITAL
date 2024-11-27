import React, { useEffect, useState } from 'react';
import './Home.style.css';
import Offcanvas from '../../components/OffCanvas/Offcanvas';
import NavBarComponent from '../../components/NavBar/NavBar';
import { Outlet } from 'react-router-dom';

const Home = () => {
    const [userEmail, setUserEmail] = useState('user')
    
    
    return (
        <div className="layout-home">
            {/* Header */}
            <header className="layout-home-header">
                {/* <div className="clinic-name">Clínica Salud</div>
                <div className="user-info">
                    <span>{userEmail}</span>
                    <button className="logout-button">Cerrar Sesión</button>
                </div> */}
                <NavBarComponent/>
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
};

export default Home;
