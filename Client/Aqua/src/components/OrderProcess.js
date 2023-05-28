import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import { Button } from 'primereact/button';
import { useHistory } from "react-router-dom";
import { Divider } from 'primereact/divider';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import Moment from 'moment';
import { Timeline } from 'primereact/timeline';
import ProductService from '../service/ProductService';
import Barcode from "react-barcode";
import { useReactToPrint } from 'react-to-print';
import APP_URL from "../service/APIConfig"
import { ScrollTop } from 'primereact/scrolltop';
import APIService from "../service/APIService"
import { Accordion, AccordionTab } from 'primereact/accordion';
import { Dialog } from 'primereact/dialog';
import { Toast } from 'primereact/toast';
import { InputText } from 'primereact/inputtext';
import { RadioButton } from 'primereact/radiobutton';
import { Dropdown } from 'primereact/dropdown';
import VideoRecorder from 'react-video-recorder';
import VideoPlayer from 'react-video-js-player';;

function OrderProcess() {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const [fromPage, setFromPage] = useState(inputs.frompage ? inputs.frompage : 'allorders');
    const [orderId, setOrderId] = useState(inputs.orderid);
    const [orderInfo, setOrderInfo] = useState();
    const [otp, setOtp] = useState(inputs.pickup_otp ? inputs.pickup_otp : '');
    const [visible, setVisible] = useState(false);
    Moment.locale('en');
    const history = useHistory();
    const componentRef = useRef();
    const toast = useRef(null);
    const [delOpt, setdelOpt] = useState('other');
    const [selDelUser, setSelDelUser] = useState();
    const [selfUser, setSelfUser] = useState();
    const [videoWin, setVideoWin] = useState(false);
    const [playVideo, setPlayVideo] = useState(false);


    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }

    useEffect(() => {
        window.scrollTo(0, 0);
        let fil = { order_id: orderId }
        APIService.postRequest('/transaction/order-details', fil).then(res => {
            setOrderInfo(res.data);
        });
        let fil1 = { userId: userData.userId, seller_id: userData.seller_id, delivery_user: "yes" };
        APIService.postRequest('/user/get-seller-users', fil1).then(res => {
            setSelfUser(res.data);
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
                    {
                        data.diccount > 0 ? <div className="product-price-offer">
                            <span> {data.pack_size}{data.uom}&nbsp;&nbsp;&nbsp;</span>  Rs : {data.diccount * data.quantity}&nbsp;&nbsp;&nbsp;
                            <s>{data.price * data.quantity}</s>
                        </div> : <div className="product-price-offer"><span> {data.pack_size}{data.uom}&nbsp;&nbsp;&nbsp;</span> Rs : {data.price * data.quantity}</div>
                    }

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

    const getBtnLable = () => {

        if (orderInfo.status === 'Order Placed') {
            return "Accepted";
        } else if (orderInfo.delivery_type === 'Delivery') {
            if (orderInfo.status === 'Accepted') {
                return "Assigned";
            } else if (orderInfo.status === 'Assigned') {
                if (orderInfo.assign_status === 'Accept')
                    return "Out For Delivery";
                else
                    return 'Waiting for delivery user acceptabce ';
            } else if (orderInfo.status === 'Out For Delivery') {
                return "Delivered";
            }
        } else {
            if (orderInfo.status === 'Accepted') {
                return "Ready For Pickup";
            } else if (orderInfo.status === 'Ready For Pickup') {
                return "Delivered";
            }
        }
        return 'Completed';



    }

    const orderProcess = () => {

        if (!hiddeOTP() || otp) {
            let check = true;
            if (delOpt === 'self' && !selDelUser)
                check = false;
            if (check) {
                orderInfo.enterotp = otp;
                orderInfo.del_opt = delOpt;
                orderInfo.sel_user = selDelUser;
                orderInfo.order_delivery_user_map_id = inputs.order_delivery_user_map_id;
                APIService.postRequest('/transaction/order-process', orderInfo).then(res => {
                    if (res.data.reason === 'success') {
                        inputs.orderid = null;
                        inputs.pickup_otp = null;
                        inputs.order_delivery_user_map_id = null;
                        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
                        setVisible(true);
                    } else {
                        growlMsg('error', res.data.reason);
                    }

                });
            } else {
                growlMsg('error', "Please Delivery Delivery User");
            }
        } else {
            growlMsg('error', "Please enter OTP");
        }
    }

    const hiddeBtn = () => {
        if (orderInfo.status === 'Delivered' || orderInfo.status === 'Out For Delivery' || (orderInfo.status === 'Assigned' && orderInfo.assign_status !== 'Accept')) {
            return true;
        } else
            if (orderInfo.seller_id === 0 || (orderInfo.seller_id > 0 && orderInfo.seller_id === userData.seller_id))
                return false;
            else
                return true;

    }

    const hiddeOTP = () => {
        if (orderInfo.status === 'Ready For Pickup' || (orderInfo.status === 'Assigned' && orderInfo.assign_status === 'Accept'))
            return true;
        else
            return false;
    }


    const OTPHeader = () => {
        if (orderInfo.status === 'Ready For Pickup')
            return "OTP";
        else if (orderInfo.status === 'Assigned' && orderInfo.assign_status === 'Accept')
            return "Pickup OTP";
    }

    const onUserSelect = (e) => {
        setSelDelUser(e.value);
    }

    const onupload = (value, path) => {
        console.log(value);
        let d = { video: value, orderId: orderId };
        APIService.postRequestFile('/import/save-video', d).then(data => {
            setVideoWin(false);
        });
    }


    return (
        <div className="main-div">
            <br />
            <Toast className="toast-demo" ref={toast} />
            {orderInfo && <div className="order_view"  >
                <h4>No. {orderInfo.order_no}
                    <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                        <Button style={{ height: '30px', marginLeft: '5px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning" onClick={(e) => back(fromPage)} />
                        <a href={APP_URL + "/transaction/order-pdf?orderid=" + orderInfo.order_id} >
                            <Button style={{ height: '30px', marginLeft: '5px' }} icon="pi pi-file-pdf"
                                tooltip="PDF" className="p-button-raised p-button-warning" /></a>
                    </div>
                </h4>
                <div>
                    <Button disabled={hiddeBtn()} style={{ width: '100%', height: "40px", margin: "auto" }} label={getBtnLable()} className="p-button-raised p-button-secondary" onClick={(e) => orderProcess()} />


                </div>
                <Button label="Take Video" onClick={(e) => setVideoWin(true)} />
                <Button label="Play Video" onClick={(e) => setPlayVideo(true)} />
                <br />
                <div ref={componentRef}>
                    <div >
                        {hiddeOTP() &&
                            <div >
                                <div>
                                    {OTPHeader()}
                                </div>
                                <div>
                                    <InputText keyfilter="int" value={otp} onChange={(e) => setOtp(e.target.value)} />
                                </div>
                            </div>
                        }
                        {getBtnLable() === 'Assigned' &&
                            <div >
                                <div>Delivery Options</div>
                                <div className="p-field-radiobutton">
                                    <RadioButton inputId="city1" name="city" value="self" onChange={(e) => setdelOpt(e.value)} checked={delOpt === 'self'} />
                                    <label htmlFor="city1">Self Delivery</label>

                                    <RadioButton inputId="city2" name="city" value="other" onChange={(e) => setdelOpt(e.value)} checked={delOpt === 'other'} />
                                    <label htmlFor="city2">Delivery Boy</label>
                                </div>
                                <div >
                                    {selfUser && delOpt === 'self' && <Dropdown value={selDelUser} options={selfUser} onChange={onUserSelect} optionLabel="first_name" placeholder="Select User" />
                                    }  </div>
                            </div>
                        }



                    </div>


                    <div >  <Divider type="dashed"><b>Summary</b></Divider>
                        <div className="card">
                            <span className="dev-product-name">Name &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.name}</span>
                            <span className="dev-product-desc">Order Date &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.order_date}</span>
                            {
                                orderInfo.status === 'Delivered' ? <span className="dev-product-desc">Delivered Date &nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;&nbsp;{orderInfo.act_delivery_date}</span> :
                                    <span className="dev-product-desc">Delivery Date &nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;&nbsp;{orderInfo.delivery_date}</span>
                            }  <span className="dev-product-desc">No. Of Items &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.items.length}</span>
                            <span className="dev-product-desc">Total Amount &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.final_price}</span>
                            <span className="dev-product-name">Status &nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp; {orderInfo.status}</span>

                            <br />
                        </div> </div>


                    <Divider type="dashed"><b>Delivery Address</b></Divider>
                    <div  >

                        <div className="card" >
                            <span className="dev-product-name">{orderInfo.full_name}</span>
                            <span className="dev-product-name">Mobile No : {orderInfo.moblie_no}</span>
                            <span className="dev-product-name">Del. Type : {orderInfo.delivery_type}</span>
                            <p>{orderInfo.address}</p>

                        </div>
                    </div>



                    <Divider type="dashed"><b>Items</b></Divider>
                    <div className="order-table">

                        <DataTable className="order-table" value={orderInfo.items} items="data" var1="data">
                            <Column field="name" header="Name"></Column>
                            <Column field="pack_uom" header="UOM"></Column>
                            <Column className="number-col" field="quantity" header="Quantity"></Column>
                            <Column className="number-col" field="final_price" header="Amount"></Column>
                        </DataTable>


                    </div>
                    <div align="right" style={{ paddingRight: "20px" }} className="card">
                        <span className="dev-product-desc">Total Price {addPreSpace(orderInfo.price)}</span>
                        <span className="dev-product-desc">Discount {addPreSpace(orderInfo.discount)}</span>
                        <span className="dev-product-desc">Delivery Charge {addPreSpace(orderInfo.delivery_charge)}</span>
                        <span className="dev-product-name">Final Price {addPreSpace(orderInfo.final_price)}</span>
                        <span className="dev-product-desc">Payment Type  {addPreSpace(orderInfo.payment_type.toUpperCase())}</span>
                    </div>
                </div>

                <br />
                <div>
                    <Button hidden={hiddeBtn()} style={{ width: '100%', height: "40px", margin: "auto" }} label={getBtnLable()} className="p-button-raised p-button-secondary" onClick={(e) => orderProcess()} />
                </div>
                <Accordion >
                    <AccordionTab header="Track">
                        <div className="timeline-demo">
                            <div className="card">
                                <Timeline value={orderInfo.track}
                                    opposite={(item) => <small className="p-text-secondary">{addPreSpaceright(item.status)}</small>} content={(item) => <small className="p-text-secondary">{item.date}</small>} marker={customizedMarker} />
                            </div></div>
                    </AccordionTab>

                </Accordion>

                <br /><br /><br />
            </div>
            }
            <ScrollTop icon="pi pi-angle-double-up" />
            <Dialog closable={false} className="register_dl" visible={visible} >
                <h6>Order Processed Successfuly</h6>
                <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning " label='Back'
                    onClick={e => back(fromPage)} />
            </Dialog>
            <Dialog closable={true} style={{ width: "100%", maxWidth: "350px" }} visible={videoWin} onHide={(e) => setVideoWin(false)}>
                <VideoRecorder
                    onRecordingComplete={(videoBlob) => {
                        const fileReader = new FileReader();
                        fileReader.onload = (e) => {
                            onupload(e.target.result);
                        };
                        fileReader.readAsDataURL(videoBlob);


                    }}
                />
            </Dialog>

            <Dialog closable={true} style={{ width: "100%", maxWidth: "350px" }} visible={playVideo} onHide={(e) => setPlayVideo(false)}>
                <VideoPlayer
                    controls={true}
                    src={APP_URL + "/import/video?path=mycart/" + orderId + "/record.webm"}
                    width="320"
                    height="320" />
            </Dialog>
        </div>
    )
}
export default OrderProcess;
