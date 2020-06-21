/*!

=========================================================
* Paper Dashboard React - v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Licensed under MIT (https://github.com/creativetimofficial/paper-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
// react plugin used to create charts
import { Line, Pie } from "react-chartjs-2";
// reactstrap components
import {
  Row,
  Col
} from "reactstrap";
import { RoleAwareComponent } from 'react-router-role-authorization';
import axios from 'axios';
import {Redirect} from 'react-router-dom';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';


class Dashboard extends RoleAwareComponent {
  constructor(props){
    super(props)
    this.state = {
      users : []
    };

    let arr = [];
    arr.push(localStorage.getItem('role'));    
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_ADMIN'];
  }
    
  
  

  loadUsers = () =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
      axios({
          method: 'get',
          url: url + 'authpoint/user',
          headers: { "Authorization": AuthStr } ,  
      }).then((response) => {
          console.log("assadsabdas");
          console.log(response.data);
          this.setState({users:response.data});
          if (response.status === 200) {
          } else {
              //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
          }

      }, (error) => {
          //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

      });
  };

  componentDidMount() {
    console.log("assadsabdas");
    this.loadUsers();
  }

  handleDelete = (e,userId) =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'delete',
      url: url + 'authpoint/user/'+userId ,
      headers: { "Authorization": AuthStr } ,  

    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
          this.loadUsers();
      } else {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });
  };

  handleActivation = (e,userId) =>{
    console.log(userId);
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'authpoint/dash/verify/'+userId ,
      headers: { "Authorization": AuthStr } ,  
    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
      } else {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });
  };

  handleBlock = (e,userId) =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'authpoint/user/deactivate/'+userId ,
      headers: { "Authorization": AuthStr } ,  
    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
      } else {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });

  };

  render() {

    let users = this.state.users.map(user => (
        <tr key={user.id}>
          <td>{user.username}</td>
          <td>{user.email}</td>
          <td><button  onClick={(e) => this.handleDelete(e,user.id)} className="btn btn-primary">Delete</button></td>
          {!user.activated && <td><button  onClick={(e) => this.handleActivation(e,user.id)} className="btn btn-primary">Activate</button></td>}
          <td><button onClick={(e) => this.handleBlock(e,user.id)} className="btn btn-primary">Block</button></td>
        </tr>));

    console.log(this.state.users);

    
        let ret = (<div className="content">
          <Row>
            <Col lg="3" md="6" sm="6">
              <table className="table">
                <thead>
                <tr>
                  <th scope="col">Username</th>
                  <th scope="col">Email</th>
                </tr>
                </thead>
                <tbody>
                {users}
                </tbody>
              </table>
            </Col>
          </Row>
        </div>)
      
      return this.rolesMatched() ? ret : <Redirect to="/ads" />;
  }
}

export default Dashboard;
