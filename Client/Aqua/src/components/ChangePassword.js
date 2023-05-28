import React, { useState, useRef } from 'react';
import { Toast } from 'primereact/toast';
import PrimeReact from 'primereact/api';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import { useHistory } from "react-router-dom";
import '../App.css';
import DynamicForm from '../service/DynamicForm';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from '../service/ProductService';
import APIService from "../service/APIService"



function ChangePassword(props) {
    const productService = new ProductService();
    const toast = useRef(null);
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));


    //DB Connection
    const userDB = useIndexedDB('users');


    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });
    }

    const changePassword = (e) => {
        if (e.confpassword.value === e.password.value) {
            let req = { user_id: userData.userId, old_pass: e.oldpassword.value, new_pass: e.password.value };
            APIService.postRequest('/user/change-password', req).then(res => {
                if (res.data.reason === 'success') {
                    growlMsg('info', 'Password changed');
                    
                }
                else {
                    growlMsg('error', res.data.reason);
                }
            });
        } else {
            growlMsg('error', "Password and confirm password should be same");
        }
    }




    return (
        <div className="content">
            <Toast className="toast-demo" ref={toast} />
            <h4>Change Password</h4><br />
            <div className='smallForm'>
                <DynamicForm type="changepassword" clname="col-sm-12" onSubmit={(e) => changePassword(e)} />
            </div>
        </div>

    );
}

export default ChangePassword;
