import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import { DataView } from 'primereact/dataview';
import { useHistory } from "react-router-dom";
import ProductService from '../service/ProductService';
import { OverlayPanel } from 'primereact/overlaypanel';
import DynamicForm from '../service/DynamicForm';
import { Button } from 'primereact/button';
import Moment from 'moment';
import { ScrollTop } from 'primereact/scrolltop';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import {useSelector, useDispatch} from 'react-redux';

function MyOrders() {
   const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
   const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
   const ordersDB = useIndexedDB('orders');
   const [orderList, setOrderList] = useState();
   const productService = new ProductService();
   const op = useRef(null);
   const op1 = useRef(null);
   const op2 = useRef(null);
   const [searchJson, setSearchJson] = useState(productService.getDynamicFrom('orderfilter'));
   const [sortJson, setSortJson] = useState(productService.getDynamicFrom('ordersort'));
   Moment.locale('en');
   const [sortBy, setSortBy] = useState('order_date');
   const [sortOption, setSortOption] = useState('desc');
   const [fromDate, setFromDate] = useState();
   const [toDate, setToDate] = useState();
   const [itemName, setItemName] = useState();
   const history = useHistory();
   const [barcode, setBarcode] = useState();
   const [scan, setScan] = useState(false);
   const [filterJson, setFilterJson] = useState();
   const dispatch = useDispatch();
  
   useEffect(() => {
      dispatch({type: 'CHANGE_NAME', payload:'My Orders'});
      let fil = { userId: userData.userId, size: 50 };
      if(APIService.getLocalStorage("delivery_address_id"))
      {
       fil.address_id=APIService.getLocalStorage("delivery_address_id");
       localStorage.removeItem("delivery_address_id");
      }
      if(APIService.getLocalStorage("select_seller_id"))
      {
       fil.select_seller_id=APIService.getLocalStorage("select_seller_id");
       localStorage.removeItem("select_seller_id");
      }

      


      setFilterJson(fil);
      getDataFromDB(fil);
   
   }, []);

    const  addnotication  = ()=>{
      setTimeout(async () => {
      let granted = false;

      if (Notification.permission === 'granted') {
          granted = true;
      } else if (Notification.permission !== 'denied') {
          let permission = await Notification.requestPermission();
          granted = permission === 'granted' ? true : false;
      }
  
      // show notification or error
      granted ? showNotification() : showError();
   }, 3000);
   }

   const showError = () => {
    
      alert('You blocked the notifications');
  }



   const showNotification = () => {
      // create a new notification
      const notification = new Notification('JavaScript Notification API', {
          body: 'This is a JavaScript Notification API demo',
          icon: './img/js.png'
      });

      // close the notification after 10 seconds
      setTimeout(() => {
          notification.close();
      }, 10 * 1000);

      // navigate to a URL when clicked
      notification.addEventListener('click', () => {

         history.push('/app/home');
      });
  }


   const getDataFromDB = (filterJSON) => {
      APIService.postRequest('/transaction/user-orders', filterJSON).then(data => {
         if(data)
         setOrderList(data.data);
      });
   }



   const setSelectedCat = (e) => {
      let fil = { ...filterJson };
      fil.fromdate = e.fromdate.value ? onlyDate(e.fromdate.value) : null;
      fil.todate = e.todate.value ? onlyDate(e.todate.value) : null;
      fil.name = e.name.value ? e.name.value : null;
      setFilterJson(fil);
      getDataFromDB(fil);
      op.current.hide();
   }

   const setSorting = (e) => {
      let fil = { ...filterJson };
      fil.orderby = e.sortby.value;
      setFilterJson(fil);
      getDataFromDB(fil);
      op1.current.hide();
   }

   const stringTodate = (vale) => {
      Moment.locale('en');
      return Moment(vale).toDate();
   }

   const stringToOnlyDate = (vale) => {
      Moment.locale('en');
      return Moment(Moment(stringTodate(vale)).format('yyyy-MM-DD')).toDate();
   }

   const onlyDate = (vale) => {
      Moment.locale('en');
      return stringToOnlyDate(Moment(vale).format('yyyy-MM-DD'));
   }

   const orderView = (data) => {
      inputs.orderid = data.order_id;
      APIService.setLocalStorage('inputs', JSON.stringify(inputs));
      history.push('/app/orderview');
   }

   const handleScan = (data) => {
      setBarcode('Hi  ' + data);

   }

   const handleError = (err) => {
      setBarcode('data');
   }
   const previewStyle = {
      height: 240,
      width: 320,
   };


   const device = (data) => {
      return <div className="p-col-12 p-md-4">
         <span className="dev-product-name">Order No :{data.order_no}</span>
         <div className="product-list-item" onClick={(e) => orderView(data)}>

            <div className="product-list-detail">
             <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
               onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
            </div>
            <div className="product-list-detail1">
               <div className="dev-product-name">{data.name}</div>
               <div className="dev-product-desc">Date : {data.order_date}</div>
               <div className="dev-product-desc">Delivery Type : {data.delivery_type}</div>
               <div className="dev-product-desc">Delivery Date : {data.delivery_date}</div>
               <div className="dev-product-desc">Price : {data.final_price}</div>
               <div className="order-date">{data.track_status}</div>

            </div>
         </div>
      </div>
   }

   const onDetected = (result) => {
      setBarcode(result.codeResult.code);
      setScan(false);
   }

   return <div className="content">


<div className="header-container">
            <div className="div-left">
            <h4>My Orders</h4>
            </div>
            <div className="div-right">
            <Button type="button" icon="pi pi-filter" tooltip="Filter" onClick={(e) => op.current.toggle(e)} aria-haspopup aria-controls="overlay_panel" className="p-button-rounded p-button-success" />
         <OverlayPanel ref={op} showCloseIcon id="overlay_panel" style={{ width: '450px' }} className="overlaypanel-demo">
            <DynamicForm type="itemSearch" formJson={searchJson} clname="col-sm-12" onSubmit={(e) => setSelectedCat(e)} />
         </OverlayPanel>
         <Button type="button" icon="pi pi-sort-alt" tooltip="Sort" onClick={(e) => op1.current.toggle(e)} aria-haspopup aria-controls="overlay_panel" className="p-button-rounded p-button-help" />
         <OverlayPanel ref={op1} showCloseIcon id="overlay_panel" style={{ width: '450px' }} className="overlaypanel-demo">
            <DynamicForm type="itemSort" formJson={sortJson} clname="col-sm-12" onSubmit={(e) => setSorting(e)} />
         </OverlayPanel>
            </div>

        </div>

     
    
      <div className="dataview-demo">
         <div className="card">
            {
               orderList && <DataView value={orderList} layout="list" itemTemplate={device} />
            }
            <ScrollTop icon="pi pi-angle-double-up" />
         </div>
      </div>
   </div>
} export default MyOrders;
