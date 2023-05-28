import { useRef, useState, useEffect } from 'react';
import './Home.css';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import { useHistory } from "react-router-dom";
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"


function SubCategory() {
  const productService = new ProductService();

  const history = useHistory();



  const onButtunClick = (rowData, type) => {

    const fs = productService.getDynamicFrom('subcategory');

      let reqData = {};
      if (type === 'add') {
        reqData = {
          actType: "add", type: "delivery", table: "sub_category", formJson: fs,
          class: "col-sm-12", url: "/product/add-sub-category", pageurl: "/app/subcategory", header: "Add Sub Category"
        };
      } else if (type === 'edit') {
        fs.name.value = rowData.name;
        fs.categoryId.value = { name: rowData.catName, key: rowData.categoryId }
        fs.image.path = APP_URL + "/user/image?path=" + rowData.image;
        reqData = {
          data: rowData,
          actType: "edit", type: "delivery", table: "sub_category", formJson: fs,
          class: "col-sm-12", url: "/product/update-sub-category", pageurl: "/app/subcategory", header: "Edit Sub Category"
          , key: "subCategoryId"
        };
      }
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
   
  }






  return (
    <div className="content">
      <h4>Sub Category</h4>

      <DyanamicTable type="subcategory" lazy={true} 
        url="/product/get-sub-category"
        onButtonClik={(e, type) => onButtunClick(e, type)} />

    </div>
  )
} export default SubCategory;