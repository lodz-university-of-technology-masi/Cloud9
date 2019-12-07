import React from "react";
import { Route, Redirect } from "react-router-dom";

function checkRecruiter(obj) {
    if (obj === false) {
        return false;
    }
    else{
        if(obj.attributes.profile === "recruiter")
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
                checkRecruiter(cProps.user)
                ? <C {...props} {...cProps} />
                : <Redirect
                    to={`/signin?redirect=${props.location.pathname}${props.location.search}`}
                />}
        />
    );
  };



