import React from 'react';
import { useRef, useState, useEffect } from 'react';
import APIService from "../service/APIService"
import DynamicForm from '../service/DynamicForm';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { ScrollTop } from 'primereact/scrolltop';
import { Bar } from 'react-chartjs-2';
import { Line } from 'react-chartjs-2';
import './Dashboard.css';
import { Pie } from 'react-chartjs-2';
import { Doughnut } from 'react-chartjs-2';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';

function Chart(props) {
    const [chartData, setChartData] = useState(props.chardata);
    const [key, setKey] = useState(props.key);
    const [filterObj, setFilterObj] = useState(props.filter);
    const [filter, setFilter] = useState();
    const [chartTable, setChartTable] = useState();
    const [visibleCT, setVisibleCT] = useState(false);
    const [filterWin, setFilterWin] = useState(false);

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





const getTableData = (filters) => {
    APIService.postRequest("/report/reports-dashboard", filters).then(data => {
        if (data) {
            setChartData(data.data[key]);
        }
    });
}

const getReportData = (e) => {
    let fil = { ...filterObj };
    fil.form = e;
    fil.selected_id = key;
    setFilterObj(fil);
    getTableData(fil);
    setFilterWin(false);

}



const openFilterWin = (chat) => {
    setFilter(chat);
    setFilterWin(true);
}


const chartdata = () => {
    if (chartData.chart_type === 'bar')
        return <Bar data={chartData.chart} options={options} />
    else if (chartData.chart_type === 'line')
        return <Line data={chartData.chart} options={options} />
    else if (chartData.chart_type === 'pie')
        return <Pie data={chartData.chart} options={options} />
    else if (chartData.chart_type === 'doughnut')
        return <Doughnut data={chartData.chart} options={options} />
    else if (chartData.chart_type === 'table')
        return <div style={{ overflow: "auto", height: "500px" }}  >
            <DataTable lazy className="DTtable" value={chartData.chart}>
                {
                    chartData.header.map((data, idx) => (
                        getColumnFile(data)
                    )
                    )
                }
            </DataTable>
            <ScrollTop className="custom-scrolltop" icon="pi pi-arrow-up" />
        </div>

}


const getColumnFile = (key) => {
    if (key.type === 'number') {
        return <Column field={key.key} header={key.key}></Column>
    }
    else {
        return <Column field={key.key} header={key.key}></Column>
    }
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

const dynamicColumns = chartTable && chartTable.header.map((col, i) => {
    return <Column key={col.key} field={col.key} header={col.name} />;
});


return (
    <div className="content">
        <h6>{chartData.name}
            <Button disabled={checkFileter(chartData.form)} icon="pi pi-filter" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openFilterWin(chartData)} className="p-button-rounded p-button-text p-button-plain" />
            <Button hidden={chartData.chart_type === 'table' ? true : false} icon="pi pi-table" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openDataTable(chartData.chart, chartData.chart_type)} className="p-button-rounded p-button-text p-button-plain" /></h6>
        {chartdata()}
        <Dialog style={{ width: "100%", maxWidth: "500px" }} visible={filterWin} onHide={() => setFilterWin(false)}>
            {filter && <DynamicForm type="report" formJson={filter.form} clname="col-sm-12" onSubmit={(e) => getReportData(e)} />}
        </Dialog>
        <Dialog style={{ width: "100%", maxWidth: "600px" }} visible={visibleCT} onHide={() => setVisibleCT(false)}>
            {
                chartTable && <DataTable className="DTtable" value={chartTable.data}>
                    {dynamicColumns}
                </DataTable>
            }
        </Dialog>
    </div>
)

}
export default Chart;