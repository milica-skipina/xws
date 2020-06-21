import React from 'react';
import { RoleAwareComponent } from 'react-router-role-authorization';
import axios from 'axios';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import { Redirect } from 'react-router-dom';

import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Label,
  CardFooter
} from 'reactstrap';

//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

class Comments extends RoleAwareComponent {
 
    constructor(props) {
        super(props);
        this.state = {          
          reviews: [],          
        };

        let arr = [];
        arr.push(localStorage.getItem('role'));
        console.log("KONS", arr);
        this.userRoles = arr;
        this.allowedRoles = ['ROLE_ADMIN'];
        
        this.getReviews = this.getReviews.bind(this);  
        this.sendRequest = this.sendRequest.bind(this);
      }
    
      componentDidMount() {
        this.getReviews();
      }
    
      getReviews = () => {
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);    
        axios({
            method: 'get',
            url: url + 'review',
            headers: { "Authorization": AuthStr } ,            
          }).then((response) => {
            let temp = response.data;
            temp.sort(function(a,b){return b.date-a.date})
            this.setState({ reviews: temp })
          }, (error) => {
            console.log(error);
          });
      }
    
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
    
      sendRequest(approved, id){
        let state = "APPROVED";
        if(!approved){
            state = "REJECTED";
        }
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);    
        axios({
            method: 'put',
            url: url + 'review/' + id,
            headers: { "Authorization": AuthStr } ,     
            data:{
                state: state,
            }       
          }).then((response) => {
            if(approved){
                NotificationManager.success("Successfully approved", '', 3000);
            }
            else{
                NotificationManager.success("Successfully rejected!", '', 3000);
            }
            this.getReviews();
          }, (error) => {
            console.log(error);
          });
      }

  render() {

    let ret = (
        <div className="content">

        <section className="bar pt-0" hidden={this.state.hideAll}>
              <div className="row">
                {this.state.reviews.map(review => (
                  <Card style={{ width: "18rem", textAlign: "left", alignItems: "left", margin: 9 }} key={review.id} data-key={review.id}>
                <CardHeader>
                  <h5><strong>{review.username}</strong></h5>
                </CardHeader>
                <CardBody >
                    {this.getDateString(review.date)}
                  <br></br>
                <Label style={{color:"black", fontSize:"12", marginTop:"0.5rem"}} >{review.text}</Label>
                <br></br>                
                </CardBody>
                <CardFooter>
                <Button color="primary" onClick={() => this.sendRequest(true, review.id)}>Approve</Button>{' '}
                <Button color="danger" onClick={() => this.sendRequest(false, review.id)}>Reject</Button>
                </CardFooter>
                  </Card>
                ))}
              </div>
            </section>
         <NotificationContainer/>
    </div>
    );
    return this.rolesMatched() ? ret : <Redirect to="/oglasi" />;
  }
}

export default Comments;
