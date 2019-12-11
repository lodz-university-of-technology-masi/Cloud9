import React, { Component } from "react";
import "./Main.css";

export default class Main extends Component {
    
    async componentDidMount() {
        if (!this.props.isAuthenticated) {
          return;
        }
    }

    render() {
        return(
            <div className="main">
                jupi zalogowany uzytkownik
            </div>
        );
    }
}