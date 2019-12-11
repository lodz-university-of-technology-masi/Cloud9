import React, { Component, Fragment } from "react";
import { Auth } from "aws-amplify";
import { Link, withRouter } from "react-router-dom";
import Routes from "./Routes";
import "./App.css";

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isAuthenticated: false,
      isAuthenticating: true,
      user: false
    };
  }

  async componentDidMount() {
    try {
      await Auth.currentSession();
      this.userHasAuthenticated(true);
      await Auth.currentAuthenticatedUser()
      .then(
        user => {
          this.userAuthenticatedObject(user);
        }
      )
      .catch(
        err => {
          if (err !== "not authenticated")
            console.log(err);
          this.setState({user: false });
        }
      );
    }
    catch(err) {
      if (err !== 'No current user')
        console.log(err);
    }

    this.setState({ isAuthenticating: false });
  }

  userHasAuthenticated = authenticated => {
    this.setState({ isAuthenticated: authenticated });
  }
  userAuthenticatedObject = user => {
    this.setState({ user: user });
  }

  handleLogout = async event => {
    await Auth.signOut();
    this.userHasAuthenticated(false);
    this.userAuthenticatedObject(false);
    this.props.history.push("/");
  }

  render() {
    const childProps = {
      isAuthenticated: this.state.isAuthenticated,
      userHasAuthenticated: this.userHasAuthenticated,
      user: this.state.user,
      userAuthenticatedObject: this.userAuthenticatedObject
    };

    return (
      !this.state.isAuthenticating &&
      <div className="App ">
        <nav className="navbar navbar-dark bg-dark">
          <div className="container">
            <Link className="navbar-brand" to="/"><b>Cloud9</b></Link>
            {this.state.isAuthenticated
                ? <Fragment>
                    <div className="nav-item dropdown ml-auto">
                      <div className="nav-link dropdown-toggle" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Ustawienia</div>
                      <div className="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                        <div className="dropdown-item" onClick={this.handleLogout}>Wyloguj</div>
                      </div>
                    </div>
                  </Fragment>
                : <Fragment>
                  </Fragment>
              }
          </div>
              
        </nav>
        <Routes childProps={childProps} />
      </div>
    );
  }
}

export default withRouter(App);