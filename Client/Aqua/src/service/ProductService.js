
import DataJson from '../data/DataJson';
import Configuration from '../data/Configuration';
import SellerForm from '../data/SellerForm';
import Orders from '../data/Orders';
import Users from '../data/Users';
import PriceListForm from '../data/PriceListForm';
import Moment from 'moment';


export default class ProductService {



    constructor(props) {
        this.dataJson = new DataJson();
        this.configuration = new Configuration();
        this.sellerForm = new SellerForm();
        this.orders = new Orders();
        this.users = new Users();
        this.priceListForm = new PriceListForm();
    }




    getDeliveryAddress(usersJson) {
        return [
            {
                del_address_id: 1, user_id: 10, address: 'Vedhaclassic, Suncity, Hyderabad 500028',
                lat: '17.354230', log: '78.395943', status: 'active', sync_status: 'closed'
            },
            {
                del_address_id: 2, user_id: 10, address: 'Mariapura, Geesukonda, Warangal 506330',
                lat: '17.354230', log: '78.395943', status: 'active', sync_status: 'closed'
            },
            {
                del_address_id: 3, user_id: 10, address: 'Vedhaclassic, Suncity, Hyderabad',
                lat: '17.354230', log: '78.395943', status: 'active', sync_status: 'closed'
            }
        ];
    }

    getCategories(usersJson) {
        return [
            { category_id: 1, order_by: 'item', name: 'Clothing', image: 'React/mycart/public/showcase/demo/images/category/cloths.jpg', status: 'active', sync_status: 'closed' },
            { category_id: 2, order_by: 'item', name: 'Home Appliances', image: 'React/mycart/public/showcase/demo/images/category/homeapp.jpg', status: 'active', sync_status: 'closed' },
            { category_id: 3, order_by: 'item', name: 'Electronics', image: 'React/mycart/public/showcase/demo/images/category/electricals.jpg', status: 'active', sync_status: 'closed' },
            { category_id: 5, order_by: 'cart', name: 'Veg & Fruits', image: 'React/mycart/public/showcase/demo/images/category/vegetables.jpg', status: 'active', sync_status: 'closed' },
            { category_id: 6, order_by: 'cart', name: 'Grocery', image: 'React/mycart/public/showcase/demo/images/category/grocery.jpg', status: 'active', sync_status: 'closed' },
        ];
    }

    getSubCategory(usersJson, catid) {
        let sub_cat = [
            { sub_cat_id: 1, name: 'Kids', image: 'React/mycart/public/showcase/demo/images/category/kids.jpg', category_id: 1, cat_name: 'Clothing', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 2, name: 'Mens', image: 'React/mycart/public/showcase/demo/images/category/mens.jpg', category_id: 1, cat_name: 'Clothing', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 11, name: 'Washing Machine', image: 'React/mycart/public/showcase/demo/images/category/washingmechine.jpg', category_id: 2, cat_name: 'Home Appliances', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 12, name: 'Refrigerator', image: 'React/mycart/public/showcase/demo/images/category/refrigerator.jpg', category_id: 2, cat_name: 'Home Appliances', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 3, name: 'Mobile', image: 'React/mycart/public/showcase/demo/images/category/mobile.jpg', category_id: 3, cat_name: 'Electronics', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 4, name: 'Laptops', image: 'React/mycart/public/showcase/demo/images/category/laptop.jpg', category_id: 3, cat_name: 'Electronics', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 5, name: 'Fruits', image: 'React/mycart/public/showcase/demo/images/category/fruits.jpg', category_id: 5, cat_name: 'Veg & Fruits', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 6, name: 'Vegetables', image: 'React/mycart/public/showcase/demo/images/category/vegetables.jpg', category_id: 5, cat_name: 'Veg & Fruits', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 7, name: 'Grocery', image: 'React/mycart/public/showcase/demo/images/category/grocery.jpg', category_id: 6, cat_name: 'Grocery', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 8, name: 'Drinks', image: 'React/mycart/public/showcase/demo/images/category/cooldrinks.jpg', category_id: 6, cat_name: 'Grocery', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 9, name: 'Chocolates', image: 'React/mycart/public/showcase/demo/images/category/chocolate.jpg', category_id: 6, cat_name: 'Grocery', status: 'active', sync_status: 'closed' },
            { sub_cat_id: 10, name: 'Biscuits', image: 'React/mycart/public/showcase/demo/images/category/biscuits.jpg', category_id: 6, cat_name: 'Grocery', status: 'active', sync_status: 'closed' }
        ];
        let ret = [];
        if (catid != null && catid > 0) {
            sub_cat.map(item => {
                if (item.category_id === catid) {
                    ret.push(item);
                }
            });
            return ret;
        }
        else {
            return sub_cat;
        }
    }

