/* eslint-disable */
import React, { useState, useContext } from "react";
import { Card } from "react-bootstrap";
import pedido from "../images/pedido.png";
import { UserContext } from "../modules/UserContext";
import { StorageContext } from "../modules/StorageContext";

export default function Pedido(props) {
  const { user, changeUser } = useContext(UserContext);
  const { storage, changeStorage } = useContext(StorageContext);
  const [open, setOpen] = useState(false);

  return (
    <div
      className={`map__element map__pedido ${props.simulacion ? "" : "open"}`}
      id={"Pedido " + props.idPedido}
      style={{
        left: (user.gridsize * props.posx).toString() + "px",
        bottom: (user.gridsize * props.posy).toString() + "px",
      }}
    >
      <img
        className={`map__pedido__img ${
          props.colapso
            ? "red"
            : props.camion === "Ninguno"
            ? "pedido-black"
            : ""
        }`}
        alt="img"
        src={pedido}
        type="button"
        id={"pedidoButtons" + props.idPedido}
        onClick={() => setOpen(!open)}
      />

      <div
        className={`map__pedido__tablero ${
          storage.tableros ? "open" : open ? "open" : "close"
        }`}
        id={"pedido" + props.idPedido}
      >
        <Card className="card card-tablero" border="light">
          <Card.Header>
            <Card.Title
              className={`map__truck__tablero__text map__pedido__tablero__tittle ${
                props.colapso ? "map__pedido__tablero__collapse" : ""
              }`}
            >
              {props.descripcion}
            </Card.Title>
          </Card.Header>
          <Card.Body className="pt-2">
            <Card.Text className="map__truck__tablero__text ">
              GLP:{" "}
              {!props.simulacion
                ? props.glpStatus + " / " + props.glp + "m"
                : "" + props.glp + "m"}
              <sup>3</sup>
              <br />
              {/* Camion: {props.camion} */}
            </Card.Text>
            <Card.Text className="map__truck__tablero__text ">
              Fecha Registro:
              <br />
              {props.fechaRegistro}
            </Card.Text>
          </Card.Body>
        </Card>
      </div>
    </div>
  );
}
