import React, { useEffect, useState } from 'react';
import './HomePacientes.style.css';
import useApi from '../../customHooks/useApi';
import { useLocation, useParams } from 'react-router-dom';

const HomePacientes = () => {
    const { param } = useParams();
    const location = useLocation();
    const { customParam } = location.state || null;
    const {error,loading,fetchData} = useApi("http://localhost:8080/HolaMundo/")
    const [perfilPaciente, setPerfilPaciente] = 
    useState({
        Direccion: "",
        Email: "",
        FechaNacimiento: "1985-07-22",
        Genero: "",
        NombreCompleto: "",
        Telefono:"5551001",
        TotalCitas: 0,
        TotalDiagnosticos: 0
    })
    const [especialidades, setEspecialidades] = useState([])
    const [citaNueva, setCitaNueva] = useState({paciente_id:"",medico_id:"",fecha_hora:"",motivo:"",estado:"pendiente"})
    const [medicos, setMedicos] = useState([])
    
    useEffect(() => {
        console.log(param);
        
      }, [param])

    useEffect(() => {
        const fetchMultipleData = async () => {
          try {
            const [response1,response2] = await Promise.all([
              fetchData('GET', 'get_perfil_paciente?id=1'),
              fetchData('GET', 'get_especialidades'),
              
            ]);
            setPerfilPaciente(response1.data);
            setEspecialidades(response2.data); 
            // console.log(response2.data);
            
          } catch (err) {
            console.error("Error fetching data", err);
          }
        };
        fetchMultipleData();
    }, []);

    useEffect(() => {
        if (citaNueva.motivo !== "") {
            const fetch = async () => {
                try {
                    const response = await fetchData('GET', `medicos?especialidad_id=${citaNueva.motivo}`);
                    setMedicos(response.data);
                    console.log(response.data);
                    
                } catch (error) {
                    console.error("Error fetching data:", error);
                }
            }; 
            fetch();    
        }
    }, [citaNueva.motivo]);



    const formHandler = (e) => {
        setCitaNueva({...citaNueva,[e.target.name]:e.target.value})
    }
    const sendForm = (e) =>{
        e.preventDefault();
        // console.log(citaNueva);
        const send = async () => {
            try {
                const response = await fetchData('POST', 'citas', {...citaNueva, paciente_id:1});
                console.log(response);
            } catch (error) {
                console.error("Error sending data:", error);
            }
        }
        send();
    }

    return (
        <div className="home-content">
            {/* Información del Perfil */}
            <div className="profile-info">
                <h2>Perfil del Usuario</h2>
                <div className='profile-info-column'>
                    <div>
                        <p><strong>Nombre Completo:</strong> {perfilPaciente.NombreCompleto}</p>
                        <p><strong>Email:</strong> {perfilPaciente.Email}</p>
                        <p><strong>Fecha de Nacimiento:</strong> {perfilPaciente.FechaNacimiento}</p>
                        <p><strong>Dirección:</strong> {perfilPaciente.Direccion}</p>
                    </div>
                    <div>
                        <p><strong>Teléfono:</strong> {perfilPaciente.Telefono}</p>
                        <p><strong>Género:</strong> {perfilPaciente.Genero}</p>
                        <p><strong>Total de Citas:</strong> {perfilPaciente.TotalCitas}</p>
                        <p><strong>Total de Diagnósticos:</strong> {perfilPaciente.TotalDiagnosticos}</p>
                    </div>
                </div>
            </div>

            {/* Sección para Crear Cita */}
            <div className="appointment-section">
                <h2>Crear Cita</h2>
                <form className="appointment-form" onSubmit={sendForm}>
                    <label htmlFor="reason">Motivo de la cita:</label>
                    <select id="reason" name="motivo" rows="3" value={citaNueva.motivo} onChange={formHandler} required>
                        <option value="">Seleccione un motivo</option>
                        {especialidades.map((especialidad,index) => <option key={index} value={especialidad.id}>{especialidad.nombre}</option>)}
                    </select>

                    <label htmlFor="datetime">Fecha y Hora:</label>
                    <input type="datetime-local" onChange={formHandler} value={citaNueva.fecha_hora} id="datetime" name="fecha_hora" required />


                    <label htmlFor="reason">Medico:</label>
                    <select id="doctor" onChange={formHandler} name="medico_id" rows="3" required>
                        <option value="">Seleccione un medico</option>
                        {medicos.map((medico,index) => <option key={index} value={medico.id}>{medico.nombre_usuario}</option>)}
                    </select>

                    <button type="submit" className="submit-button">Crear Cita</button>
                </form>
            </div>
        </div>
    );
};

export default HomePacientes;
