/* eslint-disable */
import React, { useState } from "react";

export const StorageContext = React.createContext({});

const initialStorage = {
  files: [],
  tableros: false,
  cords: false,

  mapaX: 71,
  mapaY: 51,
  mapaOn: true,

  centralX: 12,
  centralY: 8,

  pR1X: 42,
  pR1Y: 42,
  pR2X: 63,
  pR2Y: 3,
};

const StorageProvider = ({ children }) => {
  const [storage, setStorage] = useState(() => {
    const u = window.localStorage.getItem("storage")
      ? JSON.parse(window.localStorage.getItem("storage"))
      : initialStorage;
    return u;
  });

  const value = {
    storage: storage,
    changeStorage: (value) => {
      setStorage({ ...storage, ...value });
      window.localStorage.setItem("storage", JSON.stringify(value));
    },
  };

  return (
    <StorageContext.Provider value={value}>{children}</StorageContext.Provider>
  );
};

export default StorageProvider;
