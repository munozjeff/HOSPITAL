import React, { useEffect, useState } from 'react';
import './HistorialCitas.style.css';
import useApi from '../../customHooks/useApi';
import Modal from '../../components/Modal/Modal';

const HistorialCitas = () => {
    const { loading, error, fetchData } = useApi("http://localhost:8080/HolaMundo/");
    const [historial, setHistorial] = useState([]);
    const [citasFuturas, setCitasFuturas] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [citaNueva, setCitaNueva] =  useState({paciente_id:"",medico_id:"",fecha_hora:"",motivo:"",estado:"pendiente"})
    const [especialidades, setEspecialidades] = useState([])
    const [medicos, setMedicos] = useState([])
    const [editID, setEditID] = useState(null)

    const showModal = () => setIsModalOpen(!isModalOpen);

    useEffect(() => {
        if (isModalOpen){
            const fetch = async () => {
                try {
                    const response = await fetchData('GET', 'get_especialidades');
                    setEspecialidades(response.data);
                } catch (error) {
                    console.error("Error fetching data:", error);
                }
            }; 
            fetch();
        }
    }, [isModalOpen]);

    const formHandler = (e) => {
        const { name, value } = e.target;
        setCitaNueva((prevState) => ({ ...prevState, [name]: value}));
    };

    const sendForm = (e) => {
        e.preventDefault();
        // Lógica para enviar el formulario
        // console.log("Cita creada:", citaNueva);
        const citaNuevaMod = {...citaNueva, paciente_id: 1,medico_id:parseInt(citaNueva.medico_id),motivo:parseInt(citaNueva.motivo)} 
        console.log(citaNuevaMod);
        
        const send = async () => {
            try {
                console.log(editID);
                
                const response = await fetchData('PUT',`citas?id=${editID}`, citaNuevaMod);
                console.log(response);
            } catch (error) {
                console.error("Error sending data:", error);
            }
        }
        send();
        showModal(); // Cierra el modal después de enviar
    };

    useEffect(() => {
        const fetchCitas = async () => {
            try {
                const [response1, response2] = await Promise.all([
                    fetchData('GET', 'get_historial_citas?id=1'),
                    fetchData('GET', 'get_citas_futuras?id=1'),
                ])
                setHistorial(response1.data);
                setCitasFuturas(response2.data);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };
        fetchCitas();
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

    const editarCitaHandler = (e) => {
        const citaId = e.target.closest('li').dataset.id; // Obtiene el data-id del <li>
        setEditID(citaId);
        showModal(citaId)
    }
    const eliminarCitaHandler = (e) => {
        const citaId = e.target.closest('li').dataset.id
        const fetch = async () => {
            const [response1,response2] = await Promise.all([
                fetchData('DELETE', `citas?id=${citaId}`),
                fetchData('GET', 'get_citas_futuras?id=1'),
            ])
            setCitasFuturas(response2.data);
            console.log(response1);
            
        }
        fetch();
    }

    // useEffect(() => {
    //     if (citas) {
    //         const now = new Date();
    //         const pasadas = citas.filter(cita => new Date(cita.fecha) < now);
    //         const futuras = citas.filter(cita => new Date(cita.fecha) >= now);

    //         setCitasPasadas(pasadas);
    //         setCitasFuturas(futuras);
    //     }
    // }, [citas]);

    return (
        <div className="historial-citas-container">
            {/* Sidebar para Citas Futuras */}
            <aside className="sidebar-futuras">
                <h2>Citas Futuras</h2>
                {citasFuturas.length === 0 ? (
                    <p>No hay citas futuras.</p>
                ) : (
                    <ul>
                        {citasFuturas.map((cita) => (
                            <li key={cita.id} data-id={cita.id}>
                                <p><strong>Fecha y Hora:</strong> {cita.FechaHoraCita}</p>
                                <p><strong>Motivo:</strong> {cita.Motivo}</p>
                                <p><strong>Médico:</strong> {cita.NombreMedico}</p>
                                <p><strong>Especialidad:</strong> {cita.Especialidad}</p>
                                <div className='sidebar-futuras-controles'>
                                    <input className='editar' type="button" onClick={editarCitaHandler}/>
                                    <input className='eliminar' type="button" onClick={eliminarCitaHandler}/>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </aside>

            {/* Sección principal para el Historial de Citas */}
            <section className="historial-citas">
                <h2>Historial de Citas</h2>
                {loading ? (
                    <p>Cargando...</p>
                ) : error ? (
                    <p>Error al cargar las citas: {error}</p>
                ) : historial.length === 0 ? (
                    <p>No hay historial de citas.</p>
                ) : (
                    <table>
                        <thead>
                            <tr>
                                <th>Fecha y Hora</th>
                                <th>Motivo</th>
                                <th>Diagnóstico</th>
                                <th>Nombre del Médico</th>
                                <th>Especialidad</th>
                                <th>Días hasta Diagnóstico</th>
                                {/* <th>Accion</th> */}
                            </tr>
                        </thead>
                        <tbody>
                            {historial.map((cita) => (
                                <tr key={cita.id}>
                                    <td>{cita.FechaHoraCita}</td>
                                    <td>{cita.Motivo}</td>
                                    <td>{cita.Diagnostico || "Pendiente"}</td>
                                    <td>{cita.NombreMedico}</td>
                                    <td>{cita.Especialidad}</td>
                                    <td>{cita.DiasHastaDiagnostico}</td>
                                    {/* <td><button>eliminar</button></td> */}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </section>
            <Modal isOpen={isModalOpen} onClose={showModal}>
                <div className="appointment-section">
                    <h2>Crear Cita</h2>
                    <form className="appointment-form" onSubmit={sendForm}>
                        <label htmlFor="reason">Motivo de la cita:</label>
                        <select id="reason" name="motivo" rows="3" value={citaNueva.motivo} onChange={formHandler} required>
                            <option value="">Seleccione un motivo</option>
                            {especialidades.map((especialidad, index) => (
                                <option key={index} value={especialidad.id}>{especialidad.nombre}</option>
                            ))}
                        </select>

                        <label htmlFor="datetime">Fecha y Hora:</label>
                        <input type="datetime-local" onChange={formHandler} value={citaNueva.fecha_hora} id="datetime" name="fecha_hora" required />

                        <label htmlFor="doctor">Medico:</label>
                        <select id="doctor" onChange={formHandler} name="medico_id" rows="3" required>
                            <option value="">Seleccione un medico</option>
                            {medicos.map((medico, index) => (
                                <option key={index} value={medico.id}>{medico.nombre_usuario}</option>
                            ))}
                        </select>

                        <button type="submit" className="submit-button">Crear Cita</button>
                    </form>
                </div>
            </Modal>
        </div>
    );
};

export default HistorialCitas;
