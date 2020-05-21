import React, { Component } from 'react';
//import { Card, CardBody, CardHeader, Col, Row, Table, Button } from 'reactstrap';
//import { MDBCard, MDBCardTitle, MDBBtn, MDBCardGroup, MDBCardImage, MDBCardText, MDBCardBody } from "mdbreact";

import axios from 'axios';
import { Carousel, Card, CardHeader, CardBody, CarouselIndicators, CarouselControl, Col, CarouselCaption, CarouselItem, Row } from 'reactstrap';
import { MDBCard, MDBCardTitle, MDBBtn, MDBCardGroup, MDBCardImage, MDBCardText, MDBCardBody } from "mdbreact";

//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';

class Oglas extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeIndex: 0,
      oglas: {
        carAd: {
          images: []
        }
      },
    };
    this.next = this.next.bind(this);
    this.previous = this.previous.bind(this);
    this.goToIndex = this.goToIndex.bind(this);
    this.onExiting = this.onExiting.bind(this);
    this.onExited = this.onExited.bind(this);
    this.getDateString = this.getDateString.bind(this);

  }

  componentDidMount() {
    const { id } = this.props.match.params;
    //console.log(id)
    axios({
      method: 'get',
      url: url + 'advertisement/getOneAd/' + id,
    }).then((response) => {
      console.log(response);
      this.setState({ oglas: response.data })
    }, (error) => {
      console.log(error);
    });
  }

  onExiting() {
    this.animating = true;
  }

  onExited() {
    this.animating = false;
  }

  next() {
    if (this.animating) return;
    const nextIndex = this.state.activeIndex === this.state.oglas.carAd.images.length - 1 ? 0 : this.state.activeIndex + 1;
    this.setState({ activeIndex: nextIndex });
  }

  previous() {
    if (this.animating) return;
    const nextIndex = this.state.activeIndex === 0 ? this.state.oglas.carAd.images.length - 1 : this.state.activeIndex - 1;
    this.setState({ activeIndex: nextIndex });
  }

  goToIndex(newIndex) {
    if (this.animating) return;
    this.setState({ activeIndex: newIndex });
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

  render() {
    const { activeIndex } = this.state;

    const slides = this.state.oglas.carAd.images.map((item) => {
      return (
        <CarouselItem
          onExiting={this.onExiting}
          onExited={this.onExited}
          key={item.imageUrl}
        >
          <img style={{ maxWidth: "38rem", maxHeight:"35rem"}} className="d-block w-100" src={item.imageUrl}/>
          <CarouselCaption/>
        </CarouselItem>
      );
    });
    return (
      <div>
        <Row>
        <Col xs="12" sm="6" md="4">
        <Card style={{ width: "18rem", textAlign: "left", alignItems: "center", margin: 9 }} key={this.state.oglas.id} data-key={this.state.oglas.id}>
                    <CardBody>
                      <div>
                        <h4 style={{ textAlign: "center" }} className="text-primary font-weight-bold">{this.state.oglas.carAd.model} {this.state.oglas.carAd.make}</h4>
                        <Row>
                          <Col>
                            <MDBCardTitle tag="h6"> Car class: <p style={{ color: "red" }}>{this.state.oglas.carAd.carClass}</p>  </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Mileage limit: <p style={{ color: "red" }}>{this.state.oglas.carAd.mileageLimit}</p>  </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Gearbox type: <p style={{ color: "red" }}>{this.state.oglas.carAd.gearbox}</p>  </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Price: <p style={{ color: "red" }}>{this.state.oglas.carAd.price}</p>  </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Mileage: <p style={{ color: "red" }}>{this.state.oglas.carAd.mileage}</p>  </MDBCardTitle>
                          </Col>
                          <Col>
                            <MDBCardTitle tag="h6"> Kids seats: <p style={{ color: "red" }}>{this.state.oglas.carAd.kidsSeats}</p>  </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Fuel type: <p style={{ color: "red" }}>{this.state.oglas.carAd.fuel}</p> </MDBCardTitle>
                            <MDBCardTitle tag="h6"> Pick up location:<p style={{ color: "red" }}> {this.state.oglas.city} </p></MDBCardTitle>
                           <MDBCardTitle tag="h6"> Start date: <p style={{ color: "red" }}>{this.getDateString(this.state.oglas.startDate)}  </p> </MDBCardTitle>
                           <MDBCardTitle tag="h6"> End date: <p style={{ color: "red" }}>{this.getDateString(this.state.oglas.endDate)}   </p></MDBCardTitle>
                            <MDBCardTitle tag="h6"> Collision Damage Waiver: <i style={{ color: "red" }} hidden={!this.state.oglas.carAd.insurance} className="fa fa-check" /><i style={{ color: "red" }} hidden={this.state.oglas.carAd.insurance} className="fa fa-close" />  </MDBCardTitle>
                          </Col>
                        </Row>
                      </div>
                    </CardBody>
                  </Card>
          </Col>
        <Col xs="12" md = "6" xl="6">
          <Card style={{ maxWidth: "40rem", maxHeight:"40rem"}}>
            <CardHeader>
              <i className="fa fa-image fa-lg"></i><strong>Images</strong>
            </CardHeader>
            <CardBody>
              <Carousel activeIndex={activeIndex} next={this.next} previous={this.previous}>
                <CarouselIndicators items={this.state.oglas.carAd.images} activeIndex={activeIndex} onClickHandler={this.goToIndex} />
                {slides}
                <CarouselControl direction="prev" directionText="Previous" onClickHandler={this.previous} />
                <CarouselControl direction="next" directionText="Next" onClickHandler={this.next} />
              </Carousel>
            </CardBody>
          </Card>
        </Col>
        </Row>
      </div>

    );
  }
}

export default Oglas;