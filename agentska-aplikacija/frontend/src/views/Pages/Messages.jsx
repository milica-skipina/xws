import React from "react";
import axios from "axios";
import {NotificationContainer, NotificationManager} from 'react-notifications';
import {
  Button,
  Card,
  CardBody,
  Form, FormText, Input, Label,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Modal,
  ModalBody, ModalFooter,
  ModalHeader, Row, CardHeader
} from "reactstrap";
import BootstrapTable from "react-bootstrap-table-next";
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

const columns = [
  { dataField: 'senderUsername', text: 'senderUsername' },
  { dataField: 'subject', text: 'subject' },
  { dataField: 'timeSent', text: 'timeSent' }];

class Messages extends React.Component{

  constructor(props) {
    super(props);
    this.state = {
      messages: [],
      modal: false,
      primary: false,
      subject: "",
      entrepreneurUsername: "",
      endUserUsername: "",
      message: "",
      from: "",
      rowContent: ""
    };
  }

  messageValidation(c){
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({message:c});
  }

  loadMessages = () =>{
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);

    axios({
      method: 'get',
      url: url  + 'message',
      headers: { "Authorization": AuthStr } ,
    }).then((response)=>{
      console.log(response.data);
      if (response.status === 200)  {
        for(let m of response.data)
        {
          m.active = false;
        }
        console.log(response.data);
        this.setState({messages:response.data});
      } else {
        NotificationManager.info("Failed to load message.");
      }

    },(error)=>{
      console.log(error);
    });
  };

  componentDidMount() {
    this.loadMessages();
  }

  togglePrimary = () => {
    this.setState({
      primary: !this.state.primary,
    });
  };

  reply = (entrepreneur,subject,endUser) => {
    console.log(entrepreneur,subject,endUser);
    this.setState({entrepreneurUsername:entrepreneur,subject:subject,endUserUsername:endUser});
    this.togglePrimary();
  };

  sendMessage = () => {

    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);

    let data = {
      entrepreneurUsername: this.state.entrepreneurUsername,
      endUserUsername: this.state.endUserUsername,
      subject: this.state.subject,
      text: this.state.message,
      timeSent: new Date()
    };

    axios({
      method: 'post',
      url: url  + 'message',
      headers: { "Authorization": AuthStr } ,
      data: data
    }).then((response)=>{
      if (response.status === 200)  {
        NotificationManager.info("Message sent.");
      } else {
        NotificationManager.info("Failed to send message.");
      }
      this.clear();
      this.togglePrimary();
    },(error)=>{
      console.log(error);
    });

  };

  clear = () => {
    this.setState({message:"",to:"",subject:"",from: ""});
  };

  /*
  *<table className="table">
          <tbody>
          {messages}
          </tbody>
        </table>
              * <BootstrapTable
                      keyField='id'
                      data={ this.state.messages }
                      columns={ columns }
                  />
  *
  * */

  showRow = (e,id) => {
    console.log(id);
    let mess = this.state.messages;
    for(let m of mess)
    {
      if(m.id === id) {
        if(m.active === false)
          m.active = true;
        else
          m.active = false;
      }
    }
    this.setState({messages:mess});
  };

  render() {
    let messages = this.state.messages.map(message => (
      <React.Fragment  key={message.id}>
        <tr onClick={(e) => this.showRow(e,message.id)} className="clickable" style={{backgroundColor:"LightGray"}} data-toggle="collapse" data-target={"#"+ message.id} className="accordion-toggle" >
          { localStorage.getItem("role") === "ROLE_CUSTOMER" && <td>{message.entrepreneurUsername}</td>}
          { localStorage.getItem("role") === "ROLE_SELLER" && <td>{message.endUserUsername}</td>}
          <td>{message.subject}</td>
          <td>{(new Date(message.timeSent)).toLocaleDateString()+ " "}{(new Date(message.timeSent)).toLocaleTimeString()}</td>
          <td><button onClick={(e) => {this.reply(message.entrepreneurUsername,message.subject,message.endUserUsername)}} className="btn btn-primary">Reply</button></td>
        </tr>
        { message.active === true && <tr>
          <td style={{border:"none"}} className="no-border">{message.text}</td>
        </tr>}
      </React.Fragment>
    ));

    return (<div className="animated fadeIn content"><Card>
      <CardHeader>
        <i className="fa fa-align-justify"></i> Messages
      </CardHeader>

      <CardBody className="p-0">
        <table className="table p-0 m-0">
          <tbody className="p-0 m-0">
          {messages}
          </tbody>
        </table>
      </CardBody>
    </Card>
      <Modal isOpen={this.state.primary} toggle={this.togglePrimary}
             className={'modal-primary ' + this.props.className}>
        <ModalHeader>Send message</ModalHeader>
        <ModalBody>
          <Form>
            <InputGroup className="mb-3">
              <InputGroupAddon>
                <InputGroupText>
                  Subject:
                </InputGroupText>
              </InputGroupAddon>
              <Input type="text"  value={this.state.subject}
                     disabled={true}/>
            </InputGroup>
            <InputGroup className="mb-3">
              <InputGroupText>
                Message:
              </InputGroupText>
              <Input type="textarea" onChange={(event) => this.messageValidation(event.target.value)} value={this.state.message}
              />
            </InputGroup>
          </Form>
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onClick={this.sendMessage} >Send</Button>{' '}
          <Button color="secondary" onClick={this.togglePrimary}>Cancel</Button>
        </ModalFooter>
      </Modal>
      <NotificationContainer/>
    </div>);
  }

}
export default Messages;
