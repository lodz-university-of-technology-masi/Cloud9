import React, { Component } from "react";
import { Auth } from "aws-amplify";
import "./Signin.css";

export default class Signin extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          isLoading: false,
          email: "",
          password: "",
          profile: NaN
        };
    }

    validateForm = async ()=> {


    }

    handleSubmit = async (event) => {
        event.preventDefault();
        const { name, value } = event.target;
        console.log(name);
        console.log(value);
        //await Auth.signIn(this.state.email, this.state.password);
        //this.props.userHasAuthenticated(true);

    }

    render() {
        return (
            <div className="signin">
                <div className="container">
                    <div className="row justify-content-center align-items-center">
                        <div className="col-md-4 login-container justify-content-center">
                            <h2 className="text-center">Logowanie</h2>
                            <form className="login-form" onSubmit={this.handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="email-input" className="text-uppercase float-left">Adres e-mail</label>
                                    <input type="email" className="form-control email-input" placeholder="Wprowadź swój adres e-mail"/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password-input" className="text-uppercase float-left">Hasło</label>
                                    <input type="password" className="form-control password-input" placeholder="Wprowadź swoje hasło"/>
                                </div>
                                <div className="form-check">
                                    <button type="submit" className="btn btn-success btn-login">Zaloguj się</button>
                                </div>
                            </form>
                            <div className="copy-text"><h6>Created with by <b>Cloud9</b></h6></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
