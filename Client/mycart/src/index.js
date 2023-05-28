import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import RouterLinks from './RouterLinks';
import { Provider } from 'react-redux';
import rootReducer from './reducers/rootReducer';
import { BrowserRouter } from 'react-router-dom';
import { createStore } from 'redux';
import store from './store';
import ErrorBoundry from './ErrorBoundary';

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <ErrorBoundry>
                <RouterLinks />
            </ErrorBoundry>
        </BrowserRouter>
    </Provider>
    , document.getElementById('root')
);



