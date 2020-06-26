import React, {Component, Suspense} from 'react';
import {Route, Switch} from 'react-router-dom';
import {Redirect} from 'react-router-dom';
import * as router from 'react-router-dom';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
import axios from 'axios';
import {
  Container, Modal, ModalBody, ModalFooter, Label,
  Button, Form, Input, InputGroup, FormText,
  InputGroupAddon, InputGroupText, Row, ModalHeader
} from 'reactstrap';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import {
  AppAside,
  AppHeader,
  AppSidebar,
  AppSidebarFooter,
  AppSidebarForm,
  AppSidebarHeader,
  AppSidebarMinimizer,
  AppSidebarNav2 as AppSidebarNav,
} from '@coreui/react';
// sidebar nav config
import navigation from '../../_nav';
import adminnavigation from '../../nav_admin';
import customernavigation from '../../nav_customer';
import sellernavigation from '../../nav_seller';
// routes config
import routes from '../../routes';
//import config from '../../config';
import '../../scss/vendors/custom.css';

const DefaultAside = React.lazy(() => import('./DefaultAside'));
const DefaultHeader = React.lazy(() => import('./DefaultHeader'));

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

class DefaultLayout extends Component {
  constructor(props) {
    super(props);
    this.state = {
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
      primary: false ,
      hideInputMail: true,
      emailErrorText: "" ,
      formValidMail: true ,
      email: ""
    };

    this.togglePrimary = this.togglePrimary.bind(this);
    this.validateLoginFields = this.validateLoginFields.bind(this);
    this.validateUsername = this.validateUsername.bind(this);
    this.navig = this.navig.bind(this);
    this.validateEmail = this.validateEmail.bind(this)
    this.AccountRecovery = this.AccountRecovery.bind(this)
  }

  login = e => {
    console.log("ASDAS");
    this.setState({primary: true})
  };

  togglePrimary() {
    this.setState({
      primary: !this.state.primary,
    });
  }

  AccountRecovery = (event) => {
    let data = []
    data.push(this.state.email)
    axios({
      method: 'post',
      url: url + 'user/forgotPassword',
      data: data,
      ContentType: 'application/json'
    }).then((response) => {
      if (response.status === 200) {        
        NotificationManager.success("Recovery email is sent.", 'Success!', 3000); 
        this.setState({hideInputMail: !this.state.hideInputMail});       
      } else {
        NotificationManager.error(response.data.accessToken, 'Email doesn\'t exist in our system!', 3000);
      }

    }, (error) => {
      //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

    });
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
    let regex = /^[A-Za-z0-9\-_]+/;
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
  })

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
        url: url + 'auth/login',
        data: data,
        ContentType: 'application/json'
      }).then((response) => {
        if (response.status === 200) {
          localStorage.setItem("ulogovan", response.data.accessToken);
          localStorage.setItem("role", response.data.role);
          let array = [] ;
          sessionStorage.setItem("basket", JSON.stringify(array));
          NotificationManager.success("Successfully logged in", 'Success!', 3000);
          this.props.history.push('/oglasi')
        } else {
          NotificationManager.error(response.data.accessToken, 'Error!', 3000);
        }

      }, (error) => {
        //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

      });

      this.cleanAll();
      this.togglePrimary();
    }
  };


  loading = () => <div className="animated fadeIn pt-1 text-center">Loading...</div>

  signOut(e) {
    e.preventDefault()
    localStorage.removeItem("ulogovan");
    localStorage.removeItem("role");
    localStorage.removeItem("start");
    localStorage.removeItem("end");
    sessionStorage.removeItem('basket');
    if (window.location != "https://localhost:3000/oglasi") {
      this.props.history.push('/oglasi');
    }else {
      window.location.reload();
    }
    
   
  }

  navig = () => {
    if (localStorage.getItem("role") === "ROLE_ADMIN") {
      return adminnavigation;
    }else if (localStorage.getItem("role") === "ROLE_SELLER") {
      return sellernavigation;
    }else if (localStorage.getItem("role") === "ROLE_CUSTOMER") {
      return customernavigation;
    } else {
      return navigation;
    }

  }



  navBar = () => {
    let ret = (<AppSidebar fixed display="lg">
      <AppSidebarHeader>&nbsp;</AppSidebarHeader>
      <AppSidebarForm/>
      <Suspense>
        <AppSidebarNav navConfig={this.navig()} {...this.props} router={router}/>
      </Suspense>
      <AppSidebarFooter/>
      <AppSidebarMinimizer/>
    </AppSidebar>);

    if (localStorage.getItem('role'))
      return ret;
    else
      return <div></div>;
  };

  render() {
    return (
      <div className="app">
        <AppHeader fixed>
          <Suspense fallback={this.loading()}>
            <DefaultHeader onLogout={e => this.signOut(e)} onLogin={e => this.login(e)}/>
          </Suspense>
        </AppHeader>
        <div className="app-body">
          {this.navBar()}
          <main className="main bgimg">
            <br></br>
            <Container fluid>

              <Suspense fallback={this.loading()}>

                <Switch>
                  {routes.map((route, idx) => {
                    return route.component ? (
                      <Route authorize={route.authorize}
                             key={idx}
                             path={route.path}
                             exact={route.exact}
                             name={route.name}
                             render={props => (
                               <route.component {...props} />
                               )}/>
                    ) : (null);
                  })}
                  <Redirect from="/" to="/oglasi"/>
                </Switch>

              </Suspense>
            </Container>
          </main>
          <AppAside fixed>
            <Suspense fallback={this.loading()}>
              <DefaultAside/>
            </Suspense>
          </AppAside>
        </div>
        <Modal isOpen={this.state.primary} toggle={this.togglePrimary}
               className={'modal-primary ' + this.props.className}>
          <ModalHeader>Sign in to your account</ModalHeader>
          <ModalBody>
            <Form>

              <InputGroup className="mb-3">
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="icon-user"></i>
                  </InputGroupText>
                </InputGroupAddon>
                <Input type="text" placeholder="Username" autoComplete="username" value={this.state.username}
                       onChange={event => this.setState({username: event.target.value})}
                       onBlur={this.validateUsername}/>
                <FormText color="danger">{this.state.usernameErrorText}</FormText>
              </InputGroup>
              <InputGroup className="mb-4">
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <i className="icon-lock"></i>
                  </InputGroupText>
                </InputGroupAddon>
                <Input type="password" placeholder="Password" value={this.state.password}
                       onChange={event => this.setState({password: event.target.value})}/>
                <FormText color="danger">{this.state.passErrorText}</FormText>
              </InputGroup>
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
                <Button color="link" onClick={event => this.setState({hideInputMail: !this.state.hideInputMail})}>Forgot password?</Button> 
                </div>
              </Row>
            </Form>
          </ModalBody>
          <ModalFooter>
            {!this.state.hideInputMail && <Button color="primary" onClick={this.AccountRecovery} disabled={this.state.formValidMail}>Send email</Button>}{' '}
                {this.state.hideInputMail && <Button color="primary" onClick={this.sendLogin} disabled={this.state.formValid}>Log in</Button>}{' '}
            <Button color="secondary" onClick={this.togglePrimary}>Cancel</Button>
          </ModalFooter>
        </Modal>
        <NotificationContainer/>
      </div>
    );
  }
}

export default DefaultLayout;
