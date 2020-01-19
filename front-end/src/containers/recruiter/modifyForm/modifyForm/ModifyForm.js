import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./ModifyForm.css"
import { API } from "aws-amplify";

export default class ModifyForm extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          form: null,
          isLoading: false,
          errorLoading: false,
          errors: [],
          error: ""
        };
    }
    
    async componentDidMount() {
        if (!this.props.isAuthenticated) 
          return;

        if(!this.props.match.params.id)
          this.props.history.push("/recruiter_panel")
      
        this.setState({
            isLoading: true
        });
  
      try {
          const form = await this.getForm()
          if(form.recruiterId !== this.props.user.attributes.sub){
              this.setState({
                  isLoading: false,
                  notAuthorized: true
              })
              return
          }

          this.setState({
              isLoading: false,
              form: form,
          })
      } 
      catch (e) {
          if(e.response.data === "not found")
              this.setState({
                  isLoading: false,
                  notFound: true
              })
      }
    }

    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });

        return error;
    }

    deleteFormFun = () => {
        console.log("test")
    }

    handleDelete = async event => {
        event.preventDefault();
    
        const confirmed = window.confirm(
          "Jesteś pewny, że chcesz usunąć ten test?"
        );
    
        if (!confirmed) {
          return;
        }
    
        this.setState({ isLoading: true });
    
        try {
          await this.deleteForm();
          this.props.history.push(`/recruiter_panel/`)
        } catch (e) {
            this.setState({
                isLoading: false,
                error: "Wystąpił błąd w usuwaniu testu"
            })
        }
    }

    deleteForm(){
        return API.del("api", `forms/${this.state.form.id}`)
    }

    getForm(){
        return API.get("api", `forms/${this.props.match.params.id}`)
    }

    renderBase() {
        return (
            <div className="row">
                <div className="col-12 text-left">
                    <h4><b>Podstawowe informacje</b></h4>
                    <h5>
                        <b>Opis:
                        {this.state.form.description == null ? 
                        <Fragment>
                            Brak opisu
                        </Fragment>
                        :
                        <Fragment>
                            {this.state.form.description}
                        </Fragment>
                        }</b>
                    </h5>
                    <h5>
                        <b>Data utworzenia:
                        {this.state.form.creationDate == null ? 
                        <Fragment>
                            Brak utworzenia daty
                        </Fragment>
                        :
                        <Fragment>
                            {this.state.form.creationDate}
                        </Fragment>
                        }</b>
                    </h5>
                    <h5>
                        <b>Liczba osób uczestniczących w teście:
                        {this.state.form.creationDate == null ? 
                        <Fragment>
                            Brak osób
                        </Fragment>
                        :
                        <Fragment>
                            {this.state.form.users.length} osób
                        </Fragment>
                        }
                        </b>
                        </h5>
                    <h5><b>Czas wykonania testu:</b></h5>
                </div>
            </div>
        );
    }
    
    render() {
        if(this.state.notFound)
            return(
                <div className="text-center mt-5">
                    <h4>Przykro nam, ale nie znaleźliśmy testu.</h4>
                    <button type="button" className="btn btn-outline-primary mt-3" onClick={() => this.props.history.push("/recruiter_panel")}>Wróć do głównej strony</button>
                </div>
            )

        if(this.state.notAuthorized)
            return(
                <div className="text-center mt-5">
                    <h4>Nie jesteś upoważniony do edycji tego testu.</h4>
                    <button type="button" className="btn btn-outline-primary mt-3" onClick={() => this.props.history.push("/recruiter_panel")}>Wróć do głównej strony</button>
                </div>
            )

        return(
            <div className="recruiter-adduser container">
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
                                           {this.state.form != null && <Fragment>{this.state.form.name }</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/`)}>
                                                Powrót
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        <div className="row">
                                            <div className="col-12 text-left">
                                                {this.state.error && <Fragment>{this.state.error}</Fragment>}
                                                <h4><b>Podstawowe informacje:</b></h4>
                                                {this.state.form == null ?
                                                <Fragment>
                                                    <h5><b>Opis:</b> brak opisu</h5>
                                                    <h5><b>Data utworzenia:</b> brak daty utworzenia</h5>
                                                    <h5><b>Liczba osób uczestniczących w teście:</b> brak informacji o osobach</h5>
                                                    <h5><b>Czas wykonania testu: </b> brak informacji czasie wykonywania testu</h5>
                                                    <h5><b>Wersja językowa testu: </b> brak informacji o wersji językowej testu</h5>
                                                </Fragment>
                                                :
                                                <Fragment>
                                                     <h5><b>Opis: </b> {this.state.form.description == null ? <Fragment>brak opisu</Fragment> : <Fragment>{this.state.form.description}</Fragment>}</h5>
                                                    <h5><b>Data utworzenia:</b> {this.state.form.creationDate == null ? <Fragment>brak opisu</Fragment> : <Fragment>{this.state.form.creationDate}</Fragment>}</h5>
                                                    <h5><b>Liczba osób uczestniczących w teście:</b> {this.state.form.users == null ? <Fragment>brak osób</Fragment> : <Fragment>{this.state.form.users.length} osób</Fragment>}</h5>
                                                    <h5><b>Czas wykonania : </b> {this.state.form.time == null ? <Fragment>brak informacji czasie wykonywania testu</Fragment> : <Fragment>{this.state.form.time} minut</Fragment>}</h5>
                                                    <h5><b>Wersja językowa : </b> {this.state.form.lang == null ? <Fragment>PL (domyślnie) </Fragment> : <Fragment>{this.state.form.lang} </Fragment>}</h5>
                                                </Fragment>}
                                            </div>
                                            <div className="col-12 text-left">
                                                <br></br>
                                                <h4><b>Możliwe czynności:</b></h4>
                                                    <button type="button" className="btn btn-outline-primary ml-1 mt-1" onClick={() => this.props.history.push(`/recruiter_panel/modify/forms/${this.state.form.id}`)}>Modyfikuj podstawowe dane</button>
                                                    <button type="button" className="btn btn-outline-secondary ml-1 mt-1" onClick={() => this.props.history.push(`/recruiter_panel/modify/forms/${this.state.form.id}/users`)}>Modyfikuj użytkowników</button>
                                                    <button type="button" className="btn btn-outline-success ml-1 mt-1" onClick={() => this.props.history.push(`/recruiter_panel/modify/forms/${this.state.form.id}/questions`)}>Modyfikuj pytania</button>
                                                    <button type="button" className="btn btn-outline-dark ml-1 mt-1" onClick={() => this.props.history.push(`/recruiter_panel/translate/forms/${this.state.form.id}`)}>Przetłumacz test</button>
                                                    <button type="button" className="btn btn-outline-warning ml-1 mt-1" onClick={() => this.props.history.push(`/recruiter_panel/import_export/forms/${this.state.form.id}`)}>Importuj/Eksportuj test</button>
                                                    <button type="button" className="btn btn-outline-info ml-1 mt-1" onClick={() => this.props.history.push(`/recruiter_panel/solve/forms/${this.state.form.id}`)}>Sprawdź wyniki testu</button>
                                                    <button type="button" className="btn btn-outline-danger ml-1 mt-1"  onClick={this.handleDelete}>Usuń test</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}