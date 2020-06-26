/*!

=========================================================
* Paper Dashboard React - v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Licensed under MIT (https://github.com/creativetimofficial/paper-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
// javascript plugin used to create scrollbars on windows
import PerfectScrollbar from "perfect-scrollbar";
import { Route, Switch, Redirect } from "react-router-dom";

import DemoNavbar from "components/Navbars/DemoNavbar.jsx";
import Footer from "components/Footer/Footer.jsx";
import Sidebar from "components/Sidebar/Sidebar.jsx";
import FixedPlugin from "components/FixedPlugin/FixedPlugin.jsx";
import axios from 'axios';
import {
  Container, Modal, ModalBody, ModalFooter,
  Button, Card, CardBody, CardFooter, Col, Form, Input, InputGroup, FormText,
  InputGroupAddon, InputGroupText, Row, FormGroup, Label, ModalHeader
} from 'reactstrap';
import icons from 'variables/icons';
import routes from "routes.js";
import SideBarroutes from "sidebar_routes.js";
import sellernavigation from "seller_routes.js";
import customernavigation from "customer_routes.js";
import adminnavigation from "admin_routes.js";

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';
var ps;

class Dashboard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      backgroundColor: "black",
      activeColor: "warning" ,
      username: "",
      password: "",
      formValid: true,   // enable/disable submit button depending on form validation
      notFilledError: true,
      formErrorText: "",
      passErrorText: "",
      usernameErrorText: "",
      changeFirstPass: false,
      password1: "",
      password11: "",
      password11Validation: "",
      password1Validation: "",
      modal: false,
      primary: false,
      hideInputMail: true,
      emailErrorText: "" ,
      formValidMail: true ,
      email: ""
    };
    this.mainPanel = React.createRef();
    this.togglePrimary = this.togglePrimary.bind(this);
    this.validateLoginFields = this.validateLoginFields.bind(this);
    this.Login = this.Login.bind(this);
    this.validateUsername = this.validateUsername.bind(this);
    this.signOut = this.signOut.bind(this);
    this.navig = this.navig.bind(this);
    this.validateEmail = this.validateEmail.bind(this)
    this.AccountRecovery = this.AccountRecovery.bind(this)
  }
  componentDidMount() {
    if (navigator.platform.indexOf("Win") > -1) {
      ps = new PerfectScrollbar(this.mainPanel.current);
      document.body.classList.toggle("perfect-scrollbar-on");
    }
  }
  componentWillUnmount() {
    if (navigator.platform.indexOf("Win") > -1) {
      ps.destroy();
      document.body.classList.toggle("perfect-scrollbar-on");
    }
  }
  componentDidUpdate(e) {
    if (e.history.action === "PUSH") {
      this.mainPanel.current.scrollTop = 0;
      document.scrollingElement.scrollTop = 0;
    }
  }

  validateEmail = (event) => {
    const regex = /\S+@\S+\.\S+/;
     if ( !regex.test(event.target.value) )   //email not appropriate
     {
        this.setState({formValidMail: true, emailErrorText: "Expected input: local@domain."})
     } else {
      this.setState({formValidMail: false, emailErrorText: ""})
     }
  }

  AccountRecovery = (event) => {
    let data = []
    data.push(this.state.email)
    axios({
      method: 'post',
      url: url + 'authpoint/auth/forgotPassword',
      data: data,
      ContentType: 'application/json'
    }).then((response) => {
      if (response.status === 200) {        
        //NotificationManager.success("Recovery email is sent.", 'Success!', 3000);        
      } else {
        //NotificationManager.error(response.data.accessToken, 'Email doesn\'t exist in our system!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });
  }

  togglePrimary() {
    this.setState({
      primary: !this.state.primary,
    });

 /* let id = 1;
    axios({
      method: 'get',
      url: url + 'authpoint/dash/verify/' + id,
    }).then((response) => {
      if (response.status === 200) {
      console.log("AOSKNDIJSABDJIASJKD");
      } else {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });*/

  }

  signOut(e) {
    e.preventDefault()
    localStorage.removeItem("ulogovan");
    localStorage.removeItem("role");
    localStorage.removeItem("start");
    localStorage.removeItem("end");
    localStorage.removeItem("name");
    sessionStorage.removeItem('basket');
    this.props.history.push('/ads')
  }

  Login = e => {
    console.log("ASDAS");
    this.setState({primary: true});


      this.cleanAll();
      this.togglePrimary();
  };



  validateLoginFields = () => {
    if (this.state.username === "" || this.state.password === "") {
      this.setState({notFilledError: true, formValid: true});
      return true;
    } else {
      this.setState({notFilledError: false, formValid: false});
      return false;
    }
  };

  validateUsername = () => {
    let regex = /^[A-Za-z0-9\-\_]+/;
    if (this.state.username === "") {
      this.setState({usernameErrorText: "Username is required.", formValid: true, formErrorText: ""});
    } else if (!regex.test(this.state.username)) {
      this.setState({usernameErrorText: "Alfanumerics, _ and - are only allowed.", formValid: true, formErrorText: ""});
    } else {
      this.setState({usernameErrorText: "", formErrorText: "", formValid: false});
    }


  };

  cleanAll = () => this.setState({
    registerShow: false, password: "", formValid: true, usernameErrorText: "",
    notFilledError: false, formErrorText: "", passErrorText: "", username: ""
  });

  sendLogin = event => {
    event.preventDefault();

    if (this.validateLoginFields()) {
      this.setState({formErrorText: "Both fields are required.", formValid: true})
    } else {
      this.setState({formValid: false})
      let data = {
        "username": this.state.username,
        "password": this.state.password
      };

      axios({
        method: 'post',
        url: url + 'authpoint/auth/login',
        data: data,
        ContentType: 'application/json'
      }).then((response) => {
        if (response.status === 200) {
          console.log(response.data);
          localStorage.setItem("ulogovan", response.data.accessToken);
          localStorage.setItem("role", response.data.role);
          localStorage.setItem("name", response.data.name);
          console.log(response.data);
          let array = [] ;
          sessionStorage.setItem("basket", JSON.stringify(array));
          //NotificationManager.success("Successfully logged in", 'Success!', 3000);
          if (response.data.role === "ROLE_ADMIN") {
            this.props.history.push('/dashboard');
          }else{
            this.props.history.push('/ads');
          }

        } else {
          //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
        }

      }, (error) => {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

      });


      this.cleanAll();
      this.togglePrimary();
    }
  };

  navig = () => {
    if (localStorage.getItem("role") === "ROLE_ADMIN") {
      return adminnavigation;
    }else if (localStorage.getItem("role") === "ROLE_SELLER") {
      return sellernavigation;
    }else if (localStorage.getItem("role") === "ROLE_CUSTOMER") {
      return customernavigation;
    } else {
      return SideBarroutes;
    }

  }

  render() {
    return (
      <div className="wrapper">
        <Sidebar
          {...this.props}
          isOpen={false}
          routes={this.navig()}
          bgColor={this.state.backgroundColor}
          activeColor={this.state.activeColor}
        />
        <div className="main-panel" ref={this.mainPanel}>
          <DemoNavbar openModal={e => this.Login(e)} odjava={e => this.signOut(e)} {...this.props} />
          <Switch>
            {routes.map((prop, key) => {
              return (
                <Route
                  authorize={prop.authorize}
                  path={prop.path}
                  component={prop.component}
                  key={key}
                />
              );
            })}
            {/*<Redirect from="/" to="/ads"/>*/}
          </Switch>
          
        </div>
        <Modal isOpen={this.state.primary} toggle={this.togglePrimary}
               className={'modal-primary ' + this.props.className}>
          <ModalHeader>Sign in to your account</ModalHeader>
          <ModalBody>
            <Form>

              <InputGroup className="mb-3 ">
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="nc-icon nc-single-02"></i>
                  </InputGroupText>
                </InputGroupAddon>
                <Input type="text" placeholder="Username" autoComplete="username" value={this.state.username}
                       onChange={event => this.setState({username: event.target.value})}
                       onBlur={this.validateUsername}/>
                <FormText color="danger">{this.state.usernameErrorText}</FormText>
              </InputGroup>
              <InputGroup className="mb-4 ">
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="nc-icon nc-key-25"></i>
                  </InputGroupText>
                </InputGroupAddon>
                <Input type="password" placeholder="Password" value={this.state.password}
                       onChange={event => this.setState({password: event.target.value})}/>
                <FormText color="danger">{this.state.passErrorText}</FormText>
              </InputGroup>              
              <Row>
                <FormText color="danger">{this.state.formErrorText}</FormText>
              </Row>
              <Label color="info" hidden={this.state.hideInputMail}>Enter your email and we will send you recovery details:</Label>
              <InputGroup className="mb-4" hidden={this.state.hideInputMail}>              
                <InputGroupAddon addonType="prepend">
                <InputGroupText>@</InputGroupText>
                </InputGroupAddon>
                <Input type="text" placeholder="email" value={this.state.email}
                       onChange={event => this.setState({email: event.target.value})}
                       onBlur={this.validateEmail}/>
                <FormText color="danger">{this.state.emailErrorText}</FormText>
              </InputGroup>
              <Row>
                <FormText color="danger">{this.state.formErrorText}</FormText>
              </Row>
              <Row className="text-center">
                <div class="text-center">
                <Button color="link" size="sm" onClick={event => this.setState({hideInputMail: !this.state.hideInputMail})}>Forgot password? </Button> 
                </div>
              </Row>
            </Form>
          </ModalBody>
          <ModalFooter>
          {!this.state.hideInputMail && <Button color="primary" onClick={this.AccountRecovery} disabled={this.state.formValidMail}>Send email</Button>}{' '}
                {this.state.hideInputMail && <Button color="primary" onClick={this.sendLogin} disabled={this.state.formValid}>Log in</Button>}{' '}
            <Button color="danger" onClick={this.togglePrimary}>Cancel</Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }
}

export default Dashboard;
