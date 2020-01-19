import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./ModifyBasicInfromtionForm.css"
import { API } from "aws-amplify";

export default class ModifyBasicInfromtionForm extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          form: null,
          isLoading: false,
          errorLoading: false,
          errors: [],
          name: "",
          description: "",
          time: "",
          lang: "",
          success: ""

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

    getForm(){
        return API.get("api", `forms/${this.props.match.params.id}`)
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

    setLang = async (evt) => {
        this.setState({
            lang : evt.target.value
        });
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
        if(nameLen > 0 && nameLen > 50 )
            errors.push("Nazwa testu nie może przekraczać 50 znaków")
        if( descriptionLen > 0 && descriptionLen > 250 )
            errors.push("Opis testu nie może przekraczać 250 znaków")
        if( timeStringLen > 0){
            let timeNumber = parseInt(this.state.time);
            if( timeNumber < 0 &&  !isNaN(timeNumber)) 
                errors.push("Czas musi być wartością dodatnią")
            else
                this.setState({
                    time : timeNumber
                });
        }

        if(errors.length > 0)
        {
            this.setState({
                isLoading: false,
                errors : errors
            });
            return;
        }

        let obj ={
            name: this.state.form.name,
            description: this.state.form.description,
            time: this.state.form.time,
            lang: this.state.form.lang,
            recruiter: this.props.user.attributes.sub
        };

        let check = false
        if(this.state.form.name !== this.state.name && this.state.name.length > 0){
            obj.name = this.state.name
            check = true
        }
        
        if(this.state.form.description !== this.state.description && this.state.description.length > 0){
            obj.description = this.state.description
            check = true
        }

        if(this.state.form.time !== this.state.time && this.state.time > 0){
            obj.time = this.state.time
            check = true
        }

        if(this.state.form.lang !== this.state.lang && this.state.lang.length > 0){
            obj.lang = this.state.lang
            check = true
        }

        if(check === true){
            await this.updateForm(obj)
            .then(async (response) => {
                this.setState({
                    isLoading: false,
                    success: ['Pomyślnie zaktualizowano dane'],
                    form: {
                        id: this.state.form.id,
                        name: obj.name,
                        lang: obj.lang,
                        description: obj.description,
                        time: obj.time
                    }
                })
                
            })
            .catch(err => {
                this.setState({
                    isLoading: false,
                    errors: ['Coś poszło nie tak z modyfikacją testu']
                })
            }) 
        }
        else
            this.setState({
                isLoading: false,
                errors: ['Nie możesz zmodyfikować niezmienionego testu.']
            })
            
    }

    updateForm(form) {
        return API.put("api", `forms/${this.state.form.id}`, {
          body: form
        })
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
                   {this.state.form == null?
                    <Fragment>
                        <div className="row">
                            <div className="col-lg-12 col-md-12">
                                <div className="card">
                                    <div className="card-header card-header-success">
                                        <h4 className="card-title">
                                            Brak informacji
                                        </h4>
                                    </div>
                                    <div className="card-body">
                                            <h4>Brak informacji</h4>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>
                    :
                    <Fragment>
                        <div className="row">
                            <div className="col-lg-12 col-md-12">
                                <div className="card">
                                    <div className="card-header card-header-success">
                                        <h4 className="card-title">
                                        Modyfikuj podstawowe dane - {this.state.form != null && <Fragment>{this.state.form.name}</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                                <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/forms/${this.state.form.id}`)}>
                                                    Powrót
                                                </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                            
                                            <form onSubmit={this.handleBaseFormSubmit}>
                                                {this.state.success && <Fragment>
                                                    <div className="alert alert-success" role="alert">
                                                        {this.state.success}
                                                    </div>
                                                    </Fragment>}
                                                <div className={"alert " + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                                                    {this.showErrors()}
                                                </div>
                                                <div className="form-group">
                                                    <label htmlFor="name-input" className="text-uppercase float-left">Nazwa testu</label>
                                                    <input type="text" className="form-control name-input" defaultValue={this.state.form.name} placeholder="Wprowadź tytuł swojego testu" onChange={this.setName}/>
                                                </div>
                                                <div className="form-group">
                                                    <label htmlFor="description-input" className="text-uppercase float-left">Opis testu</label>
                                                    <textarea className="form-control description-input" defaultValue={this.state.form.description} rows="3" placeholder="Opisz swój tekst w celu ułatwienia dalszej pracy :)" onChange={this.setDescription}></textarea>
                                                </div>
                                                <div className="form-group mb-1">
                                                    <label htmlFor="time-input" className="text-uppercase float-left">Czas wykonywania testu w minutach</label>
                                                    <input type="number" className="form-control time-input" defaultValue={this.state.form.time} min="1" onChange={this.setTime}/>
                                                </div>
                                                <div className="form-group">
                                                    <label className="my-1 mr-2" htmlFor="langSelect">Język pytania</label>
                                                    <select className="custom-select my-1 mr-sm-2" id="langSelect" onChange={this.setLang} name="language">
                                                        {this.state.form.lang === null || this.state.form.lang === "PL" ?
                                                        <Fragment>
                                                            <option defaultValue value="PL">Polski</option>
                                                            <option value="EN">Angielski</option>
                                                        </Fragment>
                                                        :
                                                        <Fragment>
                                                            <option defaultValue value="EN">Angielski</option>
                                                            <option value="PL">Polski</option>
                                                        </Fragment>
                                                        }
                                                    </select>
                                                </div>
                                                <div className="form-group float-right">
                                                    <button type="submit" className="btn btn-outline-success btn-next-step">Przejdź do kolejnego kroku</button>
                                                </div>
                                            </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
               </Fragment>}
       </div>)
    }
}