import React, { Component } from "react";
//sluzy do dynamicznego importowania komponentow 
export default function asyncComponent(importComponent) {
  class AsyncComponent extends Component {
    constructor(props) {
      super(props);

      this.state = {
        component: null
      };
    }
    //zapisujemy komponent na stanie 
    async componentDidMount() {
      const { default: component } = await importComponent();

      this.setState({
        component: component
      });
    }
    //renderujemy komponent ze stanu | mozemy to wykorzystać do ładującej się zębatki
    render() {
      const C = this.state.component;

      return C ? <C {...this.props} /> : null;
    }
  }

  return AsyncComponent;
}
