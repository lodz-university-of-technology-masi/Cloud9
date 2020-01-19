import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../libs/style/card.css"
import "../../../libs/style/loading.css"
import "./TranslateForm.css"
import { API } from "aws-amplify";

export default class TranslateForm extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          form: null,
          isLoading: false,
          errorLoading: false,
          errors: [],
          error: "",
          translate: false,
          targetLang: "",
          sourceLang: "",
          questionList: []

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

    setInputForm = async (evt) => {
        let inputName = evt.target.name;
        let inputValue = evt.target.value;
        if(inputName === "sourceLang")
            this.setState({
                sourceLang: inputValue
            })
        else
            this.setState({
                targetLang: inputValue
            })

    }
    
    getForm(){
        return API.get("api", `forms/${this.props.match.params.id}`)
    }
    
    postTranslate(obj){
        return API.post("api", `forms/${this.state.form.id}/translate`, {
            body: obj
        })
    }
    handleSubmitForm = async event => {
        event.preventDefault()
        this.setState({
            isLoading: true,
            errors: []
        });
        let errors = []
        if(this.state.sourceLang.length === 0)
            errors.push("Musisz wybrać język z jakiego ma zostać przetłumaczony test")

        if(this.state.targetLang.length === 0)
            errors.push("Musisz wybrać język na jaki ma zostać przetłumaczony test")

        if(this.state.targetLang.length > 0 && this.state.targetLang === this.state.sourceLang)
            errors.push("Języki nie mogą być takie same")

        if(errors.length > 0){
            this.setState({
                isLoading: false,
                errors: errors
            });
            return
        }
        let targetLanguage = this.state.targetLang.toLowerCase()
        let sourceLanguage = this.state.sourceLang.toLowerCase()
        try {
            const translateQuestion = await this.postTranslate({
                targetLanguage: targetLanguage,
	            sourceLanguage: sourceLanguage
            })

            this.setState({
                translate: true,
                questionList: translateQuestion,
                isLoading: false
            })

        }
        catch (e) {
            if(e.response.data === "Not questions found with that source language")
                this.setState({
                    isLoading: false,
                    errors: ['Nie ma pytań z tego języka co chcesz przetłumaczyć.']
                })
            else
                this.setState({
                    isLoading: false,
                    errors: ['Wystąpił błąd podczas tłumaczenia pytań do testu.']
                })
            
        }
        
    }

    renderBase() {
        return (
            <div>
                <form onSubmit={this.handleSubmitForm}>
                        <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                            {this.showErrors()}
                        </div>
                        <div className="form-group">
                            <label className="my-1 mr-2" htmlFor="langSourceSelect">Wybierz język z jakiego ma zostać przetłumaczony test.</label>
                            <select className="custom-select my-1 mr-sm-2" id="langSourceSelect" name="sourceLang" onChange={this.setInputForm}>
                                <option defaultValue>Wybierz język...</option>
                                <option value="PL">Polski</option>
                                <option value="EN">Angielski</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label className="my-1 mr-2" htmlFor="langTargetSelect">Wybierz język na jaki ma zostać przetłumaczony test.</label>
                            <select className="custom-select my-1 mr-sm-2" id="langTargetSelect" name="targetLang" onChange={this.setInputForm}>
                                <option defaultValue>Wybierz język...</option>
                                <option value="PL">Polski</option>
                                <option value="EN">Angielski</option>
                            </select>
                        </div>
                        <div className="form-group float-right">
                            <button type="submit" className="btn btn-outline-success btn-next-step">Tłumacz test</button>
                        </div>
                    </form>
                </div>
        );
    }
    renderQuestion(){
        let questions = this.state.questionList
        return(
            <div>
                {questions.length > 0 ?
                <Fragment>
                    <div className="text-left mt-4 mb-4">
                        <h4><b>Uwaga!</b> Nie zapominij zmienić wersji językowej testu (w podstawowych infromacjach)! </h4>
                    </div>
                    {questions.map((question, index) => 
                    <Fragment key={index}>
                        {question.type === "W" ? 
                        <Fragment>
                            <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                                <h5><b>{question.question}</b></h5>
                                <h5> Rodzaj tłumaczenia: {question.language}</h5>
                                <h5> Poprawna odpowiedź: {question.answerList[question.answer]}</h5>
                                <h5>Odpowiedzi:</h5>
                                <ul>
                                    {question.answerList.map((answer, index) => <li key={index}>{index +1}.<b> {answer}</b></li>)}
                                </ul>
                            </div>
                        </Fragment>
                        : question.type === "L" ? 
                        <Fragment >
                            <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                            <h5><b>{question.question}</b></h5>
                            <h5> Rodzaj tłumaczenia: {question.language}</h5>
                            </div>
                        </Fragment> 
                            
                        :  <Fragment>
                                <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                                <h5><b>{question.question}</b></h5>
                                <h5> Rodzaj tłumaczenia: {question.language}</h5>
                                </div>
                            </Fragment> }
                    </Fragment>)}
                </Fragment>
                :
                <Fragment>
                    <div className="text-center mt-5 mb-5">
                        <h5>Nie posiadasz żadnych pytań. Spróbuj je stworzyć lub zakończ tworzenie testu klikając w poniższy przycisk.</h5>
                        <button type="button" className="btn btn-outline-primary mt-3" onClick={() => this.props.history.push("/recruiter_panel")}>Zakończ tworzenie testu</button>
                    </div>
                </Fragment>}

            </div>
        )
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
                                           Tłumaczenie testu - {this.state.form != null && <Fragment>{this.state.form.name }</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/forms/${this.state.form.id}`)}>
                                                Powrót
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                       {this.state.translate ?
                                            <Fragment>
                                                {this.renderQuestion()}
                                            </Fragment>
                                            :
                                            <Fragment>
                                                {this.renderBase()}
                                            </Fragment>
                                       }
                                       
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}