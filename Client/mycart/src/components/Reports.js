import React from 'react';
import { useRef, useState, useEffect } from 'react';
import APIService from "../service/APIService"
import { useSelector, useDispatch } from 'react-redux';
import './Home.css';
import './button.css';
import { DataView } from 'primereact/dataview';
import { useHistory } from "react-router-dom";
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';


function Reports(props)  {
   
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [reprotName, setReportNames] = useState();
    const [searchtext, setSearchtext] = useState();
    const [filter, setFilter] = useState();
    const dispatch = useDispatch();
    const history = useHistory();


    useEffect(() => {
        dispatch({ type: 'CHANGE_NAME', payload: 'Reports' });
        let fl = { userId: userData.userId, userType: userData.userType, seller_id: userData.seller_id, size: 10, report_name: '' };
        setFilter(fl);
        getReportlist(fl);
    }, []);

    const getReportlist = (filter) => {
        APIService.postRequest('/report/get-reports', filter).then(res => {
            if (res)
                setReportNames(res.data)
        });
    }

    const searchFilter = () => {

        let temp = { ...filter };
        temp.report_name = searchtext;
        setFilter(temp);
        getReportlist(temp);

    }

    const openReport = (report) => {
        APIService.setLocalStorage("report_json", JSON.stringify(report));
        if (report.chart_type === 'dash')
            history.push("/app/report-dashboard");
        else
            history.push("/app/reportview");
    }

    const reportCards = (data) => {
        return <div className="col-sm card">
            <div id="Item" onClick={(e) => openReport(data)} className="report-item bg-info  text-white">
                <div >
                    <div className="product-name">{data.name}</div>
                    <div className="product-description">{data.description}</div>
                </div>
            </div>
        </div>
    }

    const getIcon = (type) => {
        if (type === 'table')
            return 'pi pi-table px-2';
        else if (type === 'dash')
            return 'pi pi-bars px-2';
        else
            return 'pi pi-chart-bar px-2';
    }

    const getButtonCss = (type) => {
        if (type === 'table')
            return 'discord p-0 p-button-warning';
        else if (type === 'dash')
            return 'discord p-0 p-button-secondary';
        else
            return 'discord p-0 p-button-info';
    }

    return (
        <div className="content">
            <br />
            <h4>Reports
                <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                    <div >
                        <div style={{ width: "250px" }} className="p-inputgroup">
                            <InputText value={searchtext} onChange={(e) => setSearchtext(e.target.value)} placeholder="Search" />
                            <Button className="pi pi-search" onClick={(e) => searchFilter()} />
                        </div>
                    </div>

                </div>
            </h4>
            <div className="dataview-demo">
                <div className='card'>
                    {reprotName && reprotName.map((data, idx) => (
                        <div className='reportgroup'>
                            <span>{data.name}</span>
                            <div className='row' style={{paddingTop:"10px"}}>
                                {data.reports && data.reports.map((rep, idx) => (
                                    <div className='col-sm-4'>
                                        <Button style={{ width: "90%",height:"40px",margin:"5px",fontSize:"18px" }} onClick={(e) => openReport(rep)} className={getButtonCss(rep.chart_type)}>
                                            <i className={getIcon(rep.chart_type)}></i>
                                            <span className="px-3">{rep.name}</span>
                                        </Button>
                                       

                                    </div>

                                ))
                                } </div>
                            <br />
                        </div>

                    ))
                    }
                </div>
            </div>
        </div>
    )

}
export default Reports;