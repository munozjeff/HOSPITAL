import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { LoginForm } from './components/Forms/LoginForm'
import { Routes, Route, Outlet, Link } from "react-router-dom";
import { RegisterForm } from './components/Forms/RegisterForm'
import Main from './layouts/Home/Home'
import { Paciente } from './Pages/Paciente/Paciente'
import { Medico } from './Pages/Medico/Medico'
import Citas from './components/Citas/Citas'
import HomePacientes from './Pages/HomePacientes/HomePacientes'
import HistorialCitas from './Pages/HistorialCitas/HistorialCitas'


function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      {/* <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="about" element={<About />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="*" element={<NoMatch />} />
        </Route>
      </Routes> */}
      <Routes>
        <Route index element={<LoginForm />} />
        <Route path="/paciente" element={<Paciente/>}>
          <Route index element={<HomePacientes/>} />
          <Route path="historial" element={<HistorialCitas/>} />
          <Route path="Diagnosticos" element={<h1>Diagnosticos</h1>} />
        </Route>
        <Route path='/medico' element={<Medico/>} />
        <Route path="/register" element={<RegisterForm/>} />
      </Routes>
    </>
  )
}

export default App
