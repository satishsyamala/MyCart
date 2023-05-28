import { useRef, useState, useEffect } from 'react';
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Button } from 'primereact/button';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import './dataview123.css';
import { Dialog } from 'primereact/dialog';
import { Card } from 'primereact/card';
import { Toast } from 'primereact/toast';


function SubscribePlan() {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [plans, setPlans] = useState(null);
    const [pendingPlans, setPemdimgPlans] = useState(null);
    const [existPlans, setExistPlans] = useState(null);
    const [payamount, setPayamount] = useState(false);
    const [selPlan, setSelPlan] = useState();
    const [disbtn, setDisbtn] = useState(false);
    const toast = useRef(null);




    useEffect(() => {
        let fil = {};
        let ex = userData.sub_id;

        let exp = [];
        APIService.postRequest('/user/get-subscription-plans', {}).then(res => {
            setPlans(res.data);
            if (ex > 0) {
                for (var i = 0; i < res.data.length; i++) {

                    if (ex === res.data[i].subscription_plans_id) {
                        let e = { ...res.data[i] };
                        e.exp_data = userData.cur_days;
                        exp.push(e);
                        setExistPlans(exp);
                    }
                }
            }
        });
        let f = { seller_id: userData.seller_id, status: 'pending' };
        APIService.postRequest('/user/pending-subscription', f).then(res => {
            setPemdimgPlans(res.data);
            if(res.data.length>0)
            setDisbtn(true);
        });



    }, []);

    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }


    const renderListItem = (data) => {
        return (
            <div className="p-col-12">
                <div className="product-list-item">
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />

                    <div className="product-list-detail">
                        <div className="product-name">{data.name}</div>
                        {data.exp_data && <div className="product-description">Expired On : {userData.exp_date}</div>}
                        <i className="pi pi-tag product-category-icon"></i>
                        {data.exp_data ? <span className="product-category">{data.exp_data} days to go </span> : <span className="product-category">{data.duration_days} days</span>}
                    </div>
                    <div className="product-list-action">
                        <span className="product-price">Rs. {data.amount}</span>
                        {data.exp_data ? '' : <Button label="Subscribe" disabled={disbtn} onClick={(e) => subscribe(data)} icon="pi pi-play" className="p-button-raised p-button-danger"></Button>}

                    </div>
                </div>
            </div>
        );
    }

    const pendingPlansView = (data) => {
        return (
            <div className="p-col-12">
                <div className="product-list-item">
                    <img className="device-img" src={APP_URL + "/user/image?path=" + data.image}
                        onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />

                    <div className="product-list-detail">
                        <div className="product-name">{data.name}</div>
                        <div className="product-description">Paid Date : {data.paid_date}</div>
                        <div className="product-description">Duration : {data.start_date} To {data.end_date}</div>
                        <i className="pi pi-tag product-category-icon"></i>
                        <span className="product-category">{data.duration_days} days</span>
                    </div>
                    <div className="product-list-action">
                        <span className="product-price">Rs. {data.amount}</span>

                    </div>
                </div>
            </div>
        );
    }



    const itemTemplate = (product) => {
        return renderListItem(product);
    }

    const subscribe = (plan) => {
        setSelPlan(plan);
        setPayamount(true);
    }

    const pay = (selPlan) => {
        selPlan.seller_id = userData.seller_id;
        selPlan.user_id = userData.userId;
        APIService.postRequest('/user/add-subscription', selPlan).then(res => {
            if (res.data.reason === 'success') {
                userData.sub_id = selPlan.subscription_plans_id;
                userData.exp_date = res.data.exp_date;
                if (res.data.cur_days)
                    userData.cur_days = res.data.cur_days;
                APIService.setLocalStorage('myData', JSON.stringify(userData));
                window.location.reload()
            }
            else {
                growlMsg('error', res.data.reason);
            }

        });

    }



    return (
        <div className="order_view">
            <Toast className="toast-demo" ref={toast} />
            <h4>Subscription Plans</h4>
            <div className="dataview-demo-test">
                Existing Plan
                <div className="card">

                    <DataView value={existPlans} layout="list"
                        itemTemplate={itemTemplate}
                    />
                </div>

                <br /><br /> Pending Plans
                <div className="card">
                    <DataView value={pendingPlans} layout="list"
                        itemTemplate={pendingPlansView}
                    />
                </div>

                <br /><br /> All Plans
                <div className="card">

                    <DataView value={plans} layout="list"
                        itemTemplate={itemTemplate}
                    />
                </div>
            </div>
            <Dialog visible={payamount} onHide={() => setPayamount(false)} >
                {selPlan &&
                    <Card title={selPlan.name} style={{ width: '25em' }}  >
                        <div className="product-list-item">
                            <img className="device-img" src={APP_URL + "/user/image?path=" + selPlan.image}
                                onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={selPlan.name} />

                            <div className="product-list-detail">
                                <div className="product-name">{selPlan.name}</div>

                                <i className="pi pi-tag product-category-icon"></i>
                                <span className="product-category">{selPlan.duration_days} days</span>
                            </div>
                            <div className="product-list-action">
                                <span className="product-price">Rs. {selPlan.amount}</span>


                            </div>
                            <Button label="Subscribe" onClick={(e) => pay(selPlan)} icon="pi pi-play" className="p-button-raised p-button-danger"></Button>
                        </div>
                    </Card>}
            </Dialog>

        </div>
    );
} export default SubscribePlan;