import React from 'react';
import { useRef, useState, useEffect } from 'react';
import APIService from "../service/APIService"
import { useSelector, useDispatch } from 'react-redux';
import { DataView } from 'primereact/dataview';
import { useHistory } from "react-router-dom";
import DynamicForm from '../service/DynamicForm';
import APP_URL from "../service/APIConfig"
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { OverlayPanel } from 'primereact/overlaypanel';
import { ScrollTop } from 'primereact/scrolltop';
import { Bar } from 'react-chartjs-2';
import { Line } from 'react-chartjs-2';
import { Bubble } from 'react-chartjs-2';
import './Dashboard.css';
import { Pie } from 'react-chartjs-2';
import { Doughnut } from 'react-chartjs-2';
import { Dialog } from 'primereact/dialog';

function ReportDashBoard(props) {
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [chartData, setChartData] = useState();
    const dispatch = useDispatch();
    const history = useHistory();
    const [filter, setFilter] = useState();
    const op = useRef(null);
    const [filterObj, setFilterObj] = useState({});
    const [reportJson, setReportJson] = useState();
    const [chartTable, setChartTable] = useState();
    const [visibleCT, setVisibleCT] = useState(false);
    const [filterWin, setFilterWin] = useState(false);



    useEffect(() => {
        let repJson = JSON.parse(APIService.getLocalStorage('report_json'));
        setReportJson(repJson);
        dispatch({ type: 'CHANGE_NAME', payload: 'Report' });
        let fl = { chart_type: repJson.chart_type, userId: userData.userId, userType: userData.userType, seller_id: userData.seller_id, size: 200, dynamic_reports_id: repJson.dynamic_reports_id, first: 0, name: repJson.name };
        getTableData(fl);
        setFilterObj(fl);

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
        let fil = { ...filterObj };
        fil.form = e;
        fil.selected_id = filter.dynamic_reports_id;
        setFilterObj(fil);
        getTableData(fil);
        setFilterWin(false);
    }

    const getTableData = (filters) => {

        APIService.postRequest('/report/reports-dashboard', filters).then(data => {
            if (data) {
                if (filters.selected_id) {
                    let tem = { ...chartData };
                    tem[filters.selected_id] = data.data[filters.selected_id];
                    setChartData(tem);
                } else {
                    setChartData(data.data);
                }
            }
        });
    }

    const openFilterWin = (chat, key, e) => {

        setFilter(chat);
        setFilterWin(true);
    }

    const getColumnFile = (key) => {

        if (key.type === 'number') {
            return <Column field={key.key} header={key.key}></Column>
        }

        else {
            return <Column field={key.key} header={key.key}></Column>
        }
    }

    const chartdata = (key) => {
        if (chartData[key].chart_type === 'bar')
            return <Bar id={key} data={chartData[key].chart} options={options} />
        else if (chartData[key].chart_type === 'line')
            return <Line id={key} data={chartData[key].chart} options={options} />
        else if (chartData[key].chart_type === 'pie')
            return <Pie id={key} data={chartData[key].chart} />
        else if (chartData[key].chart_type === 'doughnut')
            return <Doughnut id={key} data={chartData[key].chart} />
        else if (chartData[key].chart_type === 'table')
            return <div className='table-dash'  >
                <DataTable lazy className="DTtable" value={chartData[key].chart}>
                    {
                        chartData[key].header.map((data, idx) => (
                            getColumnFile(data)
                        )
                        )
                    }
                </DataTable>
                <ScrollTop className="custom-scrolltop" icon="pi pi-arrow-up" />
            </div>

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
        console.log(data.label)
        return {
            'row-accessories': data.label === 'Total'
        }
    }


    const dynamicColumns = chartTable && chartTable.header.map((col, i) => {
        return <Column key={col.key} field={col.key} header={col.name} />;
    });


    return (
        <div className="content">
            <br />
            <h4>{reportJson && reportJson.name}
                <div style={{ Width: '100%', marginTop: '-25px' }} align="right">
                    <Button style={{ height: '30px', marginLeft: '5px' }} label="Back" icon="pi pi-chevron-left" className="p-button-raised p-button-warning" onClick={(e) => back('myorders')} />
                </div>
            </h4>
            <div class="container-fluid">
                <div className="row" >
                    {
                        chartData && chartData.chart_order.map((key, idx) => (
                            <div class="col-sm-4" style={{ paddingBottom: "20px" }}>
                                <h6>{chartData[key].name}
                                    <Button disabled={checkFileter(chartData[key].form)} icon="pi pi-filter" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openFilterWin(chartData[key], key, e)} className="p-button-rounded p-button-text p-button-plain" />
                                    <Button hidden={chartData[key].chart_type === 'table' ? true : false} icon="pi pi-table" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openDataTable(chartData[key].chart, chartData[key].chart_type)} className="p-button-rounded p-button-text p-button-plain" /></h6>
                                {chartdata(key)}


                            </div>
                        ))
                    }
                </div>

                <Dialog style={{ width: "100%", maxWidth: "500px" }} visible={filterWin} onHide={() => setFilterWin(false)}>
                    {filter && <DynamicForm type="report" formJson={filter.form} clname="col-sm-12" onSubmit={(e) => getReportData(e)} />}
                </Dialog>


                <Dialog modal={true} style={{ width: "100%", maxWidth: "600px" }} visible={visibleCT} onHide={() => setVisibleCT(false)}>
                    {
                        chartTable && <div>
                            <div className="card"> <DataTable className="DTtable" rowClassName={rowClass} value={chartTable.data}>
                                {dynamicColumns}
                            </DataTable>  </div>
                        </div>
                    }
                </Dialog>


            </div>
        </div>
    )

}
export default ReportDashBoard;