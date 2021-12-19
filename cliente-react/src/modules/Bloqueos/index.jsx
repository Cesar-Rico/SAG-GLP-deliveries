/* eslint-disable */
import React, { useState, useContext, useEffect } from "react";
import { Navbar, Modal, Button, Card } from "react-bootstrap";
import { useFetch } from "../../hooks/useFetch.jsx";
import { UserContext } from "../UserContext";

function Bloqueos(props) {
  const { user, changeUser } = useContext(UserContext);

  const { fetch, isLoading } = useFetch({
    initialUri: "http://localhost:8080/blocking/bulk",
    initialMethod: "POST",
  });

  const [show, setShow] = useState(false);
  const [texto, setTexto] = useState("");

  const handleClose = () => setShow(false);

  const [archivo, setArchivo] = useState(null);

  const setearArchivo = (e) => {
    e.preventDefault();
    setArchivo(e.target.files);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (archivo !== null) {
      const data = new FormData();
      data.append("file", archivo[0]);
      data.append("file2", archivo[0]);

      /* changeUser({
        fileBloqueos: archivo[0],
      }); */

      const resp = await fetch(data);

      if (resp.status === 200) {
        console.log("Success:", resp);
        setTexto("El archivo de bloqueos se ingresó con éxito");
        setShow(true);
      } else {
        setTexto("No se pudo ingresar el archivo de bloqueos");
        setShow(true);
      }
    } else {
      setTexto("No se pudo ingresar los bloqueos");
      setShow(true);
    }
  };

  useEffect(() => {
    changeUser({
      monitoreoData: false,
    });
  }, []);

  return (
    <React.Fragment>
      <Navbar
        className="flex-row justify-content-between pt-3 pb-3 navbar3 sticky"
        sticky="top"
      >
        <Navbar.Brand className="col-11 px-5 navbar3__tittle" href="">
          INGRESAR BLOQUEOS
        </Navbar.Brand>
      </Navbar>

      <div className="separator" />

      <div className="container-fluid mt-5 pedidos-container">
        <div className="d-flex justify-content-center">
          <form className="row" onSubmit={handleSubmit}>
            <div className="col-12">
              <Card className=" card-simulacion">
                <Card.Body>
                  <Card.Title>Sube el archivo para los bloqueos</Card.Title>
                  <Card.Text className="subida">Pedidos y Bloqueos</Card.Text>
                  <label className="custom-file-upload">
                    <input
                      type="file"
                      name="files"
                      onChange={setearArchivo}
                      multiple
                    />
                    Súbelo aquí
                  </label>

                  <Card.Text>
                    Tamaño máximo: 1mb
                    <br />
                    <b>No necesitas subirlos de nuevo</b>
                  </Card.Text>
                </Card.Body>
              </Card>
            </div>
            {/* <div className="form-group col-6 pedidos-container__form">
              <label htmlFor="exampleInputEmail1">Hora de Inicio</label>
              <input
                className="form-control"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="DD:MM:SS"
                //value={pedido.posx}
                onChange={(e) =>
                  setBloqueo({ ...bloqueo, hini: e.target.value })
                }
              />
            </div>

            <div className="form-group col-6 pedidos-container__form">
              <label htmlFor="exampleInputEmail1">Hora de Fin</label>
              <input
                className="form-control"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="DD:MM:SS"
                //value={pedido.posy}
                onChange={(e) =>
                  setBloqueo({ ...bloqueo, hfin: e.target.value })
                }
              />
            </div>

            <div className="form-group col-12 pedidos-container__form">
              <label htmlFor="exampleInputEmail1">Esquinas del bloqueo</label>
              <input
                className="form-control"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="x1,y1,x2,y2,..."
                //value={pedido.pedidoGlp}
                onChange={(e) =>
                  setBloqueo({ ...bloqueo, esquinas: e.target.value })
                }
              />
            </div> */}

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
              disabled={isLoading}
            >
              Ingresar
            </button>

            <Modal show={show} onHide={handleClose}>
              <Modal.Header closeButton>
                <Modal.Title>Ingreso de bloqueos</Modal.Title>
              </Modal.Header>
              <Modal.Body>{texto}</Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                  Cerrar
                </Button>
              </Modal.Footer>
            </Modal>
          </form>
        </div>
      </div>
    </React.Fragment>
  );
}

export default Bloqueos;
