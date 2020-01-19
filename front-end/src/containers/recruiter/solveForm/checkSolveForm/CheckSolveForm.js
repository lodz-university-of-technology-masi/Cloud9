import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./CheckSolveForm.css"
import { API } from "aws-amplify";

export default class ListSolveForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
          formId: "",
          solveForms: null,
          isLoading: false,
          errorLoading: false,
          errors: [],
          checks: [],
          checksTrue: [],
          checksFalse: [],
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
          const solveForm = await this.getSolveForms()
          if(solveForm.recruiterId !== this.props.user.attributes.sub){
              this.setState({
                  isLoading: false,
                  notAuthorized: true
              })
              return
          }

          let tmp = ""
          if(solveForm.questions.length > 0)
            tmp = solveForm.questions[0].formId
          if(solveForm.check !== null){
            let positive = []
            let negative = []
            solveForm.check.forEach((value) =>{
                if(value.check)
                    positive.push(value.id)
                else
                    negative.push(value.id)
            })

            this.setState({
                checksTrue: positive,
                checksFalse: negative
            })
          }
            this.setState({
                isLoading: false,
                solveForms: solveForm,
                formId: tmp
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

    getSolveForms(){
        return API.get("api", `solveform/${this.props.match.params.id}`)
    }

    setCheckAnswer = async (evt) => {
        let inputName = evt.target.name;
        let inputValue = evt.target.value;
        let check = false
        let index = 0
        let checks = this.state.checks
        checks.forEach((value, i) => {
            if(value.id === inputName){
                check = true
                index = i
            }
        })
        if(check)
            checks.splice(index, 1)

        checks.push({
            id: inputName,
            check: (inputValue === "true")
        })

        this.setState({
            checks: checks
        })

    }

    addSolveForm(obj){
        return API.post("api", `checksolveform/${this.props.match.params.id}/user/${this.state.solveForms.user.id}`, {
            body: obj
        })
    }

    submitFormHandler = async (evt) => {
        this.setState({
            isLoading: true,
            errors: []
        });
        let error = []
        if(this.state.checks.length !== this.state.solveForms.questions.length)
            error.push('Musisz sprawdzić wszystkie odpowiedzi')

        if(error.length > 0) {
            this.setState({
                isLoading: false,
                errors: error
            });
            return
        }

        try {
            await this.addSolveForm({
                checks: this.state.checks
            })
        }
        catch (e) {
            this.setState({
                isLoading: false,
                errors: ['Wystąpił błąd podczas dodowania pytań do testu.']
            })
        }

        this.setState({
            isLoading: false,
            success: "Pomyślnie zmodyfikowano pytania"
        })
    }


    renderChecksForm(){
        if(this.state.solveForms !== null){
            let checkTab = this.state.solveForms.questions.map((question) =>{
                let answerForQuestion = ""
                this.state.solveForms.answers.forEach((answer) =>{
                    if(answer.questionId === question.id){
                        answerForQuestion = answer.answer
                    }
                })
                if(question.type === "W"){
                    let tmp = parseInt(answerForQuestion)
                    let obj = {
                        id: question.id,
                        question: question.question,
                        type: question.type,
                        answer: question.answerList[tmp]
                    }
                    return obj
                }
                else{
                    let obj = {
                        id: question.id,
                        question: question.question,
                        type: question.type,
                        answer: answerForQuestion
                    }
                    return obj
                }
            })
            return(
                <div>
                    {checkTab.map((answer, index) =>
                        <Fragment key={index}>
                            <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                                <h5><b>{answer.question}</b></h5>
                                <h5>Odpowiedź: <b>{answer.answer}</b></h5>

                                <div className="text-right mt-1 mb-4">
                                    <select className="custom-select my-1 mr-sm-2" id="langSelect" onChange={this.setCheckAnswer} name={answer.id}>
                                        <option defaultValue>Wybierz odpowiedź</option>
                                        <option value="true">Poprawna</option>
                                        <option value="false">Niepoprawna</option>
                                    </select>
                                </div>
                                
                            </div>
                        </Fragment>)}
                        <div className="text-right">
                            <button type="button" className="btn btn-outline-primary mt-3" onClick={this.submitFormHandler}>Zapisz wyniki</button>
                        </div>
                        
                </div>
            )
        }
        
        return (
           <div className="text-center mt-5">
                <h4>Brak informacji</h4>
            </div>)
        
    }

    renderCheckedAnswer(){
        if(this.state.solveForms !== null){
            let checkTab = this.state.solveForms.questions.map((question) =>{
                let answerForQuestion = ""
                this.state.solveForms.answers.forEach((answer) =>{
                    
                    if(answer.questionId === question.id){
                        answerForQuestion = answer.answer
                    }
                })
                if(question.type === "W"){
                    let tmp = parseInt(answerForQuestion)
                    let obj = {
                        id: question.id,
                        question: question.question,
                        type: question.type,
                        answer: question.answerList[tmp]
                    }
                    return obj
                }
                else{
                    let obj = {
                        id: question.id,
                        question: question.question,
                        type: question.type,
                        answer: answerForQuestion
                    }
                    return obj
                }
            })
            return(
                <div>
                    {checkTab.map((answer, index) =>
                        <Fragment key={index}>
                            <div className={"text-left p-3 border  rounded border-bottom mt-3 " + (this.state.checksTrue.includes(answer.id) ? 'border-success' : 'border-danger')}>
                                <h5><b>{answer.question}</b></h5>
                                <h5>Odpowiedź: <b>{answer.answer}</b></h5>
                                {this.state.checksTrue.includes(answer.id) ?
                                <Fragment><b className="text-success">Pozytywna odpowiedź</b></Fragment>
                                :
                                <Fragment><b className="text-danger">Negatywna odpowiedź</b></Fragment>
                                }
                            </div>
                        </Fragment>)}
                        
                </div>
            )
        }
        
        return (
           <div className="text-center mt-5">
                <h4>Brak informacji</h4>
            </div>)
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
                                           Wyniki użytkownika: {this.state.solveForms != null && <Fragment>{this.state.solveForms.user.name} {this.state.solveForms.user.surname}</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/solve/forms/${this.state.formId}`)}>
                                                Powrót
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                                            {this.showErrors()}
                                        </div>

                                        {this.state.checksTrue.length === 0 && this.state.checksFalse.length ===0 ? 
                                        <Fragment>{this.renderChecksForm()}</Fragment>
                                        :
                                        <Fragment>{this.renderCheckedAnswer()}</Fragment>
                                        }

                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}