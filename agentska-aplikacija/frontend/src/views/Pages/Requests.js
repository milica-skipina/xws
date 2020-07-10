import React from 'react';
import axios from 'axios';
import { RoleAwareComponent } from 'react-router-role-authorization';
import { NotificationContainer, NotificationManager } from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
import { Redirect } from 'react-router-dom';
import BootstrapTable from 'react-bootstrap-table-next';

import '../../scss/vendors/custom.css';
import {
  Button, Modal,
  Card,
  CardBody, 
  Table, Label, Input, FormGroup, Form
} from 'reactstrap';


//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

const columns = [
  { dataField: 'customerName', text: 'Customer' },
  { dataField: 'agentName', text: 'Agent' },  
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
      openModal: false,
      agentForBundle: [],
      showModal: false,
      text: "",
      mileage: "",
      tempId: "",
      today: "",

    };


    let arr = [];
    arr.push(localStorage.getItem('role'));
    console.log("KONS", arr);
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_SELLER'];

    this.fillRequests = this.fillRequests.bind(this);
    this.toggle = this.toggle.bind(this);
    this.transformResponse = this.transformResponse.bind(this);
    this.getDateString = this.getDateString.bind(this);
  }

  componentDidMount() {
    const today = Date.now();
    //let temp1 = new Intl.DateTimeFormat('en-US', {year: 'numeric', month: '2-digit',day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit'}).format(today);
    this.setState({today:today});
    this.fillRequests();
  }

  textValidation(c){
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ text: c });
    return true;
  }

mileageValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ mileage: c });
    let sun = c;
    if (sun === '') {
      this.setState({ textMileageValidation: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ textMileageValidation: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ textMileageValidation: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
  else {
      this.setState({ textMileageValidation: "" });
      return true;
    }
  }


  fillRequests = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);

    axios({
        method: 'get',
        url: url + 'request',
        headers: { "Authorization": AuthStr } ,            
      }).then((response) => {
        if (response.status === 200) {
          this.transformResponse(response.data)
          //this.setState({requests: response.data})
        } else {
          NotificationManager.info(response.data.entity, '', 3000);  
        }       
      }, (error) => {
        console.log(error);
      });
  }

  createReport = (id) => {
    this.setState({showModal:true, tempId:id});
  }

  sendReport= event => {
    event.preventDefault();
    let ok = true;
    ok = this.mileageValidation(this.state.mileage);
    if(ok){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    let data = {
      'text': this.state.text,
      'mileage': this.state.mileage,
      'endDate': this.state.endDate,
    }
     axios({
      method: 'post',
      url: url + 'report/' +this.state.tempId,
      data: data,
      headers: { "Authorization": AuthStr },
    }).then((response) => {
      console.log(response.data.status)
      if (response.status === 200){
        this.setState({showModal:false, mileage:"", text:"", tempId:"" })
        NotificationManager.success("Successfully created!", '', 3000);  
        }   
    }, (error) => {
      console.log(error);
    });
    }
  }

  modifyRequest = (requestId, flag) => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);

    axios({
      method: 'put',
      url: url + 'request/' + requestId + '/' + flag,
      headers: { "Authorization": AuthStr },
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

  compareDates(carDate){
    let temp = this.state.today;
    console.log("today")
    console.log(this.getMyDateString(temp));
    console.log("RAZMAK")
    console.log(carDate)
    console.log("carDateee")
    return this.getMyDateString(temp) > carDate;
  }

  transformResponse = transform => {
    for (let i = 0; i < transform.length; i++) {
      for (let j=0; j < transform[i].requests.length; j++) {
        let start = this.getDateString(transform[i].requests[j].startDate);
        let end = this.getDateString(transform[i].requests[j].endDate);
        transform[i].requests[j].startDate = start;
        transform[i].requests[j].endDate = end;
      }
      
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

  getMyDateString(miliseconds) {
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
            {(row.requests.map((request, index) =>
              <tr key={index} className="crow">
                <td>
                    <div>
                        <img width="100" height="50" src={request.cars[0].images[0].imageUrl}/>
                    </div>
                </td>
                <td className="crow">{request.startDate}</td>
                <td className="crow">{request.endDate}</td>
                <td className="crow">{request.cars[0].make}</td>
                <td className="crow">{request.cars[0].model}</td>
                <td className="crow">{request.cars[0].fuel}</td>
                
                {request.state === "PAID" && this.compareDates(request.endDate) && <Button color="primary" size="md" onClick={event => this.createReport(request.cars[0].id)}> {}
                Add report </Button>}
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
    //let name = localStorage.getItem('role') === "ROLE_SELLER" ? "Customer" : "Agent";
    let ret = (<div className="animated fadeIn">
        
      <Card>
        <CardBody>
          <BootstrapTable
            keyField='id'
            data={this.state.requests}
            columns={columns}
            expandRow={this.expandRow}
          />
        </CardBody>
      </Card>
      <Modal modalClassName="modal-register" isOpen={this.state.showModal}>
        <div className="modal-header no-border-header text-center">
                  <button
                    aria-label="Close"
                    className="close"
                    data-dismiss="modal"
                    type="button"
                    onClick={event => this.setState({showModal:false, mileage:"", text:""} )}
                  >
                  <span aria-hidden={true}>x</span>
                  </button>
                  </div>
                  <div className="modal-body">     
            <Form onSubmit={this.sendReport}>
                  <FormGroup>
                  <Label>Mileage:</Label>
                  <Input type="number" className="form-control" value={this.state.mileage}  onChange={(event) => this.mileageValidation(event.target.value)}/>
                  <p style={{color:'red'}} > {this.state.textMileageValidation} </p>
                  </FormGroup>
                  <FormGroup>
                  <Label>Description:</Label>
                  <textarea cols="30" className="form-control" value={this.state.text}   onChange={(event) => this.textValidation(event.target.value)} /> 
                  </FormGroup>
                  <Button block className="btn-round" color="info">
                    Confirm
                  </Button>
              </Form>         
            </div>
        </Modal>
      <NotificationContainer />
    </div>);

    return this.rolesMatched() ? ret : <Redirect to="/oglasi" />;
  }
}

export default Request;
