import React, { Component } from 'react';
import { RoleAwareComponent } from 'react-router-role-authorization';
import axios from 'axios';
import {Redirect} from 'react-router-dom';
import {
  Button,
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
  Pagination, PaginationItem, PaginationLink
} from 'reactstrap';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../node_modules/react-notifications/lib/notifications.css"
import "../../node_modules/react-notifications/lib/Notifications.js"

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';
const pricelistPerPage = 5;

class Pricelist extends RoleAwareComponent {

  constructor(props) {
    super(props);

    this.state = {
      cijenaPoDanu:"",
      popustZaDC:"",
      popust30: "",
      popust20:"",
      Text20:"",
      Text30:"",
      dcText:"",
      danText:"",
      collapse: true,
      fadeIn: true,
      timeout: 300,
      pricelists: [],
      exceed: "",
      exceedText:"",
      showpricelist:[],
      hideEdit:true,
      editCijenaPoDanu:"",
      editPopustZaDC:"",
      editPopust30: "",
      editPopust20:"",
      editExceed: "",
      editTextDay:"",
      editText20:"",
      editText30:"",
      editTextDC:"",
      ediTextEx:"",
      editId:"",
    };

    let arr = [];
    arr.push(localStorage.getItem('role'));    
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_SELLER', 'ROLE_ADMIN'];

    this.editPricelist = this.editPricelist.bind(this);

  }

  componentDidMount()
  {
    this.getPricelist();
  }

   
edit(e){
  e.preventDefault();
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  let ok = true;
  ok = this.editCijenaPoDanuValidation(this.state.editCijenaPoDanu);
  ok = this.editDcValidation(this.state.editPopustZaDC) && ok;
  ok = this.editPopust20Validation(this.state.editPopust20) && ok;
  ok = this.editPopust30Validation(this.state.editPopust30) && ok;
  ok = this.editExceedValidation(this.state.editExcees) && ok;
  if(ok){
    let data = {
      'priceDay': this.state.editCijenaPoDanu,
      'collisionDW': this.state.editPopustZaDC,
      'discount20': this.state.editPopust20,
      'discount30': this.state.editPopust30,
      'exceedMileage': this.state.editExceed,
    }
    axios({
      method: 'put',
      url: url + 'advertisement/pricelist/' + this.state.editId,
      data: data,
      headers: { "Authorization": AuthStr } ,       
    }).then((response)=>{ 
      console.log(response.status);
      this.resetEdit();
      let temp = this.state.pricelists;
      temp.push(response.data);
      this.setState({pricelists: temp});
      NotificationManager.success("Successfully edited!", '', 3000);     
    },(error)=>{
      console.log(error);
    });
  }
  else{
      this.resetEdit();
      }
  }
  

  getPricelist = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'advertisement/pricelist', 
      headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status === 200){
        this.setState({ pricelists: response.data, showpricelist:response.data.slice(0, pricelistPerPage) })
        // dodati setovanje prvog
      }
    }, (error) => {
      console.log(error);
    });
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

  resetEdit(){
  this.setState({hideEdit:true, editCijenaPoDanu:"", editPopust20:"", editPopust30:"", editPopustZaDC:"", editText30:"", ediTextEx:"", editText20:"", editTextDC:"", editTextDay: "", ediTextEx: ""})
}

editPricelist(id, day, collision, dis20, dis30, exceed, e ){
  this.setState({hideEdit:false, editCijenaPoDanu:day, editPopust20:dis20, editPopust30:dis30,
  editExceed:exceed, editPopustZaDC:collision, editId:id});
  };
  
  deletePricelist(id,e){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'delete',
      url: url + 'advertisement/pricelist/' + id,
      headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      console.log(response);
      let temp = this.state.pricelists;
      temp.filter(item => item.id === id);
        this.setState({pricelists:temp})
        NotificationManager.success("Successfully deleted!", '', 3000);     
    }, (error) => {
      console.log(error);
    });
  }

  editExceedValidation(c) {
    this.setState({editExceed: c});
    let sun = c;
    if (sun === '' ) {
      this.setState({ediTextEx: "Ovo polje ne moze biti prazno"});
      return false;
    } 
    else if(sun <0){
        this.setState({ediTextEx: "Cijena mora biti pozitivan broj"});
        return false;
    }else {
      this.setState({ediTextEx: ""});
      return true;
    }
  }
  
  editCijenaPoDanuValidation(c) {
      this.setState({editCijenaPoDanu: c});
      let sun = c;
      if (sun === '' ) {
        this.setState({editTextDay: "Ovo polje ne moze biti prazno"});
        return false;
      }
      else if(sun <0){
          this.setState({editTextDay: "Cijena mora biti pozitivan broj"});
          return false;
      }else {
        this.setState({editTextDay: ""});
        return true;
      }
  }
  
  editDcValidation(c) {
      this.setState({editPopustZaDC: c});
      let sun = c;
      if (sun === '') {
        this.setState({editTextDC: "Ovo polje ne moze biti prazno"});
        return false;
      }
      else if(sun <0 || sun >100){
          this.setState({editTextDC: "Popust mora biti u rasponu 0-100"});
          return false;
      }else {
        this.setState({editTextDC: ""});
        return true;
      }
  }
  
  editPopust20Validation(c) {
      this.setState({editPopust20: c});
      let sun = c;
      if (sun === '') {
        this.setState({editText20: "Ovo polje ne moze biti prazno"});
        return false;
      }
      else if(sun <0 || sun >100){
          this.setState({editText20: "Popust mora biti u rasponu 0-100"});
          return false;
      }else {
        this.setState({editText20: ""});
        return true;
      }
  }
  
  editPopust30Validation(c) {
      this.setState({editPopust30: c});
      let sun = c;
      if (sun === '') {
        this.setState({editText30: "Ovo polje ne moze biti prazno"});
        return false;
      }
      else if(sun <0 || sun >100){
          this.setState({editText30: "Popust mora biti u rasponu 0-100"});
          return false;
      }else {
        this.setState({editText30: ""});
        return true;
      }
  }
 

