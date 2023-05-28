
import React, { useState, useRef, useEffect } from 'react';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Rating } from 'primereact/rating';
import { Button } from 'primereact/button';
import './Home.css';
import { connect } from 'react-redux';
import * as actionTypes from '../actiontype';
import { Toast } from 'primereact/toast';
import { useHistory } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Dialog } from 'primereact/dialog';
import ProductService from '../service/ProductService';
import { OverlayPanel } from 'primereact/overlaypanel';
import DynamicForm from '../service/DynamicForm';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"
import QRCode from 'react-qr-code';
import { InputText } from 'primereact/inputtext';
import { Checkbox } from 'primereact/checkbox';
import { useSelector, useDispatch } from 'react-redux';


function AcceptedOrders(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const [orders, setOrders] = useState();
    const toast = useRef(null);
    const history = useHistory();
    const op = useRef(null);
    const op1 = useRef(null);
    const [searchJson, setSearchJson] = useState(productService.getDynamicFrom('searchSeller'));
    const [sortJson, setSortJson] = useState(productService.getDynamicFrom('sellerSort'));
    const [filterObj, setFilterObj] = useState();
    const menu = props.match.url.replace('/app/', '');
    const [pickupwin, setPickupwin] = useState(false);
    const [deliverywin, setDeliverywin] = useState(false);
    const [selOrder, setSelOrder] = useState();
    const [otp, setOtp] = useState();
    const [nootp, setNoOtp] = useState(false);
    const [payment, setPayment] = useState(false);
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch({ type: 'CHANGE_NAME', payload: '' });
        let fl = { user_id: userData.userId, status: getStatus() };
        setFilterObj(fl);
        getDataFromDB(fl);

    }, []);

    const getStatus = () => {
        if (menu === 'acceptedorders')
            return 'assigned';
        else if (menu === 'pickuporders')
            return 'Accept';
        else if (menu === 'deliveryorders')
            return 'Pickup';
        else
            return '';

    }

    const getDataFromDB = (filterJSON) => {
        APIService.postRequest('/delivery/delivery-order', filterJSON).then(data => {
            setOrders(data.data);

        });
    }

    const setSelectedCat = (e) => {

        let fl = { ...filterObj };
        fl.shop_name = e.name.value;
        fl.stock_name = e.itemname.value;
        setFilterObj(fl);
        getDataFromDB(fl);
        op.current.hide();
    }


    const setSorting = (e) => {

        let fl = { ...filterObj };
        if (e.sortby.value !== null)
            fl.orderby = e.sortby.value;
        setFilterObj(fl);
        getDataFromDB(fl);
        op1.current.hide();
    }

    const acceptOrder = (data, status) => {
        data.qr_code = { user_id: data.user_id, seller_id: data.seller_id, order_id: data.order_id, order_delivery_user_map_id: data.order_delivery_user_map_id, pickup_otp: data.pickup_otp };
        setSelOrder(data);
        if (data.assign_status === 'assigned') {
            data.action = status;
            APIService.postRequest('/delivery/accept-order', data).then(res => {
                if (res.data.reason === 'success') {
                    window.location.reload()
                }
            });
        }
        else if (data.assign_status === 'Accept') {
            setPickupwin(true);
        }
        else if (data.assign_status === 'Pickup') {
            setDeliverywin(true);
        }



    }

    const pickupOrder = (data, status) => {
        data.qr_code = { user_id: data.user_id, seller_id: data.seller_id, order_id: data.order_id, order_delivery_user_map_id: data.order_delivery_user_map_id, pickup_otp: data.pickup_otp };
        setSelOrder(data);
        if (data.assign_status === 'Accept') {
            data.action = status;
            APIService.postRequest('/delivery/accept-order', data).then(res => {

                if (res.data.reason === 'success')
                    history.push('/app/deliveryorders');
                else
                    growlMsg('error', res.data.reason);

            });
        }
        else if (data.assign_status === 'Pickup') {
            setPickupwin(true);
        }

    }

    const hiddeBtn = (assstatus) => {
        if (assstatus === 'assigned')
            return false;
        else
            return true;
    }

    const btnLable = (assstatus) => {
        if (assstatus === 'assigned')
            return "Accept";
        else if (assstatus === 'Accept')
            return "Pickup";
        else if (assstatus === 'Pickup')
            return "Delivery";
        else if (assstatus === 'Reject')
            return "Rejected";
        else if (assstatus === 'Delivered')
            return "Delivered";

    }

    const pickbtnLable = (assstatus) => {
        if (assstatus === 'Assigned')
            return "Waiting For Out for Delivery";
        else if (assstatus === 'Out For Delivery')
            return "Pickup";

    }

    const renderGridItem = (data) => {
        if (data.assign_status === 'Pickup' || data.assign_status === 'Delivered')
            return renderDelivery(data);
        else
            return renderOrders(data);
    }

    const renderDelivery = (data) => {
        return <div className="p-col-12 p-md-4 " >
            <div className="product-grid-item  p-shadow-24 ">
                <div className='deliv-padding"'>
                    <div className="product-name">Name : {data.full_name}</div>
                    <div className="product-name">Mobile No : {data.moblie_no}</div>
                    <div >Address : {data.delivery_address}</div>
                    <div >Price : {data.price}</div>
                    <div >Payment Type : {data.payment_type}</div>
                    <div align="center" style={{ padding: "10px" }}>
                       <Button icon={btnicon(data.assign_status)} label={btnLable(data.assign_status)} className={btnCss(data.assign_status)} onClick={() => acceptOrder(data, 'Accept')} /></div>
                </div>
            </div>
        </div>
    }

    const btnCss = (assstatus) => {
        if (assstatus === 'assigned')
            return "p-button-info";
        else if (assstatus === 'Accept')
            return "p-button-secondary";
        else if (assstatus === 'Pickup')
            return "p-button-warning";
        else if (assstatus === 'Reject')
            return "p-button-danger";
        else if (assstatus === 'Delivered')
            return "p-button-success";
    }

    const btnicon = (assstatus) => {
        if (assstatus === 'assigned')
            return "pi pi-check";
        else if (assstatus === 'Accept')
            return "pi ready-for-pick";
        else if (assstatus === 'Pickup')
            return "pi ready-for-pick";
        else if (assstatus === 'Reject')
            return "pi pi-times";
        else if (assstatus === 'Delivered')
            return "pi outfordelivery";
    }

    const renderOrders = (data) => {
        return <div className="p-col-12 p-md-4 " >
            <div className="product-grid-item  p-shadow-24">
            <div className="product-name">Shop name : {data.shop_name}</div>
                <div align="center">
                 
                    {menu !== 'deliveryall' && <img className="productimage" src={APP_URL + "/user/image?path=" + data.image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />}
                </div>
                <div className="product-name">{data.order_no}</div>
                <div className="dev-product-address">Shop Address : {data.sel_addres}</div>
                <div className="dev-product-address">Delivery Address : {data.delivery_address}</div>
                <div align="center" style={{ padding: "10px" }}>
                    <Button icon="pi pi-times" hidden={hiddeBtn(data.assign_status)} label="Reject" className='p-button-danger' onClick={() => acceptOrder(data, 'Reject')} />
                    <Button icon={btnicon(data.assign_status)} label={btnLable(data.assign_status)} className={btnCss(data.assign_status)} onClick={() => acceptOrder(data, 'Accept')} /></div>

            </div>

        </div>
    }

    const orderDetails = (data) => {
        return <div className="dataview-demo" >
            <div className="product-grid-item  deliv-padding" >
                <div align="center">
                    <div className="product-name">Shop name : {data.shop_name}</div>
                    <br /> <br />
                    <QRCode value={JSON.stringify(data.qr_code)} /> <br /> <br />
                    <div className="product-name">Pickup Otp : {data.pickup_otp}</div>
                </div>
                <div className="product-name">{data.order_no}</div>
                <div className="dev-product-address">Shop Address : {data.sel_addres}</div>
                <div className="dev-product-address">Delivery Address : {data.delivery_address}</div>
                <div align="center" style={{ padding: "10px" }}>

                    <Button icon="pi pi-check" label={pickbtnLable(data.order_status)} className='p-button-secondary' onClick={() => pickupOrder(data, 'Pickup')} /></div>

            </div>

        </div>
    }

    const pageheader = () => {
        if (menu === 'acceptedorders')
            return 'Assigned Orders';
        else if (menu === 'pickuporders')
            return 'Accepted Orderd';
        else if (menu === 'deliveryorders')
            return 'Pending Deliveries';
        else
            return 'Orders';
    }

    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }

    const orderProcess = (data) => {
        if ((nootp || otp) && payment) {
            let orderInfo = {};
            orderInfo.order_id = data.order_id;
            orderInfo.status = 'Out For Delivery';
            orderInfo.delivery_type = 'delivery'
            orderInfo.enterotp = otp;
            orderInfo.nootp = nootp;
            orderInfo.order_delivery_user_map_id = data.order_delivery_user_map_id;
            APIService.postRequest('/transaction/order-process', orderInfo).then(res => {
                if (res.data.reason === 'success') {
                    setDeliverywin(false);
                    window.location.reload()
                } else {
                    growlMsg('error', res.data.reason);
                }

            });
        } else {
            if (!payment)
                growlMsg('error', "Please Complete Payment");
            else
                growlMsg('error', "Please enter OTP");
        }
    }

    const delOrderDetails = (data) => {
        return <div className="dataview-demo" >
            <div className="product-grid-item card">
                <div className="product-name">No. : {data.order_no}</div>
                <div className="product-price">Name : {data.full_name}</div>
                <div className="product-price">Mobile No : {data.moblie_no}</div>
                <div >Address : {data.delivery_address}</div>
                <div >Price :Rs {data.price}</div>
                <div >Payment Type : {data.payment_type}</div>
                <div className="p-field-checkbox">
                    <label htmlFor="binary">Payment Completed &nbsp;&nbsp;&nbsp;</label>
                    <Checkbox inputId="binary" checked={payment} onChange={e => setPayment(e.checked)} />

                </div>
                <div className="p-field-checkbox">
                    <label htmlFor="binary">No Otp &nbsp;&nbsp;&nbsp;</label>
                    <Checkbox inputId="binary" checked={nootp} onChange={e => setNoOtp(e.checked)} />

                </div>
                <InputText disabled={nootp} keyfilter="int" placeholder="OTP" style={{ float: "right" }} value={otp} onChange={(e) => setOtp(e.target.value)} />
                <div align="center" style={{ padding: "10px" }}>

                    <Button icon="pi ready-for-pick" label={btnLable(data.assign_status)} className='p-button-info' onClick={() => orderProcess(data)} /></div>

            </div>

        </div>
    }

    return (<div className="content">
        <Toast className="toast-demo" ref={toast} />
        <br />
        <div className="header-container">
            <div className="div-left">
                <h4>{pageheader()}</h4>
            </div>
            {menu === 'acceptedorders' &&
                <div className="div-right">
                    <Button type="button" icon="pi pi-filter" tooltip="Filter" onClick={(e) => op.current.toggle(e)} aria-haspopup aria-controls="overlay_panel" className="p-button-rounded p-button-success" />
                    <OverlayPanel ref={op} showCloseIcon id="overlay_panel" style={{ width: '450px' }} className="overlaypanel-demo">
                        <DynamicForm type="searchSeller" formJson={searchJson} clname="col-sm-12" onSubmit={(e) => setSelectedCat(e)} />
                    </OverlayPanel>
                    <Button type="button" icon="pi pi-sort-alt" tooltip="Sort" onClick={(e) => op1.current.toggle(e)} aria-haspopup aria-controls="overlay_panel" className="p-button-rounded p-button-help" />
                    <OverlayPanel ref={op1} showCloseIcon id="overlay_panel" style={{ width: '450px' }} className="overlaypanel-demo">
                        <DynamicForm type="sellerSort" formJson={sortJson} clname="col-sm-12" onSubmit={(e) => setSorting(e)} />
                    </OverlayPanel>
                </div>
            }

        </div>






        <div className="dataview-demo">
            <div className="card">
                <DataView value={orders} layout="grid"
                    itemTemplate={renderGridItem} />

            </div>
        </div>


        <Dialog header="Pickup Order" onHide={(e) => setPickupwin(false)} className="register_dl" visible={pickupwin} >
            {selOrder &&
                orderDetails(selOrder)
            }
        </Dialog>

        <Dialog header="Delivery" onHide={(e) => setDeliverywin(false)} className="register_dl" visible={deliverywin} >
            {selOrder &&

                delOrderDetails(selOrder)

            }
        </Dialog>


    </div>
    );



}

export default AcceptedOrders;
