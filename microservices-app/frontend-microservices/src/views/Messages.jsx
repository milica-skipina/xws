import React from "react";
import axios from "axios";
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

class Messages extends React.Component{

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

/*
*
            * <BootstrapTable
                    keyField='id'
                    data={ this.state.messages }
                    columns={ columns }
                />
*
* */

    render() {
        let messages = this.state.messages.map(message => (
            <React.Fragment  key={message.id}>
                <tr className="clickable"  data-toggle="collapse" data-target={message.id} >
                    <td>{message.senderUsername}</td>
                    <td>{message.subject}</td>
                    <td>{(new Date(message.timeSent)).toLocaleDateString()+ " "}{(new Date(message.timeSent)).toLocaleTimeString()}</td>
                    <td><button onClick={(e) => {this.reply(message.senderUsername,message.subject,message.receiverUsername)}} className="btn btn-primary">Reply</button></td>
                </tr>
                <tr >
                    <td>{message.text}</td>
                </tr>
            </React.Fragment>
        ));



        return (<div className="animated fadeIn content"><Card>
            <CardBody>
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
    }

}
export default Messages;
