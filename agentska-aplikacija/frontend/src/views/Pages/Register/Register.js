import React, { Component } from 'react';
import axios from 'axios';
import '../../../scss/vendors/custom.css';
import { Button, Card, CardBody, Col, Container, Form, Input, InputGroup, FormText,
  InputGroupAddon, InputGroupText, Row, FormGroup } from 'reactstrap';
  import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../../node_modules/react-notifications/lib/notifications.css"
import "../../../../node_modules/react-notifications/lib/Notifications.js"

  const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';


class Register extends Component {
  constructor(props)
  {
    super(props);
    this.state = {      
      username: "" ,      
      password: "", 
      name: "",
      surname: "",
      address: "",
      city: "", 
      email: "",      
      formValid: true  ,   // enable/disable submit button depending on form validation
      formUsernameText: "",
      formErrorText: "" ,
      passErrorText: "" ,  
      emailErrorText: "" ,      
      matchErrorText: "" ,  
      password1: ""
      
    };

    this.validatePassword = this.validatePassword.bind(this) ;
    this.validateFields = this.validateFields.bind(this);
  }

  validateFields = (event) => {
    if (this.state.username === "" || this.state.password === "" || this.state.name === ""
        || this.state.surname === "" || this.state.address === "" || this.state.city === ""
        || this.state.email === "" || this.state.password1 === "" )
    {
      this.setState({formValid:true, formErrorText: "All fields are required."});
      return true;
    }else {
      this.setState({formValid:false, formErrorText: ""});
      return false;
    }
  }

  validateUsername = () => {
    if (this.state.username.length < 6) {
      this.setState({formValid:true, formUsernameText: "Minimum length of username is 6."});
    } else {
      this.setState({formValid:false, formUsernameText: ""});
    }
  }
  
  validatePassword = () => {
    let upperCheck = false;
    let lowerCheck = false;
    let numCheck = false;
    if (this.state.password === "") {
      this.setState({passErrorText: "This field is required.", formErrorText: ""})
    }else if (this.state.password.length < 10 ) {
      this.setState({passErrorText: "Minimum length of password is 10 characters.", formErrorText: ""})
    }else {
      let i;
      for (i = 0 ; i <  this.state.password.length; i++) {
          if (!isNaN(this.state.password.charAt(i) * 1)) { // if it's number
          numCheck = true;
          continue;
        } else if (this.state.password.charAt(i) === this.state.password.charAt(i).toLowerCase()) {
          lowerCheck = true;
          continue;
        } else if (this.state.password.charAt(i) === this.state.password.charAt(i).toUpperCase()) {
          upperCheck = true;
          continue;
        }
      }
      
      if (!numCheck) {
        this.setState({passErrorText: "Password must contain at least one digit.", formErrorText: ""})
      } else if (!lowerCheck) {
        this.setState({passErrorText: "Password must contain at least one lowercase letter.", formErrorText: ""})
      } else if (!upperCheck) {
        this.setState({passErrorText: "Password must contain at least one capital letter.", formErrorText: ""})
      } else {
        this.setState({passErrorText: "", formValid: false, formErrorText: ""})
      }

      if (this.state.username !== "") {   
          let usrname = this.state.username.toLowerCase();
          let passchk = this.state.password.toLowerCase();
          if (passchk.includes(usrname)) {
            this.setState({passErrorText: "Password can not contain username.", formErrorText: ""})
          }
      }
              
    }
  }

  checkPasswordMatching = (event) => {
    if (this.state.password !== event.target.value)
      {
        this.setState({formValid: true, matchErrorText: "Password doesn't match."});    // disabling submit button
      } else {
        this.setState({formValid: false, matchErrorText: "", password1: event.target.value});
      }
  }

  validateEmail = (event) => {
    const regex = /\S+@\S+\.\S+/;
     if ( !regex.test(event.target.value) )   //email not appropriate
     {
        this.setState({formValid: true, emailErrorText: "Expected input: local@domain."})
     } else {
      this.setState({formValid: false, emailErrorText: ""})
     }
  }

cleanAll = () => this.setState({password: "", formValid: true  , name: "", surname: "", address: "" , email: "",
                                usernameErrorText: "", city: "", password1: "" , emailErrorText: "",
                                notFilledError: false , formErrorText: "" , passErrorText: "", username: ""})

