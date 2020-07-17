import React, {Component} from 'react';
//import {Map, InfoWindow, Marker, GoogleApiWrapper} from 'google-maps-react';
import {YMaps, Map, GeoObject} from 'react-yandex-maps';

import axios from 'axios';


const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

export class TrackingMap extends Component{
    constructor(props) {
        super(props);

    }

    state = {
        id: 0,
        address:  "Trg Dositeja Obradovica 6",
        city:  "Novi Sad",
        state: "Serbia",
        lat: 45.2464362, 
        lng: 19.8517172,
        showingInfoWindow: false,
        activeMarker: {},
        typedMessage: ""
    };
 
    /*componentDidUpdate(prevProps, prevState){
        if(prevProps.address !== this.state.address || prevProps.city !== this.state.city  || prevProps.state !== this.state.state){
            this.setState({address: this.props.address, city:this.props.city, state: this.props.state}, () =>{
                this.handleMap();
            });
        }
    }*/

    mapa = () => {
        return (<YMaps width="100%" height="600px">
            <div width="100%" height="600px">
                Car tracking
                <Map width="100%" height="600px" state={{
                    center: [this.state.lat, this.state.lng],
                    zoom: 9
                }} >
                    <GeoObject
                        geometry={{
                            type: 'Point',
                            coordinates: [this.state.lat, this.state.lng],
                        }}
                        properties={{
                            iconContent: '',
                            hintContent: 'Car',
                        }}
                    />
                </Map>
            </div>
        </YMaps>);
    };

   testCoords = () => {
       this.setState({lat:44.980194,lng:20.223405});
   }

    componentDidMount(){
        const { id } = this.props.match.params;
        this.setState({id:id});
         this.interval = setInterval(() => {
            this.getCoords();
        },1000);

    }

    getCoords = () =>{
        let token = localStorage.getItem("ulogovan")
        let AuthStr = 'Bearer '.concat(token);
        axios({
            method: 'get',
            url: url + 'advertisement/car/track/'+this.state.id,
            headers: { "Authorization": AuthStr } ,
        }).then((response) => {
            if (response.status === 200) {
                console.log(response.data);
                this.setState({let:response.data.x,lng:response.data.y});
            } else {
                //NotificationManager.error(response.data.accessToken, 'Error!', 3000);
            }

        }, (error) => {
            //NotificationManager.error(response.data.accessToken, 'Error!', 3000);

        });
    }

    stringParser = (path) => {
        console.log('path', path)
        var words = path.split(' ')
        var ret = words[0]
        var i
        for(i=1; i<words.length; i++){
            ret += '+' + words[i]
        }
        return ret;
    }
    

    /*handleMap = () =>{
        fetch('https://api.opencagedata.com/geocode/v1/json?q='
        +this.stringParser(this.state.address) + '%2c' + this.stringParser(this.state.city)
        + '%2c' + this.state.state + '&key=9a621993023f424796cb415f13d07beb').then(response => response.json())
 
        .then(data => {
            console.log('pravi poziv', data)
            if(data.status.code !== 200){
                this.setState({
                    address: 'nije dostupna',
                    state: 'nije dostupno',
                    city: 'nije dostupno'
                })
            }else{
                this.setState({
                    lat: data.results[0].geometry.lat,
                    lng: data.results[0].geometry.lng
                })
            }
        })
    }*/



    onMarkerClick = (props, marker, e) => {
        this.setState({
            activaMarker: marker,
            showingInfoWindow: true
        });
    }
   
        /*<Map google = {this.props.google} zoom = {10} syle = {mapStyles} initialCenter ={{lat: this.state.lat , lng: this.state.lng}} >
                <Marker position = {{ lat: this.state.lat, lng:this.state.lng}}> </Marker>
                <InfoWindow
                marker = {this.state.activeMarker}
                visible = {this.state.showingInfoWindow}>
                    <div className="container">
                        <div className="section-title text-center">
                            <div>
                                <p > Drzava: {this.state.state} </p>
                                <p > Grad: {this.state.city} </p>
                                <p > Adresa: {this.state.address} </p>
                                <p > </p>
                            </div>
                        </div>
                    </div>
                </InfoWindow>
                <div className="container">
                  <div className="section-title text-center">
                      <div>

                      </div>
                  </div>
              </div>
                </Map>*/
    render() {
        const mapStyles = {
            position: 'static',
            margin: 'auto',
            width: '20%',
            height: '20%'
        };
 
        if(this.state.lat === 0 &&  this.state.lng === 0){
            return( <div>
                <h2> Mapa trenutno nije dostupna </h2>
            </div>);
        }
        //onClick = {this.onMarkerClick}
        else{
            return (
                <div  className="content">
                    <div className="animated fadeIn">
                        {this.mapa()}
                </div>
                </div>
                );
 
        }
    }
}
export default TrackingMap;
 
/*export default GoogleApiWrapper({
    apiKey: 'AIzaSyA0aqRI4uzz_2cLTkurwCYLUNKzw81FOvU',
    language: 'ENG'
})(TrackingMap);*/