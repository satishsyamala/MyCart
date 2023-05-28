import React, { useState, useRef, useEffect } from 'react';
import { PhotoService } from '../service/PhotoService';
import { Galleria } from 'primereact/galleria';
import { Carousel } from 'primereact/carousel';
import { Button } from 'primereact/button';
import './Home.css';
import { useHistory } from "react-router-dom";
import { useIndexedDB } from 'react-indexed-db';
import ProductService from '../service/ProductService';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
function Categories(props)
{
   const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
   const [inputs, setInputs] = useState(JSON.parse(APIService.getLocalStorage('inputs')));
   const categoryDB = useIndexedDB('category');
   const [category, setCategory] = useState();
   const [label, setLabel] = useState();
   const [selCat, setSelCat] = useState();
   const sub_categoryDB = useIndexedDB('sub_category');
   const brandsDB = useIndexedDB('brands');
   const productService = new ProductService();
   const history = useHistory();

   useEffect(() => {
      inputs.category = null;
      inputs.sub_cat = null;
      inputs.brands = null;
      inputs.offer = null;
      inputs.searchtext=null;
      inputs.seller=null;
      localStorage.removeItem('selectedcat');
      categoryDB.getAll().then(data => {
          productService.sortJSONArray(data, 'name', 'asc');
          setCategory(data);
          setLabel('Category');
          setSelCat('Category');
      });


  }, []);

  

  const onImageSelect = (items) => {
      let redirect = true;
      
      if (items!==null && label === 'Category') {
          inputs.category = items;
          sub_categoryDB.getAll().then(data => {
              let res = [];
              data.map(item => {
                  if (item.categoryId === items.categoryId) {
                      res.push(item);
                  }
              })
              if (res && res.length > 0) {
                  productService.sortJSONArray(res, 'name', 'asc');
                  setCategory(res);
                  setLabel('Sub Category');
                  setSelCat(items.name);
                  redirect = false;
              }
              else {
                  APIService.setLocalStorage('inputs', JSON.stringify(inputs));
                  history.push('/app/products');
              }
          })
      } else if (items!==null && label === 'Sub Category') {
          inputs.sub_cat = items;
          brandsDB.getAll().then(data => {
              let res = [];
              data.map(item => {
                  if (item.subCategoryId === items.subCategoryId) {
                      res.push(item);
                  }
              })
              if (res && res.length > 0) {
                  productService.sortJSONArray(res, 'name', 'asc');
                  setCategory(res);
                  setLabel('Brand');
                  setSelCat(items.name);
                  redirect = false;
              }
              else {
                  APIService.setLocalStorage('inputs', JSON.stringify(inputs));
                  history.push('/app/products');
              }
          })

      } else {
          inputs.brands = items;
          APIService.setLocalStorage('inputs', JSON.stringify(inputs));
          history.push('/app/products');
      }

  }



  const itemTemplate = (item) => {
      return item && <img src={APP_URL +"/user/image?path=" + item.image} alt={item.alt} onClick={(e) => onOfferSelect(item)} style={{ width: '100%', height: (window.innerWidth > 750 ? 280 : 200) + 'px', display: 'block', cursor: 'grab' }} />;
  }

  const thumbnailTemplate = (item) => {
      return <img className="product-image" src={APP_URL +"/user/image?path=" + item.image} onClick={(e) => onImageSelect(item)}
          alt={item.category} style={{ width: [(window.innerWidth > 750 ? 60 : 40) + 'px'], height: [(window.innerWidth > 750 ? 60 : 40) + 'px'], display: 'block', cursor: 'grab' }} />
  }



  const headerImages = (item) => {
      return <div className="column" align="center">
          <img className="product-image" src={APP_URL +"/user/image?path="+  item.image} onClick={(e) => onImageSelect(item)}
              alt={item.name} style={{ width: [(window.innerWidth > 750 ? 200 : 100) + 'px'], height: [(window.innerWidth > 750 ? 200 : 100) + 'px'], cursor: 'grab' }} />
          <br /> <span> {item.name}</span>
      </div>

  }

  const onOfferSelect = (item) => {
      inputs.category = null;
      inputs.sub_cat = null;
      inputs.brands = null;
      inputs.searchtext=null;
      inputs.offer = item;
      APIService.setLocalStorage('inputs', JSON.stringify(inputs));
      history.push('/app/products');
  }




  return (<div className="content">
     <h4>{selCat}</h4>
     <div className="categories_dvi">
      <div className="p-col-12 total-card row">
     
               <div className="column" align="center">
                  <img tooltip="Category" className="product-image" src={process.env.PUBLIC_URL + '/showcase/demo/images/list.png'} onClick={(e) => onImageSelect(null)}
                      style={{ width: [(window.innerWidth > 750 ? 200 : 100) + 'px'], height: [(window.innerWidth > 750 ? 200 : 100) + 'px'], cursor: 'grab' }}  />
                  <br />  <span> All</span>
              </div>
             
              {
                  category && category.map((item) =>
                      headerImages(item)
                  )
              }

      </div>
     
      </div>

  </div>
  );


}export default Categories;
