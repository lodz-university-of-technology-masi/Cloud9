import React, { Component, Fragment } from "react"
import {Redirect} from "react-router-dom";
import { API } from "aws-amplify";
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./AddQuestion.css"

Array.prototype.equals = function (array) {
    if (!array)
        return false;

    if (this.length != array.length)
        return false;

    for (var i = 0, l=this.length; i < l; i++) {
        if (this[i] instanceof Array && array[i] instanceof Array) {
            if (!this[i].equals(array[i]))
                return false;       
        }           
        else if (this[i] != array[i]) { 
            return false;   
        }           
    }       
    return true;
}


export default class AddQuestion extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: true,
            errors: [],
            form: null,
            errorLoading: false,
            notFound: false,
            notAuthorized:false,
            questions:[],
            questionClassActive: "",
            questionContainer:{
                openQuestionContainer: {
                    name: '',
                    lang: ''
                },
                listQuestionContainer: {
                    name: '',
                    lang: '',
                    answers: [],
                    answer: '',
                    tmp: '',
                    error: ''
                },
                numberQuestionContainer: {
                    name: '',
                    lang: ''
                }
            }
        }
    }

    async componentDidMount() {
        if (!this.props.isAuthenticated) 
          return;

        if(!this.props.match.params.id)
          this.props.history.push("/recruiter_panel")


          this.getForm()
          .then((response) => {
              if(response.recruiterId !== this.props.user.attributes.sub){
                  this.setState({
                      isLoading: false,
                      notAuthorized: true
                  })
                  return
              }
  
              this.setState({
                  form: response
              })
              this.getQuestion()
              .then((response) => {
                  let questions = []
                  response.forEach((val) => {
                      if(val.type === "W")
                        val.answer = parseInt(val.answer)
                      
                      questions.push(val)
                  })
                  this.setState({
                      isLoading: false,
                      questions: questions
                  })
              })
              .catch((response) =>{
                  
              })
  
          })
          .catch((e) => {
  
          })

    }


    getForm(){
        return API.get("api", `forms/${this.props.match.params.id}`)
    }

    getQuestion(){
        return API.get("api", `forms/${this.state.form.id}/questions`)
    }

    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });

        return error;

    }
    //all question function
    changeQuestion = async (name) =>{
        this.setState({
            questionClassActive: name,
            openQuestionContainer: {name: '', lang: ''},
            listQuestionContainer: {name: '', lang: '', answers: [], answer: ''},
            numberQuestionContainer: {name: '', lang: ''},
            errors: []
        })
    }

    removeQuestion = async (evt) => {
        let index = evt.target.name;
        let statusCopy = Object.assign({}, this.state);
        if(statusCopy.questions.length > 0)
            statusCopy.questions.splice(index, 1)
         this.setState(statusCopy)
    }


    //number question function
    handleNumberQuestion = async event => {
        event.preventDefault()
        this.setState({
            isLoading: true,
            errors: []
        });
        let errors = []
        let obj = this.state.questionContainer.numberQuestionContainer
        if(obj.name.length === 0)
            errors.push("Treść pytania nie możę być pusta.")
        if(obj.lang === "")
            errors.push("Musisz wybrać język pytania")
        if(obj.lang !== "EN" && obj.lang !== "PL" && obj.lang.length > 0)
            errors.push("Obsługiwane języki to tylko EN i PL")

        if(errors.length > 0){
            this.setState({
                isLoading: false,
                errors: errors
            });
            return
        }
            
        let questions = this.state.questions
        questions.push({
            type: "L",
            lang: obj.lang,
            question: obj.name
        })

        this.setState({
            isLoading: false,
            errors: [],
            questions: questions,
            questionClassActive: ""
        });
        
    }

    setNumberQuestionData = async (evt) => {
        let inputName = evt.target.name;
        let inputValue = evt.target.value;
        let statusCopy = Object.assign({}, this.state);
        statusCopy.questionContainer['numberQuestionContainer'][inputName] = inputValue
        this.setState(statusCopy)
    }

    //open question
    handleOpenQuestion = async event => {
        event.preventDefault()
        this.setState({
            isLoading: true,
            errors: []
        });

        let errors = []
        let obj = this.state.questionContainer.openQuestionContainer
        if(obj.name.length === 0)
            errors.push("Treść pytania nie możę być pusta.")
        if(obj.lang === "")
            errors.push("Musisz wybrać język pytania")
        if(obj.lang !== "EN" && obj.lang !== "PL" && obj.lang.length > 0)
            errors.push("Obsługiwane języki to tylko EN i PL")

        if(errors.length > 0){
            this.setState({
                isLoading: false,
                errors: errors
            });
            return
        }
            
        let questions = this.state.questions
        questions.push({
            type: "O",
            lang: obj.lang,
            question: obj.name
        })

        this.setState({
            isLoading: false,
            errors: [],
            questions: questions,
            questionClassActive: ""
        });
        
    }
    
    setOpenQuestionData = async (evt) => {
        let inputName = evt.target.name;
        let inputValue = evt.target.value;
        let statusCopy = Object.assign({}, this.state);
        statusCopy.questionContainer['openQuestionContainer'][inputName] = inputValue
        this.setState(statusCopy)
    }

    //render function
    renderNumberQuestion() {
        return (
            <div className="number-question">
                 <form onSubmit={this.handleNumberQuestion}>
                    <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                        {this.showErrors()}
                    </div>
                    <div className="form-group">
                        <label htmlFor="name-input" className="text-uppercase float-left">Treść pytania</label>
                        <input type="text" className="form-control name-input" placeholder="Wprowadź treść swojego pytania numerycznego..." onChange={this.setNumberQuestionData} name="name"/>
                    </div>
                    <div className="form-group">
                        <label className="my-1 mr-2" htmlFor="langSelect">Język pytania</label>
                        <select className="custom-select my-1 mr-sm-2" id="langSelect" name="lang" onChange={this.setNumberQuestionData}>
                            <option defaultValue>Wybierz język...</option>
                            <option value="PL">Polski</option>
                            <option value="EN">Angielski</option>
                        </select>
                    </div>
                    <div className="form-group float-right">
                        <button type="submit" className="btn btn-outline-success btn-next-step">Dodaj pytanie</button>
                    </div>
                </form>
            </div>
        )
    }

    renderListQuestion() {
        let errorAddAnswer = this.state.questionContainer.listQuestionContainer.error
        let answers = this.state.questionContainer.listQuestionContainer.answers
        return (
            <div className="list-question">
                <form onSubmit={this.handleListQuestion}>
                    <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                        {this.showErrors()}
                    </div>
                    <div className="form-group">
                        <label htmlFor="name-input" className="text-uppercase float-left">Treść pytania</label>
                        <input type="text" className="form-control name-input" placeholder="Wprowadź treść swojego pytania wyboru..." onChange={this.setListQuestionData} name="name"/>
                    </div>
                    <div className="form-group">
                        <label className="my-1 mr-2" htmlFor="langSelect">Język pytania</label>
                        <select className="custom-select my-1 mr-sm-2" id="langSelect" onChange={this.setListQuestionData} name="lang">
                            <option defaultValue>Wybierz język...</option>
                            <option value="PL">Polski</option>
                            <option value="EN">Angielski</option>
                        </select>
                    </div>
                    <br></br>
                    {errorAddAnswer && <div className="alert alert-danger" role="alert"> {errorAddAnswer}</div>}
                    <div className="input-group mb-3">
                        <label className="my-1 mr-2" htmlFor="answersSelect">Możliwe odpowiedzi</label>
                        <input type="text" className="form-control " id="answersSelect" placeholder="Wprowadź treść odpowiedzi" aria-describedby="button-addon2" name="tmp" onChange={this.setListQuestionData}/>
                        <div className="input-group-append">
                            <button className="btn btn-outline-secondary" type="button" id="button-addon2" onClick={this.addAnswerToList}>Dodaj odpowiedź</button>
                        </div>
                    </div>
                    {answers.length > 0 && <Fragment>
                    <div className="form-group">
                        <label className="my-1 mr-2" htmlFor="answerSelect">Wybierz poprawną odpowiedź</label>
                        <select className="custom-select my-1 mr-sm-2" id="answerSelect" onChange={this.setListQuestionData} name="answer">
                            <option defaultValue>Wybierz odpowiedź</option>
                            {answers.map((answer, index) => <option key={index} value={index}>{answer}</option>)}
                        </select>
                    </div>
                    
                    <h4>Lista odpowiedzi</h4>
                    <ul>
                    {answers.map((answer, index) =>
                    <Fragment key={index}>
                        <li className="mt-1">{index+1}. {answer} <button className="btn btn-outline-danger btn-sm" onClick={this.removeAnswerFromList} name={index}>Usuń</button></li>
                    </Fragment>)}
                    </ul>
                    </Fragment>}
                    
                    <div className="form-group float-right">
                        <button type="submit" className="btn btn-outline-success btn-next-step">Dodaj pytanie</button>
                    </div>
                    

                </form>
            </div>
        )
    }

    renderOpenQuestion() {
        return (
            <div className="open-question">
                 <form onSubmit={this.handleOpenQuestion}>
                    <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                        {this.showErrors()}
                    </div>
                    <div className="form-group">
                        <label htmlFor="name-input" className="text-uppercase float-left">Treść pytania</label>
                        <input type="text" className="form-control name-input" placeholder="Wprowadź treść swojego pytania otwartego..." onChange={this.setOpenQuestionData} name="name"/>
                    </div>
                    <div className="form-group">
                        <label className="my-1 mr-2" htmlFor="langSelect">Język pytania</label>
                        <select className="custom-select my-1 mr-sm-2" id="langSelect" onChange={this.setOpenQuestionData} name="lang">
                            <option defaultValue>Wybierz język...</option>
                            <option value="PL">Polski</option>
                            <option value="EN">Angielski</option>
                        </select>
                    </div>
                    <div className="form-group float-right">
                        <button type="submit" className="btn btn-outline-success btn-next-step">Dodaj pytanie</button>
                    </div>
                </form>
            </div>
        )
    }

    

    listAllListQuestion() {
        let questions = this.state.questions
        return(
            <div>
                {questions.length > 0 ?
                <Fragment>
                    <div className="text-left mt-4 mb-4">
                        <h4><b>Wprowadzono zmiany. Czy zapisać?</b> <button type="button" className="btn btn-outline-success btn-sm" onClick={this.nextStep}>Zapisz zmiany i zakończ</button></h4>
                    </div>
                    {questions.map((question, index) => 
                    <Fragment key={index}>
                        {question.type === "W" ? 
                        <Fragment>
                            <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                                <h5><b>{question.question}</b></h5>
                                <h5> Rodzaj tłumaczenia: {question.lang}</h5>
                                <h5> Poprawna odpowiedź: {question.answerList[question.answer]}</h5>
                                <h5>Odpowiedzi:</h5>
                                <ul>
                                    {question.answerList.map((answer, index) => <li key={index}>{index +1}.<b> {answer}</b></li>)}
                                </ul>
                                <div className="text-right">
                                    <button className="btn btn-outline-danger" onClick={this.removeQuestion} name={index}>Usuń pytanie</button>
                                </div>
                            </div>
                        </Fragment>
                        : question.type === "L" ? 
                        <Fragment >
                            <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                            <h5><b>{question.question}</b></h5>
                            <h5> Rodzaj tłumaczenia: {question.lang}</h5>
                                <div className="text-right">
                                <button className="btn btn-outline-danger" onClick={this.removeQuestion} name={index}>Usuń pytanie</button>
                                </div>
                            </div>
                        </Fragment> 
                            
                        :  <Fragment>
                                <div className="text-left p-3 border border-success rounded border-bottom mt-3">
                                <h5><b>{question.question}</b></h5>
                                <h5> Rodzaj tłumaczenia: {question.lang}</h5>
                                    <div className="text-right">
                                    <button className="btn btn-outline-danger" onClick={this.removeQuestion} name={index}>Usuń pytanie</button>
                                    </div>
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

    renderQuestion() {
        let questionClassActive = this.state.questionClassActive
        let numberQuestion = this.renderNumberQuestion()
        let listQuestion = this.renderListQuestion()
        let openQuestion = this.renderOpenQuestion()
        let listAllListQuestion = this.listAllListQuestion()
        return (
            <div>
                <div className="add-question mt-4 mb-4">
                    <div className="text-center">
                        <h5>Dodaj nowe pytanie. Masz do wyboru:</h5>
                            <div className="btn-group mt-4" role="group" aria-label="Basic example">
                            <button type="button" className={"btn btn-outline-success " + (this.state.questionClassActive === "open" ? 'active' : '')} onClick={() => this.changeQuestion("open")}><b>Pytanie typu otwartego</b> </button>
                            <button type="button" className={"btn btn-outline-success " + (this.state.questionClassActive === "list" ? 'active' : '')} onClick={() => this.changeQuestion("list")}><b>Pytanie typu wyboru</b></button>
                            <button type="button" className={"btn btn-outline-success " + (this.state.questionClassActive === "number" ? 'active' : '')} onClick={() => this.changeQuestion("number")}><b>Pytanie typu liczbowego</b></button>
                            {this.state.questionClassActive !== "" && <div><button type="button" className="btn btn-outline-success" onClick={() => this.changeQuestion("")}><b>Anuluj dodawanie pytania</b></button> </div>}
                        </div>
                    </div>
                </div>
                {(function() {
                    switch(questionClassActive) {
                        case "number":
                            return numberQuestion;
                        case "list":
                            return listQuestion;
                        case "open":
                            return openQuestion;
                        default:
                            return listAllListQuestion;          
                    }
                })()}
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
                                           Dodawanie pytań 
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push("/recruiter_panel")}>
                                                Anuluj
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        {this.renderQuestion()}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}