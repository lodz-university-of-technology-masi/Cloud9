import React from "react";
import { Route, Redirect } from "react-router-dom";

function checkUser(obj) {
    if (obj === false) {
        return false;
    }
    else{
        if(obj.attributes.profile === "user")
            return true;
        else
            return false;
    }
}

export default ({ component: C, props: cProps, ...rest }) => {
    return (
      <Route
            {...rest}
            render={props =>
                checkUser(cProps.user)
                ? <C {...props} {...cProps} />
                : <Redirect
                    to={`/signin?redirect=${props.location.pathname}${props.location.search}`}
                />}
        />
    );
  };



