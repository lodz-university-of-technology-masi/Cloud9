import React, { Component } from "react"
import { Link } from "react-router-dom";
import { Auth } from "aws-amplify"
import "./verifyEmail.css"

export default class verifyEmail extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          email: "",
          confirmationCode: "",
          errors: [],
          actionStaus: false
        };
    }

    setEmail = async (evt) => {
        this.setState({
            email : evt.target.value.toLowerCase()
        });
    }

    setConfirmationCode = async (evt) => {
        this.setState({
            confirmationCode : evt.target.value
        });
    }

    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });

        return error;
    }

    handleSubmit = async event => {
        event.preventDefault();

        let errors = [];

        let emailLen = this.state.email.length;
        let confirmationCodeLen = this.state.confirmationCode.length;

        if (emailLen  === 0)
            errors.push("Pole email nie może być puste");

        if (confirmationCodeLen  === 0)
            errors.push("Pole kodu nie może być puste");

        
        
        if(errors.length > 0)
        {
            this.setState({
                errors : errors
            });
            return;
        }

        await Auth.confirmSignUp(
            this.state.email, 
            this.state.confirmationCode
        ).then( () => {
            this.setState({
                actionStaus : true
            });
        })
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
    renderForm(){
        return (
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-5 verifyemail-container">
                        <h2 className="text-center">Aktywacja konta</h2>
                        <form onSubmit={this.handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="email-input" className="text-uppercase float-left">Adres e-mail</label>
                                    <input type="email" className="form-control email-input" placeholder="Wprowadź swój adres e-mail" onChange={this.setEmail}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="code-input" className="text-uppercase float-left">Kod potwierdzenia</label>
                                    <input type="text" className="form-control code-input" placeholder="Wprowadź swój kod z maila" onChange={this.setConfirmationCode} />
                                </div>
                                <div className="form-group">
                                    <button type="submit" className="btn btn-success btn-signup">Aktywuj konto</button>
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
        );
    }

    renderInformation(){
        return (
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-5 verifyemail-container">
                        <h2 className="text-center">Aktywacja konta</h2>
                        <h5 className="text-center mb-2">Twoje konto zostało pomyślnie aktywowane.</h5>
                        <div className="text-center mt-4 mb-4">
                            <Link to="/signin" className="btn btn-success btn-md">
                                Przejdź tutaj, aby się zalogować
                            </Link>
                        </div>
                        <div className="copy-text">
                            <h6>Created by <b>Cloud9</b></h6>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    render() {
        return (
            <div className="verifyemail-wrapper">
                {this.state.actionStaus === false ? this.renderForm()  : this.renderInformation()}
            </div>
        );
    }
}