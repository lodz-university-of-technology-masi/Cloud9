import React, { Component } from "react";
import { Auth } from "aws-amplify";
import "./Signup.css";

export default class Signup extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          email: "",
          password: "",
          confirmationCode: "",
          newUser: null,
          profilType: "user",
          firstName: "",
          tmpProfilType: NaN,
        };
    }

    setFirstName = async (evt) => {
        this.setState({
            firstName : evt.target.value
        });
    }

    setEmail = async (evt) => {
        this.setState({
            email : evt.target.value
        });
    }

    setPassword = async (evt) => {
        this.setState({
            password : evt.target.value
        });
    }

    setTmpProfiType = async (evt) => {
        this.setState({
            tmpProfilType : evt.target.value
        });
    }

    setConfirmationCode = async (evt) => {
        this.setState({
            confirmationCode : evt.target.value
        });
    }

    handleSubmit = async event => {
        event.preventDefault();
    
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
        } catch (e) {
          alert(e.message);
        }
    
    }

    handleConfirmationSubmit = async event => {
        event.preventDefault();

        try {
          await Auth.confirmSignUp(this.state.email, this.state.confirmationCode);
          await Auth.signIn(this.state.email, this.state.password);
    
          this.props.userHasAuthenticated(true);
          this.props.history.push("/");
        } catch (e) {
          alert(e);
        }
      }



    renderConfirmationForm() {
        return (
            <div className="signup">
                <div className="container">
                    <div className="row justify-content-center align-items-center">
                        <div className="col-md-4 signup-container justify-content-center">
                            <h2 className="text-center">Sprawdź swój mail</h2>
                            <form className="signup-form" onSubmit={this.handleConfirmationSubmit}>
                                <div className="form-group">
                                    <label htmlFor="code-input" className="text-uppercase float-left">Kod potwierdzenia</label>
                                    <input type="text" className="form-control code-input" placeholder="Wprowadź swój kod z maila" onChange={this.setConfirmationCode} />
                                </div>
                                <div className="form-check">
                                    <button type="submit" className="btn btn-success btn-signup">Dokończ rejestrację</button>
                                </div>
                            </form>
                            <div className="copy-text"><h6>Created with by <b>Cloud9</b></h6></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
    renderForm() {
        return (
            <div className="signup">
                <div className="container">
                    <div className="row justify-content-center align-items-center">
                        <div className="col-md-4 signup-container justify-content-center">
                            <h2 className="text-center">Rejestracja</h2>
                            <form className="signup-form" onSubmit={this.handleSubmit}>
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
                                    <select className="custom-select select-profil" onChange={this.setTmpProfiType}>
                                        <option selected>Wybierz profil</option>
                                        <option value="1" >Użytkownik</option>
                                        <option value="2">Rekruter</option>
                                    </select>
                                </div>
                                
                                <div className="form-check">
                                    <button type="submit" className="btn btn-success btn-signup">Zarejestruj się</button>
                                </div>
                            </form>
                            <div className="copy-text"><h6>Created with by <b>Cloud9</b></h6></div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }



    render() {
        return (
            <div className="Signup">
                {
                    this.state.newUser === null ? this.renderForm()  : this.renderConfirmationForm()
                }
            </div>
        );
    }
}