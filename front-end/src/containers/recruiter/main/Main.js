import React, { Component, Fragment } from "react"
import { Link } from "react-router-dom"
import "../../../libs/style/card.css"
import "../../../libs/style/loading.css"
import "./Main.css"
import { API } from "aws-amplify";

export default class Main extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          forms: [],
          isLoading: false,
          errorLoading: false
        };
    }
    
    async componentDidMount() {
        if (!this.props.isAuthenticated) {
          return;
        }

        this.setState({
            isLoading: true
        });

        try {
            const forms = await this.forms()
            this.setState({ forms })
            this.setState({
                isLoading: false,
                forms: forms
            });
            
        } 
        catch (e) {
            this.setState({
                errorLoading: true,
                isLoading: false
            })
        }
    }

    forms() {
        return API.get("api", `form/recruiter/${this.props.user.attributes.sub}`);
    }

    renderForms() {
        
        return (
            <div className="tests-list">
                {this.state.forms.map((form) =>
                    <Fragment key={form.id}>
                        <div className="tests-item">
                            <h5 className="text-left"> {form.name} <small>{form.creationDate}</small></h5>
                            <h6 className="text-left">{form.description}</h6>
                            <div className="btn-action text-right mt-1 mb-4">
                                <Link to={`/recruiter_panel/forms/${form.id}`} className="btn btn-outline-primary btn-sm mr-2">
                                    Pokaż więcej
                                </Link>
                            </div>
                        </div>
                    </Fragment>)}
            </div>
        );
    }
    
    render() {
        return(
            <div className="recruiter-main container">
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
                        {this.state.errorLoading ? 
                            <Fragment>
                                <div className="text-center mt-5">
                                    <h4>Uwaga coś poszło nie tak podczas pobrania danych z serwera</h4>
                                </div>
                            </Fragment>
                            :
                            <Fragment>
                                <div className="row">
                                    <div className="col-lg-12 col-md-12">
                                        <div className="card">
                                            <div className="card-header card-header-success">
                                                <h4 className="card-title">Lista testów </h4>
                                                <div className="test-form-button text-right">
                                                    <Link to="/recruiter_panel/add_form" className="btn btn-outline-light btn-sm mr-2">
                                                        Dodaj test
                                                    </Link>
                                                </div>
                                            </div>
                                            <div className="card-body">
                                            {this.state.forms.length ? 
                                                <Fragment>
                                                    {this.renderForms()}
                                                </Fragment>
                                                :
                                                <Fragment>
                                                <div className="text-center mt-5 mb-5">
                                                    <h4>Nie masz aktualnie utworzonego testu spróbuj go dodać</h4>
                                                </div>
                                                </Fragment>}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </Fragment>}
                    </Fragment>}
            </div>
        );
    }
}