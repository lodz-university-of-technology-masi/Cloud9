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
      isAuthenticating: true, // zmienna pomocnicza
      user: NaN // profil uzytkownika, ktory sie zaloguje
    };
  }

  async componentDidMount() {
    await Auth.currentAuthenticatedUser().
    then(
      user => this.userHasAuthenticated(user)
    )
    .catch(
      this.setState({ isAuthenticating: false })
    );
  }

  userHasAuthenticated = authenticated => {
    this.setState({ 
      isAuthenticated: true,
      user: authenticated
     });
  }

  handleLogout = async event => {
    await Auth.signOut();
    this.userHasAuthenticated(false);
    this.props.history.push("/");
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