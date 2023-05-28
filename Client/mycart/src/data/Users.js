import React, { Component } from 'react';


export class Users extends Component {

    constructor(props) {
        super(props);


        this.adminusers = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            first_name: {
                name: "First Name",
                type: "text",
                mdisplay: true
            },
            last_name: {
                name: "Last Name",
                type: "text",
                mdisplay: false
            }, moblie_no: {
                name: "Mobile No.",
                type: "text",
                mdisplay: true
            },
            email: {
                name: "Email",
                type: "text",
                mdisplay: true
            },user_type: {
                name: "User Type",
                type: "text",
                mdisplay: true
            },
           last_login: {
                name: "Last_login",
                type: "text",
                mdisplay: true
            }
           
        }


        this.sellers = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            shop_name: {
                name: "Shop Name",
                type: "text",
                mdisplay: true
            }, mobile_no: {
                name: "Mobile No.",
                type: "text",
                mdisplay: true
            },
            address: {
                name: "Address",
                type: "text",
                mdisplay: false
            },
            city: {
                name: "City",
                type: "text",
                mdisplay: true
            },state: {
                name: "State",
                type: "text",
                mdisplay: true
            },
           pin_code: {
                name: "Pin Code",
                type: "text",
                mdisplay: true
            }
           
        }

        this.sellerBtn = {
            add: {
                name: 'Add',
                hidden: true,
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
                name: 'View',
                hidden: false,
                icon: 'pi pi-check-circle'
            },
            view: {
                name: 'Users',
                hidden: false,
                icon: 'pi pi-user'
            },
            del: {
                name: 'Stocks',
                hidden: true,
                icon: 'pi pi-clone'
            },
            op1: {
                name: 'Stocks',
                hidden: false,
                icon: 'pi pi-clone'
            },
        };

        this.consumersBtn = {
            add: {
                name: 'Add',
                hidden: true,
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
                name: 'View',
                hidden: false,
                icon: 'pi pi-check-circle'
            },
            view: {
                name: 'Address',
                hidden: false,
                icon: 'pi pi-map-marker'
            },
            del: {
                name: 'Stocks',
                hidden: true,
                icon: 'pi pi-clone'
            },
            op1: {
                name: 'Stocks',
                hidden: true,
                icon: 'pi pi-clone'
            },
        };

        this.adminusersBtn = {
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
                hidden: false,
                icon: 'pi pi-check-circle'
            },
            view: {
                name: 'View',
                hidden: false,
                icon: 'pi pi-check-circle'
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

        this.adminuser = {
            firstName: {
                type: "text",
                label: "First Name",
                required: true,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            , lastName: {
                type: "text",
                label: "Last Name",
                required: true,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            ,
            password: {
                type: "password",
                label: "Password",
                required: true,
                value: null,
                maxlength: 8,
                minlength: 6


            },
            confirmpassword: {
                type: "password",
                label: "Confirm Password",
                required: true,
                value: null,
                maxlength: 8,
                minlength: 6


            }, mobileNo: {
                type: "text",
                label: "Mobile No.",
                required: true,
                value: '',
                maxlength: 10,
                minlength: 6,
                filter: "int"
            },
            email: {
                type: "text",
                label: "Email",
                required: false,
                value: '',
                maxlength: 50,
                minlength: 6

            }, image: {
                type: "fileUpload",
                label: "Profile Pic.",
                required: false,
                value: null,
                path: null
            }
        };

        this.addminfilter = {
            name: {
                type: "text",
                label: "Name",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            , moblie_no: {
                type: "text",
                label: "Mobile No.",
                required: false,
                value: '',
                maxlength: 10,
                minlength: 6,
                filter: "int"
            },
            email: {
                type: "text",
                label: "Email",
                required: false,
                value: '',
                maxlength: 50,
                minlength: 6

            },
            user_type: {
                type: "dropdown",
                label: "User Type",
                required: false,
                value: null,
               
                options: [{ name: 'Admin', key: 'Admin' },
                { name: 'Delivery', key: 'Delivery' }
                ]
            }
        };

        this.consumerfilter = {
            name: {
                type: "text",
                label: "Name",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            , moblie_no: {
                type: "text",
                label: "Mobile No.",
                required: false,
                value: '',
                maxlength: 10,
                minlength: 6,
                filter: "int"
            },
            email: {
                type: "text",
                label: "Email",
                required: false,
                value: '',
                maxlength: 50,
                minlength: 6

            },
            user_type: {
                type: "dropdown",
                label: "User Type",
                required: false,
                value: null,
               
                options: [{ name: 'Personal', key: 'Personal' },
                { name: 'Retailer', key: 'Retailer' }
                ]
            }
        };


        this.sellerfilter = {
            name: {
                type: "text",
                label: "Name",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            , moblie_no: {
                type: "text",
                label: "Mobile No.",
                required: false,
                value: '',
                maxlength: 10,
                minlength: 6,
                filter: "int"
            }
        };




        this.selleruser = {
            firstName: {
                type: "text",
                label: "First Name",
                required: true,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            , lastName: {
                type: "text",
                label: "Last Name",
                required: true,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            ,
            password: {
                type: "password",
                label: "Password",
                required: true,
                value: null,
                maxlength: 8,
                minlength: 6


            },
            confirmpassword: {
                type: "password",
                label: "Confirm Password",
                required: true,
                value: null,
                maxlength: 8,
                minlength: 6


            }, mobileNo: {
                type: "text",
                label: "Mobile No.",
                required: true,
                value: '',
                maxlength: 10,
                minlength: 6,
                filter: "int"
            },
            email: {
                type: "text",
                label: "Email",
                required: false,
                value: '',
                maxlength: 50,
                minlength: 6

            }, image: {
                type: "fileUpload",
                label: "Profile Pic.",
                required: false,
                value: null,
                path: null
            }
        };




        this.selleruserfilter = {
            name: {
                type: "text",
                label: "Name",
                required: false,
                value: null,
                maxlength: 20,
                minlength: 5
            }
            , moblie_no: {
                type: "text",
                label: "Mobile No.",
                required: false,
                value: '',
                maxlength: 10,
                minlength: 6,
                filter: "int"
            },
            email: {
                type: "text",
                label: "Email",
                required: false,
                value: '',
                maxlength: 50,
                minlength: 6

            },
            user_type: {
                type: "dropdown",
                label: "User Type",
                required: false,
                value: null,
               
                options: [{ name: 'Personal', key: 'Personal' },
                { name: 'Retailer', key: 'Retailer' }
                ]
            }
        };



    }
}
export default Users;