import React, { Component } from "react"
import { Link } from "react-router-dom"
import "../../../libs/style/card.css"
import "./Main.css"
import { API } from "aws-amplify";

export default class Main extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          forms: []
        };
    }
    
    async componentDidMount() {
        if (!this.props.isAuthenticated) {
          return;
        }

        try {
            const forms = await this.forms()
            this.setState({ forms })
            console.log(forms)
        } 
        catch (e) {
            console.log(e);
        }
    }

    forms() {
        return API.get("api", "/forms");
    }



    renderListForm() {
        return(
            <div>test</div>
        );
    }

    render() {
        return(
            <div className="recruiter-main container">
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
                                <div className="tests-list">
                                    <div className="tests-item">
                                        <h5 className="text-left"> Testowy teścik <small>13.09.2019</small></h5>
                                        <h6 className="text-left">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</h6>
                                        <div className="btn-action text-right mt-1 mb-4">
                                            <Link to="/signin" className="btn btn-outline-primary btn-sm mr-2">
                                            Modyfikuj test
                                            </Link>
                                            <Link to="/signup" className="btn btn-outline-success btn-sm">
                                            Usuń test 
                                            </Link>
                                        </div>
                                    </div>
                                    <div className="tests-item">
                                        <h5 className="text-left"> Testowy teścik <small>13.09.2019</small></h5>
                                        <h6 className="text-left">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</h6>
                                        <div className="btn-action text-right mt-1 mb-4">
                                            <Link to="/signin" className="btn btn-outline-primary btn-sm mr-2">
                                            Modyfikuj test
                                            </Link>
                                            <Link to="/signup" className="btn btn-outline-success btn-sm">
                                            Usuń test 
                                            </Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}