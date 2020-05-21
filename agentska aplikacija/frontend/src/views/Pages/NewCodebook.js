import React, { Component } from 'react';
import axios from 'axios';
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

class Codebook extends Component {

  constructor(props) {
    super(props);

    this.state = {
      tip: "brand",
      sifra: "",
      naziv:"",
      activeTab: new Array(6).fill('1'),
      codebook: []
    };

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
                    </tr>
                    )}     
                  </tbody>
                </Table>
              </CardBody>
            </Card>
      </>
    )
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
        }
        axios({
          method: 'post',
          url: url + 'codebook', 
          data: data,
         // headers: { "Authorization": AuthStr } ,       
        }).then((response)=>{ 
          console.log(response.status);
          this.reset();
          let temp = this.state.codebook;
          temp.push(response.data);
          this.setState({codebook: temp});
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
    axios({
      method: 'get',
      url: url + 'codebook',
      // headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status === 200){
        this.setState({ codebook: response.data });
        // dodati setovanje prvog
      }
    }, (error) => {
      console.log(error);
    });
  }
  
  componentDidMount()
  {
    this.getCodebook();
  }

  reset(){
      this.setState({tip:"brand", sifra:"", naziv:"", tipText:"", sifraText:"", nazivText:""})
  }

  tipValidation(e){
    this.setState({tip:e})
    console.log(e);
  }

  
nazivValidation(c) {
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

sifraValidation(c) {
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

  render() {
    return (
      <div className="animated fadeIn">

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
      </div>
    );
  }
}

export default Codebook;
