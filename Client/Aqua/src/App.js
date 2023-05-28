import React, { Component } from 'react';
import logo from './logo.svg';
import { Menubar } from 'primereact/menubar';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { withRouter } from 'react-router-dom';
import { Menu } from 'primereact/menu';
import {
  Route,
  Link,
  Switch,
  Redirect
} from 'react-router-dom';
import DataJson from './data/DataJson';
import { Badge } from 'primereact/badge';
import { connect } from "react-redux";
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Dialog } from 'primereact/dialog';
import { OverlayPanel } from 'primereact/overlaypanel';
import { unstable_getThreadID } from 'scheduler/tracing';
import APP_URL from "./service/APIConfig"
import { Sidebar } from 'primereact/sidebar';
import { PanelMenu } from 'primereact/panelmenu';
import GeoCoords from "./service/GeoCoords"
import APIService from "./service/APIService"
import QRReader from './service/QRReader';
import { Card } from 'primereact/card';
import { ToggleButton } from 'primereact/togglebutton';
import { InputSwitch } from 'primereact/inputswitch';
import { useSelector, useDispatch } from 'react-redux';



class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeIndex: 3,
      menuItems: [],
      loginUserName: '',
      displaylogin: false,
      userName: '',
      password: '',
      loggedOut: false,
      cartvalue: 0,
      imagePath: '',
      windowWidth: window.innerWidth,
      searchText: '',
      visibleLeft: false,
      iscond: true,
      isdelivery: false,
      address: '',
      displayBasic: false,
      orderders: null,
      qrwin: false,
      seller_id: 0,
      location: true,
      userdata: null,
      days_exp: 0,
      sub_win: false,
      user_type: null,
      menuBar: false,
      lastLogin: null

    };
    const interval = 0;
    const tokencheck = 0;
    this.dataJson = new DataJson();
    this.geoCoords = new GeoCoords();
    // APIService.setLocalStorage('myData','satish');

  }


  componentDidMount() {
    if (APIService.getLocalStorage('myData')) {
      this.setMenuItems();
      this.tokenCheck();
      const user = JSON.parse(APIService.getLocalStorage('myData'));
      const fil = { user_id: user.userId, latest_info_id: user.latest_info_id }
      if (user.delivery_type !== 'no') {
        this.syncLocation(fil);
      }
    }
    else {
      this.props.history.push('/');
    }
  }

  syncLocation(input) {
    this.interval = setInterval(() => {
      input.latitude = this.geoCoords.getLatitude();
      input.longitude = this.geoCoords.getLongitude();
      // if (input.latitude && input.longitude) {
      APIService.postRequest('/delivery/location', input).then(res => {
        if (res.data.notification === 'yes') {

          this.setState({ displayBasic: true });
          this.setState({ orderders: res.data.orderds });
        }
      });
      // }
    }, 60000);
  }

  tokenCheck() {
    this.tokencheck = setInterval(() => {
      APIService.tokenUpdate();
    }, 300000);
  }

  setMenuItems() {
    if (APIService.getLocalStorage('myData')) {
      let userObj = JSON.parse(APIService.getLocalStorage('myData'));
      this.setState({ userdata: userObj })
      this.setState({ loginUserName: userObj.name });
      this.setState({ menuItems: JSON.parse(APIService.getLocalStorage('MenuItems')) });
      this.setState({ imagePath: userObj.image });
      this.setState({ iscond: userObj.iscons });
      this.setState({ address: userObj.address });
      this.setState({ isdelivery: userObj.isdelivery });
      this.setState({ seller_id: userObj.seller_id });
      this.setState({ user_type: userObj.userType });
      this.setState({ lastLogin: userObj.last_login });


      let exp = JSON.parse(APIService.getLocalStorage('myData')).days_exp;

      this.setState({ days_exp: exp });
      if (exp < 6) {
        if (!APIService.getLocalStorage('first')) {
          APIService.setLocalStorage('first', 1);
          this.setState({ sub_win: true });
        }
      }

    }
    else {
      this.setState({ loginUserName: '' });
      this.setState({ menuItems: [] });
    }

  }



  logout() {
    const user = JSON.parse(APIService.getLocalStorage('myData'));
    if (user.delivery_type !== 'no') {
      this.stopLocationdata(false, user);
    }
    localStorage.removeItem('myData');
    localStorage.removeItem('MenuItems');
    localStorage.removeItem('inputs');
    localStorage.removeItem('first');
    localStorage.removeItem('token');

    this.setMenuItems();
    this.setState({ loggedOut: true });
    clearInterval(this.tokencheck);
  }

  stopLocationdata(value, user) {
    this.setState({ location: value });
    if (value) {
      const fil = { user_id: user.userId, latest_info_id: user.latest_info_id }
      if (user.delivery_type !== 'no') {
        this.syncLocation(fil);
      }

    } else {
      clearInterval(this.interval);
    }
    let inp = { active: value ? 1 : 0, user_id: user.userId }
    APIService.postRequest('/delivery/active-or-inactive', inp);
  }


  componentWillReceiveProps(nextProps) {
    const { cartvalue: nextTest } = nextProps;
    const { cartvalue } = this.props;

    if (nextTest !== cartvalue) {
      this.setState({ cartvalue: nextTest });
    }
  }


  moveToCart() {
    this.props.history.push('/app/mycart');
  }
  myProfile() {
    this.props.history.push('/app/profile');
    this.op.hide();
  }

  page(name) {
    this.props.history.push('/app/' + name);
  }

  itemSearch() {
    let inputs = JSON.parse(APIService.getLocalStorage('inputs'));
    inputs.category = null;
    inputs.sub_cat = null;
    inputs.brands = null;
    inputs.searchtext = this.state.searchText;
    APIService.setLocalStorage('inputs', JSON.stringify(inputs));
    this.props.history.push('/app/products');
    this.setState({ visibleLeft: false });
  }

  onMenuClick(name) {
    alert(name);
  }

  acceptOrder(data, status) {
    data.action = status;
    APIService.postRequest('/delivery/accept-order', data).then(res => {
      if (res.data.reason === 'success') {
        this.setState({ displayBasic: false });
        this.props.history.push('/app/acceptedorders');
      }
    });

  }

  renderGridItem(data) {

    return <div className="p-col-12 p-md-12" >

      <div align="center">
        <div className="product-description">Shop name : {data.shop_name}</div>
        <img className="productimage" src={APP_URL + "/user/image?path=" + data.image}
          onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
      </div>
      <div className="product-name">{data.order_no}</div>
      <div className="dev-product-address">Shop Address : {data.sel_addres}</div>
      <div className="dev-product-address">Delivery Address : {data.delivery_address}</div>
      <div align="center" style={{ padding: "10px" }}>
        <Button icon="pi pi-times" label="Reject" className='p-button-danger' onClick={() => this.acceptOrder(data, 'Reject')} />
        <Button icon="pi pi-check" label="Accept" className='p-button-success' onClick={() => this.acceptOrder(data, 'Accept')} /></div>



    </div>
  }

  openScanWin() {
    this.setState({ qrwin: true });
  }


  scannCode(e) {
    let json = JSON.parse(e);
    if (json.seller_id === this.state.seller_id) {
      let inputs = {};
      inputs.orderid = json.order_id;
      inputs.pickup_otp = json.pickup_otp;
      inputs.order_delivery_user_map_id = json.order_delivery_user_map_id;
      APIService.setLocalStorage('inputs', JSON.stringify(inputs));
      this.props.history.push('/app/orderprocess');
      this.setState({ qrwin: false });
    }
    else {
      alert("Invalid Order");
    }
  }

  moveTosubscriptioPage() {
    this.setState({ sub_win: false });
    this.op.hide();
    this.props.history.push('/app/subscribeplan');

  }

  getPageHeader() {

    let pgane = this.props.person.name;
    let ln = pgane.length;
    for (var i = ln; i < 15; i++) {
      pgane = pgane + ' ';
    }
    if (ln === 0)
      return null
    else
      return pgane;

  }


  render() {


    let end = <div align="right" >
      <i hidden={!this.state.iscond}  className="pi pi-shopping-cart p-mr-2 p-text-secondary p-overlay-badge"
        onClick={() => this.moveToCart()} style={{ marginTop: "5px",width: "30px" }}>
        {
          this.props.cartCount > 0 ? <Badge size="small" value={this.props.cartCount} /> : ''
        }
      </i>
      <Button hidden={(this.state.user_type === 'Seller' && this.state.userdata.delivery_type === 'no') ? false : true} icon="pi code-scanner" className="p-button-rounded p-button-info p-button-text"
        onClick={() => this.openScanWin()} style={{ fontSize: '23px', fontWeight: "2000", color: "green" }}>
      </Button>
      {
        this.state.userdata && this.state.userdata.delivery_type === 'Admin' ? <InputSwitch checked={this.state.location} onChange={(e) => this.stopLocationdata(e.value, this.state.userdata)} /> : ''
      }
    </div>


    const start = <img src={process.env.PUBLIC_URL + "/logo2.png"} style={{ width: "30px", height: "30px" }} alt="Logo" />


    const footer = (
      <span>
        <Button onClick={() => this.moveTosubscriptioPage()} label="Subscribe" icon="pi pi-play" className="p-button-raised p-button-danger" />
      </span>
    );

    return (

      <div> {
        this.state.loggedOut && (<Redirect to="/" />)
      }

        <Sidebar visible={this.state.visibleLeft} position="top" onHide={() => this.setState({ visibleLeft: false })}>
          <div align="center">
            <br />


          </div>
        </Sidebar>
        <div className="App-header sticky">
          <div className='row' >
            <div className='col'>  <Button icon="pi pi-align-justify" className='p-button-rounded p-button-info p-button-text' onClick={() => this.setState({ 'menuBar': true })} style={{ width: "30px !important" }} />  </div>
            <div align="center" >
           <span className='hearder'>AQUA</span> 
            </div>
            <div className='col' > {end}</div>
          </div>


        </div>
        <Sidebar showCloseIcon={false} visible={this.state.menuBar} closable onHide={() => this.setState({ 'menuBar': false })}>
          <div className='menu_bar_headred'>
            <div>
              <table>
                <tr>
                  <td style={{ padding: "5px" }}>  <img className="profile-img"
                    src={APP_URL + "/user/image?path=" + this.state.imagePath} style={{ width: '60px', height: '60px', display: 'block', cursor: 'grab' }} /></td>
                  <td style={{ padding: "5px" }}><span style={{ fontSize: "14  x" }}>{this.state.loginUserName}</span><br />
                    <span style={{ fontSize: "10x" }}> Last Login {this.state.lastLogin}</span>
                  </td>
                  <td style={{ padding: "5px" }}>   <Button icon="pi pi-power-off" className="p-button-rounded p-button-danger" onClick={() => this.logout()} /></td>
                </tr>
              </table>


            </div>
          </div>
          <PanelMenu model={this.state.menuItems} />

        </Sidebar>

        <Dialog visible={this.state.displayBasic} header="Accept Order" style={{ width: '90%' }} closable={false} >
          {this.state.orderders &&
            <div className="dataview-demo">
              <div className="card">
                {this.renderGridItem(this.state.orderders[0])} </div> </div>}


        </Dialog>
        <Dialog visible={this.state.qrwin} className="qr-sccaner-win" onHide={() => this.setState({ qrwin: false })} >
          <QRReader onScann={(e) => this.scannCode(e)} />
        </Dialog>

        <Dialog visible={this.state.sub_win} onHide={() => this.setState({ sub_win: false })} >
          <Card title="Subscription" style={{ width: '25em' }} footer={footer} >
            {
              this.state.days_exp < 0 ? <p className="p-m-0" style={{ lineHeight: '1.5' }}>Your plan is expired please subscribe</p> : <p className="p-m-0" style={{ lineHeight: '1.5' }}>You plan is going to expire in {this.state.days_exp} days please subscribe</p>
            }
          </Card>



        </Dialog>










        <div className="contentinfo">
          {this.props.children}
        </div>

        <footer className='footer' hidden={!this.state.iscond}>
          <div className="row footer-icons" style={{ width: "100%", margin: "auto" }}>
            <div align="center" style={{ width: "33%" }}>
              <Button icon="pi pi-home" onClick={(e) => this.page('home')}   style={{ width: "40px", height: "40px" }} className="p-button-rounded p-button-text" />
            </div>
            <div align="center" style={{ width: "33%" }}>
              <Button icon="pi pi-search" onClick={(e) => this.page('search')}  style={{ width: "40px", height: "40px" }} className="p-button-rounded  p-button-text" />
            </div>
         
            <div align="center" style={{ width: "33%" }}>
              <Button icon="pi pi-book" onClick={(e) => this.page('myorders')}  style={{ width: "40px", height: "40px" }} className="p-button-rounded  p-button-text" />
            </div>
          </div>
        </footer>




      </div>
    );
  }
}
const mapStateToProps = state => ({
  cartCount: state.cart ? state.cart.cartCount : 0,
  pageName: state.pageName ? state.pageName : "Home",
  person: state.characters

});

export default withRouter(connect(mapStateToProps, null)(App));

