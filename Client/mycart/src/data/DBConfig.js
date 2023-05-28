export const DBConfig = {
  name: 'MyDB',
  version: 1,
  objectStoresMeta: [
    {
      store: 'configuration',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'user_id', keypath: 'user_id', options: { unique: true } },
        { name: 'last_sync_date', keypath: 'last_sync_date', options: { unique: false } },
      ]
    },
    {
      store: 'users',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'userId', keypath: 'userId', options: { unique: true } },
        { name: 'name', keypath: 'name', options: { unique: true } },
        { name: 'password', keypath: 'password', options: { unique: false } },
        { name: 'email', keypath: 'email', options: { unique: false } },
        { name: 'mobileNo', keypath: 'mobileNo', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'userType', keypath: 'userType', options: { unique: false } },
        { name: 'lastLogin', keypath: 'lastLogin', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'isoffline', keypath: 'isoffine', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    }, {
      store: 'delivery_address',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'delveryAddressId', keypath: 'delveryAddressId', options: { unique: false } },
        { name: 'userId', keypath: 'user_id', options: { unique: false } },
        { name: 'address', keypath: 'address', options: { unique: false } },
        { name: 'city', keypath: 'city', options: { unique: false } },
        { name: 'state', keypath: 'state', options: { unique: false } },
        { name: 'pinCode', keypath: 'pinCode', options: { unique: false } },
        { name: 'latitude', keypath: 'latitude', options: { unique: false } },
        { name: 'longitude', keypath: 'longitude', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    }, {
      store: 'category',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'categoryId', keypath: 'categoryId', options: { unique: true } },
        { name: 'name', keypath: 'name', options: { unique: true } },
        { name: 'orderBy', keypath: 'orderBy', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    },
    {
      store: 'sub_category',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'subCategoryId', keypath: 'subCategoryId', options: { unique: true } },
        { name: 'name', keypath: 'name', options: { unique: false } },
        { name: 'categoryId', keypath: 'categoryId', options: { unique: false } },
        { name: 'catName', keypath: 'catName', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    }, {
      store: 'brands',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'brandId', keypath: 'brandId', options: { unique: true } },
        { name: 'name', keypath: 'name', options: { unique: false } },
        { name: 'subCategoryId', keypath: 'subCategoryId', options: { unique: false } },
        { name: 'subCatName', keypath: 'subCatName', options: { unique: false } },
        { name: 'categoryId', keypath: 'categoryId', options: { unique: false } },
        { name: 'catName', keypath: 'catName', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    },

    
    {
      store: 'stock_items',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'stockItemId', keypath: 'stock_item_id', options: { unique: true } },
        { name: 'code', keypath: 'code', options: { unique: true } },
        { name: 'name', keypath: 'name', options: { unique: false } },
        { name: 'description', keypath: 'description', options: { unique: false } },
        { name: 'categoryId', keypath: 'categoryId', options: { unique: false } },
        { name: 'catName', keypath: 'catName', options: { unique: false } },
        { name: 'subCategoryId', keypath: 'subCategoryId', options: { unique: false } },
        { name: 'subCatName', keypath: 'subCatName', options: { unique: false } },
        { name: 'brandId', keypath: 'brandId', options: { unique: false } },
        { name: 'brandName', keypath: 'brandName', options: { unique: false } },
        { name: 'packSize', keypath: 'packSize', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
	    	{ name: 'uom', keypath: 'uom', options: { unique: false } },
        { name: 'price', keypath: 'price', options: { unique: false } },
        { name: 'discountPrice', keypath: 'discountPrice', options: { unique: false } },
        { name: 'isdiscount', keypath: 'isdiscount', options: { unique: false } },
        { name: 'quantity', keypath: 'quantity', options: { unique: false } },
        { name: 'rating', keypath: 'rating', options: { unique: false } },
        { name: 'inventoryStatus', keypath: 'inventoryStatus', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } }
      ]
    }, {
      store: 'stock_images',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'stock_item_id', keypath: 'stock_item_id', options: { unique: false } },
        { name: 'code', keypath: 'code', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    }, {
      store: 'cart_items',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'code', keypath: 'code', options: { unique: false } },
        { name: 'price', keypath: 'price', options: { unique: false } },
        { name: 'discount_price', keypath: 'discount_price', options: { unique: false } },
        { name: 'discount', keypath: 'discount', options: { unique: false } },
        { name: 'discount_per', keypath: 'discount', options: { unique: false } },
        { name: 'isdiscount', keypath: 'isdiscount', options: { unique: false } },
        { name: 'quantity', keypath: 'quantity', options: { unique: false } },
        { name: 'offer_id', keypath: 'offer_id', options: { unique: false } },
        { name: 'offer', keypath: 'offer', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'stock_items', keypath: 'stock_items', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
      ]
    }, {
      store: 'offers',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'offer_id', keypath: 'offer_id', options: { unique: false } },
        { name: 'name', keypath: 'name', options: { unique: false } },
        { name: 'description', keypath: 'description', options: { unique: false } },
        { name: 'start_date', keypath: 'start_date', options: { unique: false } },
        { name: 'end_date', keypath: 'end_date', options: { unique: false } },
        { name: 'offer_pre', keypath: 'offer_pre', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'stock_items', keypath: 'stock_items', options: { unique: false } },
      ]
    },{
      store: 'orders',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'order_ref', keypath: 'order_ref', options: { unique: true } },
        { name: 'tran_ref', keypath: 'tran_ref', options: { unique: false } },
        { name: 'user_id', keypath: 'user_id', options: { unique: false } },
        { name: 'user', keypath: 'user', options: { unique: false } },
        { name: 'image', keypath: 'image', options: { unique: false } },
        { name: 'name', keypath: 'name', options: { unique: false } },
        { name: 'cat_order_by', keypath: 'cat_order_by', options: { unique: false } },
        { name: 'order_date', keypath: 'order_date', options: { unique: false } },
        { name: 'delivery_address', keypath: 'delivery_address', options: { unique: false } },
        { name: 'del_address_id', keypath: 'del_address_id', options: { unique: false } },
        { name: 'del_lat', keypath: 'del_lat', options: { unique: false } },
        { name: 'del_log', keypath: 'del_log', options: { unique: false } },
        { name: 'payment_type', keypath: 'payment_type', options: { unique: false } },
        { name: 'delivery_date', keypath: 'delivery_date', options: { unique: false } },
        { name: 'price', keypath: 'price', options: { unique: false } },
        { name: 'discount', keypath: 'discount', options: { unique: false } },
        { name: 'final_price', keypath: 'final_price', options: { unique: false } },
        { name: 'status', keypath: 'status', options: { unique: false } },
        { name: 'sync_status', keypath: 'sync_status', options: { unique: false } },
        { name: 'cart_times', keypath: 'cart_times', options: { unique: false } },
      ]
    }, {
      store: 'last_sync_date',
      storeConfig: { keyPath: 'id', autoIncrement: true },
      storeSchema: [
        { name: 'madule_name', keypath: 'madule_name', options: { unique: false } },
        { name: 'date', keypath: 'date', options: { unique: false } }
      ]
    }
  ]
};