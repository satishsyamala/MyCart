import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';

function CheckOut()
{
    const cartDB = useIndexedDB('cart_items');
    const [deliverAdd,setDeliverAdd]=useState();
    const delvAdd=useIndexedDB('delivery_address');

    useEffect(()=>{
        delvAdd.getAll().then(data=>setDeliverAdd(data));
    },[]);

return <div>
    CheckOut
</div>
}export default CheckOut;
