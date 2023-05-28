import React, { useState, Component, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';

function AddData(props) {
    const productService = new ProductService();
    const userDb = useIndexedDB("users");
    const [tableData, setTableData] = useState();
    const [waitfordata, setWaitfordata] = useState(false);
    const [headers, setHeaders] = useState(productService.getDynamicTableHeader('users'));


    useEffect(() => {
        userDb.getAll().then(userDate => {
           
            let jsn = {};
            jsn.header = headers;
            jsn.data = userDate;
            setTableData(jsn);
           
          
        }
        );
    }, []);


    const registerUser = (e) => {
        alert(JSON.stringify(e));
    }

    const deleteData =(e)=>{
        alert(e.id);
        userDb.deleteRecord(e.id).then(event => {
            alert('Deleted!');
          }); 
    }


    return <div> {
        tableData && (<DyanamicTable type="users" data={tableData}
         onDelete={(e)=> deleteData(e)} onSubmit={(e) => registerUser(e)} />)
           }
       </div>
} export default AddData;