import React, { useState, Component, useEffect, useRef } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import './Home.css';
import APIService from "../service/APIService"
import { useHistory } from "react-router-dom";
import APP_URL from "../service/APIConfig"


function TableInfo(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [reqData, setReqData] = useState(JSON.parse(APIService.getLocalStorage('reqData')));
    const [viewData, setViewData] = useState();
    const history = useHistory();
    const [filterObj, setFilterObj] = useState({});
    const [rowCount, setRowCount] = useState(0);

    useEffect(() => {
        let fil = reqData.filter
        if (reqData.lazy) {
            fil.first = 0;
            fil.size = 250;
        }
        setFilterObj(fil);
        getTableData(fil)
    }, []);

    const getTableData = (filters) => {
         APIService.postRequest(reqData.url, filters).then(res => {
            setRowCount(res.data.data.length);
            setViewData(res.data);
        });

    }

    const pageDown = () => {
        let filter = { ...filterObj };
        if (filter.first > 0) {
            filter.first = filter.first - filter.size;
            getTableData(filter);
            setFilterObj(filter)
        }
    }
    const pageUp = () => {

        let filter = { ...filterObj };
        if (rowCount == filter.size) {
            filter.first = filter.first + filter.size;
            getTableData(filter);
            setFilterObj(filter)
        }
    }



    const backToPage = (url) => {
        localStorage.removeItem("reqData");
        history.push(url);
    }

    const dynamicColumns = viewData && viewData.headers.map((col,i) => {
        return <Column key={col.key} className={"column-"+col.type} field={col.key} header={col.key} />;
    });

    return (
        
        <div className="content">


<div className="header-container">
            <div className="div-left">
            <h4>{reqData.header}</h4>
            </div>
            <div className="div-right-table">
            <Button style={{ height: '30px' }} icon="pi pi-chevron-left" className="p-button-raised p-button-warning "
                        label='Back' onClick={e => backToPage(reqData.pageurl)} />
            </div>

        </div>


           
               
            <div className="content-section implementation">
                <div className="p-col-6 table-btns" align="left">
                    <Button type="submit" icon="pi pi-arrow-left" disabled={filterObj.first === 0 ? true : false} hidden={!reqData.lazy} onClick={(e) => pageDown()} className="p-button-rounded " />
                    <Button type="submit" icon="pi pi-arrow-right" disabled={rowCount < filterObj.size ? true : false} hidden={!reqData.lazy} onClick={(e) => pageUp()} className="p-button-rounded " />
                </div>
                <div className="card">
                    {viewData &&
                        <DataTable className="DTtable" value={viewData.data}>
                            {dynamicColumns}
                        </DataTable>}
                </div>
            </div>
        </div>
    )

}
export default TableInfo;