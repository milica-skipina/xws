import React, { Component } from 'react';
//import { Card, CardBody, CardHeader, Col, Row, Table, Button } from 'reactstrap';
//import { MDBCard, MDBCardTitle, MDBBtn, MDBCardGroup, MDBCardImage, MDBCardText, MDBCardBody } from "mdbreact";

import axios from 'axios';
import { Carousel, Card, CardHeader, CardBody, CarouselIndicators, CarouselControl, Col, CarouselCaption, CarouselItem, Row, Badge,
  Button,
  Form,
  FormGroup,
  FormText,
  Input,
  Label, CardFooter,  Pagination, PaginationItem, PaginationLink } from 'reactstrap';
import { MDBCard, MDBCardTitle, MDBBtn, MDBCardGroup, MDBCardImage, MDBCardText, MDBCardBody } from "mdbreact";
import { RoleAwareComponent } from 'react-router-role-authorization';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';
const pricelistPerPage = 7;

class Oglas extends RoleAwareComponent {
  constructor(props) {
    super(props);
    this.state = {
      activeIndex: 0,
      canEdit:false,
      idOglasa:"",
      brSjedista: "",
      cjenovnici: [],
      cjenovnikId: "",
      startDate:"",
      hideForm: true,
      brSjedista:"",
      maxKilometraza:"",
      kilometraza:"",
      endDate:"",
      city: "",
      dugme: false,
      pricelists:[],
      showpricelist:[],
      izbrisan: false,
      collision: false,
      userRoles: [],
      cjenovnik:"",
      oglas: {
        carAd: {
          images: []
        }
      },
    };

    let arr = [];
    arr.push(localStorage.getItem('role'));
    console.log("KONS",arr);
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_SELLER', 'ROLE_CUSTOMER'];

    this.next = this.next.bind(this);
    this.previous = this.previous.bind(this);
    this.goToIndex = this.goToIndex.bind(this);
    this.onExiting = this.onExiting.bind(this);
    this.onExited = this.onExited.bind(this);
    this.getDateString = this.getDateString.bind(this);
    this.getDateStringPicker = this.getDateStringPicker.bind(this);
    this.editAd = this.editAd.bind(this);
    this.getPricelist = this.getPricelist.bind(this);
  }

  getPricelist = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'pricelist', 
      headers: { "Authorization": AuthStr },  
      // headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status === 200){
        this.setState({ pricelists: response.data, showpricelist:response.data.slice(0, pricelistPerPage) })
        this.setState({ cjenovnici: response.data });

