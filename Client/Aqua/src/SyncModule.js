import React, { useState, useRef, useEffect } from 'react';
import { Toast } from 'primereact/toast';
import PrimeReact from 'primereact/api';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import { useHistory } from "react-router-dom";
import './login.css';
import { Dialog } from 'primereact/dialog';
import DynamicForm from './service/DynamicForm';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from './service/ProductService';
import { connect } from 'react-redux';
import { withRouter } from "react-router-dom";
import * as actionTypes from './actiontype';
import APIService from "./service/APIService"
import Moment from 'moment';
import loading from './images/load.gif';
import {useSelector, useDispatch} from 'react-redux';

function SyncModule(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const history = useHistory();
    //DB Connection
    const userDB = useIndexedDB('users');
    const configurationDB = useIndexedDB('configuration');
    const deliveryAddressDB = useIndexedDB('delivery_address');
    const categoryDB = useIndexedDB('category');
    const sub_categoryDB = useIndexedDB('sub_category');
    const brandsDB = useIndexedDB('brands');
    const stockItemsDB = useIndexedDB('stock_items');
    const stockImagesDB = useIndexedDB('stock_images');
    const cartItemsDB = useIndexedDB('cart_items');
    const offersDB = useIndexedDB('offers');
    const lastsyncdateDB = useIndexedDB('last_sync_date');
    const productService = new ProductService();
    const dispatch = useDispatch();


    useEffect(() => {
        APIService.setLocalStorage("cartcount", userData.cart_count);
        syncMatser(userData);
        setTimeout(() => {
          
            dispatch({type: actionTypes.UPDATE_CART, length: userData.cart_count});
          
            if (userData.delivery_type === 'no') {
                if (userData.noseller) {
                    history.push('/app/sellerdetails');
                } else {
                    if (userData.userType === 'Admin')
                        history.push('/app/dashboard');
                    else if (userData.userType === 'Seller')
                        history.push('/app/dashboard');
                    else
                        history.push('/app/home');
                }
            } else {
                history.push('/app/deliveryorders');
                
            }
        }, 500);
    }, [])


    const syncMatser = (userData) => {
        if (userData.isoffline) {
            getLastSyncdate(userData, 'address', deliveryAddressDB, '/user/get-address', 'delveryAddressId');
            getLastSyncdate(userData, 'category', categoryDB, '/product/get-category', 'categoryId');
            getLastSyncdate(userData, 'subcategory', sub_categoryDB, '/product/get-sub-category', 'subCategoryId');
            getLastSyncdate(userData, 'brands', brandsDB, '/product/get-brands', 'brandId');


        }


    }

    const getLastSyncdate = (userData, moduleName, DB, url, key) => {
        lastsyncdateDB.getByIndex('madule_name', moduleName).then(module => {
            let lsd = null;
            if (module)
                lsd = module.date;
            let req = { userId: userData.userId };
            if (lsd) {
                req.syncdate = Moment(lsd).format('DD-MM-yyyy HH:mm:ss');
            }
            APIService.postRequest(url, req).then(data => {

                let del = data.data;
                if (lsd) {
                    del.map(row => {
                        DB.getByIndex(key, row[key]).then(rec => {
                            if (rec)
                                DB.deleteRecord(rec.id);
                            DB.add(row);
                        })
                    });
                } else {
                    DB.clear();
                    addDataInDBArray(DB, del);
                }
            });
            if (module)
                lastsyncdateDB.deleteRecord(module.id);
            addDataInDB(lastsyncdateDB, { madule_name: moduleName, date: new Date() });
        });
    }



    const addDataInDB = (dbObj, data) => {
        dbObj.add(data).then(
            event => {
                return true;
            },
            error => {
                return false;
            }
        );
    }

    const addDataInDBArray = (dbObj, dataArray) => {
        dataArray.map(data => {
            dbObj.add(data);
        }
        );
    }






    return (<div align="center">
        <br /> <br /><br /><br /><br />
        <img src={loading}></img> <br />
        <h4>Loading.....</h4>
    </div>

    );
}


export default SyncModule;



