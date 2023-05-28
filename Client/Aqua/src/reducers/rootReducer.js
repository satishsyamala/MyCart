import characterReducer from './characterReducer';
import counterReducer from './counterReducer';
import todoReducer from './todoReducer';
import CartReducer from './CartReducer';
import {combineReducers} from 'redux';

//Combine all the sub reducers
const rootReducer = combineReducers({
    characters: characterReducer,
    myCounter: counterReducer,
    todos:todoReducer,
    cart:CartReducer

})

export default rootReducer