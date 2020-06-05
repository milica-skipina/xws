/*!

=========================================================
* Paper Dashboard React - v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/paper-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)

* Licensed under MIT (https://github.com/creativetimofficial/paper-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React from "react";
import ReactDOM from "react-dom";
import { createBrowserHistory } from "history";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";

import "bootstrap/dist/css/bootstrap.css";
import "assets/scss/paper-dashboard.scss?v=1.1.0";
import "assets/demo/demo.css";
import "perfect-scrollbar/css/perfect-scrollbar.css";

//import AdminLayout from "layouts/Admin.jsx";
//import Register from "views/Register.jsx";

const Register = React.lazy(() => import('views/Register.jsx'));
const AdminLayout = React.lazy(() => import('layouts/Admin.jsx'));
const hist = createBrowserHistory();
const loading = () => <div className="animated fadeIn pt-3 text-center">Loading...</div>;
ReactDOM.render(
  /*<Router history={hist}>
    <Switch>
      <Route path="/" render={props => <AdminLayout {...props} />} />
      <Route exact path="/register" render={props => <Register {...props} />} />
      
    </Switch>
  </Router>*/
  
  <BrowserRouter>
    <React.Suspense fallback={loading()}>
      <Switch>      
        <Route exact path="/register" render={props => <Register {...props} />} />
        <Route path="/" render={props => <AdminLayout {...props} />} />
      </Switch>
    </React.Suspense>
  </BrowserRouter>,
  document.getElementById("root")
);
