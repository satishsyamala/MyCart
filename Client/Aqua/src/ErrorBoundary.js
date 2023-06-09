import React, { Component } from 'react';

class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  componentDidCatch(error, info) {
    // Display fallback UI
    this.setState({ hasError: true });
    // ;
  }

  loginpage()
  {
    this.props.history.push('/')
  }

  render() {
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return <h1 onClick={(e)=>this.loginpage()}>Something went wrong.</h1>;
    } else
      return this.props.children;
  }
} export default ErrorBoundary;