import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../../libs/style/card.css"
import "../../../../libs/style/loading.css"
import "./ModifyForm.css"
import { API } from "aws-amplify";

export default class Main extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          form: null,
          isLoading: false,
          errorLoading: false,
          errors: []
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

    renderBase() {
        return (
            <div></div>
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
                                    </div>
                                    <div className="card-body">
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