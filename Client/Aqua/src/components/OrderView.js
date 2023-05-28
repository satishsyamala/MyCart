import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import { Button } from 'primereact/button';
import { useHistory } from "react-router-dom";
import { Divider } from 'primereact/divider';
import { DataView } from 'primereact/dataview';
import { Rating } from 'primereact/rating';
import Moment from 'moment';
import { Timeline } from 'primereact/timeline';
import ProductService from '../service/ProductService';
import { Card } from 'primereact/card';
import Barcode from "react-barcode";
import { useReactToPrint } from 'react-to-print';
import APP_URL from "../service/APIConfig"
import { ScrollTop } from 'primereact/scrolltop';
import APIService from "../service/APIService"
import { Accordion, AccordionTab } from 'primereact/accordion';
import { Dialog } from 'primereact/dialog';
import MapContainer from '../components/MapContainer'
import QRCode from 'react-qr-code';
import VideoPlayer from 'react-video-js-player';;

function OrderView() {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const [orderId, setOrderId] = useState(inputs.orderid);
    const [orderInfo, setOrderInfo] = useState();
    const [visible, setVisible] = useState(false);
    Moment.locale('en');
    const history = useHistory();
    const ordersDB = useIndexedDB('orders');
    const productService = new ProductService();
    const componentRef = useRef();


    useEffect(() => {
        window.scrollTo(0, 0);
        let fil = { order_id: orderId }
        APIService.postRequest('/transaction/order-details', fil).then(data => {
            if (data) {
                data.data.qr_code = { user_id: data.data.user_id, seller_id: data.data.seller_id, order_id: data.data.order_id };
                setOrderInfo(data.data);
            }

        });


    }, []);


    const device = (data) => {
        return <div className="p-col-12 p-md-6">
            <div className="product-list-item">
                <div className="product-list-detail">
                    <i className="pi pi-tag product-category-icon"></i><span className="product-category">{data.cat_name}</span>
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
                </div>
                <div className="product-list-detail1">
                    <div className="dev-product-name">{data.name}</div>
                    <span className="c">Quantity : {data.quantity}</span>

                    <div className="c"><span> {data.pack_size}&nbsp;</span></div>
                    <div className="c"> Rs : {data.price}</div>

                </div>


            </div>
        </div>

    }




    const renderListItem = (data) => {
        return device(data);
    }

    const addPreSpace = (input) => {
        let temp = ':        :';
        let var1 = temp.substring(0, 10 - (input + '').length) + input;
        return var1;
    }
    const addPreSpaceright = (input) => {
        let temp = '        ';
        let var1 = input + temp.substring(0, 10 - (input + '').length);
        return var1;
    }

    const back = (fromPage) => {
        history.push('/app/' + fromPage);
    }

    const customizedMarker = (item) => {
        return (
            <span className="custom-marker p-shadow-2" style={{ backgroundColor: item.color }}>
                <i className={item.icon}></i>
            </span>
        );
    };

    const handlePrint = useReactToPrint({
        content: () => componentRef.current,
    });

    const onHide = (e) => {
        setVisible(false);

    }

    const onLocationSet = (a, b) => {

    }


    return (
        <div className="content">
            <br />
            {orderInfo && <div className="order_view" ref={componentRef} >
                <h4>No. {orderInfo.order_no}
                    <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                        <Button style={{ height: '30px', marginLeft: '5px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning" onClick={(e) => back('myorders')} />
                        <a href={APP_URL + "/transaction/order-pdf?orderid=" + orderInfo.order_id} >
                            <Button style={{ height: '30px', marginLeft: '5px' }} icon="pi pi-file-pdf"
                                tooltip="PDF" className="p-button-raised p-button-warning" /></a>
                    </div>
                </h4>



                <Divider type="dashed"><b>Summary</b></Divider>
                <div className="card" style={{ paddingLeft: "20px" }}>
                    <span className="dev-product-name">Name &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.name}</span>
                    <span className="dev-product-desc">Order Date &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.order_date}</span>
                    {
                        orderInfo.status === 'Delivered' ? <span className="dev-product-desc">Delivered Date &nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;&nbsp;{orderInfo.act_delivery_date}</span> :
                            <span className="dev-product-desc">Delivery Date &nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;&nbsp;{orderInfo.delivery_date}</span>
                    }
                    <span className="dev-product-desc">No. Of Items &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.items.length}</span>
                    <span className="dev-product-desc">Total Amount &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.final_price}</span>
                    <span className="dev-product-desc">Status &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.status}</span>
                    <span className="dev-product-desc">Delivery Type &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.delivery_type}</span>

                    <br /> </div>


                <Accordion >
                    <AccordionTab header="QR Code">
                        <div className="card">
                            <QRCode value={JSON.stringify(orderInfo.qr_code)} /> <br /> <br />
                        </div>
                    </AccordionTab>
                    <AccordionTab header="Video">
                       
                            <VideoPlayer
                                controls={true}
                                src={APP_URL + "/import/video?path=mycart/" + orderId + "/record.webm"}
                                width="300"
                                height="300" />
                        
                    </AccordionTab>
                    <AccordionTab header="Seller">
                      
                            <span className="dev-product-name">{orderInfo.seller.shop_name}</span>
                            <p>{orderInfo.seller.address}</p>
                            <span className="dev-product-desc">Mobile No : {orderInfo.seller.mobile_no}</span><br/>
                            <span>
                                <a href={'http://maps.google.com/maps?q=loc:' + orderInfo.seller.lotitude + ',' + orderInfo.seller.longitude} target="_blank" >Location</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#" onClick={(e) => setVisible(true)} >Map</a></span>
                    
                    </AccordionTab>
                    <AccordionTab header="Track">
                        <div className="timeline-demo">
                           
                                <Timeline value={orderInfo.track}
                                    opposite={(item) => <small className="p-text-secondary">{addPreSpaceright(item.status)}</small>} content={(item) => <small className="p-text-secondary">{item.date}</small>} marker={customizedMarker} />

                                {orderInfo.otp > 0 &&
                                    <span className="dev-product-name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{orderInfo.otp} OTP for {orderInfo.delivery_type} </span>
                                }
                            </div>
                    </AccordionTab>
                    <AccordionTab header="Delivery Address">
                      
                            <span className="dev-product-name">{orderInfo.full_name}</span>
                            <p>{orderInfo.address}</p>
                            <span className="dev-product-desc">Mobile No : {orderInfo.moblie_no}</span>
                      
                    </AccordionTab>


                    <AccordionTab header="Price Details">
                        <div align="right" style={{ paddingRight: "20px" }} >
                            <div className="dev-product-desc">Total Price {addPreSpace(orderInfo.price)}</div>
                            <div className="dev-product-desc">Discount {addPreSpace(orderInfo.discount)}</div>

                            <div className="dev-product-desc">Delivery Charge {addPreSpace(orderInfo.delivery_charge)}</div>

                            <div className="dev-product-name">Final Price {addPreSpace(orderInfo.final_price)}</div>
                            <div className="dev-product-desc">Payment Type  {addPreSpace(orderInfo.payment_type.toUpperCase())}</div>
                        </div>
                    </AccordionTab>
                    <AccordionTab header="Items">
                        <div className="dataview-demo">
                           
                                <DataView value={orderInfo.items} layout="list" itemTemplate={renderListItem} />
                           
                        </div>
                    </AccordionTab>
                </Accordion>


                <Dialog header="Map" visible={visible} onHide={() => onHide()} >
                    <div style={{ width: "360px", height: "300px" }} > <MapContainer lat={orderInfo.seller.lotitude} keyType="text" lon={orderInfo.seller.longitude} onSubmit={(key, loc) => onLocationSet(key, loc)} />
                    </div>
                </Dialog>


            </div>


            }
            <ScrollTop icon="pi pi-angle-double-up" />
            <br /> <br />
        </div>
    )
}
export default OrderView;
