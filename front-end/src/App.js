import React, { Component, Fragment } from "react";
import { Auth } from "aws-amplify";
import { Link, withRouter } from "react-router-dom";
import Routes from "./Routes";
import "./App.css";

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isAuthenticated: false,//uzytkownik nie jest zalogowany
      isAuthenticating: true
    };
  }

  render() {
    const childProps = {
      isAuthenticated: this.state.isAuthenticated,
      userHasAuthenticated: this.userHasAuthenticated
    };

    return(
      <div className="App">
        <nav class="navbar navbar-dark bg-dark">
          <div class="container">
            <Link className="navbar-brand" to="/"><b>Cloud9</b></Link>
          </div>
        </nav>

        <Routes childProps={childProps} />
      </div>
    );
  }
  
}
export default App;