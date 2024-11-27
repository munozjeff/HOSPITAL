import React, { useEffect, useState } from 'react';
import { Card, Container, Row, Col, Button, Form, Modal, Alert } from 'react-bootstrap';
import useApi from '../../customHooks/useApi';

const citas = [
    { titulo: 'Consulta General', fecha: '2024-11-10', descripcion: 'Chequeo anual de salud' },
    { titulo: 'Revisión Dermatológica', fecha: '2024-10-15', descripcion: 'Revisión de manchas en la piel' },
    { titulo: 'Cita Dental', fecha: '2024-12-05', descripcion: 'Limpieza dental' },
  ];

  
  
  function Citas({ onAddCita=()=>{} }) {
    const now = new Date();
    const [showAlert, setShowAlert] = useState(false)
    const [especialidades, setEspecialidades] = useState([])
    const [userData, setUserData] = useState({id:1})
    const [citasFuturas, setCitasFuturas] = useState([])
    const [citasPasadas, setCitasPasadas] = useState([])
    const {data,error,fetchData,loading} = useApi("http://localhost/HOSPITAL/")
    const [medicos, setMedicos] = useState([])
    const [nuevaCita, setNuevaCita] = useState({ motivo: '', fecha_hora: '',paciente_id:0,medico_id:0, estado: 'pendiente' });
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        if (nuevaCita.motivo !== "") {
            fetchData('GET', `medicos.php?especialidad=${nuevaCita.motivo}`, "get_data_medicos")     
        }
    }, [nuevaCita.motivo])
    

    useEffect(() => {
        const fetchAllData = async () => {
          await Promise.all([
            fetchData('GET', "especialidades.php?accion=get_data", "get_data_especialidades"),
            fetchData('GET', `citas.php?paciente_id=${userData.id}&tipo_cita=futuras`, "get_citas_futuras"),
            fetchData('GET', `citas.php?paciente_id=${userData.id}&tipo_cita=pasadas`, "get_citas_pasadas"),
          ]);
        };
    
        fetchAllData();
      }, [userData.id]);
    
      // Actualizar el estado según el tipo de datos obtenidos
      useEffect(() => {
        if (data) {
          switch (data.accion) {
            case "get_data_especialidades":
              setEspecialidades(data.data);
              break;
            case "get_citas_futuras":
              setCitasFuturas(data.data);
              break;
            case "get_citas_pasadas":
              setCitasPasadas(data.data);
              break;
            case "get_data_medicos":
              setMedicos(data.data);
            case "add_cita":
                setShowAlert(true)
              break;
            default:
              break;
          }
        }
      }, [data]);
    
  
    // Filtra las citas en pasadas y futuras
    // const citasPasadas = citas.filter(cita => new Date(cita.fecha) < now);
    // const citasFuturas = citas.filter(cita => new Date(cita.fecha) >= now);
  
    // Estado para el modal de creación de citas
    
    // const [nuevaCita, setNuevaCita] = useState();
  
    const handleShow = () => setShowModal(true);
    const handleClose = () => {
      setShowModal(false);
      setNuevaCita({ motivo: '', fecha_hora: '',paciente_id:0,medico_id:0, estado: 'pendiente' }); // Reinicia el formulario
    };
  
    const handleChange = (e) => {
        
      setNuevaCita({ ...nuevaCita, [e.target.name]: e.target.value });
    };
    
  
    const handleSubmit = (e) => {
      e.preventDefault();
      //onst motivo = especialidades.filter((especialidad)=>especialidad.id == nuevaCita.motivo)[0]
      const dataSend = {...nuevaCita, paciente_id: userData.id,medico_id:parseInt(nuevaCita.medico_id)};
      
      fetchData('POST', 'citas.php', dataSend);
      handleClose();
    };
  
    return (
    <>
        <Container>
        
        <Row>
          <Col>
            <h2>Mis Citas</h2>
            <Button variant="primary" onClick={handleShow}>Crear Nueva Cita</Button>
          </Col>
        </Row>
  
        <Row className="mt-4">
          {/* Sección para citas futuras */}
          <Col md={6}>
            <h3>Citas Futuras</h3>
            {citasFuturas.length > 0 ? (
              citasFuturas.map((cita, index) => (
                <Card key={index} className="mb-3">
                  <Card.Body>
                    <Card.Title>{cita.motivo}</Card.Title>
                    <Card.Text>Fecha: {cita.fecha_hora}</Card.Text>
                  </Card.Body>
                </Card>
              ))
            ) : (
              <p>No hay citas futuras.</p>
            )}
          </Col>
  
          {/* Sección para citas pasadas */}
          <Col md={6}>
            <h3>Citas Pasadas</h3>
            {citasPasadas.length > 0 ? (
              citasPasadas.map((cita, index) => (
                <Card key={index} className="mb-3">
                  <Card.Body>
                    <Card.Title>{cita.motivo}</Card.Title>
                    <Card.Text>Fecha: {cita.fecha_hora}</Card.Text>
                  </Card.Body>
                </Card>
              ))
            ) : (
              <p>No hay citas pasadas.</p>
            )}
          </Col>
        </Row>
  
        {/* Modal para crear una nueva cita */}
        <Modal show={showModal} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Crear Nueva Cita</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3" controlId="formTitulo">
                <Form.Label>Especialista</Form.Label>
                <Form.Select
                    name="motivo"
                    value={nuevaCita.motivo}
                    onChange={handleChange}
                    required
                    >
                    <option value="">Seleccione un especialista</option>
                    {especialidades.map((especialidad, index) => <option key={index} value={especialidad.nombre}>{especialidad.nombre}</option>)}
                    </Form.Select>

              </Form.Group>
  
              <Form.Group className="mb-3" controlId="formFecha">
                <Form.Label>Fecha y hora</Form.Label>
                <Form.Control
                  type="datetime-local"
                  name="fecha_hora"
                  value={nuevaCita.fecha_hora}
                  onChange={handleChange}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="formTitulo">
                <Form.Label>Medico</Form.Label>
                <Form.Select
                    name="medico_id"
                    value={nuevaCita.medico_id}
                    onChange={handleChange}
                    required
                    >
                    <option value="">Seleccione un especialista</option>
                    {medicos.map((medico, index) => <option key={index} value={medico.medico_id}>{medico.nombre_medico}</option>)}
                    </Form.Select>

              </Form.Group>
  
              <Button variant="primary" type="submit">Guardar Cita</Button>
            </Form>
          </Modal.Body>
        </Modal>
      </Container>
    </>
      
    );
  }
  
  export default Citas;
  
