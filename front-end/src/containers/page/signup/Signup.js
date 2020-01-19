import React, { Component, Fragment } from "react";
import { Auth, API } from "aws-amplify";
import "./Signup.css";
import { Link } from "react-router-dom";
export default class Signup extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          email: "",
          password: "",
          recruiterPassword: "",
          confirmationCode: "",
          newUser: null,
          profilType: "",
          firstName: "",
          errors: []
        };
    }

    setFirstName = async (evt) => {
        this.setState({
            firstName : evt.target.value
        });
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
    setRecruiterPassword = async (evt) => {
        this.setState({
            recruiterPassword : evt.target.value
        });
    }
    setProfiType = async (evt) => {
        this.setState({
            profilType : parseInt(evt.target.value) === 1 ? "user" : "recruiter"
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

    handleConfirmationSubmit = async event => {
        
        event.preventDefault();
        let errors = [];

        let confirmationCodeLen = this.state.confirmationCode.length;

        if (confirmationCodeLen  === 0)
            errors.push("Pole nie może być puste");
        
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
            Auth.signIn(
                this.state.email, 
                this.state.password
            ).then(
                user => {
                    this.props.userAuthenticatedObject(user);
                    this.props.userHasAuthenticated(true);

                    if(this.state.profilType === "recruiter")
                      this.props.history.push("/recruiter_panel");
                    else
                      this.props.history.push("/user_panel");
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

    checkPasswordRecruiter(obj){
        return API.post("api", `/recruiter_check_password`, {
            body: obj
        })
    }

    handleSubmit = async event => {
        
        event.preventDefault();
        let errors = [];
        let firstNameLen = this.state.firstName.length;
        let passwordLen = this.state.password.length;
        let profilTypeLen = this.state.profilType.length;
        if( firstNameLen === 0 || firstNameLen > 20 )
            errors.push("Imię musi posiadać od 1 do 20 znaków.");

        if( passwordLen < 6 || passwordLen > 25 )
            errors.push("Hasło musi posiadać od 6 do 25 znaków.");

        if (profilTypeLen === 0)
            errors.push("Musisz wybrać rodzaj profilu.");

        // if (!(/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[A-Za-z]+$/.test(this.state.email)))
        //     errors.push("Musisz wpisać prawidłowy adres mail.");
        
        if(this.state.profilType === "recruiter")
        {
                try {
                    const res = await this.checkPasswordRecruiter({
                        name: this.state.recruiterPassword
                    })
                    
                    if(res !== "ok"){
                        errors.push("Podałeś złe hasło dla konta rekrutera");
                    }
                        
                }
                catch (e) {
                    errors.push("Wystąpił błąd w sprawdzaniu hasła rekrtera");
                }
        }


        if(errors.length > 0)
        {
            this.setState({
                errors : errors
            });
            return;
        }
        try {
            const newUser = await Auth.signUp({
                username: this.state.email,
                password: this.state.password,
                attributes: {
                    name: this.state.firstName,      
                    profile : this.state.profilType
                }
            });
            this.setState({
                newUser
            });
        }
        catch (e) {
            let errors = [];

            errors.push(e.message);

            if(errors.length > 0)
            {
                this.setState({
                    errors : errors
                });
                return;
            }
        }
    }

    renderConfirmationForm() {
        return (
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-5 signup-container">
                        <h2 className="text-center">Rejestracja</h2>
                        <form onSubmit={this.handleConfirmationSubmit}>
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

    renderForm() {
        return (
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-5 signup-container">
                        <h2 className="text-center">Rejestracja</h2>
                        <form onSubmit={this.handleSubmit}>
                                <div className="form-group">
                                    <label htmlFor="firstname-input" className="text-uppercase float-left">Imię</label>
                                    <input type="text" className="form-control firstname-input" placeholder="Wprowadź swoje imię" onChange={this.setFirstName} />
                                </div>
                                
                                <div className="form-group">
                                    <label htmlFor="email-input" className="text-uppercase float-left">Adres e-mail</label>
                                    <input type="email" className="form-control email-input" placeholder="Wprowadź swój adres e-mail" onChange={this.setEmail}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="password-input" className="text-uppercase float-left">Hasło</label>
                                    <input type="password" className="form-control password-input" placeholder="Wprowadź swoje hasło" onChange={this.setPassword}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="select-profil" className="text-uppercase float-left">Wybierz typ profilu</label>
                                    <select className="custom-select select-profil" onChange={this.setProfiType}>
                                        <option defaultValue>Wybierz profil</option>
                                        <option value="1" >Użytkownik</option>
                                        <option value="2">Rekruter</option>
                                    </select>
                                </div>
                                {this.state.profilType === "recruiter" && 
                                <Fragment>
                                    <div className="form-group">
                                        <label htmlFor="password-recruiter-input" className="text-uppercase float-left">Hasło do posiadania konta typu rekruter</label>
                                        <input type="password" className="form-control password-recruiter-input" placeholder="Wprowadź swoje hasło" onChange={this.setRecruiterPassword}/>
                                    </div>
                                </Fragment>}
                                <div className="form-group">
                                    <button type="submit" className="btn btn-success btn-signup">Zarejestruj się</button>
                                </div>
                        </form>
                        <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                                {this.showErrors()}
                        </div>
                        <div className="text-left activate-section mb-3">
                            Jeżeli posiadasz już konto, a jeszcze go nie aktywowałeś. Zrób to klikając w ten link 
                            <Link to="/verify_email" class="btn btn-outline-primary btn-sm ml-2">Aktywuj konto</Link>
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
            <div className="signup-wrapper">
                {this.state.newUser === null ? this.renderForm()  : this.renderConfirmationForm()}
            </div>
        );
    }

}
