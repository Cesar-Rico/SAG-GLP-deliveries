/* eslint-disable */
import React, { useContext } from "react";
import { UserContext } from "../modules/UserContext";
import { StorageContext } from "../modules/StorageContext";

function Mapa(props) {
  const { storage, changeStorage } = useContext(StorageContext);
  const { user, changeUser } = useContext(UserContext);

  return (
    <>
      {Array.from({ length: props.y }).map((_, j) =>
        Array.from({ length: props.x }).map((_, i) => (
          <div key={i.toString() + "," + (props.y - 1 - j).toString()}>
            {i === props.x - 1 && j === 0 ? (
              <div
                className={`cell cell--top-right d-flex align-items-end ${
                  storage.cords ? "black" : "white"
                }`}
                id={i + "," + (props.y - 1 - j)}
              >
                {i},{props.y - 1 - j}
              </div>
            ) : i === props.x - 1 ? (
              <div
                className={`cell cell--right d-flex align-items-end ${
                  storage.cords ? "black" : "white"
                }`}
                id={i + "," + (props.y - 1 - j)}
              >
                {i},{props.y - 1 - j}
              </div>
            ) : j === 0 ? (
              <div
                className={`cell cell--top d-flex align-items-end ${
                  storage.cords ? "black" : "white"
                }`}
                id={i + "," + (props.y - 1 - j)}
              >
                {i},{props.y - 1 - j}
              </div>
            ) : (
              <div
                className={`cell d-flex align-items-end ${
                  storage.cords ? "black" : "white"
                }`}
                id={i + "," + (props.y - 1 - j)}
              >
                {i},{props.y - 1 - j}
              </div>
            )}
          </div>
        ))
      )}
    </>
  );
}

export default Mapa;
