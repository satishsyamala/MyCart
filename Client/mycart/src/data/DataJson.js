import React, { Component } from 'react';


export class DataJson extends Component {

    constructor(props) {
        super(props);

        this.adminmenus = [
            { label: 'Dash Board', icon: 'chart-icon', url: 'dashboard' },
            { label: 'Orders', icon: 'order-icon', url: 'allorders' },
            {
                label: 'Users', icon: 'multi-users-icon', items: [
                    { label: 'Admin', icon: 'admin-user-icon', url: 'adminusers' },
                    { label: 'Sellers', icon: 'seller-user-icon', url: 'sellers' },
                    { label: 'Consumers', icon: 'cunsumer-icon', url: 'consumers' },
                    { label: 'Delivery Users', icon: 'cunsumer-icon', url: 'admindelivery' },
                    { label: 'Scanner', icon: 'scanner', url: 'scanner' }
                ]
            },
            {
                label: 'Products', icon: 'stock-icon', items: [
                    { label: 'Category', icon: 'category-icon', url: 'category' },
                    { label: 'Sub Category', icon: 'cate-icon', url: 'subcategory' },
                    { label: 'Brand', icon: 'cate-icon', url: 'brand' },
                    { label: 'Stock Itmes', icon: 'stock-icon', url: 'stocks' }
                ]
            },
            { label: 'Offers', icon: 'offer-icon', url: 'offers' },
            { label: 'Reports', icon: 'reports-icon', url: 'reports' },
            {
                label: 'Configuartion', icon: 'settings-icon', items: [
                    { label: 'Settings', icon: 'settings-icon', url: 'settings' },
                    { label: 'Subscription', icon: 'pi pi-play', url: 'subscription' },
                    { label: 'Import Data', icon: 'import-icon', url: 'imports' },

                ]
            }
        ];

        this.adminmenus1 = [
            { label: 'Dash Board', icon: 'chart-icon', url: 'dashboard' },
            { label: 'Reports', icon: 'reports-icon', url: 'reports' },
            {
                label: 'Configuartion', icon: 'settings-icon', items: [
                    { label: 'Settings', icon: 'settings-icon', url: 'settings' },
                    { label: 'Subscription', icon: 'pi pi-play', url: 'subscription' },
                    { label: 'Import Data', icon: 'import-icon', url: 'imports' },

                ]
            }
        ];

        this.sellermenus = [
            { label: 'Dash Board', icon: 'chart-icon', url: 'dashboard?id=hi' },
            {
                label: 'Orders', icon: 'order-icon', items: [
                    { label: 'Pending', icon: 'order-placed', url: 'pendingord' },
                    { label: 'Accepted', icon: 'accepted', url: 'acceptedord' },
                    { label: 'Ready For Pickup', icon: 'ready-for-pick', url: 'readyforpickup' },
                    { label: 'Packed', icon: 'packed', url: 'packedord' },
                    { label: 'Out For Delivery', icon: 'outfordelivery', url: 'outfordelivery' },
                    { label: 'Deliverd', icon: 'delivered', url: 'deliveredord' },
                    { label: 'All', icon: 'cart-b-icon', url: 'allorders' },
                ]
            },
            {
                label: 'Products', icon: 'stock-icon', items: [
                    { label: 'Stock Itmes', icon: 'stock-icon', url: 'stocksseller' },
                    { label: 'Stock Map', icon: 'stock-map', url: 'mapstock' },
                ]
            },
            { label: 'Offers', icon: 'offer-icon', url: 'offers' },
            {
                label: 'My Account', icon: 'user-icon', items: [ 
                    { label: 'Users', icon: 'user-icon', url: 'sellerusers' },
                    { label: 'Delivery Users', icon: 'cunsumer-icon', url: 'sellerdelivery' },
                    { label: 'My Profile', icon: 'profile-edit', url: 'profile' },
                    { label: 'Shop', icon: 'seller-user-icon', url: 'sellerdetails' },
                    { label: 'Change Password', icon: 'password-change', url: 'changepassword' },
                    { label: 'Subscription', icon: 'pi pi-play', url: 'subscribeplan' },
                ]
            },
          
            { label: 'Reports', icon: 'reports-icon', url: 'reports' },
            {
                label: 'Configuartion', icon: 'settings-icon', items: [
                    { label: 'Settings', icon: 'settings-icon', url: 'sellersettings' },
                    { label: 'Import Data', icon: 'import-icon', url: 'sellerimports' },

                ]
            }
        ];

        this.consumermenu = [
            { label: 'Home', icon: 'home-icon', url: 'home' },
            { label: 'Categories', icon: 'category-icon', url: 'categories' },
            { label: 'Sellers', icon: 'seller-user-icon', url: 'consellers' },

            
            { label: 'Offers', icon: 'offer-icon', url: 'offerscard' },
            { label: 'My Orders', icon: 'order-icon', url: 'myorders' },
            { label: 'My Cart', icon: 'cart-b-icon', url: 'mycart' },
            {
                label: 'My Account', icon: 'user-icon', items: [
                    { label: 'My Profile', icon: 'profile-edit', url: 'profile' },
                    { label: 'My Address', icon: 'address', url: 'myaddress' },
                    { label: 'Change Password', icon: 'password-change', url: 'changepassword' }
                ]
            }
        ];

        this.delivery = [
            { label: 'Deliveries', icon: 'outfordelivery', url: 'deliveryorders' },
            { label: 'Pickups', icon: 'ready-for-pick', url: 'pickuporders' },
            { label: 'Accepts', icon: 'accepted', url: 'acceptedorders' },
            { label: 'All', icon: 'cart-b-icon', url: 'deliveryall' },
            { label: 'Profile', icon: 'profile-edit', url: 'profile' }
        ];

        this.loginform = {
            name: {
                type: "text",
                label: "Mobile No",
                required: true,
                value: ''

            }, password: {
                type: "password",
                label: "Password",
                required: true,
                value: ''
            }, savepass: {
                type: "onecheckbox",
                label: "Save Password",
                required: false,
                value: false
            },
        }

        this.passwordchangeform = {
            oldpassword: {
                type: "password",
                label: "Old Password",
                required: true,
                value: ''
            }, password: {
                type: "password",
                label: "New Password",
                required: true,
                value: ''
            }
            , confpassword: {
                type: "password",
                label: "Confirm New Password",
                required: true,
                value: ''
            }
        }

        this.forgotpassword = {
            emailormobile: {
                type: "text",
                label: "Email/Mobile  No.",
                required: true,
                value: ''
            }
        }

        this.loginbtn = {
            addbtn: {
                "name": "Login",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": true
            }
        }

        this.stockHier = {
            addbtn: {
                "name": "Submit",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": true
            }
        }

        this.checkout = {
            addbtn: {
                "name": "Confirm",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": false
            }
        }

        this.forgotpass = {
            addbtn: {
                "name": "Confirm",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": true
            }
        }


        this.registerForm = {
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
            , mobileNo: {
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
                required: true,
                value: '',
                maxlength: 50,
                minlength: 6

            }
        };


        this.updateprofileBtn = {
            addbtn: {
                "name": "Confirm",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": false
            }
        }


        this.updateprofile = {
            mobileNo: {
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
                label: "Profile Pic",
                required: false,
                value: null,
                path: null


            }
        };



        this.regbtn = {
            addbtn: {
                "name": "Submit",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": false
            }
        }

        this.delivery_address = {

            address: {
                type: "textarea",
                label: "Address",
                required: true,
                value: '',
                maxlength: 200,
                minlength: 0
            }, map: {
                type: "map",
                label: "Location",
                required: false,
                value: null

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
                required: true,
                value: null,
                maxlength: 20,
                minlength: 0,
                hidde: true
            },
            longitude: {
                type: "text",
                label: "Longitude",
                required: true,
                value: null,
                maxlength: 20,
                minlength: 0,
                hidde: true
            }, defaultDel: {
                type: "onecheckbox",
                label: "Set Default",
                required: false,
                value: false
            }
        };





        this.userHeader = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            id: {
                name: "Id",
                type: "text",
                mdisplay: true
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            password: {
                name: "Password",
                type: "text",
                mdisplay: false
            },
            email: {
                name: "Email",
                type: "text",
                mdisplay: false
            },
            mobile_no: {
                name: "Mobile No.",
                type: "text",
                mdisplay: true
            },

            user_type: {
                name: "User Type",
                type: "text",
                mdisplay: true
            },


        };

        this.deliveryHeader = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            address: {
                name: "Address",
                type: "text",
                mdisplay: true
            }, city: {
                name: "City",
                type: "text",
                mdisplay: true
            }, state: {
                name: "State",
                type: "text",
                mdisplay: false
            }, pinCode: {
                name: "Pin Code",
                type: "text",
                mdisplay: false
            },
            latitude: {
                name: "Latitude",
                type: "text",
                mdisplay: false
            },
            longitude: {
                name: "Longitude",
                type: "text",
                mdisplay: false
            },
            default:{
                name: "Default",
                type: "text",
                mdisplay: false
            }
        }


