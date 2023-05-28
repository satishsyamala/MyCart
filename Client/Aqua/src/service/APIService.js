import axios from 'axios';
import APP_URL from "./APIConfig"
import Moment from 'moment';
import * as actionTypes from '../actiontype';

var CryptoJS = require("crypto-js");

const APIService = {
    getRequest,
    postRequest,
    getCurrentDate,
    getOnlyCurrentDate,
    postRequestFile,
    setJSONLocalStorage,
    getJSONLocalStorage,
    setLocalStorage,
    getLocalStorage,
    setHeader,
    tokenUpdate,
    lineAndbarChartTotable
};




async function getRequest(url, data) {
    const headers = {
        "Authorization": getLocalStorage('token')
    };
    return await axios.get(APP_URL + url, { data });
}
async function postRequest(url, data1) {
    let token = getLocalStorage('token');

    if (token && url != '/user/login-check') {
        let data = encrypt(JSON.stringify(data1));
        console.log(url);
        return await axios.post(APP_URL + url, { data }, {
            headers: {
                "Authorization": 'Bearer ' + token
            }
        }).catch(function (error) {
            console.log(JSON.stringify(error));
            if (error.message = 'Request failed with status code 401') {
                generatToken().then(res => {
                    if (res) {
                        setLocalStorage('token', res.data.token);
                        return reloadpage(url, data, res.data.token);
                    }
                })
            }
        });
    }
    else {
        let data = encrypt(JSON.stringify(data1));
        return await axios.post(APP_URL + url, { data }).catch(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

function diff_minutes(dt2, dt1) {
    //  console.log("dt2 : " + dt2);
    //  console.log("dt1 : " + dt1);
    var diff = (dt2.getTime() - dt1.getTime()) / 1000;
    diff /= 60;
    return Math.abs(Math.round(diff));
}

function tokenUpdate() {
    let tt = getJSONLocalStorage('toke-time');
    let cd = new Date();
    let d = diff_minutes(cd, new Date(tt.tokedate));
    let time = tt.session_time - 5;
    //   console.log("Time : " + time);

    if (d > time) {
        console.log("d : " + d);
        generatToken().then(res => {
            if (res) {
                setLocalStorage('token', res.data.token);
                let toketime = { tokedate: new Date(), session_time: 30 };
                APIService.setJSONLocalStorage('toke-time', toketime);
            }
        });
    }
}

async function reloadpage(url, data, token) {
    return await axios.post(APP_URL + url, { data }, {
        headers: {
            "Authorization": 'Bearer ' + token
        }
    }).then();
}


async function generatToken() {
    let us = getJSONLocalStorage('myData');
    let us1 = { user_name: us.mobileNo, password: us.password }
    let data = encrypt(JSON.stringify(us1));
    return await axios.post(APP_URL + '/user/gen-token', { data }).then();
}

async function postRequestFile(url, data1) {
    let data = encrypt(JSON.stringify(data1));
    return await axios.post(APP_URL + url, { data }, {
        responseType: 'blob',
    }).catch(function (error) {
        alert(error.message);
    });
}

function getCurrentDate() {
    Moment.locale('en');
    return Moment(new Date()).format('DD-MM-yyyy HH:mm:ss')
}

function getOnlyCurrentDate() {
    Moment.locale('en');
    return Moment(new Date()).format('DD-MM-yyyy')
}

function setJSONLocalStorage(key, json) {
    setLocalStorage(key, JSON.stringify(json))
}

function getJSONLocalStorage(key) {
    let json = getLocalStorage(key);
    if (json)
        return JSON.parse(json);
}

function setLocalStorage(key, data) {

    let endata = CryptoJS.AES.encrypt(data + '', 'my-secret-key@123').toString();
    localStorage.setItem(key, endata)
}

function setHeader(props, header) {

}


function encrypt(data) {
    return CryptoJS.AES.encrypt(data + '', 'my-secret-key@123').toString();
}

function getLocalStorage(key) {
    let ciphertext = localStorage.getItem(key);
    if (ciphertext) {
        var bytes = CryptoJS.AES.decrypt(ciphertext, 'my-secret-key@123');
        return bytes.toString(CryptoJS.enc.Utf8);
    }
   
}

function lineAndbarChartTotable(chart_date) {
    let result = {};
    let headers = [];
    let data = [];
    let d = chart_date.datasets;
  
    let l = chart_date.labels;
   
    let h = { key: "label", name: "" };
    headers.push(h);
    console.log(d.length);
    for (var i = 0; i < d.length; i++) {
        let h = { key: d[i].label, name: d[i].label };
        headers.push(h);
    }
    let total = { label: 'Total' };
    for (var k = 0; k < l.length; k++) {
        let row = { label: l[k] };
        for (var i = 0; i < d.length; i++) {
            row[d[i].label] = d[i].data[k];
            total[d[i].label]=(total[d[i].label]?total[d[i].label]:0)+d[i].data[k];
        }
        data.push(row);
    }
    data.push(total);
    result.header = headers;
    result.data = data;

    return result;
}

export default APIService;