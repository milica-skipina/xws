mport React, { Component } from 'react';
import { MDBCard, MDBCardTitle, MDBBtn, MDBCardGroup, MDBCardImage, MDBCardText, MDBCardBody } from "mdbreact";
import {
  Row, Col, Card, CardHeader, CardBody, FormGroup, Label, Modal, Form, Input, InputGroup, InputGroupAddon,
  InputGroupText, Button, Collapse, FormText, Dropdown, DropdownToggle, DropdownMenu, DropdownItem, ListGroup, ListGroupItem
} from "reactstrap";
import axios from 'axios';
import '../../scss/vendors/custom.css'
import { NotificationContainer, NotificationManager } from 'react-notifications';

import ReactDOM from 'react-dom';
import Slider, { Range, createSliderWithTooltip } from 'rc-slider';
// We can just import Slider or Range to reduce bundle size
// import Slider from 'rc-slider/lib/Slider';
// import Range from 'rc-slider/lib/Range';
import 'rc-slider/assets/index.css';

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';
const RangeWithTooltip = createSliderWithTooltip(Range);
const SliderWithTooltip = createSliderWithTooltip(Slider);

class Oglasi extends Component {

  constructor(props) {
    super(props);

    this.state = {
      oglasi: [],
      accordion: [false],
      city: '',
      startDate: '',
      endDate: '',
      startTime: '',
      endTime: '',
      codebook: [],
      adsSearch: [],
      adsFiltered: [],
      startDateError: '',
      endDateError: '',
      startTimeError: '',
      endTimeError: '',
      cityError: '',
      hideSearch: false,
      hideAll: false,
      dropdownOpen: false,
      codebook: [[], [], [], [], []],
      selectedCodes: [[], [], [], [], []],
      header: ["Car brands", "Car models", "Fuel types", "Gearbox types", "Car type"],
      hideBasket: true,
      selectedPrice: [0, 0],
      maxPrice: 0,
      maxMileage: 0,
      selectedMileage: 0,
      maxKids: 7,
      selectedKids: 0,
      unlimitedMileage: false,
      maxMileageLimit: 0,
      mileageLimit: 0,
      damageWaiver: false,
      isOpenSort: false,
      selectedSort: "Price",
      manualStartDate: "",
      manualEndDate: "",
      startDateDisable: "",
      endDateDisable: "",
      showModal: false,
      isAgent: false,
      manualId: 0 // id auta
    };
    this.saznajVise = this.saznajVise.bind(this);
    this.toggleAccordion = this.toggleAccordion.bind(this);
    this.startDateValidation = this.startDateValidation.bind(this);
    this.endDateValidation = this.endDateValidation.bind(this);
    this.startDateValidationModal = this.startDateValidationModal.bind(this);
    this.endDateValidationModal = this.endDateValidationModal.bind(this);
    this.startTimeValidation = this.startTimeValidation.bind(this);
    this.endTimeValidation = this.endTimeValidation.bind(this);
    this.cityValidation = this.cityValidation.bind(this);
    this.search = this.search.bind(this);
    this.apply = this.apply.bind(this);
    this.getDateString = this.getDateString.bind(this);
    this.getCodebook = this.getCodebook.bind(this);
    this.toggleCheckbox = this.toggleCheckbox.bind(this);
    this.renderCheckboxes = this.renderCheckboxes.bind(this);
    this.clearAll = this.clearAll.bind(this);
    this.manualRequest = this.manualRequest.bind(this);

  }

