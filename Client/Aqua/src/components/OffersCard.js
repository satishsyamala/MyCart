import React, { useState, useRef, useEffect } from 'react';
import { Button } from 'primereact/button';
import './Home.css';
import { useHistory } from "react-router-dom";
import ProductService from '../service/ProductService';
import 'bootstrap/dist/css/bootstrap.min.css';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"

function OffersCard(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const [offeres, setOfferes] = useState();
    const history = useHistory();

    useEffect(() => {
          let filterJSON={user_id:userData.userId};
          APIService.postRequest('/price/get-offers', filterJSON).then(data => {
            setOfferes(data.data);
         });
    }, []);


    const itemTemplate = (item) => {
        return <div className="p-col-12  p-md-6 " >
            <div className="product-grid-item card">
                <div className="product-grid-item-top">
                    <div>
                        <span className="product-name">{item.name}</span>
                    </div>
                </div>
                <div className="product-grid-item-content" >
                    <img src={APP_URL + "/user/image?path=" + item.offer_image}  onClick={(e) => onOfferSelect(item)} style={{ width: '100%', height: (window.innerWidth > 750 ? 280 : 200) + 'px', display: 'block', cursor: 'grab' }} />;
                <div className="product-description">{item.description}</div>
                    { item.offer_type==='Flat'?<div className="product-name">Flat {item.amt_per} Off</div>:<div className="product-name">{item.amt_per}% Off</div> }
                    <div className="product-price">Offer From {item.start_date} to {item.end_date}</div>
                </div>
                <Button icon="pi pi-shopping-cart" onClick={(e) => onOfferSelect(item)} label="Buy" ></Button>
            </div>
        </div>

    }

    const onOfferSelect = (item) => {
        inputs.category = null;
        inputs.sub_cat = null;
        inputs.brands = null;
        inputs.offer = item;
        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
        history.push('/app/products');
    }


    return (<div >
        <h4>Offers</h4>
        <div className="dataview-demo">
            <div className="card">
            <div className="card">
            <DataView value={offeres} layout="grid"
                itemTemplate={itemTemplate}  />
        </div>
               
            </div>
        </div>
    </div>
    );


} export default OffersCard;
