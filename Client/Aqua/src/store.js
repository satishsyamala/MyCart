import {createStore} from 'redux';
import rootReducer from './reducers/rootReducer';
const store = createStore(rootReducer,loadState());
store.subscribe(()=>{
    localStorage.setItem("shopping-cart",JSON.stringify(store.getState()));
});
function checkLocalStorage(){
    //console.log("localstorage..."+localStorage.getItem("bug-tracker-app"));
    if(localStorage.getItem("shopping-cart")===undefined || localStorage.getItem("shopping-cart")===null)
    localStorage.setItem('shopping-cart',JSON.stringify({"user":{"error":"","isLoggedIn":false},"cartCount":0,"items":[]}));
}
function loadState(){
    checkLocalStorage();
   return JSON.parse(localStorage.getItem("shopping-cart"));

}


export default store;