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
import Dashboard from "views/Dashboard.jsx";
import Notifications from "views/Notifications.jsx";
import Icons from "views/Icons.jsx";
import Typography from "views/Typography.jsx";
import TableList from "views/Tables.jsx";
import Maps from "views/Map.jsx";
import UserPage from "views/User.jsx";
import UpgradeToPro from "views/Upgrade.jsx";
import Codebook from "views/Codebook.jsx";
import Pricelist from "views/Pricelist.jsx";
import Ad from "views/Ad.jsx";
import Ads from "views/Ads.jsx";
import ShowAd from "views/ShowAd.jsx";
import Register from "views/Register.jsx";
import Basket from "views/Basket.jsx";
import Requests from "views/Requests.jsx";

var routes = [

    {
        path: "/codebook",
        name: "Codebook",
        icon: "nc-icon nc-bank",
        component: Codebook,
        layout: "/admin"
    },
    {
        path: "/pricelist",
        name: "Pricelist",
        icon: "nc-icon nc-tile-56",
        component: Pricelist,
        layout: "/admin"
    },


    {
        path: "/dashboard",
        name: "Dashboard",
        icon: "nc-icon nc-bank",
        component: Dashboard,

    },




];
export default routes;
