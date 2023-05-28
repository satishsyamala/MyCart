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
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';
import { ProgressBar } from 'primereact/progressbar';
import { Tooltip } from 'primereact/tooltip';
import { Tag } from 'primereact/tag';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { InputNumber } from 'primereact/inputnumber';
import { Checkbox } from 'primereact/checkbox';

function Stock(props) {
  const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
  const productService = new ProductService();
  const history = useHistory();
  const menu = props.match.url.replace('/app/', '');
  const toast = useRef(null);
  const [imageWin, setImageWin] = useState(false);
  const [totalSize, setTotalSize] = useState(0);
  const fileUploadRef = useRef(null);
  const [stockItem, setStockitem] = useState();
  const [stockPrice, setStockPrice] = useState(false);
  const [stockJson, setStockJson] = useState();

  const growlMsg = (type, message) => {
    toast.current.show({ severity: type, summary: '', detail: message });
  }

  const onButtunClick = (rowData, type) => {
    const fs = productService.getDynamicFrom('stock');
    let reqData = {};
    if (type === 'add') {
      reqData = {
        actType: "add", type: "stock", table: "stock_items", formJson: fs,
        class: "col-sm-6", url: "/product/add-stock-item", pageurl: "/app/" + menu, header: "Add Stock"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/addoredit');
    } else if (type === 'edit') {
      let check = true;
      if (menu === 'stocksseller' && userData.seller_id !== rowData.sellerId)
        check = false;
      if (check) {
        fs.name.value = rowData.name;
        fs.categoryId.value = { name: rowData.catName, key: rowData.categoryId }
        fs.subCategoryId.value = { name: rowData.subCatName, key: rowData.subCategoryId }
        fs.brandId.value = { name: rowData.brandName, key: rowData.brandId }
        fs.code.value = rowData.code;
        fs.code.readonly = true;
        fs.description.value = rowData.description;
        let pk=[];
        for(var i=0;i<rowData.packSize.length;i++)
        {
          let p={name:rowData.packSize[i].name,key:rowData.packSize[i].key,price:0.0,quantity:rowData.packSize[i].quantity,default_pack:0}
          pk.push(p);
        }
      
        fs.packSize.value = pk;
        fs.image.path = APP_URL + "/user/image?path=" + rowData.image;
        reqData = {
          data: rowData,
          actType: "edit", type: "stock", table: "stock_items", formJson: fs,editpac:rowData.packSize,
          class: "col-sm-6", url: "/product/update-stock-item", pageurl: "/app/" + menu, header: "Edit Stock"
          , key: "stockItemId"
        };
        APIService.setLocalStorage('reqData', JSON.stringify(reqData));
        history.push('/app/addoredit');
      }
      else {
        growlMsg("error", "No access to edit item");
      }
    } else if (type === 'view') {
      let filter = { stock_item_id: rowData.stockItemId, module_name: 'stock_item' };
      let reqData = {
        filter: filter, image: true,
        class: "col-sm-6", url: "/user/get-view-data", pageurl: "/app/" + menu, header: "Stock View"
      };
      APIService.setLocalStorage('reqData', JSON.stringify(reqData));
      history.push('/app/viewinfo');
    } else if (type === 'export') {

      APIService.postRequestFile('/product/stock-export', rowData).then(res => {
        const url = window.URL.createObjectURL(
          new Blob([res.data]),
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute(
          'download',
          `Stock Items.xls`,
        );
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }

      );
    } else if (type === 'update price') {
      console.log(JSON.stringify(rowData));
      setStockPrice(true);
      setStockJson(rowData);
     

    } else if (type === 'add images') {
      setStockitem(rowData);
      setImageWin(true);
    }



  }

  const textElement = (rowData) => {
    return <InputNumber className="tableinputtext"
        value={rowData.price} onChange={(e) => (rowData.price = e.value)} />;
}

const setChecked = (chek, rowData) => {
    let temp = { ...stockJson };
    if (chek) {
        let ar = stockJson.packSize;
        for (var i = 0; i < ar.length; i++) {
            ar[i].default_pack = 0;

        }
      
    }
    rowData.default_pack = chek ? 1 : 0;
    setStockJson(temp);

}

const checkBoxEle = (rowData) => {
    return <Checkbox inputId="binary" checked={rowData.default_pack === 1} onChange={e => setChecked(e.checked, rowData)} />
}

const saveStockData = () => {

  APIService.postRequest('/user/update-price', stockJson).then(data => {
      let del = data.data;
      if (del.reason === 'success') {
        setStockPrice(false);
      }
      else {
          growlMsg('error', del.reason);
      }
  });
}


  const getURL = () => {
    if (menu === 'stocks')
      return "get-stock-admin";
    else
      return "get-stock-seller";
  }



  const onUpload = () => {
    toast.current.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
  }

  const onTemplateSelect = (e) => {
    let _totalSize = totalSize;
    Object.keys(e.files).map((key, ind) => {
      createFile(e.files[key].objectURL).then(file => {
        _totalSize += file.size;
        setTotalSize(_totalSize);
      });
    });



  }

  const onTemplateUpload = (e) => {
    let _totalSize = 0;

    let images = [];
    Object.keys(e.files).map((key, ind) => {
      createFile(e.files[key].objectURL).then(file => {
        const fileReader = new FileReader();
        fileReader.onload = (e) => {
          let ob = { stock_item_id: stockItem.stockItemId, image: e.target.result };
          APIService.postRequestFile('/import/stock-image', ob).then(res => {

          });
        };
        fileReader.readAsDataURL(file);
        _totalSize += file.size;
        setTotalSize(_totalSize);
      });
    });





    toast.current.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
  }

  async function createFile(url) {
    let response = await fetch(url);
    let data = await response.blob();
    let metadata = {
      type: 'image/jpeg'
    };
    let file = new File([data], "test.jpg", metadata);
    return file;
  }

  const onTemplateRemove = (file, callback) => {
    setTotalSize(totalSize - file.size);
    callback();
  }

  const onTemplateClear = () => {
    setTotalSize(0);
  }

  const onBasicUpload = () => {
    toast.current.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded with Basic Mode' });
  }

  const onBasicUploadAuto = () => {
    toast.current.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded with Auto Mode' });
  }

  const headerTemplate = (options) => {
    const { className, chooseButton, uploadButton, cancelButton } = options;
    const value = totalSize / 10000;
    const formatedValue = fileUploadRef && fileUploadRef.current ? fileUploadRef.current.formatSize(totalSize) : '0 B';

    return (
      <div className={className} style={{ backgroundColor: 'transparent', display: 'flex', alignItems: 'center' }}>
        {chooseButton}
        {uploadButton}
        {cancelButton}
        <ProgressBar value={value} displayValueTemplate={() => `${formatedValue} / 1 MB`} style={{ width: '300px', height: '20px', marginLeft: 'auto' }}></ProgressBar>
      </div>
    );
  }

  const itemTemplate = (file, props) => {
    return (
      <div className="p-d-flex p-ai-center p-flex-wrap">
        <div className="p-d-flex p-ai-center" style={{ width: '40%' }}>
          <img alt={file.name} role="presentation" src={file.objectURL} width={100} />
          <span className="p-d-flex p-dir-col p-text-left p-ml-3">
            {file.name}
            <small>{new Date().toLocaleDateString()}</small>
          </span>
        </div>
        <Tag value={props.formatSize} severity="warning" className="p-px-3 p-py-2" />
        <Button type="button" icon="pi pi-times" className="p-button-outlined p-button-rounded p-button-danger p-ml-auto" onClick={() => onTemplateRemove(file, props.onRemove)} />
      </div>
    )
  }

  const emptyTemplate = () => {
    return (
      <div className="p-d-flex p-ai-center p-dir-col">
        <i className="pi pi-image p-mt-3 p-p-5" style={{ 'fontSize': '5em', borderRadius: '50%', backgroundColor: 'var(--surface-b)', color: 'var(--surface-d)' }}></i>
        <span style={{ 'fontSize': '1.2em', color: 'var(--text-color-secondary)' }} className="p-my-5">Drag and Drop Image Here</span>
      </div>
    )
  }

  const chooseOptions = { icon: 'pi pi-fw pi-images', iconOnly: true, className: 'custom-choose-btn p-button-rounded p-button-outlined' };
  const uploadOptions = { icon: 'pi pi-fw pi-cloud-upload', iconOnly: true, className: 'custom-upload-btn p-button-success p-button-rounded p-button-outlined' };
  const cancelOptions = { icon: 'pi pi-fw pi-times', iconOnly: true, className: 'custom-cancel-btn p-button-danger p-button-rounded p-button-outlined' };


  return (
    <div className="content">
      <h4>Stock Items</h4>
      <Toast className="toast-demo" ref={toast} />
      <DyanamicTable type="stock" lazy={true} pageSize={250}
        url={"/product/" + getURL()}
        onButtonClik={(e, type) => onButtunClick(e, type)} />


      <Dialog header="Add Images" style={{ width: "70%" }} visible={imageWin} onHide={() => setImageWin(false)}>
        <FileUpload ref={fileUploadRef} name="demo[]" customUpload={true} multiple accept="image/*" maxFileSize={1000000}
          uploadHandler={onTemplateUpload} onSelect={onTemplateSelect} onError={onTemplateClear} onClear={onTemplateClear}
          headerTemplate={headerTemplate} itemTemplate={itemTemplate} emptyTemplate={emptyTemplate}
          chooseOptions={chooseOptions} uploadOptions={uploadOptions} cancelOptions={cancelOptions} />
      </Dialog>

      <Dialog header="Stock Price" visible={stockPrice} onHide={(e)=>setStockPrice(false)} >
                <div className="card">
                    {stockJson && <div>
                        <DataTable value={stockJson.packSize} responsiveLayout="scroll">
                            <Column field="key" header="Code"></Column>
                            <Column field="name" header="Name"></Column>
                            <Column className="numberColWith" header="Price" body={textElement}></Column>
                            <Column body={checkBoxEle} header="Default"></Column>
                        </DataTable>

                        <center>
                        <Button style={{ height: '30px' }}  className="p-button-raised p-button-info " label='Update'
                            onClick={e => saveStockData()} /> </center>   </div>
                    }
                </div>
            </Dialog>


    </div>
  )
} export default Stock;