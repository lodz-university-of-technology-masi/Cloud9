import React, { Component, Fragment } from "react"
import "../../../libs/style/card.css"
import "./AddForm.css"
//import { API } from "aws-amplify";

export default class Main extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
            progress: 0,
            name: "",
            description: "",
            time: "",
            errors: []
        };
    }

    async componentDidMount() {
        if (!this.props.isAuthenticated) {
          return;
        }
        // await cognitoUser()
    }

    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });

        return error;
    }

    setName = async (evt) => {
        this.setState({
            name : evt.target.value
        });
    }

    setDescription = async (evt) => {
        this.setState({
            description : evt.target.value
        });
    }

    setTime = async (evt) => {
        this.setState({
            time : evt.target.value
        });
    }

    handleBaseFormSubmit = async event => {
        event.preventDefault()
        let errors = []
        let nameLen = this.state.name.length
        let descriptionLen = this.state.description.length
        let timeStringLen = this.state.time.length
        if( nameLen  === 0 )
            errors.push("Musisz podać nazwę testu.")

        if( nameLen > 50 )
            errors.push("Nazwa testu nie może przekraczać 50 znaków")

        if( descriptionLen  === 0 )
            errors.push("Musisz podać opis testu")

        if( descriptionLen > 250 )
            errors.push("Opis testu nie może przekraczać 250 znaków")
        
        let timeNumber = parseInt(this.state.time);

        if( timeStringLen === 0 )
            errors.push("Czas wykonywania testu nie może być pusty")

        if( timeNumber < 0 &&  !isNaN(timeNumber)) 
            errors.push("Czas musi być wartością dodatnią")
        else
            this.setState({
                time : timeNumber
            });
        
        if(errors.length > 0)
        {
            console.log(errors)
            this.setState({
                errors : errors
            });
            return;
        }
        
        this.setState({
            progress : 1
        });
    }

    handleCanel = async event => {
        this.setState({
            progress: 0,
            name: "",
            description: "",
            time: "",
            errors: []
        });
        this.props.history.push("/recruiter_panel");
      }

    renderBase() {
        return (
            <div>
                <form onSubmit={this.handleBaseFormSubmit}>
                    <div className="form-group">
                        <label htmlFor="name-input" className="text-uppercase float-left">Nazwa testu</label>
                        <input type="text" className="form-control name-input" placeholder="Wprowadź tytuł swojego testu" onChange={this.setName}/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="description-input" className="text-uppercase float-left">Opis testu</label>
                        <textarea className="form-control description-input"  rows="3" placeholder="Opisz swój tekst w celu ułatwienia dalszej pracy :)" onChange={this.setDescription}></textarea>
                    </div>
                    <div className="form-group mb-1">
                        <label htmlFor="time-input" className="text-uppercase float-left">Czas wykonywania testu w minutach</label>
                        <input type="number" className="form-control time-input" min="1" onChange={this.setTime}/>
                    </div>
                    <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                        {this.showErrors()}
                    </div>
                    <div className="form-group float-right">
                        <button type="submit" className="btn btn-outline-success btn-next-step">Przejdź do kolejnego kroku</button>
                    </div>
                </form>
            </div>
        )
    }

    renderUser() {
        return (
            <div>
                
            </div>
        )
    }

    render() {
        let state = this.state.progress
        let baseRender = this.renderBase()
        let userRender = this.renderUser()
        
        return(
            <div className="recruiter-addform container">
                <div className="row">
                    <div className="col-lg-12 col-md-12">
                        <div className="card">
                            <div className="card-header card-header-success">
                                <h4 className="card-title">
                                    {(function() {
                                        switch(state) {
                                        case 0:
                                            return <Fragment>Podstawowe informacje</Fragment>;
                                        case 1:
                                            return <Fragment>Dodaj użytkowników</Fragment>;
                                        case 2:
                                            return <Fragment>Dodaj pytania</Fragment>;
                                        default:
                                            return <Fragment></Fragment>
                                        }
                                    })()}
                                    &nbsp;- Tworzenie nowego testu
                                </h4>
                                <div className="test-form-button text-right">
                                    <div className="btn btn-outline-light btn-sm mr-2" onClick={this.handleCanel}>
                                        Anuluj
                                    </div>
                                </div>
                            </div>
                            <div className="card-body">
                            {(function() {
                                switch(state) {
                                case 0:
                                    return baseRender;
                                case 1:
                                    return userRender;
                                case 2:
                                    return <Fragment></Fragment>;
                                default:
                                    return <Fragment></Fragment>
                                }
                            })()}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}