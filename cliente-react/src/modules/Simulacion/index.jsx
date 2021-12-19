/* eslint-disable */
import React, { useState, useContext, useEffect } from "react";
import { Navbar, Card } from "react-bootstrap";
import files from "../../images/files.png";
import descargable from "../Simulacion/Formato.zip"; //Solo tienen que cambiar esta ruta, por la ruta que se quiera descargar
import { useHistory } from "react-router-dom";
import { useFetch } from "../../hooks/useFetch.jsx";
import { UserContext } from "../UserContext";
import { StorageContext } from "../StorageContext";

function Simulacion(props) {
  const { storage, changeStorage } = useContext(StorageContext);
  const { user, changeUser } = useContext(UserContext);

  const divisores = [
    1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24, 30, 36, 40, 45, 48, 60,
    72, 80, 90, 120, 144, 180, 240, 360, 720,
  ];

  const [ndivisor, setNdivisor] = useState(0);
  const [divisorShowed, setDivisorShowed] = useState(1);
  const [rangeval, setRangeval] = useState(null);

  const changeDivisor = (e) => {
    setRangeval(e.target.value);
    changeUser({
      sim3Velocity: e.target.value,
    });
  };

  const [item, setItem] = useState({
    kindOfStand: "option1",
  });

  const { kindOfStand } = item;

  const handleChange = (e) => {
    e.persist();
    //console.log(e.target.value);

    setItem((prevState) => ({
      ...prevState,
      kindOfStand: e.target.value,
    }));
  };

  const atras = (e) => {
    e.preventDefault();
    props.history.goBack();
  };

  const history = useHistory();

  const { fetch, isLoading } = useFetch({
    initialUri: "http://localhost:8080/simulacion/getFechaInicial",
    initialMethod: "POST",
  });

  const routeSimulacion3Dias = async (e) => {
    e.preventDefault();

    if (
      (archivos && archivos.length === 2) ||
      (storage.files && storage.files.length === 2)
    ) {
      if (storage.files && storage.files.length === 2) {
        console.log("Los archivos ya existían");
      } else {
        changeStorage({
          files: archivos,
        });
      }

      let path;
      if (item.kindOfStand === "option1") {
        path = `/simulacion3Dias`;
      } else {
        //alert("Esta página no está lista");
        path = `/simulacionColapso`;
      }
      history.push(path);
    } else {
      console.log("Could not send files");
    }
  };

  const [archivos, setArchivos] = useState();

  const setearArchivos = (e) => {
    e.preventDefault();
    setArchivos(e.target.files);
  };

  useEffect(() => {
    changeUser({
      sim3Velocity: 0,
    });
    setRangeval(0);
    return () => {
      // Anything in here is fired on component unmount.
    };
  }, []);

  return (
    <React.Fragment>
      <Navbar
        className="flex-row justify-content-between pt-3 pb-3 navbar3 sticky"
        sticky="top"
      >
        <Navbar.Brand className="col-11 px-5 navbar3__tittle" href="">
          SIMULACIÓN
        </Navbar.Brand>
      </Navbar>

      <div className="separator" />

      <div className="form container-fluid mt-5 pedidos-container">
        <div className="flex-row justify-content-center">
          <h4 className="col-12">
            Realiza los siguientes pasos para ejecutar la simulación:
          </h4>

          <div className="col-12 simulacion-cards">
            <div className="row g-4">
              <div className="col-4">
                <div className="d-flex justify-content-center">
                  <div className="paso">
                    <b>1</b>
                  </div>
                </div>
                <div className="col">
                  <Card className=" card-simulacion">
                    <Card.Body>
                      <Card.Title>
                        Utiliza este formato de muestra para poder ingresar los
                        datos de la simulación
                      </Card.Title>

                      <a href={descargable} download>
                        <img
                          className="descarga"
                          alt="img"
                          src={files}
                          height="100"
                        />
                      </a>

                      <Card.Text>
                        Descargar formato de muestra
                        <br />
                        <b>No olvides descomprimir</b>
                      </Card.Text>
                    </Card.Body>
                  </Card>
                </div>
              </div>
              <div className="col-4">
                <div className="d-flex justify-content-center">
                  <div className="paso">
                    <b>2</b>
                  </div>
                </div>
                <div className="col">
                  <Card className=" card-simulacion">
                    <Card.Body>
                      <Card.Title>
                        Sube el archivo para inicializar la simulación
                      </Card.Title>
                      <Card.Text className="subida">
                        Pedidos y Bloqueos
                      </Card.Text>
                      <label className="custom-file-upload">
                        <input
                          type="file"
                          name="files"
                          onChange={setearArchivos}
                          multiple
                        />
                        Sube tus archivos
                      </label>

                      <Card.Text>
                        Tamaño máximo: 1mb
                        <br />
                        <b>No necesitas subirlos de nuevo</b>
                      </Card.Text>
                    </Card.Body>
                  </Card>
                </div>
              </div>
              <div className="col-4">
                <div className="d-flex justify-content-center">
                  <div className="paso">
                    <b>3</b>
                  </div>
                </div>
                <div className="form-group col">
                  <Card className=" card-simulacion">
                    <Card.Body>
                      <Card.Title>Elige el tipo de simulación</Card.Title>

                      <Card.Text className="tipos">Tipos</Card.Text>

                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="exampleRadios"
                          id="exampleRadios1"
                          value="option1"
                          onChange={handleChange}
                          checked={kindOfStand === "option1"}
                        />
                        <label className="form-check-label">
                          Simulación a 3 Días
                        </label>
                      </div>
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="exampleRadios"
                          id="exampleRadios2"
                          value="option2"
                          onChange={handleChange}
                          checked={kindOfStand === "option2"}
                        />
                        <label className="form-check-label">
                          Simulación hasta el colapso
                        </label>
                      </div>
                      <div className="slidecontainer">
                        <input
                          type="range"
                          className="custom-range slider"
                          min="0"
                          max="28"
                          defaultValue="0"
                          onChange={(e) => changeDivisor(e)}
                        />
                        <p>Velocidad: {!rangeval ? 1 : divisores[rangeval]}</p>
                      </div>
                    </Card.Body>
                  </Card>
                </div>
              </div>
            </div>
          </div>

          <div className="col-12 justify-content-center">
            <form className="row">
              <button
                className="btn btn-primary col  pedidos-container__button"
                onClick={atras}
              >
                Cancelar
              </button>

              <button
                type="submit"
                className="btn btn-primary col  pedidos-container__button"
                onClick={routeSimulacion3Dias}
                disabled={isLoading}
              >
                Continuar
              </button>
            </form>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}

export default Simulacion;
