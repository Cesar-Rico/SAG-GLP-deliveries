/* eslint-disable */
import React, { useState, useContext } from "react";
import { Card, Button } from "react-bootstrap";
import truck from "../images/truckMapa.png";
import { UserContext } from "../modules/UserContext";
import { StorageContext } from "../modules/StorageContext";
import { useFetch } from "../hooks/useFetch.jsx";

export default function Truck(props) {
  const { user, changeUser } = useContext(UserContext);
  const { storage, changeStorage } = useContext(StorageContext);
  const [open, setOpen] = useState(false);
  const [on, setOn] = useState(true);
  const [retorno, setRetorno] = useState(props.retorno);

  return (
    <div
      className="map__element map__truck"
      id={"TA " + props.idTruck}
      style={{
        left: (user.gridsize * props.x).toString() + "px",
        bottom: (user.gridsize * props.y).toString() + "px",
      }}
    >
      <img
        className={`map__truck__img ${
          !on || props.averiado
            ? "truck--red"
            : !props.retorno
            ? "truck--blue"
            : "truck--green"
        }`}
        alt="img"
        src={truck}
        type="button"
        id={"truckButtons" + props.idTruck}
        onClick={() => setOpen(!open)}
      />

      <div
        className={`map__truck__tablero ${
          storage.tableros ? "open" : open ? "open" : "close"
        }`}
        id={"truck" + props.idTruck}
      >
        <Card className="card card-tablero" border="light">
          <Card.Header>
            <div className="row">
              <Card.Title
                className={`col-8 map__truck__tablero__text map__truck__tablero__tittle ${
                  !on || props.averiado
                    ? "map__truck__tablero__tittle--red"
                    : !props.retorno
                    ? "map__truck__tablero__tittle--blue"
                    : "map__truck__tablero__tittle--green"
                }`}
              >
                {props.descripcion}
              </Card.Title>
            </div>
          </Card.Header>
          {/*
          <Card.Body className="pt-2">
            <Card.Text className="map__truck__tablero__text">
              GLP: {props.glp + "m"}
              <sup>3</sup>
            </Card.Text>
             <div className="meter nostripes">
              <span
                style={{
                  width:
                    (100 * (props.glp / props.glpCapacity)).toString() +
                    "%!important",
                }}
              />
            </div> 
            <Card.Text className="map__truck__tablero__text">
              Destino: {props.destino}
            </Card.Text>
          </Card.Body>*/}
        </Card>
      </div>
    </div>
  );
}
