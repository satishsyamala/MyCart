import React, { Component } from 'react';


export class Configuration extends Component {

    constructor(props) {
        super(props);

        this.importHeader = {
            importId: {
                name: "Id",
                type: "number",
                mdisplay: false
            },
            module: {
                name: "Module",
                type: "text",
                mdisplay: true
            },
            importedOn: {
                name: "Imported On",
                type: "text",
                mdisplay: false
            },
            importedBy: {
                name: "Imported By",
                type: "text",
                mdisplay: true
            }
            ,
            status: {
                name: "Status",
                type: "text",
                mdisplay: false
            },
            completedOn: {
                name: "Completed On",
                type: "text",
                mdisplay: true
            },
            totalRecords: {
                name: "Total",
                type: "number",
                mdisplay: true
            }
            ,
            rejectedRecords: {
                name: "Failed",
                type: "number",
                mdisplay: true
            }
            ,
            download: {
                name: "File",
                type: "download",
                mdisplay: true
            }
        }

        this.importTbBtn = {
            add: {
                name: 'Add',
                hidden: false,
                icon: 'pi pi-plus'
            },
            op2: {
                name: 'Filter',
                hidden: false,
                icon: 'pi pi-filter'
            },
            op3: {
                name: 'Sort',
                hidden: true,
                icon: 'pi pi-sort-alt'
            },
            edit: {
                name: 'Edit',
                hidden: true
            },
            view: {
                name: 'Orders',
                hidden: true
            },
            del: {
                name: 'Delete',
                hidden: true
            },
            op1: {
                name: 'Option 1',
                hidden: true
            },
        };

        this.stockFilter = {
            categoryId: {
                type: "dropdown",
                label: "Category",
                required: false,
                value: null,
                options: [],
                bustype: 'category',
                autoload: true,
                updateEle: 'subCategoryId'


            },
            subCategoryId: {
                type: "dropdown",
                label: "Sub Category",
                required: false,
                value: null,
                options: [],
                bustype: 'subcategory',
                autoload: true,
                dependele: ['categoryId'],
                updateEle: 'brandId'
            }, brandId: {
                type: "dropdown",
                label: "Brand",
                required: false,
                value: null,
                options: [],
                bustype: 'brand',
                autoload: true,
                dependele: ['subCategoryId']
            }, name: {
                type: "text",
                label: "Code or Name",
                required: false,
                value: null,
                maxlength: 100,
                minlength: 0
            }
        }

        this.brandFilter = {
            categoryId: {
                type: "dropdown",
                label: "Category",
                required: false,
                value: null,
                options: [],
                bustype: 'category',
                autoload: true,
                updateEle: 'subCategoryId'


            },
            subCategoryId: {
                type: "dropdown",
                label: "Sub Category",
                required: false,
                value: null,
                options: [],
                bustype: 'subcategory',
                autoload: true,
                dependele: ['categoryId']
               
            },  name: {
                type: "text",
                label: "Name",
                required: false,
                value: null,
                maxlength: 100,
                minlength: 0
            }
        }

    }






}
export default Configuration;