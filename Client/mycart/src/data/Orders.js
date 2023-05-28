import React, { Component } from 'react';


export class Orders extends Component {

    constructor(props) {
        super(props);


        this.ordersTable = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            order_no: {
                name: "Order No",
                type: "text",
                mdisplay: true
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: false
            }, moblie_no: {
                name: "Mobile No.",
                type: "text",
                mdisplay: true
            },
            order_date: {
                name: "Order Date",
                type: "text",
                mdisplay: true
            },
            delivery_date: {
                name: "Delivery Date",
                type: "text",
                mdisplay: true
            }
            ,
            delivery_type: {
                name: "Delivery Type",
                type: "text",
                mdisplay: false
            },
            status: {
                name: "Status",
                type: "text",
                mdisplay: true
            },
            final_price: {
                name: "Price",
                type: "number",
                mdisplay: true
            }
            ,
            status: {
                name: "Status",
                type: "text",
                mdisplay: true
            }
        }

        this.allOrders = {
            add: {
                name: 'Scanner',
                hidden: false,
                icon: 'pi pi-sliders-h'
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
                name: 'Proccess',
                hidden: false,
                icon: 'pi pi-check-circle'
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



    }
}
export default Orders;