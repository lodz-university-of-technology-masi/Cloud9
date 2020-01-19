import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../libs/style/loading.css"
import "../../../libs/style/card.css"
import "./ImportExportForm.css"
import { API } from "aws-amplify";

import { s3Upload } from "../../../libs/awsLib";
export default class ImportExportForm extends Component {
    constructor(props) {
        super(props);
        
        this.file = null;

        this.state = {
          form: null,
          isLoading: false,
          errorLoading: false,
          errors: [],
          error: "",
          attachmentURL: null
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
                form: form
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

    showErrors = () => {
        let error = [];

        this.state.errors.forEach((value, index) => {
            error.push(<li key={index}>{value}</li>)
        });
        return error;
    }

    renderBase() {
        return (
            <div className="row">
                
            </div>
        );
    }
    
    handleFileChange = event => {
        this.file = event.target.files[0];
    }
    
    handleSubmit = async event => {
        let attachment;
    
        event.preventDefault();
    
        this.setState({ isLoading: true });
    
        try {
          if (this.file) {
            let nameFile = "test"
            attachment = await s3Upload(this.file, nameFile);
          }
    
         
        } catch (e) {
            console.log(e)
        }
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
                                           Import/Export testu - {this.state.form != null && <Fragment>{this.state.form.name }</Fragment>}
                                        </h4>
                                        <div className="test-form-button text-right">
                                            <div className="btn btn-outline-light btn-sm mr-2" onClick={() => this.props.history.push(`/recruiter_panel/`)}>
                                                Powrót
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body">
                                        <form onSubmit={this.handleSubmit}>
                                            <div className="form-group">
                                                <input type="file" className="form-control-file" onChange={this.handleFileChange} />
                                            </div>
                                            <div className="form-group float-right">
                                                <button type="submit" className="btn btn-outline-success btn-next-step">Import testu</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Fragment>}
            </div>)
    }
}