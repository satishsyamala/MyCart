import { useRef, useState, useEffect } from 'react';
import { DataView } from 'primereact/dataview';
import { Rating } from 'primereact/rating';
import { Button } from 'primereact/button';
import { connect } from 'react-redux';
import { withRouter } from "react-router-dom";
import { InputText } from 'primereact/inputtext';
import './Home.css';
import { Toast } from 'primereact/toast';
import * as actionTypes from '../actiontype';
import ProductService from '../service/ProductService';
import { useIndexedDB } from 'react-indexed-db';
import { useHistory } from "react-router-dom";
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import { TabView, TabPanel } from 'primereact/tabview';
import Moment from 'moment';
import { ScrollTop } from 'primereact/scrolltop';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import { SelectButton } from 'primereact/selectbutton';
import { RadioButton } from 'primereact/radiobutton';


const PlaceOrder = (props) => {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const [cartItem, setCartItem] = useState(JSON.parse(APIService.getLocalStorage('cartItems')));
    const selectedObj = cartItem.cart;
    const selectKey = cartItem.selectKey;
    const cartCount = cartItem.cartCount;
    const seller = cartItem.seller;
    const toast = useRef(null);
    const [deliverAdd, setDeliverAdd] = useState();
    const [selAdder, setSelAdder] = useState();
    const [delType, setDelType] = useState(selectKey === 'Item' ? 'Delivery' : null);
    const [paymentType, setPaymentType] = useState();
    const productService = new ProductService();
    const history = useHistory();

    useEffect(() => {
        let fl = { userId: userData.userId, };
        APIService.postRequest('/user/get-address', fl).then(data => {
            
            for(var i=0;i<data.data.length;i++)
            {
                if(data.data[i].defaultDel)
                   setSelAdder(data.data[i]);
            }
            setDeliverAdd(data.data);

        });
    }, []);

    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }

    const selectedDelevryAdd = (add) => {
        setSelAdder(add);
    }

    const addressLayout = (row) => {
        return (<spam>
            {row.address + ', ' + row.city + ', ' + row.state + ', ' + row.pinCode}
        </spam>
        )
    }

    const selectedDelevryType = (value) => {
        setDelType(value);
    }
    const selectedPaymentType = (value) => {
        setPaymentType(value);
    }



    const createOrder = () => {
        if (selAdder && paymentType && delType) {
            let date = new Date();
            let del = { ...selAdder };
            let fullDerr = selAdder.address + ', ' + selAdder.city + ', ' + selAdder.state + ', ' + selAdder.pinCode;
            let ref = Moment(date).format('yyyyMMDDHHmmss') + '' + userData.userId;
          
                let deliv = productService.getDeliveryDate(userData, seller.min_delivery_days);
                let order_name=seller.shop_name;
                let image_path=selectedObj.image;
                if(selectedObj.items.length===1){
                     order_name=selectedObj.items[0].name;
                     image_path=selectedObj.items[0].image;
                    }
               
                let orderObj = {
                    image: image_path, order_by: 'Cart',
                    order_ref: ref, tran_ref: ref, userId: userData.userId, user: userData.name, name: order_name,
                    cat_order_by: selectKey, order_date: Moment(date).format('DD-MM-yyyy HH:mm'), delivery_address: del.address,
                    del_address_id: del.delveryAddressId, del_lat: del.lat, del_log: del.log,
                    payment_type: paymentType, address: fullDerr, delivery_date: Moment(deliv).format('DD-MM-yyyy'), price: selectedObj.totalAMt,
                    discount: selectedObj.totalDis, final_price: selectedObj.totalFin, deltype: delType,delv_charge:selectedObj.deliveryCharge,
                    status: 'pending', sync_status: 'open', cart_times: selectedObj.items,seller_id:seller.seller_id
                };
                console.log(JSON.stringify(orderObj));
                APIService.postRequest('/transaction/place-order', orderObj).then(data => {
                    inputs.orderInfo = { tranRef: ref, delivery: fullDerr, pt: paymentType, order: selectedObj, orderdate: date, deldate: date };
                    APIService.setLocalStorage('inputs', JSON.stringify(inputs));
                    setCartCountBG(cartCount - selectedObj.items.length);
                    localStorage.removeItem("cartItems");
                    history.push('/app/orderconfirm');
                });
         

          
        }
        else {
            if (!selAdder)
                growlMsg('error', 'Please Select Delivery Address');
            else if (!delType)
                growlMsg('error', 'Please Select Delivery Type');
            else
                growlMsg('error', 'Please Select Payment Type');
        }

    }

    const setCartCountBG = (count) => {
        APIService.setLocalStorage("cartcount", count);
        props.dispatch({ type: actionTypes.UPDATE_CART, length: count });

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
        <div className="order_view">
            <Toast className="toast-demo" ref={toast} />
            <h4>Order Confirm
                <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                    <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Cart' onClick={(e) => back('mycart')} />

                </div>
            </h4>
            <div style={{ paddingLeft: "10px" }}>
                <div className="card total-card">
                    <span className="product-name"><pre>Total Price&nbsp;: {selectedObj.totalAMt}</pre></span>
                    <span className="product-name-des"><pre >Discount&nbsp;&nbsp;&nbsp;&nbsp;: {selectedObj.totalDis}</pre></span>
                    <span className="product-name-des"><pre >Delivery Charge&nbsp;:{selectedObj.deliveryCharge}</pre></span>
                    <span className="product-name"><pre>Final Price&nbsp;: {selectedObj.totalFin}</pre></span>
                </div>
                <div className="card total-card">
                <spam className="product-name">  Delivery Address</spam>
            
                {
                   userData && userData.address
                }
                 </div>
                {
                    selectKey !== 'Item' && <div> <br />  <spam>  Delivery Type</spam>
                        <SelectButton value={delType} options={['Delivery', 'Pick Up']} onChange={(e) => selectedDelevryType(e.value)} />
                    </div>

                }
                <br /> <spam>  Payment Type</spam>
                <SelectButton value={paymentType} options={seller.payment_modes} onChange={(e) => selectedPaymentType(e.value)} />
                <br /><div align="center" >
                    <Button label="Confirm Order" style={{ width: "90%" }} onClick={(e) => createOrder()} />
                </div>

                <br />
                <br />
            </div>
        </div>
    )


}
const mapStateToProps = state => ({
    cartItems: state.items
})
export default withRouter(connect(mapStateToProps, null)(PlaceOrder))