submit(e){
  e.preventDefault();
  let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
        let ok = true;
        ok = this.cijenaPoDanuValidation(this.state.cijenaPoDanu);
        ok = this.dcValidation(this.state.popustZaDC) && ok;
        ok = this.popust20Validation(this.state.popust20) && ok;
        ok = this.popust30Validation(this.state.popust30) && ok;
        ok = this.exceedValidation(this.state.excees) && ok;
        if(ok)
{
    let data = {
        'priceDay': this.state.cijenaPoDanu,
        'collisionDW': this.state.popustZaDC,
        'discount20': this.state.popust20,
        'discount30': this.state.popust30,
        'exceedMileage': this.state.exceed,
      }
      axios({
        method: 'post',
        url: url + 'advertisement/pricelist', 
        data: data,
        headers: { "Authorization": AuthStr } ,       
      }).then((response)=>{ 
        console.log(response.status);
        let temp = this.state.pricelists;
        temp.push(response.data);
        this.setState({pricelists: temp});
      this.setState({showpricelist: temp.slice(this.state.activePage*pricelistPerPage, this.state.activePage*pricelistPerPage + pricelistPerPage) })
        this.reset();
        NotificationManager.success("Successfully added!", '', 3000);     
      },(error)=>{
        console.log(error);
      });
} 
else{
    this.reset();
    }
}

reset(){
  this.setState({cijenaPoDanu:"", popust20:"", popust30:"", popustZaDC:"", Text20:"", Text30:"", dcText:"", danText:"", exceed: "", exceedText: ""})
}

cijenaPoDanuValidation(c) {
    this.setState({cijenaPoDanu: c});
    let sun = c;
    if (sun === '' ) {
      this.setState({danText: "Ovo polje ne moze biti prazno"});
      return false;
    } 
    else if(sun <0){
        this.setState({danText: "Cijena mora biti pozitivan broj"});
        return false;
    }else {
      this.setState({danText: ""});
      return true;
    }
}

exceedValidation(c) {
  this.setState({exceed: c});
  let sun = c;
  if (sun === '' ) {
    this.setState({exceedText: "Ovo polje ne moze biti prazno"});
    return false;
  } 
  else if(sun <0){
      this.setState({exceedText: "Cijena mora biti pozitivan broj"});
      return false;
  }else {
    this.setState({exceedText: ""});
    return true;
  }
}

dcValidation(c) {
    this.setState({popustZaDC: c});
    let sun = c;
    if (sun === '') {
      this.setState({dcText: "Ovo polje ne moze biti prazno"});
      return false;
    } 
    else if(sun <0 || sun >100){
        this.setState({dcText: "Popust mora biti u rasponu 0-100"});
        return false;
    }else {
      this.setState({dcText: ""});
      return true;
    }
}

popust20Validation(c) {
    this.setState({popust20: c});
    let sun = c;
    if (sun === '') {
      this.setState({Text20: "Ovo polje ne moze biti prazno"});
      return false;
    } 
    else if(sun <0 || sun >100){
        this.setState({Text20: "Popust mora biti u rasponu 0-100"});
        return false;
    }else {
      this.setState({Text20: ""});
      return true;
    }
}

