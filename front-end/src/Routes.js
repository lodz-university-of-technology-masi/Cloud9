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
const AsyncRecruiterAddForm = asyncComponent(() => import("./containers/recruiter/addform/AddForm"));

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