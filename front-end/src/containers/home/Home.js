import React, { Component } from "react";
import { Link } from "react-router-dom";
import "./Home.css";

export default class Home extends Component {
    constructor(props) {
        super(props);
        
    }
    
    renderHome() {
        return(<div>zalogowany</div>);
    }

    renderMain() {
        return(
            <div class="main">
                <Link to="/signin" className="btn btn-info btn-lg">
                    Zaloguj się
                </Link>
                <Link to="/signup" className="btn btn-success btn-lg">
                    Zarejestruj się 
                </Link>
            </div>
        );
    }

    render() {
        return(
            <div class="home">
                 {this.props.isAuthenticated ? this.renderHome() : this.renderMain()}
            </div>
        );
    }
}