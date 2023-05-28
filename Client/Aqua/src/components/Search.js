import React, { useState,useEffect } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import '../App.css';
import { useHistory } from "react-router-dom";
import { Divider } from 'primereact/divider';
import APIService from "../service/APIService"
import {useSelector, useDispatch} from 'react-redux';

function Search(props) {
    const [value4, setValue4] = useState('');
    const [searchHist, setSearchHist] = useState(APIService.getLocalStorage('searchhis') ? JSON.parse(APIService.getLocalStorage('searchhis')) : []);
    const [searchList, setSearchList] = useState(searchHist);
    const history = useHistory();
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch({type: 'CHANGE_NAME', payload:'Search'});
    }, []);

    const itemSearch = (searchtext) => {
      
        if (searchtext) {
            let check = true;
            if (searchHist) {
                for (let i = 0; i < searchHist.length; i++) {
                    if (searchHist[i].toLowerCase() === searchtext.toLowerCase()) {
                        check = false;
                        break;
                    }
                }
                if (check) {
                    searchHist.push(searchtext);
                    APIService.setLocalStorage('searchhis', JSON.stringify(searchHist));
                }
            }
            else {

                searchHist.push(searchtext);
                APIService.setLocalStorage('searchhis', JSON.stringify(searchHist));
            }
        }
            let inputs = JSON.parse(APIService.getLocalStorage('inputs'));
            inputs.category = null;
            inputs.sub_cat = null;
            inputs.brands = null;
            inputs.seller=null;
            inputs.searchtext = searchtext;
            APIService.setLocalStorage('inputs', JSON.stringify(inputs));
            history.push('/app/products');
    }

    const setSeachItems =(text,list)=>{
        setValue4(text);
        let sh=[];
        for (let i = 0; i < list.length; i++) {
            if (!text || list[i].toLowerCase().includes(text.toLowerCase())) {
                sh.push(list[i]);
            }
        }
        setSearchList(sh);
    }

    const deleteHistory = (item) => {
        let tempAr = [...searchHist];
        const index = tempAr.findIndex(x => x === item);
        if (index !== undefined)
            tempAr.splice(index, 1);
        setSearchHist(tempAr);
        setSeachItems(value4,tempAr);
        APIService.setLocalStorage('searchhis', JSON.stringify(tempAr));
    }

    return (<div>
        <div className="p-inputgroup">
            <InputText placeholder="Search item" value={value4} onChange={(e) => setSeachItems(e.target.value,searchHist)} />
            <Button icon="pi pi-search" onClick={() => itemSearch(value4)} style={{ width: "40px !important" }} />
        </div>
        <div style={{ width: "100%", paddingLeft: "5%" }}>
            <Divider align="left">
                <div className="p-d-inline-flex p-ai-center">
                    <i className="pi pi-search p-mr-2"></i>
                    <b>Search history</b>
                </div>
            </Divider>
            {
                searchList && searchList.map((item) => (
                    <div style={{ width: "100%" }} className="row search-text">
                        <div onClick={() => itemSearch(item)} style={{ width: "90%", paddingLeft: "10px" }} align="left">
                            {item}</div>
                        <div style={{ width: "10%" }} align="right">
                            <Button icon="pi pi-trash" style={{ height: "25px" }} onClick={(e) => deleteHistory(item)} className="p-button-rounded p-button-secondary p-button-text" />
                        </div>
                    </div>
                ))
            }
        </div>



    </div>
    )
} export default Search;