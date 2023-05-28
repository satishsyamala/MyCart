import { useRef, useState, useEffect } from 'react';
import './Home.css';
import '../App.css';
import ProductService from '../service/ProductService';
import DyanamicTable from '../service/DyanamicTable';
import APIService from "../service/APIService"
import { Toast } from 'primereact/toast';
import APP_URL from "../service/APIConfig"
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { FileUpload } from 'primereact/fileupload';
import { Dialog } from 'primereact/dialog';
import { useHistory } from "react-router-dom";

function Imports(props) {
    const productService = new ProductService();
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [visible, setVisible] = useState(false);
    const toast = useRef(null);
    const [module, setModule] = useState();
    const menu = props.match.url.replace('/app/','');
    const [option, setOtions] = useState(productService.getImportModuleList(menu));
    const history = useHistory();
   
   


    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });
    }

    const onButtunClick = (data, type) => {
         if (type === 'add') {
            setVisible(true);
          
        }
    }

    const downloadfile = () => {
        if (module) {
            const link = document.createElement('a');
            link.href = APP_URL + '/import/template?module=' + module.key;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } else {
            growlMsg('error', 'Please select module');
        }
    }



    const onUpload = ({ files }) => {
      
        if (module) {
           
            const [file] = files;
            const fileReader = new FileReader();
            fileReader.onload = (e) => {
                let req = {
                    userId: userData.userId,sellerId:userData.seller_id, uername: userData.name, module: module.name, modulekey: module.key,
                    uplodated: APIService.getCurrentDate(), file: e.target.result
                }
                
                APIService.postRequest('/import/uploadfile', req).then(data => {
                    let res = data.data;
                    if (res.reason === 'success') {
                        onHide();
                        window.location.reload ()
                    }
                });


            };
            fileReader.readAsDataURL(file);
            return 'success';
        }
        else {
            growlMsg('error', 'Please select module');
            return false;
        }
    };



    const onHide = (e) => {
        setVisible(false);
    }


    return <div className="content">
        <Toast className="toast-demo" ref={toast} />

        <h4>Imports</h4>
        <DyanamicTable type="import"  lazy={true} pageSize="250"
            url="/import/get-import"
            onButtonClik={(e, type) => onButtunClick(e, type)} />

        <Dialog header="Import" visible={visible} onHide={() => onHide()}>
            <div >
                <label htmlFor="module">Module</label><br />
                <Dropdown id="module"
                    value={module} options={option}
                    onChange={(e) => setModule(e.value)} optionLabel="name" filter showClear filterBy="name" placeholder="Module"
                /><br /><br />
                <Button onClick={(e) => downloadfile()} label="Template Download" /><br /><br />
                <FileUpload 
                name="invoice"
                accept=".xls"
                customUpload={true}
                uploadHandler={onUpload}
                maxFileSize={100000000}
                mode="basic"
                auto={true}
                chooseLabel="Upload invoice" 
                
              
                />
            </div>

        </Dialog>

    </div>

} export default Imports;