  /*redirection = flag => {
    if (flag === 404) {
      this.props.history
    }
  } */ 

  sendRegistration = event => {
        event.preventDefault();
        this.validateFields();
        
        if (!this.state.formValid)
        {     

          let data = {
            "username": this.state.username ,            
            "email": this.state.email ,
            "password":this.state.password,
            "name": this.state.name ,
            "surname": this.state.surname ,            
            "address": this.state.address ,
            "city": this.state.city         
          };

          axios({
            method: 'post',
            url: url + 'auth/register',
            data: data ,
            ContentType: 'application/json'            
          }).then((response) => {
            if (response.status === 200){
              NotificationManager.success('Vas zahtev za registraciju je uspesno poslat!', 'Uspjesno!', 3000);
              this.props.history.push('/oglasi');
            } else if (response.status === 500) {
              this.props.history.push('/500');
            } else if (response.status === 404) {
              this.props.history.push('/404');
            }        
          }, (error) => {
            this.props.history.push('/404');
          });

          this.cleanAll();
          
      }
  };

  render() {
    return (
      <div className="app flex-row align-items-center bgimg">
        <Container>
          <Row className="justify-content-center">
            <Col md="16" lg="16" xl="16">
              <Card className="mx-16">
                <CardBody className="p-4">
                  <Form onSubmit={this.sendRegistration}>
                    <h1>Welcome to our rent-a-car service!</h1>
                    <p className="text-muted">Create your account - it's free</p>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-user"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" placeholder="Username" autoComplete="username" onBlur={this.validateUsername}
                            value={this.state.username} onChange={event => this.setState({username: event.target.value})} 
                            />
                      <FormText color="danger">{this.state.formUsernameText}</FormText>
                    </InputGroup>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>@</InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" placeholder="Email" autoComplete="email" 
                      value={this.state.email} onChange={event => this.setState({email: event.target.value})} 
                      onBlur={this.validateEmail} />
                      <FormText color="danger">{this.state.emailErrorText}</FormText>
                    </InputGroup>
                    <FormGroup row className="my-0">
                      <Col xs="6">
                      <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                        <i className="icon-user-following"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" placeholder="Name" autoComplete="name" 
                      value={this.state.name} onChange={event => this.setState({name: event.target.value})} />
                    </InputGroup>
                      </Col>
                      <Col xs="6">
                      <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                      <InputGroupText>
                        <i className="icon-user-following"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" placeholder="Surname" autoComplete="surname"
                      value={this.state.surname} onChange={event => this.setState({surname: event.target.value})}  />
                    </InputGroup>
                      </Col>
                    </FormGroup>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                        <i className="icon-notebook"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" placeholder="Address" autoComplete="address" 
                      value={this.state.address} onChange={event => this.setState({address: event.target.value})} />
                    </InputGroup>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-home"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="text" placeholder="City" autoComplete="city"
                       value={this.state.city} onChange={event => this.setState({city: event.target.value})}/>
                    </InputGroup>
                    <InputGroup className="mb-3">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-lock"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="password" placeholder="Password" autoComplete="new-password"
                      value={this.state.password} onChange={event => this.setState({password: event.target.value})} 
                      onBlur={this.validatePassword} />
                      <FormText color="danger">{this.state.passErrorText}</FormText>
                    </InputGroup>
                    <InputGroup className="mb-4">
                      <InputGroupAddon addonType="prepend">
                        <InputGroupText>
                          <i className="icon-lock"></i>
                        </InputGroupText>
                      </InputGroupAddon>
                      <Input type="password" placeholder="Repeat password" autoComplete="new-password"
                       onChange={this.checkPasswordMatching} 
                       />
                       <FormText color="danger">{this.state.matchErrorText}</FormText>
                    </InputGroup>
                    <Button disabled={this.state.formValid} color="success" block
                    
                    >Create Account</Button>
                    <FormText color="danger">{this.state.formErrorText}</FormText>
                  </Form>
                </CardBody>
                
              </Card>
            </Col>
          </Row>
        </Container>
        <NotificationContainer/>
      </div>
    );
  }
}

export default Register;
