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
import APP_URL from "../service/APIConfig"

function ViewInfo(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [reqData, setReqData] = useState(JSON.parse(APIService.getLocalStorage('reqData')));
    const [viewData, setViewData] = useState();
    const history = useHistory();

    useEffect(() => {
        APIService.postRequest(reqData.url, reqData.filter).then(res => {
            setViewData(res.data);
        });
    }, []);



    const backToPage = (url) => {
        localStorage.removeItem("reqData");
        history.push(url);
    }



    return (
        <div className="content order_view">
            <h4>{reqData.header}
                <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                    <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning "
                        label='Back' onClick={e => backToPage(reqData.pageurl)} />
                </div></h4>
            <div className="card" style={{ width: "100%",padding:"30px", maxWidth: [reqData.class === 'col-sm-12' ? "400px" : "800px"], margin: "auto" }}>
                {
                    viewData && viewData.map((item) => (
                        item.key!=='image'?  <div >
                            <span className="dev-product-desc">{item.key}</span><br/>
                            &nbsp;&nbsp;&nbsp;<span className="dev-product-name">{item.value}</span>
                            <br/><br/>
                        </div>:<div>
                        <span className="dev-product-desc">{item.key}</span><br/>
                            <img style={{width:"300px",height:"250px"}} src={APP_URL + "/user/image?path=" + item.value}
                            onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'}  />
                        </div>  
                        
                    ))
                }
            </div>
        </div>
    )

}
export default ViewInfo;