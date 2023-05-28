
import React, { useState, useRef, useEffect } from 'react';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Rating } from 'primereact/rating';
import { Button } from 'primereact/button';
import './Home.css';
import { connect } from 'react-redux';
import { OrderList } from 'primereact/orderlist';
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


function Products(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const [products, setProducts] = useState();
    const [layout, setLayout] = useState(window.innerWidth > 750 ? 'grid' : 'list');
    const [loading, setLoading] = useState(null);
    const [first, setFirst] = useState(0);
    const [totalRecords, setTotalRecords] = useState(0);
    const [rows, setRows] = useState(102);
    const [datasource, setDatasource] = useState();
    const [selectCat, setSelectCat] = useState(inputs.category ? inputs.category.categoryId : null);
    const [selSubCat, setSelSubCat] = useState(inputs.sub_cat ? inputs.sub_cat.subCategoryId : null);
    const [selBrand, setSelBrand] = useState(inputs.brands ? inputs.brands.brandId : null);
    const [searcText, setSearcText] = useState(inputs.searchtext ? inputs.searchtext : null);
    const [seller, setSeller] = useState(inputs.seller ? inputs.seller : null);
    const [itemAvl, setItemAvl] = useState();
    const [cartItme, setCartItme] = useState(null);
    const toast = useRef(null);
    const history = useHistory();
    const categoryDB = useIndexedDB('category');
    const stockItemsDB = useIndexedDB('stock_items');
    const stockImageDB = useIndexedDB('stock_images');
    const cartDB = useIndexedDB('cart_items');
    const [itemName, setItemName] = useState();
    const [visible, setVisible] = useState(false);
    const [sortBy, setSortBy] = useState('name');
    const [sortOption, setSortOption] = useState('asc');
    const op = useRef(null);
    const op1 = useRef(null);
    const [searchJson, setSearchJson] = useState(productService.getDynamicFrom('itemSearch'));
    const [sortJson, setSortJson] = useState(productService.getDynamicFrom('itemSort'));
    const [offerItem, setOfferItem] = useState(inputs.offer);
    const [filterObj, setFilterObj] = useState();
    const [sellers, setSellers] = useState(false);
    const [sellersinfo, setSellersInfo] = useState(false);

    useEffect(() => {


        let fl = { userId: userData.userId, userType: userData.userType };
        if (offerItem) {
            fl.offerid = offerItem.price_list_id;
            fl.seller_id = offerItem.seller_id;

        }
        if (seller)
            fl.seller_id = seller.seller_id;

        fl.orderby = 'name';
        fl.size = rows;
        fl.first = 0;
        fl.name = searcText;
        fl.categoryId = selectCat;
        fl.subCategoryId = selSubCat;
        fl.brandId = selBrand;
        fl.sel_del = userData.delivery_add_id;
        setFilterObj(fl);
        getDataFromDB(fl);

    }, []);

    const getDataFromDB = (filterJSON) => {
        APIService.postRequest('/transaction/get-sales-items', filterJSON).then(data => {
            setProducts(data.data);
            setLoading(false);
        });
    }

    const setSelectedCat = (e) => {
        setLoading(true);
        let fl = { ...filterObj };
        fl.name = e.name.value;
        if (e.categoryId.value)
            fl.categoryId = e.categoryId.value.key;
        if (e.subCategoryId.value)
            fl.subCategoryId = e.subCategoryId.value.key;
        if (e.brandId.value)
            fl.brandId = e.brandId.value.key;
        fl.invstock = e.instock.value;
        setFilterObj(fl);
        getDataFromDB(fl);
        op.current.hide();
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

    const goToCart = (data) => {
        history.push('/app/mycart');
    }

    const openSellers = (data) => {
        let fl = { ...filterObj };
        fl.isSeller = true;
        fl.stock_item_id = data.stock_item_id;
        APIService.postRequest('/transaction/get-sales-items', fl).then(data => {
            setSellersInfo(data.data);
            setSellers(true);
        });

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
        let temp = [...products];
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].stock_item_id === data.stock_item_id) {
                temp[i].price = pack.price;
                temp[i].pack_size = pack.name;
                temp[i].multipack.default = pack;
                break;
            }
        }
        setProducts(temp);
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
            let temp = [...products];
            for (var i = 0; i < temp.length; i++) {
                if (temp[i].stock_item_id === object.stock_item_id) {
                    temp[i].seletecQty = object.seletecQty;
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
                    break;
                }
            }
            setProducts(temp);
        });
    }





    const renderListItem = (data) => {
        return <div>
            <div className="product-list-item">
                <div className="product-list-detail" onClick={(e) => itemdeatilsPage(data)}> 
               

                    <div className="image-div ">
                        <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                            onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />

                    </div></div>
                <div className="product-list-detail1">

                    <div className="dev-product-name">{data.name} </div>
                  
                    <div className="product-price-offer">  <Dropdown value={data.multipack.default} options={data.multipack.packs} onChange={(e) => setPrice(data, e.value)} optionLabel="name" placeholder="Select a City" /> Rs : {data.price}    <Button icon="more-sellers" onClick={(e) => openSellers(data)} tooltip="Sellers" style={{ height: "20px",marginRight:"0px" }} className="p-button-rounded p-button-text p-button-secondary" /></div>
                 
                  
                    <Button icon="pi pi-shopping-cart" onClick={(e) => addToCart(data)} hidden={buttonChange(data)} label='Add to Cart' />
                    <div hidden={!buttonChange(data)} className="p-inputgroup">
                        <Button icon="pi pi-minus" onClick={(e) => removeQty(data)} className="p-button-danger" />
                        <InputText style={{ textAlign: "right", height: "30px", width: "30px" }} readOnly={true} value={data.seletecQty} />
                        <Button icon="pi pi-plus" onClick={(e) => incrementQty(data)} className="p-button-success" />
                       
                    </div>
                  
                </div>

            </div>
         
            <Divider />
        </div>

    }



    const renderGridItem = (data) => {

        return <div className="p-col-12 p-md-3" >
            <div className="product-grid-item card">
                <div className="product-grid-item-top">
                    <div>
                        <i className="pi pi-tag product-category-icon"></i>
                        <span className="product-category">{data.category_name}</span>
                    </div>

                </div>
                <div className="product-grid-item-content" >
                    <div className="image-div" onClick={(e) => itemdeatilsPage(data)}>
                        <img className="productimage" src={APP_URL + "/user/image?path=" + data.image}
                            onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />

                    </div>
                    <div className="product-name">{data.name}</div>
                    <div className="product-description">{data.description}</div>
                    <div className="product-description">
                        <Dropdown value={data.multipack.default} options={data.multipack.packs} onChange={(e) => setPrice(data, e.value)} optionLabel="name" placeholder="Select a City" />
                    </div>
                    <div className="product-price-offer"> Rs : {data.price}</div>
                </div>
                <Button icon="pi pi-shopping-cart" onClick={(e) => addToCart(data)} hidden={buttonChange(data)} label='Add to Cart' ></Button>

                <div hidden={!buttonChange(data)} className="p-inputgroup">
                    <Button icon="pi pi-minus" onClick={(e) => removeQty(data)} className="p-button-danger" />
                    <InputText style={{ textAlign: "right" }} readOnly={true} value={data.seletecQty} />
                    <Button icon="pi pi-plus" onClick={(e) => incrementQty(data)} className="p-button-success" />

                </div>

                <span className="product-category"><Button icon="more-sellers" onClick={(e) => openSellers(data)} tooltip="Sellers" style={{ height: "30px" }} className="p-button-rounded p-button-text p-button-secondary" /> Seller : {data.shop_name} ({data.dist}km)  </span>
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



    const addToCart = (item) => {
        let cart = {
            userId: userData.userId, pack: item.multipack.default.name, pack_qty: item.multipack.default.quantity,
            code: item.code, price: item.price, stock_items: item, offer_id: item.offer_id, offer: item.offer,
            discount_price: item.discount_price, image: item.image, address_id: item.address_id, seller_id: item.seller_id,
            isdiscount: item.isdiscount ? 1 : 0, quantity: item.seletecQty ? item.seletecQty : 1, status: 'active', sync_status: 'open',
        };

        APIService.postRequest('/transaction/add-to-cart', cart).then(res => {
            if (res) {
                let pro = [...products];
                for (var i = 0; i < pro.length; i++) {
                    if (pro[i].code === item.code) {
                        let t = { pack: item.multipack.default.name, quantity: 1, cart_item_id: res.data.cart_item_id }
                        pro[i].isincart.push(t);
                        pro[i].seletecQty = 1;
                        break;
                    }
                }
                setProducts(pro);
                let co = parseInt(APIService.getLocalStorage("cartcount"));
                co = co + 1;
                APIService.setLocalStorage("cartcount", co);
                props.dispatch({ type: actionTypes.UPDATE_CART, length: co });
                onHide();
            }
        });



    };
    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }



    const onHide = (e) => {
        setVisible(false);
        setSellers(false);

    }





    const itemdeatilsPage = (item) => {
        if (false) {
            let img = productService.getStockImages(item);
            if (img)
                item.images = img;
            else {
                let im = [{ image: item.image, code: item.code }];
                item.images = im;
            }
            item.seletecQty = 1
            setItemName(item);
            setVisible(true);
        } else {
            inputs.itemcode = item.stock_item_id;
            inputs.frompage = 'products';
            APIService.setLocalStorage('inputs', JSON.stringify(inputs));
            history.push('/app/itemview');
        }
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



    const selectedItem = (data) => {

        return <div className="p-col-12" >
            <div className="product-grid-item card">
                <div className="product-grid-item-top">
                    <div>
                        <i className="pi pi-tag product-category-icon"></i>
                        <span className="product-category">{data.category_name}</span>
                    </div>
                    <span className={`product-badge status-${data.inventory_status.toLowerCase()}`}>{data.inventory_status}</span>
                </div>
                <div className="product-grid-item-content" >

                    <Carousel value={data.images} numVisible={1} numScroll={1} className="custom-carousel" circular
                        autoplayInterval={3000} itemTemplate={itemTemplateimage} />
                    <div className="dev-product-name"> Rating : {data.rating}/5</div>
                    <div className="product-name">{data.name}</div>
                    <div className="product-description">{data.description}</div>
                    <div className="product-price">Price Rs:{data.price} Per {data.quantity}{data.uom}</div>
                    {
                        data.isdiscount && <div className="product-price-offer">{data.discount_price}% Offer Price
                            Rs:{data.price - Math.round((data.discount_price / 100) * data.price)}(-{Math.round((data.discount_price / 100) * data.price)})</div>
                    }
                </div>
                <InputNumber readOnly="true" className="qtyInputField" inputId="horizontal" value={data.seletecQty} onValueChange={(e) => setUpdateQty(e.value)} showButtons buttonLayout="horizontal" step={1}
                    decrementButtonClassName="p-button-danger" incrementButtonClassName="p-button-success" incrementButtonIcon="pi pi-plus" decrementButtonIcon="pi pi-minus" />
                <Button icon="pi pi-shopping-cart" onClick={(e) => addToCart(data)} label="Add to Cart" disabled={data.inventory_status === 'OUTOFSTOCK'}></Button>

            </div>
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

    const sellerDetails = (data) => {
        return <div className="p-col-12">
            <div className="product-list-item ">
                <div className="p-col-6 product-list-detail">
                    <div className="product-name">{data.shop_name}</div>
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.seller_image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
                </div>
                <div className="p-col-6 product-list-detail1">

                    <div className="dev-product-desc">{data.dist} Km</div>


                    <Dropdown value={data.multipack.default} options={data.multipack.packs} onChange={(e) => setSellerPrice(data, e.value)} optionLabel="name" placeholder="Select a City" />
                    <div className="product-price-offer"> Rs : {data.price}</div>
                    <Button icon="pi pi-shopping-cart" onClick={(e) => addToCart(data)} hidden={buttonChange(data)} label='Add' ></Button>
                    <Button icon="pi pi-shopping-cart" className='p-button-success' hidden={!buttonChange(data)} onClick={(e) => goToCart(data)} label='Cart'></Button>
                </div>



            </div>
        </div>

    }









    return (<div className="content">
        <Toast className="toast-demo" ref={toast} />
        <div className="header-container">
            <div className="div-left">
                <h4> {offerItem ? offerItem.name : seller ? seller.shop_name : 'Items'}</h4>
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

               <DataView value={products} layout={layout}
                    itemTemplate={itemTemplate} rows={rows} lazy paginator
                    totalRecords={totalRecords} first={first} onPage={onPage} />
                <ScrollTop icon="pi pi-angle-double-up" />
            
        </div>
        <Dialog className="itemselect" header={itemName && itemName.name} visible={visible} onHide={() => onHide()}>
            <div className="dataview-demo">

                {
                    itemName && selectedItem(itemName)
                }

            </div>
        </Dialog>





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
    );



}
const mapStateToProps = state => ({
    cartItems: state.items

});
export default connect(mapStateToProps, null)(Products);
