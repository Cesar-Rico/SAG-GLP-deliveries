/* eslint-disable */
import React, { useState, useContext, useEffect } from "react";
import { Navbar, Modal, Button, Card } from "react-bootstrap";
import { useFetch } from "../../hooks/useFetch.jsx";
import { UserContext } from "../UserContext";
import { MDBDataTableV5 } from "mdbreact";

function Camiones(props) {
  const [datatable, setDatatable] = useState({
    columns: [
      {
        label: "Id",
        field: "id",
        width: 80,
        /*         attributes: {
          "aria-controls": "DataTable",
          "aria-label": "Id",
        }, */
      },
      {
        label: "X",
        field: "x",
        width: 80,
      },
      {
        label: "Y",
        field: "y",
        width: 80,
      },
      {
        label: "Fuel",
        field: "fuel_meter",
        width: 100,
      },
      {
        label: "Fuel Capacity",
        field: "fuel_tank_capacity",
        width: 100,
      },
      {
        label: "LPG Meter",
        field: "lpg_meter",
        width: 100,
      },
      {
        label: "LPG Capacity",
        field: "lpg_capacity",
        width: 100,
      },
      {
        label: "Avería",
        field: "averiado",
        width: 120,
      },
    ],
    rows: [
      {
        id: 1,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 2,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 3,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 4,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 5,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 6,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 7,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 8,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 9,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 10,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 5,
        lpg_capacity: 5,
        averiado: "false",
      },
      {
        id: 11,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 10,
        lpg_capacity: 10,
        averiado: "false",
      },
      {
        id: 12,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 10,
        lpg_capacity: 10,
        averiado: "false",
      },
      {
        id: 13,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 10,
        lpg_capacity: 10,
        averiado: "false",
      },
      {
        id: 14,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 10,
        lpg_capacity: 10,
        averiado: "false",
      },
      {
        id: 15,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 15,
        lpg_capacity: 15,
        averiado: "false",
      },
      {
        id: 16,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 15,
        lpg_capacity: 15,
        averiado: "false",
      },
      {
        id: 17,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 15,
        lpg_capacity: 15,
        averiado: "false",
      },
      {
        id: 18,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 15,
        lpg_capacity: 15,
        averiado: "false",
      },
      {
        id: 19,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 25,
        lpg_capacity: 25,
        averiado: "false",
      },
      {
        id: 20,
        x: 12,
        y: 8,
        fuel_meter: 25,
        fuel_tank_capacity: 25,
        lpg_meter: 25,
        lpg_capacity: 25,
        averiado: "false",
      },
    ],
  });

  async function tablaCamiones() {
    console.log("Se llamó al API de Camiones");
    try {
      const result = await fetch(
        {},
        {
          uri: "http://localhost:8080/flota/list", //CAMBIAR POR LA NUEVA API
          method: "get",
        }
      );

      if (result.status === 200) {
        console.log("Se trajo la data de Camiones");
        //AQUI SE HACE TODO
        //Limpiamos
        let auxTrucks = [];

        //Por cada Camion
        for (
          let indice = 0;
          result.data.payload.trucks &&
          indice < result.data.payload.trucks.length;
          indice++
        ) {
          let auxiliar = {
            id: result.data.payload.trucks[indice].id,
            x: result.data.payload.trucks[indice].x,
            y: result.data.payload.trucks[indice].y,
            fuel_meter: result.data.payload.trucks[indice].fuel_meter,
            fuel_tank_capacity:
              result.data.payload.trucks[indice].fuel_tank_capacity,
            lpg_meter: result.data.payload.trucks[indice].lpg_meter,
            lpg_capacity: result.data.payload.trucks[indice].lpg_capacity,
            averiado: result.data.payload.trucks[indice].averiado,
          };

          auxTrucks.push(auxiliar);
        }

        setDatatable({ ...datatable, rows: auxTrucks });
      } else {
        console.log("No se pudo obtener la data");
      }
    } catch (e) {
      console.error(e);
    }
  }

  useEffect(() => {
    //tablaCamiones();
  }, []);

  return (
    <>
      <Navbar
        className="flex-row justify-content-between pt-3 pb-3 navbar3 sticky"
        sticky="top"
      >
        <Navbar.Brand className="col-11 px-5 navbar3__tittle" href="">
          CAMIONES
        </Navbar.Brand>
      </Navbar>

      <div className="separator" />

      <div className="container-fluid mt-5 pedidos-container">
        <MDBDataTableV5
          hover
          entriesOptions={[20, 20, 25]}
          entries={20}
          pagesAmount={20}
          data={datatable}
          searchTop
          searchBottom={false}
        />
      </div>
    </>
  );
}

export default Camiones;
