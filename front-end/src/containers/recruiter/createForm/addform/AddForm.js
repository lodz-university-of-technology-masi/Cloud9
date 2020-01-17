import React, { Component, Fragment } from "react"
import {Redirect} from "react-router-dom";
import { API } from "aws-amplify";
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./AddForm.css"

export default class Main extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            progress: 0,
            name: "",
            description: "",
            time: "",
            errors: [],
            redirectForm: ""
        }
    }

    async componentDidMount() {
        if (!this.props.isAuthenticated) 
          return;
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

    createForm(form) {
        return API.post("api", "/forms", {
          body: form
        })
    }

    handleBaseFormSubmit = async event => {
        event.preventDefault()
        this.setState({
            isLoading: true
        });

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
            this.setState({
                isLoading: false,
                errors : errors
            });
            return;
        }

        await this.createForm({
            name: this.state.name,
            description: this.state.description,
            time: this.state.time,
            recruiter: this.props.user.attributes.sub

        })
        .then(async (response) => {
            if(response.id)
                this.setState({
                    isLoading: false,
                    redirectForm: response.id
                })
            
        })
        .catch(err => {
            this.setState({
                isLoading: false,
                errors: ['Coś poszło nie tak z usuwaniem testu']
            })
        })  
    }

    handleCanel = async event => {
        this.setState({
            isLoading: true
        });

        this.props.history.push("/recruiter_panel");
      }

    renderBase() {
        return (
            <div>
                <form onSubmit={this.handleBaseFormSubmit}>
                    <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                        {this.showErrors()}
                    </div>
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
                    
                    <div className="form-group float-right">
                        <button type="submit" className="btn btn-outline-success btn-next-step">Przejdź do kolejnego kroku</button>
                    </div>
                </form>
            </div>
        )
    }

    render() {
        if(this.state.redirectForm.length)
            return (<Redirect to={`/recruiter_panel/forms/${this.state.redirectForm}/users`} />)
        return(
            <div className="recruiter-addform container">
                 {this.state.isLoading ? 
                    <Fragment>
                        <div className="loading mt-5">
                            <ul className="efect">
                                <li></li>
                                <li></li>
                                <li></li>
                            </ul>
                        </div>
                    </Fragment>
                    :
                    <Fragment>
                        <div className="row">
                            <div className="col-lg-12 col-md-12">
                                <div className="card">
                                    <div className="card-header card-header-success">
                                        <h4 className="card-title">
                                            Tworzenie nowego testu
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={this.handleCanel}>
                                                Anuluj
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        {this.renderBase()}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}