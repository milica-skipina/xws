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
import UserPage from "views/User.jsx";
import Codebook from "views/Codebook.jsx";
import Pricelist from "views/Pricelist.jsx";
import Ad from "views/Ad.jsx";
import Ads from "views/Ads.jsx";
import ShowAd from "views/ShowAd.jsx";
import Basket from "views/Basket.jsx";
import Requests from "views/Requests.jsx";
import CommentRequests from "views/CommentRequests";
import Profile from "views/Profile";
import Orders from "views/Orders.jsx";
import Messages from "views/Messages";

var routes = [

  {
    authorize: ["ROLE_SELLER", "ROLE_ADMIN"],
    path: "/codebook",
    name: "Codebook",
    icon: "nc-icon nc-bank",
    component: Codebook,
    layout: "/admin"
  },
  {
    authorize: ["ROLE_SELLER", 'ROLE_ADMIN', 'ROLE_CUSTOMER'] ,
    path: "/profile",
    name: "Profile",
    icon: "fa fa-user-circle",
    component: Profile,
  },
  {
    authorize: ["ROLE_SELLER", 'ROLE_ADMIN'] ,
    path: "/pricelist",
    name: "Pricelist",
    icon: "nc-icon nc-tile-56",
    component: Pricelist,
    layout: "/admin"
  },
  {
    authorize: ["fsg"] ,
    path: "/ads",
    name: "Ads",
    icon: "nc-icon nc-caps-small",
    component: Ads,
    layout: "/admin"
  },
  {
    authorize: ["fsg"] ,
    attributes: { hidden: true },
    path: "/showad/:id",
    name: "Ad Details",
    icon: "nc-icon nc-caps-small",
    component: ShowAd,
    layout: "/admin"
  },
  {
    authorize: ["ROLE_SELLER", 'ROLE_CUSTOMER'] ,
    path: "/newad",
    name: "New Ad",
    icon: "nc-icon nc-caps-small",
    component: Ad,
    layout: "/admin"
  },

  {
    authorize: ['ROLE_ADMIN'] ,
    path: "/dashboard",
    name: "Dashboard",
    icon: "nc-icon nc-bank",
    component: Dashboard,
    layout: "/admin"
  },
  {
    authorize: ["fsg"] ,
    path: "/icons",
    name: "Icons",
    icon: "nc-icon nc-diamond",
    component: Icons,
    layout: "/admin"
  },
  {
    authorize: ["ROLE_SELLER", 'ROLE_CUSTOMER'] ,
    path: '/ad',
    name: "Advertisement",
    icon: "nc-icon nc-tile-56",
    component: UserPage,

  },
  {
    authorize: ['ROLE_CUSTOMER'] ,
    path: '/basket',
    name: "Your basket",
    component: Basket,

  },
  {
    authorize: ["ROLE_SELLER", 'ROLE_CUSTOMER'] ,
    path: '/requests',
    name: "Requests",
    component: Requests,

  },
  {
    authorize: ["fsg"] ,
    path: '/comments',
    name: "Comments",
    component: CommentRequests,
  },
  {
    authorize: ['ROLE_CUSTOMER'] ,
    path: "/orders",
    name: "Orders",    
    component: Orders,    
},
  {
    authorize: ['fgs'] ,
    path: "/messages",
    name: "Messages",
    component: Messages,
  },
];
export default routes;
