import React, { Component } from 'react';
import axios from 'axios';
import { RoleAwareComponent } from 'react-router-role-authorization';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
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

class Codebook extends RoleAwareComponent {

  constructor(props) {
    super(props);

    this.state = {
      tip: "brand",
      sifra: "",
      naziv:"",
      activeTab: new Array(6).fill('1'),
      codebook: [],
      editName:"",
      editCode:"",
      editId:"",
      hideEdit: true,
    };

    this.edit = this.edit.bind(this);



    let arr = new Array();
    arr.push(localStorage.getItem('role'));
    console.log("KONS",arr);
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_ADMIN'];

    this.getCodebook = this.getCodebook.bind(this);
  }

  toggle(tabPane, tab) {
    const newArray = this.state.activeTab.slice()
    newArray[tabPane] = tab
    this.setState({
      activeTab: newArray,
    });
  }

  tabPane() {
    return (
      <>
        <TabPane tabId="1">
          { this.renderTable('brand')}
        </TabPane>
        <TabPane tabId="2">
          { this.renderTable('model')}
        </TabPane>
        <TabPane tabId="3">
          {this.renderTable('class')}
        </TabPane>
        <TabPane tabId="4">
          {this.renderTable('gearbox')}
        </TabPane>
        <TabPane tabId="5">
          {this.renderTable('fuel')}
        </TabPane>
      </>
    );
  }

  renderTable(codeType) {
    return (
      <>
      <Card>
              <CardHeader>
              </CardHeader>
              <CardBody>
                <Table responsive>
                  <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Code</th>

                  </tr>
                  </thead>
                  <tbody>
                  {(this.state.codebook.filter(item => item.codeType === codeType))
                  .map((code, index) =>
                  <tr key={codeType + '_' + code.name}>
                    <td>{index + 1}</td>
                    <td>{code.name}</td>
                    <td>{code.code}</td>
                    <td><Button block className="btn-round" color="info"
                          onClick={(e) => this.editCodebook(code.id, code.name, code.code, code.codeType, e)}>Edit</Button>
                    </td>
                    <td><Button block className="btn-round" color="info"
                         onClick={(e) => this.deleteCodebook(code.id, e)}>Delete</Button>
                    </td>
                    </tr>
                    )}
                  </tbody>
                </Table>
              </CardBody>
            </Card>
      </>
    )
  }

  editCodebook(id, name, code, tip, e ){
    this.setState({hideEdit:false, editCode:code, editName:name, editTip: tip,editId:id});
    };
    
  deleteCodebook(id,e){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
      axios({
        method: 'delete',
        url: url+ 'codebook/' + id,
        headers: { "Authorization": AuthStr },  
      }).then((response) => {
        console.log(response.status);
          let temp = this.state.codebook.filter(item=>item.id != id);
          this.setState({codebook: temp});
          this.reset();
          NotificationManager.success("Successfully deleted!", '', 3000);     
      }, (error) => {
        console.log(error);
      });
    }
    
    
  edit(e){
    e.preventDefault();
          let ok = this.editSifraValidation(this.state.editCode);
          ok = this.editNazivValidation(this.state.editName) && ok;
          if(ok)
  {
      let data = {
          'code': this.state.editCode,
          'name': this.state.editName,
          'codeType': this.state.editTip,
        };
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        axios({
          method: 'put',
          url: url + 'codebook/' + this.state.editId,
          data: data,
          headers: { "Authorization": AuthStr },  
        }).then((response)=>{
          console.log(response.status);
          let temp = this.state.codebook;
          temp = temp.filter(item=> item.id !== this.state.editId);
          temp.push(response.data);
          this.setState({hideEdit: true})
          this.setState({codebook: temp});
          this.reset();
          NotificationManager.success("Successfully edited!", '', 3000);     
        },(error)=>{
          console.log(error);
          this.resetEdit();
        });
  }
  else{
      this.resetEdit();
      }
  }

  submit(e){
    e.preventDefault();
          let ok = this.sifraValidation(this.state.sifra);
          ok = this.nazivValidation(this.state.naziv) && ok;
          if(ok)
  {
      let data = {
          'code': this.state.sifra,
          'name': this.state.naziv,
          'codeType': this.state.tip,
        };
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        axios({
          method: 'post',
          url: url + 'codebook',
          data: data,
          headers: { "Authorization": AuthStr } ,
        }).then((response)=>{
          console.log(response.status);
          this.reset();
          let temp = this.state.codebook;
          temp.push(response.data);
          this.setState({codebook: temp});
          NotificationManager.success("Successfully added!", '', 3000);     
        },(error)=>{
          console.log(error);
          this.reset();
        });
  }
  else{
      this.reset();
      }
  }

  getCodebook = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'codebook',
      headers: { "Authorization": AuthStr },  
    }).then((response) => {
      if (response.status === 200){
        this.setState({ codebook: response.data });
        // dodati setovanje prvog
      }
    }, (error) => {
      console.log(localStorage.getItem("ulogovan"));
      console.log(error);
    });
  };

  componentDidMount()
  {
    this.getCodebook();
  }

  reset(){
      this.setState({tip:"brand", sifra:"", naziv:"", tipText:"", sifraText:"", nazivText:""})
  }

  tipValidation(e){
    this.setState({tip:e});
    console.log(e);
  }

  editTipValidation(e){
    this.setState({editTip:e});
  }

nazivValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({naziv: c});
    let sun = c;
    let ret = true;
    if (sun === '') {
      this.setState({nazivText: "Ovo polje ne moze biti prazno"});
      console.log(c);
      ret = false;
      return ret;
    } else {
      this.setState({nazivText: ""});
      console.log(c)
      ret = true;
      return ret;
    }
}

