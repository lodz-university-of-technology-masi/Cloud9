import React, { Component } from "react";
import { Link } from "react-router-dom";
import "./Home.css";

export default class Home extends Component {
    async componentDidMount() {
      if(this.props.user !== false){
        if(this.props.user.attributes.profile === "recruiter")
            this.props.history.push("/recruiter_panel");
        else
            this.props.history.push("/user_panel");
      }
        
      if (!this.props.isAuthenticated) {
        return;
      }
    }
  
   
    
    render() {
      return (
        <div className="Home">
            <div className="container">
                <div className="row slide-margin">
                <div className="col-sm-12 col-12 col-md-12 col-lg-6">
                    <h2 className="text-left">Witaj w Cloud9</h2>
                    <h5 className="text-left">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                    </h5>
                    <div className="btn-action text-center mt-5">
                        <Link to="/signin" className="btn btn-outline-primary btn-lg mr-2">
                        Zaloguj się
                        </Link>
                        <Link to="/signup" className="btn btn-outline-success btn-lg">
                        Zarejestruj się 
                        </Link>
                    </div>
                </div>
                </div>
            </div>
        </div>
      );
    }
  }