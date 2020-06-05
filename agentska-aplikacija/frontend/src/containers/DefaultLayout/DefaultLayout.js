import React, {Component, Suspense} from 'react';
import {RouterGuard} from 'react-router-guard';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import {Redirect} from 'react-router-dom';
import * as router from 'react-router-dom';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
import axios from 'axios';
import {
  Container, Modal, ModalBody, ModalFooter,
  Button, Card, CardBody, CardFooter, Col, Form, Input, InputGroup, FormText,
  InputGroupAddon, InputGroupText, Row, FormGroup, Label, ModalHeader
} from 'reactstrap';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import {
  AppAside,
  AppFooter,
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
const DefaultFooter = React.lazy(() => import('./DefaultFooter'));
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
      primary: false
    };

    this.togglePrimary = this.togglePrimary.bind(this);
    this.validateLoginFields = this.validateLoginFields.bind(this);
    this.validateUsername = this.validateUsername.bind(this);
    this.navig = this.navig.bind(this);
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
  })

  sendLogin = event => {
    event.preventDefault();
    console.log("ASDASASASD");

    if (this.validateLoginFields()) {
      this.setState({formErrorText: "Both fields are required.", formValid: true})
    } else {
      this.setState({formValid: false})
      let data = {
        "username": this.state.username,
        "password": this.state.password
      };
      console.log("ASDASASASD");
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
    this.props.history.push('/oglasi')
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
                    console.log(route.authorize);
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
              <Row>
                <FormText color="danger">{this.state.formErrorText}</FormText>
              </Row>
            </Form>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onClick={this.sendLogin} disabled={this.state.formValid}>Log in</Button>{' '}
            <Button color="secondary" onClick={this.togglePrimary}>Cancel</Button>
          </ModalFooter>
        </Modal>
        <NotificationContainer/>
      </div>
    );
  }
}

export default DefaultLayout;