        this.categoryHeader = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            categoryId: {
                name: "ID",
                type: "number",
                mdisplay: false
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            orderBy: {
                name: "Order By",
                type: "text",
                mdisplay: true
            }
            ,
            status: {
                name: "Status",
                type: "text",
                mdisplay: true
            }
        }
        this.subCategoryHeader = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            subCategoryId: {
                name: "ID",
                type: "number",
                mdisplay: false
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            catName: {
                name: "Category",
                type: "text",
                mdisplay: true
            }
            ,
            status: {
                name: "Status",
                type: "text",
                mdisplay: true
            }
        }

        this.brandsHeader = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            brandId: {
                name: "ID",
                type: "number",
                mdisplay: false
            },
            name: {
                name: "Name",
                type: "text",
                mdisplay: true
            },
            catName: {
                name: "Category",
                type: "text",
                mdisplay: true
            },
            subCatName: {
                name: "Sub Category",
                type: "text",
                mdisplay: true
            }
            ,
            status: {
                name: "Status",
                type: "text",
                mdisplay: false
            }
        }


        this.stockHeader = {
            addBtn: {
                name: "Add",
                type: "addbutton",
                mdisplay: true
            },
            image: {
                name: "Image",
                type: "image",
                mdisplay: true
            },
            code: {
                name: "Code",
                type: "text",
                mdisplay: false
            },
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
            }
            ,
            status: {
                name: "Status",
                type: "text",
                mdisplay: true
            }
        }






        this.dataTable = {
            "data": [
                {
                    "id": "1000", "code": "f230fh0g3", "name": "Bamboo Watch", "description": "Product Description", "image": "bamboo-watch.jpg", "price": 65, "category": "Accessories", "quantity": 24, "inventoryStatus": "INSTOCK", "rating": 5, 'selected': { name: 'Marketing', key: 'M' }, 'options': [{ name: 'Accounting', key: 'A' },
                    { name: 'Marketing', key: 'M' },
                    { name: 'Production', key: 'P' },
                    { name: 'Research', key: 'R' }]
                },
                {
                    "id": "1001", "code": "nvklal433", "name": "Black Watch", "description": "Product Description", "image": "black-watch.jpg", "price": 72, "category": "Accessories", "quantity": 61, "inventoryStatus": "INSTOCK", "rating": 4, 'selected': null, 'options': [{ name: 'Accounting', key: 'A' },
                    { name: 'Marketing', key: 'M' },
                    { name: 'Production', key: 'P' },
                    { name: 'Research', key: 'R' }]
                },
                { "id": "1002", "code": "zz21cz3c1", "name": "Blue Band", "description": "Some text about the jeans. Super slim and comfy lorem ipsum lorem jeansum. Lorem jeamsun denim lorem jeansum.", "image": "blue-band.jpg", "price": 79, "category": "Fitness", "quantity": 2, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1003", "code": "244wgerg2", "name": "Blue T-Shirt", "description": "Product Description", "image": "blue-t-shirt.jpg", "price": 29, "category": "Clothing", "quantity": 25, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1004", "code": "h456wer53", "name": "Bracelet", "description": "Product Description", "image": "bracelet.jpg", "price": 15, "category": "Accessories", "quantity": 73, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1005", "code": "av2231fwg", "name": "Brown Purse", "description": "Product Description", "image": "brown-purse.jpg", "price": 120, "category": "Accessories", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 4 },
                { "id": "1006", "code": "bib36pfvm", "name": "Chakra Bracelet", "description": "Product Description", "image": "chakra-bracelet.jpg", "price": 32, "category": "Accessories", "quantity": 5, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1007", "code": "mbvjkgip5", "name": "Galaxy Earrings", "description": "Product Description", "image": "galaxy-earrings.jpg", "price": 34, "category": "Accessories", "quantity": 23, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1008", "code": "vbb124btr", "name": "Game Controller", "description": "Product Description", "image": "game-controller.jpg", "price": 99, "category": "Electronics", "quantity": 2, "inventoryStatus": "LOWSTOCK", "rating": 4 },
                { "id": "1009", "code": "cm230f032", "name": "Gaming Set", "description": "Product Description", "image": "gaming-set.jpg", "price": 299, "category": "Electronics", "quantity": 63, "inventoryStatus": "INSTOCK", "rating": 3 },
                { "id": "1010", "code": "plb34234v", "name": "Gold Phone Case", "description": "Product Description", "image": "gold-phone-case.jpg", "price": 24, "category": "Accessories", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 4 },
                { "id": "1011", "code": "4920nnc2d", "name": "Green Earbuds", "description": "Product Description", "image": "green-earbuds.jpg", "price": 89, "category": "Electronics", "quantity": 23, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1012", "code": "250vm23cc", "name": "Green T-Shirt", "description": "Product Description", "image": "green-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 74, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1013", "code": "fldsmn31b", "name": "Grey T-Shirt", "description": "Product Description", "image": "grey-t-shirt.jpg", "price": 48, "category": "Clothing", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 3 },
                { "id": "1014", "code": "waas1x2as", "name": "Headphones", "description": "Product Description", "image": "headphones.jpg", "price": 175, "category": "Electronics", "quantity": 8, "inventoryStatus": "LOWSTOCK", "rating": 5 },
                { "id": "1015", "code": "vb34btbg5", "name": "Light Green T-Shirt", "description": "Product Description", "image": "light-green-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 34, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1016", "code": "k8l6j58jl", "name": "Lime Band", "description": "Product Description", "image": "lime-band.jpg", "price": 79, "category": "Fitness", "quantity": 12, "inventoryStatus": "INSTOCK", "rating": 3 },
                { "id": "1017", "code": "v435nn85n", "name": "Mini Speakers", "description": "Product Description", "image": "mini-speakers.jpg", "price": 85, "category": "Clothing", "quantity": 42, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1018", "code": "09zx9c0zc", "name": "Painted Phone Case", "description": "Product Description", "image": "painted-phone-case.jpg", "price": 56, "category": "Accessories", "quantity": 41, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1019", "code": "mnb5mb2m5", "name": "Pink Band", "description": "Product Description", "image": "pink-band.jpg", "price": 79, "category": "Fitness", "quantity": 63, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1020", "code": "r23fwf2w3", "name": "Pink Purse", "description": "Product Description", "image": "pink-purse.jpg", "price": 110, "category": "Accessories", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 4 },
                { "id": "1021", "code": "pxpzczo23", "name": "Purple Band", "description": "Product Description", "image": "purple-band.jpg", "price": 79, "category": "Fitness", "quantity": 6, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1022", "code": "2c42cb5cb", "name": "Purple Gemstone Necklace", "description": "Product Description", "image": "purple-gemstone-necklace.jpg", "price": 45, "category": "Accessories", "quantity": 62, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1023", "code": "5k43kkk23", "name": "Purple T-Shirt", "description": "Product Description", "image": "purple-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 2, "inventoryStatus": "LOWSTOCK", "rating": 5 },
                { "id": "1024", "code": "lm2tny2k4", "name": "Shoes", "description": "Product Description", "image": "shoes.jpg", "price": 64, "category": "Clothing", "quantity": 0, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1025", "code": "nbm5mv45n", "name": "Sneakers", "description": "Product Description", "image": "sneakers.jpg", "price": 78, "category": "Clothing", "quantity": 52, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1026", "code": "zx23zc42c", "name": "Teal T-Shirt", "description": "Product Description", "image": "teal-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 3, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1027", "code": "acvx872gc", "name": "Yellow Earbuds", "description": "Product Description", "image": "yellow-earbuds.jpg", "price": 89, "category": "Electronics", "quantity": 35, "inventoryStatus": "INSTOCK", "rating": 3 },
                {
                    "id": "1028", "code": "tx125ck42", "name": "Yoga Mat", "description": "Product Description", "image": "yoga-mat.jpg", "price": 20, "category": "Fitness", "quantity": 15, "inventoryStatus": "INSTOCK", "rating": 5, 'selected': null, 'options': [{ name: 'Accounting', key: 'A' },
                    { name: 'Marketing', key: 'M' },
                    { name: 'Production', key: 'P' },
                    { name: 'Research', key: 'R' }]
                },
                {
                    "id": "1029", "code": "gwuby345v", "name": "Yoga Set", "description": "Product Description", "image": "yoga-set.jpg", "price": 20, "category": "Fitness", "quantity": 25, "inventoryStatus": "INSTOCK", "rating": 8, 'selected': null, 'options': [{ name: 'Accounting', key: 'A' },
                    { name: 'Marketing', key: 'M' },
                    { name: 'Production', key: 'P' },
                    { name: 'Research', key: 'R' }]
                }
            ],
            "header": {
                id: {
                    name: "ID",
                    type: "text",
                    mdisplay: false
                },
                code: {
                    name: "Code",
                    type: "text",
                    mdisplay: false
                },
                name: {
                    name: "Name",
                    type: "text",
                    mdisplay: false
                },
                description: {
                    name: "Description",
                    type: "text",
                    mdisplay: true
                },
                /**  image:{
                     name:"Image",
                     type:"image",
                     mdisplay:false
                 },*/
                category: {
                    name: "Category",
                    type: "dropdown",
                    mdisplay: true
                },
                quantity: {
                    name: "Qty.",
                    type: "inputnumber",
                    mdisplay: true
                },

                price: {
                    name: "Price",
                    type: "number",
                    mdisplay: true
                },

                addBtn: {
                    name: "Add",
                    type: "addbutton",
                    mdisplay: true
                },

                removeBtn: {
                    name: "Remove",
                    type: "removebutton",
                    mdisplay: true
                }
            }

        };



        this.products = {
            "data": [
                { "id": "1000", "code": "f230fh0g3", "name": "Bamboo Watch", "description": "Some text about the jeans. Super slim and comfy lorem ipsum lorem jeansum. Lorem jeamsun denim lorem jeansum.", "image": "bamboo-watch.jpg", "price": 65, "category": "Accessories", "quantity": 24, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1001", "code": "nvklal433", "name": "Black Watch", "description": "Product Description", "image": "black-watch.jpg", "price": 72, "category": "Accessories", "quantity": 61, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1002", "code": "zz21cz3c1", "name": "Blue Band", "description": "Product Description", "image": "blue-band.jpg", "price": 79, "category": "Fitness", "quantity": 2, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1003", "code": "244wgerg2", "name": "Blue T-Shirt", "description": "Product Description", "image": "blue-t-shirt.jpg", "price": 29, "category": "Clothing", "quantity": 25, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1004", "code": "h456wer53", "name": "Bracelet", "description": "Product Description", "image": "bracelet.jpg", "price": 15, "category": "Accessories", "quantity": 73, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1005", "code": "av2231fwg", "name": "Brown Purse", "description": "Product Description", "image": "brown-purse.jpg", "price": 120, "category": "Accessories", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 4 },
                { "id": "1006", "code": "bib36pfvm", "name": "Chakra Bracelet", "description": "Product Description", "image": "chakra-bracelet.jpg", "price": 32, "category": "Accessories", "quantity": 5, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1007", "code": "mbvjkgip5", "name": "Galaxy Earrings", "description": "Product Description", "image": "galaxy-earrings.jpg", "price": 34, "category": "Accessories", "quantity": 23, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1008", "code": "vbb124btr", "name": "Game Controller", "description": "Product Description", "image": "game-controller.jpg", "price": 99, "category": "Electronics", "quantity": 2, "inventoryStatus": "LOWSTOCK", "rating": 4 },
                { "id": "1009", "code": "cm230f032", "name": "Gaming Set", "description": "Product Description", "image": "gaming-set.jpg", "price": 299, "category": "Electronics", "quantity": 63, "inventoryStatus": "INSTOCK", "rating": 3 },
                { "id": "1010", "code": "plb34234v", "name": "Gold Phone Case", "description": "Product Description", "image": "gold-phone-case.jpg", "price": 24, "category": "Accessories", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 4 },
                { "id": "1011", "code": "4920nnc2d", "name": "Green Earbuds", "description": "Product Description", "image": "green-earbuds.jpg", "price": 89, "category": "Electronics", "quantity": 23, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1012", "code": "250vm23cc", "name": "Green T-Shirt", "description": "Product Description", "image": "green-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 74, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1013", "code": "fldsmn31b", "name": "Grey T-Shirt", "description": "Product Description", "image": "grey-t-shirt.jpg", "price": 48, "category": "Clothing", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 3 },
                { "id": "1014", "code": "waas1x2as", "name": "Headphones", "description": "Product Description", "image": "headphones.jpg", "price": 175, "category": "Electronics", "quantity": 8, "inventoryStatus": "LOWSTOCK", "rating": 5 },
                { "id": "1015", "code": "vb34btbg5", "name": "Light Green T-Shirt", "description": "Product Description", "image": "light-green-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 34, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1016", "code": "k8l6j58jl", "name": "Lime Band", "description": "Product Description", "image": "lime-band.jpg", "price": 79, "category": "Fitness", "quantity": 12, "inventoryStatus": "INSTOCK", "rating": 3 },
                { "id": "1017", "code": "v435nn85n", "name": "Mini Speakers", "description": "Product Description", "image": "mini-speakers.jpg", "price": 85, "category": "Clothing", "quantity": 42, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1018", "code": "09zx9c0zc", "name": "Painted Phone Case", "description": "Product Description", "image": "painted-phone-case.jpg", "price": 56, "category": "Accessories", "quantity": 41, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1019", "code": "mnb5mb2m5", "name": "Pink Band", "description": "Product Description", "image": "pink-band.jpg", "price": 79, "category": "Fitness", "quantity": 63, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1020", "code": "r23fwf2w3", "name": "Pink Purse", "description": "Product Description", "image": "pink-purse.jpg", "price": 110, "category": "Accessories", "quantity": 0, "inventoryStatus": "OUTOFSTOCK", "rating": 4 },
                { "id": "1021", "code": "pxpzczo23", "name": "Purple Band", "description": "Product Description", "image": "purple-band.jpg", "price": 79, "category": "Fitness", "quantity": 6, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1022", "code": "2c42cb5cb", "name": "Purple Gemstone Necklace", "description": "Product Description", "image": "purple-gemstone-necklace.jpg", "price": 45, "category": "Accessories", "quantity": 62, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1023", "code": "5k43kkk23", "name": "Purple T-Shirt", "description": "Product Description", "image": "purple-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 2, "inventoryStatus": "LOWSTOCK", "rating": 5 },
                { "id": "1024", "code": "lm2tny2k4", "name": "Shoes", "description": "Product Description", "image": "shoes.jpg", "price": 64, "category": "Clothing", "quantity": 0, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1025", "code": "nbm5mv45n", "name": "Sneakers", "description": "Product Description", "image": "sneakers.jpg", "price": 78, "category": "Clothing", "quantity": 52, "inventoryStatus": "INSTOCK", "rating": 4 },
                { "id": "1026", "code": "zx23zc42c", "name": "Teal T-Shirt", "description": "Product Description", "image": "teal-t-shirt.jpg", "price": 49, "category": "Clothing", "quantity": 3, "inventoryStatus": "LOWSTOCK", "rating": 3 },
                { "id": "1027", "code": "acvx872gc", "name": "Yellow Earbuds", "description": "Product Description", "image": "yellow-earbuds.jpg", "price": 89, "category": "Electronics", "quantity": 35, "inventoryStatus": "INSTOCK", "rating": 3 },
                { "id": "1028", "code": "tx125ck42", "name": "Yoga Mat", "description": "Product Description", "image": "yoga-mat.jpg", "price": 20, "category": "Fitness", "quantity": 15, "inventoryStatus": "INSTOCK", "rating": 5 },
                { "id": "1029", "code": "gwuby345v", "name": "Yoga Set", "description": "Product Description", "image": "yoga-set.jpg", "price": 20, "category": "Fitness", "quantity": 25, "inventoryStatus": "INSTOCK", "rating": 8 }
            ]
        };

        this.searchSeller = {
            name: {
                type: "text",
                label: "Seller Name",
                required: false,
                value: null
            },
            itemname: {
                type: "text",
                label: "Stock Item",
                required: false,
                value: null
            },

        }

        this.searchFrom = {
            name: {
                type: "autoComplete",
                label: "Search By",
                required: false,
                value: null

            }, categoryId: {
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
            },
            instock: {
                type: "radiobutton",
                label: "Item Availability",
                required: false,
                value: null,
                defoptions: [],
                options: [{ name: 'In Stock', key: 'INSTOCK' },
                { name: 'Low Stock', key: 'LOWSTOCK' },
                { name: 'Out Of Stock', key: 'OUTOFSTOCK' }]
            }

        }
        this.searchFromBtn = {
            addbtn: {
                "name": "Search",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": false
            }
        }

        this.sortFrom = {
            sortby: {
                type: "radiobutton",
                label: "Sort By",
                required: true,
                value: null,
                defoptions: [],
                options: [{ name: 'Name', key: 'name' },
                { name: 'Price Low to High', key: 'plh' },
                { name: 'Price High to Low', key: 'phl' },
                { name: 'Rating', key: 'rating' }]
            }

        }

        this.sortSeller = {
            sortby: {
                type: "radiobutton",
                label: "Sort By",
                required: true,
                value: null,
                defoptions: [],
                options: [
                { name: 'Distance', key: 'distance' },
                { name: 'Name', key: 'name' }]
            }

        }



        
        this.sortFromBtn = {
            addbtn: {
                "name": "Sort",
                "hide": false
            },
            reset: {
                "name": "Reset",
                "hide": false
            }
        }
        this.sortFromOrder = {
            sortby: {
                type: "radiobutton",
                label: "Sort By",
                required: true,
                value: null,
                defoptions: [],
                options: [
                    { name: 'Sort By Order Date Asc', key: 'oda' },
                    { name: 'Sort By Order Date Desc', key: 'odd' },
                    { name: 'Sort By Delivery Date Asc', key: 'dda' },
                    { name: 'Sort By Delivery Date Desc', key: 'ddd' },
                ]
            }

        }

        this.ordersearch = {
            name: {
                type: "text",
                label: "Item Name",
                required: false,
                value: null

            },
            fromdate: {
                type: "calender",
                label: "From Date",
                required: false,
                value: null,
            },
            todate: {
                type: "calender",
                label: "To Date",
                required: false,
                value: null,
            }

        }

        this.categoryForm = {
            name: {
                type: "text",
                label: "Name",
                required: true,
                value: null,
                maxlength: 50,
                minlength: 0
            },
            orderBy: {
                type: "dropdown",
                label: "Order By",
                required: true,
                value: null,
                options: [{ name: 'Item', key: 'Item' },
                { name: 'Cart', key: 'Cart' }]
            }, image: {
                type: "fileUpload",
                label: "Image",
                required: false,
                value: null,
                path: null
            }
        }


        this.subCategoryForm = {

            categoryId: {
                type: "dropdown",
                label: "Category",
                required: true,
                value: null,
                options: [],
                bustype: 'category',
                autoload: true

            }, name: {
                type: "text",
                label: "Name",
                required: true,
                value: null,
                maxlength: 50,
                minlength: 0
            }, image: {
                type: "fileUpload",
                label: "Image",
                required: false,
                value: null,
                path: null
            }
        }


        this.brandForm = {

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
                required: true,
                value: null,
                options: [],
                bustype: 'subcategory',
                autoload: true,
                dependele: ['categoryId']
            }, name: {
                type: "text",
                label: "Name",
                required: true,
                value: null,
                maxlength: 10,
                minlength: 0
            }, image: {
                type: "fileUpload",
                label: "Image",
                required: false,
                value: null,
                path: null
            }
        }


        this.stockForm = {

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
                required: true,
                value: null,
                options: [],
                bustype: 'subcategory',
                autoload: true,
                dependele: ['categoryId'],
                updateEle: 'brandId'
            }, brandId: {
                type: "dropdown",
                label: "Brand",
                required: true,
                value: null,
                options: [],
                bustype: 'brand',
                autoload: true,
                dependele: ['subCategoryId']
            }, code: {
                type: "text",
                label: "code",
                required: true,
                value: null,
                maxlength: 20,
                minlength: 0
            }, name: {
                type: "text",
                label: "Name",
                required: true,
                value: null,
                maxlength: 100,
                minlength: 0
            }, packSize: {
                type: "multiselection",
                label: "Pack Size",
                required: true,
                value: null,
                defoptions: [],
                options: [],
                url: '/product/pack-options'

            }


            , description: {
                type: "textarea",
                label: "Description",
                required: true,
                value: '',
                maxlength: 500,
                minlength: 0
            }
            , image: {
                type: "fileUpload",
                label: "Image",
                required: false,
                value: null,
                path: null
            }
        }
        this.stockhier = {
            stockhier: {
                type: "stockhier",
                label: "Stock Hierarchy",
                required: false,
                value: null,
                maxlength: 10,
                minlength: 0

            }
        }

        this.chartFilter = {
            stockhier: {
                type: "stockhier",
                label: "Stock Hierarchy",
                required: false,
                value: null,
                maxlength: 10,
                minlength: 0

            },chartBy: {
                type: "dropdown",
                label: "Chart By",
                required: true,
                value: { name: 'Month', key: 'Month' },
                options: [{ name: 'Day', key: 'Day' },
                { name: 'Month', key: 'Month' },
                { name: 'Year', key: 'Year' }]
               
            }
        }

        this.sample = {
            name: {
                type: "stockhier",
                label: "Name",
                required: true,
                value: null,
                maxlength: 10,
                minlength: 0

            },
            email: {
                type: "calender",
                label: "Date of Birth",
                required: true,
                value: null,


            }, name1: {
                type: "password",
                label: "Password",
                required: true,
                value: null,


            },
            email1: {
                type: "number",
                label: "Age",
                required: true,
                value: 0,
                maxValue: 10,
                minMinvalu: 0
            },

            fileUpload: {
                type: "fileUpload",
                label: "File Upload",
                required: true,
                value: null,
                path: null


            }, fileUpload1: {
                type: "fileUpload",
                label: "File Upload",
                required: true,
                value: null,
                path: null


            }, fileUpload2: {
                type: "fileUpload",
                label: "File Upload",
                required: true,
                value: null,
                path: null


            },

            address: {
                type: "textarea",
                label: "Address",
                required: true,
                value: '',
                maxlength: 10,
                minlength: 0
            },
            checkBox: {
                type: "checkbox",
                label: "Check Box",
                required: true,
                value: [],
                defoptions: [],
                options: [{ name: 'Accounting', key: 'A' },
                { name: 'Marketing', key: 'M' },
                { name: 'Production', key: 'P' },
                { name: 'Research', key: 'R' }]
            },

            radiobutton: {
                type: "radiobutton",
                label: "Radio Button",
                required: true,
                value: null,
                defoptions: [],
                options: [{ name: 'Accounting', key: 'A' },
                { name: 'Marketing', key: 'M' },
                { name: 'Production', key: 'P' },
                { name: 'Research', key: 'R' }]
            },

            checkBox1: {
                type: "dropdown",
                label: "Drop Down",
                required: true,
                value: null,
                defoptions: [],
                options: [{ name: 'Accounting', key: 'A' },
                { name: 'Marketing', key: 'M' },
                { name: 'Production', key: 'P' },
                { name: 'Research', key: 'R' }],
                updateEle: 'states'
            },
            states: {
                type: "dropdown",
                label: "Drop Down",
                required: true,
                value: null,
                defoptions: [],
                options: []
            }, packSize: {
                type: "multiselection",
                label: "Pack Size",
                required: true,
                value: null,
                defoptions: [],
                options: [],
                url: '/product/pack-options'

            }



        };
    }






}
export default DataJson;