/* eslint-disable */
import React from "react";
import Navbar2 from "./Navbar.jsx";

function Layout(props) {
  return (
    <React.Fragment>
      <Navbar2 />
      {props.children}
    </React.Fragment>
  );
}

export default Layout;