  componentDidMount() {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'advertisement',
      headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status !== 404)
        this.setState({ oglasi: response.data });
    }, (error) => {
      console.log(error);
    });
    this.getCodebook();
    axios({
      method: 'get',
      url: url + 'advertisement/isAgent',
      headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status !== 404)
        this.setState({ isAgent: response.data });
    }, (error) => {
      console.log(error);
    });
  }


  manualRequest = event => {
    event.preventDefault();
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    let ok = true;
    ok = this.startDateValidationModal;
    ok = this.endDateValidationModal && ok;
    let start = this.state.manualStartDate;
    let end = this.state.manualEndDate;
    let carId = this.state.manualId;
    if(ok){
      console.log("ok");
      axios({
        method: 'get',
        url: url + 'request/' + carId +'/' + start + '/' + end ,
        headers: { "Authorization": AuthStr } ,       
      }).then((response) => {
        if (response.data) {
          NotificationManager.success("Manual reservation successful!", '', 3000);
        } else {
          NotificationManager.error("These days are already booked. Try to change dates.", '', 3000);
        }
        
      }, (error) => {
        console.log(error);
      });
      this.setState({startDateDisable:"", endDateDisable:"", manualStartDate:"", manualEndDate:"",
                     showModal: false});
    }
    else{
      console.log("false")
    }
  }

  openModal(oglas) {
    let date = new Date(oglas.startDate);
    let month = date.getMonth() + 1;
    let pom = date.getDate();
    if(month<10){
      month = '0'+month;
    }
    if(date.getDate() < 10){
      pom = '0'+date.getDate();
    }
    let ret = date.getFullYear() + '-' + month + '-' + pom;
    this.setState({manualStartDate:ret});
    this.setState({startDateDisable:ret});
    date = new Date(oglas.endDate);
    month = date.getMonth() + 1;
    pom = date.getDate();
    if(month<10){
      month = '0'+month;
    }
    if(date.getDate() < 10){
      pom = '0'+date.getDate();
    }
    ret = date.getFullYear() + '-' + month + '-' + pom;
    this.setState({manualEndDate:ret, manualId: oglas.carAd.id});
    this.setState({endDateDisable:ret});
    this.setState({showModal:true});
  }

  startDateValidationModal(ddatum) {
    this.setState({ manualStartDate: ddatum })
    let godina;
    let mjesec;
    let dan;
    if (ddatum !== "" && ddatum !== undefined) {
      godina = ddatum[0] + ddatum[1] + ddatum[2] + ddatum[3]

      mjesec = ddatum[5] + ddatum[6]

      dan = ddatum[8] + ddatum[9]

      let newDate = new Date();
      let date = parseInt(newDate.getDate())
      let month = parseInt(newDate.getMonth() + 1);
      let year = parseInt(newDate.getFullYear());
      godina = parseInt(godina)
      mjesec = parseInt(mjesec)
      dan = parseInt(dan)
if (godina < year) {
        this.setState({ textStartManualValidation: "Godina je prosla" })
        return false;
      }
      else if (mjesec < month && godina <= year) {
        this.setState({ textStartManualValidation: "Mjesec je prosao" })
        return false;
      }
      else if (dan < date && mjesec <= month && godina <= year) {
        this.setState({ textStartManualValidation: "Dan je prosao" })
        return false;
      }
        else if(ddatum < this.state.startDateDisable || ddatum > this.state.endDateDisable){
          this.setState({ textStartManualValidation: "Nije u odgovarajucem periodu" })
        }
        else if(ddatum > this.state.manualEndDate){
          this.setState({ textStartManualValidation: "Mora biti manji od krajnjeg!" })
        }
        else if(ddatum < this.state.startDateDisable){
          this.setState({ textStartManualValidation: "Nije u odgovarajucem periodu!" })
        }
      else {
        this.setState({ textStartManualValidation: "" })
        return true;
      }
    }
  };

  endDateValidationModal(ddatum) {
    this.setState({ manualEndDate: ddatum })
    let godina;
    let mjesec;
    let dan;
    if (ddatum !== "" && ddatum !== undefined) {
      godina = ddatum[0] + ddatum[1] + ddatum[2] + ddatum[3]

      mjesec = ddatum[5] + ddatum[6]

      dan = ddatum[8] + ddatum[9]

      let newDate = new Date();
      let date = parseInt(newDate.getDate())
      let month = parseInt(newDate.getMonth() + 1);
      let year = parseInt(newDate.getFullYear());
      godina = parseInt(godina)
      mjesec = parseInt(mjesec)
      dan = parseInt(dan)

      if (godina < year) {
        this.setState({ textEndManualValidation: "Godina je prosla" })
        return false;
      }
      else if (mjesec < month && godina <= year) {
        this.setState({ textEndManualValidation: "Mjesec je prosao" })
        return false;
      }
      else if (dan < date && mjesec <= month && godina <= year) {
        this.setState({ textEndManualValidation: "Dan je prosao" })
        return false;
      }
        else if(ddatum < this.state.startDateDisable || ddatum > this.state.endDateDisable){
          this.setState({ textEndManualValidation: "Nije u odgovarajucem periodu" })
        }
        else if(ddatum < this.state.manualStartDate){
          this.setState({ textEndManualValidation: "Mora biti veci od pocetnog!" })
        }
        else if(ddatum > this.state.endDateDisable){
          this.setState({ textEndManualValidation: "Nije u odgovarajucem periodu!" })
        }
      else {
        this.setState({ textEndManualValidation: "" })
        return true;
      }
    }
  };


  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen,
    });
  }

  toggle2() {
    this.setState({
      isOpenSort: !this.state.isOpenSort,
    });
  }

  toggleAccordion(tab) {

    const prevState = this.state.accordion;
    const state = prevState.map((x, index) => tab === index ? !x : false);

    this.setState({
      accordion: state,
    });
  }

  saznajVise(id) {
    this.props.history.push('oglas/' + id);
  }

  startDateValidation(date) {
    if (date === '') {
      this.setState({ startDateError: "This field can't be empty" })
      return false;
    }
    this.setState({ startDate: date });
    date = new Date(date);
    let today = new Date();
    if (this.state.startTime !== '') {
      let t = this.state.startTime.split(':');
      date.setHours(t[0], t[1], 0, 0);
    }
    else {
      date.setHours(0, 0, 0, 0);
      today.setHours(0, 0, 0, 0);
    }
    date.setDate(date.getDate() - 2);
    if (date >= today) {
      this.setState({ startDateError: '' });
      return true;
    }
    else {
      this.setState({ startDateError: "You can't pick up car in less than 48h from now" });
      return false;
    }
  }

  endDateValidation(date) {
    this.setState({ endDate: date });
    if (date === '') {
      this.setState({ endDateError: "This field can't be empty" });
      return false;
    }
    date = new Date(date);
    date.setHours(0, 0, 0, 0);
    if (this.state.startDate !== '' && this.state.startTime !== '' && this.state.endTime !== '') {
      let start = new Date(this.state.startDate);
      let startT = this.state.startTime.split(':');
      start.setHours(startT[0], startT[1], 0, 0);
      let endT = this.state.endTime.split(':');
      date.setHours(endT[0], endT[1], 0, 0);
      if (date < start) {
        this.setState({ endDateError: "Drop off date can't be before pick up date" });
        return false;
      }
      else {
        this.setState({ endDateError: "" });
        return true;
      }
    }
    else {
      this.setState({ endDateError: "" });
      return true;
    }
  }

  startTimeValidation(time) {
    this.setState({ startTime: time });
    if (time === '') {
      this.setState({ startTimeError: "This field can't be empty" });
      return false;
    }
    else {
      this.setState({ startTimeError: "" });
      return true;
    }
  }

  endTimeValidation(time) {
    this.setState({ endTime: time });
    if (time === '') {
      this.setState({ endTimeError: "This field can't be empty" });
      return false;
    }
    else {
      this.setState({ endTimeError: "" });
      return true;
    }
  }

  cityValidation(city) {
    city = city.replace(/</g, '');
    city = city.replace(/>/g, '');
    city = city.replace(/script/g, '');
    city = city.replace(/alert/g, '');

    this.setState({ city: city });
    if (city === '') {
      this.setState({ cityError: "This field can't be empty" });
      return false;
    }
    else {
      this.setState({ cityError: '' });
      return true;
    }
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

  getDateTime(date, time) {
    let newDate = new Date(date);
    let t = time.split(':');
    newDate.setHours(t[0], t[1], 0, 0);
    return newDate;
  }

  getCodebook() {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'codebook',
      headers: { "Authorization": AuthStr },
    }).then((response) => {
      if (response.status === 200) {
        this.setState({
          codebook: [response.data.filter(item => item.codeType === "brand"),
          response.data.filter(item => item.codeType === "model"),
          response.data.filter(item => item.codeType === "fuel"),
          response.data.filter(item => item.codeType === "gearbox"),
          response.data.filter(item => item.codeType === "class")]
        });
      }
    }, (error) => {
      console.log(error);
    });
  }

  search = () => {
    let ok = this.startDateValidation(this.state.startDate);
    ok = this.endDateValidation(this.state.endDate) && ok;
    ok = this.startTimeValidation(this.state.startTime) && ok;
    ok = this.endTimeValidation(this.state.endTime) && ok;
    ok = this.cityValidation(this.state.city) && ok;
    if (ok) {
      let startDate = new Date(this.state.startDate);
      let startTime = this.state.startTime.split(':');
      startDate.setHours(startTime[0], startTime[1], 0, 0);
      startDate = startDate.getTime();
      localStorage.setItem('start', startDate)
      let endDate = new Date(this.state.endDate);
      let endTime = this.state.endTime.split(':');
      endDate.setHours(endTime[0], endTime[1], 0, 0);
      endDate = endDate.getTime();
      localStorage.setItem('end', endDate)
      let city = this.state.city;
      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      axios({
        method: 'get',
        url: url + 'advertisement/search',
        params: { startDate, endDate, city },
        headers: { "Authorization": AuthStr },
      }).then((response) => {
        let prices = response.data.map(d => d.pricelist.priceDay);
        let maxPrice = Math.max(...prices);
        let mileages = response.data.map(d => d.carAd.mileage);
        let maxMileage = Math.max(...mileages);
        let kidsSeats = response.data.map(d => d.carAd.kidsSeats);
        let maxKidsSeats = Math.max(...kidsSeats);
        let limits = response.data.map(d => d.carAd.mileageLimit);
        let maxLimit = Math.max(...limits);
        let temp = response.data;
        temp.sort(function(a,b){return a.carAd.price - b.carAd.price})

        this.setState({
          adsSearch: temp, adsFiltered: temp, hideSearch: true, hideAll: true,
          maxPrice: maxPrice, selectedPrice: [0, maxPrice],
          maxMileage: maxMileage, selectedMileage: maxMileage,
          maxKids: maxKidsSeats, selectedKids: 0,
          maxMileageLimit: maxLimit, selectedMileageLimit: maxLimit,
          unlimitedMileage: false, damageWaiver: false, selectedSort: "Price"
        });
        this.getCodebook();
        this.setState({ hideBasket: !(localStorage.getItem('role') === "ROLE_CUSTOMER") });
        if(response.data.length === 0){
          NotificationManager.info("There are no available cars for the selected parameters", '', 3000);
          this.setState({hideSearch: false});
        }
      }, (error) => {
        console.log(error);
      });
    }
  }

  apply = () => {
    let temp = this.state.adsSearch;
    if (this.state.selectedCodes[0].length > 0) {
      temp = temp.filter(item => this.state.selectedCodes[0].includes(item.carAd.make));
    }
    if (this.state.selectedCodes[1].length > 0) {
      temp = temp.filter(item => this.state.selectedCodes[1].includes(item.carAd.model));
    }
    if (this.state.selectedCodes[2].length > 0) {
      temp = temp.filter(item => this.state.selectedCodes[2].includes(item.carAd.fuel));
    }
    if (this.state.selectedCodes[3].length > 0) {
      temp = temp.filter(item => this.state.selectedCodes[3].includes(item.carAd.gearbox));
    }
    if (this.state.selectedCodes[4].length > 0) {
      temp = temp.filter(item => this.state.selectedCodes[4].includes(item.carAd.carClass));
    }
    temp = temp.filter(item => item.pricelist.priceDay <= this.state.selectedPrice[1] && item.pricelist.priceDay >= this.state.selectedPrice[0]);
    temp = temp.filter(item => item.carAd.mileage <= this.state.selectedMileage);
    temp = temp.filter(item => item.carAd.kidsSeats >= this.state.selectedKids);
    if (!this.state.unlimitedMileage) {
      temp = temp.filter(item => item.carAd.mileageLimit <= this.state.selectedMileageLimit);
    }
    else {
      temp = temp.filter(item => item.carAd.mileageLimit == -1);
    }
    if (this.state.damageWaiver) {
      temp = temp.filter(item => item.carAd.insurance);
    }
    if(this.state.selectedSort === "Price"){
      temp.sort(function(a,b){return a.carAd.price - b.carAd.price})
    }
    else if(this.state.selectedSort === "Mileage"){
      temp.sort(function(a,b){return a.carAd.mileage - b.carAd.mileage})
    }
    else if(this.state.selectedSort === "Rating"){
      temp.sort(function(a,b){return a.carAd.rating - b.carAd.rating})
    }
    this.setState({ adsFiltered: temp });
    this.toggle();
  }

  clearAll = () => {
    this.setState({
      selectedCodes: [[], [], [], [], []], selectedPrice: [0, this.state.maxPrice], damageWaiver: false, selectedSort:"Price",
      selectedKids: 0, selectedMileage: this.state.maxMileage, unlimitedMileage: false, selectedMileageLimit: this.state.maxMileageLimit
    })
  }

  renderCheckboxes(int) {
    return (
      <>
        <Col md="2">
          <h5>{this.state.header[int]}</h5>
          <ListGroup>
            {this.state.codebook[int].map(code => (
              <ListGroupItem key={code.id} className="list-group-item">
                <Label>&nbsp;&nbsp;
                  <Input type="checkbox" onChange={(e) => this.toggleCheckbox(int, e.target.value)} value={code.name} checked={this.state.selectedCodes[int].includes(code.name)}></Input>
                  {code.name}
                </Label>
              </ListGroupItem>
            ))}
          </ListGroup>
        </Col>
      </>
    )
  }

  toggleCheckbox(int, name) {
    let temp = this.state.selectedCodes;
    let index = temp[int].indexOf(name);
    if (index > -1) {
      temp[int].splice(index, 1);
    } else {
      temp[int].push(name);
    }
    this.setState({ selectedCodes: temp });
  }

  addToBasket = id => {
    for (let i = 0; i < this.state.oglasi.length; i++) {
      if (this.state.oglasi[i].id == id) {
        let array = [];
        array = JSON.parse(sessionStorage.getItem('basket')) || [];
        array.push(id);
        console.log(array);
        sessionStorage.setItem('basket', JSON.stringify(array));
        NotificationManager.success("Item successfully added!", '', 3000);
        break;
      }
    }

  }

  render() {
    return (
      <div>
        <Modal modalClassName="modal-register" isOpen={this.state.showModal}>
        <div className="modal-header no-border-header text-center">
                  <button
                    aria-label="Close"
                    className="close"
                    data-dismiss="modal"
                    type="button"
                    onClick={event => this.setState({showModal:false} )}
                  >
                  <span aria-hidden={true}>x</span>
                  </button>
                  </div>
                  <div className="modal-body">     
            <Form onSubmit={this.manualRequest}>
                  <FormGroup>
                  <Label>From:</Label>
                  <Input type="date" className="form-control" value={this.state.manualStartDate}  onChange={(event) => this.startDateValidationModal(event.target.value)}/>
                  <p style={{color:'red'}} > {this.state.textStartManualValidation} </p>
                  </FormGroup>
                  <FormGroup>
                  <Label>Until:</Label>
                  <Input type="date" className="form-control" value={this.state.manualEndDate}  onChange={(event) => this.endDateValidationModal(event.target.value)}/>
                  <p style={{color:'red'}} > {this.state.textEndManualValidation} </p>
                  </FormGroup>
                  <Button block className="btn-round" color="info">
                    Confirm
                  </Button>
              </Form>         
            </div>
        </Modal>

        <Row hidden={this.state.hideSearch}>
          <Col xs="12" md="12" sm="12">
            <Card>
              <CardHeader>
                <Button block color="link" className="text-center m-0 p-0" onClick={() => this.toggleAccordion(0)} aria-expanded={this.state.accordion[0]} aria-controls="collapseOne">
                  <h5 className="m-0 p-0">Start your search here</h5>
                </Button>              </CardHeader>
              <Collapse isOpen={this.state.accordion[0]} data-parent="#accordion" id="collapseOne" aria-labelledby="headingOne">
                <CardBody>
                  <Row>
                    <Col md="3" xs="3" sm="3">
                      <FormGroup>
                        <Label>Pick up date</Label>
                        <InputGroup>
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="fa fa-calendar"></i>
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input type="date" name="startDate" onChange={(e) => this.startDateValidation(e.target.value)} placeholder="Pick up date" />
                        </InputGroup>
                        <FormText color="danger">{this.state.startDateError}</FormText>
                      </FormGroup>
                    </Col>
                    <Col md="3" xs="3" sm="3">
                      <FormGroup>
                        <Label>Pick up time</Label>
                        <InputGroup>
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="fa fa-clock-o"></i>
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input type="time" name="startTime" onChange={(e) => this.startTimeValidation(e.target.value)} placeholder="Pick up time" />
                        </InputGroup>
                        <FormText color="danger">{this.state.startTimeError}</FormText>
                      </FormGroup>
                    </Col>
                    <Col md="3" xs="3" sm="3">
                      <FormGroup>
                        <Label>Drop off date</Label>
                        <InputGroup>
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="fa fa-calendar-check-o"></i>
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input type="date" name="endDate" onChange={(e) => this.endDateValidation(e.target.value)} placeholder="Drop off date" />
                        </InputGroup>
                        <FormText color="danger">{this.state.endDateError}</FormText>
                      </FormGroup>
                    </Col>
                    <Col md="3" xs="3" sm="3">
                      <FormGroup>
                        <Label>Drop off time</Label>
                        <InputGroup>
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="fa fa-clock-o"></i>
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input type="time" name="endTime" onChange={(e) => this.endTimeValidation(e.target.value)} placeholder="Drop off time" />
                        </InputGroup>
                        <FormText color="danger">{this.state.endTimeError}</FormText>
                      </FormGroup>
                    </Col>
                  </Row>
                  <Row>
                    <Col md="6" xs="6" sm="6">
                      <FormGroup>
                        <Label>City</Label>
                        <InputGroup>
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="icon-location-pin icons"></i>
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input type="text" name="city" onChange={(e) => this.cityValidation(e.target.value)} placeholder="Pick up and drop off" />
                        </InputGroup>
                        <FormText color="danger">{this.state.cityError}</FormText>
                      </FormGroup>
                    </Col>
                    <Col md="3" xs="3" sm="3">
                      <FormGroup row>
                        <Label>&nbsp;</Label>
                        <InputGroup>
                          <Button type="button" color="primary" onClick={(e) => this.search()}><i className="fa fa-search"></i>&nbsp;&nbsp;Search</Button>
                          <Button style={{ marginLeft: "1rem" }} type="button" color="danger" onClick={(e) => this.setState({ adsSearch: this.state.oglasi, adsFiltered: this.state.oglasi, hideAll: false })}>Reset</Button>
                        </InputGroup>
                      </FormGroup>
                    </Col>
                  </Row>
                </CardBody>
              </Collapse>
            </Card>
          </Col>
        </Row>

        <section className="bar pt-0" hidden={this.state.hideAll}>
          <div className="row">
            {this.state.oglasi.map(oglas => (
              <Card style={{ width: "19rem", textAlign: "left", alignItems: "center", margin: 9 }} key={oglas.id} data-key={oglas.id}>
                <CardBody>
                  <div onClick={(e) => this.saznajVise(oglas.id)} style={{ width: "18rem", height: "15rem" }}>
                    <MDBCardImage style={{ maxWidth: "18rem", maxHeight: "14rem", textAlign: "center", alignItems: "center", margin: 5 }} src={oglas.carAd.images[0].imageUrl} alt="Fotografija" top hover
                      overlay="white-slight" />
                  </div>
                  <div>
                    <h4 style={{ textAlign: "center" }} className="text-primary font-weight-bold">{oglas.carAd.model} {oglas.carAd.make}</h4>
                    <MDBCardTitle tag="h6"> Pick up location: {oglas.city} </MDBCardTitle>
                    <MDBCardTitle tag="h6"> Start date: {this.getDateString(oglas.startDate)}  </MDBCardTitle>
                    <MDBCardTitle tag="h6"> End date: {this.getDateString(oglas.endDate)}  </MDBCardTitle>
                    <MDBCardTitle tag="h6" hidden={!oglas.carAd.insurance}> Collision Damage Waiver: <i className="fa fa-check" />  </MDBCardTitle>
                    <MDBCardTitle tag="h6" hidden={oglas.carAd.insurance}> Collision Damage Waiver: <i className="fa fa-close" />  </MDBCardTitle>
                    <MDBBtn color="primary" size="md" onClick={(e) => this.saznajVise(oglas.id)}>
                      Details
                    </MDBBtn>{}
                    <MDBBtn hidden = {!this.state.isAgent} style={{ marginLeft: "1rem" }} color="warning" size="md" onClick={(e) => this.openModal(oglas)}>
                      Reserve
                    </MDBBtn>
                    <MDBBtn style={{ marginLeft: "1rem" }} color="warning" size="md" onClick={(e) => this.addToBasket(oglas.id)} hidden={this.state.hideBasket}>
                      Add to basket
                    </MDBBtn>
                  </div>
                </CardBody>
              </Card>
            ))}
          </div>
        </section>

        <Card hidden={!this.state.hideAll}>
          <CardHeader style={{ backgroundColor: "whiteSmoke" }} hidden={!this.state.hideSearch}>
            <Row>
              <Col md="9" xs="9" lg="9"><i className="fa fa-car"></i> <b>{this.state.city}, {this.getDateString(this.getDateTime(this.state.startDate, this.state.startTime))} - {this.getDateString(this.getDateTime(this.state.endDate, this.state.endTime))}</b></Col>
              <Col md="3" xs="3" lg="3">
                <Button color="link" className="text-right m-0 p-0" onClick={() => this.setState({ hideSearch: false })}>
                  <h6 className="m-0 p-0">Change search <i className="fa fa-search"></i></h6>
                </Button>
                <Button style={{marginLeft:"1rem"}} type="button" color="danger" onClick={(e) => this.setState({ adsSearch: this.state.oglasi, adsFiltered: this.state.oglasi, hideSearch: false })}>Reset</Button>
              </Col>
            </Row>
            <Row style={{ flex: 1, flexDirection: "column" }}>
              <Dropdown style={{ flex: 1 }} isOpen={this.state.dropdownOpen} toggle={() => { this.toggle(); }}>
                <DropdownToggle
                  tag="span"
                  onClick={() => { this.toggle(); }}
                  data-toggle="dropdown"
                  aria-expanded={this.state.dropdownOpen}
                >
                  <Button color="link" className="text-right m-0 p-0"><h6 className="m-0 p-0">All filters</h6></Button>
                </DropdownToggle>
                <DropdownMenu md="12" lg="12" xs="12" block style={{ padding: "1rem" }}>
                  <div>
                    <Row >
                      {this.renderCheckboxes(0)}
                      {this.renderCheckboxes(1)}
                      {this.renderCheckboxes(2)}
                      {this.renderCheckboxes(3)}
                      {this.renderCheckboxes(4)}
                      <Col md="2">
                        <h5>Daily price (KM)</h5>
                        <RangeWithTooltip
                          min={0}
                          max={this.state.maxPrice}
                          defaultValue={[this.state.selectedPrice[0], this.state.selectedPrice[1]]}
                          allowCross={false}
                          tipFormatter={value => `${value}`}
                          onChange={value => { this.setState({ selectedPrice: value }) }}
                          trackStyle={[{ backgroundColor: 'blue' }, { backgroundColor: 'blue' }]}
                          railStyle={{ backgroundColor: 'lightBlue' }}
                          tipProps={{
                            placement: 'top',
                            prefixCls: 'rc-slider-tooltip'
                          }}
                        />
                        <br></br>
                        <h5>Mileage (km)</h5>
                        <SliderWithTooltip
                          min={0}
                          max={this.state.maxMileage}
                          defaultValue={this.state.selectedMileage}
                          tipFormatter={value => `${value}`}
                          onChange={value => { this.setState({ selectedMileage: value }) }}
                          trackStyle={{ backgroundColor: 'blue' }}
                          railStyle={{ backgroundColor: 'lightBlue' }} />
                        <br></br>
                        <h5>Mileage limit (km)</h5>
                        <Row>
                          <Col md="6">
                            Unlimited
                        </Col>
                          <Col md="2">
                            <Input type="checkbox" checked={this.state.unlimitedMileage} value={this.state.unlimitedMileage} onChange={(e) => this.setState({ unlimitedMileage: !this.state.unlimitedMileage })}></Input>
                          </Col>
                        </Row>
                        <SliderWithTooltip
                          disabled={this.state.unlimitedMileage} style={{ marginTop: "0.5rem" }}
                          min={0}
                          max={this.state.maxMileageLimit}
                          defaultValue={this.state.selectedMileageLimit}
                          tipFormatter={value => `${value}`}
                          onChange={value => { this.setState({ selectedMileageLimit: value }) }}
                          trackStyle={{ backgroundColor: 'blue' }}
                          railStyle={{ backgroundColor: 'lightBlue' }} />
                        <br></br>
                        <h5>Kids seats</h5>
                        <SliderWithTooltip
                          min={0}
                          max={this.state.maxKids}
                          defaultValue={this.state.selectedKids}
                          tipFormatter={value => `${value}`}
                          onChange={value => { this.setState({ selectedKids: value }) }}
                          trackStyle={{ backgroundColor: 'lightBlue' }}
                          railStyle={{ backgroundColor: 'blue' }} />
                      </Col>
                    </Row>
                    <br></br>
                    <Row>
                      <Col md="2">
                        <h5>Collision damage waiver</h5>
                        </Col>
                        <Col md="3">
                        <Row>
                          <Input type="checkbox" checked={this.state.damageWaiver} value={this.state.damageWaiver} onChange={(e) => this.setState({ damageWaiver: !this.state.damageWaiver })}></Input>
                          <h5 style={{marginLeft:"2rem"}}>Sort by</h5>
                          <Dropdown style={{marginLeft:"1rem"}} isOpen={this.state.isOpenSort} toggle={() => { this.toggle2(); }}>
                            <DropdownToggle caret style={{width:"5rem"}}>
                              {this.state.selectedSort}
                  </DropdownToggle>
                            <DropdownMenu>
                              <DropdownItem onClick={(e) => this.setState({selectedSort:"Price"})}>&nbsp;&nbsp;<Input name="sort" type="radio" checked={this.state.selectedSort==="Price"}  value="Price">Price</Input>Price</DropdownItem>
                              <DropdownItem onClick={(e) => this.setState({selectedSort: "Rating"})}>&nbsp;&nbsp;<Input name="sort" type="radio" checked={this.state.selectedSort==="Rating"}  value="Rating">Rating</Input>Rating</DropdownItem>
                              <DropdownItem onClick={(e) => this.setState({selectedSort: "Mileage"})}>&nbsp;&nbsp;<Input name="sort" type="radio" checked={this.state.selectedSort==="Mileage"}  value="Mileage">Mileage</Input>Mileage</DropdownItem>
                            </DropdownMenu>
                          </Dropdown>
                          </Row>
                        </Col>
                    </Row>
                  </div>
                    <div style={{ justifyContent: "right", justifySelf: "right" }}>
                      <br></br>
                      <Button type="button" style={{ margin: 5 }} color="secondary" onClick={(e) => this.clearAll()}>Clear all</Button>
                      <Button type="button" color="primary" onClick={(e) => this.apply()}>Apply</Button>
                    </div>
                </DropdownMenu>
              </Dropdown>
            </Row>
          </CardHeader>

            <CardBody style={{ backgroundColor: "whiteSmoke" }}>
              <section className="bar pt-0">
                <div className="row">
                  {this.state.adsFiltered.map(oglas => (
                    <Card style={{ width: "18rem", textAlign: "left", alignItems: "center", margin: 9 }} key={oglas.id} data-key={oglas.id}>
                      <CardBody>
                        <div onClick={(e) => this.saznajVise(oglas.id)}  style={{ width: "17rem", height: "13rem" }}>
                          <MDBCardImage style={{ maxWidth: "17rem", maxHeight: "12rem", textAlign: "center", alignItems: "center", margin: 5 }} src={oglas.carAd.images[0].imageUrl} alt="Fotografija" top hover
                            overlay="white-slight" />
                        </div>
                        <div>
                          <h4 style={{ textAlign: "center" }} className="text-primary font-weight-bold">{oglas.carAd.model} {oglas.carAd.make}</h4>
                          <Row>
                            <Col>
                              <MDBCardTitle tag="h6"> Car class: <p style={{ color: "red" }}>{oglas.carAd.carClass}</p>  </MDBCardTitle>
                              <MDBCardTitle tag="h6"> Daily price: <p style={{ color: "red" }}>{oglas.pricelist.priceDay}</p>  </MDBCardTitle>
                              <MDBCardTitle tag="h6"> Mileage: <p style={{ color: "red" }}>{oglas.carAd.mileage}</p>  </MDBCardTitle>
                              <MDBCardTitle tag="h6"> Fuel type: <p style={{ color: "red" }}>{oglas.carAd.fuel}</p> </MDBCardTitle>
                            </Col>
                            <Col>
                              <MDBCardTitle tag="h6"> Kids seats: <p style={{ color: "red" }}>{oglas.carAd.kidsSeats}</p>  </MDBCardTitle>
                              <MDBCardTitle tag="h6"> Collision Damage Waiver: <i style={{ color: "red" }} hidden={!oglas.carAd.insurance} className="fa fa-check" /><i style={{ color: "red" }} hidden={oglas.carAd.insurance} className="fa fa-close" />  </MDBCardTitle>
                              <MDBCardTitle tag="h6"> Mileage limit: <p style={{ color: "red" }}>{oglas.carAd.mileageLimit}</p>  </MDBCardTitle>
                              <MDBCardTitle tag="h6"> Gearbox type: <p style={{ color: "red" }}>{oglas.carAd.gearbox}</p>  </MDBCardTitle>
                            </Col>
                          </Row>
                          <MDBCardTitle tag="h6"> Total price: <p style={{ color: "red" }}>{oglas.carAd.price}</p>  </MDBCardTitle>

                          <MDBBtn color="primary" size="md" onClick={(e) => this.saznajVise(oglas.id)}>
                            Details
                    </MDBBtn>
                          <MDBBtn color="warning" size="md" onClick={(e) => this.addToBasket(oglas.id)} hidden={this.state.hideBasket}>
                            Add to basket
                    </MDBBtn>

                        </div>
                      </CardBody>
                    </Card>
                  ))}
                </div>
              </section>
            </CardBody>
        </Card>
      </div>
    );
  }
}

export default Oglasi;
