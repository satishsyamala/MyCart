import React, { useState, useRef, useEffect } from 'react';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Rating } from 'primereact/rating';
import { Button } from 'primereact/button';
import './Home.css';
import { connect } from 'react-redux';
import * as actionTypes from '../actiontype';
import { Toast } from 'primereact/toast';
import { useHistory } from "react-router-dom";
import { useIndexedDB } from 'react-indexed-db';
import { InputText } from 'primereact/inputtext';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Dropdown } from 'primereact/dropdown';
import { Dialog } from 'primereact/dialog';
import { Carousel } from 'primereact/carousel';
import ProductService from '../service/ProductService';
import { InputNumber } from 'primereact/inputnumber';
import { Galleria } from 'primereact/galleria';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import { Badge } from 'primereact/badge';




function ItemView(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const toast = useRef(null);
    const history = useHistory();
    const [itemCode, setItemCode] = useState(inputs.itemcode);
    const [fromPage, setFromPage] = useState(inputs.frompage ? inputs.frompage : 'products');
    const [item, setItem] = useState();
    const stockItemsDB = useIndexedDB('stock_items');
    const cartDB = useIndexedDB('cart_items');
    const [cartItme, setCartItme] = useState(null);
    const [sellers, setSellers] = useState(false);
    const [sellersinfo, setSellersInfo] = useState(false);
    const [filterObj, setFilterObj] = useState();

    useEffect(() => {
        let fl = { userId: userData.userId, userType: userData.userType, stock_item_id: itemCode };
        APIService.postRequest('/transaction/get-sales-items', fl).then(data => {
            let itm = data.data[0];

            if (itm.images)
                itm.images.push({ image: itm.image, code: itm.code });
            else
                itm.images = [{ image: itm.image, code: itm.code }];
            itm.seletecQty = 1;
            setItem(itm);
        });
    }, []);


    const setUpdateQty = (qty) => {
        let ite = { ...item };
        ite.seletecQty = qty;
        setItem(ite);
    }




    const itemTemplateimage = (item) => {
        return <img className="productimageinView_page" src={APP_URL + "/user/image?path=" + item.image} alt={item.code} />;
    }

    const goToCart = (data) => {
        history.push('/app/mycart');
    }

    const buttonChange = (data) => {
        let rs = false;
        if (data.isincart) {
            for (var i = 0; i < data.isincart.length; i++) {
                if (data.isincart[i].pack === data.multipack.default.name) {
                    rs = true;
                    data.seletecQty = data.isincart[i].quantity;
                    data.cart_item_id = data.isincart[i].cart_item_id;
                    break;
                }
            }
        }
        return rs;

    }

    const setPrice = (data, pack) => {
        let temp = { ...item };
        temp.price = pack.price;
        temp.pack_size = pack.name;
        temp.multipack.default = pack;
        setItem(temp);
    }

    const removeQty = (item) => {
        item.seletecQty -= 1;
        if (item.seletecQty == 0) {
            updateDB(item, 'delete-item');
            growlMsg('error', 'Item removed from cart');
            let co = parseInt(APIService.getLocalStorage("cartcount"));
            co = co - 1;
            APIService.setLocalStorage("cartcount", co);
            props.dispatch({ type: actionTypes.UPDATE_CART, length: co });
        }
        else {
            updateDB(item, 'update-qty');
        }

    }

    const incrementQty = (item) => {
        item.seletecQty += 1;
        updateDB(item, 'update-qty');
    }

    const updateDB = (object, url) => {
        let carttemp = { cart_item_id: object.cart_item_id, quantity: object.seletecQty, price: object.price }

        APIService.postRequest('/transaction/' + url, carttemp).then(data => {
            let temp = { ...item };
            temp.seletecQty = object.seletecQty;
            if (object.isincart) {
                for (var i = 0; i < object.isincart.length; i++) {
                    if (object.isincart[i].pack === object.multipack.default.name) {
                        if (url === 'update-qty')
                            object.isincart[i].quantity = object.seletecQty;
                        else
                            object.isincart.splice(i, 1);

                        console.log(JSON.stringify(object.isincart));
                        break;
                    }
                }
            }
            setItem(temp);
        });
    }

    const selectedItem = (data) => {

        return <div className="p-col-12" >
            <div className="card">
                <Galleria value={data.images} numVisible={5} style={{ Width: '100%', height: '350px' }}
                    item={itemTemplateimage} showThumbnails={false} showIndicators circular indicatorsPosition="bottom" autoPlay transitionInterval={4000} />
             
            </div>

            <div className="product-grid-item card">
                <div className="product-grid-item-top">
                    <div>
                        <i className="pi pi-tag product-category-icon"></i>
                        <span className="product-category">{data.category_name}</span>
                    </div>
                    <span className={`product-badge status-${data.inventory_status.toLowerCase()}`}>{data.inventory_status}</span>
                </div>

                <div className="product-grid-item-content" >
                    <div className="product-name">{data.name}</div>
                    <div className="product-description">{data.description}</div>
                    <div className="product-description">
                        <Dropdown value={data.multipack.default} options={data.multipack.packs} onChange={(e) => setPrice(data, e.value)} optionLabel="name" placeholder="Select a City" />
                    </div>
                    <div className="product-price-offer"> Rs : {data.price}</div>
                    <div className="product-price-offer">
                    <Button icon="pi pi-shopping-cart" onClick={(e) => addToCart(data)} hidden={buttonChange(data)} label='Add to Cart' ></Button>
                  
                    <center>

                   
                    <div hidden={!buttonChange(data)} style={{ width: "300px" }} className="p-inputgroup">
                        <Button icon="pi pi-minus" onClick={(e) => removeQty(data)} className="p-button-danger" />
                        <InputText style={{ textAlign: "right" }} readOnly={true} value={data.seletecQty} />
                        <Button icon="pi pi-plus" onClick={(e) => incrementQty(data)} className="p-button-success" />

                    </div>  </center>
                    </div>
                    <span className="product-category">Seller : {data.shop_name} ({data.dist}km) <Button icon="more-sellers" onClick={(e) => openSellers(data)} tooltip="Sellers" style={{ height: "20px" }} className="p-button-rounded p-button-text p-button-secondary" /></span>
                <br/><br/></div></div>

        </div>
    }

    const setSellerPrice = (data, pack) => {
        let temp = [...sellersinfo];
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].stock_item_id === data.stock_item_id) {
                temp[i].price = pack.price;
                temp[i].pack_size = pack.name;
                temp[i].multipack.default = pack;
                break;
            }
        }
        setSellersInfo(temp);
    }

    const openSellers = (data) => {
        let fl = { userId: userData.userId, userType: userData.userType };
        fl.sel_del = userData.delivery_add_id;
        fl.isSeller = true;
        fl.stock_item_id = data.stock_item_id;
        APIService.postRequest('/transaction/get-sales-items', fl).then(data => {
            setSellersInfo(data.data);
            setSellers(true);
        });

    }

    const addToCart = (item) => {
        let cart = {
            userId: userData.userId,
            code: item.code, price: item.price, stock_items: item, offer_id: item.offer_id, offer: item.offer,pack: item.multipack.default.name, pack_qty: item.multipack.default.quantity,
            discount_price: item.discount_price, image: item.image, address_id: item.address_id, seller_id: item.seller_id,
            isdiscount: item.isdiscount ? 1 : 0, quantity: item.seletecQty ? item.seletecQty : 1, status: 'active', sync_status: 'open'
        };

        APIService.postRequest('/transaction/add-to-cart', cart).then(data => {
            let co = parseInt(APIService.getLocalStorage("cartcount"));
            co = co + 1;
            APIService.setLocalStorage("cartcount", co);
            props.dispatch({ type: actionTypes.UPDATE_CART, length: co });
            history.push('/app/' + fromPage);
        });




    };
    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }

    const back = () => {
        history.push('/app/' + fromPage);
    }

    const sellerDetails = (data) => {
        return <div className="p-col-12">
           <div className="row product-list-item ">
                <div className="col-sm product-list-detail">
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.seller_image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
                </div>


                <div className="col-sm product-list-detail1">
                    <div className="product-name">{data.shop_name}</div>
                    <div className="dev-product-desc">{data.dist}Km</div>
                   
                </div>
                <div className="col-sm product-list-detail1">
                <Dropdown value={data.multipack.default} options={data.multipack.packs} onChange={(e) => setSellerPrice(data, e.value)} optionLabel="name" placeholder="Select a City" />
                    <div className="product-price-offer"> Rs : {data.price}</div>
                    <Button icon="pi pi-shopping-cart" onClick={(e) => addToCart(data)} hidden={buttonChange(data)} label='Add to Cart' ></Button>
                    <Button icon="pi pi-shopping-cart" className='p-button-success' hidden={!buttonChange(data)} onClick={(e) => goToCart(data)} label='Go to Cart'></Button>
                </div>



            </div>
        </div>

    }

    return <div className="order-view">
        <h4>{item ? item.name : 'Item View'}
            <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Back' onClick={(e) => back()} />
            </div>
        </h4>

        <Toast className="toast-demo" ref={toast} />

        <div className="dataview-demo">

            {
                item && selectedItem(item)
            }
            <br/><br/>
        </div>

        <Dialog header="Sellers" className="seller-info" visible={sellers} onHide={() => setSellers(false)}>
            {sellersinfo &&
                <div className="dataview-demo">
                    <div className="card">
                        <DataView value={sellersinfo}
                            itemTemplate={sellerDetails}
                        />


                    </div>
                </div>
            }
        </Dialog>
    </div>
}
const mapStateToProps = state => ({
    cartItems: state.items

});
export default connect(mapStateToProps, null)(ItemView);

