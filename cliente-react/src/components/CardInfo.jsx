/* eslint-disable */
import React, { useContext } from "react";
import { Card, Button } from "react-bootstrap";
import truck from "../images/truck.png";
import tanque from "../images/tanques.png";
import planta from "../images/planta.png";
import bloqueo from "../images/bloqueo.png";
import { UserContext } from "../modules/UserContext";
import { StorageContext } from "../modules/StorageContext";

export default function CardSartenesYOllas(props) {
  const { storage, changeStorage } = useContext(StorageContext);
  const { user, changeUser } = useContext(UserContext);

  const coordenadaOff = (e) => {
    e.preventDefault();
    changeStorage({
      cords: true,
    });
  };

  const coordenadaOn = (e) => {
    e.preventDefault();
    changeStorage({
      cords: false,
    });
  };

  const tablerosOff = (e) => {
    e.preventDefault();
    changeStorage({
      tableros: true,
    });
  };

  const tablerosOn = (e) => {
    e.preventDefault();
    changeStorage({
      tableros: false,
    });
  };

  return (
    <Card className="card card-info" border="secondary">
      <Card.Body className="">
        <div className="row">
          <Card.Title className="card-info__tittle">Leyenda</Card.Title>
          <hr />
        </div>

        <div className="row">
          <div className="col-6">
            <div className="row card-info__row">
              <div className="col-3">
                <img
                  className="card-info__img truck--blue"
                  alt="img"
                  src={truck}
                />
              </div>
              <div className="col-9">
                <Card.Text className="card-info__text">En camino</Card.Text>
              </div>
            </div>

            <div className="row card-info__row">
              <div className="col-3">
                <img
                  className="card-info__img truck--green"
                  alt="img"
                  src={truck}
                />
              </div>
              <div className="col-9">
                <Card.Text className="card-info__text">Disponible</Card.Text>
              </div>
            </div>

            <div className="row card-info__row">
              <div className="col-3">
                <img
                  className="card-info__img truck--red"
                  alt="img"
                  src={truck}
                />
              </div>
              <div className="col-9">
                <Card.Text className="card-info__text">Averiado</Card.Text>
              </div>
            </div>
          </div>

          <div className="col-6">
            <div className="row card-info__row">
              <div className="col-4 ">
                <img
                  className="card-info__img card-info__img--legend"
                  alt="img"
                  src={tanque}
                />
              </div>
              <div className="col-8">
                <Card.Text className="card-info__text">
                  Tanques de
                  <br />
                  recarga
                </Card.Text>
              </div>
            </div>

            <div className="row card-info__row">
              <div className="col-4">
                <img className="card-info__img" alt="img" src={planta} />
              </div>
              <div className="col-8">
                <Card.Text className="card-info__text">
                  Planta
                  <br />
                  principal
                </Card.Text>
              </div>
            </div>

            <div className="row card-info__row">
              <div className="col-4">
                <img
                  className="card-info__img card-info__img-bloqueo card-info__img-bloqueo--legend"
                  alt="img"
                  src={bloqueo}
                />
              </div>
              <div className="col-8">
                <Card.Text className="card-info__text">Bloqueo</Card.Text>
              </div>
            </div>
          </div>
        </div>

        <div className="row card-info__row pt-2">
          <Card.Title className="card-info__tittle">Visibilidad</Card.Title>
          <hr />
        </div>

        <div className="row card-info__row pt-3">
          <div className="col-2">
            {storage.tableros ? (
              <Button
                className="cordButton"
                variant="success"
                size="sm"
                onClick={tablerosOn}
              >
                ON
              </Button>
            ) : (
              <Button
                className="cordButton"
                variant="danger"
                size="sm"
                onClick={tablerosOff}
              >
                OFF
              </Button>
            )}
          </div>
          <div className="col-10">
            <Card.Text className="card-info__text mx-2">
              Tableros de Camiones
            </Card.Text>
          </div>
        </div>

        <div className="row card-info__row pt-3">
          <div className="col-2">
            {storage.cords ? (
              <Button
                className="cordButton"
                variant="success"
                size="sm"
                onClick={coordenadaOn}
              >
                ON
              </Button>
            ) : (
              <Button
                className="cordButton"
                variant="danger"
                size="sm"
                onClick={coordenadaOff}
              >
                OFF
              </Button>
            )}
          </div>
          <div className="col-10">
            <Card.Text className="card-info__text mx-2">Coordenadas</Card.Text>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}
