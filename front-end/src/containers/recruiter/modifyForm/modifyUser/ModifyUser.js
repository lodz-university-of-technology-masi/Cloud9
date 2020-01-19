import React, { Component, Fragment } from "react"
import {Redirect} from "react-router-dom";
import { API } from "aws-amplify";
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./ModifyUser.css"

Array.prototype.equals = function (array) {
    if (!array)
        return false;

    if (this.length !== array.length)
        return false;

    for (var i = 0, l=this.length; i < l; i++) {
        if (this[i] instanceof Array && array[i] instanceof Array) {
            if (!this[i].equals(array[i]))
                return false;       
        }           
        else if (this[i] !== array[i]) { 
            return false;   
        }           
    }       
    return true;
}

export default class ModifyUser extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            errors: [],
            form: null,
            errorLoading: false,
            notFound: false,
            userToForm: [],
            users: [],
            notAuthorized:false,
            redirectForm: "",
            success: ""
        }

        
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

          if(form.users !== null){
              let tmp = form.users.map(value => {
                  return value.id
              })
              this.setState({
                  userToForm: tmp
              })
          }

          const users = await this.getUsers()
          this.setState({
              isLoading: false,
              form: form,
              users: users
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

    getForm(){
        return API.get("api", `forms/${this.props.match.params.id}`)
    }

    getUsers(){
        return API.get("api", `users`)
    }

    addUsersForm(users){
        return API.put("api", `/forms/${this.state.form.id}`, {
            body: users
        })
    }

    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });

        return error;
    }

    handleRemoveUserFromList = async (userId) => {
        let tmpList = this.state.userToForm
        let number = 0
        tmpList.forEach((value, index) => {
            if(value === userId)
                number = index
        })
        tmpList.splice(number, 1)
        this.setState({
            userToForm: tmpList,
            success: ""
        })
    }

    handleAddUserToList = async (userId) => {
        let tmpList = this.state.userToForm
        tmpList.push(userId)
        this.setState({
            userToForm: tmpList,
            success: ""
        })
        
    }

    checkDifferenceUserList = () => {
        if(this.state.form !== null ){
            let actualUserInForm
            if(this.state.form.users === null)
                actualUserInForm = []
            else
                actualUserInForm = this.state.form.users.map((value) => {return value.id})
            let actualUser = this.state.userToForm
            if(actualUserInForm.equals(actualUser))
                return false
            else 
                return true
            
        }
        
        return false
    }

    nextStep = async event => {
        this.setState({
            isLoading: true,
            success: ""
        });

        try {
            await this.addUsersForm({
                recruiter: this.state.form.recruiterId,
                users: this.state.userToForm
            })
        }
        catch (e) {
            this.setState({
                isLoading: false,
                errors: ['Wystąpił błąd podczas dodowania użytkowników do testu.']
            })
        }

        this.setState({
            isLoading: false,
            success: "Pomyślnie zaktualizowano dane użytkowników"
        })
    }


    renderBase() {
        return (
            <div className="row">
                {this.state.users.map((user) =>
                    <Fragment key={user.id}>
                        <div className="col-4">
                                <div className="p-3 border border-success rounded">
                                <h5>{user.name}</h5>
                                <h5>{user.surname}</h5>
                                <h5>{user.email}</h5>
                                    <div className="text-right mt-2">
                                        {this.state.userToForm.indexOf(user.id) >= 0 ?
                                            <Fragment>
                                                <button onClick={() => this.handleRemoveUserFromList(user.id)} className="btn btn-outline-danger">
                                                    Usuń użytkownika
                                                </button>
                                            </Fragment>
                                            : 
                                            <Fragment>
                                                <button onClick={() => this.handleAddUserToList(user.id)} className="btn btn-outline-success">
                                                    Dodaj użytkownika
                                                </button>
                                            </Fragment>
                                        }
                                    </div>
                                </div>
                        </div>
                    </Fragment>)}
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
                                            {this.state.form != null && <Fragment> Modyfikacja użytkowników - {this.state.form.name}</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/forms/${this.state.form.id}`)}>
                                                Powrót
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        {this.state.success && <Fragment>
                                                    <div className="alert alert-success" role="alert">
                                                        {this.state.success}
                                                    </div>
                                                    </Fragment>}
                                        {this.checkDifferenceUserList() && this.state.success.length === 0 && 
                                        <Fragment>
                                            <div>
                                                <div className="text-left mt-4 mb-4">
                                                    <h4>Wprowadzono zmiany. Czy zapisać? <button type="button" className="btn btn-outline-success btn-sm" onClick={this.nextStep}>Zapisz zmiany i przejdź dalej</button></h4>
                                                </div>
                                            </div>
                                        </Fragment>}
                                        <div className={"alert" + (this.state.errors.length > 0 ? 'alert-danger' : '')} role="alert">
                                            {this.showErrors()}
                                        </div>
                                        {this.renderBase()}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}
