import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { useHistory } from "react-router-dom";
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"


function Subscription() {
  const productService = new ProductService();
  const history = useHistory();

  

  const onButtunClick = (data, type) => {
  
    if (type === 'add') {
      const fs = productService.getDynamicFrom('subscription');
      let reqData = {
        actType: "add", type: "delivery",  formJson: fs,
        class: "col-sm-12", url: "/user/save-subscription-plans", pageurl: "/app/subscription", header: "Add Subscription"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    }
    else if (type === 'edit') {
      const fs = productService.getDynamicFrom('subscription');
      fs.name.value = data.name;
      fs.durationDays.value = data.duration_days;
      fs.amount.value = data.amount;
      fs.image.path = APP_URL + "/user/image?path=" + data.image;
      alert(JSON.stringify(data));
       let reqData = {
        data: data, actType: "edit", type: "delivery",  formJson: fs,
        class: "col-sm-12", url: "/user/update-subscription-plans", pageurl: "/app/subscription", header: "Edit Subscription"
        ,key:"subscription_plans_id" };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');

    }
  }

  return (
    <div className="content">
      <h4>Subscription Plans</h4>
     
       <DyanamicTable type="subscription" lazy={false} 
        url="/user/get-subscription-plans"
        onButtonClik={(e, type) => onButtunClick(e, type)} />

    </div>
  )
} export default Subscription;