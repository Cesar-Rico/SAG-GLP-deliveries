/* eslint-disable */
import React, { useState } from "react";

export const UserContext = React.createContext({});

const initialUser = {
  startTimeSimulation: null,
  monitoreoData: false,
  sim3Velocity: 0,
  gridsize: 12, //ANTES: 48
  cellbordercolor: "rgba(255, 255, 255, 0.678)",
  routecolor: "rgba(250, 220, 100, 0.950)",

  mapaX: 70,
  mapaY: 50,
  mapaOn: true,

  centralX: 12,
  centralY: 8,

  pR1X: 42,
  pR1Y: 42,
  pR2X: 63,
  pR2Y: 3,

  batchTime: 0.2, // 12 minutos

  fileBloqueos: null,

  contaAveria: 0,
};

const UserProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const u = initialUser;
    return u;
  });

  const value = {
    user: user,
    changeUser: (value) => {
      setUser({ ...user, ...value });
    },
  };

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};

export default UserProvider;