    generateStockItemJSON() {

        let finl = [];
        let stock_it = 1;
        let data = this.getStockItems1(null);
        for (var i = 0; i < data.length; i++) {
            data.stock_item_id = stock_it;
            data[i].image = 'React/mycart/public/showcase/demo/images/product/' + data[i].image;
            finl.push(data[i]);
            stock_it++;
        }

        let cat = this.getCategories(null);
        for (var a = 0; a < cat.length; a++) {
            let subcat = this.getSubCategory(null, cat[a].category_id);
            for (var b = 0; b < subcat.length; b++) {
                let brand = this.getBrands(null, subcat[b].sub_cat_id);
                if (brand.length === 0) {
                    for (var i = 1; i < 100; i++) {
                        finl.push(this.stockJSON(cat[a], subcat[b], 0, '', stock_it));
                        stock_it++;
                    }
                }
                else {
                    for (var c = 0; c < brand.length; c++) {
                        for (var i = 1; i < 100; i++) {
                            finl.push(this.stockJSON(cat[a], subcat[b], brand[c].brand_id, brand[c].name, stock_it));
                            stock_it++;
                        }
                    }
                }
            }
        }
        return finl;


    }

    stockJSON(cat, subCat, barnID, brandName, id) {
        let dis = this.genRandomNumber(0, 10);
        let qty = this.genRandomNumber(0, 10);
        return {
            "stock_item_id": id,
            "code": id,
            "name": subCat.name + " " + id,
            "category_id": cat.category_id,
            "category_name": cat.name,
            "sub_cat_id": subCat.sub_cat_id,
            "sub_cat_name": subCat.name,
            "brand_id": barnID,
            "brand_name": brandName,
            "description": cat.name + ' ' + subCat.name + ' ' + brandName,
            "image": subCat.image,
            "uom": cat.order_by === 'item' ? 'Pc' : 'Kg',
            "price": this.genRandomNumber(50, 10000),
            "discount_price": dis,
            "isdiscount": dis > 0 ? true : false,
            "quantity": qty,
            "rating": this.genRandomNumber(1, 5),
            "inventory_status": qty === 0 ? 'OUTOFSTOCK' : (qty < 3 ? 'LOWSTOCK' : 'INSTOCK'),
            "statu": "active",
            "sync_status": "closed"
        };
    }

    getBrands(usersJson, sub_cat_id) {
        let sub_cat = [
            { brand_id: 1, name: "Samsung", sub_cat_id: 3, sub_cat_name: 'Mobile', image: 'React/mycart/public/showcase/demo/images/category/samsung.jpg', category_id: 3, cat_name: 'Electronics', status: 'active', sync_status: 'closed' },
            { brand_id: 2, name: "Samsung", sub_cat_id: 4, sub_cat_name: 'Laptops', image: 'React/mycart/public/showcase/demo/images/category/samsung.jpg', category_id: 3, cat_name: 'Electronics', status: 'active', sync_status: 'closed' },
            { brand_id: 3, name: "Red MI", sub_cat_id: 3, sub_cat_name: 'Mobile', image: 'React/mycart/public/showcase/demo/images/category/mi.jpg', category_id: 3, cat_name: 'Electronics', status: 'active', sync_status: 'closed' },
            { brand_id: 4, name: "Dell", sub_cat_id: 4, sub_cat_name: 'Laptops', image: 'React/mycart/public/showcase/demo/images/category/dell.jpg', category_id: 3, cat_name: 'Electronics', status: 'active', sync_status: 'closed' },
            { brand_id: 5, name: "Samsung", sub_cat_id: 11, sub_cat_name: 'Washing Machine', image: 'React/mycart/public/showcase/demo/images/category/samsung.jpg', category_id: 2, cat_name: 'Home Appliances', status: 'active', sync_status: 'closed' },
            { brand_id: 6, name: "LG", sub_cat_id: 11, sub_cat_name: 'Washing Machine', image: 'React/mycart/public/showcase/demo/images/category/lg.jpg', category_id: 2, cat_name: 'Home Appliances', status: 'active', sync_status: 'closed' },
            { brand_id: 7, name: "Samsung", sub_cat_id: 12, sub_cat_name: 'Refrigerator', image: 'React/mycart/public/showcase/demo/images/category/samsung.jpg', category_id: 2, cat_name: 'Home Appliances', status: 'active', sync_status: 'closed' },
            { brand_id: 8, name: "LG", sub_cat_id: 12, sub_cat_name: 'Refrigerator', image: 'React/mycart/public/showcase/demo/images/category/lg.jpg', category_id: 2, cat_name: 'Home Appliances', status: 'active', sync_status: 'closed' },



        ];
        let ret = [];
        if (sub_cat_id != null && sub_cat_id > 0) {
            sub_cat.map(item => {
                if (item.sub_cat_id === sub_cat_id) {
                    ret.push(item);
                }
            });
            return ret;
        }
        else {
            return sub_cat;
        }
    }


