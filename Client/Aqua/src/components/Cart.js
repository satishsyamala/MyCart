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
import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import { useHistory } from "react-router-dom";
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import { TabView, TabPanel } from 'primereact/tabview';
import Moment from 'moment';
import { ScrollTop } from 'primereact/scrolltop';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import loading from '../images/load.gif';
import {useSelector, useDispatch} from 'react-redux';

const Cart = (props) => {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const toast = useRef(null);
    const [cartCount, setCartCount] = useState(0);
    const [selectKey, setSelectKey] = useState();
    const history = useHistory();
    const [formJson, setFormJson] = useState({});
    const [visible, setVisible] = useState(false);
    const [cartByType, setCartByType] = useState({});
    Moment.locale('en');
    const productService = new ProductService();
    const [orderAmt, setOrderAmt] = useState({});
    const [sellerCharges, setSellerCharges] = useState({});
    const dispatch = useDispatch();
    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }
    useEffect(() => {
        dispatch({type: 'CHANGE_NAME', payload:'My Cart'});
        let fl = { userId: userData.userId, userType: userData.userType, address_id: userData.delivery_add_id };
        APIService.postRequest('/transaction/get-cart', fl).then(data => {
            let cartItem = data.data.items;
            let orderType = {};
            cartItem.map(c => {
                let key = c.seller_id + '@' + c.address_id;
                if (!orderType[key]) {
                    orderType[key] = { name: c.shop_name, seller_d: c.seller_id, image: c.cat_image, items: [], totalAMt: 0, totalDis: 0, totalFin: 0, deliveryCharge: 0 };
                }
            });

            cartItem.map(d => {
                let key = d.seller_id + '@' + d.address_id;
                d.key = key;
                orderType[key].items.push(d);
            });
            setSellerCharges(data.data.sellers);
            getTotalAmountLoop(orderType, data.data.sellers);
        });
    }, []);


    const removeQty = (item) => {
        let cart = { ...cartByType };
        let products = cart[item.key].items;
        for (var k = 0; k < products.length; k++) {
            if (products[k].cart_item_id == item.cart_item_id) {
                item.quantity -= 1;
                if (item.quantity == 0) {
                    products.splice(k, 1);
                    updateDB(item, 'delete-item');
                    growlMsg('error', 'Item removed from cart');
                }
                else {
                    updateDB(item, 'update-qty');
                }
                break;
            }
        }
        getTotalAmountLoop(cart, sellerCharges);


    }

    const updateDB = (object, url) => {

        APIService.postRequest('/transaction/' + url, object).then(data => {

        });
    }

    const removeItem = (item) => {
        let cart = { ...cartByType };
        let products = cart[item.key].items;;
        for (var k = 0; k < products.length; k++) {
            if (products[k].cart_item_id == item.cart_item_id) {
                products.splice(k, 1);
                updateDB(item, 'delete-item');
                growlMsg('error', 'Item removed from cart');
                break;
            }
        }

        getTotalAmountLoop(cart, sellerCharges);
    }
    const incrementQty = (item) => {
        let cart = { ...cartByType };
        let products = cart[item.key].items;
        for (var k = 0; k < products.length; k++) {
            if (products[k].code == item.code) {
                item.quantity += 1;
                updateDB(item, 'update-qty');
                break;
            }
        }

        getTotalAmountLoop(cart, sellerCharges);

    }
    const getTotalAmountLoop = (products, sellerCharges) => {
        let tempObj = { ...products };
        let actualCart = {};
        let count = 0;
        Object.keys(tempObj).map((key, ind) => {
            let tempCount = 0;
            let tempDisc = 0;

            let ary = tempObj[key].items;

            for (var k = 0; k < ary.length; k++) {
                count++;
                tempCount += (ary[k].price * ary[k].quantity);
              //  if (ary[k].isdiscount)
               //     tempDisc += ((ary[k].price - ary[k].discount_price) * ary[k].quantity);
            }
            tempObj[key].totalAMt = tempCount;
            tempObj[key].totalDis = tempDisc;
            let afterDis = tempCount - tempDisc;
            let del_ch = 0;
            let sel_char = sellerCharges[tempObj[key].seller_d];
            if (sel_char) {
                if (sel_char.min_delivery_charge && sel_char.min_delivery_charge > 0) {
                    if ((tempCount - tempDisc) < sel_char.max_value_for_free_delv) {
                        del_ch = sel_char.min_delivery_charge;
                        if (sel_char.dist > sel_char.min_dist_for_min_charge) {
                            del_ch = del_ch + (sel_char.dist - sel_char.min_dist_for_min_charge) * sel_char.dele_charge_for_km;
                            if (del_ch > sel_char.max_delivery_charge)
                                del_ch = sel_char.max_delivery_charge;
                        }
                    }
                }
            }
            tempObj[key].deliveryCharge = del_ch;
            tempObj[key].totalFin = (tempCount + del_ch) - tempDisc;
            tempObj[key].minOrderValu=sel_char.min_order_value;

            if (ary.length > 0)
                actualCart[key] = tempObj[key];
        });

        setCartCount(count);
        setCartCountBG(count);
        setCartByType(actualCart);
    }


    const setCartCountBG = (count) => {
        APIService.setLocalStorage("cartcount", count);
        props.dispatch({ type: actionTypes.UPDATE_CART, length: count });

    }



    const device = (data) => {
        return <div className="p-col-12">
            <div className="product-list-item">
                <div className="product-list-detail">
                    <i className="pi pi-tag product-category-icon"></i><span className="product-category">{data.category_name}</span>
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                        onClick={(e) => onItemSelect(data.stock_item_id)} onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />

                </div>


                <div className="product-list-detail1">


                    <div className="product-name">{data.name}</div>
                    <div className="dev-product-desc">{data.description.length > 60 ? data.description.substring(0, 60) + '..' : data.description}</div>
                    <div align="right">
                     
                         <div className="product-price-offer"><span> {data.pack_size}&nbsp;&nbsp;&nbsp;</span> Rs : {data.price * data.quantity}</div>
                      
                        <div className="inputgroup">
                            <Button icon="pi pi-minus" className="p-button-secondary" onClick={() => removeQty(data)} />
                            <InputText className="qtyinput" style={{ height: "10px  !important" }} placeholder="Qty" value={data.quantity} />
                            <Button icon="pi pi-plus" className="p-button-success" onClick={() => incrementQty(data)} />
                            <span>&nbsp;&nbsp;&nbsp;</span>
                            <Button icon="pi pi-trash" className="p-button-danger" onClick={() => removeItem(data)} />
                        </div>

                    </div>
                </div>


            </div>
        </div>

    }

    const portal = (data) => {
        return <div className="p-col-12">
            <div className="product-list-item">
                <div className="product-list-detail">
                    <i className="pi pi-tag product-category-icon"></i><span className="product-category">{data.category_name}</span>
                    <img src={APP_URL + "/user/image?path=" + data.image}
                        onClick={(e) => onItemSelect(data.stock_item_id)} onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />


                </div>


                <div className="product-list-detail1">


                    <div className="product-name">{data.name}</div>
                    <div className="product-description">{data.description}</div>

                    <div align="right">
                        {
                            <div className="product-price-offer"><span> {data.pack_size}&nbsp;&nbsp;&nbsp;</span> Rs : {data.price * data.quantity}</div>
                        }
                        <div className="inputgroup">
                            <Button icon="pi pi-minus" className="p-button-secondary" onClick={() => removeQty(data)} />
                            <InputText className="qtyinput" style={{ height: "10px  !important;" }} placeholder="Qty" value={data.quantity} />
                            <Button icon="pi pi-plus" className="p-button-success" onClick={() => incrementQty(data)} />
                            <span>&nbsp;&nbsp;&nbsp;</span>
                            <Button icon="pi pi-trash" className="p-button-danger" onClick={() => removeItem(data)} />
                        </div>
                    </div>
                </div>


            </div>
        </div>

    }
    const onItemSelect = (code) => {
        inputs.itemcode = code;
        inputs.frompage = 'mycart';
        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
        history.push('/app/itemview');
    }

    const renderListItem = (data) => {
        if (window.innerWidth > 750)
            return portal(data);
        else
            return device(data);
    }

    const addPreSpace = (input) => {
        let temp = ':        :';
        let var1 = temp.substring(0, 10 - (input + '').length) + input;
        return var1;
    }

    const checkOut = (key, amount) => {
        let lo = { cart: cartByType[key], selectKey: key, cartCount: cartCount, seller: sellerCharges[cartByType[key].seller_d] }
        APIService.setLocalStorage("cartItems", JSON.stringify(lo));
        history.push("/app/placeorder");



    }
    const onHide = (e) => {
        setVisible(false);
    }



    const cartView = (object, key) => {
        let headerName = object.name + ' (' + object.items.length + ')';
        return <TabPanel header={headerName}><div className="dataview-demo">
            <div className="card">
                {
                    object.items.length == 0 ?
                        <img src={process.env.PUBLIC_URL + '/emptycart.png'} width="340" height="340" />
                        :
                        <DataView value={object.items} layout="list" itemTemplate={renderListItem} />

                }

            </div>
            <div>
                {
                    object.totalAMt > 0 &&
                    <div >
                        <div align="right" className="card total-card">

                            <span className="product-name"><pre>Total Price {addPreSpace(object.totalAMt)}</pre></span>
                            <span className="product-name-des"><pre >Discount {addPreSpace(object.totalDis)}</pre></span>
                            <span className="product-name-des"><pre >Delivery Charge{addPreSpace(object.deliveryCharge)}</pre></span>

                            <span className="product-name"><pre>Final Price {addPreSpace(object.totalFin)}</pre></span>
                            <br />
                           {object.minOrderValu>0 && <span ><pre>Min. Order Value {(object.minOrderValu)}</pre></span> }
                            
                            <Button disabled={object.minOrderValu<=object.totalFin?false:true} label="Place Order" onClick={(e) => checkOut(key, object.totalFin)} />
                        </div>

                    </div>
                }</div>
        </div ></TabPanel  >

    }

    return (
        <div className="order_view">
            <h4>Cart Items</h4>
            <Toast className="toast-demo" ref={toast} />
            <div> {
                cartCount > 0 ? <TabView>{

                    Object.keys(cartByType).map((key, ind) =>
                        cartView(cartByType[key], key)
                    )
                }</TabView> : <div align='center'><img src={loading}></img></div>}
                <ScrollTop icon="pi pi-angle-double-up" />
            </div> <br />

            <Dialog header="Place Order" className="register_dl" visible={visible} onHide={() => onHide()}>
                <div className="order-date-amt">Total Amount : {orderAmt}</div>

            </Dialog>
        </div>


    )
}
const mapStateToProps = state => ({
    cartItems: state.items
})
export default withRouter(connect(mapStateToProps, null)(Cart))