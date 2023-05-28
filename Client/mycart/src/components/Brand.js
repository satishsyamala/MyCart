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

function Brand() {
  const productService = new ProductService();
  const history = useHistory();





  const onButtunClick = (rowData, type) => {
    const fs = productService.getDynamicFrom('brand');
    let filt = { type: "category" };
    if (type === 'edit') {
      filt.action = "edit";
      filt.catId = rowData.categoryId;
     
    }
    APIService.postRequest('/product/get-cat-options', filt).then(data => {
      fs.categoryId.options = data.data.category;
      let reqData = {};
      if (type === 'add') {
        reqData = {
          actType: "add", type: "delivery", table: "brands", formJson: fs,
          class: "col-sm-12", url: "/product/add-brands", pageurl: "/app/brand", header: "Add Brand"
        };
      } else if (type === 'edit') {
        fs.subCategoryId.options = data.data.subcategory;
        fs.name.value = rowData.name;
        fs.categoryId.options = data.data.category;
        fs.categoryId.value = { name: rowData.catName, key: rowData.categoryId }
        fs.subCategoryId.value = { name: rowData.subCatName, key: rowData.subCategoryId }
        fs.image.path = APP_URL + "/user/image?path=" + rowData.image;
        reqData = {
          data: rowData,
          actType: "edit", type: "delivery", table: "brands", formJson: fs,
          class: "col-sm-12", url: "/product/update-brands", pageurl: "/app/brand", header: "Edit Brand"
          , key: "brandId"
        };
      }
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    });



  }




  return (
    <div className="content">
      <h4>Brands</h4>

      <DyanamicTable type="brand" lazy={true}
        url="/product/get-brands"
        onButtonClik={(e, type) => onButtunClick(e, type)} />
    </div>
  )
} export default Brand;