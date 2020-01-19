import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./listSolveForm.css"
import { API } from "aws-amplify";

export default class ListSolveForm extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          form: null,
          solveForms: [],
          isLoading: false,
          errorLoading: false,
          errors: [],
          error: "",
          personNotSolve: null
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
          const solveforms = await this.getSolveForms()
          let tmpSolveUser = null
          if(solveforms.length !== form.users.length)
            tmpSolveUser = form.users.length - solveforms.length

          this.setState({
              isLoading: false,
              form: form,
              solveForms: solveforms,
              personNotSolve: tmpSolveUser
          })
      } 
      catch (e) {
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


    getForm(){
        return API.get("api", `forms/${this.props.match.params.id}`)
    }

    getSolveForms(){
        return API.get("api", `solveform/form/${this.props.match.params.id}`)
    }
    
    renderSolveForm(){
        let renderList = this.state.solveForms.map((value) => {
            let nameUser = ""
            let emailUser = ""
            let check = null
            if(value.check !== null){
                check = 0
                value.check.forEach((v) => {
                    if(v.check === true)
                        check++
                })
            }
            if(value.user.name)
                nameUser += value.user.name
            if(value.user.surname)
                nameUser +=" "+ value.user.surname
            if(value.user.email)
                emailUser = value.user.email

            let obj ={
                id: value.id,
                nameUser: nameUser,
                emailUser: emailUser,
                fitInTime: value.fitInTime,
                check: check,
                answersLen: value.answers.length
            }
            return obj
        })


        return (
            <div className="tests-list">
                {renderList.map((formSolutions) =>
                    <Fragment key={formSolutions.id}>
                        <div className="tests-item">
                            <h4 className="text-left">Użytkownik: {formSolutions.nameUser}</h4>
                            <h5 className="text-left">Adres e-mail: {formSolutions.emailUser}</h5>
                            <h5 className="text-left">Spóźnienie w rozwiązaniu testu: {formSolutions.fitInTime === false ? 
                                <Fragment>
                                    nie
                                </Fragment>
                                :
                                <Fragment>
                                    tak
                                </Fragment>
                                }
                            </h5>
                            {formSolutions.check === null ?
                            <Fragment>
                                <div className="btn-action text-right mt-1 mb-4">
                                    <Link to={`/recruiter_panel/solve/forms/check/${formSolutions.id}`} className="btn btn-outline-success btn-sm mr-2">
                                        Sprawdź odpowiedzi
                                    </Link>
                                </div>
                            </Fragment>
                            :
                            <Fragment>
                                <h5 className="text-left">Liczba poprawnych odpowiedzi: <b>{formSolutions.check}</b> / <b>{formSolutions.answersLen}</b></h5>
                                <div className="btn-action text-right mt-1 mb-4">
                                    <Link to={`/recruiter_panel/solve/forms/check/${formSolutions.id}`} className="btn btn-outline-success btn-sm mr-2">
                                        Sprawdź odpowiedzi
                                    </Link>
                                </div>
                            </Fragment>}
                        </div>
                    </Fragment>)}
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
                                           Sprawdź wyniki - {this.state.form != null && <Fragment>{this.state.form.name }</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/forms/${this.state.form.id}`)}>
                                                Powrót
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        <div className="tests-list">
                                        {this.state.personNotSolve !== null && 
                                            <Fragment>
                                                <div className="tests-item">
                                                    <h5 className="text-left"> Liczba użytkowników, która nie rozwiązała testu: {this.state.personNotSolve} </h5> 
                                                </div>
                                            </Fragment>}
                                            {this.state.solveForms ? 
                                            <Fragment>
                                                {this.renderSolveForm()}
                                            </Fragment>
                                            :
                                            <Fragment>
                                                <div className="text-center mt-5 mb-5">
                                                    <h4>Nie masz aktualnie testów do sprawdzenia</h4>
                                                </div>
                                            </Fragment>}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}