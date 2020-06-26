import React, { Component } from 'react';
import axios from 'axios';
import { RoleAwareComponent } from 'react-router-role-authorization';
import {Redirect} from 'react-router-dom';
import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
import {
  Badge, Button, Card,  CardBody, CardFooter, CardHeader, Col, Form, FormGroup, FormText, Input, Label, Row, InputGroupText, InputGroupAddon, InputGroup, Pagination, PaginationItem, PaginationLink
} from 'reactstrap';

//const url = 'http://localhost:8099/';
const url = (process.env.REACT_APP_DOMAIN) + ':' + (process.env.REACT_APP_PORT) + '/';
const pricelistPerPage = 7;

class NewAd extends RoleAwareComponent {

  fileObj = [];
  fileArray = [];
  lista = [];

  constructor(props) {
    super(props);

    let arr = new Array();
    arr.push(localStorage.getItem('role'));
    console.log("KONS",arr);
    this.userRoles = arr;
    this.allowedRoles = ['ROLE_SELLER'];

    this.toggle = this.toggle.bind(this);
    this.toggleFade = this.toggleFade.bind(this);
    this.state = {
      collapse: true,
      fadeIn: true,
      timeout: 300,
      file: [null],
      marka: "",
      model: "",
      gorivo: "",
      mjenjac: "",
      klasa: "",
      brSjedista: "",
      kilometraza: "",
      maxKilometara: "",
      maxKilometraza: "",
      startDate: "",
      oglasiText: "",
      endDate: "",
      collision: false,
      noviAuto: true,
      cars: [],
      sakrij: false,
      trenutniId: "",
      prikazAuto: true,
      dugmeAuto: true,
      novoAuto: false,
      drugiZahtjev: false,
      dodaoSlike: false,
      slikeText: "",
      cjenovnikId: "",
      cjenovnici: [],
      possible: false,
      city: "",
      cityText: "",
      codebook: [],
      pricelists:[],
      showpricelist:[],
      unlimitedMileage: false,
      following: false
    };

    this.uploadMultipleFiles = this.uploadMultipleFiles.bind(this);
    this.uploadFiles = this.uploadFiles.bind(this)
    this.vratiFormu = this.vratiFormu.bind(this);
    this.vratiAuto = this.vratiAuto.bind(this);
    this.getCodebook = this.getCodebook.bind(this);
    this.getPricelist = this.getPricelist.bind(this);
  }

