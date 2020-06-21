import React from 'react';
import axios from 'axios';

import {
    Button,
    Col,
    Label,
    FormGroup,
    Input,
    Row,
    InputGroup,
    FormText,
} from 'reactstrap';
import RenderStatistics from './RenderStatistics';
import { RoleAwareComponent } from 'react-router-role-authorization';
import {Redirect} from 'react-router-dom';
//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

class Profile extends RoleAwareComponent {

    constructor(props) {
        super(props);
        this.state = {
            user: {},
            hideChangePass: true,
            oldPassword: "",
            password:"",
            passErrorText: "",
            bestRated: [],
            mostCrossed: [],
            mostCommented: []
        };

        let arr = [];
        arr.push(localStorage.getItem('role'));    
        this.userRoles = arr;
        this.allowedRoles = ['ROLE_CUSTOMER', 'ROLE_SELLER', 'ROLE_ADMIN'];

        this.getUser = this.getUser.bind(this);
        this.changePass = this.changePass.bind(this);
        this.validatePassword = this.validatePassword.bind(this) ;
        this.getStatistics = this.getStatistics.bind(this);
        this.getMostCommented = this.getMostCommented.bind(this);
        this.getBestRated = this.getBestRated.bind(this);
        this.getMostCrossed = this.getMostCrossed.bind(this);
    }

    componentDidMount() {
        this.getUser();
        let role = localStorage.getItem("role");
        if(role === "ROLE_SELLER"){
            this.getStatistics();
        }
    }

    getUser = () => {
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        axios({
            method: 'get',
            url: url + 'authpoint/user/current',
            headers: { "Authorization": AuthStr },
        }).then((response) => {
            this.setState({ user: response.data })
        }, (error) => {
            console.log(error);
        });
    }

    changePass(){
        alert("Implementiraj me!");
    }

      validatePassword = () => {
    let upperCheck = false;
    let lowerCheck = false;
    let numCheck = false;
    if (this.state.password === "") {
      this.setState({passErrorText: "This field is required.", formErrorText: ""})
    }else if (this.state.password.length < 8 ) {
      this.setState({passErrorText: "Minimum length of password is 8 characters.", formErrorText: ""})
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

  getStatistics(){
      this.getMostCommented();
      this.getBestRated();
      this.getMostCrossed();
  }

  getMostCommented(){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
        method: 'get',
        url: url + 'advertisement/car/statistics/comments',
        headers: { "Authorization": AuthStr },
    }).then((response) => {
        this.setState({ mostCommented: response.data })
    }, (error) => {
        console.log(error);
    });
  }

  getBestRated(){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
        method: 'get',
        url: url + 'advertisement/car/statistics/rating',
        headers: { "Authorization": AuthStr },
    }).then((response) => {
        this.setState({ bestRated: response.data })
    }, (error) => {
        console.log(error);
    });
  }

  getMostCrossed(){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
        method: 'get',
        url: url + 'orders/car/statistics',
        headers: { "Authorization": AuthStr },
    }).then((response) => {
        this.setState({ mostCrossed: response.data })
    }, (error) => {
        console.log(error);
    });
  }

    render() {
        let ret = (<div className="content">

            <h4>{this.state.user.username}</h4>
            <Row>
                <Col xs="12" md="6" xl="6">
                    <Label hidden={localStorage.getItem("role") !== "ROLE_SELLER"}>Company name: {this.state.user.company}</Label>
                    <br></br>
                    <Label hidden={localStorage.getItem("role") !== "ROLE_CUSTOMER"}>Name and surname: {this.state.user.name} {this.state.user.surname}</Label>
                    <br></br>
                    <Label>Email: {this.state.user.email}</Label>
                    <br></br>
                    <Label hidden={localStorage.getItem("role") === "ROLE_ADMIN"}>Address: {this.state.user.address}</Label>
                    <br></br>
                    <Label hidden={localStorage.getItem("role") === "ROLE_ADMIN"}>City: {this.state.user.city}</Label>
                    <Button hidden={!this.state.hideChangePass} style={{ width: "auto" }} size="sm" onClick={() => this.setState({ hideChangePass: false })} block color="info">
                        Change password
            </Button>
                </Col>
                <Col xs="12" md="6" xl="6">
                    <FormGroup hidden={this.state.hideChangePass}>
                    <InputGroup className="mb-3">
                            <Input type="password" placeholder="Old password"
                                value={this.state.oldPassword} onChange={event => this.setState({ oldPassword: event.target.value })}/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <Input type="password" placeholder="New password" autoComplete="new-password"
                                value={this.state.password} onChange={event => this.setState({ password: event.target.value })}
                                onBlur={this.validatePassword} />
                            <FormText color="danger">{this.state.passErrorText}</FormText>
                        </InputGroup>
                        <InputGroup className="mb-4">

                            <Input type="password" placeholder="Repeat new password" autoComplete="new-password"
                                onChange={this.checkPasswordMatching}
                            />
                            <FormText color="danger">{this.state.matchErrorText}</FormText>
                        </InputGroup>
                        <InputGroup>
                        <Button type="submit" size="sm" color="primary" onClick={(e) => this.changePass()}><i className="fa fa-dot-circle-o"></i> Change</Button>
                        <Button type="reset" size="sm" color="danger" onClick={(e) => this.setState({hideChangePass: true, password:"", oldPassword:""})}><i className="fa fa-ban"></i> Cancel</Button>
                        </InputGroup>
                    </FormGroup>
                </Col>
            </Row>
            <Row hidden={localStorage.getItem("role") !== "ROLE_SELLER"}>
            
            <Col md="4" xl="4" xs="12">
                <h4><i className="nc-icon nc-delivery-fast" style={{color:"red"}}></i> The most crossed kilometers cars</h4>
                <RenderStatistics cars={this.state.mostCrossed} typeCol="crossed"/>             
            </Col>
            <Col md="4" xl="4" xs="12">
                <h4><i className="fa fa-commenting-o" style={{color:"blue"}}></i> The most commented cars</h4>
                <RenderStatistics cars={this.state.mostCommented} typeCol="comments"/>             
            </Col>
            <Col md="4" xl="4" xs="12">
                <h4><i className="fa fa-star" style={{color:"gold"}}></i> The best-rated cars</h4>
                <RenderStatistics cars={this.state.bestRated} typeCol="rating"/>             
            </Col>
            </Row>
        </div>);

      return this.rolesMatched() ? ret : <Redirect to="/ads" />;
    }
}

export default Profile;
