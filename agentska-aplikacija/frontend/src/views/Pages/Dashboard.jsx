import React from "react";
import axios from 'axios';
import { RoleAwareComponent } from 'react-router-role-authorization';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import {Redirect} from 'react-router-dom';

import {
  Row,
  Col, Card, CardBody, CardHeader
} from "reactstrap";

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';


class Dashboard extends RoleAwareComponent{
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
      url: url + 'user',
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      console.log("USERS",response.data);
      this.setState({users:response.data});
      if (response.status === 200) {
      } else {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });
  };

  handleDelete = (e,userId) =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'delete',
      url: url + 'user/'+userId ,
      headers: { "Authorization": AuthStr } ,

    }).then((response) => {
      //console.log(response.data);
      if (response.status === 200) {
        NotificationManager.info("User deleted");
        this.loadUsers();
      } else {
        NotificationManager.error("Failed to deleted user");
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
      url: url + 'user/activate/'+userId ,
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
        NotificationManager.info("Activation mail sent");
        this.loadUsers();
      } else {
        NotificationManager.error("Failed to send activation mail");
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
      url: url + 'user/blockUser/'+userId ,
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
        NotificationManager.info("User blocked");
        this.loadUsers();
      } else {
        NotificationManager.error("Failed to block user");
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });

  };

  handleunBlock = (e,userId) =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'user/unblockUser/'+userId ,
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
        NotificationManager.info("User unblocked");
        this.loadUsers();
      } else {
        NotificationManager.error("Failed to unblock user");
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });

  };

  changePermission = (e,temp, username) =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'put',
      url: url + 'user/'+  temp + '/' + username,
      headers: { "Authorization": AuthStr } ,

    }).then((response) => {
      console.log(response.data);
      if (response.status === 200) {
          this.loadUsers();
      } else {
      }

    }, (error) => {

    });
  };

  componentDidMount() {
    this.loadUsers();
  }

  render() {

    let users = this.state.users.map(user => (
      (user.user.deleted === false) && <tr key={user.userId}>
        <td>{user.user.username}</td>
        <td>{user.user.email}</td>
         {user.endUserDTO !== null && <td> {user.canReserve} </td>}
          {user.endUserDTO !== null && <td> {user.canComment} </td>}
          {user.endUserDTO !== null && <td> {user.numberRefusedComments} </td>}
          {user.endUserDTO !== null && <td> {user.numberCanceledRequest} </td>}
          {user.endUserDTO !== null && user.canComment && <td> <button className="btn btn-primary" onClick={(e) => this.changePermission(e,"blockComment",user.user.username)} className="btn btn-primary">Block commenting</button></td>}
          {user.endUserDTO !== null && user.canReserve && <td> <button className="btn btn-primary" onClick={(e) => this.changePermission(e,"blockReserve",user.user.username)} className="btn btn-primary">Block reserving</button></td>}
          {user.endUserDTO !== null && !user.canComment && <td> <button className="btn btn-primary" onClick={(e) => this.changePermission(e,"alloweComment",user.user.username)} className="btn btn-primary">Allowe commenting</button></td>}
          {user.endUserDTO !== null && !user.canReserve && <td> <button className="btn btn-primary" onClick={(e) => this.changePermission(e,"alloweReserve",user.user.username)} className="btn btn-primary">Allowe reserving</button> </td>}

        { !user.user.deleted && <td><button  onClick={(e) => this.handleDelete(e,user.id)} className="btn btn-primary">Delete</button></td>}
        {!user.activated && <td><button  onClick={(e) => this.handleActivation(e,user.id)} className="btn btn-primary">Activate</button></td>}
        {!user.user.blocked && <td><button onClick={(e) => this.handleBlock(e,user.id)} className="btn btn-primary">Block</button></td>}
        {user.user.blocked && <td><button onClick={(e) => this.handleunBlock(e,user.id)} className="btn btn-primary">Unblock</button></td>}
      </tr>));

    console.log(this.state.users);


    let ret = (<div className="animated fadeIn content">

          <Card>
            <CardHeader>
              <i className="fa fa-align-justify"></i> Users
            </CardHeader>
            <CardBody>
          <table className="table">
            <thead>
            <tr>
              <th scope="col">Username</th>
              <th scope="col">Email</th>
              <th scope="col"></th>
              <th scope="col"></th>
              <th scope="col">Refused comments</th>
              <th scope="col">Canceled requests</th>
            </tr>
            </thead>
            <tbody>
            {users}
            </tbody>
          </table>
            </CardBody>
          </Card>
    <NotificationContainer/>
    </div>);

    return this.rolesMatched() ? ret : <Redirect to="/oglasi" />;
  }
}export default Dashboard;
