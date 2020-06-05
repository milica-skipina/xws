import React, { Component } from 'react';
import axios from 'axios';
import { RoleAwareComponent } from 'react-router-role-authorization';
import Checkbox from '@material-ui/core/Checkbox';
import {NotificationContainer, NotificationManager} from 'react-notifications';
//import "../../../node_modules/react-notifications/lib/notifications.css"
//import "../../../node_modules/react-notifications/lib/Notifications.js"
import {Redirect} from 'react-router-dom';
import BootstrapTable from 'react-bootstrap-table-next';

//import '../../scss/vendors/custom.css';
import {
  Button, Modal, ModalBody, ModalFooter, ModalHeader,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Col,
  Form,
  FormGroup,
  FormText,
  Input,
  Label,
  Row,
  Nav,
  NavItem,
  NavLink,
  TabContent,
  TabPane,
  Table,
  Pagination,
  PaginationItem,
  PaginationLink
} from 'reactstrap';

//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

const columns = [
  { dataField: 'userId', text: 'Customer' },
  { dataField: 'startDate', text: 'Start' },
  { dataField: 'endDate', text: 'End' },
  { dataField: 'state', text: 'Status' },
]

class Request extends RoleAwareComponent {

  constructor(props) {
    super(props);
    this.modifyRequest = this.modifyRequest.bind(this);
    this.state = {          
      requests: [],      
      hideEdit: true,
      checkedAds: [],        // ids of selected cars for rent
      bundle: false,
      openModal: false ,
      agentForBundle: [] 
      
    };

    
    let arr = new Array();
    arr.push(localStorage.getItem('role'));
    console.log("KONS",arr);
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_SELLER'];

    this.fillRequests = this.fillRequests.bind(this);
    this.toggle = this.toggle.bind(this);    
    this.transformResponse = this.transformResponse.bind(this);
    this.getDateString = this.getDateString.bind(this);   
  }

  componentDidMount() {
    this.fillRequests();
  }

  fillRequests = () => {
    let data = sessionStorage.getItem('basket');
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);    
    
    axios({
        method: 'get',
        url: url + 'orders/request',
        headers: { "Authorization": AuthStr } ,            
      }).then((response) => {
        if (response.data.status === 200) {
            console.log(response.data.entity)
          let transform = JSON.parse(response.data.entity)
          this.transformResponse(transform)  
        }               
         
      }, (error) => {
        console.log(error);
      });
  }

  modifyRequest = (requestId, flag) => {
    let data = sessionStorage.getItem('basket');
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);    
    
    axios({
        method: 'put',
        url: url + 'orders/request/' + requestId + '/' + flag,
        headers: { "Authorization": AuthStr } ,            
      }).then((response) => {
        if (response.data.status === 200)
          this.fillRequests(); 
        else {
          NotificationManager.error(response.data.entity, '', 3000);   
        }
      }, (error) => {
        console.log(error);
      });
  }
  
  transformResponse = transform => {
    for (let i = 0; i < transform.length; i++) {
      let start = this.getDateString(transform[i].startDate);
      let end = this.getDateString(transform[i].endDate);
      transform[i].startDate = start;
      transform[i].endDate = end;
    }

    this.setState({ requests: transform });
  }

  getDateString(miliseconds) {
    let date = new Date(miliseconds);
    let month = date.getMonth() + 1;
    let ret = date.getDate() + '.' + month + '.' + date.getFullYear() + ', ';
    let hours = date.getHours();
    let minutes = date.getMinutes();
    if (hours < 10) {
      hours = '0' + hours;
    }
    if (minutes < 10) {
      minutes = '0' + minutes;
    }
    ret = ret + hours + ':' + minutes;
    return ret;
  }

  toggle = () => {
    this.setState({
      openModal: !this.state.openModal,
    });
  }

 
  expandRow = {
    renderer: row => (
      <div>
        <Table responsive>                  
                  <tbody>
                  {(row.cars.map((car, index) =>
                  <tr key={index} className="crow">               
                    <td className="crow">{car.make}</td>
                    <td className="crow">{car.model}</td>  
                    <td className="crow">{car.fuel}</td>                  
                    <td className="crow">{car.price}</td>                         
                    </tr>
                    
                    ))}
                    
                  </tbody>
                </Table>
                {row.state === "PENDING" && <Button color="primary" onClick={event => this.modifyRequest(row.id, true)}>Accept</Button>} {'   '}  
                {row.state === "PENDING" && <Button color="primary" onClick={event => this.modifyRequest(row.id, false)}>Decline</Button>} 
                  
      </div>
    ),
    showExpandColumn: true
  };


  render() {
    let name = localStorage.getItem('role') === "ROLE_SELLER" ? "Customer" : "Agent" ;
    let ret = (<div className="animated fadeIn content">
        <Card>
            <CardBody>
            <BootstrapTable
                keyField='id'
                data={ this.state.requests }
                columns={ columns }
                expandRow={ this.expandRow }
              />
               
            </CardBody>
         </Card>
           
                
           
         <NotificationContainer/>
    </div>);

    return ret; {/*this.rolesMatched() ? ret : <Redirect to="/oglasi" />;*/}
  }
}

export default Request;