    getCartItems(usersJson) {
        return [];
    }

    getStockItems1(usersJson) {
        return [{ "stock_item_id": 31, "code": "apple001", "name": "Apple", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Apple", "image": "apple.jpg", "uom": "Kg", "price": 120, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 32, "code": "bananas001", "name": "Bananas", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Bananas", "image": "bananas.jpg", "uom": "Kg", "price": 50, "discount_price": 0, "isdiscount": false, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 33, "code": "grapes001", "name": "Grapes", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Grapes", "image": "grapes.jpg", "uom": "Kg", "price": 100, "discount_price": 7, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 34, "code": "guava001", "name": "Guava", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Guava", "image": "guava.jpg", "uom": "Kg", "price": 90, "discount_price": 10, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 35, "code": "mango001", "name": "Mango", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Mango", "image": "mango.jpg", "uom": "Kg", "price": 60, "discount_price": 0, "isdiscount": false, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 36, "code": "pineapple001", "name": "Pineapple", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Pineapple", "image": "pineapple.jpg", "uom": "Kg", "price": 40, "discount_price": 2, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 37, "code": "watermelon001", "name": "Watermelon", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 5, "sub_cat_name": "Fruits", "brand_id": 0, "brand_name": "", "description": "Watermelon", "image": "watermelon.jpg", "uom": "Kg", "price": 80, "discount_price": 0, "isdiscount": false, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" },
        { "stock_item_id": 38, "code": "cabbage001", "name": "Cabbage", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Cabbage", "image": "cabbage.jpg", "uom": "Kg", "price": 40, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 39, "code": "beanse001", "name": "Beans", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Beans", "image": "beans.jpg", "uom": "Kg", "price": 80, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 40, "code": "bottlegourd001", "name": "Bottle gourd", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Bottle gourd", "image": "bottle gourd.jpg", "uom": "Kg", "price": 30, "discount_price": 2, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 41, "code": "brinjal001", "name": "Brinjal", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Brinjal", "image": "brinjal.jpg", "uom": "Kg", "price": 50, "discount_price": 0, "isdiscount": false, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 42, "code": "carrot", "name": "Carrot", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Carrot", "image": "carrot.jpg", "uom": "Kg", "price": 80, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 43, "code": "cauliflower", "name": "Cauliflower", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Cauliflower", "image": "cauliflower.jpg", "uom": "Kg", "price": 45, "discount_price": 3, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 44, "code": "drumstick", "name": "Drumstick", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Drumstick", "image": "drumstick.jpg", "uom": "Kg", "price": 20, "discount_price": 3, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 45, "code": "ladies finger", "name": "Ladies finger", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Ladies finger", "image": "ladies finger.jpg", "uom": "Kg", "price": 46, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 46, "code": "tomato", "name": "Tomato", "category_id": 5, "category_name": "Veg & Fruits", "sub_cat_id": 6, "sub_cat_name": "Vegetables", "brand_id": 0, "brand_name": "", "description": "Tomato", "image": "tomato.jpg", "uom": "Kg", "price": 20, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" },
        { "stock_item_id": 1, "code": "f230fh0g3", "name": "Bamboo Watch", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Bamboo Watch", "image": "bamboo-watch.jpg", "uom": "Kg", "price": 65, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 2, "code": "nvklal433", "name": "Black Watch", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Black Watch", "image": "black-watch.jpg", "uom": "Kg", "price": 72, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 4, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 5, "code": "h456wer53", "name": "Bracelet", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Bracelet", "image": "bracelet.jpg", "uom": "Kg", "price": 15, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 4, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 6, "code": "av2231fwg", "name": "Brown Purse", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Brown Purse", "image": "brown-purse.jpg", "uom": "Kg", "price": 120, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 4, "inventory_status": "OUTOFSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 7, "code": "bib36pfvm", "name": "Chakra Bracelet", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Chakra Bracelet", "image": "chakra-bracelet.jpg", "uom": "Kg", "price": 32, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 3, "inventory_status": "LOWSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 8, "code": "mbvjkgip5", "name": "Galaxy Earrings", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Galaxy Earrings", "image": "galaxy-earrings.jpg", "uom": "Kg", "price": 34, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 11, "code": "plb34234v", "name": "Gold Phone Case", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Gold Phone Case", "image": "gold-phone-case.jpg", "uom": "Kg", "price": 24, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 4, "inventory_status": "OUTOFSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 19, "code": "09zx9c0zc", "name": "Painted Phone Case", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Painted Phone Case", "image": "painted-phone-case.jpg", "uom": "Kg", "price": 56, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 5, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 21, "code": "r23fwf2w3", "name": "Pink Purse", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Pink Purse", "image": "pink-purse.jpg", "uom": "Kg", "price": 110, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 4, "inventory_status": "OUTOFSTOCK", "statu": "active", "sync_status": "closed" }, { "stock_item_id": 23, "code": "2c42cb5cb", "name": "Purple Gemstone Necklace", "category_id": 3, "category_name": "Electronics", "sub_cat_id": 3, "sub_cat_name": "Mobils", "brand_id": 3, "brand_name": "Red MI", "description": "Purple Gemstone Necklace", "image": "purple-gemstone-necklace.jpg", "uom": "Kg", "price": 45, "discount_price": 5, "isdiscount": true, "quantity": 1, "rating": 4, "inventory_status": "INSTOCK", "statu": "active", "sync_status": "closed" }
        ];
    }

    getStockItems(usersJson) {
        return this.generateStockItemJSON();
    }


    getStockImages(stockItem) {
        return [
            { stock_item_id: stockItem.stock_item_id, code: stockItem.code, image: 'React/mycart/public/showcase/demo/images/product/apple.jpg', statu: 'active', sync_status: 'closed' },
            { stock_item_id: stockItem.stock_item_id, code: stockItem.code, image: 'React/mycart/public/showcase/demo/images/product/tomato.jpg', statu: 'active', sync_status: 'closed' },
            { stock_item_id: stockItem.stock_item_id, code: stockItem.code, image: 'React/mycart/public/showcase/demo/images/product/carrot.jpg', statu: 'active', sync_status: 'closed' },
            { stock_item_id: stockItem.stock_item_id, code: stockItem.code, image: 'React/mycart/public/showcase/demo/images/product/brinjal.jpg', statu: 'active', sync_status: 'closed' },
            { stock_item_id: stockItem.stock_item_id, code: stockItem.code, image: 'React/mycart/public/showcase/demo/images/product/black-watch.jpg', statu: 'active', sync_status: 'closed' }
        ];
    }



    getProductsSmall() {
        return this.dataJson.dataTable;
    }

    getProducts() {

        return this.dataJson.products;
    }

    getProductsWithOrdersSmall() {
        return '';
    }

    getDynamicFrom(name) {
        if (name === 'register') {
            return this.dataJson.registerForm;
        }
        else if (name === 'login') {
            return this.dataJson.loginform;;
        } else if (name === 'delivery') {
            return this.dataJson.delivery_address;;
        } else if (name === 'changepassword') {
            return this.dataJson.passwordchangeform;;
        } else if (name === 'forgotpassword') {
            return this.dataJson.forgotpassword;;
        }


        else if (name === 'itemSearch') {
            return this.dataJson.searchFrom;
        } else if (name === 'searchSeller') {
            return this.dataJson.searchSeller;
        }


        else if (name === 'itemSort') {
            return this.dataJson.sortFrom;
        } else if (name === 'sellerSort') {
            return this.dataJson.sortSeller;
        }



        else if (name === 'updateprofile') {
            return this.dataJson.updateprofile;;
        }
        else if (name === 'ordersort' || name === 'allorders') {
            return this.dataJson.sortFromOrder;
        }
        else if (name === 'orderfilter') {
            return this.dataJson.ordersearch;;
        }
        else if (name === 'category') {
            return this.dataJson.categoryForm;
        }
        else if (name === 'subcategory') {
            return this.dataJson.subCategoryForm;
        } else if (name === 'brand') {
            return this.dataJson.brandForm;
        } else if (name === 'stock') {
            return this.dataJson.stockForm;
        } else if (name === 'sellerform') {
            return this.sellerForm.sellerForm;
        } else if (name === 'stockmap') {
            return this.sellerForm.stockmapform;
        } else if (name === 'stockmapedit') {
            return this.sellerForm.stockmapedit;
        }
        else if (name === 'AdminUsers' || name === 'DeliveryUsers') {
            return this.users.adminuser;
        } else if (name === 'SellerUsers') {
            return this.users.selleruser;
        }
        else if (name === 'pricelist') {
            return this.priceListForm.pricelistform;
        }

        else if (name === 'gensettings') {
            return this.priceListForm.genSettings;
        }
        else if (name === 'deliverycharge') {
            return this.priceListForm.deliveryCharge;
        }
        else if (name === 'subscription') {
            return this.priceListForm.subscription;
        } else if (name === 'stockhier') {
            return this.dataJson.stockhier;
        }else if (name === 'chartFilter') {
            return this.dataJson.chartFilter;
        }
       else {
            return this.dataJson.sample;
        }
    }



    getFilterForm(name) {
        if (name === 'stock' || name === 'pricelistmap') {
            return this.configuration.stockFilter;
        } else if (name === 'brand') {
            return this.configuration.brandFilter;
        } else if (name === 'allorders') {
            return this.orders.ordersFilter;
        } else if (name === 'AdminUsers') {
            return this.users.addminfilter;
        } else if (name === 'Sellers') {
            return this.users.sellerfilter;
        }
        else if (name === 'Consumers') {
            return this.users.consumerfilter;
        } else if (name === 'SellerUsers') {
            return this.users.selleruserfilter;
        }


    }

    getDynamicFormBtn(name) {
        if (name === 'register' || name === 'category') {
            return this.dataJson.regbtn;
        } else if (name === 'login' ) {
            return this.dataJson.loginbtn;
        } else if ( name === 'stockhier' || name === 'chartFilter') {
            return this.dataJson.stockHier;
        } else if (name === 'checkout') {
            return this.dataJson.checkout;
        } else if (name === 'forgotpassword') {
            return this.dataJson.forgotpass;
        }
        else if (name === 'itemSearch' || name === 'orderfilter') {
            return this.dataJson.searchFromBtn;
        } else if (name === 'itemSort' || name === 'ordersort') {
            return this.dataJson.sortFromBtn;
        } else if (name === 'updateprofile') {
            return this.dataJson.updateprofileBtn;
        }
        else if (name === 'pricelist') {
            return this.priceListForm.priceformbtn;
        }

        else {
            return this.dataJson.regbtn;
        }
    }

    getDynamicTableHeader(name) {
        if (name === 'users') {
            return this.dataJson.userHeader;
        }
        else if (name === 'delivery') {
            return this.dataJson.deliveryHeader;
        } else if (name === 'category') {
            return this.dataJson.categoryHeader;
        } else if (name === 'subcategory') {
            return this.dataJson.subCategoryHeader;
        } else if (name === 'brand') {
            return this.dataJson.brandsHeader;
        } else if (name === 'stock') {
            return this.dataJson.stockHeader;
        } else if (name === 'import') {
            return this.configuration.importHeader;
        } else if (name === 'stockmap') {
            return this.sellerForm.stockMap;
        } else if (name === 'allorders') {
            return this.orders.ordersTable;
        } else if (name === 'AdminUsers' || name === 'Consumers' || name === 'SellerUsers' || name === 'DeliveryUsers') {
            return this.users.adminusers;
        } else if (name === 'Sellers') {
            return this.users.sellers;
        }
        else if (name === 'pricelist') {
            return this.priceListForm.priceListTable;
        } else if (name === 'pricelistmap') {
            return this.priceListForm.stockMap;
        } else if (name === 'pricelistadd') {
            return this.priceListForm.addedMap;
        } else if (name === 'subscription') {
            return this.priceListForm.subscriptionheaders;
        }









        else {
            return null;
        }
    }

    getTableDropdownOptions(name) {
        return [{ name: "Percentage", key: "Percentage" }, { name: "New Price", key: "New price" }]
    }

    getTableBtnDetais(name) {
        if (name === 'delivery')
            return {
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
                    hidden: false
                },
                view: {
                    name: 'Orders',
                    hidden: false
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
        else if (name === 'category' || name === 'subcategory' || name === 'brand')
            return {
                add: {
                    name: 'Add',
                    hidden: false,
                    icon: 'pi pi-plus'
                },
                op2: {
                    name: 'Filter',
                    hidden: name === 'category' ? true : false,
                    icon: 'pi pi-filter'
                },
                op3: {
                    name: 'Sort',
                    hidden: true,
                    icon: 'pi pi-sort-alt'
                },
                edit: {
                    name: 'Edit',
                    hidden: false
                },
                view: {
                    name: 'View',
                    hidden: false
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

        else if (name === 'stockmap')
            return {
                add: {
                    name: 'Add',
                    hidden: false,
                    icon: 'pi pi-plus'
                },
                op2: {
                    name: 'Filter',
                    hidden: name === 'category' ? true : false,
                    icon: 'pi pi-filter'
                },
                op3: {
                    name: 'Sort',
                    hidden: true,
                    icon: 'pi pi-sort-alt'
                },
                edit: {
                    name: 'Update Price',
                    hidden: false
                },
                view: {
                    name: 'View',
                    hidden: false
                },
                del: {
                    name: 'Remove',
                    hidden: false
                },
                op1: {
                    name: 'Option 1',
                    hidden: true
                },
            };

        else if (name === 'stock') {
            return {
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
                    name: 'Export',
                    hidden: false,
                    icon: 'pi pi-file-excel'
                },
                edit: {
                    name: 'Edit',
                    hidden: false
                },
                view: {
                    name: 'View',
                    hidden: false
                },
                del: {
                    name: 'Update Price',
                    hidden: false
                },
                op1: {
                    name: 'Add Images',
                    hidden: false
                },
            };
        }
        else if (name === 'pricelistmap') {
            return {
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
                    name: 'Edit',
                    hidden: false
                },
                view: {
                    name: 'View',
                    hidden: false
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
        }

        else if (name === 'import')
            return this.configuration.importTbBtn;
        else if (name === 'allorders')
            return this.orders.allOrders;
        else if (name === 'AdminUsers' || name === 'SellerUsers' || name === 'DeliveryUsers') {
            return this.users.adminusersBtn;
        } else if (name === 'Sellers') {
            return this.users.sellerBtn;
        } else if (name === 'Consumers') {
            return this.users.consumersBtn;
        } else if (name === 'pricelist') {
            return this.priceListForm.pricelistbtn;
        } else if (name === 'subscription') {
            return this.priceListForm.subscriptionbtn;
        }


        else
            return {
                add: {
                    name: 'Add',
                    hidden: true
                },
                edit: {
                    name: 'Edit',
                    hidden: true
                },
                view: {
                    name: 'View',
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
                op2: {
                    name: 'Option 2',
                    hidden: true
                },
                op3: {
                    name: 'Option 3',
                    hidden: true
                },
            };

    }





    getMenuItems(userType) {
        if (userType === 'Admin')
            return this.dataJson.adminmenus;
        else if (userType === 'Seller')
            return this.dataJson.sellermenus;
        else
            return this.dataJson.consumermenu;
    }

    getMenuItemsDelivery(userType) {
        return this.dataJson.delivery;
    }

    getOptionJSON() {
        return [{ name: 'Accounting', key: 'A' },
        { name: 'Marketing', key: 'M' },
        { name: 'Production', key: 'P' },
        { name: 'Research', key: 'R' }]
    }



    getOfferDetails(usersJson) {
        return [
            { offer_id: 1, name: "30% off on Vegetables", description: "30% off on Vegetables", offer_pre: 30, start_date: '22-JUN-2021', end_date: '30-JUN-2021', image: 'showcase/demo/images/offers/offerveg.jpg', stock_items: [{ "stock_item_id": 44 }, { "stock_item_id": 43 }, { "stock_item_id": 42 }] },
            { offer_id: 1, name: "20% off on fruits", description: "20% off on fruits", offer_pre: 20, start_date: '22-JUN-2021', end_date: '30-JUN-2021', image: 'showcase/demo/images/offers/fruitsoffer.jpg', stock_items: [{ "stock_item_id": 34 }, { "stock_item_id": 35 }, { "stock_item_id": 36 }] }
        ];
    }

    sortJSONArray(data, key, option) {
        if (option === null || option === 'asc')
            return data.sort((a, b) => (a[key] > b[key]) ? 1 : -1);
        else
            return data.sort((a, b) => (a[key] < b[key]) ? 1 : -1);
    }

    sortJSONArrayDate(data, key, option) {
        if (option === null || option === 'asc')
            return data.sort((a, b) => (this.stringTodate(a[key]) > this.stringTodate(b[key])) ? 1 : -1);
        else
            return data.sort((a, b) => (this.stringTodate(a[key]) < this.stringTodate(b[key])) ? 1 : -1);
    }

    stringTodate(vale) {
        Moment.locale('en');
        return Moment(vale).toDate();
    }

    dateToString(vale) {
        Moment.locale('en');
        return Moment(vale).format('DD-MM-yyyy HH:mm:ss');
    }

    getDataFormDB(dbObj, filterKey, value) {
        let res = [];
        dbObj.getAll().then(data => {
            if (value !== null) {
                data.map(item => {
                    if (item[filterKey] === value) {
                        res.push(item);
                    }
                })

            } else {
                res = data;
            }
        })
        return res;
    }

    getDeliveryDate(userName, item) {
        let date = new Date();
        let m = this.genRandomNumber(2, 5);

        date.setDate(date.getDate() + m);
        return date;
    }

    genRandomNumber(min, max) {
        return Math.floor((Math.random() * max) + min);
    }

    orderTrack(order) {
        return [
            { status: 'Ordered', date: order.order_date, icon: 'pi pi-shopping-cart', color: 'green' },
            { status: 'Processing', date: '15/10/2020 14:00', icon: 'pi pi-cog', color: 'green' },
            { status: 'Shipped', date: '15/10/2020 16:15', icon: 'pi pi-shopping-cart', color: 'green' },
            { status: 'Delivered', date: '', icon: 'pi pi-check', color: '' }
        ];
    }

    getBussnessOptions(type, filters) {
        let result = [];
        if (type === 'category') {
            let cat = this.getCategories();
            for (var i = 0; i < cat.length; i++) {
                result.push({ name: cat[i].name, key: cat[i].category_id });
            }
        } else if (type === 'subcategory') {
            let cat = this.getSubCategory(null, filters.category);
            for (var i = 0; i < cat.length; i++) {
                result.push({ name: cat[i].name, key: cat[i].sub_cat_id });
            }
        } else if (type === 'brand') {
            let cat = this.getBrands(null, filters.subcategory);
            for (var i = 0; i < cat.length; i++) {
                result.push({ name: cat[i].name, key: cat[i].sub_cat_id });
            }
        }



        return result;
    }


    getImportModuleList(menu) {
        if (menu === 'imports')
            return [
                { name: "Category", key: "category" },
                { name: "Sub Category", key: "subcategory" },
                { name: "Brands", key: "brands" },
                { name: "Stock Items", key: "stockitems" }

            ];
        else
            return [
                { name: "Stock Items", key: "stockitems" },
                { name: "Price", key: "price" }
            ];
    }


}
