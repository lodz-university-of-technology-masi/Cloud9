import React, { Component } from "react";
import "./Main.css";

export default class Main extends Component {
    
    constructor(props) {
        super(props);
        console.log(this.props.user.attributes.profile);
    }

    async componentDidMount() {
        if (!this.props.isAuthenticated) {
          return;
        }
    }

    render() {
        return(
            <div className="main">
                jupi zalogowany
            </div>
        );
    }
}