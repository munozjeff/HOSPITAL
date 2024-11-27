import { NavLink } from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import './NavBar.style.css';
import user from '../../assets/user.png';

function NavBarComponent({navLinks=[],user="",id=""}) {
  return (
    <>
      <Navbar bg="transparent" data-bs-theme="dark">
        <Container>
          <Navbar.Brand href={navLinks[0].to}>Navbar</Navbar.Brand>
          <Nav className="me-auto">
            {navLinks.map((link, index) => (
              <Nav.Link  
                key={index} 
                as={NavLink} 
                // to={`${link.to}/${param}`}
                to={link.to}
                // to={`${link.to}/${id}`}
                state={{ customParam: 'valor' }}
                className={({ isActive }) => isActive ? 'active-link' : ''}
              >
                {link.title}
              </Nav.Link>
            ))}
          </Nav>
        </Container>
      </Navbar>
      <div className='logout-container'>
        <span>{user}</span>
        <span>
          <img src={user} alt="" />
          <Nav.Link href="#logout">LogOut</Nav.Link>
        </span>
      </div>
    </>
  );
}

export default NavBarComponent;
