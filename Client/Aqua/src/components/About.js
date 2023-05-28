import React from 'react';
import DynamicForm from '../service/DynamicForm';
import APIService from "../service/APIService"

import './Home.css';


function About(props) {
  const registerUser = (e) => {
    alert(JSON.stringify(e));
  }

  return (
    <div className="content">
       <h2>{APIService.getLocalStorage('menuName')}</h2>
      <DynamicForm type="sample" onSubmit={(e) => registerUser(e)} />
      
    </div>


  )

}
export default About;