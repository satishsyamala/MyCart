import React, { useState, useRef, useEffect } from 'react';
import { PhotoService } from '../service/PhotoService';
import { Galleria } from 'primereact/galleria';
import { Carousel } from 'primereact/carousel';
import { Button } from 'primereact/button';
import './Home.css';
import { useHistory } from "react-router-dom";
import { useIndexedDB } from 'react-indexed-db';
import ProductService from '../service/ProductService';
import APP_URL from "../service/APIConfig"
import { Dialog } from 'primereact/dialog';
import { RadioButton } from 'primereact/radiobutton';
import APIService from "../service/APIService"
import * as actionTypes from '../actiontype';
import { connect } from 'react-redux';
import { useSelector, useDispatch } from 'react-redux';


function Home(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const [images, setImages] = useState();
    const history = useHistory();
    const categoryDB = useIndexedDB('category');
    const [category, setCategory] = useState();
    const [label, setLabel] = useState();
    const [selCat, setSelCat] = useState();
    const sub_categoryDB = useIndexedDB('sub_category');
    const brandsDB = useIndexedDB('brands');
    const [showAdd, setShowAdd] = useState(false);
    const [address, setAddress] = useState();
    const [selectAdd, setSelectAdd] = useState();
    const [cartItems, setCartItems] = useState();
    const [last10, setLast10] = useState();
    const dispatch = useDispatch();


    useEffect(() => {
        dispatch({ type: 'CHANGE_NAME', payload: 'Home' });
        inputs.category = null;
        inputs.sub_cat = null;
        inputs.brands = null;
        inputs.offer = null;
        inputs.seller = null;
        inputs.searchtext = null;
        localStorage.removeItem('selectedcat');
        categoryDB.getAll().then(data => {
            productService.sortJSONArray(data, 'name', 'asc');
            setCategory(data);
            setLabel('Category');
            setSelCat('Category');
        });

        let fil = { userId: userData.userId };
        APIService.postRequest('/user/get-address', fil).then(data => {
            if (data) {
                for (var i = 0; i < data.data.length; i++) {
                    if (userData.delivery_add_id === data.data[i].delveryAddressId)
                        setSelectAdd(data.data[i]);

                }
                setAddress(data.data);
            }
        });

        let filterJSON = { user_id: userData.userId };
        APIService.postRequest('/price/get-offers', filterJSON).then(data => {
            if (data)
                setImages(data.data);
        });
        let fl = { userId: userData.userId, userType: userData.userType, address_id: userData.delivery_add_id };
        APIService.postRequest('/transaction/get-cart', fl).then(data => {
            if (data)
                setCartItems(data.data)
        });

        APIService.postRequest('/transaction/last-10-items', fl).then(data => {
            if (data)
                setLast10(data.data)
        });


    }, []);



    const onImageSelect = (items) => {
        let redirect = true;

        if (items !== null && label === 'Category') {
            inputs.category = items;
            sub_categoryDB.getAll().then(data => {
                let res = [];
                data.map(item => {
                    if (item.categoryId === items.categoryId) {
                        res.push(item);
                    }
                })
                if (res && res.length > 0) {
                    productService.sortJSONArray(res, 'name', 'asc');
                    setCategory(res);
                    setLabel('Sub Category');
                    setSelCat(items.name);
                    redirect = false;
                }
                else {
                    APIService.setLocalStorage('inputs', JSON.stringify(inputs));
                    history.push('/app/products');
                }
            })
        } else if (items !== null && label === 'Sub Category') {
            inputs.sub_cat = items;
            brandsDB.getAll().then(data => {
                let res = [];
                data.map(item => {
                    if (item.subCategoryId === items.subCategoryId) {
                        res.push(item);
                    }
                })
                if (res && res.length > 0) {
                    productService.sortJSONArray(res, 'name', 'asc');
                    setCategory(res);
                    setLabel('Brand');
                    setSelCat(items.name);
                    redirect = false;
                }
                else {
                    APIService.setLocalStorage('inputs', JSON.stringify(inputs));
                    history.push('/app/products');
                }
            })

        } else {
            inputs.brands = items;
            APIService.setLocalStorage('inputs', JSON.stringify(inputs));
            history.push('/app/products');
        }

    }

    const onOfferSelect = (item) => {
        inputs.category = null;
        inputs.sub_cat = null;
        inputs.brands = null;
        inputs.offer = item;
        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
        history.push('/app/products');
    }

    const itemTemplate = (item) => {
        return item && <img className="offer-slides" src={APP_URL + "/user/image?path=" + item.offer_image} alt={item.alt} onClick={(e) => onOfferSelect(item)} style={{ width: '100%', height: (window.innerWidth > 750 ? 280 : 200) + 'px', display: 'block', cursor: 'grab' }} />;
    }

    const thumbnailTemplate = (item) => {
        return <img className="product-image" src={process.env.PUBLIC_URL + item.image} onClick={(e) => onImageSelect(item)}
            alt={item.category} style={{ width: [(window.innerWidth > 750 ? 60 : 40) + 'px'], height: [(window.innerWidth > 750 ? 60 : 40) + 'px'], display: 'block', cursor: 'grab' }} />
    }



    const headerImages = (item) => {
        return <div align="center" className="column">
            <img className="product-image" src={APP_URL + "/user/image?path=" + item.image} onClick={(e) => onImageSelect(item)}
                alt={item.name} style={{ width: [(window.innerWidth > 750 ? 100 : 80) + 'px'], height: [(window.innerWidth > 750 ? 100 : 80) + 'px'], cursor: 'grab' }} />
            <br /> <span> {item.name}</span>
        </div>

    }

    const cartImages = (item) => {
        return <div align="center" className="column">
            <img className="product-image" src={APP_URL + "/user/image?path=" + item.image} onClick={(e) => itemdeatilsPage (item)}
                alt={item.name} style={{ width: [(window.innerWidth > 750 ? 100 : 80) + 'px'], height: [(window.innerWidth > 750 ? 100 : 80) + 'px'], cursor: 'grab' }} />
            <br /> <span> {item.name}</span>
        </div>

    }


    const itemdeatilsPage = (item) => {
        inputs.itemcode = item.stock_item_id;
        inputs.frompage = 'home';
        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
        history.push('/app/itemview');
    }



    const showDeliveryAdd = () => {
        setShowAdd(true);
    }

    const changeAddress = () => {

        let fil = { userId: userData.userId, address_id: selectAdd.delveryAddressId };
        APIService.postRequest('/user/address-change', fil).then(data => {
            let userDat = { ...userData };
            userDat.delivery_add_id = selectAdd.delveryAddressId;
            userDat.address = selectAdd.address + ', ' + selectAdd.city + ', ' + selectAdd.state + ', ' + selectAdd.pinCode;
            setUserData(userDat);
            APIService.setLocalStorage("myData", JSON.stringify(userDat));
            setShowAdd(false);
            props.dispatch({ type: actionTypes.UPDATE_CART, length: data.data.cart_count });

        });


    }


    return (<div style={{ paddingTop: "5px" }} className="main-div">

        {userData.isdelivery &&
            <div className="row address-bar" >
                <div style={{ width: "10%" }}>
                    {(address && address.length > 1) && <Button icon="pi pi-map-marker" onClick={(e) => showDeliveryAdd()} tooltip="Change Address" style={{ height: "30px", width: "50px" }} className="p-button-rounded p-button-text p-button-danger" />}
                </div>
                <div style={{ width: "90%" }}>

                    {selectAdd &&
                        <marquee width="100%" direction="left"> {selectAdd.address + ' ' + selectAdd.state}</marquee>
                    }
                </div> </div>}
        <div >
            <Galleria value={images} showIndicators numVisible={5} style={{ maxWidth: '100%', height: [(window.innerWidth > 750 ? 280 : 200) + 'px'] }}
                item={itemTemplate} showThumbnails={false} circular transitionInterval={2000} />
        </div>

        {
            category && <div>
                <span className="header">{selCat}</span>
                <div className="category">
                    {
                        category.map((item) =>
                            headerImages(item)
                        )
                    }
                </div></div>
        } {
            cartItems && cartItems.items && cartItems.items.length > 0 && <div>
                <span className="header">Cart item</span>
                <div className="category">
                    {
                        cartItems.items.map((item) =>
                            cartImages(item)
                        )
                    }
                </div>
            </div>
        }{
            last10 && last10.length > 0 && <div>
                <span className="header">Last 9 Order Items</span>
                <div className="category">
                    {
                        last10.map((item) =>
                            cartImages(item)
                        )
                    }
                </div>
            </div>
        }



        <Dialog header="Change Address" visible={showAdd} onHide={() => setShowAdd(false)}>
            {
                address && address.map((add) => {
                    return (
                        <div key={add.delveryAddressId} className="p-field-radiobutton">
                            <RadioButton inputId={add.delveryAddressId} name="Address" value={add} onChange={(e) => setSelectAdd(e.value)} checked={selectAdd.delveryAddressId === add.delveryAddressId} />
                            <label htmlFor={add.delveryAddressId}>{add.address + ', ' + add.city + ', ' + add.state + ', ' + add.pinCode}</label>
                        </div>
                    )
                })
            }
            <br /><br /><center><Button label="Change" onClick={(e) => changeAddress()} /></center>

        </Dialog>
    </div>
    );

} const mapStateToProps = state => ({
    cartItems: state.items
})
export default connect(mapStateToProps, null)(Home);


