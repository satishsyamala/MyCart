import React from 'react';
import { useRef, useState, useEffect } from 'react';
import APIService from "../service/APIService"
import { useSelector, useDispatch } from 'react-redux';
import './Home.css';
import { DataView } from 'primereact/dataview';
import { useHistory } from "react-router-dom";
import DynamicForm from '../service/DynamicForm';
import APP_URL from "../service/APIConfig"
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { Dialog } from 'primereact/dialog';
import { ScrollTop } from 'primereact/scrolltop';
import { Bar } from 'react-chartjs-2';
import { Line } from 'react-chartjs-2';
import { Pie } from 'react-chartjs-2';
import { Doughnut } from 'react-chartjs-2';


function ReportView(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [reprotName, setReportNames] = useState();
    const [reprotData, setReportData] = useState();
    const [chartData, setChartData] = useState();
    const dispatch = useDispatch();
    const history = useHistory();
    const [filter, setFilter] = useState(false);
    const [visible, setVisible] = useState(false);
    const [filterObj, setFilterObj] = useState({});
    const [rowCount, setRowCount] = useState(0);
    const [reportJson, setReportJson] = useState(0);
    const [viewBt, setViewBt] = useState(false);
    const [visibleCT, setVisibleCT] = useState(false);
    const [chartTable, setChartTable] = useState();


    useEffect(() => {
        let repJson = JSON.parse(APIService.getLocalStorage('report_json'));
        setReportJson(repJson);
        setViewBt(repJson.chart_type === 'table' ? false : true);
        dispatch({ type: 'CHANGE_NAME', payload: 'Report' });
        let fl = { chart_type: repJson.chart_type, userId: userData.userId, userType: userData.userType, seller_id: userData.seller_id, size: 200, dynamic_reports_id: repJson.dynamic_reports_id, first: 0, name: repJson.name };
        APIService.postRequest('/report/reports-json', fl).then(res => {
            if (res) {
                setReportNames(res.data)
                fl.filters = res.data.form;
                getTableData(fl);
                setFilterObj(fl);
            }
        });
    }, []);

    const footer = (tooltipItems) => {
        let sum = 0;

        tooltipItems.forEach(function (tooltipItem) {
            sum += tooltipItem.parsed.y;
        });
        return 'Sum: ' + sum;
    };

    const options = {
        scales: {
            yAxes: [
                {
                    ticks: {
                        beginAtZero: true,
                    },
                },
            ],
        }, interaction: {
            intersect: false,
            mode: 'index',
        },
        plugins: {
            tooltip: {
                callbacks: {
                    footer: footer,
                }
            }, legend: {
                display: true,
                labels: {
                    usePointStyle: true,
                },
            }
        }
    };


    const back = (report) => {
        history.push("/app/reports");
    }

    const getReportData = (e) => {

        let filter = { ...filterObj };
        filter.filters = e;
        setFilterObj(filter);
        getTableData(filter);
        setVisible(false);
    }

    const getTableData = (filters) => {
        APIService.postRequest('/report/reports-data', filters).then(data => {
            if (data) {

                if (filters.chart_type === 'table') {
                    setRowCount(data.data.length);
                    setReportData(data.data);
                } else {
                    console.log(JSON.stringify(data.data[0]))
                    setChartData(data.data[0]);
                }


            }
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

    const imageBodyTemplate = (rowData) => {
        return <img src={APP_URL + '/user/image?path=' + rowData.image} onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={rowData.image} className="product-image" />;
    }


    const getColumnFile = (key) => {

        if (key.type === 'number') {
            return <Column style={{ width: "200px" }} field={key.key} header={key.key}></Column>
        }
        else if (key.type === 'image') {
            return <Column header={key.key} body={imageBodyTemplate}></Column>
        }
        else {
            return <Column style={{ width: "200px" }} field={key.key} header={key.key}></Column>
        }
    }

    const exportData = () => {
        let tmp = { ...filterObj };
        tmp.first = 0;
        tmp.size = 100000;
        APIService.postRequestFile('/report/report-export', tmp).then(res => {
            if (res) {
                const url = window.URL.createObjectURL(
                    new Blob([res.data]),
                );
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute(
                    'download',
                    reprotName.name + `.xls`,
                );
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
        }
        );

    }

    const checkFileter = (fil) => {
        let val = 0;
        Object.keys(fil).map((key, ind) => {
            if (!fil[key].hidde) {
                val = val + 1;
            }
        });
        if (val > 0)
            return false;
        else
            return true;
    }

    const openDataTable = (chartData, type) => {
        setChartTable(APIService.lineAndbarChartTotable(chartData));
        setVisibleCT(true);

    }

    const rowClass = (data) => {
 
        return {
            'row-accessories': data.label === 'Total'
        }
      }

    const dynamicColumns = chartTable && chartTable.header.map((col,i) => {
        return <Column key={col.key}  field={col.key} header={col.name} />;
    });

    return (
        <div className="content">
            <br />
            <h4>{reprotName && reprotName.name}
                <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                    <Button style={{ height: '30px', marginLeft: '5px' }} label="Back" icon="pi pi-chevron-left" className="p-button-raised p-button-warning" onClick={(e) => back('myorders')} />

                </div>
            </h4>
            <div className="datatable-templating-demo">
                {!viewBt && <div >
                    <div className="p-grid">
                        <div className="p-col-6 table-btns" align="left">
                            <Button type="submit" hidden={viewBt} icon="pi pi-arrow-left" disabled={filterObj.first === 0 ? true : false} onClick={(e) => pageDown()} className="p-button-rounded p-button-secondary" />
                            <Button type="submit" hidden={viewBt} icon="pi pi-arrow-right" disabled={rowCount < filterObj.size ? true : false} onClick={(e) => pageUp()} className="p-button-rounded  p-button-secondary" />
                        </div>
                        <div className="p-col-6 table-btns" align="right">
                            <Button type="submit" hidden={viewBt} tooltip="Filter" onClick={(e) => setVisible(true)} icon="pi pi-filter" className="p-button-rounded  p-button-success" />
                            <Button type="submit" hidden={viewBt} tooltip="Export" onClick={(e) => exportData()} icon="pi pi-file-excel" className="p-button-rounded  p-button-help" />

                        </div>
                    </div>
                </div>
                }
                {reprotName && <div>{reprotName.chart_type === 'table' ?
                    <div style={{ overflow: "auto" }} >


                        <DataTable lazy className="DTtable" value={reprotData}>
                            {
                                reprotName.header.map((data, idx) => (
                                    getColumnFile(data)
                                )
                                )
                            }

                        </DataTable>
                        <ScrollTop className="custom-scrolltop" icon="pi pi-arrow-up" />
                    </div> : <div ><center>
                        <br />
                        {chartData && <div style={{ maxWidth: "1000px", maxHeight: "600px" }} >
                            <Button icon="pi pi-filter" disabled={checkFileter(reprotName.form)} style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => setVisible(true)} className="p-button-rounded p-button-text p-button-plain" />
                            <Button  icon="pi pi-table" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openDataTable(chartData,'')} className="p-button-rounded p-button-text p-button-plain" />
                            {
                                reprotName.chart_type === 'bar' ?
                                    <Bar style={{ maxWidth: "1000px", maxHeight: "600px" }} data={chartData} options={options} />
                                    :
                                    reprotName.chart_type === 'line' ? <Line style={{ maxWidth: "1000px", maxHeight: "600px" }} data={chartData} options={options} />
                                        : reprotName.chart_type === 'pie' ? <Pie style={{ maxWidth: "1000px", maxHeight: "600px" }} data={chartData} options={options} />

                                            : <Doughnut style={{ maxWidth: "1000px", maxHeight: "600px" }} data={chartData} options={options} />
                            }
                        </div>}
                    </center>
                    </div>






                }
                </div>
                }

                <Dialog header="Filter" style={{ width: "100%" }} className="register_dl" visible={visible} onHide={() => setVisible(false)}>
                    {reprotName && <DynamicForm type="report" formJson={reprotName.form} clname="col-sm-12" onSubmit={(e) => getReportData(e)} />}
                </Dialog>

                <Dialog modal={true} style={{width:"100%",maxWidth:"600px"}} visible={visibleCT} onHide={() => setVisibleCT(false)}>
                      {
                         chartTable &&   <DataTable className="DTtable" rowClassName={rowClass}  value={chartTable.data}>
                         {dynamicColumns}
                     </DataTable>
                      }
                </Dialog>
            </div>

        </div>
    )

}
export default ReportView;