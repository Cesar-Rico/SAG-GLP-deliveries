/* eslint-disable */
import React, { useEffect, useState, useContext } from "react";
import { useFetch } from "../../hooks/useFetch";
import { Navbar, Row, Card, Col, Modal, Button } from "react-bootstrap";
import Moment from "react-moment";
import info from "../../images/info.png";
import CardInfo from "../../components/CardInfo.jsx";
import Mapa from "../../components/Mapa.jsx";
import Pedido from "../../components/Pedido.jsx";
import Truck from "../../components/Truck.jsx";
import Almacen from "../../components/Almacen.jsx";
import { UserContext } from "../UserContext";
import { StorageContext } from "../StorageContext";
import * as moment from "moment";

function Simulacioncollapse(props) {
  //Funciones o hooks
  const { storage, changeStorage } = useContext(StorageContext);
  const { user, changeUser } = useContext(UserContext);
  const [open, setOpen] = useState(false);
  const [hour, setHour] = useState();
  const [truck, setTrucks] = useState([]);
  const [orders, setOrders] = useState([]);
  const [blocks, setBlocks] = useState([]);
  const [mapa, setMapa] = useState([]);
  const [tank, setTank] = useState([]);
  const [fetching, setFetching] = useState(false);
  const [allowUpload, setAllowUpload] = useState(true);
  const [_trucks, set_Trucks] = useState([]);
  const [_orders, set_Orders] = useState([]);
  const [_blocks, set_Blocks] = useState([]);
  const [_start, set_Start] = useState([]);
  const [_end, set_End] = useState([]);
  const [hrShow, setHrShow] = useState(false);
  const [texto, setTexto] = useState("");
  const [idRuta, setIdRuta] = useState(-1);

  let changeHour = null;
  let eventSource = null;
  const divisores = [
    1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24, 30, 36, 40, 45, 48, 60,
    72, 80, 90, 120, 144, 180, 240, 360, 720,
  ];

  //Modal
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  //Los paramateros se trabajarn en forma de lista y se hará lo mismo por cada uno con un foreach
  let count = 0;
  let aux_hour = -1;
  //let limit_hour = 0;
  let render_hour = 0;
  let init_hour = 0;
  let auxOrders = [];
  let auxTrucks = [];
  let auxBlocks = [];

  //Mapa---------------------------------------------------------------------------------------
  mapa[0] = document.getElementById("mapaCol");
  mapa[1] = user.mapaX + 1;
  mapa[2] = user.mapaY + 1;
  mapa[3] = user.mapaOn;

  //Almacenes---------------------------------------------------------------------------------------
  tank[0] = [user.centralX, user.centralY, 0, 0, true];
  tank[1] = [user.pR1X, user.pR1Y, 1000, 1000, false];
  tank[2] = [user.pR2X, user.pR2Y, 1000, 1000, false];

  async function clearBorders() {
    for (let j = 0; j < user.mapaY; j++) {
      for (let i = 0; i < user.mapaX; i++) {
        let cellAux = document.getElementById(
          i.toString() + "," + j.toString()
        );
        if (j === 0 && i === user.mapaX - 1) {
          //console.log("Nada");
        } else if (i === user.mapaX - 1) {
          if (cellAux) cellAux.style.borderLeftColor = user.cellbordercolor;
        } else if (j === 0) {
          if (cellAux) cellAux.style.borderBottomColor = user.cellbordercolor;
        } else {
          if (cellAux) {
            cellAux.style.borderLeftColor = user.cellbordercolor;
            cellAux.style.borderBottomColor = user.cellbordercolor;
          }
        }
      }
    }
  }

  const hrClose = () => {
    setHrShow(false);
    setTexto("");
    setIdRuta(-1);
  };

  const hrOpen = (id) => {
    let ind = 0;
    for (ind = 0; ind < truck.length; ind++) {
      if (truck[ind][3] === id) break;
    }
    setIdRuta(id);
    setTexto(truck[ind][9]);
    setHrShow(true);
  };

  const uploadToServer = (url, data) => {
    setFetching(true);

    const requestOptions = {
      method: "POST",
      mode: "no-cors",
      body: data,
    };
    fetch(url, requestOptions).then(() => setAllowUpload(true));
  };

  const initialHour = (initialTime) => {
    aux_hour = new Date(initialTime);
    init_hour = new Date(initialTime);
    //limit_hour = new Date(aux_hour.getTime() + 3600 * 1000 * 72);
    render_hour = new Date(aux_hour.getTime());
    setHour(moment(aux_hour));
  };

  useEffect((event) => {
    const data = new FormData();
    data.append("file1", storage.files[0]);
    data.append("file2", storage.files[1]);
    let url = "http://localhost:8080/collapse/run";
    eventSource = new EventSource("http://localhost:8080/collapse/set");
    let guidValue = null;

    eventSource.addEventListener("GUI_ID", (event) => {
      guidValue = JSON.parse(event.data);
      data.append("guid", guidValue);

      eventSource.addEventListener(guidValue, (event) => {
        const result = JSON.parse(event.data);

        if (result.data.initialTime !== null) {
          initialHour(result.data.initialTime);
        }
        _trucks.push(result.data.trucks);
        _orders.push(result.data.orders);
        _blocks.push(result.data.blockings);
        _start.push(new Date(result.data.startTime));
        _end.push(new Date(result.data.endTime));

        //CUANDO EL MENSAJE ALCANZA EL COLAPSO, SE DEBERÏA DETENER EL LISTENER.
        /* if (
          result.data.startTime !== undefined &&
          new Date(result.data.startTime) === limit_hour
        ) {
          clearInterval(changeHour);
        } */
      });
      uploadToServer(url, data);
      //RAUL

      changeHour = setInterval(() => {
        //Comienzo la simulación cuando tengo la suficiente data
        if (aux_hour !== -1 && _start.length > 12) {
          if (aux_hour.getTime() >= render_hour.getTime()) {
            //Limpiamos
            auxTrucks = [];
            auxOrders = [];
            auxBlocks = [];

            render_hour = new Date(
              render_hour.getTime() + 3600 * 1000 * user.batchTime //Esta en horas
            );

            let i = parseInt(
              (aux_hour.getTime() - init_hour.getTime()) /
                (3600 * 1000 * user.batchTime) //Ejemplo, 12 min = 1/5 horas
            );

            //Por cada Bloqueo
            for (
              let indice = 0;
              _blocks[i + 2] && indice < _blocks[i + 2].length;
              indice++
            ) {
              auxBlocks.push([
                null,
                _blocks[i + 2][indice].l_nodes, //Sino solamente nodes
                new Date(_blocks[i + 2][indice].start),
                new Date(_blocks[i + 2][indice].end),
                false,
              ]);
            }

            //Por cada Orden
            for (
              let indice = 0;
              _orders[i + 2] && indice < _orders[i + 2].length;
              indice++
            ) {
              let auxiliar = [
                null,
                _orders[i + 2][indice].request.x,
                _orders[i + 2][indice].request.y,
                _orders[i + 2][indice].id,
                new Date(_orders[i + 2][indice].request.dateOrder),
                _orders[i + 2][indice].request.amountGLP,
                _orders[i + 2][indice].request.hLimit,
                false,
                -1,
                _orders[i + 2][indice].request.id,
                false, //ESTO ANTES ERA orders[9], cambiar en el documento por orders[10]
                _orders[i + 2][indice].request.completedGLP,
              ];

              auxOrders.push(auxiliar);
            }

            //Por cada Camion
            for (
              let indice = 0;
              _trucks[i + 1] && indice < _trucks[i + 1].length;
              indice++
            ) {
              //LLeno la información inicial
              let auxiliar = [
                null,
                _trucks[i + 1][indice].x,
                _trucks[i + 1][indice].y,
                _trucks[i + 1][indice].id,
                _trucks[i + 1][indice].lpgMeter,
                _trucks[i + 1][indice].packages, // a donde tiene que ir primero pos x
                _trucks[i + 1][indice].route, //Inicialmente serán las POSICIONES
                _trucks[i + 1][indice].lpgCapacity,
                false,
                _trucks[i + 1][indice].routeReport,
              ];

              //Asingo los a los PEDIDOS los CAMIONES
              for (let indice2 = 0; indice2 < auxiliar[5].length; indice2++) {
                for (let indice3 = 0; indice3 < auxOrders.length; indice3++) {
                  if (auxOrders[indice3][3] === auxiliar[5][indice2].id) {
                    //Debemos asignar ese camion al pedido
                    auxOrders[indice3][8] = auxiliar[3];
                  }
                }
              }

              //Sobreescribo las POSICIONES por los MOVIMIENTOS por los que debe pasar
              let auxMovements = [];
              auxiliar[6].unshift({ x: auxiliar[1], y: auxiliar[2] });

              for (
                let indice4 = 0;
                indice4 < auxiliar[6].length - 1; //TIENE QUE HABER POR LO MENOS 2
                indice4++
              ) {
                if (
                  auxiliar[6][indice4 + 1].x - auxiliar[6][indice4].x === 0 &&
                  auxiliar[6][indice4 + 1].y - auxiliar[6][indice4].y === 0
                ) {
                } else {
                  auxMovements.push(
                    auxiliar[6][indice4 + 1].x - auxiliar[6][indice4].x
                  );
                  auxMovements.push(
                    auxiliar[6][indice4 + 1].y - auxiliar[6][indice4].y
                  );
                }
              }

              auxiliar[6] = auxMovements;

              auxTrucks.push(auxiliar);
            }

            //console.log("Pedidos : ", auxOrders);
            //console.log("Camiones: ", auxTrucks);
            //console.log("Bloqueos: ", auxBlocks);

            setOrders(Array.prototype.slice.call(auxOrders));
            setTrucks(Array.prototype.slice.call(auxTrucks));
            setBlocks(Array.prototype.slice.call(auxBlocks));
          }

          aux_hour = new Date(
            aux_hour.getTime() + 100 * divisores[user.sim3Velocity]
          );
          setHour(moment(aux_hour));
        }
      }, 100);
    });

    eventSource.onerror = (event) => {
      if (event.target.readyState === EventSource.CLOSED) {
        console.log("El listener falló (" + event.target.readyState + ")");
      }
      eventSource.close();
    };

    eventSource.onopen = () => {
      console.log("El listener comenzó a traer DATA");
    };
  }, []);

  const renderPage = setInterval(() => {
    //Comienzo la simulación cuando tengo la suficiente data
    if (aux_hour !== -1 && _start.length > 12) {
      //Por cada Bloqueo
      for (let i = 0; i < auxBlocks.length; i++) {
        if (
          auxBlocks[i][2].getTime() <= aux_hour.getTime() &&
          aux_hour.getTime() <= auxBlocks[i][3].getTime()
        ) {
          for (let j = 0; j < auxBlocks[i][1].length - 2; j += 2) {
            let V = {
              x: parseInt(auxBlocks[i][1][j]),
              y: parseInt(auxBlocks[i][1][j + 1]),
            };
            let W = {
              x: parseInt(auxBlocks[i][1][j + 2]),
              y: parseInt(auxBlocks[i][1][j + 3]),
            };

            //Linea Vertical
            if (V.x === W.x) {
              if (V.y > W.y) [V, W] = [W, V];

              let cantY = W.y - V.y;
              for (let indice = 0; indice < cantY; indice++) {
                let pointer = document.getElementById(
                  V.x.toString() + "," + (V.y + indice).toString()
                );
                if (pointer) pointer.style.borderLeftColor = "red";
              }
            }

            //Linea Horiontal
            if (V.y === W.y) {
              if (V.x > W.x) [V, W] = [W, V];

              let cantX = W.x - V.x;
              for (let indice = 0; indice < cantX; indice++) {
                let pointer = document.getElementById(
                  (V.x + indice).toString() + "," + V.y.toString()
                );
                if (pointer) pointer.style.borderBottomColor = "red";
              }
            }
          }
        }

        if (
          auxBlocks[i][3].getTime() < aux_hour.getTime() &&
          auxBlocks[i][4] === false
        ) {
          auxBlocks[i][4] = true;
          //if (i === 0) console.log("El bloqueo acabó");
          clearBorders();
        }
      }

      //Por cada Camion
      for (let indice = 0; indice < auxTrucks.length; indice++) {
        auxTrucks[indice][0] = document.getElementById(
          "TA " + indice.toString()
        );

        if (auxTrucks[indice] && auxTrucks[indice][0]) {
          //Inicializamos su posición
          if (count === 0) {
            auxTrucks[indice][0].style.left =
              (user.gridsize * auxTrucks[indice][1]).toString() + "px";
            auxTrucks[indice][0].style.bottom =
              (user.gridsize * auxTrucks[indice][2]).toString() + "px";
          }

          //Moveremos al camión truck[indice]
          if (auxTrucks[indice][6]) {
            // Si el camión tiene movimientos, lo muevo
            auxTrucks[indice][0].style.left =
              (
                user.gridsize *
                (auxTrucks[indice][1] +
                  (auxTrucks[indice][6][0] * count) /
                    (720 / divisores[user.sim3Velocity]))
              ).toString() + "px";

            auxTrucks[indice][0].style.bottom =
              (
                user.gridsize *
                (auxTrucks[indice][2] +
                  (auxTrucks[indice][6][1] * count) /
                    (720 / divisores[user.sim3Velocity]))
              ).toString() + "px";
          }
        }
      }

      //Por cada Pedido
      for (let indice = 0; indice < auxOrders.length; indice++) {
        auxOrders[indice][0] = document.getElementById(
          "Pedido " + auxOrders[indice][3].toString()
        );

        if (
          //Muestra y oculta las órdenes
          auxOrders[indice] &&
          auxOrders[indice][0] &&
          auxOrders[indice][4] &&
          auxOrders[indice][7] === false
        ) {
          if (auxOrders[indice][4].getTime() <= aux_hour.getTime()) {
            auxOrders[indice][7] = true;
            auxOrders[indice][0].style.display = "block";
          } else {
            auxOrders[indice][0].style.display = "none";
          }

          if (
            auxOrders[indice][4].getTime() + auxOrders[indice][6] * 3600000 <
            aux_hour.getTime()
          ) {
            handleShow();
            eventSource.close();
            clearInterval(renderPage);
            clearInterval(changeHour);
            console.log(
              "El colapso sucedió en el pedido ",
              auxOrders[indice][9]
            );
            auxOrders[indice][0].style.zIndex = "9999999999999999";
            auxOrders[indice][10] = true;
          }
        }
      }

      count += 1;

      if (count === 720 / divisores[user.sim3Velocity]) {
        count = 0;
        for (let indice = 0; indice < auxTrucks.length; indice++) {
          if (auxTrucks[indice][6].length !== 0) {
            //Si tiene movimientos, actualizo posición y lo elimino
            auxTrucks[indice][1] += auxTrucks[indice][6].shift();
            auxTrucks[indice][2] += auxTrucks[indice][6].shift();
          }

          if (
            //Si el camión ha llegado a una orden
            auxTrucks[indice][5] &&
            auxTrucks[indice][5][0] &&
            (auxTrucks[indice][5][0].request === null ||
              (auxTrucks[indice][5][0].request &&
                auxTrucks[indice][5][0].request.x &&
                auxTrucks[indice][5][0].request.y &&
                auxTrucks[indice][1] === auxTrucks[indice][5][0].request.x &&
                auxTrucks[indice][2] === auxTrucks[indice][5][0].request.y))
          ) {
            //Si es un punto de recarga o central
            if (
              (auxTrucks[indice][1] === 12 && auxTrucks[indice][2] === 8) ||
              (auxTrucks[indice][1] === 42 && auxTrucks[indice][2] === 42) ||
              (auxTrucks[indice][1] === 63 && auxTrucks[indice][2] === 3)
            ) {
              auxTrucks[indice][4] = auxTrucks[indice][7]; //Se recargó su combustible
            } else {
              //Actualizamos el GLP del camión
              if (auxTrucks[indice] && auxTrucks[indice][4])
                auxTrucks[indice][4] -= auxTrucks[indice][5][0].amountGLP;

              let indiceO = 0;
              for (indiceO = 0; indiceO < auxOrders.length; indiceO++) {
                if (auxTrucks[indice][5][0].id === auxOrders[indiceO][3]) break;
              }

              let pedido = document.getElementById(
                "Pedido " + auxTrucks[indice][5][0].id.toString()
              );

              if (pedido) {
                pedido.style.display = "none";
              }
            }
            auxTrucks[indice][5].shift(); //Quitamos una orden de la lista de Ordenes del Camion
          }
        }
      }
    }
  }, 100);

  useEffect(() => {
    //console.log(storage.files);
    console.log("Velocidad ", divisores[user.sim3Velocity]);
    console.log("Mapa :", user.mapaX, ",", user.mapaY);
    return () => {
      // Anything in here is fired on component unmount.
      aux_hour = -1;
      clearInterval(changeHour);
      clearInterval(renderPage);
    };
  }, []);

  //Comienzo del HTML
  return (
    <>
      <Navbar
        className="flex-row justify-content-between pt-3 pb-3 navbar3"
        sticky="top"
      >
        <Navbar.Brand className="col-9 px-5 navbar3__tittle" href="">
          SIMULACIÓN HASTA EL COLAPSO
        </Navbar.Brand>

        <div
          className={`d-flex hora hora--moment ${
            _start.length > 12 ? "open" : "close"
          }`}
          id="hora"
        >
          &nbsp;Hora virtual : &nbsp;
          <Moment format="DD/MM/YY, HH:mm:ss">{hour}</Moment>
        </div>

        <img
          type="button"
          className={`info-button__img ${
            _start.length > 12 ? "open" : "close"
          }`}
          alt="img"
          src={info}
          id="cardInfoButton"
          onClick={() => setOpen(!open)}
        />
        <div className="info-button__card-container">
          <div
            className={`info-button__card ${open ? "open" : "close"}`}
            id="cardInfo"
          >
            <CardInfo mapa={mapa} />
          </div>
        </div>
      </Navbar>

      <Modal className="modalColapso" show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Colapso Logístico</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Puedes observar el mapa para obtener mayor información
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>

      <div className="separator" />

      {_start.length > 12 ? (
        <>
          <div className="row row-mapa">
            <div className="col">
              <div
                className="d-flex flex-wrap map"
                id="mapaSim"
                style={{
                  width: (12 * (mapa[1] + 1)).toString() + "px",
                }}
              >
                {/* MAPA INICIAL*/}

                <Mapa x={mapa[1]} y={mapa[2]} on={mapa[3]} />

                {/* RESTO DE ELEMENTOS CON UN Z-INDEX DISTINTO*/}
                {/* ALMACENES*/}
                {Array.from(tank).map((v, i) => (
                  //v: elemento[i], i: index
                  <Almacen
                    posx={v[0]}
                    posy={v[1]}
                    capacidad={160}
                    capacidadActual={v[3]}
                    principal={v[4]}
                    descripcion={
                      v[4] ? "Almacen" : "Punto de Recarga " + i.toString()
                    }
                    idAlmacen={i.toString()}
                    key={"c-alm " + i.toString()}
                  />
                ))}

                {/* CAMIONES*/}
                {Array.from(truck).map((v, i) => (
                  //v: elemento[i], i: index
                  <Truck
                    descripcion={"Camión " + v[3]}
                    idTruck={i}
                    glp={v[4] + " / " + v[7]}
                    glpCapacity={v[7]}
                    x={v[1]}
                    y={v[2]}
                    destino={
                      v[5][0] && v[5][0].request && v[5][0].request.x
                        ? "(" +
                          v[5][0].request.x +
                          "," +
                          v[5][0].request.y +
                          ")"
                        : "Procesando"
                    }
                    key={"c-truck " + i.toString()}
                    retorno={
                      v &&
                      v[5] &&
                      (v[5].length === 0 ||
                        (v[5][0] &&
                          v[5][0].request === null &&
                          v[5].length <= 1))
                        ? true
                        : false
                    }
                    simulacion={true}
                    averiado={false}
                  />
                ))}

                {/*PEDIDOS*/}
                {Array.from(orders).map((v, i) => (
                  //v: elemento[i], i: index
                  <Pedido
                    descripcion={"Pedido " + v[9]}
                    idPedido={v[3]}
                    posx={v[1]}
                    posy={v[2]}
                    glp={v[5]}
                    glpStatus={v[11]}
                    fechaRegistro={moment(v[4])
                      .format("DD/MM/YY, HH:mm:ss")
                      .toString()}
                    key={"c-ped " + i.toString()}
                    simulacion={true}
                    colapso={v[10]}
                    camion={v[8] !== -1 ? "Camion " + v[8] : "Ninguno"}
                  />
                ))}
              </div>
            </div>

            <div className="col hola">
              <Row>
                <h2 className="col d-flex justify-content-center">Camiones</h2>
              </Row>
              <Row xs={1} md={1} className="g-2">
                {Array.from(truck).map((_, idx) => (
                  <Col
                    className=" d-flex justify-content-center"
                    key={"card-der" + (idx + 1)}
                  >
                    <Card className="card-derecha">
                      <Card.Body>
                        <div className="row">
                          <Card.Title
                            className={`col-8 map__truck__tablero__text map__truck__tablero__tittle ${
                              truck[idx] &&
                              truck[idx][5] &&
                              (truck[idx][5].length === 0 ||
                                (truck[idx][5][0] &&
                                  truck[idx][5][0].request === null &&
                                  truck[idx][5].length <= 1))
                                ? "map__truck__tablero__tittle--green"
                                : "map__truck__tablero__tittle--blue"
                            }`}
                          >
                            Camión{" "}
                            {truck[idx] && truck[idx][3] ? truck[idx][3] : ""}
                          </Card.Title>
                          <Button
                            className={`col-3 cordButton monButton ${
                              false ? "close" : "open"
                            }`}
                            variant="secondary"
                            size="sm"
                            onClick={() => hrOpen(truck[idx][3])}
                          >
                            Ruta
                          </Button>
                        </div>

                        <Card.Text className="map__truck__tablero__text">
                          GLP:{" "}
                          {truck[idx] &&
                          truck[idx][4] !== null &&
                          truck[idx][7] !== null
                            ? truck[idx][4] + " / " + truck[idx][7] + "m"
                            : "0 / 0" + "m"}
                          <sup>3</sup>
                        </Card.Text>
                        {/* <Card.Text className="map__truck__tablero__text">
                          Ruta:{" "}
                          {truck[idx] && truck[idx][5] && truck[idx][5][0]
                            ? truck[idx][5].map((v, idy) =>
                                v.station === null && v.request !== null
                                  ? "(" + v.request.x + "," + v.request.y + ") "
                                  : v.request === null && v.station !== null
                                  ? "(" + v.station.x + "," + v.station.y + ") "
                                  : "Ninguna Ruta"
                              )
                            : "Procesando Ruta"}
                        </Card.Text> */}
                      </Card.Body>
                    </Card>
                  </Col>
                ))}
              </Row>
              <Modal className="modalMon" show={hrShow} onHide={hrClose}>
                <Modal.Header closeButton>
                  <Modal.Title>Camión {idRuta} - Hoja de Ruta</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  {Array.from(texto ? texto.split("\n") : []).map((v, i) => (
                    <p>{v}</p>
                  ))}
                </Modal.Body>
                <Modal.Footer>
                  <Button variant="secondary" onClick={hrClose}>
                    Cerrar
                  </Button>
                </Modal.Footer>
              </Modal>
            </div>
          </div>

          {/* <div className="d-flex mx-5 m-2 hora">Eje coordenadas</div> */}
        </>
      ) : (
        <div className="loader">Loading...</div>
      )}
    </>
  );
}

export default Simulacioncollapse;
