import React, { useState, Component, useEffect, useRef } from 'react';
import DynamicForm from '../service/DynamicForm';
import ProductService from '../service/ProductService';
import './Home.css';
import APIService from "../service/APIService"
import { Toast } from 'primereact/toast';
import { useHistory } from "react-router-dom";
import APP_URL from "../service/APIConfig"
import useGeolocation from 'react-hook-geolocation'

function SellerDetails(props) {
    const productService = new ProductService();
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [formJson, setFormJson] = useState(productService.getDynamicFrom("sellerform"));
    const toast = useRef(null);
    const history = useHistory();
    const [location, setLocation] = useState(true);
    const onGeolocationUpdate = geolocation => {
        if (location) {
           
            let form = { ...formJson };
            if(!form.latitude.value)
               form.latitude.value = geolocation.latitude;
            if(!form.longitude.value)
               form.longitude.value = geolocation.longitude;
            setFormJson(form);
            setLocation(false);
        }
    }
    const geolocation = useGeolocation({
        enableHighAccuracy: true,
        maximumAge: 15000,
        timeout: 12000
    }, onGeolocationUpdate);

    useEffect(() => {

        window.scrollTo(0, 0);
        let fil = { user_id: userData.userId }
        APIService.postRequest('/user/seller-details', fil).then(data => {
            if (data) {
                let form = { ...formJson };
                let sd = data.data
                if(sd) {
               Object.keys(sd).map((key, ind) => {
                    if (form[key]) {
                        if (key === 'image')
                            form[key].path = APP_URL + "/user/image?path=" + sd[key];
                        else
                            form[key].value = sd[key];
                    }
                });
                setLocation(false);
            }
                setFormJson(form);



            }

        });

    }, []);


    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });
    }

    const backToPage = (url) => {
        localStorage.removeItem("reqData");
        history.push(url);
    }


    const saveData = (e) => {
        e.user_id = userData.userId;
        APIService.postRequest('/user/save-seller', e).then(data => {
            if (data.data.status === 'success') {
                growlMsg('info', data.data.reason);
            }
            else {
                growlMsg('error', data.data.reason);
            }
        });


    }





    return (
        <div className="content order_view">
            <Toast className="toast-demo" ref={toast} />
            <h4>Shop Details</h4>
            <DynamicForm type="sellerform" formJson={formJson} clname="col-sm-6" onSubmit={(e) => saveData(e)} />
        </div>
    )

}
export default SellerDetails;