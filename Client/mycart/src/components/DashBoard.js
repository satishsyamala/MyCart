import React from 'react'
import { Bar } from 'react-chartjs-2';
import { Line } from 'react-chartjs-2';
import './Dashboard.css';
import { useRef, useState, useEffect } from 'react';
import APIService from "../service/APIService"
import DyanamicTable from '../service/DyanamicTable';
import APP_URL from "../service/APIConfig"
import { Button } from 'primereact/button';
import { OverlayPanel } from 'primereact/overlaypanel';
import { ListBox } from 'primereact/listbox';
import { useSelector, useDispatch } from 'react-redux';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Dialog } from 'primereact/dialog';
import DynamicForm from '../service/DynamicForm';

function DashBoard(props) {
  const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
  const [dashBoard, setDashBoard] = useState();
  const [tableData, setTableData] = useState();
  const op = useRef(null);
  const [filterWin, setFilterWin] = useState(false);
  const [selectedType, setSelectedType] = useState('month');
  const [chartType, setChartType] = useState();
  const [chart1, setChart1] = useState("Month");
  const [chart2, setChart2] = useState("Month");
  const [chart3, setChart3] = useState("Month");
  const [visibleCT, setVisibleCT] = useState(false);
  const [chartTable, setChartTable] = useState();
  const [filterType, setFilterType] = useState();
  const [filterForm, setFilterForm] = useState();

  const types = [
    { name: 'Day', code: 'Day' },
    { name: 'Month', code: 'Month' },
    { name: 'Year', code: 'Year' }
  ];
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch({ type: 'CHANGE_NAME', payload: 'Dashboard' });
    let fl = { userId: userData.userId, userType: userData.userType, seller_id: userData.seller_id, size: 10 };
    closeCharOption('month');
    fl.size = 15;
    fl.orderby = 'odd';
    APIService.postRequest('/transaction/all-orders', fl).then(data => {
      if (data) {
        setTableData(data.data);
      }
    });


  }, []);

  const chartsForPage = 2;



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

  const openCharOption = (e, type) => {
    setChartType(type)
    op.current.toggle(e);
  }



  const closeCharOption = (type) => {
    let fl = { userId: userData.userId, userType: userData.userType, seller_id: userData.seller_id, size: 10 };
    if (chartType) {
      fl.chartType = chartType;
    }
    fl.chartBy = type;
    let temp = { ...dashBoard };
    APIService.postRequest('/report/get-dashboard', fl).then(data => {
      if (data) {
        if (chartType) {
          if (chartType === 'count') {
            temp.order_count = data.data.order_count;
            setChart1(type);
          }
          else if (chartType === 'amount') {
            temp.order_value = data.data.order_value;
            setChart2(type);
          }
          else {
            temp.order_status = data.data.order_status;
            setChart3(type);
          }
          setDashBoard(temp);
        } else {
          setDashBoard(data.data);
        }
      }

    });
    op.current.hide();
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

  const openFilterWin = (e, type) => {
    setFilterType(type);
    if (type === 'order_value' || type === 'order_count' || type === 'order_status')
      setFilterForm('chartFilter');
    else
      setFilterForm('stockhier');

    setFilterWin(true);
  }

  const getReportData = (e) => {


    let fl = { userId: userData.userId, userType: userData.userType, seller_id: userData.seller_id, size: 15, chartType: filterType };
    Object.keys(e).forEach(function (key) {
      if (e[key].value) {
        if (e[key].type === 'dropdown')
          fl[key] = e[key].value.key;
        else
          fl[key] = e[key].value;
      }
    })
    if (fl.chartBy) {
      if (filterType === 'order_count')
        setChart1(fl.chartBy);
      if (filterType === 'order_value')
        setChart2(fl.chartBy);
      if (filterType === 'order_status')
        setChart3(fl.chartBy);
    }
    if (filterType === 'last_orders') {
      fl.orderby = 'odd';
      APIService.postRequest('/transaction/all-orders', fl).then(data => {
        if (data) {
          setTableData(data.data);
        }
      });
    } else {
      let temp = { ...dashBoard };
      APIService.postRequest('/report/get-dashboard', fl).then(data => {
        if (data) {
          temp[filterType] = data.data[filterType];
          setDashBoard(temp);
        }
      });
    }
    setFilterWin(false);
  }

  return (<div>


    {dashBoard &&

      <div class="container-fluid">
        <div class="row">
          <div class="col-sm card">
            <div style={{ padding: "10px" }}>
              <h6>Last 12 Months Orders</h6>
              <div class="container-fluid">
                <div className="row card-font"  >
                  <div class="col-sm card bg-success text-white overview-box overview-box-1">
                    <div style={{ padding: "10px" }}> <h6>Count <Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'total_orders')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
                      <span className=''>{dashBoard.total_orders}</span>

                    </div>
                    <img src={process.env.PUBLIC_URL + "/graph-blue.svg"} />
                  </div>

                  <div class="col-sm card bg-primary text-white overview-box overview-box-2">
                    <div style={{ padding: "10px" }}><h6>Amount<Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'total_amt')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
                      <span>{dashBoard.total_amt}</span></div>
                    <img src={process.env.PUBLIC_URL + "/graph-green.svg"} />
                  </div>

                  <div class="col-sm card bg-warning text-white overview-box">
                    <div style={{ padding: "10px" }}><h6>Completed<Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'completed_orders')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
                      <span>{dashBoard.completed_orders}</span></div>
                    <img src={process.env.PUBLIC_URL + "/graph-yellow.svg"} />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-sm card">
            <div style={{ padding: "10px" }}>
              <h6>Active Users</h6>
              <div class="container-fluid">
                <div class="row card-font">

                  <div class="col-sm card bg-info  text-white overview-box">
                    <div style={{ padding: "10px" }}><h6>Sellers<Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'count')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
                      <span>300</span></div> <img src={process.env.PUBLIC_URL + "/graph-lb.svg"} />
                  </div>

                  <div class="col-sm  card bg-dark  text-white overview-box">
                    <div style={{ padding: "10px" }}><h6>Consumers<Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'count')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
                      <span>850</span></div> <img src={process.env.PUBLIC_URL + "/graph-bl.svg"} />
                  </div>
                  <div class="col-sm  card bg-danger text-white overview-box">
                    <div style={{ padding: "10px" }}><h6>Delivery<Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'count')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
                      <span>100</span></div> <img src={process.env.PUBLIC_URL + "/graph-red.svg"} />
                  </div>
                </div> </div>
            </div>
          </div>

        </div>
      </div>


    }
    <div class="container-fluid">
      <div className="row">
        <div class="col-sm-4 container-fluid">
          <h6>{chart1} Wise Order Count <Button icon="pi pi-ellipsis-v" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openFilterWin(e, 'order_count')} className="p-button-rounded p-button-text p-button-plain" />
            <Button icon="pi pi-table" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openDataTable(dashBoard.order_count, '')} className="p-button-rounded p-button-text p-button-plain" /></h6>
          {dashBoard && <Bar data={dashBoard.order_count} options={options} />}
        </div>
        <div class="col-sm-4 container-fluid">
          <h6>{chart2} Wise Order Value <Button icon="pi pi-ellipsis-v" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openFilterWin(e, 'order_value')} className="p-button-rounded p-button-text p-button-plain" />
            <Button icon="pi pi-table" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openDataTable(dashBoard.order_value, '')} className="p-button-rounded p-button-text p-button-plain" /></h6>
          {dashBoard && <Line data={dashBoard.order_value} options={options} />}
        </div>

        <div class="col-sm-4 container-fluid">
          <h6>{chart3} Wise Order Status  <Button icon="pi pi-ellipsis-v" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openFilterWin(e, 'order_status')} className="p-button-rounded p-button-text p-button-plain" />
            <Button icon="pi pi-table" style={{ width: "20px", height: "20px", float: "right" }} onClick={(e) => openDataTable(dashBoard.order_status, '')} className="p-button-rounded p-button-text p-button-plain" />
          </h6>
          {dashBoard && <Bar data={dashBoard.order_status} options={options} />}
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm card">
          <div style={{ padding: "10px" }} >
            <h6>Last 15 Orders <Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'last_orders')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
            <table class="table table-bordered">
              <thead class="bg-info text-white">
                <tr>
                  <th>Seller</th>
                  <th>Mobile No.</th>
                  <th>Order Date</th>
                  <th>Price</th>
                </tr>
              </thead>
              <tbody>
                {
                  tableData && tableData.map((data, idx) => (
                    <tr>
                      <td>{data.shop_name}</td>
                      <td>{data.moblie_no}</td>
                      <td>{data.order_date}</td>
                      <td style={{ textAlign: "right" }}>{data.final_price}</td>
                    </tr>
                  ))
                }
              </tbody>
            </table>
          </div>
        </div>
        <div class="col-sm card">
          <div style={{ padding: "10px" }}>
            <h6>Top 10 Items <Button icon="pi pi-ellipsis-v" onClick={(e) => openFilterWin(e, 'top_items')} style={{ width: "20px", height: "20px", float: "right" }} className="p-button-rounded p-button-text p-button-plain " /></h6>
            <table class="table table-bordered">
              <thead class="bg-info text-white">
                <tr>
                  <th>Image</th>
                  <th>Name</th>
                  <th>Qty</th>
                  <th>Amount</th>
                </tr>
              </thead>
              <tbody>
                {
                  dashBoard && dashBoard.top_items.map((data, idx) => (
                    <tr>

                      <td><center>


                        <img src={APP_URL + "/user/image?path=" + data.image} style={{ width: "50px", height: "50px" }}
                          onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={data.name} />
                      </center>
                      </td>
                      <td>{data.name}</td>
                      <td style={{ textAlign: "right" }}>{data.coun}</td>
                      <td style={{ textAlign: "right" }}>{data.amt}</td>
                    </tr>


                  ))

                }

              </tbody>
            </table>

          </div>
        </div>

      </div>
    </div>

    <OverlayPanel ref={op} style={{ width: '200px' }}>
      <ListBox value={selectedType} options={types} onChange={(e) => closeCharOption(e.value.code)} optionLabel="name" />
    </OverlayPanel>

    <Dialog modal={true} header="Filters" style={{ width: "100%", maxWidth: "400px" }} visible={filterWin} onHide={() => setFilterWin(false)}>
      <DynamicForm type={filterForm} clname="col-sm-12" onSubmit={(e) => getReportData(e)} />
    </Dialog>


    <Dialog modal={true} style={{ width: "100%", maxWidth: "600px" }} visible={visibleCT} onHide={() => setVisibleCT(false)}>
      {
        chartTable && <DataTable className="DTtable" rowClassName={rowClass} value={chartTable.data}>
          {dynamicColumns}
        </DataTable>
      }

    </Dialog>

  </div >


  )
} export default DashBoard