  componentDidMount() {
    this.getCodebook();
    //this.getPricelist();
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'car',
      headers: { "Authorization": AuthStr },  
    }).then((response) => {
      if (response.status === 200)
        this.setState({ cars: response.data, sakrij: true, trenutniId: response.data[0].id, prikazAuto: false, drugiZahtjev: true });
    }, (error) => {
      console.log(error);
    });
    axios({
      method: 'get',
      url: url + 'pricelist',
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      if (response.status === 200)
      this.setState({ pricelists: response.data, showpricelist:response.data.slice(0, pricelistPerPage) })
        this.setState({ cjenovnici: response.data, cjenovnikId: response.data[0].id });
    }, (error) => {
      console.log(error);
      this.setState({ possible: true })
    });
  }

  getPricelist = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'pricelist', 
      headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status === 200){
        this.setState({ pricelists: response.data, showpricelist:response.data.slice(0, pricelistPerPage) })
        // dodati setovanje prvog
      }
    }, (error) => {
      console.log(error);
    });
  }

  getCodebook = () => {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'codebook',
      headers: { "Authorization": AuthStr } ,       
    }).then((response) => {
      if (response.status === 200)
        this.setState({ codebook: response.data, gorivo:response.data.filter(item=>item.codeType === 'fuel')[0].name,
         mjenjac:response.data.filter(item=>item.codeType === 'gearbox')[0].name,
          marka:response.data.filter(item=>item.codeType === 'brand')[0].name,  
          model:response.data.filter(item=>item.codeType === 'model')[0].name, 
          klasa:response.data.filter(item=>item.codeType === 'class')[0].name });
        // dodati setovanje prvog
        console.log(this.state.klasa)
    }, (error) => {
      console.log(error);
    });
  }

  uploadMultipleFiles(e) {
    e.preventDefault();
    this.fileObj.push(e.target.files)
    this.setState({ dodaoSlike: true })
    for (let i = 0; i < this.fileObj[0].length; i++) {
      this.fileArray.push(URL.createObjectURL(this.fileObj[0][i]))
      console.log(this.fileObj[0])
    }
    this.setState({ file: this.fileArray })
    console.log(this.fileArray)
    console.log(this.fileArray[0])

    let files = e.target.files; //FileList object
    for (var i = 0; i < files.length; i++) {
      //this.lista.push(file);
      console.log(this.lista);
      let fajlovi = e.target.files;
      console.log(fajlovi);
      let reader = new FileReader();
      reader.readAsDataURL(fajlovi[i]);
      reader.onload = e => {
        console.log(e.target.result);
        this.lista.push(e.target.result);
      }
    }
  }

  uploadFiles(e) {
    e.preventDefault();
    console.log(this.state.file)
  }


  submit(e) {
    e.preventDefault();
    console.log(this.state.startDate)
    console.log(this.state.endDate)
    let ok = this.markaValidation(this.state.marka)
    ok = this.modelValidation(this.state.model) && ok;
    ok = this.gorivoValidation(this.state.gorivo) && ok;
    ok = this.klasaValidation(this.state.klasa) && ok;
    ok = this.sjedisteValidation(this.state.brSjedista) && ok;
    ok = this.startDateValidation(this.state.startDate) && ok;
    ok = this.endDateValidation(this.state.endDate) && ok;
    let mileage = -1;
    console.log(this.state.maxKilometraza);
    if (this.state.unlimitedMileage){
      mileage = -1
    }
    else{
      ok = this.maxKilometrazaValidation(this.state.maxKilometraza) && ok;
      mileage = this.state.maxKilometraza;
    }
    ok = this.kilometrazaValidation(this.state.kilometraza) && ok;
    ok = this.state.dodaoSlike && ok;
    ok = this.mjenjacValidation(this.state.mjenjac) && ok;
    ok = this.daLiJeDodao(this.state.dodaoSlike) && ok;
    ok = this.cityValidation(this.state.city) && ok;
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
  if (ok && this.state.dodaoSlike) {
  
        let data = {
          "carAd": {
            'make': this.state.marka,
            'model': this.state.model,
            'fuel': this.state.gorivo,
            'gearbox': this.state.mjenjac,
            'carClass': this.state.klasa,
            'insurance': this.state.collision,
            'min_price': 100,
            'max_price': 480000,
            'mileage': this.state.kilometraza,
            'mileageLimit': mileage,
            'kidsSeats': this.state.brSjedista,
            'rating': 0,
            'state': 'shared',
            'following': this.state.following,
          },
          'city': this.state.city,
          'startDate': this.state.startDate,
          'endDate': this.state.endDate,
        }
        let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
        //let reqURL =  'http://localhost:8099/advertisement/addNew';
        axios({
          method: 'post',
          url: url + 'advertisement/addNew/' + this.state.cjenovnikId,
          data: data,
          headers: { "Authorization": AuthStr }
        }).then((response) => {
          console.log(response);
          NotificationManager.success("Successfully added!", '', 3000);     
          let pom = response.data.id;
            let data = []
            for (let i = 0; i < this.fileObj[0].length; i++) {
              data.push(this.fileArray[i]);
            }
            let reqURL = url + 'advertisement/addImages/' + pom;
            axios({
              method: 'post',
              url: reqURL,
              data: this.lista,
              headers: { "Authorization": AuthStr }
            }).then((response) => {
              console.log(response);
              this.reset();
            }, (error) => {
              console.log(error);
            });
          

        }, (error) => {
          console.log(error);
        });
      }
  }

  vratiFormu(e) {
    this.setState({
      sakrij: false, prikazAuto: true, novoAuto: true, drugiZahtjev: false, file: [null],
      gorivo:this.state.codebook.filter(item=>item.codeType === 'fuel')[0].name,
      mjenjac:this.state.codebook.filter(item=>item.codeType === 'gearbox')[0].name,
     marka:this.state.codebook.filter(item=>item.codeType === 'brand')[0].name,
     model:this.state.codebook.filter(item=>item.codeType === 'model')[0].name,
     klasa:this.state.codebook.filter(item=>item.codeType === 'class')[0].name ,
      brSjedista: "",
      markaText: "",
      modelText: "",
      gorivoText: "",
      cityText: "",
      mjenjacText: "",
      klasaText: "",
      kilometraza: "",
      maxKilometara: "",
      maxKilometraza: "",
      startDate: "",
      endDate: "",
      oglasiText: "",
      collision: false,
    })
  }

  vratiAuto(e) {
    this.setState({
      sakrij: true, prikazAuto: false, dugmeAuto: true, novoAuto: true, drugiZahtjev: true, oglasiText: ""
    })
  }

  
  
  handlePageChange(pageNumber) {
    let temp = (pageNumber - 1) * pricelistPerPage;
    this.setState({ activePage: pageNumber, showpricelist: this.state.pricelists.slice(temp, temp + pricelistPerPage) })
  }

  toggle() {
    this.setState({ collapse: !this.state.collapse });
  }

  toggleFade() {
    this.setState((prevState) => { return { fadeIn: !prevState } });
  }


  reset() {
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'pricelist',
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      if (response.status === 200)
        this.setState({ cjenovnici: response.data, cjenovnikId: response.data[0].id, possible: false });
    }, (error) => {
      console.log(error);
      this.setState({ possible: true })
    });
    this.lista = [];
    this.fileArray = [];
    this.fileObj = []
    this.setState({
      file: [null],
      gorivo:this.state.codebook.filter(item=>item.codeType === 'fuel')[0].name,
      mjenjac:this.state.codebook.filter(item=>item.codeType === 'gearbox')[0].name,
     marka:this.state.codebook.filter(item=>item.codeType === 'brand')[0].name,
     model:this.state.codebook.filter(item=>item.codeType === 'model')[0].name,
     klasa:this.state.codebook.filter(item=>item.codeType === 'class')[0].name ,
      markaText: "",
      modelText: "",
      gorivoText: "",
      mjenjacText: "",
      klasaText: "",
      brSjedista: "",
      sjedisteText: "",
      kilometraza: "",
      kilometrazaText: "",
      maxKilometara: "",
      oglasiText: "",
      maxKilometraza: "",
      maxKilometrazaText: "",
      startDate: "",
      startDateText: "",
      endDate: "",
      endDateText: "",
      collision: false,
      noviAuto: true,
      cars: [],
      sakrij: false,
      trenutniId: "",
      prikazAuto: true,
      dugmeAuto: false,
      novoAuto: false,
      drugiZahtjev: false,
      dodaoSlike: false,
      slikeText: "",
      cjenovnikId: this.state.cjenovnici[0].id,
      city: "",
      unlimitedMileage:false
    })
    this.lista = [];
    token = localStorage.getItem("ulogovan")
    AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'car',
      headers: { "Authorization": AuthStr } ,
    }).then((response) => {
      if (response.status !== 200)
        this.setState({ dugmeAuto: false });
    }, (error) => {
      console.log(error);
    });
  }

  daLiJeDodao(pom) {
    if (pom !== true) {
      this.setState({ slikeText: "Obavezno dodajte fotografije" });
    }
    else {
      this.setState({ slikeText: "" });
    }
    return pom;
  }

  markaValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ marka: c });
    let sun = c;
    console.log("udje lii marka")
    if (sun === '') {
      this.setState({ markaText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ markaText: "" });
      return true;
    }
  }

  cityValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ city: c });
    if (c === '') {
      this.setState({ cityText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ cityText: "" });
      return true;
    }
  }

  autoValidation(id) {
    console.log("udje lii auto")

    this.setState({ trenutni: id, sakrij: true, novoAuto: false, drugiZahtjev: true, oglasiText: "" });
    console.log(this.state.sakrij)
    console.log({ id })
  }

  cjenovnikValidation(e) {
    this.setState({ cjenovnikId: e });
  }

  modelValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ model: c });
    console.log('udje li model')
    let sun = c;
    if (sun === '') {
      this.setState({ modelText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ modelText: "" });
      return true;
    }
  }

  gorivoValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ gorivo: c });
    let sun = c;
    if (sun === '') {
      this.setState({ gorivoText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ gorivoText: "" });
      return true;
    }
  }

  mjenjacValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    console.log("udje lii mjenjac")

    this.setState({ mjenjac: c });
    let sun = c;
    if (sun === '') {
      this.setState({ mjenjacText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ mjenjacText: "" });
      return true;
    }
  }

  klasaValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ klasa: c });
    let sun = c;
    if (sun === '') {
      this.setState({ klasaText: "Ovo polje ne moze biti prazno" });
      return false;
    } else {
      this.setState({ klasaText: "" });
      return true;
    }
  }

  sjedisteValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ brSjedista: c });
    let sun = c;
    if (sun === '') {
      this.setState({ sjedisteText: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ sjedisteText: "Broj sjedista mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ sjedisteText: "Broj sjedista mora biti pozitivan broj!" });
      return false;
    }
    else if(sun > 7){
      this.setState({ sjedisteText: "Maksimalna broj sjedista je 7!" });
      return false;
    }else {
      this.setState({ sjedisteText: "" });
      return true;
    }
  }

  kilometrazaValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ kilometraza: c });
    let sun = c;
    if (sun === '') {
      this.setState({ kilometrazaText: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ kilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ kilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
  else {
      this.setState({ kilometrazaText: "" });
      return true;
    }
  }

  maxKilometrazaValidation(c) {
    c = c.replace(/</g,'');
    c = c.replace(/>/g,'');
    c = c.replace(/script/g,'');
    c = c.replace(/alert/g,'');
    this.setState({ maxKilometraza: c });
    let sun = c;
    if (sun === '') {
      this.setState({ maxKilometrazaText: "Ovo polje ne moze biti prazno" });
      return false;
    }else if(sun === '-'){
      this.setState({ maxKilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
    else if(sun <0 ){
      this.setState({ maxKilometrazaText: "Kilometraza mora biti pozitivan broj!" });
      return false;
    }
  else {
      this.setState({ maxKilometrazaText: "" });
      return true;
    }
  }

  collisionValidation = (e) => {
    if (e.target.checked) {
      this.setState({ collision: true });
    }
    else {
      this.setState({ collision: false });
    }
    console.log(this.state.collision);
  }



  startDateValidation(ddatum) {
    console.log(ddatum);
    console.log(this.state.collision);
    this.setState({ startDate: ddatum })
    let godina;
    let mjesec;
    let dan;
    if(ddatum === "" || ddatum === undefined){
      this.setState({ startDateText: "Morate unijeti datum" });
      return false;
    }
    if (ddatum !== "" && ddatum !== undefined) {
      godina = ddatum[0] + ddatum[1] + ddatum[2] + ddatum[3]

      mjesec = ddatum[5] + ddatum[6]

      dan = ddatum[8] + ddatum[9]

      let newDate = new Date();
      let date = parseInt(newDate.getDate())
      let month = parseInt(newDate.getMonth() + 1);
      let year = parseInt(newDate.getFullYear());
      godina = parseInt(godina)
      mjesec = parseInt(mjesec)
      dan = parseInt(dan)


      if (ddatum === undefined || ddatum === '') {
        this.setState({ startDateText: "Datum je obavezno polje" })
        return false;
      }
      else if (godina < year) {
        this.setState({ startDateText: "Godina je prosla" })
        return false;
      }
      else if (mjesec < month && godina <= year) {
        this.setState({ startDateText: "Mjesec je prosao" })
        return false;
      }
      else if (dan < date && mjesec <= month && godina <= year) {
        this.setState({ startDateText: "Dan je prosao" })
        return false;
      }
      else {
        this.setState({ startDateText: "" })
        return true;
      }
    }
  };

  endDateValidation(ddatum) {
    this.setState({ endDate: ddatum })
    let start = this.state.startDate;
    let end = ddatum;
    let godina;
    let mjesec;
    let dan;
     if(ddatum === "" || ddatum === undefined){
      this.setState({ startDateText: "Morate unijeti datum" });
      return false;
    }
    if (ddatum !== "" && ddatum !== undefined) {
      godina = ddatum[0] + ddatum[1] + ddatum[2] + ddatum[3]

      mjesec = ddatum[5] + ddatum[6]

      dan = ddatum[8] + ddatum[9]

      let newDate = new Date();
      let date = parseInt(newDate.getDate())
      let month = parseInt(newDate.getMonth() + 1);
      let year = parseInt(newDate.getFullYear());
      godina = parseInt(godina)
      mjesec = parseInt(mjesec)
      dan = parseInt(dan)


      if (ddatum === undefined || ddatum === '') {
        this.setState({ endDateText: "Datum je obavezno polje" })
        return false;
      }
      else if (godina < year) {
        this.setState({ endDateText: "Godina je prosla" })
        return false;
      }
      else if (mjesec < month && godina <= year) {
        this.setState({ endDateText: "Mjesec je prosao" })
        return false;
      }
      else if (dan < date && mjesec <= month && godina <= year) {
        this.setState({ endDateText: "Dan je prosao" })
        return false;
      }
      else if (new Date(end) <= new Date(start)) {
        this.setState({ endDateText: "Datum mora biti veci od pocetnog" })
        return false;
      }

      else {
        this.setState({ endDateText: "" })
        return true;
      }
    }
  };

  toggle() {
    this.setState({ collapse: !this.state.collapse });
  }

  toggleFade() {
    this.setState((prevState) => { return { fadeIn: !prevState } });
  }

  render() {
    const len = this.state.pricelists.length;
    const pageNumbers = Array.from(Array(Math.ceil(len / pricelistPerPage)).keys());

    let ret = (<div className="animated fadeIn">

      <Row>
        <Col xs="12" md="8">
          <Card>
            <CardHeader>
              <strong>Dodavanje novog oglasa</strong>
            </CardHeader>
            <CardHeader hidden={!this.state.possible}>
              <strong>Ne postoji nijedan cjenonvik u sistemu</strong>
            </CardHeader>
            <CardBody hidden={this.state.possible}>
              <Form action="" method="post" encType="multipart/form-data" className="form-horizontal">
          
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="text-input">Marka</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="select" name="selectBrand" onChange={(event) => this.markaValidation(event.target.value)}>
                          {(this.state.codebook.filter(item => item.codeType === 'brand')).map((code) => <option key={code.name} value={code.name} > {code.name}</option>)}
                        </Input>
                        <FormText color="danger">{this.state.markaText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="email-input">Model</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="select" name="selectModel" onChange={(event) => this.modelValidation(event.target.value)}>
                          {(this.state.codebook.filter(item => item.codeType === 'model')).map((code) => <option key={code.name} value={code.name} > {code.name}</option>)}
                        </Input>                          <FormText color="danger">{this.state.modelText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Vrsta goriva</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="select" name="selectFuel" onChange={(event) => this.gorivoValidation(event.target.value)}>
                          {(this.state.codebook.filter(item => item.codeType === 'fuel')).map((code) => <option key={code.name} value={code.name} > {code.name}</option>)}
                        </Input>                          <FormText color="danger">{this.state.gorivoText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label>Tip mjenjaca</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="select" name="selectGearbox" onChange={(event) => this.mjenjacValidation(event.target.value)}>
                          {(this.state.codebook.filter(item => item.codeType === 'gearbox')).map((code) => <option key={code.name} value={code.name} > {code.name}</option>)}
                        </Input>
                        <FormText color="danger">{this.state.mjenjacText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label>Klasa automobila</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="select" name="selectClass" onChange={(event) => this.klasaValidation(event.target.value)}>
                          {(this.state.codebook.filter(item => item.codeType === 'class')).map((code) => <option key={code.name} value={code.name} > {code.name}</option>)}
                        </Input>
                        <FormText color="danger">{this.state.klasaText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label>Kids seats</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" id="date-input" value={this.state.brSjedista} onChange={(event) => this.sjedisteValidation(event.target.value)} name="date-input" placeholder="broj sjedista" />
                        <FormText color="danger">{this.state.sjedisteText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Kilometraza</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="number" value={this.state.kilometraza} id="password-input" onChange={(event) => this.kilometrazaValidation(event.target.value)} name="password-input" placeholder="predjeni kilometri" autoComplete="new-password" />
                        <FormText color="danger">{this.state.kilometrazaText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Max kilometara</Label>
                      </Col>
                        <Col md = "5">
                        <Input disabled={this.state.unlimitedMileage} type="number" id="date-input" value={this.state.maxKilometraza} onChange={(event) => this.maxKilometrazaValidation(event.target.value)} name="date-input" placeholder="max kilometara" />
                        <FormText color="danger">{this.state.maxKilometrazaText}</FormText>
                        </Col>
                        <Col md="3">
                        <Label>Unlimited</Label>
                        </Col>
                        <Col md="1">
                        <Input type="checkbox" id="unlimitedMileage" value={this.state.unlimitedMileage} onChange={(e) => this.setState({unlimitedMileage: !this.state.unlimitedMileage})}>Unlimited</Input>
                        </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row md="3">
                  <Col md="6">
                    <FormGroup row>
                      <Col md="3">
                        <Label>City</Label>
                      </Col>
                      <Col md="9">
                        <Input type="text" name="city" value={this.state.city} onChange={(event) => this.cityValidation(event.target.value)} placeholder="Pick up location" />
                        <FormText color="danger">{this.state.cityText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row md="3">
                  <strong> Period vazenja oglasa: </strong>
                </Row>
                <br>
                </br>
                <Row>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Od</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="date" id="password-input" value={this.state.startDate} onChange={(event) => this.startDateValidation(event.target.value)} name="password-input" placeholder="od" autoComplete="new-password" />
                        <FormText color="danger">{this.state.startDateText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="password-input">Do</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input type="date" id="date-input" value={this.state.endDate} onChange={(event) => this.endDateValidation(event.target.value)} name="date-input" placeholder="do" />
                        <FormText color="danger">{this.state.endDateText}</FormText>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                <Row >
                  <Col>
                    <FormGroup row>
                      <Col md="3">
                        <Label htmlFor="select">Izaberite cjenovnik</Label>
                      </Col>
                      <Col xs="12" md="9">
                        <Input disabled = {true} type="select" value={this.state.cjenovnikId}
                               onChange={(e) => {
                                 this.cjenovnikValidation(e.target.value)
                               }}>
                          {this.state.cjenovnici.map((c) => <option key={c.id} value={c.id} >Po danu:{c.priceDay} DC:{c.collisionDW} Popust na 20 dana:{c.discount20}% Popust na 30 dana:{c.discount30}%  </option>)}
                        </Input>
                      </Col>
                    </FormGroup>
                  </Col>
                </Row>
                
                <Row md="24">
                <Col>
                    <FormGroup row>
                    
                  <Col md="9"><Label>Collision Damage Waiver</Label></Col>
                  <Col md="9">
                    <FormGroup check className="checkbox">
                      <Input className="form-check-input" type="checkbox" onChange={this.collisionValidation} checked={this.state.collision} name="collision" value="collision" />
                      <Col md="3">
                        <Label check className="form-check-label" htmlFor="checkbox1">Yes </Label>
                      </Col>
                      </FormGroup>
                  </Col>
                  </FormGroup>
                  </Col>
                  <Col md="6">
                  <FormGroup row>
                  <Col md="6"><Label>Tracking device</Label></Col>
                  <Col md="9">
                    <FormGroup check className="checkbox">
                      <Input className="form-check-input" type="checkbox" onChange={e => this.setState({following: !this.state.following})} checked={this.state.following} name="collision" value="following" />
                      <Col md="3">
                        <Label check className="form-check-label" htmlFor="checkbox1">Yes</Label>
                      </Col>
                    </FormGroup>
                  </Col>
                  </FormGroup>
                  </Col>
                  </Row>
                

                <FormGroup>
                  <Row md="3">
                    <strong> Fotografije </strong>
                  </Row>
                  <br>
                  </br>
                  <div className="form-group multi-preview">
                    {(this.fileArray || []).map(url => (
                      <img src={url} alt="..." style={{ width: 100, height: 50 }} />
                    ))}
                  </div>

                  <div className="form-group">
                    <input type="file" className="form-control" accept="image/png,image/jpeg" onChange={this.uploadMultipleFiles} multiple />
                  </div>
                  <FormText color="danger">{this.state.slikeText}</FormText>

                </FormGroup>

                <FormGroup row hidden>
                  <Col md="3">
                    <Label className="custom-file" htmlFor="custom-file-input">Custom file input</Label>
                  </Col>
                  <Col xs="12" md="9">
                    <Label className="custom-file">
                      <Input className="custom-file" type="file" id="custom-file-input" name="file-input" />
                      <span className="custom-file-control"></span>
                    </Label>
                  </Col>
                </FormGroup>
              </Form>
            </CardBody>
            <CardFooter>
              <FormText color="danger">{this.state.oglasiText}</FormText>

              <Button hidden={this.state.possible} type="submit" size="sm" color="primary" onClick={(e) => this.submit(e)}><i className="fa fa-dot-circle-o"></i> Postavi</Button>
              <Button hidden={this.state.possible} type="reset" size="sm" color="danger" onClick={(e) => this.reset()}><i className="fa fa-ban"></i> Ponisti</Button>
            </CardFooter>
          </Card>
        </Col>
     
        <Col xs="12" md="12" hidden = {this.state.hideForm}>
                <Card>
                  <CardHeader>
                    <strong>Pricelists</strong>
                  </CardHeader>
                  <CardBody>
                    <div>
                      <section className="bar pt-0">
                        <div className="row">
                          <div className="col-md-12">

                            <div className="box mt-0 mb-lg-0">
                              <div className="table-responsive">
                                <table className="table table-hover">
                                  <thead>
                                  <tr>
                                    <th className="text-primary font-weight-bold">Day price</th>
                                    <th className="text-primary font-weight-bold">Collision DW</th>
                                    <th className="text-primary font-weight-bold">Discount 20 days</th>
                                    <th className="text-primary font-weight-bold">Discount 30 days</th>
                                    <th className="text-primary font-weight-bold">Exceed mileage</th>
                                  </tr>
                                  </thead>

                                  <tbody>
                                  {this.state.showpricelist.map(price => (
                                    <tr key={price.id}>
                                      <td>{price.priceDay} RSD</td>
                                      <td>{price.collisionDW} RSD</td>
                                      <td>{price.discount20} %</td>
                                      <td>{price.discount30} %</td>
                                      <td>{price.exceedMileage} RSD</td>
                                      <td><Button block className="btn-round" color="info"
                                                   onClick={(e) => this.cjenovnikValidation(price.id, e)}>Select</Button>
                                      </td>
                                    </tr>
                                  ))}

                                  </tbody>
                                </table>
                              </div>
                            </div>
                          </div>
                        </div>
                      </section>
                    </div>
                  </CardBody>
                  <CardFooter>
                    <nav>
                      <Pagination>
                        <PaginationItem><PaginationLink disabled={this.state.activePage === 1} previous tag="button"
                                                        onClick={(event) => this.handlePageChange(this.state.activePage - 1)}></PaginationLink></PaginationItem>
                        {pageNumbers.map((pageNumber) =>
                          <div>
                            <PaginationItem active hidden={this.state.activePage !== pageNumber + 1}>
                              <PaginationLink tag="button">{pageNumber + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem hidden={this.state.activePage === pageNumber + 1}>
                              <PaginationLink tag="button" onClick={(event) => this.handlePageChange(pageNumber + 1)}>
                                {pageNumber + 1}
                              </PaginationLink>
                            </PaginationItem>
                          </div>
                        )}
                        <PaginationItem><PaginationLink
                          disabled={this.state.activePage === (Math.ceil(len / pricelistPerPage))} next tag="button"
                          onClick={(event) => this.handlePageChange(this.state.activePage + 1)}></PaginationLink></PaginationItem>
                      </Pagination>
                    </nav>
                  </CardFooter>
                </Card>
              </Col>
      </Row>
      <NotificationContainer/>
    </div>);
    return this.rolesMatched() ? ret : <Redirect to="/oglasi" />;
  }
}

export default NewAd;
