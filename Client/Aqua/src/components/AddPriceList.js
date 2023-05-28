import React, { useState, Component, useEffect, useRef } from 'react';
import DynamicForm from '../service/DynamicForm';
import DyanamicTable from '../service/DyanamicTable';
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
import { ScrollTop } from 'primereact/scrolltop';
function AddPriceList(props) {
    const productService = new ProductService();
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [reqData, setReqData] = useState(JSON.parse(APIService.getLocalStorage('reqData')));
    const [tableBtn, setTableBtn] = useState(productService.getTableBtnDetais('pricelistadd'));
    const [visible, setVisible] = useState(false);
    const [formJson, setFormJson] = useState(reqData.formJson ? reqData.formJson : productService.getDynamicFrom(reqData.type));
    const [actType, setActType] = useState(reqData.actType);
    const toast = useRef(null);
    const objectDB = useIndexedDB(reqData.table);
    const [addTableData, setAddTableDate] = useState([]);
    const history = useHistory();
    const [tableHeader, setTableHeader] = useState(productService.getDynamicTableHeader('pricelistadd'));

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
            if (addTableData && addTableData.length > 0) {
                e.userId = { type: "number", value: userData.userId };
                e.sellerId = { type: "number", value: userData.seller_id };
                e.createdBy = { type: "text", value: userData.name };
                e.createdOn = { type: "calender", value: APIService.getCurrentDate() };
                e.items = addTableData;
                APIService.postRequest(reqData.url, e).then(data => {
                    let del = data.data;
                    if (del.reason === 'success') {
                        setVisible(true);
                    }else {
                        growlMsg('error', del.reason);
                    }
                });
            } else {
                growlMsg('error', "Please add items");
            }
        } else if (actType === 'edit') {
            let check = validation(e);
            if (check === 'success') {
                e.userId = { type: "number", value: userData.userId };
                e.seller_id = { type: "number", value: userData.seller_id };
                e.updatedBy = { type: "text", value: userData.name };
                e.updatedOn = { type: "calender", value: APIService.getCurrentDate() };
                e[reqData.key] = { type: "number", value: reqData.data[reqData.key] };
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
            } else {
                growlMsg('error', check);
            }

        }

    }

    const onHide = (e) => {
        setVisible(false);
    }

    const onButtunClick = (row, type) => {
        if (type === 'add to table') {
          
                let temp = [...addTableData];
                temp.push(row);
                setAddTableDate(temp);
                console.log(JSON.stringify(temp));
           
        }
    }

    const onRemoveBtnSubmit = (rowData) => {
        const obj = [...addTableData];
        var index = obj.findIndex(e => e.stockItemId = rowData.stockItemId);
        if (index != -1) {
            obj.splice(index, 1);

        } else {
            console.log("Item not found in appGlobal.ObjProduct");
        }
        setAddTableDate(obj);
    }

    const setRemoveButton = (rowData) => {
        return <Button type="submit" onClick={(e) => onRemoveBtnSubmit(rowData)} icon="pi pi-times" className="p-button-rounded p-button-danger p-button-text" />
    }

    const dynamicColumns = addTableData && Object.keys(tableHeader).map((key, ind) => {
        if (tableHeader[key].type === 'removebutton')
            return <Column className="tableBtn" style={{ width: "80px!important" }} body={setRemoveButton}></Column>
        else
            return <Column key={key} className={"column-" + tableHeader[key].type} field={key} header={tableHeader[key].name} />;
    });

    return (
        <div >
            <Toast className="toast-demo" ref={toast} />
            <div>
                <h4>{reqData.header}
                    <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                        <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning "
                            label='Back' onClick={e => backToPage(reqData.pageurl)} />
                    </div></h4>
                <DynamicForm type={reqData.type} buttonsTop={true} formJson={formJson} clname={reqData.class ? reqData.class : "col-sm-4"} onSubmit={(e) => registerUser(e)} />

                {addTableData &&
                    <DataTable className="DTtable" value={addTableData}>
                        {dynamicColumns}
                    </DataTable>}

                <DyanamicTable type="pricelistmap" lazy={true} pageSize={250}
                    url="/product/get-stock-seller"
                    onButtonClik={(e, type) => onButtunClick(e, type)} />
            </div>


            <Dialog closable={false} className="register_dl" visible={visible} >
                <h6>Data saved Successfuly</h6>
                <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Back'
                    onClick={e => backToPage(reqData.pageurl)} />
            </Dialog>
            <ScrollTop icon="pi pi-angle-double-up" />
        </div>
    )

}
export default AddPriceList;