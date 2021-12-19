/* eslint-disable */
import React, { useState, useContext, useEffect } from "react";
import { Navbar, Modal, Button } from "react-bootstrap";
import { useFetch } from "../../hooks/useFetch.jsx";
import { UserContext } from "../UserContext";

function Pedidos(props) {
  const [pedido, setPedido] = useState({
    x: 0,
    y: 0,
    amountGLP: 0,
    hLimit: 4,
    dateOrder: null,
    dateCompleted: null,
    completed: false,
  });

  const { user, changeUser } = useContext(UserContext);

  const { fetch, isLoading } = useFetch({
    initialUri: "http://localhost:8080/request/add",
    initialMethod: "POST",
  });

  const [show, setShow] = useState(false);
  const [texto, setTexto] = useState("");

  const handleClose = () => setShow(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (
      pedido.hLimit >= 4 &&
      pedido.x >= 0 &&
      pedido.y >= 0 &&
      pedido.x < user.mapaX &&
      pedido.y < user.mapaY &&
      pedido.amountGLP > 0
    ) {
      let aux = pedido;
      aux.dateOrder = new Date();

      setPedido(aux);

      const resp = await fetch(pedido);

      if (resp.status === 200) {
        console.log("Success:", resp);
        setTexto("El pedido se ingresó con éxito");
        setShow(true);

        setPedido({
          ...pedido,
          x: 0,
          y: 0,
          hLimit: 4,
          amountGLP: 0,
        });
      } else {
        setTexto("No se pudo ingresar el pedido");
        setShow(true);
      }
    } else {
      if (pedido.hLimit < 4) {
        setTexto(
          "El sistema solo permite registrar pedidos con un mínimo de 4 horas de antelación"
        );
      }

      if (
        pedido.x < 0 ||
        pedido.y < 0 ||
        pedido.x >= user.mapaX ||
        pedido.y >= user.mapaY
      ) {
        setTexto("Las coordenadas están fuera del rango");
      }

      if (pedido.amountGLP <= 0) {
        setTexto("El monto de GLP no puede ser menor a 0");
      }
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
          INGRESAR UN PEDIDO
        </Navbar.Brand>
      </Navbar>

      <div className="separator" />

      <div className="container-fluid mt-5 pedidos-container">
        <div className="d-flex justify-content-center">
          <form className="row" onSubmit={handleSubmit}>
            <div className="form-group col-6 pedidos-container__form">
              <label htmlFor="exampleInputEmail1">Posición X</label>
              <input
                className="form-control"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="Ingrese la posición X del pedido"
                value={pedido.x}
                onChange={(e) => setPedido({ ...pedido, x: e.target.value })}
              />
            </div>

            <div className="form-group col-6 pedidos-container__form">
              <label htmlFor="exampleInputEmail1">Posición Y</label>
              <input
                className="form-control"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="Ingrese la posición Y del pedido"
                value={pedido.y}
                onChange={(e) => setPedido({ ...pedido, y: e.target.value })}
              />
            </div>

            <div className="form-group col-12 pedidos-container__form">
              <label htmlFor="exampleInputEmail1">Cantidad GLP(m3)</label>
              <input
                className="form-control"
                id="exampleInputEmail1"
                aria-describedby="emailHelp"
                placeholder="Ingrese la cantidad de GLP pedido en m3"
                value={pedido.amountGLP}
                onChange={(e) =>
                  setPedido({ ...pedido, amountGLP: e.target.value })
                }
              />
            </div>

            <div className="form-group col-12 pedidos-container__form">
              <label htmlFor="exampleInputPassword1">
                Horas límite de entrega
              </label>
              <input
                className="form-control"
                id="exampleInputPassword1"
                placeholder="Ingrese la hora límite de entrega del pedido"
                value={pedido.hLimit}
                onChange={(e) =>
                  setPedido({ ...pedido, hLimit: e.target.value })
                }
              />
            </div>

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
                <Modal.Title>Ingreso de pedidos</Modal.Title>
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

export default Pedidos;