popust30Validation(c) {
    this.setState({popust30: c});
    let sun = c;
    if (sun === '') {
      this.setState({Text30: "Ovo polje ne moze biti prazno"});
      return false;
    } 
    else if(sun <0 || sun >100){
        this.setState({Text30: "Popust mora biti u rasponu 0-100"});
        return false;
    }else {
      this.setState({Text30: ""});
      return true;
    }
}

  render() {
    const len = this.state.pricelists.length;
    const pageNumbers = Array.from(Array(Math.ceil(len / pricelistPerPage)).keys())

    
        let ret = (<div className="content">

      <div className="animated fadeIn">

        
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
                                                   onClick={(e) => this.editPricelist(price.id, price.priceDay, price.collisionDW,
                                                   price.discount20, price.discount30, price.exceedMileage, e)}>Edit</Button>
                                      </td>
                                      <td><Button block className="btn-round" color="info"
                                                   onClick={(e) => this.deletePricelist(price.id, e)}>Delete</Button>
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
              <Row>
          <Col xs="12" md="6">
            <Card>
              <CardHeader>
                <strong>Dodavanje novog cjenovnika</strong> 
              </CardHeader>
              <CardBody>
                <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
                  <Row >
                    <Col>
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="text-input">Cijena po danu</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="number" id="text-input" value = {this.state.cijenaPoDanu} name="text-input" placeholder="... KM" onChange={(event) => this.cijenaPoDanuValidation(event.target.value)} />
                      <FormText color="danger">{this.state.danText}</FormText>
                    </Col>
                  </FormGroup>
                  </Col>
                  <Col> 
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="email-input">Popust za Demage Collision</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="number" id="email-input" value = {this.state.popustZaDC} name="email-input" placeholder="%" onChange={(event) => this.dcValidation(event.target.value)}/> 
                      <FormText color="danger">{this.state.dcText}</FormText>
                    </Col>
                  </FormGroup>
                  </Col>
                  </Row>
                  <Row > 
                    <Col> 
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password-input">Popus za preko 20 dana</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="number" id="password-input" value = {this.state.popust20} name="password-input" onChange={(event) => this.popust20Validation(event.target.value)} placeholder="%" autoComplete="new-password" /> 
                      <FormText color="danger">{this.state.Text20}</FormText>
                    </Col>
                  </FormGroup>
                  </Col>
                  <Col> 
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password-input">Popus za preko 30 dana</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="number" id="password-input" value = {this.state.popust30} name="password-input" onChange={(event) => this.popust30Validation(event.target.value)} placeholder="%" autoComplete="new-password" /> 
                      <FormText color="danger">{this.state.Text30}</FormText>
                    </Col>
                  </FormGroup>
                  </Col>
                  </Row>
                  <Row > 
                    <Col> 
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password-input">Dodatno placanje za prekoracenje</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="number" id="password-input" value = {this.state.exceed} name="password-input" onChange={(event) => this.exceedValidation(event.target.value)} placeholder="... RSD" autoComplete="new-password" /> 
                      <FormText color="danger">{this.state.exceedText}</FormText>
                    </Col>
                  </FormGroup>
                  </Col>
                  </Row>
                    <br> 
                    </br>
                </Form>
              </CardBody>
              <CardFooter>
              <Button type="submit" size="sm" color="primary" onClick={(e) => this.submit(e)}><i className="fa fa-dot-circle-o"></i> Dodaj cjenovnik</Button>
              <Button type="reset" size="sm" color="danger" onClick={(e) => this.reset()}><i className="fa fa-ban"></i> Ponisti</Button>
              </CardFooter>
            </Card>
          </Col>

        

          <Col xs="12" md="6" hidden ={this.state.hideEdit}> 
        <Card>
            <CardHeader>
              <strong>Edit pricelist</strong>
            </CardHeader>
            <CardBody>
              <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="text-input">Cijena po danu</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="text-input" value = {this.state.editCijenaPoDanu} name="text-input" placeholder="... KM" onChange={(event) => this.editCijenaPoDanuValidation(event.target.value)} />
                        <FormText color="danger">{this.state.editTextDay}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="email-input">Popust za Demage Collision</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="email-input" value = {this.state.editPopustZaDC} name="email-input" placeholder="%" onChange={(event) => this.editDcValidation(event.target.value)}/>
                        <FormText color="danger">{this.state.editTextDC}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Popus za preko 20 dana</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="password-input" value = {this.state.editPopust20} name="password-input" onChange={(event) => this.editPopust20Validation(event.target.value)} placeholder="%" autoComplete="new-password" />
                        <FormText color="danger">{this.state.editText20}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Popus za preko 30 dana</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="password-input" value = {this.state.editPopust30} name="password-input" onChange={(event) => this.editPopust30Validation(event.target.value)} placeholder="%" autoComplete="new-password" />
                        <FormText color="danger">{this.state.editText30}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row > 
                    <Col> 
                  <FormGroup row>
                    <Col md="3">
                      <Label htmlFor="password-input">Dodatno placanje za prekoracenje</Label>
                    </Col>
                    <Col xs="12" md="9">
                      <Input type="number" id="password-input" value = {this.state.editExceed} name="password-input" onChange={(event) => this.editExceedValidation(event.target.value)} placeholder="... RSD" autoComplete="new-password" /> 
                      <FormText color="danger">{this.state.ediTextEx}</FormText>
                    </Col>
                  </FormGroup>
                  </Col>
                  </Row>
                <br>
                </br>
              </Form>
            </CardBody>
            <CardFooter>
              <Button type="submit" size="sm" color="primary" onClick={(e) => this.edit(e)}><i className="fa fa-dot-circle-o"></i> Edit</Button>
              <Button type="reset" size="sm" color="danger" onClick={(e) => this.resetEdit()}><i className="fa fa-ban"></i> Cancel</Button>
            </CardFooter>
          </Card>
        </Col>
  

        </Row>
      </div>
      </div>);
    return this.rolesMatched() ? ret : <Redirect to="/ads" />;
  }
}

export default Pricelist;
