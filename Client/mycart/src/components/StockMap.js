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
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputNumber } from 'primereact/inputnumber';
import { Checkbox } from 'primereact/checkbox';


function StockMap(props) {
  const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
  const productService = new ProductService();
  const history = useHistory();
  const menu = props.match.url.replace('/app/', '');
  const [stockItem, setStockitem] = useState();
  const [stockPrice, setStockPrice] = useState(false);
  const [stockJson, setStockJson] = useState();

  const onButtunClick = (data, type) => {

    if (type === 'add') {
      const fs = productService.getDynamicFrom('stockmap');
      let reqData = {
        actType: "add", type: "stockmap", table: "", formJson: fs,
        class: "col-sm-12", url: "/user/map-stocks", pageurl: "/app/mapstock", header: "Add Stock"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    } else if (type === 'view') {
      let filter = { stock_item_id: data.stock_item_id, module_name: 'stock_item' };
      let reqData = {
        filter: filter, image: true,
        class: "col-sm-6", url: "/user/get-view-data", pageurl: "/app/" + menu, header: "Stock View"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/viewinfo');
    }
    else if (type === 'update price') {
      console.log(JSON.stringify(data));
      setStockPrice(true);
      setStockJson(data);

    } else if (type === 'remove') {
      let fil = { seller_stock_map_id: data.seller_stock_map_id }
      APIService.postRequest('/user/remove-stock', fil).then(data => {
        if (data.data.reason === 'success') {
          alert("Item removed succesfully");
          window.location.reload();
        }
        else {
          alert(data.data.reason);
        }
      });
    }
  }

  const textElement = (rowData) => {
    return <InputNumber className="tableinputtext"
      value={rowData.price} onChange={(e) => (rowData.price = e.value)} />;
  }

  const setChecked = (chek, rowData) => {
    let temp = { ...stockJson };
    if (chek) {
      let ar = stockJson.packSize;
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

  const saveStockData = () => {

    APIService.postRequest('/user/update-price', stockJson).then(data => {
      let del = data.data;
      if (del.reason === 'success') {
        setStockPrice(false);
      }
      else {
        alert(del.reason);
      }
    });
  }

  return (
    <div className="content">
      <h4>Stock Mapping</h4>

      <DyanamicTable type="stockmap" lazy={true}
        url="/user/get-seller-stock"
        onButtonClik={(e, type) => onButtunClick(e, type)} />
      <Dialog header="Stock Price" visible={stockPrice} onHide={(e) => setStockPrice(false)} >
        <div className="card">
          {stockJson && <div>
            <DataTable value={stockJson.packSize} responsiveLayout="scroll">
              <Column field="key" header="Code"></Column>
              <Column field="name" header="Name"></Column>
              <Column className="numberColWith" header="Price" body={textElement}></Column>
              <Column body={checkBoxEle} header="Default"></Column>
            </DataTable>

            <center>
              <Button style={{ height: '30px' }} className="p-button-raised p-button-info " label='Update'
                onClick={e => saveStockData()} /> </center>   </div>
          }
        </div>
      </Dialog>
    </div>
  )
} export default StockMap;