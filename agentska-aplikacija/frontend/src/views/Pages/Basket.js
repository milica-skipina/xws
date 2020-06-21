import React from 'react';
import axios from 'axios';
import { RoleAwareComponent } from 'react-router-role-authorization';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
import {Redirect} from 'react-router-dom';
import {
  Button, Modal, ModalBody, ModalFooter, ModalHeader,
  Card,
  CardBody,
  Table,
} from 'reactstrap';

//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

class Basket extends RoleAwareComponent {

  constructor(props) {
    super(props);

    this.state = {          
      ads: [],      
      hideEdit: true,
      checkedAds: [],        // ids of selected cars for rent
      bundle: false,
      openModal: false ,
      agentForBundle: [],
      readyForRequest: true
    };

    
    let arr = [];
    arr.push(localStorage.getItem('role'));
    console.log("KONS",arr);
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_CUSTOMER'];

    this.fillBasket = this.fillBasket.bind(this);
    this.requestSelection = this.requestSelection.bind(this);
    this.askForRent = this.askForRent.bind(this);
    this.createRequests = this.createRequests.bind(this);
    this.toggle = this.toggle.bind(this);
    this.handleSelectedCars = this.handleSelectedCars.bind(this);
    this.removeFromBasket = this.removeFromBasket.bind(this);
    this.getDateString = this.getDateString.bind(this);
    this.addToBundle = this.addToBundle.bind(this);
  }


  componentDidMount() {
    this.fillBasket();
  }

  fillBasket = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    let array = JSON.parse(sessionStorage.getItem('basket')) || [] ;
    console.log(array)
    axios({
        method: 'post',
        url: url + 'advertisement/filter',
        headers: { "Authorization": AuthStr } ,   
        data: array    
      }).then((response) => {
        if (response.status === 200) {
          this.setState({ ads: response.data });
        }else {
          this.setState({ ads: [] });
          //NotificationManager.info("Your basket is empty!", '', 3000);
        }
      }, (error) => {
        console.log(error);
      });
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
  
  requestSelection = (key, name) => {
    let found = false;
    for (let i=0; i < this.state.checkedAds.length; i++) {          
        if (key === this.state.checkedAds[i].split("+")[0]) {
              found = true;
              break;
        }
    }

    if (found) {      // obrisi iz niza
      let helperArray = [];
      for (let i=0; i < this.state.checkedAds.length; i++) {
          if (key !== this.state.checkedAds[i].split("+")[0]) {
                helperArray.push(this.state.checkedAds[i]);
          }
      }
      this.state.checkedAds = helperArray;
    }else {
      let str = key.concat("+").concat(name);
      this.state.checkedAds.push(str);
     // this.setState({checkedAds: [...this.state.checkedAds, str]})
    }

    if (this.state.checkedAds.length === 0) {
      this.setState({readyForRequest: true})    // nek bude dugme disabled
    }else {
      this.setState({readyForRequest: false})
    }
    
      
}

  toggle = () => {
    this.setState({
      openModal: !this.state.openModal,
    });
  }

