import React, { useState, Component, useEffect, useRef } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import '../App.css';
import ProductService from '../service/ProductService';
import { InputNumber } from 'primereact/inputnumber';
import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { Dialog } from 'primereact/dialog';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"
import { useIndexedDB } from 'react-indexed-db';
import Moment from 'moment';
import DynamicForm from '../service/DynamicForm';
import { OverlayPanel } from 'primereact/overlaypanel';
import BarcodeScanner from '../components/BarcodeScanner'; 
import { Skeleton } from 'primereact/skeleton';

function DyanamicTable(props) {
    const [loading, setLoading] = useState(true);
    const productService = new ProductService();
    const [userData, setUserData] = useState(JSON.parse(APIService.getLocalStorage('myData')));
    const [tableHeader, setTableHeader] = useState();
    const [tableData, setTableData] = useState();
    const [windowWidth, setWindowWidth] = useState(window.innerWidth);
    const [selectedRow, setSelectedRow] = useState();
    const [tableBtn, setTableBtn] = useState(productService.getTableBtnDetais(props.type));
    const [visible, setVisible] = useState(false);
    const [lazy, setLazy] = useState(props.lazy ? props.lazy : false)
    const dataBase = useIndexedDB(props.table);
    const [filter, setFilter] = useState(false);
    const [filterForm, setFilterForm] = useState();
    const [filterObj, setFilterObj] = useState({});
    const [rowCount, setRowCount] = useState(0);
    const [sort, setSort] = useState(false);
    const [scanner, setScanner] = useState(false);
    const tbOptions=productService.getTableDropdownOptions(props.type)
    let editKey = null;
    const [sortJson, setSortJson] = useState(productService.getDynamicFrom(props.type));
    const op = useRef(null);

    useEffect(() => {
        setTableHeader(productService.getDynamicTableHeader(props.type));
        let fil = { userId: userData.userId };
        if (userData.userType === 'Seller')
            fil.seller_id = userData.seller_id;
        if (props.lazy) {
            fil.first = 0;
            fil.size = props.pageSize ? props.pageSize : 250;
        }
        if (props.filter_status)
            fil.status = props.filter_status;
        if(props.type==='DeliveryUsers')
             fil.delivery_user='yes';

        setFilterObj(fil);
        if (props.tabledata){
              setTableData(props.tabledata);
        }
        else
            getTableData(fil);
    }, []);

    const getTableData = (filters) => {
        setLoading(true);
        if (props.offline) {

        }
        else {
            APIService.postRequest(props.url, filters).then(data => {
                if(data){
                setRowCount(data.data.length);
                setTableData(data.data);
                setLoading(false);
                }
            });
        }
    }


    const textElement = (rowData) => {
         return <InputNumber className="tableinputtext"
            value={rowData[editKey]} onChange={(e) => (rowData[editKey] = e.value)} />;
    }

    const setDropDownValue = (rowdata, value) => {
        rowdata.plType = value.key;
        rowdata.pltypesel=value;
        const obj = [...tableData];
        setTableData(obj);
    }
    const dropdownElement = (rowData) => {
        return <Dropdown className="tabledropdown"
            value={rowData.pltypesel} options={tbOptions} optionLabel="name"
            onChange={(e) => setDropDownValue(rowData, e.value)} placeholder="Select" />


    }

    const onAddBtnSubmit = (rowData, e) => {
        setSelectedRow(rowData);
        //  setVisible(true);
        op.current.toggle(e);
    }
    const setAddButton = (rowData) => {
        return <Button type="submit" onClick={(e) => onAddBtnSubmit(rowData, e)} icon="pi pi-ellipsis-v" className="p-button-rounded p-button-secondary  p-button-text" />
    }

    const setAddTotable = (rowData) => {
        return <Button type="submit" onClick={(e) => onAddToTableBtnSubmit(rowData, 'add to table')} icon="pi pi-plus" className="p-button-rounded p-button-secondary  p-button-text" />
    }

    const onRemoveBtnSubmit = (rowData) => {
        const obj = [...tableData];
        var index = obj.findIndex(e => e[props.removekey] = rowData[props.removekey]);
        if (index != -1) {
            obj.splice(index, 1);
           
        } else {
            console.log("Item not found in appGlobal.ObjProduct");
        }
        setTableData(obj);
    }

    const setRemoveButton = (rowData) => {
        return <Button type="submit" onClick={(e) => onRemoveBtnSubmit(rowData)} icon="pi pi-times" className="p-button-rounded p-button-danger p-button-text" />
    }

    const imageBodyTemplate = (rowData) => {
        return <img src={APP_URL + '/user/image?path=' + rowData.image} onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} alt={rowData.image} className="product-image" style={{width:"50px",height:"50px"}} />;
    }

    const download = (rowData) => {
        return rowData.rejectedFilePath && <a href={APP_URL + '/import/file?path=' + rowData.rejectedFilePath} download ><Button icon="pi pi-arrow-down" className="p-button-rounded  p-button-text" /></a>
    }

    const getColumnFile = (key) => {
      
        if ((windowWidth > 1000 || tableHeader[key].mdisplay)) {
            if (tableHeader[key].type === 'number') {
                return <Column field={key} className="numberColWith" header={tableHeader[key].name}></Column>
            } else if (tableHeader[key].type === 'inputnumber') {
                editKey=key;
                return <Column className="numberColWith" header={tableHeader[key].name} body={textElement}></Column>
            }
            else if (tableHeader[key].type === 'image') {
                return <Column header={tableHeader[key].name} body={imageBodyTemplate}></Column>
            } else if (tableHeader[key].type === 'dropdown') {
                return <Column header={tableHeader[key].name} body={dropdownElement}></Column>
            } else if (tableHeader[key].type === 'addbutton') {
                return <Column className="tableBtn" body={setAddButton}></Column>
            } else if (tableHeader[key].type === 'addtotable') {
                return <Column className="tableBtn" body={setAddTotable}></Column>
            }



            else if (tableHeader[key].type === 'removebutton') {
                return <Column className="tableBtn" body={setRemoveButton}></Column>
            }
            else if (tableHeader[key].type === 'download') {
                return <Column className="numberColWith" header={tableHeader[key].name} body={download}></Column>
            }
            else {
                return <Column field={key} header={tableHeader[key].name}></Column>
            }
        }


    }

    const onAddToTableBtnSubmit = (rowData, type) => {
        props.onButtonClik(rowData, type);
    }

    const onFormSubmit = (type1) => {
        let type = type1.toLowerCase();
        if (type === 'add')
            props.onButtonClik(selectedRow, type);
        else if (selectedRow)
            props.onButtonClik(selectedRow, type);
        else if (type === 'filter') {
            let fif = productService.getFilterForm(props.type);
            if (props.filter_status && props.filter_status.length > 0) {
                fif.status.value = { name: props.filter_status, key: props.filter_status };
                fif.status.hidde = true;
            }
            setFilterForm(fif);
            setFilter(true);
        } else if (type === 'sort') {
            setSort(true);
        }else if (type === 'export') {
            props.onButtonClik(filterObj, type);
        }else if (type === 'scanner') {
              setScanner(true);
        }


        onHide();
    }

    const onHide = (e) => {
        setVisible(false);

    }
    const onHidefilter = (e) => {
        setScanner(false);
        setFilter(false);
        setSort(false)
    }
    const applayFilter = (e) => {
        let filter = { ...filterObj };
        filter.first = 0;
        filter.size = props.pageSize ? props.pageSize : 250;
        Object.keys(e).map((key, ind) => {
            if (e[key].value) {
                if (e[key].type === 'dropdown')
                    filter[key] = e[key].value.key;
                else if (e[key].type === 'calender')
                    filter[key] = Moment(e[key].value,'DD-MM-YYYY HH:mm:ss').format('DD-MM-yyyy');
                else
                    filter[key] = e[key].value;
            } else {
                filter[key] = e[key].value;
            }
        });
        let fil = {};
        Object.keys(filter).map((key, ind) => {
            if (filter[key])
                fil[key] = filter[key];
        });
        onHidefilter();
        getTableData(fil);
        setFilterObj(fil)

    }

    const onScanner =(e)=>{
        let filter = { ...filterObj };
        filter.order_no=e;
        onHidefilter();
        getTableData(filter);
        setFilterObj(filter)
      
      
    }

    const setSorting = (e) => {
        let fil = { ...filterObj };
        fil.orderby = e.sortby.value;
        setFilterObj(fil);
        getTableData(fil);
        onHidefilter();
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

    return (
        <div className="datatable-templating-demo">
            <div className="card">
                <div className="p-grid">
                    <div className="p-col-6 table-btns" align="left">
                        <Button type="submit" icon="pi pi-arrow-left" disabled={filterObj.first === 0 ? true : false} hidden={!props.lazy} onClick={(e) => pageDown()} className="p-button-rounded " />
                        <Button type="submit" icon="pi pi-arrow-right" disabled={rowCount < filterObj.size ? true : false} hidden={!props.lazy} onClick={(e) => pageUp()} className="p-button-rounded " />
                    </div>
                    <div className="p-col-6 table-btns" align="right">
                        <Button type="submit" hidden={tableBtn.add.hidden} tooltip={tableBtn.add.name} onClick={(e) => onFormSubmit(tableBtn.add.name)} icon={tableBtn.add.icon} className="p-button-rounded p-button-success" />
                        <Button type="submit" hidden={tableBtn.op2.hidden} tooltip={tableBtn.op2.name} onClick={(e) => onFormSubmit(tableBtn.op2.name)} icon={tableBtn.op2.icon} className="p-button-rounded p-button-help" />
                        <Button type="submit" hidden={tableBtn.op3.hidden} tooltip={tableBtn.op3.name} onClick={(e) => onFormSubmit(tableBtn.op3.name)} icon={tableBtn.op3.icon} className="p-button-rounded p-button-secondary" />
                    </div>
                </div>
            </div>
            <div className="card">


                {tableData &&

                    <DataTable lazy className="DTtable" value={tableData}>
                        {
                            Object.keys(tableHeader).map((key, ind) => (
                              
                                getColumnFile(key)
                            )
                            )
                        }
                    </DataTable>}
            </div>

            <Dialog header="Actions" className="register_dl" visible={visible} onHide={() => onHide()} loading={loading}>
                <div className="rowbuttons">
                    <Button type="submit" hidden={tableBtn.edit.hidden} label={tableBtn.edit.name} onClick={(e) => onFormSubmit(tableBtn.edit.name)} icon={tableBtn.edit.icon} className="p-button-rounded p-button-info" />
                    <Button type="submit" hidden={tableBtn.view.hidden} label={tableBtn.view.name} onClick={(e) => onFormSubmit(tableBtn.view.name)} icon={tableBtn.view.icon} className="p-button-rounded p-button-help" />
                    <Button type="submit" hidden={tableBtn.del.hidden} label={tableBtn.del.name} onClick={(e) => onFormSubmit(tableBtn.del.name)} icon={tableBtn.del.icon} className="p-button-rounded p-button-danger" />
                    <Button type="submit" hidden={tableBtn.op1.hidden} label={tableBtn.op1.name} onClick={(e) => onFormSubmit(tableBtn.op1.name)} icon={tableBtn.op1.icon} className="p-button-rounded p-button-secondary" />

                </div>
            </Dialog>

            <OverlayPanel ref={op} showCloseIcon id="overlay_panel" style={{ width: '180px' }}  >
                <div className="rowbuttons">
                    <Button type="submit" hidden={tableBtn.edit.hidden} label={tableBtn.edit.name} onClick={(e) => onFormSubmit(tableBtn.edit.name)} icon={tableBtn.edit.icon} className=" p-button-info" />
                    <Button type="submit" hidden={tableBtn.view.hidden} label={tableBtn.view.name} onClick={(e) => onFormSubmit(tableBtn.view.name)} icon={tableBtn.view.icon} className=" p-button-help" />
                    <Button type="submit" hidden={tableBtn.del.hidden} label={tableBtn.del.name} onClick={(e) => onFormSubmit(tableBtn.del.name)} icon={tableBtn.del.icon} className=" p-button-danger" />
                    <Button type="submit" hidden={tableBtn.op1.hidden} label={tableBtn.op1.name} onClick={(e) => onFormSubmit(tableBtn.op1.name)} icon={tableBtn.op1.icon} className=" p-button-secondary" />
                </div>
            </OverlayPanel>




            <Dialog header="Filter" style={{ width: "100%" }} className="register_dl" visible={filter} onHide={() => onHidefilter()}>
                <DynamicForm type={props.type} formJson={filterForm} clname="col-sm-12" onSubmit={(e) => applayFilter(e)} />
            </Dialog>

            <Dialog header="Filter" style={{ width: "100%" }} className="register_dl" visible={sort} onHide={() => onHidefilter()}>
                <DynamicForm type="itemSort" formJson={sortJson} clname="col-sm-12" onSubmit={(e) => setSorting(e)} />
            </Dialog>

            <Dialog header="Scanner"   visible={scanner} onHide={() => onHidefilter()}>
                <BarcodeScanner  onScanner={(e) => onScanner(e)} />
            </Dialog>

        </div>
    )

} export default DyanamicTable;