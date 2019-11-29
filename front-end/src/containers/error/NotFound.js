import React from "react";
import { Link } from "react-router-dom";
import "./NotFound.css";

export default () =>
  <div class="NotFound">
    <h3>Wyskoczył błąd 404</h3>
    <Link class="navbar-brand" to="/">Przejdz do storny głównej</Link>
  </div>;
