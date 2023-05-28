import React, { useState, Component, useEffect, useRef } from 'react';
import DynamicForm from './service/DynamicForm';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from './service/ProductService';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import './components/Home.css';
import APIService from "./service/APIService"
import { Toast } from 'primereact/toast';
import { useHistory } from "react-router-dom";

function SingeUp(props) {
    const productService = new ProductService();
    const [reqData, setReqData] = useState(JSON.parse(APIService.getLocalStorage('reqData')));
    const [visible, setVisible] = useState(false);
    const [formJson, setFormJson] = useState(reqData.formJson ? reqData.formJson : productService.getDynamicFrom(reqData.type));
    const [actType, setActType] = useState(reqData.actType);
    const toast = useRef(null);
    const objectDB = useIndexedDB(reqData.table);;
    const history = useHistory();

    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }



    const backToPage = (url) => {

        localStorage.removeItem("reqData");
        onHide();
        history.push(url);
    }




    const validation = (e) => {
        let res = 'success';
        if (reqData.type === 'register') {
            if (e.password.value !== e.confirmpassword.value) {
                res = 'Password and Confirm Password are not matching';
            }
        }
        return res;
    }

    const registerUser = (e) => {

        if (actType === 'add') {
            e.userType={type:'text',value:reqData.usertype}
            APIService.postRequest(reqData.url, e).then(data => {
                let del = data.data;
                if (del.reason === 'success') {
                    if (reqData.table)
                        objectDB.add(del);
                    setVisible(true);
                }
                else {
                    growlMsg('error', del.reason);
                }
            });

        } else if (actType === 'edit') {
            APIService.postRequest(reqData.url, e).then(data => {
                let del = data.data;
                if (del.reason === 'success') {
                    if (reqData.table) {
                        if (reqData.type === 'updateprofile') {

                            objectDB.getByIndex('user_id', del.user_id).then(user => {
                                user.email = del.email;
                                user.mobileNo = del.mobile_no;
                                user.userType = del.user_type
                                user.image = del.image;
                                objectDB.update(user);
                                localStorage.setItem('myData', JSON.stringify(user));

                            })
                        } else {
                            objectDB.deleteRecord(reqData.data.id);
                            objectDB.add(del);
                        }
                    }
                    setVisible(true);

                }
                else {
                    growlMsg('error', del.reason);
                }
            });


        }

    }

    const onHide = (e) => {
        setVisible(false);
    }



    return (
        <div className="order_view">
            <Toast className="toast-demo" ref={toast} />
            <div style={{ width: "100%", maxWidth: "360px", margin: "auto" }}>
                <h4>{reqData.header}
                    <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                        <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning "
                            label='Back' onClick={e => backToPage(reqData.pageurl)} />
                    </div></h4>
                <DynamicForm type={reqData.type} formJson={formJson} clname={reqData.class ? reqData.class : "col-sm-12"} onSubmit={(e) => registerUser(e)} />
              <br/>
           </div>


            <Dialog closable={false} className="register_dl" visible={visible} >
                <h6>Password sent to your mail</h6>
                <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Back'
                    onClick={e => backToPage(reqData.pageurl)} />
            </Dialog>
        </div>
    )

}
export default SingeUp;