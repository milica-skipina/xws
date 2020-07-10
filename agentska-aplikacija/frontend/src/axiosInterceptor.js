import axios from 'axios';
/*
const axiosInterceptor =  axios.interceptors.request.use((config) => {

    const https = require("https");
    const fs = require("fs");
    config.httpsAgent = new https.Agent({
      rejectUnauthorized: false, 
      ca: fs.readFileSync("./cert/tls-ca-chain.pem"),        
      cert: fs.readFileSync("./cert/client.crt"),
      key: fs.readFileSync("./cert/client.key"),
    });
    return config;
    }, (error) => {
      return Promise.reject(error);
    });
*/
export default axiosInterceptor;