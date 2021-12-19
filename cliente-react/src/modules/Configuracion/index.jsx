/* eslint-disable */
import React, { useState, useContext, useEffect } from "react";
import { Navbar, Modal, Button } from "react-bootstrap";
import { useFetch } from "../../hooks/useFetch.jsx";
import { UserContext } from "../UserContext";

function Configuracion(props) {
  const { user, changeUser } = useContext(UserContext);

  const { fetch, isLoading } = useFetch({
    initialUri: "http://localhost:8080/pedido/create",
    initialMethod: "POST",
  });

  const [configs, setConfigs] = useState({
    mapaX: user.mapaX,
    mapaY: user.mapaY,
    mapaOn: user.mapaOn,
    centralX: user.centralX,
    centralY: user.centralY,
    pR1X: user.pR1X,
    pR1Y: user.pR1Y,
    pR2X: user.pR2X,
    pR2Y: user.pR2Y,
    batchTime: user.batchTime,
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    /* 
    const resp = await fetch(configs);

    console.log(resp);

    if (resp.status === 200) {
      console.log("Se llamó al batch time con éxito");
 */
    changeUser({
      mapaX: parseInt(configs.mapaX),
      mapaY: parseInt(configs.mapaY),
      mapaOn: parseInt(configs.mapaOn),
      centralX: parseInt(configs.centralX),
      centralY: parseInt(configs.centralY),
      pR1X: parseInt(configs.pR1X),
      pR1Y: parseInt(configs.pR1Y),
      pR2X: parseInt(configs.pR2X),
      pR2Y: parseInt(configs.pR2Y),
      batchTime: parseInt(configs.batchTime),
    });

    alert("Se configuró con éxito");
    /*} else {
      alert("No se pudo llamar al batch time");
    } */
  };

  return (
    <>
      <div className="config-container">
        <h2>Configuración</h2>
        <hr />
        <form className="row" onSubmit={handleSubmit}>
          <div className="col-2 mt-4">
            <h5>MAPA:</h5>
          </div>
          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">
              Distancia Horizontal (Km){" "}
            </label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la distancia horizontal del mapa"
              value={configs.mapaX}
              onChange={(e) =>
                setConfigs({ ...configs, mapaX: e.target.value })
              }
            />
          </div>

          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Distancia Vertical (Km) </label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la distancia vertical del mapa"
              value={configs.mapaY}
              onChange={(e) =>
                setConfigs({ ...configs, mapaY: e.target.value })
              }
            />
          </div>

          <div className="col-2" />

          <hr />

          <div className="col-2 mt-4">
            <h5>ALMACÉN:</h5>
          </div>
          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Posición X</label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la posición X del almacén"
              value={configs.centralX}
              onChange={(e) =>
                setConfigs({ ...configs, centralX: e.target.value })
              }
            />
          </div>

          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Posición Y</label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la posición Y del almacén"
              value={configs.centralY}
              onChange={(e) =>
                setConfigs({ ...configs, centralY: e.target.value })
              }
            />
          </div>

          <div className="col-2" />

          <hr />

          <div className="col-2 mt-4">
            <h5>P.RECARGA 1:</h5>
          </div>
          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Posición X</label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la posición X del p.recarga 1"
              value={configs.pR1X}
              onChange={(e) => setConfigs({ ...configs, pR1X: e.target.value })}
            />
          </div>

          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Posición Y</label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la posición Y del p.recarga 1"
              value={configs.pR1Y}
              onChange={(e) => setConfigs({ ...configs, pR1Y: e.target.value })}
            />
          </div>

          <div className="col-2" />

          <hr />

          <div className="col-2 mt-4">
            <h5>P.RECARGA 2:</h5>
          </div>
          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Posición X</label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la posición X del p.recarga 2"
              value={configs.pR2X}
              onChange={(e) => setConfigs({ ...configs, pR2X: e.target.value })}
            />
          </div>

          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">Posición Y</label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese la posición Y del p.recarga 2  "
              value={configs.pR2Y}
              onChange={(e) => setConfigs({ ...configs, pR2Y: e.target.value })}
            />
          </div>

          <div className="col-2" />

          <hr />

          {/* <div className="col-2 mt-4">
            <h5>BATCH TIME</h5>
          </div>
          <div className="form-group col-4 pedidos-container__form">
            <label htmlFor="exampleInputEmail1">
              Lanzamiento del Algoritmo (horas)
            </label>
            <input
              className="form-control"
              id="exampleInputEmail1"
              aria-describedby="emailHelp"
              placeholder="Ingrese el batch para lanzar el algoritmo"
              value={configs.batchTime}
              onChange={(e) =>
                setConfigs({ ...configs, batchTime: e.target.value })
              }
            />
          </div> 

          <div className="col-6" />

          <hr />*/}

          <button
            type="button"
            onClick={(e) => {
              e.preventDefault();
              window.location.href = "/";
            }}
            className="btn btn-primary col  pedidos-container__button"
          >
            Cancelar
          </button>

          <button
            type="submit"
            className="btn btn-primary col  pedidos-container__button"
          >
            Guardar
          </button>
        </form>
      </div>
    </>
  );
}

export default Configuracion;
