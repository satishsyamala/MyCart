import APISevice from "./APIService";
const APICalls = {
    loginValidation
};
async function loginValidation(userName, password) {
    
        return  APISevice.getRequest('/user/login?username='+userName+'&password='+password, null);
   
}

export default APICalls;