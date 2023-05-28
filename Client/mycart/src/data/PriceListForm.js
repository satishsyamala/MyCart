import React, { Component } from 'react';


export class PriceListForm extends Component {

    constructor(props) {
        super(props);


        this.priceListTable = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            start_date: {
                name: "Start Date",
                type: "text",
                mdisplay: true
            }, end_date: {
                name: "End Date",
                type: "text",
                mdisplay: true
            },
            created_on: {
                name: "Created On",
                type: "text",
                mdisplay: false
            },
            created_by: {
                name: "Created By",
                type: "text",
                mdisplay: false
            }
            ,
            status: {
                name: "Delivery Type",
                type: "text",
                mdisplay: false
            }
        }

        this.pricelistbtn = {
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
                hidden: false,
                icon: 'pi pi-sort-alt'
            },
            edit: {
                name: 'View',
                hidden: false,
               
            },
            view: {
                name: 'Items',
                hidden: false,
                
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

        this.subscriptionbtn = {
            add: {
                name: 'Add',
                hidden: false,
                icon: 'pi pi-plus'
            },
            op2: {
                name: 'Filter',
                hidden: true,
                icon: 'pi pi-filter'
            },
            op3: {
                name: 'Sort',
                hidden: true,
                icon: 'pi pi-sort-alt'
            },
            edit: {
                name: 'Edit',
                hidden: false,
               
            },
            view: {
                name: 'View',
                hidden: true,
                
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
        this.pricelistform = {
            name: {
                type: "text",
                label: "Name",
                required: true,
                value: null,
                maxlength: 50,
                minlength: 0

            },
            startDate: {
                type: "calender",
                label: "Start Date",
                required: true,
                value: new Date(),


            }, endDate: {
                type: "calender",
                label: "End Date",
                required: true,
                value: new Date(),


            },offerType: {
                type: "dropdown",
                label: "Offer Type",
                required: true,
                value: null,
                defoptions: [],
                options: [{ name: 'Flat', key: 'Flat' },
                { name: 'Percentage', key: 'Percentage' }
              ],
              
            },amtOrPer: {
                type: "number",
                label: "Amount/Percentage",
                required: true,
                value: 0,
                maxValue: 10,
                minMinvalu: 0
            }, image: {
                type: "fileUpload",
                label: "Offer Pic",
                required: true,
                value: null,
                path: null


            }


        }

        this.priceformbtn = {
            addbtn: {
                "name": "Submit",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": true
            }
        }

        this.addedMap = {
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            catName: {
                name: "Category",
                type: "text",
                mdisplay: false
            },
            subCatName: {
                name: "Sub Category",
                type: "text",
                mdisplay: true
            }
            ,
            brandName: {
                name: "Brand",
                type: "text",
                mdisplay: false
            },
            uom: {
                name: "UOM",
                type: "text",
                mdisplay: true
            } ,
            price: {
                name: "Price",
                type: "number",
                mdisplay: true
            },
            addBtn: {
                name: "Delete",
                type: "removebutton",
                mdisplay: true
            },
        };

        this.subscriptionheaders = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            duration_days: {
                name: "Duration Days",
                type: "number",
                mdisplay: true
            },
            amount: {
                name: "Amount",
                type: "number",
                mdisplay: true
            }
           
        };

        

        this.stockMap = {
           
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            catName: {
                name: "Category",
                type: "text",
                mdisplay: false
            },
            subCatName: {
                name: "Sub Category",
                type: "text",
                mdisplay: true
            }
            ,
            brandName: {
                name: "Brand",
                type: "text",
                mdisplay: false
            },
            uom: {
                name: "UOM",
                type: "text",
                mdisplay: true
            } ,
            price: {
                name: "Price",
                type: "number",
                mdisplay: true
            },
            addBtn: {
                name: "Add",
                type: "addtotable",
                mdisplay: true
            },
           
        
        };



        this.ordersFilter = {
            mobile_no: {
                type: "text",
                label: "Mobile No.",
                required: false,
                value: null,
                maxlength: 50,
                minlength: 0

            }, order_no: {
                type: "text",
                label: "Order No.",
                required: false,
                value: null,
                maxlength: 50,
                minlength: 0

            },
            from_date: {
                type: "calender",
                label: "From Date",
                required: false,
                value: new Date(),


            }, to_date: {
                type: "calender",
                label: "To Date",
                required: false,
                value: new Date(),


            },

            status: {
                type: "dropdown",
                label: "Status",
                required: false,
                value: null,
                defoptions: [],
                options: [{ name: 'Order Placed', key: 'Order Placed' },
                { name: 'Accepted', key: 'Accepted' },
                { name: 'Packed', key: 'Packed' },
                { name: 'Ready For Pickup', key: 'Ready For Pickup' },
                { name: 'Out For Delivery', key: 'Out For Delivery' },
                { name: 'Delivered', key: 'Delivered' },
                ],
            }
        }

        this.genSettings={
            distanceKm: {
                label: "Orders Km Range",
                type: "number",
                required: true
            },minOrderValue: {
                label: "Min. order Vaule",
                type: "number",
                required: false
            },minDeliveryDays : {
                label: "Min Delivery Days",
                type: "number",
                required: false
            },paymentModes : {
                label: "Payment Modes",
                type: "multiselection",
                required: true,
                value: null,
                defoptions: [],
                options: [{ name: 'Cash On Delivery', key: 'Cash On Delivery' },
                { name: 'Card On Delivery', key: 'Card On Delivery' },
                { name: 'Wallet', key: 'Wallet' }
                ]
            },
        }

        this.subscription={
            name: {
                type: "text",
                label: "Name",
                required: true,
                value: null,
                maxlength: 50,
                minlength: 0
            },
            durationDays: {
                label: "Duration In Days",
                type: "number",
                required: false
            },amount : {
                label: "Amount",
                type: "number",
                required: false
            }, image: {
                type: "fileUpload",
                label: "Plan Image",
                required: false,
                value: null,
                path: null


            }
        }

        this.deliveryCharge={
            minDeliveryCharge: {
                label: "Min. Delivery Charge",
                type: "number",
                required: true
            },maxDeliveryCharge: {
                label: "Max. Delivery Charge",
                type: "number",
                required: true
            },maxValueForFreeDelivery: {
                label: "Max. Value for Free Delivery",
                type: "number",
                required: true
            },minDistForMinCharge: {
                label: "Min. Distance For Min charge",
                type: "number",
                required: true
            },deliveryChargeForKm: {
                label: "Delivery Charge For Km",
                type: "number",
                required: true
            }

            
        }



    }
}
export default PriceListForm;