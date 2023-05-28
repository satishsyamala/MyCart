import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"
import { useHistory } from "react-router-dom";
import { Toast } from 'primereact/toast';

function Offers(props) {
  const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
  const productService = new ProductService();
  const history = useHistory();
  const menu = props.match.url.replace('/app/', '');
  const toast = useRef(null);


  const growlMsg = (type, message) => {
    toast.current.show({ severity: type, summary: '', detail: message });
  }

  const onButtunClick = (rowData, type) => {
    const fs = productService.getDynamicFrom('pricelist');
    let reqData = {};
    if (type === 'add') {
      reqData = {
        actType: "add", type: "pricelist",  formJson: fs,
        class: "col-sm-4", url: "/price/save-price-list", pageurl: "/app/"+menu, header: "Add Offer"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addpricelist');
    } else if (type === 'edit') {
     
    } else if (type === 'view') {
      let filter = { stock_item_id: rowData.stockItemId, module_name: 'stock_item' };
      let reqData = {
        filter: filter, image: true,
        class: "col-sm-6", url: "/user/get-view-data", pageurl: "/app/"+menu, header: "Stock View"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/viewinfo');
    }



  }

 

  return (
    <div className="content">
      <h4>Offers</h4>
      <Toast className="toast-demo" ref={toast} />
      <DyanamicTable type="pricelist" lazy={true} pageSize={250}
        url={"/price/get-price-list"}
        onButtonClik={(e, type) => onButtunClick(e, type)} />


    </div>
  )
} export default Offers;