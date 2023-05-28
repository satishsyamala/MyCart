
import React, { useState, Component, useRef, useEffect } from 'react';
import { InputText } from 'primereact/inputtext';
import '../App.css';

import ProductService from '../service/ProductService';
import { Calendar } from 'primereact/calendar';
import Moment from 'moment';
import { Checkbox } from 'primereact/checkbox';
import { Button } from 'primereact/button';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Dropdown } from 'primereact/dropdown';
import { InputNumber } from 'primereact/inputnumber';
import { InputTextarea } from 'primereact/inputtextarea';
import { Toast } from 'primereact/toast';
import { RadioButton } from 'primereact/radiobutton';
import { Password } from 'primereact/password';
import { FileUpload } from 'primereact/fileupload';
import { Tag } from 'primereact/tag';
import { AutoComplete } from 'primereact/autocomplete';
import APIService from "../service/APIService"
import APP_URL from "../service/APIConfig"
import { MultiSelect } from 'primereact/multiselect';
import MapContainer from '../components/MapContainer'

function DynamicForm(props) {
  const toast = useRef(null);
  const productService = new ProductService();
  const [formSchema1, setFormSchema1] = useState();
  const [formSchema, setFormSchema] = useState();
  const [formBtn, setFormBtn] = useState(productService.getDynamicFormBtn(props.type));
  const [filePath, setFilePath] = useState();
  const [totalSize, setTotalSize] = useState(0);
  const [filteredCountries, setFilteredCountries] = useState(null);
  const [stockHier, setStockHier] = useState();


  useEffect(() => {
    let tempForm = props.formJson ? props.formJson : productService.getDynamicFrom(props.type);
    let check = true;
    let intvel = '';
    let onlybran = true;
    Object.keys(tempForm).map((key, ind) => {
      if (tempForm[key].type === 'stockhier') {
        let cat = { value: null, options: [] }
        let subcat = { value: null, options: [] }
        let brand = { value: null, options: [] }
        tempForm[key].cat = cat;
        tempForm[key].subcat = subcat;
        tempForm[key].brand = brand;
        check = false;
        intvel = key;
        onlybran=false;
      }
      if (tempForm[key].url) {
        check = false;
        intvel = key;
        onlybran=false;
      }
      if (tempForm[key].bustype) {
        check = false;
        intvel = key;
      
      }

    });
    if (check)
      setFormSchema(tempForm);
    else {
      Object.keys(tempForm).map((key, ind) => {

        if (tempForm[key].bustype) {
          let filt = getBussnessOptions(tempForm[key], tempForm);

          if (filt) {
            console.log('bustype ')
            APIService.postRequest('/product/get-cat-options', filt).then(data => {
              if (data) {
                tempForm[key].options = data.data[tempForm[key].bustype];
                console.log('bustype Res')
                if(onlybran)
                setFormSchema(tempForm);
               else if (intvel == key)
                 setFormSchema(tempForm);
              }
            });
          }
        }
        if (tempForm[key].url) {
          console.log('url ')
          APIService.postRequest(tempForm[key].url, {}).then(res => {
            if (res) {
              console.log('url res')
              tempForm[key].options = res.data.options;
              if (intvel == key)
              setFormSchema(tempForm);
            }
          });
        }

        if (tempForm[key].type === 'stockhier') {
          console.log('stockhier ')
          let filt = { action: false, type: 'category' }
          APIService.postRequest('/product/get-cat-options', filt).then(res => {
            if (res) {
              console.log('stockhier res')
              let cat = { value: null, options: res.data.category }
              let subcat = { value: null, options: [] }
              let brand = { value: null, options: [] }
              tempForm[key].cat = cat;
              tempForm[key].subcat = subcat;
              tempForm[key].brand = brand;
              if (intvel == key)
              setFormSchema(tempForm);
            }
          });
        }
      });



    }

  }, []);

  useEffect(() => {
    if (formSchema1) {
      setFormSchema(formSchema1);

    }
  }, [formSchema1]);

  Moment.locale('en');


  const growlMsg = (type, message) => {
    toast.current.show({ severity: type, summary: '', detail: message });

  }

  const getDynamicFormJson = () => {
    return this.state.formSchema;
  }

  const setSelected = (key, value) => {
    this.state.formSchema[key].value = value;
  }






  const setTextSelected = (key, value) => {
    let obj = { ...formSchema };
    let tempValue = value;
    if (obj[key].maxlength > 0)
      tempValue = value.slice(0, obj[key].maxlength);
    obj[key].value = tempValue;
    setFormSchema(obj);
  }

  const setNumberSelected = (key, value) => {
    let obj = { ...formSchema };
    obj[key].value = value;
    setFormSchema(obj);
  }



  const textElement = (key, jsonObj) => {
    return <InputText id={key} className="inputtext" readOnly={jsonObj.readonly} value={formSchema[key].value} keyfilter={formSchema[key].filter} onChange={(e) => setTextSelected(key, e.target.value)} />;
  }

  const passwordElement = (key, jsonObj) => {
    return <Password id={key} feedback={false} className="inputtext" value={formSchema[key].value} onChange={(e) => setTextSelected(key, e.target.value)} />;
  }

  const numberElement = (key, jsonObj) => {
    return <InputNumber id={key} className="numberfld" mode="decimal" useGrouping={false} value={formSchema[key].value} onChange={(e) => setNumberSelected(key, e.value)} />;
  }

  const textAreaElement = (key, jsonObj) => {
    return <div>
      <InputTextarea id={key} className="inputtextarea" autoResize={true} rows={5} value={formSchema[key].value} onChange={(e) => setTextSelected(key, e.target.value)} />
      <br /> {formSchema[key].value.trim().length}/{formSchema[key].maxlength}
    </div>
  }

  const setCalenderSelected = (key, value) => {
    let obj = { ...formSchema };
    obj[key].value = value;
    setFormSchema(obj);

  }

  const calenderElement = (key, jsonObj) => {
    if (!jsonObj.value)
      jsonObj.value = new Date();
    return <Calendar id={key} className="inputtext p-datepicker-today" value={jsonObj.value} onChange={(e) => setCalenderSelected(key, e.value)} readOnlyInput showIcon />
  }



  const setCheckBoxSelected = (key, e) => {
    let obj = { ...formSchema };
    let selectedCities = obj[key].value;
    if (e.checked)
      selectedCities.push(e.value);
    else
      selectedCities.splice(selectedCities.indexOf(e.value), 1);
    obj[key].value = selectedCities;
    setFormSchema(obj);
    console.log(JSON.stringify(formSchema));
  }


  const checkboxElement = (key, option) => {

    return (
      <div key={option.key} className="col-sm-8 p-field-checkbox">
        <Checkbox inputId={option.key} name={key} value={option.key} onChange={(e) => setCheckBoxSelected(key, e)}
          checked={formSchema[key].value.indexOf(option.key) !== -1} />
        <label htmlFor={option.key}>{option.name}</label>
      </div>
    )
  }
  const setRadioButtonSelected = (key, e) => {
    let obj = { ...formSchema };
    obj[key].value = e.value;
    setFormSchema(obj);
    console.log(JSON.stringify(formSchema));

  }


  const radioButtonElement = (key, option) => {

    return (
      <div key={option.key} className="p-field-radiobutton">
        <RadioButton inputId={option.key} name={key} value={option.key} onChange={(e) => setRadioButtonSelected(key, e)}
          checked={formSchema[key].value === option.key} />
        <label htmlFor={option.key}>{option.name}</label>
      </div>
    )
  }



  const onDropDownChange = (key, e) => {
    let obj = { ...formSchema };
    obj[key].value = e != null ? e.value : [];

    let ud = obj[key].updateEle;
    if (ud) {
      let fil = getBussnessOptions(obj[ud], obj);
      if (fil) {
        APIService.postRequest('/product/get-cat-options', fil).then(data => {
          obj[ud].options = data.data[obj[ud].bustype];
          obj[ud].value = null;
          if (obj[ud].updateEle) {
            obj[obj[ud].updateEle].options = [];
            obj[obj[ud].updateEle].value = null;
          }


          if (obj[ud] && obj[ud].options && obj[ud].options.length == 0) {
            onDropDownChange(ud, null);
          }
          setFormSchema(obj);
        });
      }
      else {
        setFormSchema(obj);
      }
    } else {
      setFormSchema(obj);
    }
  }

  const getBussnessOptions = (jsonObj, obj) => {
    let filter = {};
    let check = false;
    if (jsonObj.dependele && jsonObj.dependele.length > 0) {
      for (var i = 0; i < jsonObj.dependele.length; i++) {
        if (obj[jsonObj.dependele[i]] && obj[jsonObj.dependele[i]].value) {
          filter[jsonObj.dependele[i]] = obj[jsonObj.dependele[i]].value.key
          check = true;
        }
      }
    }
    else {
      check = true;
    }
    if (check) {
      filter.type = jsonObj.bustype;
      return filter;
    }
    else
      return null;

  }

  const dropdownElement = (key, jsonObj) => {
    //alert(key);
    //alert(JSON.stringify(jsonObj));
    return <Dropdown className="dropdown"
      value={formSchema[key].value} options={jsonObj.options}
      onChange={(e) => onDropDownChange(key, e)} optionLabel="name" filter showClear filterBy="name" placeholder={jsonObj.label}
    />
  }




  const invoiceUploadHandler = ({ files }) => {
    const [file] = files;
    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      onupload(e.target.result, file);
    };

    fileReader.readAsDataURL(file);
    return 'success';
  };

  const onupload = (props, file) => {
    let obj = { ...formSchema };
    obj[filePath].value = props;
    obj[filePath].path = file.objectURL;
    setFormSchema(obj);
  }

  const onSelect = (key) => {
    setFilePath(key);
  }

  const removeImage = (key) => {
    let obj = { ...formSchema };
    obj[key].value = null;
    obj[key].path = null;
    setFormSchema(obj);
  }



  const fileUpload = (key, jsonObj) => {
    return <div>
      {
        formSchema[key].path && <img className="dynaImage" src={formSchema[key].path} />

      }<Button type="submit" hidden={formSchema[key].value ? false : true} onClick={(e) => removeImage(key)} icon="pi pi-times"
        className="p-button-raised p-button-danger p-button-text remove_Image_Bt" />
      <FileUpload name="invoice"
        accept="image/*"
        customUpload={true}
        uploadHandler={invoiceUploadHandler}
        mode="basic"
        onSelect={(e) => onSelect(key)}
        auto={true}
        chooseLabel="Upload invoice" />



    </div>
  }

  const searchCountry = (event) => {
    setTimeout(() => {
      let _filteredCountries;
      let filterData = JSON.parse(APIService.getLocalStorage('filterdata'));
      if (filterData) {
        if (!event.query.trim().length) {
          _filteredCountries = [...filterData];
        }
        else {
          _filteredCountries = filterData.filter((country) => {
            return country.name.toLowerCase().startsWith(event.query.toLowerCase());
          });
        }

        setFilteredCountries(_filteredCountries);
      }
    }, 250);
  }

  const autoCompleteElement = (key, jsonObj) => {
    return <AutoComplete value={jsonObj.value} suggestions={filteredCountries} completeMethod={searchCountry} field="name" onSelect={(e) => setAutoCompleteSelected(key, e.value.name)} onChange={(e) => setAutoCompleteSelected(key, e.value)} />
  }

  const setAutoCompleteSelected = (key, value) => {
    let obj = { ...formSchema };
    obj[key].value = value;
    setFormSchema(obj);
  }

  const setOnecheckbox = (key, value) => {
    let obj = { ...formSchema };
    obj[key].value = value;
    setFormSchema(obj);
  }

  const onecheckboxElement = (key, jsonObj) => {
    return <div className="p-field-checkbox">
      <Checkbox inputId={key} checked={jsonObj.value} onChange={e => setOnecheckbox(key, e.checked)} />
      <label htmlFor={key}>{jsonObj.label}</label>
    </div>
  }

  const setSelectedMultiple = (key, value) => {
    let obj = { ...formSchema };
    obj[key].value = value;
    setFormSchema(obj);
  }

  const multiSelectionElement = (key, jsonObj) => {
    return <MultiSelect value={formSchema[key].value} options={jsonObj.options} onChange={(e) => setSelectedMultiple(key, e.value)}
      optionLabel="name" filter showClear filterBy="name" placeholder={jsonObj.label} />
  }

  const onLocationSet = (key, loc) => {
    let obj = { ...formSchema };
    obj.latitude.value = loc.lat;
    obj.longitude.value = loc.lon;
    setFormSchema(obj);
  }

  const mapElement = (key, jsonObj) => {
    return <div className="dynaImage"><MapContainer lat={formSchema.latitude.value} keyType={key} lon={formSchema.longitude.value} onSubmit={(key, loc) => onLocationSet(key, loc)} />
    </div>
  }

  const stockHierChange = (key, type, e) => {
    let temp = { ...formSchema }
    temp[key][type].value = e != null ? e.value : {};
    let filt = { action: false, type: type === 'cat' ? 'subcategory' : 'brand' }
    if (type === 'cat')
      filt.categoryId = temp[key][type].value.key;
    else
      filt.subCategoryId = temp[key][type].value.key;
    if (type !== 'brand') {
      APIService.postRequest('/product/get-cat-options', filt).then(res => {
        if (res) {
          if (type === 'cat') {
            temp[key].subcat.options = res.data.subcategory;
            temp[key].subcat.value = null;
            temp[key].brand.options = [];
            temp[key].brand.value = null;
          }
          else
            temp[key].brand.options = res.data.brand;
          temp[key].brand.value = null;
        }
        setFormSchema(temp);
      });
    } else {
      setFormSchema(temp);
    }


  }

  const stockHierarchy = (key, jsonObj) => {
    return <div>
      <div>
        Category<br />
        <Dropdown className="dropdown"
          value={formSchema[key].cat.value} options={formSchema[key].cat.options}
          onChange={(e) => stockHierChange(key, 'cat', e)} optionLabel="name" filter showClear filterBy="name" placeholder="Category"
        />    </div>
      <div>
        Sub Category<br />
        <Dropdown className="dropdown"
          value={formSchema[key].subcat.value} options={formSchema[key].subcat.options}
          onChange={(e) => stockHierChange(key, 'subcat', e)} optionLabel="name" filter showClear filterBy="name" placeholder="Sub Category"
        />    </div>
      <div>
        Brand<br />
        <Dropdown className="dropdown"
          value={formSchema[key].brand.value} options={formSchema[key].brand.options}
          onChange={(e) => stockHierChange(key, 'brand', e)} optionLabel="name" filter showClear filterBy="name" placeholder="Brand"
        />   </div>


    </div>
  }


  const getElements = (key, jsonObj) => {
    if (jsonObj.type === 'text')
      return textElement(key, jsonObj);
    if (jsonObj.type === 'password')
      return passwordElement(key, jsonObj);
    else if (jsonObj.type === 'number')
      return numberElement(key, jsonObj);
    else if (jsonObj.type === 'textarea')
      return textAreaElement(key, jsonObj);
    else if (jsonObj.type === 'calender')
      return calenderElement(key, jsonObj);
    else if (jsonObj.type === 'checkbox') {
      return (

        formSchema[key].options.map((option) => {
          return checkboxElement(key, option);
        }

        )
      )
    } else if (jsonObj.type === 'dropdown') {

      return dropdownElement(key, jsonObj);
    }

    else if (jsonObj.type === 'radiobutton') {
      return (
        formSchema[key].options.map((option) => {
          return radioButtonElement(key, option);
        }
        )
      )
    } else if (jsonObj.type === 'fileUpload') {
      return fileUpload(key, jsonObj);
    } else if (jsonObj.type === 'autoComplete') {
      return autoCompleteElement(key, jsonObj);
    } else if (jsonObj.type === 'onecheckbox') {
      return onecheckboxElement(key, jsonObj);
    } else if (jsonObj.type === 'multiselection') {
      return multiSelectionElement(key, jsonObj);
    } else if (jsonObj.type === 'map') {
      return mapElement(key, jsonObj);
    } else if (jsonObj.type === 'stockhier') {
      return stockHierarchy(key, jsonObj);
    }







  }






  const reset = () => {
    let obj = { ...formSchema };
    Object.keys(formSchema).forEach(function (key) {
      let obj = { ...formSchema };
      var jsonObj = obj[key];
      if (jsonObj.type === 'text')
        jsonObj.value = '';
      else if (jsonObj.type === 'password')
        jsonObj.value = '';
      else if (jsonObj.type === 'autoComplete')
        jsonObj.value = '';
      else if (jsonObj.type === 'number')
        jsonObj.value = 0;
      else if (jsonObj.type === 'textarea')
        jsonObj.value = '';
      else if (jsonObj.type === 'calender')
        jsonObj.value = null;
      else if (jsonObj.type === 'checkbox') {
        jsonObj.value = [];
      } else if (jsonObj.type === 'dropdown')
        jsonObj.value = null;
      else if (jsonObj.type === 'radiobutton') {
        jsonObj.value = null;
      }
    })
    setFormSchema(obj);
  }

  const onFormSubmit = () => {
    var resd = true;
    let tempJson = { ...formSchema };
    Object.keys(tempJson).forEach(function (key) {

      var obj = tempJson[key];
      tempJson[key].valid = false;
      tempJson[key].validmsg = null;
      if (obj.type === 'text' || obj.type === 'password' || obj.type === 'email' || obj.type === 'textarea' || obj.type === 'autoComplete') {
        if (obj.required && (obj.value === null || obj.value.length === 0)) {
          //growlMsg('error', obj.label + ' is required');
          tempJson[key].valid = true;
          tempJson[key].validmsg = obj.label + ' is required';
          resd = false;
        }
      }
      else if (obj.type === 'dropdown' || obj.type === 'multiselection') {
        if (obj.options.length > 0 && obj.required && obj.value === null) {
          // growlMsg('error', obj.label + ' is required');
          tempJson[key].valid = true;
          tempJson[key].validmsg = obj.label + ' is required';
          resd = false;
        }
      } else if (formSchema[key].type === 'stockhier' && obj.required) {
        let v = '';
        console.log(JSON.stringify(formSchema[key]))
        if (formSchema[key].cat.value)
          v += formSchema[key].cat.value.key + '@';
        if (formSchema[key].subcat.value)
          v += formSchema[key].subcat.value.key + '@';
        if (formSchema[key].brand.value)
          v += formSchema[key].brand.value.key + '@';
        console.log(v)
        if (v.length === 0) {
          tempJson[key].valid = true;
          tempJson[key].validmsg = obj.label + ' is required';
          resd = false;
        }
      }
      else if (obj.type === 'fileUpload') {
        if (obj.required && obj.path === null) {
          // growlMsg('error', obj.label + ' is required');
          tempJson[key].valid = true;
          tempJson[key].validmsg = obj.label + ' is required';
          resd = false;
        }
      }
      else {
        if (obj.required && obj.value === null) {
          // growlMsg('error', obj.label + ' is required');
          tempJson[key].valid = true;
          tempJson[key].validmsg = obj.label + ' is required';
          resd = false;
        }
      }
    })
    setFormSchema(tempJson);
    if (resd) {
      let res = {};
      Object.keys(formSchema).forEach(function (key) {
        if (formSchema[key].type === 'calender')
          res[key] = { type: formSchema[key].type, value: productService.dateToString(formSchema[key].value) };
        else if (formSchema[key].type === 'stockhier') {
          let v = '';
          let hr = { cat: 0, subcat: 0, brand: 0 };
          if (formSchema[key].cat.value) {
            v += formSchema[key].cat.value.key + '@';
            hr.cat = formSchema[key].cat.value.key;
          }
          if (formSchema[key].subcat.value) {
            v += formSchema[key].subcat.value.key + '@';
            hr.subcat = formSchema[key].subcat.value.key;
          }
          if (formSchema[key].brand.value) {
            v += formSchema[key].brand.value.key + '@';
            hr.brand = formSchema[key].brand.value.key;
          }
          res[key] = { type: formSchema[key].type, value: v, hier: hr };
        } else {
          res[key] = { type: formSchema[key].type, value: formSchema[key].value };
        }
      });
      props.onSubmit(res);
    }



  }

  return (
    <div className="formDiv">
      <Toast className="toast-demo" ref={toast} />
      <div align="right" hidden={!props.buttonsTop}  >
        <Button type="submit" hidden={formBtn.addbtn.hide} label={formBtn.addbtn.name} onClick={onFormSubmit} icon="pi pi-check" className="p-button-rounded" />
        <Button type="submit" hidden={formBtn.reset.hide} label={formBtn.reset.name} onClick={reset} icon="pi pi-times" className="p-button-rounded p-button-danger" />
      </div>
      <div className="row">
        {
          formSchema && Object.keys(formSchema).map((key, ind) => (
            !formSchema[key].hidde &&
            <div className={props.clname ? props.clname : 'col-sm-6'} key={key}>

              {formSchema[key].type !== 'onecheckbox' && formSchema[key].type !== 'stockhier' ?
                <div><label htmlFor={key}>{formSchema[key].label} {formSchema[key].required ? '*' : ''}</label><br /></div> : ''}
              {getElements(key, formSchema[key])}
              <span className="errormasg">{formSchema[key].validmsg}</span>


            </div>

          ))
        }
      </div><br />
      <div hidden={props.buttonsTop} className="formbottons">
        <Button type="submit" hidden={formBtn.addbtn.hide} label={formBtn.addbtn.name} onClick={onFormSubmit} icon="pi pi-check" className="p-button-rounded" />
        <Button type="submit" hidden={formBtn.reset.hide} label={formBtn.reset.name} onClick={reset} icon="pi pi-times" className="p-button-rounded p-button-danger" />

      </div>


    </div>
  )

} export default DynamicForm;