  askForRent = () => {      // rentiraaaj ove noci poveruj da je ljubav dosla samaaaa
    //let flag = this.state.bundle ;
    let data = this.state.checkedAds;
    data.push(localStorage.getItem('start'))  // posalji i datume pretrage
    data.push(localStorage.getItem('end'))
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
        method: 'post',
        url: url + 'request',
        headers: { "Authorization": AuthStr } ,   
        data: data        // niz oglasa u formi id+entrepreneurName+true/false    
      }).then((response) => {
        if (response.status === 201) {
            for (let i=0 ; i <  this.state.checkedAds.length ; i++) {
              this.removeFromBasket(this.state.checkedAds[i].split("+")[0]);
            }
            this.setState({checkedAds: [], agentForBundle: [], readyForRequest: true}) ;
            NotificationManager.success("Order is sent!", '', 3000);
        }         
         
      }, (error) => {
        console.log(error);
      });
    
  }

  createRequests = flag => {
      //this.setState({bundle: flag})
      this.state.bundle = flag;
      this.state.agentForBundle = []
      this.askForRent();        // da li se bundle setovao ?
      this.toggle();
  }

  removeFromBasket =  (id) => {
    let array = JSON.parse(sessionStorage.getItem('basket')) || [] ;
    let tempArray = []
    for (let i = 0; i < array.length ; i++) {
      if (array[i] !== id) {
          tempArray.push(array[i]);
      }
    }    
    sessionStorage.setItem('basket', JSON.stringify(tempArray));
    this.state.ads = [];
    this.fillBasket()
  }

  handleSelectedCars = () => {
      let namesOnly = [];
      let hlp = false;
      for (let j=0; j < this.state.checkedAds.length ; j++) {
          if (namesOnly.includes(this.state.checkedAds[j].split("+")[1]) && 
              !this.state.agentForBundle.includes(this.state.checkedAds[j].split("+")[1])) {
              this.setState({agentForBundle: [...this.state.agentForBundle, this.state.checkedAds[j].split("+")[1]]})
              hlp = true;
              //break;
            } else {
                namesOnly.push(this.state.checkedAds[j].split("+")[1]);
          }
       
      }

      if(hlp) {
        this.setState({openModal: true}) 
      }else {
        this.askForRent();  
      }
  
  }

  addToBundle = (agent, event) => {
    let temp = this.state.checkedAds;
    let isChecked = event.target.checked
    for (let i=0; i < temp.length; i++) {
      if (temp[i].split("+")[1] === agent) {
        if (isChecked) {
          temp[i] = temp[i].concat("+").concat("true");
        }else {   // uncekiran je agent
          temp[i] = temp[i].substring(0, temp[i].length - 5);
        }
        
      }
    }
    this.setState({checkedAds: temp});
  }

  render() {

    let ret = (<div className="animated fadeIn">
        <Card>
            <CardBody>
        <Table responsive striped>
                  <thead>
                  <tr>
                    <th>#</th>
                    <th color="secondary">Agent</th>
                    <th className="crow">Brand</th>
                    <th className="crow"> Model</th>
                    <th className="crow">Start date</th>
                    <th className="crow">End date</th>
                    <th className="crow">City</th>
                    <th className="crow">Rating</th>
                    <th className="crow">Price for selected dates</th>
                  </tr>
                  </thead>
                  <tbody>
                  {(this.state.ads.map((ad, index) =>
                  <tr key={ad.advertisementId} keyad={ad.advertisementId} className="crow">
                    <td className="crow"><input type="checkbox"                 
                        onChange={event => this.requestSelection(event.target.parentNode.parentNode.getAttribute("keyad"),
                        ad.entrepreneur)}
                       
                    /></td>
                    <td className="crow">{ad.entrepreneur}</td>
                    <td className="crow">{ad.make}</td>
                    <td className="crow">{ad.model}</td>
                    <td className="crow">{this.getDateString(ad.startDate)}</td>
                    <td className="crow">{this.getDateString(ad.endDate)}</td>
                    <td className="crow">{ad.city}</td>
                    <td className="crow">{ad.rating}</td>
                    <td className="crow">{ad.price}</td>                                      
                    <td className="crow"><Button block color="info" onClick={event => this.removeFromBasket(ad.advertisementId)}>Remove</Button>
                    </td>
                    </tr>
                    ))}
                  </tbody>
                </Table>
                <Button block color="info" onClick={this.handleSelectedCars} disabled={this.state.readyForRequest}>Create request</Button>
                </CardBody>
         </Card>

         <Modal isOpen={this.state.openModal} className='modal-primary'>
                  <ModalHeader></ModalHeader>
                  <ModalBody>
                    You selected more than one car from the same agent. Do you want to create bundle for your 
                    requests?
                    {(this.state.agentForBundle.map((agent, index) =>
                     <label className="checkbox-inline">
                      <input type="checkbox" onChange={event => this.addToBundle(agent, event)} />
                      {agent}
                   </label>                  
                    ))}
                  </ModalBody>
                  <ModalFooter>
                    <Button color="primary" onClick={event => this.createRequests(true)}>Create bundle</Button>{' '}
                    <Button color="secondary" onClick={event => this.createRequests(false)}>Cancel</Button>
                  </ModalFooter>
                </Modal>
                <NotificationContainer/>
    </div>);

    return this.rolesMatched() ? ret : <Redirect to="/oglasi" />;
  }
}

export default Basket;
