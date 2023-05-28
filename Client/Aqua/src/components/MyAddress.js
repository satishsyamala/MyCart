import React, { useState, Component, useEffect, useRef } from 'react';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import { useHistory } from "react-router-dom";
import './Home.css';
import GeoCoords from "../service/GeoCoords"
import { Toast } from 'primereact/toast';
import APIService from "../service/APIService"

function MyAddress(props) {
  const productService = new ProductService();
  const [visible, setVisible] = useState(false);
  const geoCoords = new GeoCoords();
  const [formJson, setFormJson] = useState();
  const [actType, setActType] = useState();
  const [rowData, setRowData] = useState();
  const history = useHistory();
  const toast = useRef(null);


  const growlMsg = (type, message) => {
    toast.current.show({ severity: type, summary: '', detail: message });

  }




  const onButtunClick = (data, type) => {
    setActType(type);
    setRowData(data);
    if (type === 'add') {
      const fs = productService.getDynamicFrom('delivery');
      fs.latitude.value = geoCoords.getLatitude();
      fs.longitude.value = geoCoords.getLongitude();
      let reqData = {
        actType: "add", type: "delivery", table: "delivery_address", formJson: fs,
        class: "col-sm-6", url: "/user/add-address", pageurl: "/app/myaddress", header: "Add Address"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');

    }
    else if (type === 'edit') {
      const fs = productService.getDynamicFrom('delivery');
      fs.address.value = data.address;
      fs.state.value = data.state;
      fs.city.value = data.city;
      fs.pinCode.value = data.pinCode;
      fs.defaultDel.value=data.defaultDel;
      if (data.latitude)
        fs.latitude.value = data.latitude;
      else
        fs.latitude.value = geoCoords.getLatitude();
      if (data.longitude)
        fs.longitude.value = data.longitude;
      else
        fs.longitude.value = geoCoords.getLongitude();
        let reqData = {
        data: data, actType: "edit", type: "delivery", table: "delivery_address", formJson: fs,
        class: "col-sm-6", url: "/user/update-address", pageurl: "/app/myaddress", header: "Edit Address", key: "delveryAddressId"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    }else if (type==='orders')
    {
      APIService.setLocalStorage('delivery_address_id', data.delveryAddressId);
      history.push('/app/myorders');
    }

  }

  const onHide = (e) => {
    setVisible(false);
  }

  const deleteData = (e) => {
    alert(JSON.stringify(formJson));
  }

  return (
    <div className="content">
      <Toast className="toast-demo" ref={toast} />
      <h4>Delivery Address </h4>
      {

        <DyanamicTable type="delivery" lazy={false}
          url="/user/get-address"
          onButtonClik={(e, type) => onButtunClick(e, type)} />
      }

    </div>
  )

}
export default MyAddress;