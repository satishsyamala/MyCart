import React, { useState, Component, useEffect, useRef } from 'react';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import { useHistory } from "react-router-dom";
import './Home.css';
import GeoCoords from "../service/GeoCoords"
import { Toast } from 'primereact/toast';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"

function Users(props) {
  const productService = new ProductService();
  const [visible, setVisible] = useState(false);
  const geoCoords = new GeoCoords();
  const [formJson, setFormJson] = useState();
  const [actType, setActType] = useState();
  const [rowData, setRowData] = useState();
  const history = useHistory();
  const toast = useRef(null);
  const menu = props.match.url.replace('/app/', '');

  const growlMsg = (type, message) => {
    toast.current.show({ severity: type, summary: '', detail: message });
  }

  const getheaderName = () => {
    if (menu === 'sellers')
      return 'Sellers';
    else if (menu === 'adminusers')
      return 'Admin Users';
    else if (menu === 'consumers')
      return 'Consumers';
    else if (menu === 'sellerusers')
      return 'Users';
    else if (menu === 'admindelivery' || menu === 'sellerdelivery')
      return 'Delivery';

  }

  const getModuleName = () => {
    if (menu === 'sellers')
      return 'Sellers';
    else if (menu === 'adminusers')
      return 'AdminUsers';
    else if (menu === 'consumers')
      return 'Consumers';
    else if (menu === 'sellerusers')
      return 'SellerUsers';
    else if (menu === 'admindelivery' || menu === 'sellerdelivery')
      return 'DeliveryUsers';



  }

  const getUrl = () => {
    if (menu === 'sellers')
      return 'get-sellers';
    else if (menu === 'adminusers' || menu === 'admindelivery')
      return 'get-admin-user';
    else if (menu === 'consumers')
      return 'get-consumers';
    else if (menu === 'sellerusers' || menu === 'sellerdelivery')
      return 'get-seller-users';


  }




  const onButtunClick = (data, type) => {
    setActType(type);
    setRowData(data);

    if (type === 'add') {
      const fs = productService.getDynamicFrom(getModuleName());
      let reqData = {
        actType: "add", type: getModuleName(), formJson: fs,
        class: "col-sm-6", url: "/user/adduser", pageurl: '/app/' + menu, header: "Add User"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');

    }
    else if (type === 'edit') {
      const fs = productService.getDynamicFrom(getModuleName());
      fs.firstName.value = data.first_name;
      fs.lastName.value = data.last_name;
      fs.mobileNo.value = data.moblie_no;
      fs.email.value = data.email;
      fs.password = { hidde: true };
      fs.confirmpassword = { hidde: true };
      fs.image.path = APP_URL + "/user/image?path=" + data.image;
      let reqData = {
        data: data, actType: "edit", type: getModuleName(), formJson: fs, key: "user_id",
        class: "col-sm-6", url: "/user/updateuser", pageurl: "/app/" + menu, header: "Edit User"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    } else if (type === 'view') {
      let filter = {};

      if (getModuleName() === 'Sellers') {
        filter = { seller_id: data.seller_id, module_name: 'seller' };
      }
      else {
        filter = { user_id: data.user_id, module_name: 'user' };
      }
      let reqData = {
        type: getModuleName(), filter: filter, image: true,
        class: "col-sm-6", url: "/user/get-view-data", pageurl: "/app/" + menu, header: "User View"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/viewinfo');
    } else if (type === 'users') {
      let filter = { seller_id: data.seller_id, module_name: 'seller_users' };
      let reqData = {
        type: getModuleName(), filter: filter, image: true, lazy: true,
        class: "col-sm-6", url: "/user/get-table-data", pageurl: "/app/" + menu, header: data.shop_name + " Users"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/tableinfo');
    } else if (type === 'stocks') {
      let filter = { seller_id: data.seller_id, module_name: 'seller_stock' };
      let reqData = {
        type: getModuleName(), filter: filter, image: true, lazy: true,
        class: "col-sm-6", url: "/user/get-table-data", pageurl: "/app/" + menu, header: data.shop_name + " Stocks"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/tableinfo');
    } else if (type === 'address') {
      let filter = { user_id: data.user_id, module_name: 'address' };
      let reqData = {
        type: getModuleName(), filter: filter, image: true, lazy: true,
        class: "col-sm-6", url: "/user/get-table-data", pageurl: "/app/" + menu, header: data.shop_name + " Delivery Address"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/tableinfo');
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
      <h4>{getheaderName()} </h4>
      {
        <DyanamicTable type={getModuleName()} lazy={false}
          url={"/user/" + getUrl()}
          onButtonClik={(e, type) => onButtunClick(e, type)} />
      }
    </div>
  )

}
export default Users;