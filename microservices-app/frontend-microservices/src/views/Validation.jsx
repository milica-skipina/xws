import React from "react";
import axios from "axios";
import {Card,
    CardBody,} from "reactstrap";
import {NotificationContainer, NotificationManager} from 'react-notifications';

const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';



class Validation extends React.Component{
    constructor(props) {
        super(props);
        this.state = {

        };
    }

    componentDidMount() {
        let currurl  = window.location.href.toString();
        let arr = currurl.split("validation/");
        let token = arr[1];
        if(token && token !== "") {
            let AuthStr = 'Bearer '.concat(token);
            console.log(url, "url", arr, "arr", token, "token");
            axios({
                method: 'get',
                url: url + 'authpoint/user/verify',
                headers: {"Authorization": AuthStr},
            }).then((response) => {
                if (response.status === 200) {
                    NotificationManager.info("Registration confirmed.");
                } else {
                    NotificationManager.error("Failed to validate account.");
                }

            }, (error) => {
                console.log(error);
            });
        }
    }

    render() {
        return (<div className="animated fadeIn content">
            <Card>
                <CardBody>

                </CardBody>
            </Card>
            <NotificationContainer/>
        </div>);
    }
}export default Validation;