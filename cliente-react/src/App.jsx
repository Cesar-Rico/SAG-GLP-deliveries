import React from "react";
import Layout from "./components/Layout";
import Monitoreo from "./modules/Monitoreo";
import Simulacion from "./modules/Simulacion";
import Simulacion3Dias from "./modules/Simulacion3Dias";
import SimulacionColapso from "./modules/SimulacionColapso";
import Pedidos from "./modules/Pedidos";
import Configuracion from "./modules/Configuracion";
import Bloqueos from "./modules/Bloqueos";
import Camiones from "./modules/Camiones";
import NotFound from "./modules/NotFound";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import UserProvider from "./modules/UserContext";
import StorageProvider from "./modules/StorageContext";

export default function App() {
  return (
    <StorageProvider>
      {/* SE ENCARGA DE GUARDAR DATA EN EL LOCAL STORAGE */}
      <UserProvider>
        {/* SE ENCARGA DE GUARDAR DATA TEMPORAL (recargar pagina, elimina la data) */}
        <BrowserRouter>
          <Layout>
            <Switch>
              <Route exact path="/" component={Monitoreo}></Route>
              <Route exact path="/simulacion" component={Simulacion}></Route>
              <Route
                exact
                path="/simulacion3Dias"
                component={Simulacion3Dias}
              ></Route>
              <Route
                exact
                path="/simulacionColapso"
                component={SimulacionColapso}
              ></Route>
              <Route exact path="/pedidos" component={Pedidos}></Route>
              <Route
                exact
                path="/configuracion"
                component={Configuracion}
              ></Route>
              <Route exact path="/bloqueos" component={Bloqueos}></Route>
              <Route exact path="/flota" component={Camiones}></Route>
              <Route component={NotFound}></Route>
            </Switch>
          </Layout>
        </BrowserRouter>
      </UserProvider>
    </StorageProvider>
  );
}
