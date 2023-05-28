import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"
import { useHistory } from "react-router-dom";
import {useSelector, useDispatch} from 'react-redux';

function AllOrders(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
    const productService = new ProductService();
    const history = useHistory();
    const menu = props.match.url.replace('/app/','');
    const dispatch = useDispatch();
   
    useEffect(() => {
        dispatch({type: 'CHANGE_NAME', payload:'Orders'});
      }, []);


    const onButtunClick = (rowData, type) => {
        inputs.orderid = rowData.order_id;
        inputs.frompage = menu;
        APIService.setLocalStorage('inputs', JSON.stringify(inputs));
        history.push('/app/orderprocess');
    }



    const getheaderName = () => {
        if (menu === 'allorders')
            return 'Orders';
        else if (menu === 'pendingord')
            return 'Pending Orders';
        else if (menu === 'acceptedord')
            return 'Accepted Orders';
        else if (menu === 'packedord')
            return 'Assigned Orders';
        else if (menu === 'readyforpickup')
            return 'Ready For Pickup';
        else if (menu === 'outfordelivery')
            return 'Out For Delivery ';
        else if (menu === 'deliveredord')
            return 'Delivered';
    }

    const getStatus = () => {
        if (menu === 'allorders')
            return '';
        else if (menu === 'pendingord')
            return 'Order Placed';
        else if (menu === 'acceptedord')
            return 'Accepted';
        else if (menu === 'packedord')
            return 'Assigned';
        else if (menu === 'readyforpickup')
            return 'Ready For Pickup';
        else if (menu === 'outfordelivery')
            return 'Out For Delivery';
        else if (menu === 'deliveredord')
            return 'Delivered';
    }


    return (
        <div className="content">
          <h4>{getheaderName()}</h4>

            <DyanamicTable type="allorders" lazy={true} pageSize={250}
                url="/transaction/all-orders" filter_status={getStatus()} menu_name={menu}
                onButtonClik={(e, type) => onButtunClick(e, type)} />


        </div>
    )
} export default AllOrders;