import React, { useState, Component, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';

function DataTableBasicDemo(props) {
    const productService = new ProductService();
    const userDb = useIndexedDB("users");
    const [tableData, setTableData] = useState(productService.getProductsSmall());
   

   


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
        tableData && (<DyanamicTable type="sample" data={tableData} onDelete={(e)=> deleteData(e)} onSubmit={(e) => registerUser(e)} />)
        }
       </div>
} export default DataTableBasicDemo;