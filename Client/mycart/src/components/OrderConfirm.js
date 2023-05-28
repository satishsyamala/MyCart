import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import { Button } from 'primereact/button';
import { useHistory } from "react-router-dom";
import { Divider } from 'primereact/divider';
import { DataView } from 'primereact/dataview';
import { Rating } from 'primereact/rating';
import Moment from 'moment';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"

function OrderConfirm() {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const [orderInfo, setOrderInfo] = useState(inputs.orderInfo);
    Moment.locale('en');
    const history = useHistory();

    useEffect(() => {
      
        window.scrollTo(0, 0);
    }, []);


    const device = (data) => {
      
        
        return <div className="p-col-12 p-md-6">
            <div className="product-list-item">
                <div className="product-list-detail">
                    <i className="pi pi-tag product-category-icon"></i><span className="product-category">{data.category_name}</span>
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />


                </div>


                <div className="product-list-detail1">


                    <div className="dev-product-name">{data.name}</div>
                    <div className="c">Qty. : {data.quantity}</div>
                    <span className="c">Price : {data.price * data.quantity}</span>

                    {
                        <div className="product-price-offer"><span> {data.pack_size}&nbsp;&nbsp;&nbsp;</span> Rs : {data.price}</div>
                    }



                </div>


            </div>
        </div>

    }




    const renderListItem = (data) => {
        return device(data);
    }

    const addPreSpace = (input) => {
        let temp = ':        :';
        let var1 = temp.substring(0, 10 - (input + '').length) + input;
        return var1;
    }

    const back = (fromPage) => {
        history.push('/app/' + fromPage);
    }

    return (
        <div className="main-div order_view">
            <br/>
            <h4>Order Details
                <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                    <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Cart' onClick={(e) => back('mycart')} />
                    <Button style={{ height: '30px', marginLeft: '5px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-Help " label='Order' onClick={(e) => back('myorders')} />
                </div>
            </h4>
            <Divider type="dashed"><b>Summary</b></Divider>
            <div style={{paddingLeft:"20px"}} className="card">
                <span className="dev-product-name">Transaction Id &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.tranRef}</span>
                <span className="dev-product-desc">Order Date &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {Moment(orderInfo.orderdate).format('DD-MM-yyyy HH-mm')}</span>
                <span className="dev-product-desc">Delivery Date &nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;&nbsp;{Moment(orderInfo.deldate).format('DD-MM-yyyy')}</span>
                <span className="dev-product-desc">No. Of Items &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.order.items.length}</span>
                <span className="dev-product-desc">Total Amount &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.order.totalFin}</span>
            </div>
            <Divider><b>Delivery Address</b></Divider>
            <div style={{paddingLeft:"20px"}} className="card">
                <span className="dev-product-name">{userData.name}</span>
                <p>{orderInfo.delivery}</p>
                <span className="dev-product-desc">Mobile No : {userData.mobileNo}</span>
            </div>
            <Divider ><b>Price Details</b></Divider>
            <div style={{paddingRight:"20px"}} align="right" className="card">

                <span className="dev-product-desc">Total Price {addPreSpace(orderInfo.order.totalAMt)}</span>
                <span className="dev-product-desc">Discount {addPreSpace(orderInfo.order.totalDis)}</span>
                <span className="dev-product-desc">Delivery Charge {addPreSpace(orderInfo.order.deliveryCharge)}</span>
                <span className="dev-product-name">Final Price {addPreSpace(orderInfo.order.totalFin)}</span>
                <span className="dev-product-desc">Payment Type  {addPreSpace(orderInfo.pt.toUpperCase())}</span>
            </div>
            <Divider><b>Items</b></Divider>
            <div className="dataview-demo">
                <div className="card">
                    <DataView value={orderInfo.order.items} layout="list" itemTemplate={renderListItem} />
                </div>
            </div><br/><br/>
        </div>
    )
} export default OrderConfirm;
