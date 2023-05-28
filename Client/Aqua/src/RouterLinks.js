import React, { Component } from 'react';
import logo from './logo.svg';
import { Menu } from 'primereact/menu';
import { Menubar } from 'primereact/menubar';
import { InputText } from 'primereact/inputtext';
import './App.css';

import {
    Route,
    Link,
    Switch,
    Redirect
} from 'react-router-dom';

import Home from './components/Home';
import Products from './components/Products';
import App from './App';
import SingeUp from './SingeUp';
import About from './components/About';
import Messages from './components/Messages';
import Login from './Login';
import DataTableBasicDemo from "./components/DataTableBasicDemo";
import Cart from './components/Cart';
import DataJson from './data/DataJson';
import AddData from "./data/AddData";
import ShowAll from "./data/ShowAll";
import OffersCard from "./components/OffersCard"
import MyOrders from "./components/MyOrders"
import MyProfile from "./components/MyProfile"
import MyAddress from "./components/MyAddress"
import ChangePassword from "./components/ChangePassword"
import CheckOut from "./components/CheckOut"
import Categories from "./components/Categories"
import OrderConfirm from "./components/OrderConfirm"
import OrderView from "./components/OrderView"
import ItemView from "./components/ItemView"
import Category from "./components/Category";
import SubCategory from "./components/SubCategory";
import Brand from "./components/Brand";
import Stock from "./components/Stock";
import AddOrEditForm from "./components/AddOrEditForm";
import Imports from './components/Imports';
import SyncModule from './SyncModule'
import { DBConfig } from './data/DBConfig';
import { initDB } from 'react-indexed-db';
import SellerDetails from './components/SellerDetails';
import StockMap from './components/StockMap';
import PlaceOrder from './components/PlaceOrder'
import AllOrders from './components/AllOrders'
import OrderProcess from './components/OrderProcess';
import DashBoard from './components/DashBoard';
import Users from './components/Users';
import ViewInfo from './components/ViewInfo';
import TableInfo from './components/TableInfo'
import Search from './components/Search';
import Settings from './components/Settings';
import PriceList from './components/PriceList';
import AddPriceList from './components/AddPriceList';
import Sellers from './components/Sellers';
import Offers from './components/Offers'
import BarcodeScanner from './components/BarcodeScanner'
import AcceptedOrders from './components/AcceptedOrders';
import QRReader from './service/QRReader';
import Signature from './components/Signature';
import VideoRecord from './components/VideoRecord';

import Subscription from "./components/Subscription";
import SubscribePlan from "./components/SubscribePlan";
import Reports from "./components/Reports";
import ReportView from "./components/ReportView";
import ReportDashBoard from "./components/ReportDashBoard";



initDB(DBConfig);
class RouterLinks extends Component {

    render() {
        return (
            <div >
                <Switch>
                    <Route exact path="/" component={Login} />
                    <Route exact path="/app" component={App} />
                    <Route exact path="/signeup" component={SingeUp} />
                    <Route path="/sync" component={SyncModule} />
                    <App>
                        <Route component={({ match }) =>
                            <>
                                <Route path="/app/home" component={Home} />
                                <Route path="/app/products" component={Products} />
                                <Route path="/app/offerscard" component={OffersCard} />
                                <Route path="/app/myorders" component={MyOrders} />
                                <Route path="/app/mycart" component={Cart} />
                                <Route path="/app/profile" component={MyProfile} />
                                <Route path="/app/myaddress" component={MyAddress} />
                                <Route path="/app/changepassword" component={ChangePassword} />
                                <Route path="/app/savedata" component={AddData} />
                                <Route path="/app/categories" component={Categories} />
                                <Route path="/app/about" component={About} />
                                <Route path="/app/table" component={DataTableBasicDemo} />
                                <Route path="/app/checkout" component={CheckOut} />
                                <Route path="/app/orderconfirm" component={OrderConfirm} />
                                <Route path="/app/orderview" component={OrderView} />
                                <Route path="/app/itemview" component={ItemView} />
                                <Route path="/app/category" component={Category} />
                                <Route path="/app/subcategory" component={SubCategory} />
                                <Route path="/app/brand" component={Brand} />
                                <Route path="/app/stocks" component={Stock} />
                                <Route path="/app/stocksseller" component={Stock} />
                                <Route path="/app/addoredit" component={AddOrEditForm} />
                                <Route path="/app/imports" component={Imports} />
                                <Route path="/app/sellerdetails" component={SellerDetails} />
                                <Route path="/app/mapstock" component={StockMap} />
                                <Route path="/app/placeorder" component={PlaceOrder} />
                                <Route path="/app/allorders" component={AllOrders} />
                                <Route path="/app/pendingord" component={AllOrders} />
                                <Route path="/app/acceptedord" component={AllOrders} />
                                <Route path="/app/packedord" component={AllOrders} />
                                <Route path="/app/readyforpickup" component={AllOrders} />
                                <Route path="/app/outfordelivery" component={AllOrders} />
                                <Route path="/app/deliveredord" component={AllOrders} />
                                <Route path="/app/orderprocess" component={OrderProcess} />
                                <Route path="/app/dashboard" component={DashBoard} />
                                <Route path="/app/sellers" component={Users} />
                                <Route path="/app/adminusers" component={Users} />
                                <Route path="/app/sellerusers" component={Users} />
                                <Route path="/app/consumers" component={Users} />
                                <Route path="/app/admindelivery" component={Users} />
                                <Route path="/app/sellerdelivery" component={Users} />
                                <Route path="/app/viewinfo" component={ViewInfo} />
                                <Route path="/app/tableinfo" component={TableInfo} />
                                <Route path="/app/search" component={Search} />
                                <Route path="/app/settings" component={Settings} />
                                <Route path="/app/sellersettings" component={Settings} />
                                <Route path="/app/sellerimports" component={Imports} />
                                <Route path="/app/pricelist" component={PriceList} />
                                <Route path="/app/addpricelist" component={AddPriceList} />
                                <Route path="/app/consellers" component={Sellers} />
                                <Route path="/app/offers" component={Offers} />
                                <Route path="/app/scanner" component={VideoRecord}/>
                                <Route path="/app/acceptedorders" component={AcceptedOrders}/>
                                <Route path="/app/pickuporders" component={AcceptedOrders}/>
                                <Route path="/app/deliveryorders" component={AcceptedOrders}/>
                                <Route path="/app/deliveryall" component={AcceptedOrders}/>
                                <Route path="/app/subscription" component={Subscription}/>
                                <Route path="/app/subscribeplan" component={SubscribePlan}/>
                                <Route path="/app/reports" component={Reports}/>
                                <Route path="/app/reportview" component={ReportView}/>
                                <Route path="/app/report-dashboard" component={ReportDashBoard}/>
                                
                                                  
                            </>
                        } />
                    </App>
                </Switch>
            </div>


        );
    }
}

export default RouterLinks;
