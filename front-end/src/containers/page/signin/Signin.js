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
          errors: []
        };
    }

    setEmail = async (evt) => {
        this.setState({
            email : evt.target.value.toLowerCase()
        });
    }

    setPassword = async (evt) => {
        this.setState({
            password : evt.target.value
        });
    }
     
    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });

        return error;
    }

    handleFormSubmit = async event => {
        event.preventDefault();
        let errors = [];
        let passwordLen = this.state.password.length;

        // if (!(/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[A-Za-z]+$/.test(this.state.email)))
        //     errors.push("Musisz wpisać prawidłowy adres mail.");

        if( passwordLen < 6 || passwordLen > 25 )
            errors.push("Hasło musi posiadać od 6 do 25 znaków.");
        
        if(errors.length > 0)
        {
            this.setState({
                errors : errors
            });
            return;
        }

        await Auth.signIn(
            this.state.email, 
            this.state.password
        ).then(
            user => {
              this.props.userAuthenticatedObject(user);
              this.props.userHasAuthenticated(true);
            //   if(user.attributes.profile === "recruiter")
            //     this.props.history.push("/recruiter_panel");
            //   else
            //     this.props.history.push("/user_panel");
            }
        )
        .catch(
            err => {
                let errors = [];
                errors.push(err.message);

                if(errors.length > 0)
                {
                    this.setState({
                        errors : errors
                    });
                    return;
                }
            }
        );

    
        
       
    }

    render() {
        return(
            <div className="signin-wrapper">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-md-5 signin-container">
                            <h2 className="text-center">Logowanie</h2>
                            <form onSubmit={this.handleFormSubmit}>
                                <div className="form-group">
                                    <label htmlFor="email-input" className="text-uppercase float-left">Adres e-mail</label>
                                    <input type="email" className="form-control email-input" placeholder="Wprowadź swój adres e-mail" onChange={this.setEmail}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password-input" className="text-uppercase float-left">Hasło</label>
                                    <input type="password" className="form-control password-input" placeholder="Wprowadź swoje hasło" onChange={this.setPassword}/>
                                </div>
                                <div className="form-group">
                                    <button type="submit" className="btn btn-success btn-signin">Zaloguj się</button>
                                </div>
                            </form>
                            <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                                {this.showErrors()}
                            </div>
                            <div className="copy-text">
                                <h6>Created by <b>Cloud9</b></h6>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
