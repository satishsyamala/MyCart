import React, { useState,useEffect } from 'react';
import { TabView, TabPanel } from 'primereact/tabview';
import { Button } from 'primereact/button';
import DynamicForm from '../service/DynamicForm';
import './Home.css';
import ProductService from '../service/ProductService';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"

function Settings(props) {
    const [activeIndex, setActiveIndex] = useState(1);
    const productService = new ProductService();
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [general, setGeneral] = useState();
    const [delivery, setDelivery] = useState();

    useEffect(() => {
      
        APIService.postRequest('/user/get-general-settings', { sellerId: userData.seller_id }).then(data => {
             if (data.data) {
                let js = productService.getDynamicFrom('gensettings');
                js.distanceKm.value = data.data.distanceKm;
                js.minOrderValue.value = data.data.minOrderValue;
                js.minDeliveryDays.value = data.data.minDeliveryDays;
                js.paymentModes.value = data.data.paymentModes;
               
                setGeneral(js);
            }
            else{
                setGeneral(productService.getDynamicFrom('gensettings'));
            }
        });

        APIService.postRequest('/user/get-delivery-charges', { sellerId: userData.seller_id }).then(data => {
            if (data.data) {
                let js = productService.getDynamicFrom('deliverycharge');
                js.minDeliveryCharge.value = data.data.minDeliveryCharge;
                js.maxDeliveryCharge.value = data.data.maxDeliveryCharge;
                js.maxValueForFreeDelivery.value = data.data.maxValueForFreeDelivery;
                js.minDistForMinCharge.value = data.data.minDistForMinCharge;
                js.deliveryChargeForKm.value = data.data.deliveryChargeForKm;
                setDelivery(js);
            }else{
                setDelivery(productService.getDynamicFrom('deliverycharge'));
            }
        });

    }, []);


    const generalSubmit = (e) => {
        e.sellerId=userData.seller_id;
        APIService.postRequest('/user/save-general-settings', e).then(data => {
            if (data.data.reason==='success') {
                alert("Data Saved");
            } else{
                alert(data.data.reason);
            }
        });
    }

    const deliveryCharges = (e) => {
        e.sellerId=userData.seller_id;
        APIService.postRequest('/user/save-delivery-charges', e).then(data => {
            if (data.data.reason==='success') {
               alert("Data Saved");
                } else{
                alert(data.data.reason);
            }
        });
    }

    return (
        <div className="content order_view">

            <h5>Settings</h5>
            <TabView>
                <TabPanel header="General">
               { general &&  <DynamicForm type="gensettings" formJson={general} clname="col-sm-6" onSubmit={(e) => generalSubmit(e)} /> }
                </TabPanel>
                <TabPanel header="Delivery Charges">
                  {delivery &&  <DynamicForm type="deliverycharge" formJson={delivery} clname="col-sm-6" onSubmit={(e) => deliveryCharges(e)} /> }
                </TabPanel>

            </TabView>


        </div>
    )
} export default Settings;