import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { useHistory } from "react-router-dom";
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"


function Category() {
  const productService = new ProductService();
  const history = useHistory();

  

  const onButtunClick = (data, type) => {
  
    if (type === 'add') {
      const fs = productService.getDynamicFrom('category');
      let reqData = {
        actType: "add", type: "delivery", table: "category", formJson: fs,
        class: "col-sm-12", url: "/product/add-category", pageurl: "/app/category", header: "Add Category"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    }
    else if (type === 'edit') {
      const fs = productService.getDynamicFrom('category');
      fs.name.value = data.name;
      for (var i = 0; i < fs.orderBy.options.length; i++) {
        if (fs.orderBy.options[i].key === data.orderBy) {
          fs.orderBy.value = fs.orderBy.options[i];
          break;
        }
      }
      fs.image.path = APP_URL + "/user/image?path=" +data.image;
      let reqData = {
        data: data, actType: "edit", type: "delivery", table: "category", formJson: fs,
        class: "col-sm-12", url: "/product/update-category", pageurl: "/app/category", header: "Edit Category"
        ,key:"categoryId" };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');

    }
  }

  return (
    <div className="content">
      <h4>Category</h4>
     
       <DyanamicTable type="category" lazy={false} 
        url="/product/get-category"
        onButtonClik={(e, type) => onButtunClick(e, type)} />

    </div>
  )
} export default Category;