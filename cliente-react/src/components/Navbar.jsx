/* eslint-disable */
import React from "react";
import SideNav, { NavItem, NavIcon, NavText } from "@trendmicro/react-sidenav";
import "@trendmicro/react-sidenav/dist/react-sidenav.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faClock,
  faCalculator,
  faCog,
  faMapMarkerAlt,
  faBan,
  faTruckMoving,
} from "@fortawesome/free-solid-svg-icons";
import { useHistory } from "react-router-dom";
import logo from "../images/logo.png";

function Navbar2(props) {
  const history = useHistory();

  const routeMonitoreo = () => {
    let path = `/`;
    history.push(path);
  };

  const routePedidos = () => {
    let path = `/pedidos`;
    history.push(path);
  };

  const routeSimulacion = () => {
    let path = `/simulacion`;
    history.push(path);
  };

  const routeConfiguracion = () => {
    let path = `/configuracion`;
    history.push(path);
  };

  const routeBloqueos = () => {
    let path = `/bloqueos`;
    history.push(path);
  };

  const routeFlota = () => {
    let path = `/flota`;
    history.push(path);
  };

  return (
    <>
      <SideNav className="sidebar">
        <SideNav.Toggle />
        <SideNav.Nav>
          <hr />
          <NavItem eventKey="monitoreo" onClick={routeMonitoreo}>
            <NavIcon>
              <FontAwesomeIcon icon={faClock} />
            </NavIcon>
            <NavText>Monitoreo</NavText>
          </NavItem>

          <NavItem eventKey="pedidos" onClick={routePedidos}>
            <NavIcon>
              <FontAwesomeIcon icon={faMapMarkerAlt} />
            </NavIcon>
            <NavText>Pedidos</NavText>
          </NavItem>

          <NavItem eventKey="bloqueos" onClick={routeBloqueos}>
            <NavIcon>
              <FontAwesomeIcon icon={faBan} />
            </NavIcon>
            <NavText>Bloqueos</NavText>
          </NavItem>

          <hr />

          <NavItem eventKey="simulacion" onClick={routeSimulacion}>
            <NavIcon>
              <FontAwesomeIcon icon={faCalculator} />
            </NavIcon>
            <NavText>Simulación</NavText>
          </NavItem>

          <hr />

          <NavItem eventKey="flota" onClick={routeFlota}>
            <NavIcon>
              <FontAwesomeIcon icon={faTruckMoving} />
            </NavIcon>
            <NavText>Flota</NavText>
          </NavItem>

          <NavItem eventKey="configuracion" onClick={routeConfiguracion}>
            <NavIcon>
              <FontAwesomeIcon icon={faCog} />
            </NavIcon>
            <NavText>Configuración</NavText>
          </NavItem>
        </SideNav.Nav>
      </SideNav>

      <img className="logoImagen" alt="img" src={logo} />
    </>
  );
}

export default Navbar2;
