import React, { useState, Component, useEffect, useRef } from 'react';
import DynamicForm from '../service/DynamicForm';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from '../service/ProductService';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import './Home.css';
import APIService from "../service/APIService"
import { Toast } from 'primereact/toast';
import { useHistory } from "react-router-dom";
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputNumber } from 'primereact/inputnumber';
import { Checkbox } from 'primereact/checkbox';

function AddOrEditForm(props) {
    const productService = new ProductService();
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [reqData, setReqData] = useState(JSON.parse(APIService.getLocalStorage('reqData')));
    const [visible, setVisible] = useState(false);
    const [formJson, setFormJson] = useState(reqData.formJson ? reqData.formJson : productService.getDynamicFrom(reqData.type));
    const [actType, setActType] = useState(reqData.actType);
    const toast = useRef(null);
    const [stockPrice, setStockPrice] = useState(false);
    const objectDB = useIndexedDB(reqData.table);;
    const history = useHistory();
    const [stockJson, setStockJson] = useState();

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

    const saveStockData = () => {

        APIService.postRequest(reqData.url, stockJson).then(data => {
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
    }

    const registerUser = (e) => {

        if (actType === 'add') {
            let check = validation(e);
            if (check === 'success') {

                if (userData) {
                    e.userId = { type: "number", value: userData.userId };
                    e.seller_id = { type: "number", value: userData.seller_id };
                    e.createdBy = { type: "text", value: userData.name };
                    e.createdOn = { type: "calender", value: APIService.getCurrentDate() };
                }

                if (reqData.type === 'SellerUsers')
                    e.userType = { type: "text", value: 'Seller' };
                else if (reqData.type === 'AdminUsers')
                    e.userType = { type: "text", value: 'Admin' };
                else if (reqData.type === 'DeliveryUsers') {
                    if (userData.seller_id > 0) {
                        e.userType = { type: "text", value: 'Seller' };
                        e.deliveryType = { type: "text", value: 'Seller' };
                    }
                    else {
                        e.userType = { type: "text", value: 'Admin' };
                        e.deliveryType = { type: "text", value: 'Admin' };
                    }
                }

                if (reqData.type !== 'stock') {
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
                } else {
                    setStockJson(e);
                    setStockPrice(true);
                }
            } else {
                growlMsg('error', check);
            }
        } else if (actType === 'edit') {
            let check = validation(e);
            if (check === 'success') {
                e.userId = { type: "number", value: userData.userId };
                e.seller_id = { type: "number", value: userData.seller_id };
                e.updatedBy = { type: "text", value: userData.name };
                e.updatedOn = { type: "calender", value: APIService.getCurrentDate() };
                e[reqData.key] = { type: "number", value: reqData.data[reqData.key] };
                if (reqData.type !== 'stock') {
                    APIService.postRequest(reqData.url, e).then(data => {
                        let del = data.data;
                        if (del.reason === 'success') {
                            if (reqData.table) {
                                if (reqData.type === 'updateprofile') {

                                    objectDB.getByIndex('userId', del.userId).then(user => {
                                        user.email = del.email;
                                        user.mobileNo = del.mobileNo;
                                        user.userType = del.userType
                                        user.image = del.image;
                                        objectDB.update(user);
                                        APIService.setLocalStorage('myData', JSON.stringify(user));

                                    })
                                } else {
                                    if (reqData.data.id) {
                                        objectDB.deleteRecord(reqData.data.id);
                                        objectDB.add(del);
                                    }

                                }
                            }
                            setVisible(true);

                        }
                        else {
                            growlMsg('error', del.reason);
                        }
                    });
                }
                else {
                  
                    if (reqData.editpac) {
                        for (var i = 0; i < e.packSize.value.length; i++) {
                            for (var k = 0; k < reqData.editpac.length; k++) {
                                if (reqData.editpac[k].key === e.packSize.value[i].key) {
                                    e.packSize.value[i].price = reqData.editpac[k].price;
                                    e.packSize.value[i].default_pack = reqData.editpac[k].default_pack;
                                }
                            }
                        }
                    }

                    setStockJson(e);
                    setStockPrice(true);
                }
            } else {
                growlMsg('error', check);
            }

        }

    }

    const onHide = (e) => {
        setVisible(false);
    }

    const textElement = (rowData) => {
        return <InputNumber className="tableinputtext"
            value={rowData.price} onChange={(e) => (rowData.price = e.value)} />;
    }

    const setChecked = (chek, rowData) => {
        let temp = { ...stockJson };
        if (chek) {
            let ar = stockJson.packSize.value;
            for (var i = 0; i < ar.length; i++) {
                ar[i].default_pack = 0;

            }

        }
        rowData.default_pack = chek ? 1 : 0;
        setStockJson(temp);

    }

    const checkBoxEle = (rowData) => {
        return <Checkbox inputId="binary" checked={rowData.default_pack === 1} onChange={e => setChecked(e.checked, rowData)} />
    }


    return (
        <div className="content order_view">
            <Toast className="toast-demo" ref={toast} />
            <div style={{ width: "100%", maxWidth: [reqData.class === 'col-sm-12' ? "400px" : "800px"], margin: "auto" }}>
                <h4>{reqData.header}
                    <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                        <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning "
                            label='Back' onClick={e => backToPage(reqData.pageurl)} />
                    </div></h4>
                <DynamicForm type={reqData.type} formJson={formJson} clname={reqData.class ? reqData.class : "col-sm-6"} onSubmit={(e) => registerUser(e)} />
            </div>


            <Dialog closable={false} className="register_dl" visible={visible} >
                <h6>Data saved Successfuly</h6>
                <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Back'
                    onClick={e => backToPage(reqData.pageurl)} />
            </Dialog>


            <Dialog header="Stock Price" visible={stockPrice} onHide={(e) => setStockPrice(false)}>
                <div className="card">
                    {stockJson && <div>
                        <DataTable value={stockJson.packSize.value}  responsiveLayout="scroll">
                            <Column field="key" header="Code"></Column>
                            <Column field="name" header="Name"></Column>
                            <Column className="numberColWith" header="Price" body={textElement}></Column>
                            <Column body={checkBoxEle} header="Default"></Column>
                        </DataTable>
                        <center>
                            <Button style={{ height: '30px' }} className="p-button-raised p-button-info " label='Save'
                                onClick={e => saveStockData()} /> </center>  </div>
                    }
                </div>
            </Dialog>
        </div>
    )

}
export default AddOrEditForm;