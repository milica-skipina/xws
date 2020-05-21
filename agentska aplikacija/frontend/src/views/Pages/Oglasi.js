import React, { Component } from 'react';
import { MDBCard, MDBCardTitle, MDBBtn, MDBCardGroup, MDBCardImage, MDBCardText, MDBCardBody } from "mdbreact";
import {
  Row, Col, Card, CardHeader, CardBody, FormGroup, Label, Input, InputGroup, InputGroupAddon,
  InputGroupText, Button, Collapse, FormText, Dropdown, DropdownToggle, DropdownMenu, Table, ListGroup, ListGroupItem
} from "reactstrap";
import axios from 'axios';
import '../../scss/vendors/custom.css'
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

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
      header: ["Car brands", "Car models", "Fuel types", "Gearbox types", "Car type"]
    };
    this.saznajVise = this.saznajVise.bind(this);
    this.toggleAccordion = this.toggleAccordion.bind(this);
    this.startDateValidation = this.startDateValidation.bind(this);
    this.endDateValidation = this.endDateValidation.bind(this);
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
  }

  componentDidMount() {
    axios({
      method: 'get',
      url: url + 'advertisement/getAllAds',
      // headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status !== 404)
        this.setState({ oglasi: response.data });
    }, (error) => {
      console.log(error);
    });
    this.getCodebook();
  }

  toggle() {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen,
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
    axios({
      method: 'get',
      url: url + 'codebook',
      // headers: { "Authorization": AuthStr } ,       
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

      let endDate = new Date(this.state.endDate);
      let endTime = this.state.endTime.split(':');
      endDate.setHours(endTime[0], endTime[1], 0, 0);
      endDate = endDate.getTime();
      let city = this.state.city;
      axios({
        method: 'get',
        url: url + 'advertisement',
        params: { startDate, endDate, city }
      }).then((response) => {
        this.setState({ adsSearch: response.data, adsFiltered: response.data, hideSearch: true, hideAll: true });
        this.getCodebook();
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
    this.setState({ adsFiltered: temp });
  }

  clearAll = () => {
    this.setState({ selectedCodes: [[], [], [], [], []] })
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

  render() {
    return (
      <div>
        
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
                      <FormGroup>
                        <Label>&nbsp;</Label>
                        <InputGroup>
                          <Button type="button" color="primary" onClick={(e) => this.search()}><i className="fa fa-search"></i>&nbsp;&nbsp;Search</Button>
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
                  <div style={{ width: "18rem", height: "21rem" }}>
                    <MDBCardImage style={{ maxWidth: "18rem", maxHeight: "20rem", textAlign: "center", alignItems: "center", margin: 5 }} src={oglas.carAd.images[0].imageUrl} alt="Fotografija" top hover
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
                    </MDBBtn>
                  </div>
                </CardBody>
              </Card>
            ))}
          </div>
        </section>

        <Card hidden={!this.state.hideAll}>
          <CardHeader style={{ backgroundColor: "whiteSmoke" }} hidden={!this.state.hideSearch}><Row>
            <Col md="10" xs="8" lg="10"><i className="fa fa-car"></i> <b>{this.state.city}, {this.getDateString(this.getDateTime(this.state.startDate, this.state.startTime))} - {this.getDateString(this.getDateTime(this.state.endDate, this.state.endTime))}</b></Col>
            <Col md="2" xs="4" lg="2">
              <Button color="link" className="text-right m-0 p-0" onClick={() => this.setState({ hideSearch: false })}>
                <h6 className="m-0 p-0">Change search <i className="fa fa-search"></i></h6>
              </Button></Col>
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
                        <h5>Daily price</h5>

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
                      <div style={{ width: "17rem", height: "21rem" }}>
                        <MDBCardImage style={{ maxWidth: "17rem", maxHeight: "20rem", textAlign: "center", alignItems: "center", margin: 5 }} src={oglas.carAd.images[0].imageUrl} alt="Fotografija" top hover
                          overlay="white-slight" />
                      </div>
                      <div>
                        <h4 style={{ textAlign: "center" }} className="text-primary font-weight-bold">{oglas.carAd.model} {oglas.carAd.make}</h4>
                        <Row>
                          <Col>
                            <MDBCardTitle tag="h6"> Car class: <p style={{ color: "red" }}>{oglas.carAd.carClass}</p>  </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Price: <p style={{ color: "red" }}>{oglas.carAd.price}</p>  </MDBCardTitle>
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

                        <MDBBtn color="primary" size="md" onClick={(e) => this.saznajVise(oglas.id)}>
                          Details
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


