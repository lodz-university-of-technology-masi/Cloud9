import React from "react";
import { Route, Switch } from "react-router-dom";
import asyncComponent from "./components/AsyncComponent";
import AppliedRoute from "./components/AppliedRoute";
import UnauthenticatedRoute from "./components/UnauthenticatedRoute";
//import AuthenticatedRoute from "./components/AuthenticatedRoute";
import RecruiterRoute from "./components/RecruiterRoute";
import UserRoute from "./components/UserRoute";

const AsyncHome = asyncComponent(() => import("./containers/home/Home"));
const AsyncSignin = asyncComponent(() => import("./containers/page/signin/Signin"));
const AsyncSignup = asyncComponent(() => import("./containers/page/signup/Signup"));
const AsyncNotFound = asyncComponent(() => import("./containers/error/NotFound"));
const AsyncRecruiterPanel = asyncComponent(() => import("./containers/recruiter/main/Main"));
const AsyncUserPanel = asyncComponent(() => import("./containers/user/main/Main"));

export default ({ childProps }) =>
  <Switch>
    <AppliedRoute
      path="/"
      exact
      component={AsyncHome}
      props={childProps}
    />
     <AppliedRoute
      path="/signin"
      exact
      component={AsyncSignin}
      props={childProps}
    />
    <AppliedRoute
      path="/signup"
      exact
      component={AsyncSignup}
      props={childProps}
    />
    <RecruiterRoute
      path="/recruiter_panel"
      exact
      component={AsyncRecruiterPanel}
      props={childProps}
    />
    <UserRoute
      path="/user_panel"
      exact
      component={AsyncUserPanel}
      props={childProps}
    />
    <Route component={AsyncNotFound} />
  </Switch>
;
