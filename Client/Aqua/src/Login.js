import React, { useState, useRef, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import PrimeReact from 'primereact/api';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import { useHistory } from "react-router-dom";
import './login.css';
import { Dialog } from 'primereact/dialog';
import DynamicForm from './service/DynamicForm';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from './service/ProductService';
import { connect } from 'react-redux';
import { withRouter } from "react-router-dom";
import * as actionTypes from './actiontype';
import APIService from "./service/APIService"
import Moment from 'moment';
import loading from './images/R.gif';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
var CryptoJS = require("crypto-js");






function Login(props) {
    const toast = useRef(null);
    const history = useHistory();
    const productService = new ProductService();
    PrimeReact.ripple = true;
    const [showLogin, setShowLogin] = useState(false);
    const userDB = useIndexedDB('users');
    const [visible, setVisible] = useState(false);
    const [userInfo, setUserInfo] = useState();
    const [passChange, setPassChange] = useState(false);
    const [forgotpass,setForgotPass] = useState(false);
    var CryptoJS = require("crypto-js");

    useEffect(() => {
     
        let us = APIService.getLocalStorage('myData');
        if (us) {
            let user = JSON.parse(APIService.getLocalStorage('myData'));

            if (user.savepass) {
                if (user.userType === 'Admin')
                    history.push('/app/dashboard');
                else if (user.userType === 'Seller')
                    history.push('/app/dashboard');
                else
                    history.push('/app/home');
            } else {
                setShowLogin(true);
            }
        }
        else {
            setShowLogin(true);
        }
    }, []);







    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }
    const loginValidation = (e) => {
        let req = { user_name: e.name.value, password: e.password.value }
        APIService.postRequest('/user/login-check', req).then(res => {
            let userdata = res.data;
            if (userdata && userdata.status === 'active') {
                setUserInfo(userdata);
                APIService.setLocalStorage('token',userdata.token);
                let toketime={tokedate:new Date(),session_time:30};
                APIService.setJSONLocalStorage('toke-time',toketime);
                if (userdata.firstLogin === 1) {
                    userdata.savepass = e.savepass.value;
                    APIService.setLocalStorage('myData', JSON.stringify(userdata));
                    APIService.setLocalStorage('inputs', JSON.stringify({}));
                    if (userdata.delivery_type === 'no') {
                        let menuItems = productService.getMenuItems(userdata.userType);
                        APIService.setLocalStorage('MenuItems', JSON.stringify(menuItems));
                    }
                    else {
                        let menuItems = productService.getMenuItemsDelivery(userdata.delivery_type);
                        APIService.setLocalStorage('MenuItems', JSON.stringify(menuItems));
                    }
                    userDB.clear();
                    userDB.add(userdata);
                    history.push('/sync');
                } else {
                    setPassChange(true);
                }
            } else {
                growlMsg('error', userdata.status);
            }
        }).catch(function (error) {
            growlMsg('error', error.message);
        });



    }






    const selectoption = () => {
        setVisible(true);
    }

    const register = (type) => {
        let reqData = { actType: "add", usertype: type, type: "register", url: "/user/adduser", pageurl: "/", header: "Sign Up" };
        APIService.setLocalStorage('reqData', JSON.stringify(reqData));
        history.push('/signeup');
    }


    const onHidde = () => {
        setVisible(false);
        setPassChange(false);
    }

    const changePassword = (e) => {
        if (e.confpassword.value === e.password.value) {
            let req = { user_id: userInfo.userId, old_pass: e.oldpassword.value, new_pass: e.password.value };
            APIService.postRequest('/user/change-password', req).then(res => {
                if (res.data.reason === 'success') {
                    growlMsg('info', 'Password changed please re-login');
                    onHidde();
                }
                else {
                    growlMsg('error', res.data.reason);
                }
            });
        } else {
            growlMsg('error', "Password and confirm password should be same");
        }
    }

    const forgotPassword =(e)=>{
        let req = { login_id: e.emailormobile.value};
        APIService.postRequest('/user/forgot-password', req).then(res => {
            if (res.data.reason === 'success') {
                growlMsg('info', 'Password changed please re-login');
                setForgotPass(false);
            }
            else {
                growlMsg('error', res.data.reason);
            }
        });
    }



    return (<div>
        <Toast className="toast-demo" ref={toast} />

        <br /> <br /> <br /> <br /> <br />
        {
            showLogin ?
                <div class="container h-100">
                    <div class="d-flex justify-content-center h-100">
                        <div class="user_card">
                            <div class="d-flex justify-content-center">
                                <div class="brand_logo_container">
                                    <img src={process.env.PUBLIC_URL + "logo3.png"} class="brand_logo" alt="Logo" />
                                </div>
                            </div>
                            <div class="d-flex justify-content-center form_container">
                                <DynamicForm type="login" clname="col-sm-12" onSubmit={(e) => loginValidation(e)} />
                            </div>

                            <div >
                                <div class="d-flex justify-content-center links">
                                    Don't have an account? <a href="#" onClick={selectoption} class="ml-2">Sign Up</a>
                                </div>
                                <div class="d-flex justify-content-center links">
                                    <a href="#" onClick={(e)=>setForgotPass(true)}>Forgot your password?</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div> : <img src={loading}></img>
        }
        <Dialog  header="Sign Up" className="register_dl" visible={visible} onHide={(e) => onHidde()} >
            <div >
                <Divider align="center" type="dashed">
                    <b>Users</b>
                </Divider>
                <Button style={{ height: '40px', width: "200px" }} label='Consumer' onClick={e => register('Personal')} /><br /><br />
                <Button style={{ height: '40px', width: "200px", display: "none" }} label='Retailer' onClick={e => register('Retailer')} />
                <Divider align="center" type="dashed">

                </Divider>
                <Button style={{ height: '40px', width: "200px" }} label='Seller' onClick={e => register('Seller')} />
            </div>
        </Dialog>

        <Dialog  header="Change Password" className="register_dl" visible={passChange} onHide={(e) => onHidde()} >
            <DynamicForm type="changepassword" clname="col-sm-12" onSubmit={(e) => changePassword(e)} />
        </Dialog>

        <Dialog className="forgot-password" header="Forgot Password"  visible={forgotpass}  onHide={(e) => setForgotPass(false)} >
            <DynamicForm type="forgotpassword" clname="col-sm-12" onSubmit={(e) => forgotPassword(e)} />
        </Dialog>
    </div>
    );
}

export default Login;



