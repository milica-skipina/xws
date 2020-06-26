import React from "react";
import axios from "axios";
import { RoleAwareComponent } from 'react-router-role-authorization';
import {Redirect} from 'react-router-dom';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import {
    Button,
    Card,
    CardBody,
    Form, FormText, Input,Label,
    InputGroup,
    InputGroupAddon,
    InputGroupText,
    Modal,
    ModalBody, ModalFooter,
    ModalHeader, Row
} from "reactstrap";
import BootstrapTable from "react-bootstrap-table-next";
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

const columns = [
    { dataField: 'senderUsername', text: 'senderUsername' },
    { dataField: 'subject', text: 'subject' },
    { dataField: 'timeSent', text: 'timeSent' }];

class Messages extends RoleAwareComponent {

    constructor(props) {
        super(props);
        this.state = {
            messages: [],
            modal: false,
            primary: false,
            subject: "",
            to: "",
            message: "",
            from: ""
        };

    let arr = new Array();
    arr.push(localStorage.getItem('role'));    
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_SELLER', 'ROLE_CUSTOMER'];

    }

    loadMessages = () =>{
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);

        axios({
            method: 'get',
            url: url  + 'message/message',
            headers: { "Authorization": AuthStr } ,
        }).then((response)=>{
            if (response.status === 200)  {
                for(let m of response.data)
                {
                    m.active = false;
                }
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
        if(this.state.primary === true)
            this.clear();

        this.setState({
            primary: !this.state.primary,
        });
    };

    reply = (to,subject,from) => {
        console.log(to,subject,from);
        this.setState({to:to,subject:subject,from:from});
        this.togglePrimary();
    };

    
  messageValidation(c){
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({message:c});
  }

    sendMessage = () => {

        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);

        let data = {
            receiverUsername: this.state.to,
            senderUsername: this.state.from,
            subject: this.state.subject,
            text: this.state.message,
            timeSent: new Date()
        };

        axios({
            method: 'post',
            url: url  + 'message/message',
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
                <tr  onClick={(e) => this.showRow(e,message.id)} >
                    <td>{message.senderUsername}</td>
                    <td>{message.subject}</td>
                    <td>{(new Date(message.timeSent)).toLocaleDateString()+ " "}{(new Date(message.timeSent)).toLocaleTimeString()}</td>
                    <td><button onClick={(e) => {this.reply(message.senderUsername,message.subject,message.receiverUsername)}} className="btn btn-primary">Reply</button></td>
                </tr>
                { message.active === true && <tr>
                    <td style={{border:"none"}} className="no-border">{message.text}</td>
                </tr>}
            </React.Fragment>
        ));



        let ret = (<div className="animated fadeIn content"><Card>
            <CardBody className="p-0">
                <table className="table">
                    <tbody>
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
                                    To:
                                </InputGroupText>
                            </InputGroupAddon>
                            <Input type="text"  value={this.state.to}
                                   disabled={true} autoComplete="username"/>
                        </InputGroup>
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

    return this.rolesMatched() ? ret : <Redirect to="/ads" />;
    }

}
export default Messages;
