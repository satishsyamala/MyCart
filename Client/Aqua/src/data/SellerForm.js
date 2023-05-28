import React, { Component } from 'react';


export class SellerForm extends Component {

    constructor(props) {
        super(props);

        this.sellerForm = {
            shopName: {
                type: "text",
                label: "Shop Name",
                required: true,
                value: '',
                maxlength: 20,
                minlength: 0
            },
            mobileNo: {
                type: "text",
                label: "Mobile No.",
                required: true,
                value: '',
                maxlength: 20,
                minlength: 0,
                filter: "int"
            },licenceNo: {
                type: "text",
                label: "Licence No",
                required: false,
                value: '',
                maxlength: 20,
                minlength: 0
            },
            city: {
                type: "text",
                label: "City",
                required: true,
                value: '',
                maxlength: 20,
                minlength: 0
            }, state: {
                type: "text",
                label: "State",
                required: true,
                value: '',
                maxlength: 20,
                minlength: 0
            }, pinCode: {
                type: "text",
                label: "Pin Code",
                required: true,
                value: '',
                maxlength: 20,
                minlength: 0
            },
            latitude: {
                type: "text",
                label: "Latitude",
                required: false,
                value: null,
                hidde: true
            },
            longitude: {
                type: "text",
                label: "Longitude",
                required: false,
                value: null,
                hidde: true
            },
            address: {
                type: "textarea",
                label: "Address",
                required: true,
                value: '',
                maxlength: 200,
                minlength: 0
            }, image: {
                type: "fileUpload",
                label: "Shop Image",
                required: true,
                value: null,
                path: null


            }, map: {
                type: "map",
                label: "Location",
                required: false,
                value: null
               
            }
        };

        this.stockMap = {
            addBtn: {
                name: "Update",
                type: "addbutton",
                mdisplay: true
            },image: {
                name: "image",
                type: "image",
                mdisplay: true
            },
            code: {
                name: "Code",
                type: "text",
                mdisplay: true
            },
            name: {
                name: "Stock Name",
                type: "text",
                mdisplay: true
            },
            cat_name: {
                name: "Category",
                type: "text",
                mdisplay: false
            },
            sub_cat_name: {
                name: "Sub Category",
                type: "text",
                mdisplay: false
            },
            bar_name: {
                name: "Brand",
                type: "text",
                mdisplay: false
            }
           
            
        };

        this.stockmapform = {

            categoryId: {
                type: "dropdown",
                label: "Category",
                required: true,
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
                dependele: ['subCategoryId'],
                updateEle: 'name'
            }, name: {
                type: "multiselection",
                label: "Stock Name",
                required: false,
                value: null,
                bustype: 'stock',
                dependele: ['categoryId','subCategoryId','brandId'],
                options: [],
                maxlength: 20,
                minlength: 0
            }
        };



        this.stockmapedit={
             name: {
                type: "text",
                label: "Stock Name",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 0,
                readonly:true,
            },
            pack_size: {
                type: "number",
                label: "Pack Size",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 0,
                readonly:true,
            },
            uom: {
                type: "text",
                label: "UOM",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 0,
                readonly:true,
            },
           
            price: {
                type: "number",
                label: "Price",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 0
            }
        };


       
    }
}
export default SellerForm;