        // dodati setovanje prvog
      }
    }, (error) => {
      console.log(error);
    });
  }

  reset(){
    this.setState({hideForm:true, cjenovnikId:this.state.oglas.pricelist.id,  dugme:false,  startDate: "", endDate:"", brSjedista: this.state.oglas.carAd.kidsSeats, kilometraza:this.state.oglas.carAd.mileage, maxKilometraza:this.state.oglas.carAd.mileageLimit, city : this.state.oglas.city})
  }

  edit(e){
this.setState({hideForm:false, canEdit:true})
  }

      
  delete(e){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
      axios({
        method: 'delete',
        url: url+ 'advertisement/' + this.state.idOglasa,
        headers: { "Authorization": AuthStr },  
      }).then((response) => {
       this.setState({izbrisan: true})
       NotificationManager.success("Successfully deleted!", '', 3000);     
      }, (error) => {
        console.log(error);
      });
    }
    


  cityValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ city: c });
    if (c === '') {
      this.setState({ cityText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ cityText: "" });
      return true;
    }
  }

  promijeniPeriod(e){
    this.setState({dugme:true});
  }

  componentDidMount() {
    const { id } = this.props.match.params;
    this.getPricelist();
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'advertisement/' + id,
      headers: { "Authorization": AuthStr },  
    }).then((response) => {
      console.log(response);
      this.setState({ oglas: response.data })
      this.setState({idOglasa: response.data.id})
      console.log("id")
      console.log(this.state.idOglasa)
      this.setState({ cjenovnikId: response.data.pricelist.id, kilometraza:response.data.carAd.mileage, maxKilometraza:response.data.carAd.mileageLimit, city:response.data.city, cjenovnikId:response.data.pricelist.id, brSjedista:response.data.carAd.kidsSeats})
     this.setState({cjenovnik:response.data.pricelist})
      if(response.data.carAd.insurance === true){
        this.setState({collision: true})
      }
      let date = new Date(response.data.startDate);
      let month = date.getMonth() + 1;
      let pom = date.getDate();
      if(month<10){
        month = '0'+month;
      }
      if(date.getDate() < 10){
        pom = '0'+date.getDate();
      }
      let ret = date.getFullYear() + '-' + month + '-' + pom;
      console.log("start")
      console.log(ret);
      this.setState({startDate:ret});

      date = new Date(response.data.endDate);
      month = date.getMonth() + 1;
      pom = date.getDate();
      if(month<10){
        month = '0'+month;
      }
      if(date.getDate() < 10){
        pom = '0'+date.getDate();
      }
      ret = date.getFullYear() + '-' + month + '-' + pom;
      console.log("end")
      console.log(ret);
      this.setState({endDate:ret});
    }, (error) => {
      console.log(error);
    });

   // this.setState({cjenovnikId:this.state.oglas.pricelist.id})

    axios({
      method: 'get',
      url: url + 'advertisement/canAccess/' + id,
      headers: { "Authorization": AuthStr },  
    }).then((response) => {
      console.log(response);
      this.setState({ canEdit: false })
    }, (error) => {
      console.log(error);
    });

  }

  cjenovnikValidation(e) {
    this.setState({ cjenovnikId: e });
  }

  onExiting() {
    this.animating = true;
  }

  collisionValidation = (e) => {
    if (e.target.checked) {
      this.setState({ collision: true });
    }
    else {
      this.setState({ collision: false });
    }
    console.log(this.state.collision);
  }

  sjedisteValidation(c) {
 
    this.setState({ brSjedista: c });
    let sun = c;
    if (sun === '') {
      this.setState({ sjedisteText: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ sjedisteText: "Broj sjedista mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ sjedisteText: "Broj sjedista mora biti pozitivan broj!" });
      return false;
    }
    else if(sun > 7){
      this.setState({ sjedisteText: "Maksimalna broj sjedista je 7!" });
      return false;
    }else {
      this.setState({ sjedisteText: "" });
      return true;
    }
  }


  kilometrazaValidation(c) {
   
    this.setState({ kilometraza: c });
    let sun = c;
    if (sun === '') {
      this.setState({ kilometrazaText: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ kilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ kilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
  else {
      this.setState({ kilometrazaText: "" });
      return true;
    }
  }


  maxKilometrazaValidation(c) {
   
    this.setState({ maxKilometraza: c });
    let sun = c;
    if (sun === '') {
      this.setState({ maxKilometrazaText: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ maxKilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ maxKilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
  else {
      this.setState({ maxKilometrazaText: "" });
      return true;
    }
  }

  
  handlePageChange(pageNumber) {
    let temp = (pageNumber - 1) * pricelistPerPage;
    this.setState({ activePage: pageNumber, showpricelist: this.state.pricelists.slice(temp, temp + pricelistPerPage) })
  }

  toggle() {
    this.setState({ collapse: !this.state.collapse });
  }

  toggleFade() {
    this.setState((prevState) => { return { fadeIn: !prevState } });
  }


  onExited() {
    this.animating = false;
  }

  

  next() {
    if (this.animating) return;
    const nextIndex = this.state.activeIndex === this.state.oglas.carAd.images.length - 1 ? 0 : this.state.activeIndex + 1;
    this.setState({ activeIndex: nextIndex });
  }

  previous() {
    if (this.animating) return;
    const nextIndex = this.state.activeIndex === 0 ? this.state.oglas.carAd.images.length - 1 : this.state.activeIndex - 1;
    this.setState({ activeIndex: nextIndex });
  }

  goToIndex(newIndex) {
    if (this.animating) return;
    this.setState({ activeIndex: newIndex });
  }

  editAd(e){
    e.preventDefault();
    let ok = true;
    ok = this.startDateValidation(this.state.startDate);
    ok = this.endDateValidation(this.state.endDate) && ok;
    ok = this.cityValidation(this.state.city) && ok;
    ok = this.maxKilometrazaValidation(this.state.maxKilometraza) && ok;
    ok = this.kilometrazaValidation(this.state.kilometraza) && ok;
    ok = this.sjedisteValidation(this.state.brSjedista) && ok;

    if(ok){
        let data = {
          'city': this.state.city,
          'startDate': this.state.startDate,
          'endDate': this.state.endDate,
          "carAd":{
            'mileage': this.state.kilometraza,
            'mileageLimit': this.state.maxKilometraza,
            'kidsSeats': this.state.brSjedista,
            'insurance': this.state.collision,
          }
        }
    
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'put',
      url: url + 'advertisement/'+this.state.oglas.id + '/' + this.state.cjenovnikId,
      data: data,
      headers: { "Authorization": AuthStr },  
    }).then((response)=>{ 
      this.setState({oglas:response.data})
      this.reset();
      NotificationManager.success("Successfully edited!", '', 3000);     
    },(error)=>{
      console.log(error);
      this.reset();
    });
      }
    }

  getDateStringPicker(miliseconds) {
    let date = new Date(miliseconds);
    let month = date.getMonth() + 1;
    let ret = month + '/' + date.getDate() + '/' + date.getFullYear();
    console.log(ret);
    return ret;
  }

  startDateValidation(ddatum) {
    console.log(ddatum);
    console.log(this.state.collision);
    this.setState({ startDate: ddatum })
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


      if (ddatum === undefined || ddatum === '') {
        this.setState({ startDateText: "Datum je obavezno polje" })
        return false;
      }
      else if (godina < year) {
        this.setState({ startDateText: "Godina je prosla" })
        return false;
      }
      else if (mjesec < month && godina <= year) {
        this.setState({ startDateText: "Mjesec je prosao" })
        return false;
      }
      else if (dan < date && mjesec <= month && godina <= year) {
        this.setState({ startDateText: "Dan je prosao" })
        return false;
      }
      else {
        this.setState({ startDateText: "" })
        return true;
      }
    }
  };

  endDateValidation(ddatum) {
    this.setState({ endDate: ddatum })
    let start = this.state.startDate;
    let end = ddatum;
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


      if (ddatum === undefined || ddatum === '') {
        this.setState({ endDateText: "Datum je obavezno polje" })
        return false;
      }
      else if (godina < year) {
        this.setState({ endDateText: "Godina je prosla" })
        return false;
      }
      else if (mjesec < month && godina <= year) {
        this.setState({ endDateText: "Mjesec je prosao" })
        return false;
      }
      else if (dan < date && mjesec <= month && godina <= year) {
        this.setState({ endDateText: "Dan je prosao" })
        return false;
      }
      else if (new Date(end) <= new Date(start)) {
        this.setState({ endDateText: "Datum mora biti veci od pocetnog" })
        return false;
      }

      else {
        this.setState({ endDateText: "" })
        return true;
      }
    }
  };

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

  render() {
    const { activeIndex } = this.state;
    const len = this.state.pricelists.length;
    const pageNumbers = Array.from(Array(Math.ceil(len / pricelistPerPage)).keys());

    const slides = this.state.oglas.carAd.images.map((item) => {
      return (
        <CarouselItem
          onExiting={this.onExiting}
          onExited={this.onExited}
          key={item.imageUrl}
        >
          <img style={{ maxWidth: "38rem", maxHeight:"35rem"}} className="d-block w-100" src={item.imageUrl}/>
          <CarouselCaption/>
        </CarouselItem>
      );
    });

    let ret = (<div>
        <Row hidden = {this.state.izbrisan}>
          <Col xs="12" sm="6" md="4">
            <Card style={{ width: "18rem", textAlign: "left", alignItems: "center", margin: 9 }} key={this.state.oglas.id} data-key={this.state.oglas.id}>
              <CardBody>
                <div>
                  <h4 style={{ textAlign: "center" }} className="text-primary font-weight-bold">{this.state.oglas.carAd.model} {this.state.oglas.carAd.make}</h4>
                  <Row>
                    <Col>
                      <MDBCardTitle tag="h6"> Car class: <p style={{ color: "red" }}>{this.state.oglas.carAd.carClass}</p>  </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Mileage limit: <p style={{ color: "red" }}>{this.state.oglas.carAd.mileageLimit}km</p>  </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Gearbox type: <p style={{ color: "red" }}>{this.state.oglas.carAd.gearbox}</p>  </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Kids seats: <p style={{ color: "red" }}>{this.state.oglas.carAd.kidsSeats}</p>  </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Mileage: <p style={{ color: "red" }}>{this.state.oglas.carAd.mileage}km</p>  </MDBCardTitle>
                    </Col>
                    <Col>
                      <MDBCardTitle tag="h6"> Fuel type: <p style={{ color: "red" }}>{this.state.oglas.carAd.fuel}</p> </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Pick up location:<p style={{ color: "red" }}> {this.state.oglas.city} </p></MDBCardTitle>
                      <MDBCardTitle tag="h6"> Start date: <p style={{ color: "red" }}>{this.getDateString(this.state.oglas.startDate)}  </p> </MDBCardTitle>
                      <MDBCardTitle tag="h6"> End date: <p style={{ color: "red" }}>{this.getDateString(this.state.oglas.endDate)}   </p></MDBCardTitle>
                      <MDBCardTitle hidden={!this.state.collision}  tag="h6"> Collision Damage Waiver: <i style={{ color: "red" }} className="fa fa-check" /><i style={{ color: "red" }} hidden={this.state.oglas.carAd.insurance} className="fa fa-close" />  </MDBCardTitle>
                    </Col>
                    <Col>
                      <MDBCardTitle tag="h6"> Day price: <p style={{ color: "red" }}>{this.state.cjenovnik.priceDay}RSD</p> </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Discount for 20 days:<p style={{ color: "red" }}> {this.state.cjenovnik.discount20}RSD </p></MDBCardTitle>
                      <MDBCardTitle tag="h6"> Discount for 30 days: <p style={{ color: "red" }}>{this.state.cjenovnik.discount30}RSD  </p> </MDBCardTitle>
                      <MDBCardTitle tag="h6"> Exceed: <p style={{ color: "red" }}>{this.state.cjenovnik.exceedMileage}RSD   </p></MDBCardTitle>
                    </Col>
                   
                  </Row>
                </div>
              </CardBody>
            </Card>
          </Col>
          <Col xs="12" md = "6" xl="6">
            <Card style={{ maxWidth: "40rem", maxHeight:"40rem"}}>
              <CardHeader>
                <i className="fa fa-image fa-lg"></i><strong>Images</strong>
              </CardHeader>
              <CardBody>
                <Carousel activeIndex={activeIndex} next={this.next} previous={this.previous}>
                  <CarouselIndicators items={this.state.oglas.carAd.images} activeIndex={activeIndex} onClickHandler={this.goToIndex} />
                  {slides}
                  <CarouselControl direction="prev" directionText="Previous" onClickHandler={this.previous} />
                  <CarouselControl direction="next" directionText="Next" onClickHandler={this.next} />
                </Carousel>
              </CardBody>
            </Card>
          </Col>
          <Col hidden ={this.state.canEdit} className="mb-3 mb-xl-0">
            <Row>
                      <Button block color="primary" onClick={(e) => this.edit(e)}>Edit</Button>
            </Row>
            <br></br> 
            <Row>        
                    <Button block color="primary" onClick={(e) => this.delete(e)}>Delete</Button>
            </Row>
          </Col>
         
        </Row>
        <Row hidden = {this.state.hideForm}>
          <Col xs="12" md = "6" xl="6"> 
          <Card>
          <CardHeader>
              <strong>Editing advertisement</strong>
            </CardHeader>
            <CardBody>
            <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
              <Row>
            <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label>Kids seats</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="date-input" value={this.state.brSjedista} onChange={(event) => this.sjedisteValidation(event.target.value)} name="date-input" placeholder="broj sjedista" />
                        <FormText color="danger">{this.state.sjedisteText}</FormText>
                      </Col>
                    </FormGroup>
              </Col>
              <Col md="6">
                    <FormGroup row>
                      <Col md="3">
                        <Label>City</Label>
                      </Col>
                      <Col md="9">
                        <Input type="text" name="city" value={this.state.city} onChange={(event) => this.cityValidation(event.target.value)} placeholder="Pick up location" />
                        <FormText color="danger">{this.state.cityText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
              </Row>   
              <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Kilometraza</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" value={this.state.kilometraza} id="password-input" onChange={(event) => this.kilometrazaValidation(event.target.value)} name="password-input" placeholder="predjeni kilometri" autoComplete="new-password" />
                        <FormText color="danger">{this.state.kilometrazaText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Max kilometara</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="date-input" value={this.state.maxKilometraza} onChange={(event) => this.maxKilometrazaValidation(event.target.value)} name="date-input" placeholder="max kilometara" />
                        <FormText color="danger">{this.state.maxKilometrazaText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row>
                  <Col>
                </Col>
                </Row>
                <Row hidden = {this.state.editDate}>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Od</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="date" id="password-input" value={this.state.startDate}   onChange={(event) => this.startDateValidation(event.target.value)} name="password-input" placeholder="od" autoComplete="new-password" />
                        <FormText color="danger">{this.state.startDateText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Do</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="date" id="date-input" value={this.state.endDate}   onChange={(event) => this.endDateValidation(event.target.value)} name="date-input" placeholder="do" />
                        <FormText color="danger">{this.state.endDateText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                    <FormGroup row>
                    <Col xs="12" md="9">
                        <Input disabled = {true} type="select" value={this.state.cjenovnikId}
                               onChange={(e) => {
                                 this.cjenovnikValidation(e.target.value)
                               }}>
                          {this.state.cjenovnici.map((c) => <option key={c.id} value={c.id} >Po danu:{c.priceDay} DC:{c.collisionDW} Popust na 20 dana:{c.discount20}% Popust na 30 dana:{c.discount30}%  </option>)}
                        </Input>
                      </Col>
                    </FormGroup>
                </Row>
                 <FormGroup >
                    <Col md="3"><Label>Collision Damage Waiver</Label></Col>
                    <Col md="9">
                      <FormGroup className="checkbox">
                        <Input  type="checkbox" onChange={this.collisionValidation} checked={this.state.collision} name="collision" value="collision" />
                        <Col md="3">
                          <Label check className="form-check-label" htmlFor="checkbox1">Da </Label>
                        </Col>
                      </FormGroup>
                    </Col>
                  </FormGroup>
            </Form>
            </CardBody>
            <CardFooter>
              <FormText color="danger">{this.state.oglasiText}</FormText>

              <Button  type="submit" size="sm" color="primary" onClick={(e) => this.editAd(e)}><i className="fa fa-dot-circle-o"></i> Edit</Button>
              <Button type="reset" size="sm" color="danger" onClick={(e) => this.reset(e)}><i className="fa fa-ban"></i> Cancel</Button>
            </CardFooter>
          </Card>
          </Col>

          <Row>
      <Col xs="12" md="12">
                <Card>
                  <CardHeader>
                    <strong>Pricelists</strong>
                  </CardHeader>
                  <CardBody>
                    <div>
                      <section className="bar pt-0">
                        <div className="row">
                          <div className="col-md-12">

                            <div className="box mt-0 mb-lg-0">
                              <div className="table-responsive">
                                <table className="table table-hover">
                                  <thead>
                                  <tr>
                                    <th className="text-primary font-weight-bold">Day price</th>
                                    <th className="text-primary font-weight-bold">Collision DW</th>
                                    <th className="text-primary font-weight-bold">Discount 20 days</th>
                                    <th className="text-primary font-weight-bold">Discount 30 days</th>
                                    <th className="text-primary font-weight-bold">Exceed mileage</th>
                                  </tr>
                                  </thead>

                                  <tbody>
                                  {this.state.showpricelist.map(price => (
                                    <tr key={price.id}>
                                      <td>{price.priceDay} RSD</td>
                                      <td>{price.collisionDW} RSD</td>
                                      <td>{price.discount20} %</td>
                                      <td>{price.discount30} %</td>
                                      <td>{price.exceedMileage} RSD</td>
                                      <td><Button block className="btn-round" color="info"
                                                   onClick={(e) => this.cjenovnikValidation(price.id, e)}>Select</Button>
                                      </td>
                                    </tr>
                                  ))}

                                  </tbody>
                                </table>
                              </div>
                            </div>
                          </div>
                        </div>
                      </section>
                    </div>
                  </CardBody>
                  <CardFooter>
                    <nav>
                      <Pagination>
                        <PaginationItem><PaginationLink disabled={this.state.activePage === 1} previous tag="button"
                                                        onClick={(event) => this.handlePageChange(this.state.activePage - 1)}></PaginationLink></PaginationItem>
                        {pageNumbers.map((pageNumber) =>
                          <div>
                            <PaginationItem active hidden={this.state.activePage !== pageNumber + 1}>
                              <PaginationLink tag="button">{pageNumber + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem hidden={this.state.activePage === pageNumber + 1}>
                              <PaginationLink tag="button" onClick={(event) => this.handlePageChange(pageNumber + 1)}>
                                {pageNumber + 1}
                              </PaginationLink>
                            </PaginationItem>
                          </div>
                        )}
                        <PaginationItem><PaginationLink
                          disabled={this.state.activePage === (Math.ceil(len / pricelistPerPage))} next tag="button"
                          onClick={(event) => this.handlePageChange(this.state.activePage + 1)}></PaginationLink></PaginationItem>
                      </Pagination>
                    </nav>
                  </CardFooter>
                </Card>
              </Col>
              </Row>

        </Row>
        <NotificationContainer/>
      </div>
    );

    return this.rolesMatched() ? ret : <span>ivana</span>;
  }
}

export default Oglas;
