import { useRef, useState, useEffect } from 'react';
import { useIndexedDB } from 'react-indexed-db';
import './Home.css';
import { useHistory } from "react-router-dom";
import { Button } from 'primereact/button';
import DynamicForm from '../service/DynamicForm';
import { Dialog } from 'primereact/dialog';
import ProductService from '../service/ProductService';
import APP_URL from "../service/APIConfig"
import APIService from "../service/APIService"
import { Toast } from 'primereact/toast';

function MyProfile() {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [visible, setVisible] = useState(false);
    const [formJson, setFormJson] = useState();
    const productService = new ProductService();
    const userDB = useIndexedDB('users');
    const toast = useRef(null);
    const history = useHistory();
    const growlMsg = (type, message) => {
        toast.current.show({ severity: type, summary: '', detail: message });

    }

    const openUpdatePrifile = () => {
        let up = productService.getDynamicFrom('updateprofile');
        up.email.value = userData.email;
        up.mobileNo.value = userData.mobileNo;
       
        up.image.path = APP_URL + "/user/image?path=" + userData.image;
        let reqData = { data: userData, actType: "edit", type: "updateprofile", table: "users", formJson: up, class: "col-sm-6", url: "/user/updateuser", pageurl: "/app/profile", header: "Edit Profile" };
        APIService.setLocalStorage('reqData', JSON.stringify(reqData));
        history.push('/app/addoredit');

    }



    const onHide = (e) => {
        setVisible(false);
    }

    return <div>
        <Toast className="toast-demo" ref={toast} />
        <h4>My Profile</h4>
        <div align="center" className="profile-info">
            <div>
                <td colSpan="2" align="center" > <img className="profile-img"
                    src={APP_URL + "/user/image?path=" + userData.image} style={{ width: '150', height: '150px', display: 'block', cursor: 'grab' }} /></td>
            </div><br />
            <div>
                <span className="label">Name</span><br />
                <span className="value">{userData.name}</span>
            </div> <div>
                <span className="label">Mobile</span><br />
                <span className="value">{userData.mobileNo}</span>
            </div>
            <div>
                <span className="label">Email</span><br />
                <span className="value">{userData.email}</span>
            </div>
           <br />
            <div>
                <Button icon="pi pi-user-edit" onClick={openUpdatePrifile} label="Edit" className="p-button-rounded p-button-secondary"  ></Button>
            </div><br />

        </div>

    </div>
} export default MyProfile;
