var axios = require("axios");

export const jwtToken = APIService.getLocalStorage("token");

axios.interceptors.request.use(
  function(config) {
    if (jwtToken) {
      config.headers["authorization"] =  jwtToken;
    }
    return config;
  },
  function(err) {
    return Promise.reject(err);
  }
);