/* eslint-disable */
import React, { useEffect, useState, useContext } from "react";
import { useFetch } from "../../hooks/useFetch";
import { Navbar, Row, Card, Col, Button, Modal } from "react-bootstrap";
import Moment from "react-moment";
import moment from "moment";
import info from "../../images/info.png";
import CardInfo from "../../components/CardInfo.jsx";
import Mapa from "../../components/Mapa.jsx";
import Pedido from "../../components/Pedido.jsx";
import Truck from "../../components/Truck.jsx";
import Almacen from "../../components/Almacen.jsx";
import { UserContext } from "../UserContext";
import { StorageContext } from "../StorageContext";

function Monitoreo(props) {
  //Funciones o hooks o ayudas
  const { user, changeUser } = useContext(UserContext);
  const { storage, changeStorage } = useContext(StorageContext);
  const [open, setOpen] = useState(false);
  const [hour, setHour] = useState();
  const [truck, setTrucks] = useState([]);
  const [orders, setOrders] = useState([]);
  const [blocks, setBlocks] = useState([]);
  const [points, setPoints] = useState([]);
  const [mapa, setMapa] = useState([]);
  const [tank, setTank] = useState([]);
  const [fetching, setFetching] = useState(false);
  const [allowUpload, setAllowUpload] = useState(true);
  /*   const [_trucks, set_Trucks] = useState([]);
  const [_orders, set_Orders] = useState([]);
  const [_blocks, set_Blocks] = useState([]); */
  const [twelveMinutes, setTwelveMinutes] = useState(-1);
  const [contador, setContador] = useState(0);
  const [hrShow, setHrShow] = useState(false);
  const [texto, setTexto] = useState("");
  const [idRuta, setIdRuta] = useState(-1);

  let boolean = false;

  //Los paramateros se trabajarn en forma de lista y se hará lo mismo por cada uno con un foreach
  let count = 0;
  let auxOrders = [];
  let auxTrucks = [];
  let auxBlocks = [];
  let auxPoints = [];
  let cell = [];

  //Mapa---------------------------------------------------------------------------------------
  mapa[0] = document.getElementById("mapaMon");
  mapa[1] = user.mapaX + 1;
  mapa[2] = user.mapaY + 1;
  mapa[3] = user.mapaOn;

  //Almacenes---------------------------------------------------------------------------------------
  tank[0] = [user.centralX, user.centralY, 0, 0, true];
  tank[1] = [user.pR1X, user.pR1Y, 160, 160, false];
  tank[2] = [user.pR2X, user.pR2Y, 160, 160, false];

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

  const { fetch, isLoading } = useFetch({
    initialMethod: "get",
  });

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

  const averiaSubmit = async (id) => {
    const result = await fetch(
      {},
      {
        uri:
          "http://localhost:8080/maintenance/setAveriado?idTruck=" +
          id.toString() +
          "&counter=" +
          user.contaAveria.toString(),
        method: "POST",
      }
    );

    if (result.status === 200) {
      //Busco qué camión es:
      let ind = 0;
      for (ind = 0; ind < truck.length; ind++) {
        if (truck[ind][3] === id) break;
      }

      if (truck && truck[ind]) {
        truck[ind][9] = " ";
        truck[ind][8] = true;
        truck[ind][6] = [];
        setContador(contador + 1);
      }
      console.log(
        "La avería del camión " + id.toString() + " se registró con éxito"
      );
      clearBorders();

      //Por cada Camion
      console.log(truck);
      for (let indice = 0; indice < truck.length; indice++) {
        truck[indice][0] = document.getElementById("TA " + indice.toString());

        if (truck[indice] && truck[indice][0]) {
          let i = 0,
            j = 1;
          let auxX = truck[indice][1],
            auxY = truck[indice][2];

          //Pintaremos el recorrido del camion truck[indice]
          while (i !== truck[indice][6].length) {
            if (truck[indice][6][i] === 0) {
              //VERTICAL
              cell[indice] = document.getElementById(
                (auxX + truck[indice][6][i]).toString() +
                  "," +
                  (
                    auxY +
                    (truck[indice][6][j] > 0
                      ? truck[indice][6][j] - 1
                      : truck[indice][6][j])
                  ).toString()
              );
              if (cell[indice])
                cell[indice].style.borderLeftColor = user.routecolor;
            } else if (truck[indice][6][j] === 0) {
              //HORIZONTAL
              cell[indice] = document.getElementById(
                (
                  auxX +
                  (truck[indice][6][i] > 0
                    ? truck[indice][6][i] - 1
                    : truck[indice][6][i])
                ).toString() +
                  "," +
                  (auxY + truck[indice][6][j]).toString()
              );
              if (cell[indice])
                cell[indice].style.borderBottomColor = user.routecolor;
            }

            auxX += truck[indice][6][i];
            auxY += truck[indice][6][j];
            i += 2;
            j += 2;
          }
        }
      }
      setContador(contador + 1);
    } else {
      console.log("No se pudo ingresar la avería");
    }
  };

  async function synchro() {
    const synchroPage = setInterval(() => {
      //Si han pasado 6 segundos después de la última actualización de 72 segundos entonces:
      if (
        (new Date().getMinutes() * 60 + new Date().getSeconds()) % 72 ===
        12
      ) {
        setTwelveMinutes(twelveMinutes + 1);
        clearInterval(synchroPage);
      }
    }, 100);
  }

  async function fetchBlockings() {
    console.log("Se llamó al API de Bloqueos");
    try {
      const result = await fetch(
        {},
        {
          uri: "http://localhost:8080/blocking/1day", //CAMBIAR POR LA NUEVA API
          method: "get",
        }
      );

      if (result.status === 200) {
        /* _blocks.push(result.data.payload); */

        //AQUI SE HACE TODO
        //Limpiamos
        auxBlocks = [];

        //Por cada Bloqueo
        for (
          let indice = 0;
          result.data.payload && indice < result.data.payload.length;
          indice++
        ) {
          auxBlocks.push([
            null,
            result.data.payload[indice].l_nodes, //Sino solamente nodes
            new Date(result.data.payload[indice].start),
            new Date(result.data.payload[indice].end),
            false, //Saber si ya mostró el bloqueo o no
          ]);
        }

        //console.log("Bloqueos: ", auxBlocks);

        setBlocks(Array.prototype.slice.call(auxBlocks));
      } else {
        console.log("No se pudo obtener la data de bloqueos");
      }
    } catch (e) {
      console.error(e);
    }
  }

  async function fetchData() {
    if (twelveMinutes >= 0) {
      if ((new Date().getMinutes() * 60 + new Date().getSeconds()) % 360 < 72) {
        clearBorders();
      }

      try {
        const result = await fetch(
          {},
          {
            uri: "http://localhost:8080/truck/list", //CAMBIAR POR LA NUEVA API
            method: "get",
          }
        );

        //console.log(result);

        if (result.status === 200 && boolean === false) {
          console.log(" ");
          console.log("Se trajo la Data :", new Date());
          /*           _trucks.push(result.data.payload.trucks);
          _orders.push(result.data.payload.orders); */

          //AQUI SE HACE TODO
          //Limpiamos
          auxTrucks = [];
          auxOrders = [];
          auxPoints = [];

          //Por cada Punto
          for (
            let indice = 0;
            result.data.payload.stations &&
            indice < result.data.payload.stations.length;
            indice++
          ) {
            let auxiliar = [
              null,
              result.data.payload.stations[indice].station.x,
              result.data.payload.stations[indice].station.y,
              result.data.payload.stations[indice].station.lpgMeter,
              result.data.payload.stations[indice].station.lpgCapacity,
            ];

            auxPoints.push(auxiliar);
          }

          //Por cada Orden
          for (
            let indice = 0;
            result.data.payload.orders &&
            indice < result.data.payload.orders.length;
            indice++
          ) {
            let auxiliar = [
              null,
              result.data.payload.orders[indice].x,
              result.data.payload.orders[indice].y,
              result.data.payload.orders[indice].id,
              new Date(result.data.payload.orders[indice].dateOrder),
              result.data.payload.orders[indice].amountGLP,
              result.data.payload.orders[indice].hLimit,
              false,
              -1,
              result.data.payload.orders[indice].completedGLP,
            ];

            auxOrders.push(auxiliar);
          }

          //Por cada Camion
          for (
            let indice = 0;
            result.data.payload.trucks &&
            indice < result.data.payload.trucks.length;
            indice++
          ) {
            let auxiliar = [
              null,
              result.data.payload.trucks[indice].x,
              result.data.payload.trucks[indice].y,
              result.data.payload.trucks[indice].id,
              result.data.payload.trucks[indice].lpgMeter,
              result.data.payload.trucks[indice].packages, // a donde tiene que ir primero pos x
              result.data.payload.trucks[indice].averiado === false
                ? result.data.payload.trucks[indice].route
                : [],
              result.data.payload.trucks[indice].lpgCapacity,
              result.data.payload.trucks[indice].averiado,
              result.data.payload.trucks[indice].averiado === false
                ? result.data.payload.trucks[indice].routeReport
                : " ",
            ];

            //Asingo a los PEDIDOS los CAMIONES
            for (
              let indice2 = 0;
              auxiliar[5] && indice2 < auxiliar[5].length;
              indice2++
            ) {
              for (let indice3 = 0; indice3 < auxOrders.length; indice3++) {
                if (
                  auxiliar[5][indice2].request &&
                  auxOrders[indice3][3] === auxiliar[5][indice2].request.id
                ) {
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

          //console.log("Camiones: ", auxTrucks);
          //console.log("Pedidos : ", auxOrders);
          //console.log("Recargas : ", auxPoints);

          setOrders(Array.prototype.slice.call(auxOrders));
          setTrucks(Array.prototype.slice.call(auxTrucks));
          setPoints(Array.prototype.slice.call(auxPoints));
          console.log("Comienzo del Render: ", new Date());
          /*           if (twelveMinutes === 0) {
            setTwelveMinutes(twelveMinutes + 1);
          } */
          boolean = true;
        } else {
          console.log("No se pudo obtener la data");
        }
      } catch (e) {
        console.error(e);
      }
    }
  }

  const renderPage = setInterval(() => {
    //Comienzo la simulación cuando tengo la suficiente data
    if (boolean === true) {
      //Por cada Bloqueo
      for (let i = 0; i < blocks.length; i++) {
        if (
          blocks[i][2].getTime() > new Date().getTime() &&
          blocks[i][4] === true
          //|| blocks[i][3].getTime() < new Date().getTime()
        ) {
          for (let j = 0; j < blocks[i][1].length - 2; j += 2) {
            let V = {
              x: parseInt(blocks[i][1][j]),
              y: parseInt(blocks[i][1][j + 1]),
            };
            let W = {
              x: parseInt(blocks[i][1][j + 2]),
              y: parseInt(blocks[i][1][j + 3]),
            };

            //Linea Vertical
            if (V.x === W.x) {
              if (V.y > W.y) [V, W] = [W, V];

              let cantY = W.y - V.y;
              for (let indice = 0; indice < cantY; indice++) {
                let pointer = document.getElementById(
                  V.x.toString() + "," + (V.y + indice).toString()
                );
                if (pointer)
                  pointer.style.borderLeftColor = user.cellbordercolor;
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
                if (pointer)
                  pointer.style.borderBottomColor = user.cellbordercolor;
              }
            }
          }
          blocks[i][4] = false;
        } else if (
          blocks[i][2].getTime() <= new Date().getTime() &&
          new Date().getTime() <= blocks[i][3].getTime()
        ) {
          if (blocks[i] && blocks[i][4] === false) blocks[i][4] = true;
          for (let j = 0; j < blocks[i][1].length - 2; j += 2) {
            let V = {
              x: parseInt(blocks[i][1][j]),
              y: parseInt(blocks[i][1][j + 1]),
            };
            let W = {
              x: parseInt(blocks[i][1][j + 2]),
              y: parseInt(blocks[i][1][j + 3]),
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
      }

      //Por cada Camion
      for (let indice = 0; indice < auxTrucks.length; indice++) {
        auxTrucks[indice][0] = document.getElementById(
          "TA " + indice.toString()
        );

        if (auxTrucks[indice] && auxTrucks[indice][0]) {
          //Inicializamos su posición
          if (count === 0 && auxTrucks[indice][8] === false) {
            auxTrucks[indice][0].style.left =
              (user.gridsize * auxTrucks[indice][1]).toString() + "px";
            auxTrucks[indice][0].style.bottom =
              (user.gridsize * auxTrucks[indice][2]).toString() + "px";
          }

          let i = 0,
            j = 1;
          let auxX = auxTrucks[indice][1],
            auxY = auxTrucks[indice][2];

          //Pintaremos el recorrido del camion auxTrucks[indice]
          if (count === 0) {
            while (i !== auxTrucks[indice][6].length) {
              if (auxTrucks[indice][6][i] === 0) {
                //VERTICAL
                cell[indice] = document.getElementById(
                  (auxX + auxTrucks[indice][6][i]).toString() +
                    "," +
                    (
                      auxY +
                      (auxTrucks[indice][6][j] > 0
                        ? auxTrucks[indice][6][j] - 1
                        : auxTrucks[indice][6][j])
                    ).toString()
                );
                if (cell[indice])
                  cell[indice].style.borderLeftColor = user.routecolor;
              } else if (auxTrucks[indice][6][j] === 0) {
                //HORIZONTAL
                cell[indice] = document.getElementById(
                  (
                    auxX +
                    (auxTrucks[indice][6][i] > 0
                      ? auxTrucks[indice][6][i] - 1
                      : auxTrucks[indice][6][i])
                  ).toString() +
                    "," +
                    (auxY + auxTrucks[indice][6][j]).toString()
                );
                if (cell[indice])
                  cell[indice].style.borderBottomColor = user.routecolor;
              }

              auxX += auxTrucks[indice][6][i];
              auxY += auxTrucks[indice][6][j];
              i += 2;
              j += 2;
            }
          }

          //Moveremos al camión auxTrucks[indice]
          if (auxTrucks[indice][6]) {
            // Si el camión tiene movimientos, lo muevo
            auxTrucks[indice][0].style.left =
              (
                user.gridsize *
                (auxTrucks[indice][1] +
                  (auxTrucks[indice][6][0] * count) / (720 / 1))
              ).toString() + "px";

            auxTrucks[indice][0].style.bottom =
              (
                user.gridsize *
                (auxTrucks[indice][2] +
                  (auxTrucks[indice][6][1] * count) / (720 / 1))
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
          if (auxOrders[indice][4].getTime() <= new Date().getTime()) {
            auxOrders[indice][7] = true;
            auxOrders[indice][0].style.display = "block";
          } else {
            auxOrders[indice][0].style.display = "none";
          }
        }
      }

      count += 1;
      //setContador(contador + 1);
      /* changeUser({
        contaAveria: count,
      }); */

      if (count === 720 / 1) {
        count = 0;
        /* changeUser({
          contaAveria: 0,
        }); */

        //Por cada Camión
        for (let indice = 0; indice < auxTrucks.length; indice++) {
          if (auxTrucks[indice][6].length !== 0) {
            if (auxTrucks[indice][6][0] === 0) {
              //VERTICAL
              cell[indice] = document.getElementById(
                (auxTrucks[indice][1] + auxTrucks[indice][6][0]).toString() +
                  "," +
                  (
                    auxTrucks[indice][2] +
                    (auxTrucks[indice][6][1] > 0
                      ? auxTrucks[indice][6][1] - 1
                      : auxTrucks[indice][6][1])
                  ).toString()
              );
              if (cell[indice])
                cell[indice].style.borderLeftColor = user.cellbordercolor;
            } else if (auxTrucks[indice][6][1] === 0) {
              //HORIZONTAL
              cell[indice] = document.getElementById(
                (
                  auxTrucks[indice][1] +
                  (auxTrucks[indice][6][0] > 0
                    ? auxTrucks[indice][6][0] - 1
                    : auxTrucks[indice][6][0])
                ).toString() +
                  "," +
                  (auxTrucks[indice][2] + auxTrucks[indice][6][1]).toString()
              );
              if (cell[indice])
                cell[indice].style.borderBottomColor = user.cellbordercolor;
            }
            //Si tiene movimientos, actualizo posición y lo elimino
            auxTrucks[indice][1] += auxTrucks[indice][6].shift();
            auxTrucks[indice][2] += auxTrucks[indice][6].shift();
          }

          /* if (
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
          } */
        }
        console.log("El render acabó :", new Date());
        setTwelveMinutes(twelveMinutes + 1);
        boolean = false;
      }
    }
  }, 100); //Aquí añado según el tiempo que se tarda en moverse una casilla

  useEffect(() => {
    fetchBlockings();
    synchro();
  }, []);

  useEffect(() => {
    fetchData();

    return () => {
      clearInterval(renderPage);
    };
  }, [twelveMinutes]);

  //Comienzo del HTML
  return (
    <>
      <Navbar
        className="flex-row justify-content-between pt-3 pb-3 navbar3"
        sticky="top"
      >
        <Navbar.Brand className="col-9 px-5 navbar3__tittle" href="">
          DÍA A DÍA
        </Navbar.Brand>
        {
          <>
            <div className="d-flex hora hora--moment" id="hora">
              &nbsp;Hora actual : &nbsp;
              <Moment interval={1000} format="DD/MM/YY, hh:mm:ss">
                {hour}
              </Moment>
            </div>

            <img
              type="button"
              className="info-button__img"
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
          </>
        }
      </Navbar>
      <div className="separator" />

      {truck.length > 0 ? (
        <>
          <div className="row row-mapa">
            <div className="col">
              <div
                className="d-flex flex-wrap map"
                id="mapaMon"
                style={{
                  width: (12 * (mapa[1] + 1)).toString() + "px",
                }}
              >
                {/* MAPA INICIAL*/}

                <Mapa x={mapa[1]} y={mapa[2]} on={mapa[3]} />

                {/* RESTO DE ELEMENTOS CON UN Z-INDEX DISTINTO*/}
                {/* ALMACENES*/}
                <Almacen
                  posx={12}
                  posy={8}
                  capacidad={0}
                  capacidadActual={0}
                  principal={true}
                  descripcion={
                    true ? "Almacen" : "Punto de Recarga " + i.toString()
                  }
                  idAlmacen={(0).toString()}
                  key={"c-alm " + (0).toString()}
                />
                {Array.from(points).map((v, i) => (
                  //v: elemento[i], i: index
                  <Almacen
                    posx={v[1]}
                    posy={v[2]}
                    capacidad={v[3]}
                    capacidadActual={v[4]}
                    principal={false}
                    descripcion={
                      false
                        ? "Almacen"
                        : "Punto de Recarga " + (i + 1).toString()
                    }
                    idAlmacen={(i + 1).toString()}
                    key={"c-alm " + (i + 1).toString()}
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
                    /* destino={
                  v[5][0] && v[5][0].request && v[5][0].request.x
                    ? "(" + v[5][0].request.x + "," + v[5][0].request.y + ")"
                    : "Procesando"
                } */
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
                    averiado={v[8]}
                    simulacion={false}
                  />
                ))}

                {/*PEDIDOS*/}
                {Array.from(orders).map((v, i) => (
                  //v: elemento[i], i: index
                  <Pedido
                    descripcion={"Pedido " + v[3]}
                    idPedido={v[3]}
                    posx={v[1]}
                    posy={v[2]}
                    glp={v[5]}
                    glpStatus={v[9]}
                    fechaRegistro={moment(v[4])
                      .format("DD/MM/YY, HH:mm:ss")
                      .toString()}
                    key={"c-ped " + i.toString()}
                    simulacion={false}
                    colapso={false}
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
                            className={`col-5 map__truck__tablero__text map__truck__tablero__tittle ${
                              truck[idx][8]
                                ? "map__truck__tablero__tittle--red"
                                : truck[idx] &&
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
                          {!truck[idx][8] ? (
                            <Button
                              className={`col-3 cordButton monButton ${
                                false ? "close" : "open"
                              }`}
                              variant="success"
                              size="sm"
                              onClick={() => averiaSubmit(truck[idx][3])}
                            >
                              ON
                            </Button>
                          ) : (
                            <Button
                              className={`col-3 cordButton monButton ${
                                false ? "close" : "open"
                              }`}
                              variant="danger"
                              size="sm"
                              //onClick={() => (truck[idx][8] = true)}
                              disabled
                            >
                              OFF
                            </Button>
                          )}
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

export default Monitoreo;