editNazivValidation(c) {
  c = c.replace(/</g,'');
  c = c.replace(/>/g,'');
  c = c.replace(/script/g,'');
  c = c.replace(/alert/g,'');
  this.setState({editName: c});
  let sun = c;
  let ret = true;
  if (sun === '') {
    this.setState({editTextName: "Ovo polje ne moze biti prazno"});
    console.log(c);
    ret = false;
    return ret;
  } else {
    this.setState({editTextName: ""});
    console.log(c)
    ret = true;
    return ret;
  }
}

sifraValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({sifra: c});
    let sun = c;
    if (sun === '' || sun === undefined) {
      this.setState({sifraText: "Ovo polje ne moze biti prazno"});
      console.log(c)
      return false;
    } else {
      this.setState({sifraText: ""});
      console.log(c)
      return true;
    }
}

editSifraValidation(c) {
  c = c.replace(/</g,'');
  c = c.replace(/>/g,'');
  c = c.replace(/script/g,'');
  c = c.replace(/alert/g,'');
  this.setState({editCode: c});
  let sun = c;
  if (sun === '' || sun === undefined) {
    this.setState({editTextCode: "Ovo polje ne moze biti prazno"});
    console.log(c)
    return false;
  } else {
    this.setState({editTextCode: ""});
    console.log(c)
    return true;
  }
}

resetEdit(){
  this.setState({hideEdit:true, editCode:"", editName:"", editTip:"", editTextCode:"", editTextName:"", editeTextTip:""})
}

  render() {

    let ret = (<div className="animated fadeIn">

      <Row>

      <Col xs="12" md="6" className="mb-4">
          <Nav tabs>
            <NavItem>
              <NavLink
                active={this.state.activeTab[0] === '1'}
                onClick={() => { this.toggle(0, '1'); }}
              >
                Brands
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                active={this.state.activeTab[0] === '2'}
                onClick={() => { this.toggle(0, '2'); }}
              >
                Models
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                active={this.state.activeTab[0] === '3'}
                onClick={() => { this.toggle(0, '3'); }}
              >
                Car class
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                active={this.state.activeTab[0] === '4'}
                onClick={() => { this.toggle(0, '4'); }}
              >
                Gearbox types
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                active={this.state.activeTab[0] === '5'}
                onClick={() => { this.toggle(0, '5'); }}
              >
                Fuel types
              </NavLink>
            </NavItem>
          </Nav>
          <TabContent activeTab={this.state.activeTab[0]}>
            {this.tabPane()}
          </TabContent>
        </Col>
        </Row>
        <Row>
        <Col xs="12" md="6">
          <Card>
            <CardHeader>
              <strong>Dodavanje novog sifrarnika</strong>
            </CardHeader>
            <CardBody>
              <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="select">Izaberite tip</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="select"  value={this.state.tip}
                               onChange={(e) => {
                                 this.tipValidation(e.target.value)
                               }}>
                          <option value={"brand"}>Marka automobila</option>
                          <option value={"model"}>Model automobila</option>
                          <option value={"class"}>Klasa automobila</option>
                          <option value={"fuel"}>Vrsta goriva</option>
                          <option value={"gearbox"}>Tip mjenjaca</option>
                        </Input>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Sifra</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="text"  value = {this.state.sifra} name="password-input" onChange={(event) => this.sifraValidation(event.target.value)} placeholder="sifra" />
                        <FormText color="danger">{this.state.sifraText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Naziv</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="text" value = {this.state.naziv} name="password-input" onChange={(event) => this.nazivValidation(event.target.value)} placeholder="naziv" />
                        <FormText color="danger">{this.state.nazivText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <br>
                </br>
              </Form>
            </CardBody>
            <CardFooter>
              <Button type="submit" size="sm" color="primary" onClick={(e) => this.submit(e)}><i className="fa fa-dot-circle-o"></i> Dodaj sifrarnik</Button>
              <Button type="reset" size="sm" color="danger" onClick={(e) => this.reset()}><i className="fa fa-ban"></i> Ponisti</Button>
            </CardFooter>
          </Card>
        </Col>
        <Col xs="12" md="6" hidden = {this.state.hideEdit}>
          <Card>
            <CardHeader>
              <strong>Izmjena sifrarnika</strong>
            </CardHeader>
            <CardBody>
              <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="select">Izaberite tip</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input disabled= {true} type="select"  value={this.state.editTip}
                               onChange={(e) => {
                                 this.editTipValidation(e.target.value)
                               }}>
                          <option value={"brand"}>Marka automobila</option>
                          <option value={"model"}>Model automobila</option>
                          <option value={"class"}>Klasa automobila</option>
                          <option value={"fuel"}>Vrsta goriva</option>
                          <option value={"gearbox"}>Tip mjenjaca</option>
                        </Input>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Sifra</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="text"  value = {this.state.editCode} name="password-input" onChange={(event) => this.editSifraValidation(event.target.value)} placeholder="sifra" />
                        <FormText color="danger">{this.state.editSifraText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Naziv</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="text" value = {this.state.editName} name="password-input" onChange={(event) => this.editNazivValidation(event.target.value)} placeholder="naziv" />
                        <FormText color="danger">{this.state.editNazivText}</FormText>
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
              <Button type="reset" size="sm" color="danger" onClick={(e) => this.resetEdit(e)}><i className="fa fa-ban"></i> Cancel</Button>
            </CardFooter>
          </Card>
        </Col>
      </Row>
              <NotificationContainer/>

    </div>);

    return this.rolesMatched() ? ret : <span>ivana</span>;
  }
}

export default Codebook;
