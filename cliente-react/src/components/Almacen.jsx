/* eslint-disable */
import React, { useState, useContext } from "react";
import { Card } from "react-bootstrap";
import almacen1 from "../images/planta.png";
import almacen2 from "../images/tanques.png";
import { UserContext } from "../modules/UserContext";
import { StorageContext } from "../modules/StorageContext";

export default function Almacen(props) {
  const { user, changeUser } = useContext(UserContext);
  const { storage, changeStorage } = useContext(StorageContext);
  const [open, setOpen] = useState(false);
  const [principal, setPrincipal] = useState(props.principal);

  return (
    <div
      className="map__element map__almacen"
      style={{
        left: (user.gridsize * props.posx).toString() + "px",
        bottom: (user.gridsize * props.posy).toString() + "px",
      }}
      id={"alm " + props.idAlmacen.toString()}
    >
      <img
        className={`${principal ? "map__almacen__img" : "map__precarga__img"}`}
        alt="img"
        src={principal ? almacen1 : almacen2}
        type="button"
        id={"almacenButton " + props.idAlmacen.toString()}
        onClick={() => setOpen(!open)}
      />
      <div
        className={`map__almacen__tablero ${open ? "open" : "close"}`}
        id={"almacen " + props.idAlmacen.toString()}
      >
        <Card
          className="card card-tablero"
          border="light"
          id={"card-tablero " + props.idAlmacen.toString()}
        >
          <Card.Header>
            <Card.Title className="map__truck__tablero__text map__almacen__tablero__tittle">
              {props.descripcion}
            </Card.Title>
          </Card.Header>
          <Card.Body className="pt-2">
            <Card.Text className="map__truck__tablero__text">
              GLP :{" "}
              {principal ? (
                <>
                  <b>&infin;</b> / <b>&infin;</b>
                </>
              ) : (
                props.capacidad + " / " + props.capacidadActual
              )}
              m<sup>3</sup>
            </Card.Text>
          </Card.Body>
        </Card>
      </div>
    </div>
  );
}
