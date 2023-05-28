
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
import { ScrollTop } from 'primereact/scrolltop';
import { OverlayPanel } from 'primereact/overlaypanel';
import DynamicForm from '../service/DynamicForm';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"
import { Badge } from 'primereact/badge';
import { Divider } from 'primereact/divider';
import {useSelector, useDispatch} from 'react-redux';


function Sellers(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const [products, setProducts] = useState();
    const [layout, setLayout] = useState(window.innerWidth > 750 ? 'grid' : 'list');
    const [loading, setLoading] = useState(null);
    const [first, setFirst] = useState(0);
    const [totalRecords, setTotalRecords] = useState(0);
    const [rows, setRows] = useState(102);
    const toast = useRef(null);
    const history = useHistory();
    const [itemName, setItemName] = useState();
    const [visible, setVisible] = useState(false);
    const op = useRef(null);
    const op1 = useRef(null);
    const [searchJson, setSearchJson] = useState(productService.getDynamicFrom('searchSeller'));
    const [sortJson, setSortJson] = useState(productService.getDynamicFrom('sellerSort'));
    const [filterObj, setFilterObj] = useState();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch({type: 'CHANGE_NAME', payload:'Sellers'});

        let fl = { userId: userData.userId, userType: userData.userType };
        fl.size = rows;
        fl.first = 0;
        setFilterObj(fl);
        getDataFromDB(fl);

    }, []);

    const getDataFromDB = (filterJSON) => {
        APIService.postRequest('/transaction/get-sellers', filterJSON).then(data => {
            setProducts(data.data);
            setLoading(false);
        });
    }

    const setSelectedCat = (e) => {
        setLoading(true);
        let fl = { ...filterObj };
        fl.shop_name = e.name.value;
        fl.stock_name = e.itemname.value;
        setFilterObj(fl);
        getDataFromDB(fl);
        op.current.hide();
    }

    const itemdeatilsPage = (item) => {
        inputs.seller = item;
        inputs.searchtext = filterObj.stock_name;
        inputs.frompage = 'consellers';
        inputs.category = null;
        inputs.sub_cat = null;
        inputs.brands = null;
        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
        history.push('/app/products');
    }

    const setSorting = (e) => {
        setLoading(true);
        let fl = { ...filterObj };
        if (e.sortby.value !== null)
            fl.orderby = e.sortby.value;
        setFilterObj(fl);
        getDataFromDB(fl);
        op1.current.hide();
    }










    const onPage = (event) => {
        setLoading(true);
        let fl = { ...filterObj };
        fl.first = event.first;
        setFilterObj(fl);
        getDataFromDB(fl);

    }

    const goToOrders = (data) => {
        APIService.setLocalStorage('select_seller_id', data.seller_id);
        history.push('/app/myorders');
    }

    const renderListItem = (data) => {
        return <div className="p-col-12 p-md-4 card">
            <div className="product-list-item">
                <div className="product-list-detail">

                    <div className="image-div "  onClick={(e) => itemdeatilsPage(data)}>
                        <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                            onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />

                         
                    </div></div>
                <div className="product-list-detail1">

                    <div className="dev-product-name">{data.shop_name} <Button type="button" icon="order-icon" style={{height:"25px"}} onClick={(e) =>goToOrders(data)} className="p-button-rounded p-button-text" /></div>
                    <div className="dev-product-desc">{data.mobile_no}</div>
                    <div className="dev-product-address">{data.address}</div>
                    <div className="product-price-offer">Distance : {data.distance} km</div>
                    {data.min_order_value>0 && <div className="product-description">Min Order value :Rs {data.min_order_value}</div> }
                   
                </div>
            </div>
         
            <Divider />
        </div>

    }

    const renderGridItem = (data) => {
        return <div className="p-col-12 p-md-3" >
            <div className="product-grid-item card">
                <div className="product-grid-item-content" >
                    <div className="image-div" onClick={(e) => itemdeatilsPage(data)}>
                        <img className="productimage" src={APP_URL + "/user/image?path=" + data.image}
                            onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
                    </div>
                    <div className="product-name">{data.shop_name} <Button type="button" icon="order-icon" tooltip="Go to orders" aria-haspopup aria-controls="overlay_panel" style={{height:"25px"}} onClick={(e) =>goToOrders(data)} className="p-button-rounded p-button-text" /></div>
                    <div className="product-description">{data.mobile_no}</div>
                    <div className="dev-product-address">{data.address}</div>
                    <div className="product-price-offer">Distance : {data.distance} km</div>
                    {data.min_order_value>0 && <div className="product-description">Min Order value :Rs {data.min_order_value}</div> }
                   
                </div>
               
            </div>
           
        </div>
    }


    const itemTemplate = (product, layout) => {
        if (!product) {
            return null;
        }
        if (layout === 'list')
            return renderListItem(product);
        else if (layout === 'grid')
            return renderGridItem(product);
    }

    const renderHeader = () => {
        let onOptionChange = (e) => {
            setLoading(true);
            setTimeout(() => {
                setLoading(false);
                setLayout(e.value);
            }, 1000);
        };
        return (
            <div className="row">

            </div >

        );
    }


    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }

    const onHide = (e) => {
        setVisible(false);
    }



    const setUpdateQty = (qty) => {
        let ite = { ...itemName };
        ite.seletecQty = qty;
        setItemName(ite);
    }


    const header = renderHeader();

    const itemTemplateimage = (item) => {
        return <img className="productimageinView" src={process.env.PUBLIC_URL + `/showcase/demo/images/product/${item.image}`} alt={item.code} />;
    }









    return (<div className="content">
        <Toast className="toast-demo" ref={toast} />
        <div className="header-container">
            <div className="div-left">
                <h4>Sellers</h4>
            </div>
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

        </div>






        <div className="dataview-demo">
            <div className="card">
                <DataView value={products} layout={layout}
                    itemTemplate={itemTemplate} rows={rows} lazy paginator
                    totalRecords={totalRecords} first={first} onPage={onPage} />
                <ScrollTop icon="pi pi-angle-double-up" />
            </div>
        </div>

    </div>
    );



}

export default Sellers;
