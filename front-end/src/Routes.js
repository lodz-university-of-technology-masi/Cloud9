import React from "react";
import { Route, Switch } from "react-router-dom";
import asyncComponent from "./components/AsyncComponent";
import AppliedRoute from "./components/AppliedRoute";
//import AuthenticatedRoute from "./components/AuthenticatedRoute";
import UnauthenticatedRoute from "./components/UnauthenticatedRoute";
import RecruiterRoute from "./components/RecruiterRoute";
import UserRoute from "./components/UserRoute";

const AsyncHome = asyncComponent(() => import("./containers/home/Home"));
const AsyncSignIn = asyncComponent(() => import("./containers/page/signin/Signin"));
const AsyncSignUp = asyncComponent(() => import("./containers/page/signup/Signup"));
const AsyncNotFound = asyncComponent(() => import("./containers/error/NotFound"));
const AsyncRecruiterPanel = asyncComponent(() => import("./containers/recruiter/main/Main"));
const AsyncUserPanel = asyncComponent(() => import("./containers/user/main/Main"));
const AsyncVerifyEmail = asyncComponent(() => import("./containers/page/verifyemail/verifyEmail"));
const AsyncRecruiterAddForm = asyncComponent(() => import("./containers/recruiter/createForm/addform/AddForm"));
const AsyncRecruiterAddUserToForm = asyncComponent(() => import("./containers/recruiter/createForm/addUserToForm/AddUser"));
const AsyncRecruiterAddQuestionToForm = asyncComponent(() => import("./containers/recruiter/createForm/addQuestionToForm/AddQuestion"))
const MainRecruiterGetForm = asyncComponent(() => import("./containers/recruiter/modifyForm/modifyForm/ModifyForm"))
const  ModifyBasicInformatioForm  = asyncComponent(() => import("./containers/recruiter/modifyForm/modifyBasicInformationForm/ModifyBasicInfromtionForm"))
const ModifyUsersForm = asyncComponent(() => import("./containers/recruiter/modifyForm/modifyUser/ModifyUser"))
const ModifyQuestionsForm = asyncComponent(() => import("./containers/recruiter/modifyForm/modifyQuestion/ModifyQuestion"))
const ListSolveFormsForRecruiter =  asyncComponent(() => import("./containers/recruiter/solveForm/listSolveForm/listSolveForm"))
const CheckSolveFormForRecruiter =  asyncComponent(() => import("./containers/recruiter/solveForm/checkSolveForm/CheckSolveForm"))
const TranslateFormsForRecruiter = asyncComponent(() => import("./containers/recruiter/translateForm/TranslateForm"))
const ImportExportFormsForRecruiter = asyncComponent(() => import("./containers/recruiter/importExportForm/ImportExportForm"))

export default ({ childProps }) =>
  <Switch>
    <AppliedRoute
      path="/"
      exact
      component={AsyncHome}
      props={childProps}
    />
    <AppliedRoute
      path="/verify_email"
      exact
      component={AsyncVerifyEmail}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel"
      exact
      component={AsyncRecruiterPanel}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/add_form"
      exact
      component={AsyncRecruiterAddForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/forms/:id/users"
      exact
      component={AsyncRecruiterAddUserToForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/forms/:id/questions"
      exact
      component={AsyncRecruiterAddQuestionToForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/forms/:id"
      exact
      component={MainRecruiterGetForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/modify/forms/:id"
      exact
      component={ModifyBasicInformatioForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/modify/forms/:id/users"
      exact
      component={ModifyUsersForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/modify/forms/:id/questions"
      exact
      component={ModifyQuestionsForm}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/solve/forms/:id"
      exact
      component={ListSolveFormsForRecruiter}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/solve/forms/check/:id"
      exact
      component={CheckSolveFormForRecruiter}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/translate/forms/:id"
      exact
      component={TranslateFormsForRecruiter}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel/import_export/forms/:id"
      exact
      component={ImportExportFormsForRecruiter}
      props={childProps}
    />
    <UserRoute
      path="/user_panel"
      exact
      component={AsyncUserPanel}
      props={childProps}
    />
    <UnauthenticatedRoute
      path="/signin"
      exact
      component={AsyncSignIn}
      props={childProps}
    />
    <UnauthenticatedRoute
      path="/signup"
      exact
      component={AsyncSignUp}
      props={childProps}
    />

    {/* 404 */}
    <Route component={AsyncNotFound} />
  </Switch>
;