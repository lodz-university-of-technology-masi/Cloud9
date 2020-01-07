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
      user: false,
      navbarActive: false
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
        <nav className="navbar navbar-expand-lg navbar-light ">
          <div className="container">
          <Link className="navbar-brand" to="/"><b>Cloud9</b></Link>
          <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarText">
          {this.state.isAuthenticated? 
          <Fragment>
              
              <ul className="navbar-nav ml-auto">
                <li className="nav-item">
                  <div className="navbar-brand" onClick={this.handleLogout}><b>Wyloguj</b></div>
                </li>
              </ul>
            </Fragment>
            :
            <Fragment>
              <ul className="navbar-nav ml-auto">
                <li className="nav-item active">
                  <Link className="navbar-brand" to="/signup"><b>Rejestracja</b></Link>
                </li>
                <li className="nav-item active">
                  <Link className="navbar-brand" to="/signin"><b>Logowanie</b></Link>
                </li>
              </ul>
            </Fragment>}
          </div>
        </div>
      </nav>
        <Routes childProps={childProps} />
      </div>
      
    );
  }
}

export default withRouter(App);