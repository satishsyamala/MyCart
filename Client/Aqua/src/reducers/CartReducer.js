
import * as actionTypes from '../actiontype';
const CartReducer=(state={'cartCount':0,'pageName':''},action)=>{
    switch(action.type)
    {
        case actionTypes.UPDATE_CART:
                 return {
                     ...state,
                   'cartCount':action.length
                 }
        case actionTypes.PAGE_HEADER:
            return {
                ...state,
                'pageName':action.pagename
            }         
             default:
                return state;
    }
}

export default CartReducer
