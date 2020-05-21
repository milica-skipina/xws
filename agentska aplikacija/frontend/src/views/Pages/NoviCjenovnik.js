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
} from 'reactstrap';

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

class NoviCjenovnik extends Component {

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

    };

  }

  componentDidMount()
  {
  
  }

 

submit(e){
  e.preventDefault();
        let ok = true;
        ok = this.cijenaPoDanuValidation(this.state.cijenaPoDanu);
        ok = this.dcValidation(this.state.popustZaDC);
        ok = this.popust20Validation(this.state.popust20);
        ok = this.popust30Validation(this.popust30);
        if(ok)
{
    let data = {
        'priceDay': this.state.cijenaPoDanu,
        'discountDC': this.state.popustZaDC,
        'discount20': this.state.popust20,
        'discount30': this.state.popust30,
      }
      axios({
        method: 'post',
        url: url + 'pricelist', 
        data: data,
       // headers: { "Authorization": AuthStr } ,       
      }).then((response)=>{ 
        console.log(response.status);
        this.reset();
      },(error)=>{
        console.log(error);
      });
} 
else{
    this.reset();
    }
}

reset(){
  this.setState({cijenaPoDanu:"", popust20:"", popust30:"", popustZaDC:"", Text20:"", Text30:"", dcText:"", danText:""})
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
    return (
      <div className="animated fadeIn">

        <Row>
          <Col xs="12" md="6">
            <Card>
              <CardHeader>
                <strong>Dodavanje novog oglasa</strong> 
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
        </Row>
      </div>
    );
  }
}

export default NoviCjenovnik;
