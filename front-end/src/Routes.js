import React from "react";
import { Route, Switch } from "react-router-dom";
import asyncComponent from "./components/AsyncComponent";
import AppliedRoute from "./components/AppliedRoute";
import UnauthenticatedRoute from "./components/UnauthenticatedRoute";

const AsyncHome = asyncComponent(() => import("./containers/home/Home"));
const AsyncSignin = asyncComponent(() => import("./containers/page/signin/Signin"));
const AsyncNotFound = asyncComponent(() => import("./containers/error/NotFound"));
const AsyncSignup = asyncComponent(() => import("./containers/page/signup/Signup"));

export default ({ childProps }) =>
<Switch>
    <AppliedRoute
      path="/"
      exact
      component={AsyncHome}
      props={childProps}
    />
    <UnauthenticatedRoute
      path="/signin"
      exact
      component={AsyncSignin}
      props={childProps}
    />
    <UnauthenticatedRoute
      path="/signup"
      exact
      component={AsyncSignup}
      props={childProps}
    />
    {/*jezeleli nic nie znajdzie to wyswietl 404*/}
    <Route component={AsyncNotFound} />
</Switch>
;